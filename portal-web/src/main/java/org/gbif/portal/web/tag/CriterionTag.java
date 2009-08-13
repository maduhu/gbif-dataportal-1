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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.web.filter.CriteriaUtil;
/**
 * A tag that is nested within a FilterLinkTag that holds a criterion
 * for a search.
 *
 * @author Dave Martin
 * @todo this tag should be able to take a filter name and translate into
 * a filter id - making the jsps easier to read
 */
public class CriterionTag extends TagSupport{

	private static final long serialVersionUID = 3085655637275045871L;
	
	protected static Log log = LogFactory.getLog(CriterionTag.class);
	protected String subject;
	protected String predicate;
	protected String value;
	protected int index=0;
	protected boolean urlEncode = false;
	
	@Override
	public int doStartTag() throws JspException {
		try{
			String criterion = CriteriaUtil.getUrl(subject, predicate, value, index);
			if(urlEncode)
				criterion = URLEncoder.encode(criterion, "UTF-8");
			pageContext.getOut().print(criterion);
		} catch (IOException e){
			throw new JspException(e);
		}
		return SKIP_BODY;
	}

	/**
	 * @param predicate the predicate to set
	 */
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @param urlEncode the urlEncode to set
	 */
	public void setUrlEncode(boolean urlEncode) {
		this.urlEncode = urlEncode;
	}	
}