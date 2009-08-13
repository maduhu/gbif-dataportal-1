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
package org.gbif.portal.web.content.occurrence;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.gbif.portal.web.util.DateUtil;
import org.springframework.context.MessageSource;

/**
 * Simple class for creating a i18n friendly citation.
 * 
 * @author dmartin
 */
public class CitationCreator {

	protected String introductionI18nKey = "citation.introduction";
	protected String singleEntryI18nKey = "citation.entry";
	
	/**
	 * Creates a citation for the supplied resources.
	 * 
	 * @param dataResourceKeyNames
	 * @param messageSource
	 * @param locale
	 * @param dataResourceUrl
	 * @return string buffer containing a citation.
	 */
	public StringBuffer createCitation(Map<String, String> dataResourceKeyNames, MessageSource messageSource, Locale locale, String dataResourceUrl){
		
		Date today = new Date(System.currentTimeMillis());
		String todaysDate = DateUtil.getDateString(today);
		StringBuffer sb = new StringBuffer();
		sb.append(messageSource.getMessage(introductionI18nKey, null, locale));
		for(String dataResourceKey: dataResourceKeyNames.keySet()){
			Object[] params = new Object[3];
			params[0]=dataResourceKeyNames.get(dataResourceKey);
			params[1]=dataResourceUrl+dataResourceKey;
			params[2]=todaysDate;
			sb.append(messageSource.getMessage(singleEntryI18nKey, params, locale));
			sb.append('\n');	
		}
		return sb;
	}
}