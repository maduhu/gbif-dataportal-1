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
package org.gbif.portal.service.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author trobertson
 */
public abstract class AbstractLoggableFromPredicateAndObject implements LoggableFromPredicateAndObject {
	/**
	 * Logger
	 */
	protected Log logger = LogFactory.getLog(getClass());
	
	/**
	 * Parses the supplied key. Returns null if supplied object.toString() invalid
	 * @param key
	 * @return a concept key. Returns null if supplied string invalid key
	 */
	protected static Long parseKey(Object key){
		if(key==null)
			return null;
		Long parsedKey = null;
		try {
			parsedKey = Long.parseLong(key.toString());
		} catch (NumberFormatException e){
			//expected behaviour for invalid keys
		}
		return parsedKey;
	}
}