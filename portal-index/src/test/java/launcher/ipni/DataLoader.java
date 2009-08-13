/**
 * 
 */
package launcher.ipni;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.gbif.portal.util.workflow.SequenceProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Launcher for loading IPNI 
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
		SequenceProcessor workflow = (SequenceProcessor)context.getBean("IPNI:0.1:dataLoad");
		
		try {
			Map<String, Object> seed = new HashMap<String, Object>();
			List<String> files = new LinkedList<String>();
			//files.add("/Users/tim/Desktop/ipni.txt");
			
			files.add("c:/temp/ipni/minimal_download_0.txt");
			files.add("c:/temp/ipni/minimal_download_1.txt");
			files.add("c:/temp/ipni/minimal_download_2.txt");
			files.add("c:/temp/ipni/minimal_download_3.txt");
			files.add("c:/temp/ipni/minimal_download_4.txt");
			files.add("c:/temp/ipni/minimal_download_5.txt");
			files.add("c:/temp/ipni/minimal_download_6.txt");
			files.add("c:/temp/ipni/minimal_download_7.txt");
			files.add("c:/temp/ipni/minimal_download_8.txt");
			files.add("c:/temp/ipni/minimal_download_9.txt");
			files.add("c:/temp/ipni/minimal_download_10.txt");
			files.add("c:/temp/ipni/minimal_download_11.txt");
			files.add("c:/temp/ipni/minimal_download_12.txt");
			files.add("c:/temp/ipni/minimal_download_13.txt");
			files.add("c:/temp/ipni/minimal_download_14.txt");
			files.add("c:/temp/ipni/minimal_download_15.txt");
			files.add("c:/temp/ipni/minimal_download_16.txt");
			files.add("c:/temp/ipni/minimal_download_17.txt");
			files.add("c:/temp/ipni/minimal_download_18.txt");
			files.add("c:/temp/ipni/minimal_download_19.txt");
			files.add("c:/temp/ipni/minimal_download_20.txt");
			files.add("c:/temp/ipni/minimal_download_21.txt");
			files.add("c:/temp/ipni/minimal_download_22.txt");
			files.add("c:/temp/ipni/minimal_download_23.txt");
			files.add("c:/temp/ipni/minimal_download_24.txt");
			files.add("c:/temp/ipni/minimal_download_25.txt");
			files.add("c:/temp/ipni/minimal_download_26.txt");
			files.add("c:/temp/ipni/minimal_download_27.txt");
			files.add("c:/temp/ipni/minimal_download_28.txt");
			files.add("c:/temp/ipni/minimal_download_29.txt");
			files.add("c:/temp/ipni/minimal_download_30.txt");
			files.add("c:/temp/ipni/minimal_download_31.txt");
			
			seed.put("urlList", files);
			seed.put("dataProviderName", "The International Plant Names Index");
			seed.put("dataResourceName", "IPNI");
			seed.put("description", "The International Plant Names Index (IPNI) is a database of the names and associated basic bibliographical details of all seed plants, ferns and fern allies.");
			seed.put("basisOfRecord", "Unknown");
			seed.put("citation", "The International Plant Names Index (2004). Published on the Internet http://www.ipni.org [accessed 2007]");
			seed.put("rights", "See http://www.ipni.org");
			seed.put("logoUrl", "http://www.ipni.org/images/ipni_logoonly_sm.jpg");
			seed.put("taxonomicPriorityText", "10");			
			
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
