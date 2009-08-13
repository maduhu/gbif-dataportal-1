/**
 * 
 */
package launcher;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.util.workflow.SequenceProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Launcher for indexing tests
 * @author tim
 */
public class ExtractTaxonomyLauncher {
	protected Log logger = LogFactory.getLog(ExtractTaxonomyLauncher.class);
	private ApplicationContext context;
	
	private void init() {
		String[] locations = {"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"};
		context = new ClassPathXmlApplicationContext(locations);
	}
	
	private void index(long id) {
		SequenceProcessor workflow = (SequenceProcessor)context.getBean("GBIF:INDEX:1.0:extractTaxonomy");
		Map<String, Object> seed = new HashMap<String, Object>();
		seed.put("resourceAccessPointId", new Long(id));
		try {
			
			workflow.doActivities(seed);
			logger.info("Finished...");
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage(), e);
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExtractTaxonomyLauncher launcher = new ExtractTaxonomyLauncher();		
		launcher.init();
		if(args.length>=1){
			try {
				for (int i=0; i<args.length; i++) {
					long resourceAccessPointId = Long.parseLong(args[i]);
					System.out.println("Starting ExtractTaxonomyLauncher for rapId: " + resourceAccessPointId);
					long time = System.currentTimeMillis();
					launcher.index(resourceAccessPointId);
					System.out.println("Finished ExtractTaxonomyLauncher  for rapId [" + resourceAccessPointId +"] in " + ((System.currentTimeMillis()-time)/1000) + " secs");
				}
			} catch(NumberFormatException e){
				printUsage();
				return;
			}
		} else {	
			printUsage();
			return;
		}
		System.exit(1);
	}
	
	public static void printUsage(){
		System.out.println(ExtractTaxonomyLauncher.class.getName()+" <resource-access-point-id+>");
		System.out.println(" e.g. "+ExtractTaxonomyLauncher.class.getName()+" 99 12 14");		
	}
}
