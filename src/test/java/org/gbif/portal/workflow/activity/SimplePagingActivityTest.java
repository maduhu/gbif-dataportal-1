package org.gbif.portal.workflow.activity;

import java.util.HashMap;
import java.util.Map;

import org.gbif.portal.util.workflow.ProcessContext;
import org.gbif.portal.util.workflow.Processor;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class SimplePagingActivityTest  extends AbstractDependencyInjectionSpringContextTests {
	
	/**
	 * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#getConfigLocations()
	 */
	protected String[] getConfigLocations() {
		return new String [] {
				"classpath*:org/gbif/portal/**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/test-applicationContext-*.xml",
				};
	}

	/**
	 * Test method for checking the loop limit works on the SimplePagingActivity
	 */
	public void testHardCodedLimit() {
		Processor workflow = (Processor) applicationContext.getBean("test:simplePagingActivityWorkflow");
		try {
			Map<String, Object> seed = new HashMap<String, Object>();
			seed.put("flagToFinish", "false"); // never finishes "naturally"
			ProcessContext context = workflow.doActivities(seed);
			int count = (Integer) context.get("loopCount", Integer.class, true);
			assertEquals(10, count);
		} catch (Exception e) {
			logger.info(e.getMessage(), e);			
		}
	}
}
