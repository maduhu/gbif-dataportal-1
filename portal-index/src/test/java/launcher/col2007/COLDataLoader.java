/**
 * 
 */
package launcher.col2007;

import org.gbif.portal.util.workflow.SequenceProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Launcher for loading Col2007 
 * @author tim
 */
public class COLDataLoader {
	private ApplicationContext context;
	
	private void init() {
		String[] locations = {
				"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"
		};
		context = new ClassPathXmlApplicationContext(locations);
	}
	
	private void index() {
		SequenceProcessor workflow = (SequenceProcessor)context.getBean("COL:2007:dataLoad");
		try {
			workflow.doActivities();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		System.exit(0);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		COLDataLoader launcher = new COLDataLoader();
		launcher.init();
		launcher.index();
	}
}
