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
package org.gbif.portal.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.dto.util.TaxonRankType;

/**
 * Wraps the content with a span if the concept is a genus or species.
 * 
 * @author dmartin
 */
public class TaxonPrettyPrintTag extends TagSupport {

	private static final long serialVersionUID = 7479013583350496209L;

	protected BriefTaxonConceptDTO concept;

	protected boolean printName = true;

	protected String cssClass="genera";
	
	/**
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		
		if(concept==null)
			return Tag.EVAL_BODY_INCLUDE;
		
		StringBuffer sb = new StringBuffer();
		TaxonRankType rankType = TaxonRankType.getRank(concept.getRank());
		if(rankType !=null && rankType.getValue()>=TaxonRankType.GENUS.getValue()){
			sb.append("<span class=\"");
			sb.append(cssClass);
			sb.append("\">");
		}
		try {
			pageContext.getOut().write(sb.toString());
		} catch (IOException e) {
			throw new JspException(e);
		}
		return Tag.EVAL_BODY_INCLUDE;
	}	
	
	/**
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		if(concept==null)
			return Tag.EVAL_PAGE;
		
		StringBuffer sb = new StringBuffer();
		if(printName){
			sb.append(concept.getTaxonName());			
		}
		TaxonRankType rankType = TaxonRankType.getRank(concept.getRank());
		if(rankType !=null && rankType.getValue()>=TaxonRankType.GENUS.getValue()){
			sb.append("</span>");
		}
		try {
			pageContext.getOut().write(sb.toString());
		} catch (IOException e) {
			throw new JspException(e);
		}
		return Tag.EVAL_PAGE;
	}

	/**
	 * @param concept the concept to set
	 */
	public void setConcept(BriefTaxonConceptDTO concept) {
		this.concept = concept;
	}

	/**
	 * @param cssClass the cssClass to set
	 */
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	/**
	 * @param printName the printName to set
	 */
	public void setPrintName(boolean printName) {
		this.printName = printName;
	}
}