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
package org.gbif.portal.util.mhf.message;

/**
 * Used to indicate that some unexpected access to a message has been detected
 *
 * @author trobertson
 */
public class MessageAccessException extends Exception {
  /**
   * Generated
   */
  private static final long serialVersionUID = 1133732931996743848L;

  /**
   * Delegates implementation to super class
   *
   * @param message
   * @param exception
   */
  public MessageAccessException(String message, Throwable exception) {
    super(message, exception);
  }

  /**
   * Delegates implementation to super class
   *
   * @param message
   */
  public MessageAccessException(String message) {
    super(message);
  }
}
