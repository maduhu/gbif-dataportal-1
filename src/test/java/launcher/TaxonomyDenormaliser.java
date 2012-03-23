/**
 * 
 */
package launcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.harvest.taxonomy.TaxonomyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Denormalises the taxonomy for a provider or resource
 * @author tim
 */
public class TaxonomyDenormaliser {
	protected static Log logger = LogFactory.getLog(TaxonomyDenormaliser.class);
	private ApplicationContext context;
	private TaxonomyUtils taxonomyUtils;
	
	private void init() {
		String[] locations = {"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"
		};
		context = new ClassPathXmlApplicationContext(locations);
		taxonomyUtils = (TaxonomyUtils) context.getBean("taxonomyUtils");
	}
	
	/**
	 * @param isProvider true if provider, false if resource
	 * @param id 
	 */
	private void launch(boolean isProvider, int id) {
		if (isProvider) {
			logger.info("Starting taxonomy denormalisation of provider " + id);
			taxonomyUtils.denormalisedTaxonomyForProvider(id);
			logger.info("Finished taxonomy denormalisation of provider " + id);
		} else {
			logger.info("Starting taxonomy denormalisation of resource " + id);
			taxonomyUtils.denormalisedTaxonomyForResource(id);
			logger.info("Finished taxonomy denormalisation of resource " + id);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length!=2){
			System.out.println("Usage: <TYPE [resource or provider] > <id>");
			System.out.println("eg: TaxonomyDenormaliser resource 203");
			return;
		}
		try {
			TaxonomyDenormaliser launcher = new TaxonomyDenormaliser();
			launcher.init();
			if (args[0].equals("resource"))
				launcher.launch(false, Integer.parseInt(args[1]));
			else if  (args[0].equals("provider"))
				launcher.launch(true, Integer.parseInt(args[1]));
			else {
				System.out.println("Usage: <TYPE [resource or provider] > <id>");
				System.out.println("eg: TaxonomyDenormaliser resource 203");
			}
		} catch (NumberFormatException e){
			logger.error(e);
			System.out.println("Usage: <TYPE [resource or provider] > <id>");
			System.out.println("eg: TaxonomyDenormaliser resource 203");
		}
		System.exit(0);
	}
}
