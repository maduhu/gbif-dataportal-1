/**
 * 
 */
package launcher.ecat;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.gbif.portal.harvest.taxonomy.ClassificationContainer;
import org.gbif.portal.util.workflow.SequenceProcessor;
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
		SequenceProcessor workflow = (SequenceProcessor)context.getBean("ECAT:0.1:dataLoad");
		try {
			Map<String, Object> seed = new HashMap<String, Object>();
			List<String> files = new LinkedList<String>();
			files.add(args[0]);

			seed.put("urlList", files);
			seed.put("dataProviderName", args[1]);
			seed.put("dataResourceName", args[2]);
			seed.put("taxonomicPriorityText", "20");
			seed.put("basisOfRecord", "Taxonomy");
			seed.put("description", "new taxonomic resource");
			seed.put("citation", "Enter citation here");
			seed.put("rights", "Enter rights here");
			seed.put("logoUrl", "Need logo here");
			if (args.length >= 4 && new Boolean(args[3])) {
				seed.put("previousClassificationContainer", new ClassificationContainer());
			}
			if (args.length >= 5 && args[4].length() > 0) {
				seed.put("kingdom", args[4]);
			}
			if (args.length >= 6 && args[5].length() > 0) {
				seed.put("phylum", args[5]);
			}
			if (args.length >= 7 && args[6].length() > 0) {
				seed.put("class", args[6]);
			}
			if (args.length >= 8 && args[7].length() > 0) {
				seed.put("order", args[7]);
			}
			if (args.length >= 9 && args[8].length() > 0) {
				seed.put("family", args[8]);
			}
			if (args.length >= 10 && args[9].length() > 0) {
				seed.put("genus", args[9]);
			}
			if (args.length >= 11 && args[10].length() > 0) {
				seed.put("separator", args[10]);
			}
			if (args.length >= 12 && args[11].length() > 0) {
				seed.put("quoteCharacter", args[11]);
			}
			
			workflow.doActivities(seed);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println("DataLoader fileName providerName resourceName [isNested [kingdom [phylum [class [order [family [genus [separator [quoteCharacter]]]]]]]]]");
		}
		else {
			DataLoader launcher = new DataLoader();
			launcher.init();
			launcher.index(args);
		}
	}
}
