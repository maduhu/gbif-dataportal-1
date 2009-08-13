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
 * This tag formats different elements inside a text string:
 * 		- Links: by appending the '<a href="'prefix and '">someName</a>' suffix
 * 
 * @author josecuadra
 */
public class FormatTextTag extends TagSupport{
 

	private static final long serialVersionUID = 3161689558535927050L;
	/** Logger*/
	private static Log logger = LogFactory.getLog(FormatTextTag.class);	
	/**The scientific name from the record**/
	protected String content;
	
	//Elements for formatting an URL
	protected String urlPrefix = "<a href=\"";
	protected String urlSuffix = "\">";
	protected String urlEndTag = "</a>";
	protected String urlCompletePattern = "http://";
	protected String urlSimplePattern = "www.";
	protected char[] urlDelimiters = new char[]{' ', '(',')', '[', ']', '{', '}'};


	/**
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	public int doStartTag() throws JspException {
		
		String output = "";
		output = formatLink();
					
		try {
			pageContext.getOut().print(output.toString());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new JspException(e);
		}
		return SKIP_BODY;
	}
	
	public String formatLink() {
		
		int currentPos=0;
		int startPos=0;
		int endPos=0;
		String tempUrl;
		String tempLink;
		
		StringBuffer output=new StringBuffer();
				
		while( (startPos = content.indexOf(urlCompletePattern, currentPos)) != -1 ) {
			endPos = delimitedString(content, startPos);
			tempUrl = content.substring(startPos, endPos);
			tempLink = urlPrefix+tempUrl+urlSuffix+tempUrl+urlEndTag;
			//append the text before the link ref
			output.append(content.substring(currentPos, startPos));
			//append the formatted link
			output.append(tempLink);
			currentPos = endPos;
		}
		output.append(content.substring(endPos, content.length()));
		
		return output.toString();
	}
	
	public int delimitedString(String tempUrl, int startPos) {
		int firstDelimiter=tempUrl.length(); //index of the appearance of the first delimiter
		for(char delimiter: urlDelimiters) {
			if(tempUrl.indexOf(delimiter,startPos)!=-1) {
				int indexof = tempUrl.indexOf(delimiter,startPos);
				if(indexof<firstDelimiter)
					firstDelimiter=indexof;
			}
		}
		return firstDelimiter;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}


	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}	
}