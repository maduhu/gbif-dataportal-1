/**
 * 
 */
package launcher;

import org.gbif.portal.util.workflow.SequenceProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Launcher for synchronising UDDI
 * @author tim
 */
public class UDDISynchroniser {
	private ApplicationContext context;
	
	private void init() {
		String[] locations = {"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"
		};
		
		context = new ClassPathXmlApplicationContext(locations);
	}
	
	private void index() {
		SequenceProcessor workflow = (SequenceProcessor)context.getBean("GBIF:INDEX:1.0:uddi");
		try {
			workflow.doActivities();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UDDISynchroniser launcher = new UDDISynchroniser();
		launcher.init();
		launcher.index();
		System.exit(1);
	}

}
