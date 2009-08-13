/***************************************************************************
 * Copyright (C) 2006 Global Biodiversity Information Facility Secretariat.
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
package org.gbif.portal.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Simple tag for highlighting sections of a string. Wraps the supplied keyword with a span. 
 * @author dmartin
 */
public class HighlightTag extends BodyTagSupport{

	private static final long serialVersionUID = -7507209235604350913L;
	/** Logger*/
	private static Log logger = LogFactory.getLog(HighlightTag.class);	
	/**The keyword to highlight**/
	protected String keyword;
	/**The CSS class of the span**/
	protected String cssClass;
	/** Whether to match any part or just words starting with. defaults to false */
	protected boolean matchAnyPart = false;

	/**
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	public int doEndTag() throws JspException {

		BodyContent bodyContent = getBodyContent();
		String body = bodyContent.getString();		

		try {
			if(keyword==null || keyword.length()==0){
				pageContext.getOut().print(body);
			} else if (body!=null){
				pageContext.getOut().print(highlight(body, keyword, cssClass));
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new JspException(e);
		}
		return SKIP_BODY;
	}
	
	/**
	 * Wraps the keyword in spans if present in the supplied body
	 * @param body
	 * @param keyword
	 * @param cssClass
	 * @return
	 */
	public String highlight(String body, String keyword, String cssClass){
		body = body.trim();
		if(logger.isDebugEnabled())
			logger.debug("body = "+ body);
		keyword = keyword.replaceAll("\\Q*\\E|%", "");
		if(logger.isDebugEnabled())
			logger.debug("after keyword wildcards  = "+ keyword);
		String uppercaseBody = body.toUpperCase();
		if(logger.isDebugEnabled())
			logger.debug("upper case body: "+uppercaseBody);
		
		int startIndex = -1;
		if(uppercaseBody.startsWith(keyword.toUpperCase())){
			startIndex=0;
		} else {
			if(matchAnyPart){
				startIndex = uppercaseBody.indexOf(keyword.toUpperCase());
			} else {
				startIndex = uppercaseBody.indexOf(" "+keyword.toUpperCase());
				if(startIndex!=-1)
					startIndex++;				
			}
		}
		
		
		if(startIndex!=-1){
			String newBody = 
				"<span class=\""+cssClass+"\">"
				+ body.substring(startIndex, startIndex+keyword.length())
				+"</span><span>"
				+ body.substring(startIndex+keyword.length())
				+"</span>";
			if(startIndex!=0){
				newBody = "<span>"+body.substring(0, startIndex)+"</span>"+newBody;
			}
			if(logger.isDebugEnabled())
				logger.debug("new body = "+ newBody);	
			return newBody;
		} else {
			return body;
		}
	}

	/**
	 * @return the cssClass
	 */
	public String getCssClass() {
		return cssClass;
	}

	/**
	 * @param cssClass the cssClass to set
	 */
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	/**
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * @param keyword the keyword to set
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	/**
	 * @return the matchAnyPart
	 */
	public boolean isMatchAnyPart() {
		return matchAnyPart;
	}
	
	/**
	 * @return the matchAnyPart
	 */
	public boolean getMatchAnyPart() {
		return matchAnyPart;
	}	

	/**
	 * @param matchAnyPart the matchAnyPart to set
	 */
	public void setMatchAnyPart(boolean matchAnyPart) {
		this.matchAnyPart = matchAnyPart;
	}
}