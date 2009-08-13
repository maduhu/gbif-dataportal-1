/**
 * 
 */
package launcher;

import java.util.HashMap;
import java.util.Map;

import org.gbif.portal.util.workflow.SequenceProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Launcher for indexing tests
 * @author tim
 */
public class DataResourceSyncLauncher {
	private ApplicationContext context;
	
	private void init() {
		String[] locations = {"classpath*:/**/applicationContext-*.xml"};
		
		context = new ClassPathXmlApplicationContext(locations);
	}
	
	private void index() {
		//SequenceProcessor workflow = (SequenceProcessor)context.getBean("BIOCASE:1.3:capabilites");
		SequenceProcessor workflow = (SequenceProcessor)context.getBean("DIGIR:1.0:metadata");
		Map<String, Object> seed = new HashMap<String, Object>();
		seed.put("resourceAccessPointId", new Long(457));		
		try {
			workflow.doActivities(seed);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DataResourceSyncLauncher launcher = new DataResourceSyncLauncher();
		launcher.init();
		launcher.index();
	}
}
