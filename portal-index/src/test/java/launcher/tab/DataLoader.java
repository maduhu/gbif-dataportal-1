/**
 * 
 */
package launcher.tab;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.gbif.portal.util.workflow.SequenceProcessor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Launcher for loading ECAT files 
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
	
	private void index(String[] args) {
		harvestTabFile(238, 2193, 1942, "http://www.yorku.ca/bugsrus/GBIF_Gschwendtner_Grixti_2002.txt");
		harvestTabFile(238, 2194, 1943, "http://www.yorku.ca/bugsrus/GBIF_Argentina_Gravel_2005.txt");
		harvestTabFile(238, 2195, 1944, "http://www.yorku.ca/bugsrus/GBIF_FraserValley_Ratti_2003.txt");
		harvestTabFile(238, 2196, 1945, "http://www.yorku.ca/bugsrus/GBIF_Gschwendtner_Knerer_1968.txt");
		harvestTabFile(238, 2197, 1946, "http://www.yorku.ca/bugsrus/GBIF_LaCrete_Morandin_2002.txt");
		harvestTabFile(238, 2198, 1947, "http://www.yorku.ca/bugsrus/GBIF_JokersHill_Constantin_2002.txt");
		harvestTabFile(238, 2199, 1948, "http://www.yorku.ca/bugsrus/GBIF_Madagascar_Packer_2002.txt");
		harvestTabFile(238, 2200, 1949, "http://www.yorku.ca/bugsrus/GBIF_ChileXenochilicola_Packer_2004.txt");
	}

	/**
	 * @param dpId
	 * @param rapId
	 * @param drId
	 * @param url
	 * @throws Exception
	 */
	private void harvestTabFile(long dpId, long rapId, long drId, String url) {
		try {
			System.out.println("Starting RAPID: " + rapId);
			SequenceProcessor workflow = (SequenceProcessor)context.getBean("TAB:1.0:harvester");			
			Map<String, Object> seed = new HashMap<String, Object>();
			List<String> files = new LinkedList<String>();
			files.add(url);
			seed.put("urlList", files);
			seed.put("dataProviderId", dpId);
			seed.put("dataResourceId", drId);
			seed.put("resourceAccessPointId", rapId);			
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
		launcher.index(args);
	}
}
