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
package org.gbif.portal.web.view;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.web.download.Field;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.AbstractView;

/**
 * Produces a downloadable delimited view. The delimiter to use is spring configurable. 
 * 
 * @author dmartin
 */
public class DelimitedView extends AbstractView {

	/** Message source for i18n lookups */
	protected MessageSource messageSource;
	/** The delimiter to use */
	protected String delimiter = "\t";
	/** The content type to use */
	protected String contentType = "text/tab-separated-values";
	/** The content disposition attachment attribute */
	protected String contentDisposition = "occurrence-search.txt";
	/** The delimiter to use */
	protected String endOfRecord = "\n";	
	
	protected boolean renderFieldNames = false;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map map, HttpServletRequest request, HttpServletResponse response) throws Exception {

		List<Field> fields = (List<Field>) request.getAttribute("requestedFields");
		List results = (List) request.getAttribute("results");
		StringBuffer sb = new StringBuffer();
		
		Locale locale = RequestContextUtils.getLocale(request);
		
		if(renderFieldNames){
			for(Field field: fields){
				sb.append(messageSource.getMessage(field.getFieldI18nNameKey(), null, locale));
				sb.append(delimiter);
			}
			sb.append(endOfRecord);
		}
		
		for(int i=0; i<results.size(); i++){
			
			Object result = results.get(i);
			
			for(Field field: fields){
				String propertyValue = field.getRenderValue(request, messageSource, locale, result);
				if(propertyValue!=null){
					sb.append(propertyValue);
				}
				sb.append(delimiter);
			}
			sb.append(endOfRecord);
		}
		
		response.setContentType(contentType);
		response.setHeader("Content-Disposition", "attachment; "+contentDisposition);
		
		ByteArrayInputStream bInput = new ByteArrayInputStream(sb.toString().getBytes());
		ServletOutputStream sOut = response.getOutputStream();
		
		//write out zipped version to output stream
		byte[] buf = new byte[1024];
		int len;
		while ((len = bInput.read(buf)) > 0) {
			sOut.write(buf, 0, len);
        }
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @param delimiter the delimiter to set
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	/**
	 * @param contentDisposition the contentDisposition to set
	 */
	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @param endOfRecord the endOfRecord to set
	 */
	public void setEndOfRecord(String endOfRecord) {
		this.endOfRecord = endOfRecord;
	}
}