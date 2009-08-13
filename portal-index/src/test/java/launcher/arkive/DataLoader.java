/**
 * 
 */
package launcher.arkive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gbif.portal.util.workflow.SequenceProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Launcher for loading ARKive 
 * @author Donald Hobern
 */
public class DataLoader {
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
		SequenceProcessor workflow = (SequenceProcessor)context.getBean("ARKive:0.1:dataLoad");
		try {
			Map<String, Object> seed = new HashMap<String, Object>();
			seed.put("dataProviderName", "Wildscreen");
			seed.put("dataResourceName", "ARKive");
			seed.put("taxonomicPriorityText", "100");
			seed.put("description", "Creating a lasting audio-visual record of life on Earth.");
			seed.put("basisOfRecord", "Unknown");
			seed.put("citation", "© Wildscreen Trading Limited or its contributors");
			seed.put("url", "http://portaldev2.gbif.org/species-v2.xml");
			seed.put("requestToIssue", "");
			seed.put("website", "http://www.arkive.org/");
			seed.put("rights", "See http://www.arkive.org/terms.html");
			seed.put("logoUrl", "http://www.arkive.org/images/header_logo_60.png");
			List<String> psNamespaces = new ArrayList<String>();
			psNamespaces.add("http://www.arkive.org/0.1");
			seed.put("psNamespaces", psNamespaces);
			
			workflow.doActivities(seed);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DataLoader launcher = new DataLoader();
		launcher.init();
		launcher.index();
		System.exit(1);
	}
}
