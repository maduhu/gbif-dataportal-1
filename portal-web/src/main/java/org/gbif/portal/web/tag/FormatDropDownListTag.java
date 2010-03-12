package org.gbif.portal.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FormatDropDownListTag extends TagSupport{
 

	private static final long serialVersionUID = -8115727364284355169L;
	/** Logger*/
	private static Log logger = LogFactory.getLog(FormatTextTag.class);	
	/**The scientific name from the record**/
	protected String content;
	/**Max length of string value**/
	protected int maxLength;
	/**End string for long string values**/
	protected String end;
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
		output = cutLongValues();
		
						
		try {
			pageContext.getOut().print(output.toString());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new JspException(e);
		}
		return SKIP_BODY;
	}
	
	public String cutLongValues() {
		if(content.length()>maxLength && maxLength-end.length()>-1){
			content=content.substring(0,maxLength-end.length())+end;
		}
		return content;
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

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}
}