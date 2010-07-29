/***************************************************************************
 * Copyright (C) 2005 Global Biodiversity Information Facility Secretariat. All Rights Reserved. The contents of this
 * file are subject to the Mozilla Public License Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at http://www.mozilla.org/MPL/ Software distributed
 * under the License is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and limitations under the License.
 ***************************************************************************/
package org.gbif.portal.web.tag;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Simple tag that prints out a list of alphabetical links.
 * 
 * @author Dave Martin
 */
public class AlphabetLinkTag extends TagSupport {

  private static final long serialVersionUID = 2671584462097497344L;
  protected static Log logger = LogFactory.getLog(AlphabetLinkTag.class);
  /** The root url */
  protected String rootUrl;
  /** The selected char */
  protected char selected;
  /** Upper case or lower */
  protected boolean lowerCase = false;
  /** The class name of the links */
  protected String listClass = "alphalist";
  /** The class name of the links */
  protected String linkClass = "alphalink";
  /** A comma separated list of chars to skip */
  protected String ignores;
  /** A comma separated list of characters to add */
  protected List<Character> letters;

  private String[] accepted = {
      "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
      "W", "X", "Y", "Z"};

  /**
   * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
   */
  @Override
  public int doStartTag() throws JspException {
    if (letters == null || letters.isEmpty()) {
      return super.doStartTag();
    }

    StringBuffer sb = new StringBuffer();
    HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    String contextPath = request.getContextPath();
    List<Character> ignoreChars = new ArrayList<Character>();

    if (StringUtils.isNotEmpty(ignores)) {
      ignores = ignores.trim();
      StringTokenizer st = new StringTokenizer(ignores);
      while (st.hasMoreTokens()) {
        String ignoreChar = st.nextToken();
        if (ignoreChar.length() != 1) {
          throw new JspException("Invalid ignore list :" + ignoreChar);
        }
        ignoreChars.add(new Character(ignoreChar.charAt(0)));
      }
    }

    sb.append("<ul class=\"");
    sb.append(listClass);
    sb.append("\">");
    sb.append("<li class=\"lt\">");

    int indexOfSelected = letters.indexOf(selected);

    if (indexOfSelected > 0) {
      addLink(sb, contextPath, letters.get(indexOfSelected - 1), "&lt;&lt;");
    } else {
      sb.append("&lt;&lt;");
    }
    sb.append("</li>");

    List acceptedChars = Arrays.asList(accepted);
    for (Character letter : letters) {
      if (!ignoreChars.contains(letter) && acceptedChars.contains(letter.toString())) {
        sb.append("<li");
        if (selected != letter) {
          sb.append('>');
          addLink(sb, contextPath, letter);
        } else {
          sb.append(" id=\"chosen\">");
          sb.append(letter);
        }
        sb.append("</li>");
      }
    }

    sb.append("<li class=\"lt\">");

    if (indexOfSelected < (letters.size() - 1)) {
      addLink(sb, contextPath, letters.get(indexOfSelected + 1), "&gt;&gt;");
    } else {
      sb.append("&gt;&gt;");
    }
    sb.append("</li>");
    sb.append("</ul>");

    try {
      pageContext.getOut().write(sb.toString());
    } catch (IOException e) {
      throw new JspException(e);
    }
    return super.doStartTag();
  }

  /**
   * @param ignores the ignores to set
   */
  public void setIgnores(String ignores) {
    this.ignores = ignores;
  }

  /**
   * @param letters the letters to set
   */
  public void setLetters(List<Character> letters) {
    this.letters = letters;
  }

  /**
   * @param linkClass the linkClass to set
   */
  public void setLinkClass(String linkClass) {
    this.linkClass = linkClass;
  }

  /**
   * @param listClass the listClass to set
   */
  public void setListClass(String listClass) {
    this.listClass = listClass;
  }

  /**
   * @param lowerCase the lowerCase to set
   */
  public void setLowerCase(boolean lowerCase) {
    this.lowerCase = lowerCase;
  }

  /**
   * @param rootUrl the rootUrl to set
   */
  public void setRootUrl(String rootUrl) {
    this.rootUrl = rootUrl;
  }

  /**
   * @param selected the selected to set
   */
  public void setSelected(char selected) {
    this.selected = selected;
  }

  void addLink(StringBuffer sb, String contextPath, char letter) throws JspException {
    sb.append("<a href=\"");
    sb.append(contextPath);
    sb.append(rootUrl);
    if (logger.isDebugEnabled()) {
      logger.debug("Character to render:" + letter + ", unicode value:" + ((int) letter));
    }
    try {
      String urlEncodedChar = URLEncoder.encode(String.valueOf(letter), "UTF-8");
      if (!urlEncodedChar.equals(String.valueOf(letter))) {
        sb.append((int) letter);
      } else {
        sb.append(letter);
      }
    } catch (Exception e) {
      throw new JspException(e);
    }
    sb.append("\" class=\"");
    sb.append(linkClass);
    sb.append("\">");
    sb.append(letter);
    sb.append("</a>");
  }

  void addLink(StringBuffer sb, String contextPath, char letter, String displayedChar) throws JspException {
    sb.append("<a href=\"");
    sb.append(contextPath);
    sb.append(rootUrl);
    try {
      sb.append(URLEncoder.encode(String.valueOf(letter), "UTF-8"));
    } catch (Exception e) {
      throw new JspException(e);
    }
    sb.append("\" class=\"");
    sb.append(linkClass);
    sb.append("\">");
    sb.append(displayedChar);
    sb.append("</a>");
  }
}