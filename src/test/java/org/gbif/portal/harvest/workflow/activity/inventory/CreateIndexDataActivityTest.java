package org.gbif.portal.harvest.workflow.activity.inventory;

import junit.framework.TestCase;

/**
 * Tests for the validation
 * @author trobertson
 */
public class CreateIndexDataActivityTest extends TestCase {

	/*
	 * Test method for 'org.gbif.portal.harvest.workflow.activity.inventory.CreateIndexDataActivity.validateRange(String, String)'
	 */
	public void testValidateRange() {
		CreateIndexDataActivity test = new CreateIndexDataActivity();
		assertEquals("Aus", test.validateRange("Aus", "A", false));
		
		assertEquals("AuA", test.validateRange("Au", "A", false));
		assertEquals("Aus bus ", test.validateRange("Aus bus & cus", "A", false));
		assertEquals("AAA", test.validateRange(" ", "A", false));
		assertEquals("   Aus", test.validateRange("   Aus", "A", false));
		assertEquals("AAA", test.validateRange("", "A", false));
		assertEquals("AAA", test.validateRange(null, "A", false));
		assertEquals("AuA", test.validateRange("Au&", "A", false));
		assertEquals("  Aus  ", test.validateRange("  Aus  ", "A", false));
		
		assertEquals("Aus z", test.validateRange("Aus &", "z", true));
		assertEquals("Aus Oz", test.validateRange("Aus O'Tuama", "z", true));
		
		
		
	}
}
