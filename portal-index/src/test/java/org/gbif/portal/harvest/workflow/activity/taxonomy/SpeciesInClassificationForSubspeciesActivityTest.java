package org.gbif.portal.harvest.workflow.activity.taxonomy;

import java.util.LinkedList;
import java.util.List;

import org.gbif.portal.model.TaxonName;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.MapContext;
import org.gbif.portal.util.workflow.ProcessContext;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class SpeciesInClassificationForSubspeciesActivityTest  extends AbstractDependencyInjectionSpringContextTests {
	
	/**
	 * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#getConfigLocations()
	 */
	protected String[] getConfigLocations() {
		return new String [] {
				"classpath*:org/gbif/portal/**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-test-*.xml",
				};
	}
	
	
	@SuppressWarnings("unchecked")
	public void testSpeciesAdded() {
		try {
			Activity activity = (Activity) applicationContext.getBean("speciesInClassificationForSubspeciesActivity");
			ProcessContext context = new MapContext();
			List<TaxonName> classification = new LinkedList<TaxonName>();
			
			context.put("classification", classification);		
			classification.add(new TaxonName("Animalia", null, 1000));
			classification.add(new TaxonName("Chordata", null, 2000));
			classification.add(new TaxonName("Passer", null, 6000));
			
			logger.info("1:");
			for (TaxonName name : classification) {
				logger.info(name);
			}
			activity.execute(context);
			assertTrue(classification.size()==3);
			
			classification.add(new TaxonName("Passer domesticus biblicus", null, 8000));
			activity.execute(context);
			assertTrue(classification.size()==5);
			activity.execute(context);
			assertTrue(classification.size()==5);
			logger.info("2:");
			for (TaxonName name : classification) {
				logger.info(name);
			}
		} catch (Exception e) {
			logger.error("Activity failed", e);
		}
	}
}
