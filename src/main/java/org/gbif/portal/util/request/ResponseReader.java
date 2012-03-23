/**
 *
 */
package org.gbif.portal.util.request;

import java.io.InputStream;

/**
 * An interface to define that an object will handle the response fully.
 * Instances are typically used as delegating objects to enable the stream
 * to be dealt with directly for efficiency, while also decoupling the stream
 * resource management from the content handling.
 *
 * @author tim
 */
public interface ResponseReader {
  /**
   * Reads the IS, returning optionally an object.
   *
   * @param is The input stream to read
   * @return Optionally an object may be returned, depending on the
   *         implementation.  Some implementations may be a factory returning an
   *         object, others may be a decorator decoarating an existing object.
   * @throws Exception Depending on implementation
   */
  Object read(InputStream is) throws Exception;
}
