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
package org.gbif.portal.util.mapping;

/**
 * Used to indicate that mapping has not been defined for a raw string
 *
 * @author dhobern
 */
public class MappingNotDefinedException extends Exception {
  /**
   * Generated
   */
  private static final long serialVersionUID = 1043429702099805426L;

  /**
   * Delegates implementation to super class
   *
   * @param message
   * @param exception
   */
  public MappingNotDefinedException(String message, Throwable exception) {
    super(message, exception);
  }

  /**
   * Delegates implementation to super class
   *
   * @param message
   */
  public MappingNotDefinedException(String message) {
    super(message);
  }
}
