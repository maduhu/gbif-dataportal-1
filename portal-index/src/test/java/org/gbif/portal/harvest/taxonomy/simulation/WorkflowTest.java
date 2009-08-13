package org.gbif.portal.harvest.taxonomy.simulation;

import java.util.HashMap;
import java.util.Map;

import org.gbif.portal.dao.RawOccurrenceRecordDAO;
import org.gbif.portal.model.RawOccurrenceRecord;
import org.gbif.portal.util.workflow.Processor;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class WorkflowTest extends AbstractDependencyInjectionSpringContextTests {
	/**
	 * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#getConfigLocations()
	 */
	protected String[] getConfigLocations() {
		return new String [] {
				"classpath*:org/gbif/portal/**/applicationContext-*.xml",
				};
	}

	/*
	 * Test method for parsing
	 */
	public void testParsing() {
		// load a problematic raw_occurrence_record
		long id = 24712404;
		RawOccurrenceRecordDAO dao = (RawOccurrenceRecordDAO) applicationContext.getBean("rawOccurrenceRecordDAO");
		RawOccurrenceRecord ror = (RawOccurrenceRecord) dao.getById(id);
		if(ror!=null){
			logger.info("ror: " + ror.getId());
			
			Processor workflow = (Processor) applicationContext.getBean("TEST:EXTRACT:CLASSIFICATION_PARSE_TEST");
			Map<String, Object> seed = new HashMap<String, Object>();
			seed.put("rawOccurrenceRecord", ror);
			try {
				workflow.doActivities(seed);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		
	}
}
