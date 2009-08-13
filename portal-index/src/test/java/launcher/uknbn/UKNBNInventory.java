/**
 * 
 */
package launcher.uknbn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dao.IndexDataDAO;
import org.gbif.portal.harvest.workflow.activity.inventory.CreateIndexDataActivity;
import org.gbif.portal.model.IndexData;
import org.gbif.portal.util.file.DelimitedFileReader;
import org.gbif.portal.util.mhf.message.Message;
import org.gbif.portal.util.propertystore.PropertyNotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * A fake inventory -> index data creator
 *  
 * @author tim
 */
public class UKNBNInventory {
	/**
	 * The commons logger
	 */
	protected Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * The spring context 
	 */
	protected ApplicationContext context;
	
	protected IndexDataDAO indexDataDAO;
	
	/**
	 * Creates the index data
	 */
	protected void launch() {
		try {
			logger.info("Starting UKNBN inventory creation...");
			DelimitedFileReader dfr = new DelimitedFileReader(this.getClass().getResourceAsStream("uknbn.txt"), "\t", null, false);
			logger.info("DFR: " + dfr);
			
			int runningCount = 0;
			String lowerConcept = null;
			String upperConcept = null;
			int countIfNotProvided = 10;
			int countPerRange = 1000;
			// loop over building up the ranges as indexData's 
			while (dfr.next()) {
				String concept = dfr.get(0);
				
				if (lowerConcept == null) {
					lowerConcept = concept;				
				} 			
				
				// get the count or use the default for the current loop 
				int count = countIfNotProvided;
				try {
					count = Integer.parseInt(dfr.get(1));
				} catch (Exception e) {
					// swallow - not valid
				} 
				
				//logger.info(concept + ": " + count);
				
				// the running count + the count is greater than that of the limit, then create the range			
				if ((count + runningCount) > countPerRange) {
					
					// it may be that the first record has more than that required, but if so we need to use it
					if (upperConcept == null) {
						upperConcept = lowerConcept;
					}
					
					logger.info(lowerConcept + ": " + upperConcept);
					
					indexDataDAO.create(
							new IndexData(
									1087,
									1,
									validateRange(lowerConcept, "A", false),
									validateRange(upperConcept, "z", true)));
					
					// reset for the next loop
					lowerConcept = concept;
					upperConcept = null;
					runningCount = 0;
					
				} else {
					// set it for later
					upperConcept = concept;
					runningCount += count;
				}			
			}
			
				if (lowerConcept != null) {
					if (upperConcept == null) {
						upperConcept = lowerConcept;
					}
					indexDataDAO.create(
							new IndexData(
									1087,
									1,
									validateRange(lowerConcept, "A", false),
									validateRange(upperConcept, "z", true)));
			}
			
			
			
			
			logger.info("Finished UKNBN inventory creation");
		} catch (RuntimeException e) {
			logger.error(e);
		}
	}
	
	/**
	 * Validates the range for usage
	 * Checks for &, " " and ranges less than 
	 * @param range
	 * @param replaceValue
	 * @return
	 */
	public String validateRange(String range, String replaceValue, boolean atEnd) {
		// if it looks ok, return
		if ( range != null
				&& range.length()>=3
				&& !range.contains("&")
				&& !range.matches(" +")) {
			return range;
			
		} else if (range == null) {
			return replaceValue + replaceValue + replaceValue;
			
		} else {
			if (range.matches(" +")) {
				return replaceValue + replaceValue + replaceValue;
			}
			
			// start by removing everything after &
			if (range.contains("&")) {
				if (!atEnd)
					range = range.substring(0,range.indexOf("&"));
				else 
					range = range.substring(0,range.indexOf("&")) + replaceValue;
			}
			// pad if necessary
			if (range==null || range.length()==0) {
				return replaceValue + replaceValue + replaceValue;
			}
			if (range.length()==1) {
				return range + replaceValue + replaceValue;
			}
			if (range.length()==2) {
				return range + replaceValue;
			}
			return range;
		}
	}
	
	
	/**
	 * Hidden constructor forcing the spring context
	 */
	protected UKNBNInventory() {
		String[] locations = {"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"
		};
		context = new ClassPathXmlApplicationContext(locations);
		indexDataDAO = (IndexDataDAO) context.getBean("indexDataDAO");
	}
	
	/**
	 * Entry
	 */
	public static void main(String[] args) {
		UKNBNInventory me = new UKNBNInventory();
		me.launch();
		System.exit(1);
	}
}
