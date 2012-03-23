package org.gbif.portal.util.file;

import junit.framework.TestCase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Little test cases
 *
 * @author trobertson
 */
public class DelimitedFileReaderTest extends TestCase {
  public void testTDF() throws FileNotFoundException {
    DelimitedFileReader dfr = new DelimitedFileReader((new FileInputStream(
      "./src/test/java/org/gbif/portal/util/file/tdf.txt")), "\t", null, false);
    String[][] data = {{"1", "Tim"}, {"2", "Tim, tom"}};
    int i = 0;
    while (dfr.next()) {
      assertEquals(data[i][0], dfr.get(0));
      assertEquals(data[i][1], dfr.get(1));
      i++;
    }
  }
}
