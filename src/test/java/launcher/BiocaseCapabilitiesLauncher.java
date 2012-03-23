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
 * 
 * @author tim
 */
public class BiocaseCapabilitiesLauncher {
	protected static Log logger = LogFactory.getLog(BiocaseCapabilitiesLauncher.class);
	private ApplicationContext context;
	
	private void init() {
		String[] locations = {
				"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"
		};
		context = new ClassPathXmlApplicationContext(locations);
	}
	
	private void index(long resourceAccessPointId) {
		SequenceProcessor workflow = (SequenceProcessor)context.getBean("BIOCASE:1.3:capabilites");
		Map<String, Object> seed = new HashMap<String, Object>();
		seed.put("resourceAccessPointId", resourceAccessPointId);		
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
		BiocaseCapabilitiesLauncher launcher = new BiocaseCapabilitiesLauncher();
		launcher.init();
		if(args.length>=1){
			try {
				for (int i=0; i<args.length; i++) {
					long resourceAccessPointId = Long.parseLong(args[i]);
					System.out.println("Starting BiocaseCapabilitiesLauncher for rapId: " + resourceAccessPointId);
					launcher.index(resourceAccessPointId);
					System.out.println("Finished BiocaseCapabilitiesLauncher  for rapId: " + resourceAccessPointId);
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
		System.out.println(BiocaseCapabilitiesLauncher.class.getName()+" <resource-access-point-id+>");
		System.out.println(" e.g. "+BiocaseCapabilitiesLauncher.class.getName()+" 99 12 14");		
	}
}
