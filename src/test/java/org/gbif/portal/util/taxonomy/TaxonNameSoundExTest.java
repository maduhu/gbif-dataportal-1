package org.gbif.portal.util.taxonomy;

import junit.framework.TestCase;

public class TaxonNameSoundExTest extends TestCase {

	/*
	 * Test method for 'org.gbif.portal.util.taxonomy.TaxonNameSoundEx.soundEx(String)'
	 */
	public void testSoundEx() {
		assertEquals("TIM", new TaxonNameSoundEx().soundEx("tim"));
		assertEquals("TABC", new TaxonNameSoundEx().soundEx("tCBa"));
	}

	/*
	 * Test method for 'org.gbif.portal.util.taxonomy.TaxonNameSoundEx.alphabetiseWordsIgnoringFirstLetter(String)'
	 */
	public void testAlphabetiseWordsIgnoringFirstLetter() {
		assertEquals("Cbcdfg", new TaxonNameSoundEx().alphabetiseWordsIgnoringFirstLetter("Cbcdgf"));
	}
	
	
	/*
	 * Test method for 'org.gbif.portal.util.taxonomy.TaxonNameSoundEx.removeRepeatedChars(String)'
	 */
	public void testRemoveRepeatedChars() {
		assertEquals("I", new TaxonNameSoundEx().soundEx("iii"));
		assertEquals("TIM", new TaxonNameSoundEx().soundEx("TTTTiiimmmm"));
	}

	/*
	 * Test method for 'org.gbif.portal.util.taxonomy.TaxonNameSoundEx.selectiveReplaceWithoutFirstChar(String)'
	 */
	public void testSelectiveReplaceWithoutFirstChar() {
		assertEquals("TIM", new TaxonNameSoundEx().selectiveReplaceWithoutFirstChar("TAEM"));
	}

	/*
	 * Test method for 'org.gbif.portal.util.taxonomy.TaxonNameSoundEx.selectiveReplaceFirstChar(String)'
	 */
	public void testSelectiveReplaceFirstChar() {
		assertEquals("ETIM", new TaxonNameSoundEx().selectiveReplaceFirstChar("AETIM"));
	}

}
