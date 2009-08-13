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
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.gbif.portal.web.filter.CriteriaDTO;
import org.gbif.portal.web.filter.CriteriaUtil;
import org.gbif.portal.web.filter.CriterionDTO;

/**
 * Simple tag that dumps out a criteria url from a CriteriaDTO or a list
 * of Criterion.
 * 
 * @author dmartin
 */
public class CriteriaTag extends TagSupport{

	private static final long serialVersionUID = 3742362192193275846L;

	/**the criteria to produce a url from **/
	protected Object criteria;
	
	protected boolean urlEncode = false;
	
	/**
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@SuppressWarnings("unchecked")
	public int doStartTag() throws JspException {
		try{
			String criteriaString = null;
			
			if(criteria instanceof CriteriaDTO) {
				criteriaString = CriteriaUtil.getUrl( ((CriteriaDTO) criteria));
			} else if (criteria instanceof List){
				criteriaString = CriteriaUtil.getUrlFromList( (List<CriterionDTO>) criteria);
			}	
			
			if(urlEncode){
				criteriaString = URLEncoder.encode(criteriaString, "UTF-8");
			}
			
			pageContext.getOut().print(criteriaString);
		} catch (IOException e){
			throw new JspException(e);
		}
		return EVAL_PAGE;
	}

	/**
	 * @param criteria the criteria to set
	 */
	public void setCriteria(Object criteria) {
		this.criteria = criteria;
	}

	/**
	 * @param urlEncode the urlEncode to set
	 */
	public void setUrlEncode(boolean urlEncode) {
		this.urlEncode = urlEncode;
	}
}