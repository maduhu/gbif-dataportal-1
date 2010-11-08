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
package org.gbif.portal.web.download;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;

/**
 * A field formatter for delimited values.
 * 
 * @author dmartin
 */
public class DelimitedFieldFormatter extends FieldFormatter {

	protected String delimiter = "\t";
	/** The characters to replace */
	protected Map<String, String> replaceChars=new HashMap<String, String>();
	
	/**
	 * 
	 * @param fields
	 * @param messageSource
	 * @param locale
	 * @param hostUrl
	 */
	public DelimitedFieldFormatter(List<Field> fields, MessageSource messageSource, Locale locale, String hostUrl) {
		super(fields, messageSource, locale, hostUrl);
	}

	/**
	 * @see org.gbif.portal.web.download.FieldFormatter#format(java.lang.String, java.lang.String)
	 */
	@Override
	public String format(String propertyName, String propertyValue) {
		String formattedValue = super.format(propertyName, propertyValue);
		if(formattedValue!=null){
			if (replaceChars!=null && replaceChars.keySet()!=null) {
				for(String character: replaceChars.keySet()){
					formattedValue=formattedValue.replaceAll(character, replaceChars.get(character));
				}			
				/*if(formattedValue.contains(delimiter)){
					//enclose in quotes
					formattedValue = "\""+formattedValue+"\"";
				}*/
			}
		}		
		return formattedValue;
	}

	/**
	 * @return the delimiter
	 */
	public String getDelimiter() {
		return delimiter;
	}

	/**
	 * @param delimiter the delimiter to set
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	/**
	 * @return the replaceChars
	 */
	public Map<String, String> getReplaceChars() {
		return replaceChars;
	}

	/**
	 * @param replaceChars the replaceChars to set
	 */
	public void setReplaceChars(Map<String, String> replaceChars) {
		this.replaceChars = replaceChars;
	}
}