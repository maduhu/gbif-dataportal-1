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
package org.gbif.portal.util.mapping.impl;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.util.mapping.CodeMapping;
import org.gbif.portal.util.mapping.MappingNotDefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple map-based implementation of CodeMapping.
 *
 * @author dhobern
 */
public class MapCodeMapping implements CodeMapping {
  /**
   * Map of values to Integer codes.
   */
  protected Map<String, Integer> map = new HashMap<String, Integer>();

  /**
   * Default code if not found in map.
   */
  protected Integer defaultCode = null;

  /**
   * Default constructor
   */
  public MapCodeMapping() {
  }

  /**
   * Map supplied string to a code value
   *
   * @param raw String to be mapped
   * @return Code value corresponding to raw string
   * @throws NoMappingDefinedException If no mapping exists for the value
   */
  public Integer mapToCode(String raw) throws MappingNotDefinedException {
    Integer code = null;

    String processed = StringUtils.trimToNull(raw);

    if (processed != null) {
      processed = processed.replaceAll(" ", "").toUpperCase();
      code = (Integer) map.get(processed);
    }

    if (code == null) {
      code = defaultCode;
    }

    if (code == null) {
      throw new MappingNotDefinedException("No mapping for raw string \"" + raw + "\"");
    }

    return code;
  }

  /**
   * @return the defaultCode
   */
  public Integer getDefaultCode() {
    return defaultCode;
  }

  /**
   * @param defaultCode the defaultCode to set
   */
  public void setDefaultCode(Integer defaultCode) {
    this.defaultCode = defaultCode;
  }

  /**
   * @return the map
   */
  public Map<String, Integer> getMap() {
    return map;
  }

  /**
   * @param map the map to set
   */
  public void setMap(Map<String, Integer> map) {
    this.map = map;
  }
}
