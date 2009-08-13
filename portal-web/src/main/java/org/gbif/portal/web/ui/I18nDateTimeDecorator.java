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
package org.gbif.portal.web.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.PageContext;

import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;
import org.gbif.portal.web.tag.TagUtils;

/**
 * Display tag decorator that will format the date according to the format pattern in the
 * resource bundle.
 * 
 * The resource bundle key used to determine the property is defined by 
 * <code>RESOURCE_BUNDLE_KEY</code> 
 * 
 * @author trobertson
 */
public class I18nDateTimeDecorator implements DisplaytagColumnDecorator {
	/**
	 * The key to resource bundle for the date format
	 */
	public static final String RESOURCE_BUNDLE_KEY = "I18nDateTimeDecorator.date.format";
	
	/**
	 * Reads the  property from the i18n file, and uses that for the 
	 * date format.  Should it not be mapped, an exception is thrown
	 * 
	 * @see org.displaytag.decorator.DisplaytagColumnDecorator#decorate(java.lang.Object, javax.servlet.jsp.PageContext, org.displaytag.properties.MediaTypeEnum)
	 */
	public Object decorate(Object columnValue, PageContext pageContext, MediaTypeEnum mte) throws DecoratorException {
        String format = TagUtils.getMessage(RESOURCE_BUNDLE_KEY, null, pageContext);
        if (format == null) {
        	throw new DecoratorException(I18nDateTimeDecorator.class, "No format found for current local and key [" + RESOURCE_BUNDLE_KEY + "]" );
        } else if (columnValue == null) { 
        	return null;
        } else {
        	DateFormat df = new SimpleDateFormat(format);
        	return df.format((Date) columnValue);
        }	
    }
}