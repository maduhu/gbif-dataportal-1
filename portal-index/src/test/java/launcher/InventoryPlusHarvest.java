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
public class InventoryPlusHarvest {
	protected static Log logger = LogFactory.getLog(InventoryPlusHarvest.class);
	private ApplicationContext context;
	
	private void init() {
		String[] locations = {
				"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"
		};
		context = new ClassPathXmlApplicationContext(locations);
	}
	
	private void inventory(long resourceAccessPointId) {
		SequenceProcessor workflow = (SequenceProcessor)context.getBean("GBIF:INDEX:1.0:inventoryOfNamesAtResourceAccessPoint");
		Map<String, Object> seed = new HashMap<String, Object>();
		seed.put("resourceAccessPointId", resourceAccessPointId);
		seed.put("dateLastModified", "2007-01-01");
		try {
			workflow.doActivities(seed);
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}		
	}

	private void harvest(long resourceAccessPointId) {
		SequenceProcessor workflow = (SequenceProcessor)context.getBean("GBIF:INDEX:1.0:harvestResourceAccessPoint");
		Map<String, Object> seed = new HashMap<String, Object>();
		seed.put("resourceAccessPointId", resourceAccessPointId);
		seed.put("dateLastModified", "2007-01-01");
		try {
			workflow.doActivities(seed);
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InventoryPlusHarvest launcher = new InventoryPlusHarvest();
		launcher.init();
		if(args.length>=1){
			try {
				for (int i=0; i<args.length; i++) {
					long resourceAccessPointId = Long.parseLong(args[i]);
					System.out.println("Starting Inventory for rapId: " + resourceAccessPointId);
					launcher.inventory(resourceAccessPointId);
					System.out.println("Finished Inventory  for rapId: " + resourceAccessPointId);
				}
			} catch(NumberFormatException e){
				printUsage();
				return;
			}
			try {
				for (int i=0; i<args.length; i++) {
					long resourceAccessPointId = Long.parseLong(args[i]);
					System.out.println("Starting Harvest for rapId: " + resourceAccessPointId);
					launcher.harvest(resourceAccessPointId);
					System.out.println("Finished Harvest  for rapId: " + resourceAccessPointId);
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
		System.out.println(InventoryWorkflowLauncher.class.getName()+" <resource-access-point-id>+");
		System.out.println(" e.g. "+InventoryWorkflowLauncher.class.getName()+" 99 12 14 78");		
	}
}
