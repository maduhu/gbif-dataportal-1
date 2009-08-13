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
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Tag for checking duplication of author names when scientific names are displayed
 * @author josecuadra
 */
public class DuplicateAuthorNamesTag extends TagSupport{
 

	private static final long serialVersionUID = 3161689558535927050L;
	/** Logger*/
	private static Log logger = LogFactory.getLog(DuplicateAuthorNamesTag.class);	
	/**The scientific name from the record**/
	protected String scientificName;
	/**The author name from the record**/
	protected String authorName;
	/**The CSS class of the span**/
	protected String cssClass;	

	/**
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	public int doStartTag() throws JspException {

		String nameDisplayed;

		if(authorName==null) {
			nameDisplayed =
				"<span class=\""+cssClass+"\">"
				+ scientificName
				+"</span>";
		}
		else if(authorName.length()==0) {
			nameDisplayed =
				"<span class=\""+cssClass+"\">"
				+ scientificName
				+"</span>";
		}
		else {
			
			scientificName = scientificName.trim();
			authorName = authorName.trim();
			
			if(logger.isDebugEnabled()) {
				logger.debug("scientificName = " + scientificName);
				logger.debug("authorName = " + authorName);
			}
				
			int indexOfName = scientificName.indexOf(authorName);
			if(indexOfName==-1) {
				nameDisplayed =
					"<span class=\""+cssClass+"\">"
					+ scientificName
					+"</span>"
					+" "
					+ authorName;
			}
			else{
				nameDisplayed =
					"<span class=\""+cssClass+"\">"
					+ scientificName
					+"</span>";
			}			
		}		
			
		try {
			pageContext.getOut().print(nameDisplayed);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new JspException(e);
		}
		return SKIP_BODY;
	}	


	/**
	 * @return the scientificName
	 */
	public String getScientificName() {
		return scientificName;
	}

	/**
	 * @param scientificName the scientificName to set
	 */
	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}

	/**
	 * @return the authorName
	 */
	public String getAuthorName() {
		return authorName;
	}

	/**
	 * @param authorName the authorName to set
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
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
	
	
	

	
	
}