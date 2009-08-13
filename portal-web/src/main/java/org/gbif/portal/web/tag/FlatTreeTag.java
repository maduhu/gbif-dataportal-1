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
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.dto.util.TaxonRankType;

/**
 * Simple tag that takes a taxatree and writes it out in an unordered list
 * 
 * @author davemartin
 * @author trobertson
 */
public class FlatTreeTag extends TagSupport {
	
	private static final long serialVersionUID = -1305918203551260073L;
	protected static Log log = LogFactory.getLog(FlatTreeTag.class);	
	
	/**The list of concepts to write out, ordered by rank**/
	private List<BriefTaxonConceptDTO> concepts;
	/**The selected concept**/
	private BriefTaxonConceptDTO selectedConcept;
	/** The classname for the UL tag**/
	private String classname;
	
	protected String generaClassName = "genera";
	protected String speciesUrlPrefix = "/species/";
	
	/**
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		StringBuffer sb = new StringBuffer();
		
		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
		String contextPath = request.getContextPath();
		
		if (concepts!=null && concepts.size()>0){
			if (StringUtils.isNotEmpty(classname)) {
				sb.append("<ul class='");
				sb.append(classname);
				sb.append("'> ");				
			} else {
				sb.append("<ul>");
			}
			
			Iterator<BriefTaxonConceptDTO> iter = concepts.iterator();
			
			while (iter.hasNext()){
				BriefTaxonConceptDTO conceptDTO = iter.next();
				sb.append("<li>");
				sb.append(StringUtils.capitalize(conceptDTO.getRank()));
				sb.append(": ");			
				boolean isSelectedConcept = false;
				if(selectedConcept!=null)
					isSelectedConcept = conceptDTO.getKey().equals(selectedConcept.getKey());
				if(!isSelectedConcept){
					sb.append("<a href=\"");
  		    sb.append(contextPath);		
  		    sb.append(speciesUrlPrefix);		
					sb.append(conceptDTO.getKey());
					sb.append("\">");		
				}
				TaxonRankType rankType = TaxonRankType.getRank(conceptDTO.getRank());
				if(rankType!=null && rankType.getValue()>= TaxonRankType.GENUS.getValue()){
					sb.append("<span class=\"");
					sb.append(generaClassName);
					sb.append("\">");
					sb.append(conceptDTO.getTaxonName());
					sb.append("</span>");
				} else {
					sb.append(conceptDTO.getTaxonName());
				}
				if(!isSelectedConcept)			
					sb.append("</a>");
				sb.append("</li>");
			}
			sb.append("</ul>");
		}
		try {
			this.pageContext.getOut().println(sb.toString());
		} catch (IOException e){
			log.error(e.getMessage(), e);
		}
		return SKIP_BODY;
	}

	/**
	 * @param concepts the concepts to set
	 */
	public void setConcepts(List<BriefTaxonConceptDTO> concepts) {
		this.concepts = concepts;
	}

	/**
	 * @param selectedConcept the selectedConcept to set
	 */
	public void setSelectedConcept(BriefTaxonConceptDTO selectedConcept) {
		this.selectedConcept = selectedConcept;
	}

	/**
	 * @param classname The classname to set.
	 */
	public void setClassname(String classname) {
		this.classname = classname;
	}
}