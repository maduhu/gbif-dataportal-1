/**
 * 
 */
package launcher;

import java.util.Calendar;
import java.util.GregorianCalendar;
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
public class ExtractHarvestedOccurrencesLauncher {
	protected Log logger = LogFactory.getLog(ExtractHarvestedOccurrencesLauncher.class);
	private ApplicationContext context;
	
	private void init() {
		String[] locations = {"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"};
		context = new ClassPathXmlApplicationContext(locations);
	}
	
	private void index(long id) {
		SequenceProcessor workflow = (SequenceProcessor)context.getBean("GBIF:INDEX:1.0:extractOccurrence");
		Map<String, Object> seed = new HashMap<String, Object>();
		GregorianCalendar cal = new GregorianCalendar();
		cal.add(Calendar.MONTH, -12);
		seed.put("pageFrom", cal.getTime());
		seed.put("resourceAccessPointId", new Long(id));
		try {
			workflow.doActivities(seed);
			logger.info("Finished...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExtractHarvestedOccurrencesLauncher launcher = new ExtractHarvestedOccurrencesLauncher();		
		launcher.init();
		if(args.length>=1){
			try {
				for (int i=0; i<args.length; i++) {
					long resourceAccessPointId = Long.parseLong(args[i]);
					System.out.println("Starting ExtractHarvestedOccurrencesLauncher for rapId: " + resourceAccessPointId);
					launcher.index(resourceAccessPointId);
					System.out.println("Finished ExtractHarvestedOccurrencesLauncher  for rapId: " + resourceAccessPointId);
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
		System.out.println(ExtractHarvestedOccurrencesLauncher.class.getName()+" <resource-access-point-id+>");
		System.out.println(" e.g. "+ExtractHarvestedOccurrencesLauncher.class.getName()+" 99 12 14");		
	}
}
