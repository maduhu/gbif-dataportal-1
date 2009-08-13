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
package org.gbif.portal.util.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class for simple reusable String utilities
 * @author trobertson
 */
public class StringUtils {
	/**
	 * A pattern for an UPPERCASE word that is at least
	 * 2 characters long
	 * E.g. 
	 *  AUS
	 */
	public static Pattern UPPER_CASE_WORD = Pattern.compile("[A-Z]{2,}");
	
	/**
	 * A utility to remove change the case of words in the given string that are 
	 * all UPPERCASE.  Optionally the first found uppercase word can be set to be 
	 * of case [A-Z][a-z]+
	 * 
	 * AUS BUS spp. CUS Linneus 1771
	 *   -> aus bus spp. cus Linneus 1771 (firstLetterCaptital = false) 
	 *   -> Aus bus spp. cus Linneus 1771 (firstLetterCaptital = true)
	 * 
	 * @param toAlter The string who's case needs addressed
	 * @param firstLetterCaptital True if the first letter of the first word should be uppercase 
	 * (see example)
	 * @return The case normalised string
	 */
	public static String lowercaseCapitalisedWords(String toAlter, boolean firstLetterCaptital) {
		Matcher m = UPPER_CASE_WORD.matcher(toAlter);
		String modified = toAlter;
		boolean firstWord = true;
		while (m.find()) {
			String capitalWord = toAlter.substring(m.start(), m.end());
			if (firstWord && firstLetterCaptital) {
				firstWord = false;
				modified = modified.replaceFirst(capitalWord, org.apache.commons.lang.StringUtils.capitalize(capitalWord.toLowerCase()));
			} else {
				modified = modified.replaceFirst(capitalWord, capitalWord.toLowerCase());
			}
			
		}
		return modified;
	}	
	
	/**
	 * A utility to replace multiple ' ' with a single ' ' 
	 * 
	 * @param toAlter The string who's spaces need addressed
	 * @return The string with multiple spaces replaced with single spaces
	 */
	public static String trimMultipleSpaces(String toAlter) {
		return toAlter.replaceAll("[ ]+", " ");
	}		
}
