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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibm.icu.util.StringTokenizer;

/**
 * Simple tag that inserts line breaks into lone strings.
 * 
 * @author dmartin
 */
public class LineBreakTag extends BodyTagSupport {

	private static final long serialVersionUID = 4344388014706769204L;

	protected static Log logger = LogFactory.getLog(LineBreakTag.class);	

	/** When to explicitly break the line */	
	protected int maxLengthBeforeBreak = 15;
	/** The line break to insert */
	protected String lineBreak = "<br/>";
	/** Chars to add a break after */
	protected char[] breakAfterChars = new char[]{',', ';'};
	/** Whether to break after specified chars */
	protected boolean useBreakAfterChars = true;
	/**
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		
		String bodyContentAsString = getBodyContent().getString();
		StringBuffer sb = new StringBuffer();
		
		StringTokenizer st = new StringTokenizer(bodyContentAsString, " ");
		List<String> tokens = new ArrayList<String>();
		while(st.hasMoreTokens()){
			tokens.add(st.nextToken());
		}
		
		try{
			
			for(String token: tokens){
				char lastChar = (char)-1;
				if(sb.length()>0)
					lastChar = sb.charAt(sb.length()-1);
				
				if(sb.length()>maxLengthBeforeBreak 
						|| (
							sb.length()>0 &&
							Arrays.binarySearch(breakAfterChars, lastChar) >0
						)
					){
					pageContext.getOut().print(sb.toString());
					pageContext.getOut().print(lineBreak);
					sb = new StringBuffer(token);
				} else {
					if(sb.length()>0)
						sb.append(" ");
					sb.append(token);
				}
			}
			pageContext.getOut().print(sb.toString());
		} catch(Exception e){
			logger.debug(e.getMessage(), e);
			throw new JspException(e);
		}
		return super.doStartTag();
	}

	/**
	 * @param maxLengthBeforeBreak the maxLengthBeforeBreak to set
	 */
	public void setMaxLengthBeforeBreak(int maxLengthBeforeBreak) {
		this.maxLengthBeforeBreak = maxLengthBeforeBreak;
	}

	/**
	 * @param lineBreak the lineBreak to set
	 */
	public void setLineBreak(String lineBreak) {
		this.lineBreak = lineBreak;
	}
}