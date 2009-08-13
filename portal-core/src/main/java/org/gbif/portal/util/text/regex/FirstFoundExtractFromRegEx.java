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
package org.gbif.portal.util.text.regex;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A utility that will use a List of GroupExtractFromRegEx and the first
 * found will return the result. 
 * 
 * @author trobertson
 */
public class FirstFoundExtractFromRegEx {
	/**
	 * Logger
	 */
	protected static Log log = LogFactory.getLog(FirstFoundExtractFromRegEx.class);
	
	/**
	 * The list of extractors to use
	 */
	protected List<GroupExtractFromRegEx> extractors;
	
	/**
	 * Iterates over the list of extractors in turn trying to extract
	 * the String.  The first result found will be used or null if no
	 * matches are found
	 * 
	 * @param test To extract
	 * @return The String representing the groups or null 
	 */
	public String extract(String test) {
		for (GroupExtractFromRegEx extractor : extractors) {
			String result = extractor.extract(test);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	/**
	 * @return Returns the extractors.
	 */
	public List<GroupExtractFromRegEx> getExtractors() {
		return extractors;
	}

	/**
	 * @param extractors The extractors to set.
	 */
	public void setExtractors(List<GroupExtractFromRegEx> extractors) {
		this.extractors = extractors;
	}
}
