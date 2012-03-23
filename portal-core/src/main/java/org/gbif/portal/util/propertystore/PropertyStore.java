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
 * The property store is a utility for retreiving an object
 * located by a key and a namespace.
 * <p/>
 * The property returned can be of any type and it is expected
 * that a more specific layer be developed on top of the property
 * store to make a more useful API.
 * <p/>
 * For example, a message utils may exist to pull values from a
 * message based on the namespace and key
 *
 * @author Tim Robertson
 */
public interface PropertyStore {
  /**
   * Returns the property defined for the given namespace and key
   *
   * @param namespace Within which the key is defined
   * @param key       The unique key within the namespace
   * @return The object that is defined by the key within the namespace
   * @throws PropertyNotFoundException If the Property Store does not find the
   *                                   namespace or the key within the namespace
   */
  Object getProperty(String namespace, String key) throws PropertyNotFoundException;

  /**
   * Returns the property defined for the given namespace and key, with checking on
   * the returned Objects type.
   *
   * @param namespace    Within which the key is defined
   * @param key          The unique key within the namespace
   * @param expectedType To check against
   * @return The object that is defined by the key within the namespace
   * @throws PropertyNotFoundException      If the Property Store does not find the
   *                                        namespace or the key within the namespace
   * @throws MisconfiguredPropertyException If the Property Store holds an object of
   *                                        unepxected type for the provided key and namespace
   */
  Object getProperty(String namespace, String key, Class expectedType) throws PropertyNotFoundException,
    MisconfiguredPropertyException;

  /**
   * Returns the property defined for the given namespace list and key
   * Each namespace is tested in turn and the first found is returned
   *
   * @param namespaces Within which the key is defined.  The property
   *                   may exist in multiple namespaces, but the first in the list is honoured
   * @param key        The unique key within the namespace
   * @return The object that is defined by the key within the first namespace it is
   *         found in
   * @throws PropertyNotFoundException If the Property Store does not find the
   *                                   namespace or the key within the namespace
   */
  Object getProperty(List<String> namespaces, String key) throws PropertyNotFoundException;

  /**
   * Returns the property defined for the given namespaces and key, with checking on
   * the returned Objects type. The namespaces are checked in order and the first namespace
   * found that holds the property is used
   *
   * @param namespaces   Within which the key is defined
   * @param key          The unique key within the namespace
   * @param expectedType To check against
   * @return The object that is defined by the key within the first namespace it is found in
   * @throws PropertyNotFoundException      If the Property Store does not find the
   *                                        namespace or the key within the namespace
   * @throws MisconfiguredPropertyException If the Property Store holds an object of
   *                                        unepxected type for the provided key and namespace that it is found in
   */
  Object getProperty(List<String> namespaces, String key, Class expectedType) throws
    PropertyNotFoundException, MisconfiguredPropertyException;

  /**
   * Determines if the keyed property is mapped for the given namespace
   *
   * @param namespace Within which the key is defined
   * @param key       The unique key within the namespace
   * @return The object that is defined by the key within the namespace
   */
  boolean propertySupported(String namespace, String key);

  /**
   * Determines if the keyed property is mapped for the given namespace
   *
   * @param namespaces Within which the key is defined
   * @param key        The unique key within the namespace
   * @return The object that is defined by the key within the namespace
   */
  boolean propertySupported(List<String> namespaces, String key);
}
