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
package org.gbif.portal.util.propertystore;


/**
 * Used to indicate that a property is not declared in the PropertyStore
 * in the expected format
 *
 * @author trobertson
 */
public class MisconfiguredPropertyException extends Exception {
  /**
   * Generated
   */
  private static final long serialVersionUID = 8283377767768583868L;

  /**
   * Generates a logable message saying that the property found in the namespace is of
   * an invalid form
   *
   * @param namespace That is being used
   * @param property  Within the namespace that is of the wrong form
   * @param expected  The type expected
   * @param received  The type mapped
   */
  public MisconfiguredPropertyException(String namespace, String property, Class expected, Class received) {
    super("Property [" + property + "] for namespace [" + namespace + "] in the property store is of invalid type. " +
      "Expecting [" + expected + "] received[" + received + "]");
  }

  /**
   * Takes the logable message
   *
   * @param message To use for the exception
   */
  public MisconfiguredPropertyException(String message) {
    super(message);
  }

  /**
   * Takes the message and cause
   *
   * @param message To use for the exception
   * @param cause   To signal
   */
  public MisconfiguredPropertyException(String message, Throwable t) {
    super(message, t);
  }
}
