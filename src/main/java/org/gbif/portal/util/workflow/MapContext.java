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

import java.util.HashMap;

/**
 * A Map implementation to allow any data be assigned to the context
 *
 * @author trobertson
 */
public class MapContext extends HashMap implements ProcessContext {
  /**
   * Generated
   */
  private static final long serialVersionUID = 5876699973037309771L;
  /**
   * Indicates that something has decided the process should stop
   */
  protected boolean stopProcess;

  public boolean isStopProcess() {
    return stopProcess;
  }

  /**
   * @param key             To locate the object of interest
   * @param expectedType    Of the context object
   * @param exceptionIfNull If true, will throw an exception if the
   *                        context does not have the object
   * @return the object of interest
   */
  @SuppressWarnings("unchecked")
  public Object get(String key, Class expectedType, boolean exceptionIfNull) throws ContextCorruptException {
    if (key == null && exceptionIfNull) {
      throw new ContextCorruptException("Usage of MapContext does not support null keys");
    }
    if (key == null) {
      return null; // basically swallow the user error
    }
    Object contextObject = get(key);
    if (exceptionIfNull && contextObject == null) {
      throw new ContextCorruptException("Expecting an object keyed[" + key + "] but was null");
    }
    if (contextObject == null) {
      return null;
    }
    if (!expectedType.isAssignableFrom(contextObject.getClass())) {
      throw new ContextCorruptException("Expecting context object to be of type[" + expectedType + "] but was of " +
        "type[" + contextObject.getClass() + "]");
    }
    return get(key);
  }

  public void setStopProcess(boolean stopProcess) {
    this.stopProcess = stopProcess;
  }
}
