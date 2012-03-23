/***************************************************************************
 * Copyright (C) 2005 Global Biodiversity Information Facility Secretariat.
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ***************************************************************************/
package org.gbif.portal.util.image;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * Utilities for dealing with images
 *
 * @author dhobern
 */
public class ImageUtils {

  protected static Log logger = LogFactory.getLog(ImageUtils.class);

  /**
   * Return true if the URL resolves to an image (rather than e.g. a web
   * page or application launcher.
   *
   * @param url
   * @return true if image is loadable
   */
  public static boolean isImageLoadable(String url) {
    try {
      RenderedImage image = ImageIO.read(new URL(url));
      image.getMinX();
    } catch (Exception e) {
      return false;
    } catch (Throwable e) {
      logger.warn("JAI is not functioning correctly - Ignoring: " + e.getMessage());
      // happens when the JAI is not working.... no reason to know it is bust
      return true;
    }
    return true;
  }

  /**
   * Return true if the URL resolves to an image (rather than e.g. a web
   * page or application launcher.
   *
   * @param url
   * @return image width and height in a int array
   */
  public static int[] findImageDimensions(String url) {
    try {
      RenderedImage image = ImageIO.read(new URL(url));
      return new int[]{image.getWidth(), image.getHeight()};
    } catch (Exception e) {
      logger.error("Problem loading image from url: " + url);
      logger.error(e.getMessage(), e);
      return null;
    }
  }

  /**
   * Scales the image to the specified height and width in pixels.
   *
   * @param url
   * @param width
   * @param height
   * @param fileOutputUrl
   * @param format
   * @return true if image is loadable
   */
  public static boolean scaleImageAndWriteToFile(String url, int width, int height, String fileOutputUrl,
                                                 String format) {
    try {
      RenderedImage image = ImageIO.read(new URL(url));
      scaleImage(image, width, height);
      ImageIO.write(image, format, new File(fileOutputUrl));
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  /**
   * Scales the image to the specified height and width in pixels.
   *
   * @param url
   * @param width
   * @param height
   * @param fileOutputUrl
   * @param format
   * @return true if image is loadable
   */
  public static byte[] scaleImageFromStream(InputStream imageStream, int width, int height, String format) {
    try {
      BufferedImage bufferedImage = ImageIO.read(imageStream);
//			RenderedImage image = JAI.create("stream", imageStream);
      scaleImage(bufferedImage, width, height);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      BufferedOutputStream bos = new BufferedOutputStream(baos);
//			JAI.create("stream", image, bos, format);

//			ImageIO.
      ImageIO.write(bufferedImage, format, bos);

      return baos.toByteArray();
    } catch (Exception e) {
      return null;
    }
  }


  /**
   * Scale an image to the required size
   *
   * @param image
   * @param width
   * @param height
   */
  public static void scaleImage(RenderedImage image, int width, int height) {

    int originalWidth = image.getWidth();
    int originalHeight = image.getHeight();

    Float heightScale = 1.0f;
    Float widthScale = 1.0f;

    if (originalHeight > height) {
      heightScale = (float) height / (float) originalHeight;
    }
    if (originalWidth > width) {
      widthScale = (float) width / (float) originalWidth;
    }

    if (heightScale < widthScale) {
      widthScale = heightScale;
    } else {
      heightScale = widthScale;
    }
    image = JAI.create("scale", image, widthScale, heightScale);
  }
}
