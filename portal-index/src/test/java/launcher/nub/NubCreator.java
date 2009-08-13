/**
 * 
 */
package launcher.nub;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dao.DataResourceDAO;
import org.gbif.portal.harvest.taxonomy.TaxonomyUtils;
import org.gbif.portal.model.DataResource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Launcher for creating the nub taxonomy tree.
 *  
 * @author tim
 */
public class NubCreator {
	/**
	 * The commons logger
	 */
	protected Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * The utils
	 */
	protected TaxonomyUtils taxonomyUtils;
	
	/**
	 * The DataResourceDAO
	 */
	protected DataResourceDAO dataResourceDAO;
	
	/**
	 * The spring context 
	 */
	protected ApplicationContext context;
	
	/**
	 * Does the merging of the taxonomy by paging over each resources'
	 */
	protected void launch(List<Long> resourceIds) throws Exception {
		for (long id : resourceIds) {
			long time = System.currentTimeMillis();
			logger.info("Starting importing resource[" + id +"]");
			DataResource resource = dataResourceDAO.getById(id);
			if(resource==null){
				throw new Exception("No resource for id:"+id);
			}
			
			// Only allow our highest taxonomic authorities to create kingdoms
			boolean canCreateKingdoms = (resource.getTaxonomicPriority() <= 10);
			taxonomyUtils.importTaxonomyFromDataResource(id, 1, 1, canCreateKingdoms, false, false);
			logger.info("Finished importing resource[" + id +"] in " + ((1+System.currentTimeMillis()-time)/1000) + " secs");
		}
		/*
		THIS TAKES A WHILE MATE
		long time = System.currentTimeMillis();
		logger.info("Denormalising taxonomy of provider[1]");
		taxonomyUtils.denormalisedTaxonomyForProvider(1);
		logger.info("Finished denormalising taxonomy from provider[1] in " + ((1+System.currentTimeMillis()-time)/1000) + " secs");
		*/
	}
	
	/**
	 * Hidden constructor forcing the setting of the required values
	 * @param nubProviderId For the NUB taxonomy
	 * @param nubResourceId For the NUB taxonomy
	 * @param resources That are to be merged into the Nub taxonomy
	 */
	protected NubCreator() {
		String[] locations = {"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"
		};
		context = new ClassPathXmlApplicationContext(locations);
		taxonomyUtils = (TaxonomyUtils) context.getBean("taxonomyUtils");
		dataResourceDAO = (DataResourceDAO) context.getBean("dataResourceDAO");
	}
	
	/**
	 * @param args Usage: [<resource-id-to-merge-in]+
	 * E.g. NubTaxonomyCreator 10 12 14 15
	 */
	public static void main(String[] args) {
		if(args.length<1){
			System.out.println("Usage: [<resource-id-to-merge-in]+");
		} else {
			try {
				List<Long> ids = new LinkedList<Long>();
				for (int i=0; i<args.length; i++) {
					ids.add(Long.parseLong(args[i]));
				}
				NubCreator launcher = new NubCreator();
				launcher.launch(ids);
			} catch (Exception e) {
				System.out.println("Usage: [<resource-id-to-merge-in]+");
				e.printStackTrace();
			}
		}
		System.exit(1);
	}
}
