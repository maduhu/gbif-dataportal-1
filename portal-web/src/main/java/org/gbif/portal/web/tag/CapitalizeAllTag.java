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
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Custom Capitalize Tag that ignores brackets, braces, full stops etc. 
 * The jakarta string:capitalizeAllWords tag doesnt ignore these chars. 
 *  
 * @author Dave Martin
 */
public class CapitalizeAllTag extends BodyTagSupport {

	private static final long serialVersionUID = -6168419298144706152L;
	protected static Log logger = LogFactory.getLog(CapitalizeAllTag.class);		
	
	protected char[] ignores = new char[]{' ', '.', '(', '[', '{'};
	
	/**
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		
		String string2Capitalize = this.bodyContent.getString();
		if(StringUtils.isNotEmpty(string2Capitalize)){
			try{
				string2Capitalize= string2Capitalize.trim();
				string2Capitalize = string2Capitalize.toLowerCase();
				pageContext.getOut().print(WordUtils.capitalizeFully(string2Capitalize, ignores));
			} catch (IOException e){
				throw new JspException(e);
			}
		}
		return super.doEndTag();
	}
}