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

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.fmt.LocaleSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Static Util methods for tags.
 * @author dmartin
 */
public class TagUtils {

	protected static Log log = LogFactory.getLog(TagUtils.class);			
	
	/**
	 * Returns a i18n string for the supplied key.
	 * @param messageKey
	 * @param defaultValue
	 * @param pageContext
	 * @return the i18n string for the supplied key
	 */
    public static String getMessage(String messageKey, String defaultValue, PageContext pageContext){
        // if messageKey isn't defined either, use defaultValue
        String key = (messageKey != null) ? messageKey : defaultValue;
    	String message = LocaleSupport.getLocalizedMessage(pageContext, key);
        return message;
    }
}