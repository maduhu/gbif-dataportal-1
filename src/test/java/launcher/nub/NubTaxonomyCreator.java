/**
 * 
 */
package launcher.nub;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dao.TaxonConceptDAO;
import org.gbif.portal.harvest.taxonomy.TaxonomyUtils;
import org.gbif.portal.model.TaxonConceptLite;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Launcher for creating the nub taxonomy tree.
 * 
 * This will take an ordered list of IDs representing Data Resource IDs and merge them into the
 * nub taxonomy tree.
 *  
 * @author tim
 * @deprecated Use Nub creator...
 */
public class NubTaxonomyCreator {
	/**
	 * The commons logger
	 */
	protected Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * The provider that is the nub taxonomy
	 */
	protected long nubProviderId;
	
	/**
	 * The resource within the nub provider that is termed the nub taxonomy
	 */
	protected long nubResourceId;
	
	/**
	 * The ids of the resources to merge into the nub taxonomy
	 */
	protected List<Long> resourceIds;
	
	/**
	 * The DAO for dealing with the taxon concepts
	 */
	protected TaxonConceptDAO taxonConceptDAO;
	
	/**
	 * The spring context 
	 */
	protected ApplicationContext context;
	
	/**
	 * The utilities for merging the classifications
	 */
	protected TaxonomyUtils taxonomyUtils;

	/**
	 * Does the merging of the taxonomy by paging over each resources' species or lower concepts 1000 at time
	 */
	protected void launch() {
		for (long id : resourceIds) {
			logger.info("Starting resource id: " + id);
			boolean hasMore = true;
			long minId = 0;
			while (hasMore) {
				List<List<TaxonConceptLite>> classifications = taxonConceptDAO.getClassificationsOf(7000, id, true, minId, 1000);
				logger.info("Received " + classifications.size() + " results to merge");
				
				// see if there needs to be another page received
				if (classifications.size()==1000) {
					List<TaxonConceptLite> classification = classifications.get(999);
					TaxonConceptLite lastConcept =  classification.get(classification.size()-1);
					minId = lastConcept.getId();
					hasMore=true;
				} else {
					hasMore = false;
				}
				
				for (List<TaxonConceptLite> classification : classifications) {
					taxonomyUtils.synchroniseAtLowestJoinPoint(classification, nubProviderId, nubResourceId, true);
				}				
			}
			logger.info("Finished resource id: " + id);
		}
	}
	
	/**
	 * Hidden constructor forcing the setting of the required values
	 * @param nubProviderId For the NUB taxonomy
	 * @param nubResourceId For the NUB taxonomy
	 * @param resources That are to be merged into the Nub taxonomy
	 */
	protected NubTaxonomyCreator(long nubProviderId, long nubResourceId, List<Long> resources) {
		this.nubProviderId = nubProviderId;
		this.nubResourceId = nubResourceId;
		this.resourceIds = resources;
		StringBuffer sb = new StringBuffer();
		sb.append("Merging resources [ ");
		for (Long id : resources) {
			sb.append(id + " ");
		}
		sb.append("] into nub provider [ ");
		sb.append(nubProviderId + "");
		sb.append(" ] nub resource [ ");
		sb.append(nubResourceId + " ]");
		
		String[] locations = {"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"
		};
		context = new ClassPathXmlApplicationContext(locations);
		taxonConceptDAO = (TaxonConceptDAO) context.getBean("taxonConceptDAO");
		taxonomyUtils = (TaxonomyUtils)context.getBean("taxonomyUtils");
		
		logger.info(sb.toString());
	}
	
	/**
	 * @param args Usage: <nub-provider-id> <nub-resource-id> [<resource-id-to-merge-in]+
	 * E.g. NubTaxonomyCreator 1 1 10 12 14 15
	 */
	public static void main(String[] args) {
		if(args.length<3){
			System.out.println("Usage: <nub-provider-id> <nub-resource-id> [<resource-id-to-merge-in]+");
		} else {
			try {
				Long nubProviderId = Long.parseLong(args[0]);
				Long nubResourceId = Long.parseLong(args[1]);
				List<Long> ids = new LinkedList<Long>();
				for (int i=2; i<args.length; i++) {
					ids.add(Long.parseLong(args[i]));
				}
				NubTaxonomyCreator launcher = new NubTaxonomyCreator(nubProviderId, nubResourceId, ids);
				launcher.launch();
			} catch (NumberFormatException e) {
				System.out.println("Usage: <nub-provider-id> <nub-resource-id> [<resource-id-to-merge-in]+");
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return the taxonConceptDAO
	 */
	public TaxonConceptDAO getTaxonConceptDAO() {
		return taxonConceptDAO;
	}

	/**
	 * @param taxonConceptDAO the taxonConceptDAO to set
	 */
	public void setTaxonConceptDAO(TaxonConceptDAO taxonConceptDAO) {
		this.taxonConceptDAO = taxonConceptDAO;
	}

	/**
	 * @return the context
	 */
	public ApplicationContext getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	/**
	 * @return Returns the taxonomyUtils.
	 */
	public TaxonomyUtils getTaxonomyUtils() {
		return taxonomyUtils;
	}

	/**
	 * @param taxonomyUtils The taxonomyUtils to set.
	 */
	public void setTaxonomyUtils(TaxonomyUtils taxonomyUtils) {
		this.taxonomyUtils = taxonomyUtils;
	}
}
