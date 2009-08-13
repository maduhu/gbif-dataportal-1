package org.gbif.portal.util.file;

import junit.framework.TestCase;

/**
 * Little test cases
 * @author trobertson
 *
 */
public class DelimitedFileReaderTest extends TestCase {
	public void testTDF() {
		DelimitedFileReader dfr = new DelimitedFileReader(this.getClass().getResourceAsStream("tdf.txt"), "\t", null, false);
		String[][] data = {{"1", "Tim"},{"2", "Tim, tom"}};
		int i=0;
		while (dfr.next()) {
			assertEquals(data[i][0], dfr.get(0));
			assertEquals(data[i][1], dfr.get(1));
			i++;
		}
	}
}
