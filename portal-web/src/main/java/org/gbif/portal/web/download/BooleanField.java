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

import java.util.Locale;

import org.springframework.context.MessageSource;

/**
 * Simple Field  implfor display values for booleans.
 * 
 * @author dmartin
 */
public class BooleanField extends Field {

	protected String trueI18nKey = "Y";
	protected String falseI18nKey = "N";
	
	
	/**
	 * @see org.gbif.portal.web.download.Field#getFieldValue(org.springframework.context.MessageSource, java.util.Locale, java.lang.String)
	 */
	@Override
	public String getFieldValue(MessageSource messageSource, Locale locale, String propertyValue) {
		
		if(propertyValue==null)
			return propertyValue;
		Boolean boolValue = Boolean.parseBoolean(propertyValue);
		if(boolValue)
			return messageSource.getMessage(trueI18nKey, null, locale);
		else
			return messageSource.getMessage(falseI18nKey, null, locale);
	}
}