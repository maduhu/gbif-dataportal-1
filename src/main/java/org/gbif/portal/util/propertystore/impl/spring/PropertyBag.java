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
package org.gbif.portal.util.propertystore.impl.spring;

import org.springframework.beans.factory.BeanNameAware;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents all the the Properties within a namespace
 *
 * @author trobertson
 */
public class PropertyBag implements BeanNameAware {

  /**
   * The id/name of this property bag. Nullable.
   */
  protected String name;

  /**
   * The namespace that the concepts exist in
   */
  protected String namespace;

  /**
   * The properties that exist within this namespace
   */
  protected Map<String, Object> properties = new HashMap<String, Object>();

  /**
   * @return Returns the namespace.
   */
  public String getNamespace() {
    return namespace;
  }

  /**
   * @param namespace The namespace to set.
   */
  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  /**
   * A utility for retreiving a defined property from the collection for the
   * given key
   *
   * @param key To locate the Property with
   * @return The Object for the key or null if not found
   */
  public Object getProperty(String key) {
    return getProperties().get(key);
  }

  /**
   * @return Returns the properties.
   */
  public Map<String, Object> getProperties() {
    return properties;
  }

  /**
   * @param properties The properties to set.
   */
  public void setProperties(Map<String, Object> properties) {
    this.properties = properties;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @see org.springframework.beans.factory.BeanNameAware#setBeanName(java.lang.String)
   */
  public void setBeanName(String beanName) {
    this.name = beanName;
  }
}
