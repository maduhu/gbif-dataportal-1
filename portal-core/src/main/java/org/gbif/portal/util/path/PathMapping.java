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

package org.gbif.portal.util.path;

import java.util.List;


/**
 * Bean to store information on mappings between REST URLs and maps of properties
 *
 * @author dhobern
 */
public class PathMapping {

  /**
   * String identifying final section of URL prior to section to rewrite (without slashes).
   * <p/>
   * For example, consider URL rewriting between the forms:
   * http://localhost/webapp/servlet/a/b/c
   * http://localhost/webapp/servlet?alpha=a&second=b&value=c
   * <p/>
   * In this case urlRoot could be "servlet"
   */
  protected String urlRoot;

  /**
   * List of patterns supported by URL mapping, each pattern being a list of Strings to be
   * mapped against the URL
   * <p/>
   * For example, consider URL rewriting between the forms:
   * http://localhost/webapp/servlet/a/b/c
   * http://localhost/webapp/servlet?alpha=a&second=b&value=c
   * <p/>
   * In this case supportedPatters could include the list ("alpha", "second", "value").
   */
  protected List<List<String>> supportedPatterns;

  /**
   * @return the supportedPatterns
   */
  public List<List<String>> getSupportedPatterns() {
    return supportedPatterns;
  }

  /**
   * @param supportedPatterns the supportedPatterns to set
   */
  public void setSupportedPatterns(List<List<String>> supportedPatterns) {
    this.supportedPatterns = supportedPatterns;
  }

  /**
   * @return the urlRoot
   */
  public String getUrlRoot() {
    return urlRoot;
  }

  /**
   * @param urlRoot the urlRoot to set
   */
  public void setUrlRoot(String urlRoot) {
    this.urlRoot = urlRoot;
  }

}
