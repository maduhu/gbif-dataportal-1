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

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.util.log.LogEvent;
import org.springframework.context.MessageSource;

/**
 * A field impl for rendering a log event.
 * 
 * @author dmartin
 */
public class LogEventField extends Field {

	/**
	 * @see org.gbif.portal.web.download.Field#getFieldValue(org.springframework.context.MessageSource, java.util.Locale, java.lang.String)
	 */
	@Override
	public String getFieldValue(MessageSource messageSource, Locale locale, String propertyValue) {
		try {
			if(StringUtils.isNotEmpty(propertyValue)){
				Integer eventId = Integer.parseInt(propertyValue);
				LogEvent logEvent = LogEvent.get(eventId);
				if(logEvent!=null)
					return messageSource.getMessage(logEvent.getName(), null, logEvent.getName(), locale);
			}
		} catch (Exception e){
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}