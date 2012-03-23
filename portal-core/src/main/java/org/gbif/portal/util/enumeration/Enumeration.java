package org.gbif.portal.util.enumeration;

import java.util.Map;

/**
 * Base class for enumerations
 *
 * @author Donald Hobern
 */
public class Enumeration {
  private String name;
  private Integer value;

  private Enumeration() {
    // Prevent use
  }

  protected Enumeration(Map<String, Enumeration> nameMap, Map<Integer, Enumeration> valueMap, String name,
                        Integer value) {
    this.name = name;
    this.value = value;
    nameMap.put(name, this);
    valueMap.put(value, this);
  }

  public String getName() {
    return name;
  }

  public Integer getValue() {
    return value;
  }

  public String toString() {
    return name;
  }
}
