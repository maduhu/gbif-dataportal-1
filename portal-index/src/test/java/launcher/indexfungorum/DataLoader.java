/**
 * 
 */
package launcher.indexfungorum;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.gbif.portal.util.workflow.SequenceProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Launcher for loading IF 
 * @author tim
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
		SequenceProcessor workflow = (SequenceProcessor)context.getBean("INDEX_FUNGORUM:0.1:dataLoad");
		
		try {
			Map<String, Object> seed = new HashMap<String, Object>();
			List<String> files = new LinkedList<String>();
			files.add("/home/tim/indexFungorum/IFDumpLSID.txt");
			seed.put("urlList", files);
			seed.put("dataProviderName", "Index Fungorum");
			seed.put("dataResourceName", "Index Fungorum");
			seed.put("kingdom", "Fungi");
			seed.put("description", "The Index Fungorum, the world database of fungal names coordinated and supported by the Index Fungorum Partnership, contains names of fungi (including yeasts, lichens, chromistan fungi, protozoan fungi and fossil forms) at species level and below.");
			seed.put("basisOfRecord", "Nomenclator");
			seed.put("citation", "Index Fungorum (2007)");
			seed.put("rights", "See http://www.indexfungorum.org");
			seed.put("logoUrl", "http://www.indexfungorum.org/IMAGES/LogoIF.gif");
			seed.put("taxonomicPriorityText", "10");
			seed.put("separator", ",");
			seed.put("quoteCharacter", "\"");
			
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
	}
}
