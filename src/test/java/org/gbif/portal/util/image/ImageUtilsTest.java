/***************************************************************************
 * Copyright (C) 2005 Global Biodiversity Information Facility Secretariat.
 * All Rights Reserved.
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ***************************************************************************/

package org.gbif.portal.util.image;

import junit.framework.TestCase;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author tim
 */
public class ImageUtilsTest extends TestCase {

  /**
   * Test method for {@link org.gbif.portal.util.image.ImageUtils#isImageLoadable(java.lang.String)}.
   */
  public void testIsImageLoadable() {
    try {
      // assertTrue(ImageUtils.isImageLoadable("http://www.gbif.org/images/spacer.gif")); // fails
      assertTrue(ImageUtils.isImageLoadable("http://ukmoths.org.uk/images/02286_Borkhausenia_minutella_3.jpg"));
      assertFalse(ImageUtils.isImageLoadable("absolute garbage string"));
      assertFalse(ImageUtils.isImageLoadable("http://www.gbif.org/"));
    } catch (Exception e) {
      fail(e.toString());
    }
  }

  /**
   * Test method for {@link org.gbif.portal.util.image.ImageUtils#scaleImage(java.lang.String, int, int)}.
   */
  public void testScaleImage() {
    try {
      String tmpDir = System.getProperty("java.io.tmpdir");
      assertTrue(ImageUtils.scaleImageAndWriteToFile("http://ukmoths.org.uk/images/02286_Borkhausenia_minutella_3.jpg",
        20, 20, tmpDir + "/image.jpg", "JPEG"));
      assertTrue(ImageUtils.scaleImageAndWriteToFile("http://ukmoths.org.uk/images/02286_Borkhausenia_minutella_3.jpg",
        200, 200, tmpDir + "/image.jpg", "JPEG"));
      assertTrue(ImageUtils.scaleImageAndWriteToFile("http://ukmoths.org.uk/images/02286_Borkhausenia_minutella_3.jpg",
        150, 150, tmpDir + "/image.jpg", "JPEG")); // passes!
      assertFalse(ImageUtils.isImageLoadable("http://www.gbif.org/"));
    } catch (Exception e) {
      fail(e.toString());
    }
  }

  public void testScaleImageFromStream() {
    HttpClient httpClient = new HttpClient();
    GetMethod getMethod = new GetMethod("http://ukmoths.org.uk/images/02286_Borkhausenia_minutella_3.jpg");
    try {
      httpClient.executeMethod(getMethod);
      InputStream in = getMethod.getResponseBodyAsStream();
      ImageUtils.scaleImageFromStream(in, 20, 20, "JPEG");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
