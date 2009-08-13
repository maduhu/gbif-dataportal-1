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

import org.gbif.portal.io.PropertyFormatter;
import org.springframework.context.MessageSource;

/**
 * Property formatter that applies i18n to certain fields.
 * 
 * @author dmartin
 */
public class FieldFormatter implements PropertyFormatter {
	
	/** the property field mappings to use */ 
	protected Map<String, Field> propertyFieldMappings;
	/** The host url, required for url fields */
	private String hostUrl;
	/** Message sourcefor i18n message retrieval */
	private MessageSource messageSource;
	/** Message source */
	private Locale locale;
	
	/**
	 * Initialize a field formatter.
	 * 
	 * @param fields the fields to format
	 * @param messageSource i18n message source
	 * @param locale the locale to use
	 * @param hostUrl required by some field for url construction
	 */
	public FieldFormatter(List<Field> fields, MessageSource messageSource, Locale locale, String hostUrl){
		
		//stick fields into a map for search purposes
		Map<String, Field> propertyFieldMappings = new HashMap<String, Field>();
		if(fields!=null){
			for(Field field: fields)
				propertyFieldMappings.put(field.getFieldName(), field);
		}
		this.propertyFieldMappings = propertyFieldMappings;
		this.messageSource = messageSource;
		this.locale = locale;
		this.hostUrl = hostUrl;
	}
	
	/**
	 * @see org.gbif.portal.io.PropertyFormatter#format(java.lang.String, java.lang.String)
	 */
	public String format(String propertyName, String propertyValue) {
		Field field = propertyFieldMappings.get(propertyName);
		if(field!=null){
			if(field instanceof UrlField){
				return ((UrlField)field).getFieldValue(hostUrl, messageSource, locale, propertyValue);
			} else {
				return field.getFieldValue(messageSource, locale, propertyValue);
			}
		}
		return propertyValue;
	}

	/**
	 * @param propertyFieldMappings the propertyFieldMappings to set
	 */
	public void setPropertyFieldMappings(Map<String, Field> propertyFieldMappings) {
		this.propertyFieldMappings = propertyFieldMappings;
	}

	/**
	 * @param hostUrl the hostUrl to set
	 */
	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}