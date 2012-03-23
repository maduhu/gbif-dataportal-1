package org.gbif.portal.harvest.taxonomy;

import java.util.LinkedList;

import org.gbif.portal.model.TaxonName;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class ScientificNameParserTest extends AbstractDependencyInjectionSpringContextTests {
	protected ScientificNameParser scientificNameParser;
	
	/**
	 * @see org.springframework.test.AbstractSingleSpringContextTests#onSetUp()
	 */
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		scientificNameParser = (ScientificNameParser) applicationContext.getBean("scientificNameParser");
	}

	/**
	 * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#getConfigLocations()
	 */
	protected String[] getConfigLocations() {
		return new String [] {
				"classpath*:org/gbif/portal/**/applicationContext-*.xml",
				};
	}

	/*
	 * Test method for 'org.gbif.portal.harvest.taxonomy.ScientificNameParser.parse(String, List<TaxonName>)'
	 */
	public void testParseStringListOfTaxonName() {
		LinkedList<TaxonName> classification = new LinkedList<TaxonName>();
		scientificNameParser.parse(null, "Aus (Bus)", classification);
		logger.info("Parsed classification: ");
		for (TaxonName name : classification) {
			logger.info(name.toString());
		}
		assertEquals(1, classification.size());
		assertEquals(6500, classification.get(0).getRank());
		assertEquals("Bus", classification.get(0).getCanonical());
		
		classification = new LinkedList<TaxonName>();
		scientificNameParser.parse(null, "Aus (Bus) cus", classification);
		logger.info("Parsed classification: ");
		for (TaxonName name : classification) {
			logger.info(name.toString());
		}		
		assertEquals(1, classification.size());
		assertEquals(7000, classification.get(0).getRank());
		assertEquals("Aus", classification.get(0).getGeneric());
		assertEquals("Bus", classification.get(0).getInfraGeneric());
		assertEquals("cus", classification.get(0).getSpecific());
		assertEquals("Aus cus", classification.get(0).getCanonical());
		
		classification = new LinkedList<TaxonName>();
		scientificNameParser.parse(null, "Aus (Bus) cus dus", classification);
		logger.info("Parsed classification: ");
		for (TaxonName name : classification) {
			logger.info(name.toString());
		}		
		assertEquals(1, classification.size());
		assertEquals(8000, classification.get(0).getRank());
		assertEquals("Aus", classification.get(0).getGeneric());
		assertEquals("Bus", classification.get(0).getInfraGeneric());
		assertEquals("cus", classification.get(0).getSpecific());
		assertEquals("dus", classification.get(0).getInfraSpecific());
		assertEquals("Aus cus dus", classification.get(0).getCanonical());
		
		classification = new LinkedList<TaxonName>();
		scientificNameParser.parse(null, "Chamaeleo (Furcifer)", classification);
		logger.info("Parsed classification: ");
		for (TaxonName name : classification) {
			logger.info(name.toString());
		}		
		assertEquals(1, classification.size());
		assertEquals(6500, classification.get(0).getRank());
		assertEquals("Chamaeleo", classification.get(0).getGeneric());
		assertEquals("Furcifer", classification.get(0).getInfraGeneric());
		assertEquals("Furcifer", classification.get(0).getCanonical());
	}
}
