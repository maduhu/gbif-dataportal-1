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
import java.util.Calendar;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Tag for displaying the current date in a given format
 * @author josecuadra
 */
public class CurrentDateTag extends TagSupport{
 

	private static final long serialVersionUID = 3161689558535927050L;
	/** Logger*/
	private static Log logger = LogFactory.getLog(CurrentDateTag.class);	


	/**
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	public int doStartTag() throws JspException {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(System.currentTimeMillis()));
		
		StringBuffer dateBuffer = new StringBuffer();
		dateBuffer.append(calendar.get(Calendar.YEAR)); 
		dateBuffer.append("-");
		// NB add one to zero-based month
		if(calendar.get(Calendar.MONTH) < 9) {
			dateBuffer.append("0");
		}
		dateBuffer.append(calendar.get(Calendar.MONTH) + 1); 
		dateBuffer.append("-");
		if(calendar.get(Calendar.DAY_OF_MONTH) < 10) {
			dateBuffer.append("0");
		}
		dateBuffer.append(calendar.get(Calendar.DAY_OF_MONTH));
		
		try {
			pageContext.getOut().print(dateBuffer);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new JspException(e);
		}
		return SKIP_BODY;	
	}	
}