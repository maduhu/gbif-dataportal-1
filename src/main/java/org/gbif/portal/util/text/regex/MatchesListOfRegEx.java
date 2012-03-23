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
package org.gbif.portal.util.text.regex;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility that will test a String against a list of configured regular expressions,
 * and determine if it matches a known type
 *
 * @author trobertson
 */
public class MatchesListOfRegEx {
  /**
   * Logger
   */
  protected static Log log = LogFactory.getLog(MatchesListOfRegEx.class);

  /**
   * The list of valid expressions
   */
  protected List<String> validFormats;

  /**
   * The list of valid expressions as a compiled set of patterns
   */
  private List<Pattern> validPatterns;

  /**
   * Compares the test String against the known formats
   *
   * @param test To match against known formats
   * @return True if the format is known, false otherwise
   */
  public boolean matches(String test) {
    for (Pattern pattern : getValidPatterns()) {
      Matcher matcher = pattern.matcher(test);
      if (matcher.matches()) {
        return true;
      }
    }
    return false;
  }

  /**
   * @return Returns the validFormats.
   */
  public List<String> getValidFormats() {
    return validFormats;
  }

  /**
   * Set's the formats and compiles them into the validPatterns
   *
   * @param validFormats The validFormats to set.
   */
  public void setValidFormats(List<String> validFormats) {
    this.validFormats = validFormats;
    List<Pattern> compiled = new LinkedList<Pattern>();
    for (String format : validFormats) {
      compiled.add(Pattern.compile(format));
    }
    setValidPatterns(compiled);
  }

  /**
   * @return Returns the validPatterns.
   */
  protected List<Pattern> getValidPatterns() {
    return validPatterns;
  }

  /**
   * @param validPatterns The validPatterns to set.
   */
  protected void setValidPatterns(List<Pattern> validPatterns) {
    this.validPatterns = validPatterns;
  }
}
