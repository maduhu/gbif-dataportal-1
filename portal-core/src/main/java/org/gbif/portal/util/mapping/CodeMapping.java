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
 * This interface supports mappings between arbitrary string values and a set of
 * defined codes.
 * <p/>
 * Implementations could include:
 * <ul>
 * <li>MapCodeMapping with mappings provided via a Map</li>
 * <li>DBCodeMapping with mappings stored in a database table</li>
 * </ul>
 *
 * @author dhobern
 */
public interface CodeMapping {
  /**
   * Map supplied string to a code value
   *
   * @param raw String to be mapped
   * @return Code value corresponding to raw string
   * @throws NoMappingDefinedException If no mapping exists for the value
   */
  Integer mapToCode(String raw) throws MappingNotDefinedException;
}
