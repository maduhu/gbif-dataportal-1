package org.gbif.portal.index.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dao.RawOccurrenceRecordDAO;
import org.gbif.portal.model.RawOccurrenceRecord;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * The single table test application 
 */
public class HarvestSimulate implements Runnable {
	static Log logger = LogFactory.getLog(HarvestSimulate.class);
	protected String prefix;
	protected int dataProviderId;
	protected int dataResourceId;
	protected int resourceAccessPointId;
	protected RawOccurrenceRecordDAO rawOccurrenceRecordDAO;
	
	public void run() {
		int ic = 0;
		int cc = 0;
		int cn = 0;
		
		long start = System.currentTimeMillis();
		for (int i=0; i<5000; i++, cc++) {
			long startPage = System.currentTimeMillis();
			for (int j=0; j<1000; j++, cn++) {
				// simulate a read
				rawOccurrenceRecordDAO.getUniqueRecord(dataResourceId, prefix+ic, ""+cc, ""+cn, "");
				//just do an insert for this test... 
				RawOccurrenceRecord ror = new RawOccurrenceRecord();
				ror.setDataProviderId(dataProviderId);
				ror.setDataResourceId(dataResourceId);
				ror.setResourceAccessPointId(resourceAccessPointId);
				ror.setInstitutionCode(prefix+ic);
				ror.setCollectionCode(prefix+cc);
				// mix it up a little...
				if (prefix.startsWith("x") || prefix.startsWith("y") || prefix.startsWith("z")
						|| prefix.startsWith("a") || prefix.startsWith("b") || prefix.startsWith("c")) {
					ror.setCatalogueNumber(prefix+cn);
				} else {
					ror.setCatalogueNumber(""+cn);
				}
				rawOccurrenceRecordDAO.create(ror);
			}
			logger.info("Page " + ((System.currentTimeMillis()-startPage)/1000) + " secs");
		}
		logger.info("Total: " + ((System.currentTimeMillis()-start)/1000) + " secs");
	}
	
	public static void main(String[] args) {
		String[] locations = {"classpath*:/org/gbif/**/applicationContext-*.xml"};
		ApplicationContext context = new ClassPathXmlApplicationContext(locations);
		RawOccurrenceRecordDAO rawOccurrenceRecordDAO;
		rawOccurrenceRecordDAO = (RawOccurrenceRecordDAO) context.getBean("rawOccurrenceRecordDAO");
		String[] prefixes = {"aaaa","bbbb","cccc","dddd","eeee","ffff","gggg","hhhh","iiii","jjjj","kkkk",
				"llll","mmmm","nnnn","oooo","ppppp","qqqq","rrrr","ssss","tttt","uuuu","vvvv","wwww","xxxx","yyyy","zzzz"};
		for (int i=1; i<=25; i++) {
			Thread t = new Thread(new HarvestSimulate(i,i,i,rawOccurrenceRecordDAO, prefixes[i-1]));
			t.start();
		}
	}


	
	public HarvestSimulate(int dataProviderId, int dataResourceId, int resourceAccessPointId, RawOccurrenceRecordDAO rawOccurrenceRecordDAO, String prefix) {
		this.dataProviderId = dataProviderId;
		this.dataResourceId = dataResourceId;
		this.resourceAccessPointId = resourceAccessPointId;
		this.rawOccurrenceRecordDAO = rawOccurrenceRecordDAO;
		this.prefix = prefix;
	}

}
