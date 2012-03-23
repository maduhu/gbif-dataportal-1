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
package org.gbif.portal.util.workflow;

import java.util.Map;

/**
 * @author trobertson
 *         [based on the article by Steve Dodge: http://www.javaworld.com/javaworld/jw-04-2005/jw-0411-spring.html]
 */
public interface ProcessContext extends Map {

  /**
   * @param key
   * @param expectedType
   * @param exceptionIfNull
   * @return
   * @throws ContextCorruptException
   */
  Object get(String key, Class expectedType, boolean exceptionIfNull) throws ContextCorruptException;

  /**
   * Actively informs the workflow process to stop processing
   * no further activities will be exeecuted
   *
   * @return
   */
  boolean isStopProcess();

  /**
   * Sets the indication to stop processing at the next available point
   *
   * @param stopProcess To set
   */
  void setStopProcess(boolean stopProcess);
}
