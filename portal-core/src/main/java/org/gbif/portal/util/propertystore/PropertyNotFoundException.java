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

import java.util.List;

/**
 * Used to indicate that a property is not declared in the PropertyStore
 *
 * @author trobertson
 */
public class PropertyNotFoundException extends RuntimeException {
  /**
   * Generated
   */
  private static final long serialVersionUID = -3021068230882147947L;

  /**
   * Generates a logable message saying that the property is not found
   *
   * @param namespace That is being located
   */
  public PropertyNotFoundException(String namespace) {
    super("Namespace [" + namespace + "] is not found in the property store");
  }

  /**
   * Generates a logable message saying that the property is not found in the namespace
   *
   * @param namespace That is being used
   * @param property  Within the namespace that couldn't be found
   */
  public PropertyNotFoundException(String namespace, String property) {
    super("Property [" + property + "] is not mapped for namespace [" + namespace + "] in the property store");
  }

  /**
   * Generates a logable message saying that the property is not found in the namespaces
   *
   * @param namespace That is being used
   * @param property  Within the namespace that couldn't be found
   */
  public PropertyNotFoundException(List<String> namespaces, String property) {
    super("Property [" + property + "] is not mapped for namespace list [" + namespaces + "]");
  }
}
