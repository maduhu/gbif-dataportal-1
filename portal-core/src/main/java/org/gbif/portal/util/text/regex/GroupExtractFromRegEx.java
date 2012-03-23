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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility that will test a String against a configured regular expression,
 * and should it pass it will attempt to extract the groups and return a string
 * representing the desired groups.
 * <p/>
 * For example:
 * Puma concolor (L. 1771) concolor
 * <p/>
 * Can be extracted with the correct RE and groups to return
 * Puma concolor concolor
 *
 * @author trobertson
 */
public class GroupExtractFromRegEx {
  /**
   * Logger
   */
  protected static Log log = LogFactory.getLog(GroupExtractFromRegEx.class);

  /**
   * The regular expression
   */
  protected String regex;

  /**
   * The compiled pattern for the regex
   */
  private Pattern pattern;

  /**
   * The groups of interest to extract
   */
  protected List<Integer> groupsToExtract;

  /**
   * Attempts to extract the desired groups from the text
   * according to the configured regex.
   * <p/>
   * Errors are swallowed and logged if a misconfiguration is found
   * <p/>
   * For example:
   * Puma concolor (L. 1771) concolor
   * <p/>
   * Can be extracted with the correct RE and groups to return
   * Puma concolor concolor
   *
   * @param test To extract
   * @return The String representing the groups or null
   */
  public String extract(String test) {
    Matcher matcher = pattern.matcher(test);
    try {
      if (matcher.matches()) {
        StringBuilder sb = new StringBuilder();
        for (Integer groupOfInterest : groupsToExtract) {
          if (log.isDebugEnabled()) {
            log.debug("extracting: " + groupOfInterest);
            for (int i = 0; i <= matcher.groupCount(); i++) {
              log.debug(i + ": " + matcher.group(i));
            }
          }
          // only append found groups (some may be optional in the regex)
          if (matcher.group(groupOfInterest.intValue()) != null) {
            sb.append(matcher.group(groupOfInterest.intValue()));
            sb.append(" ");
          }
        }
        return sb.toString().trim();
      } else {
        if (log.isDebugEnabled()) {
          log.debug("No match from [" + test +
            "] using regex[ " + getRegex() + "]");
        }
      }
    } catch (RuntimeException e) {
      log.error("Error extracting the groups [" + groupsToExtract + "] from [" + test +
        "] using regex[" + getRegex() + "] - returning null", e);
    }
    return null;
  }

  /**
   * @return Returns the groupsToExtract.
   */
  public List<Integer> getGroupsToExtract() {
    return groupsToExtract;
  }

  /**
   * @param groupsToExtract The groupsToExtract to set.
   */
  public void setGroupsToExtract(List<Integer> groupsToExtract) {
    this.groupsToExtract = groupsToExtract;
  }

  /**
   * @return Returns the pattern.
   */
  public Pattern getPattern() {
    return pattern;
  }

  /**
   * @return Returns the regex.
   */
  public String getRegex() {
    return regex;
  }

  /**
   * @param regex The regex to set.
   */
  public void setRegex(String regex) {
    this.regex = regex;
    pattern = Pattern.compile(regex);
  }
}
