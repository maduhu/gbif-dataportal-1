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
package org.gbif.portal.util.taxonomy;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A Java implementation of the sound ex algorithm supplied by Tony Rees 
 * @author trobertson
 */
public class TaxonNameSoundEx {
	
	/**
	 * Returns the SoundEx for the source string
	 * @param source String to get the sound ex of 
	 * @return The sound ex string
	 */
	public String soundEx(String source) {
		String temp = source.toUpperCase();
		temp = selectiveReplaceFirstChar(temp);
		temp = selectiveReplaceWithoutFirstChar(temp);
		temp = removeRepeatedChars(temp);
		temp = alphabetiseWordsIgnoringFirstLetter(temp);
		
		return temp;		
	}
	
	/**
	 * Ignoring the first letter, alphabetise each word
	 */
	String alphabetiseWordsIgnoringFirstLetter(String source) {
		StringTokenizer st = new StringTokenizer(source, " ");
		StringBuffer sb = new StringBuffer();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			char[] chars = token.toCharArray();
			List<Character> charList = new LinkedList<Character>();
			for (int i=1; i<chars.length; i++) {
				charList.add(chars[i]);
			}
			Collections.sort(charList);
			sb.append(chars[0]);
			for (Character c : charList) {
				sb.append(c);	
			}
			if (st.hasMoreTokens()) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	/**
	 * Removes repeated characters
	 * Can't get the regex version working so pretty primitive...
	 */
	String removeRepeatedChars(String source) {
		StringBuffer sb = new StringBuffer();
		char c = ' ';
		for (int i=0; i<source.length(); i++) {
			char sourceC = source.charAt(i);
			if (sourceC != c) {
				sb.append(sourceC);
			}
			c = sourceC;
		}		
		return sb.toString();
	}

	/**
	 * Ignoring the first character, selectively replace sound alikes
	 */
	String selectiveReplaceWithoutFirstChar(String source) {
		if (source.length()>1) {
			String temp = source.substring(1);
			temp = temp.replaceAll("AE", "I");
			temp = temp.replaceAll("IA", "A");
			temp = temp.replaceAll("OE", "I");
			temp = temp.replaceAll("OI", "A");
			temp = temp.replaceAll("MC", "MAC");
			temp = temp.replaceAll("SC", "S");
			temp = temp.replaceAll("EOUYKZH", "IAIICS");
		
			return source.substring(0,1) + temp;
		} else {
			return source;
		}
	}

	/**
	 * Selectively replaces the first character
	 */
	String selectiveReplaceFirstChar(String source) {
		if (source.startsWith("Æ")) {
			return source.replaceFirst("Æ", "E");
			
		} else if (source.startsWith("AE")) {
			return source.replaceFirst("AE", "E");
			
		} else if (source.startsWith("CN")) {
			return source.replaceFirst("CN", "N");
			
		} else if (source.startsWith("CT")) {
			return source.replaceFirst("CT", "T");
			
		} else if (source.startsWith("CZ")) {
			return source.replaceFirst("CZ", "C");
			
		} else if (source.startsWith("DJ")) {
			return source.replaceFirst("DJ", "J");
			
		} else if (source.startsWith("EA")) {
			return source.replaceFirst("EA", "E");
			
		} else if (source.startsWith("EU")) {
			return source.replaceFirst("EU", "U");
			
		} else if (source.startsWith("GN")) {
			return source.replaceFirst("GN", "N");
			
		} else if (source.startsWith("KN")) {
			return source.replaceFirst("KN", "N");
			
		} else if (source.startsWith("MN")) {
			return source.replaceFirst("MN", "N");
			
		} else if (source.startsWith("OE")) {
			return source.replaceFirst("OE", "E");
			
		} else if (source.startsWith("QU")) {
			return source.replaceFirst("QU", "Q");
			
		} else if (source.startsWith("PS")) {
			return source.replaceFirst("PS", "S");
			
		} else if (source.startsWith("PT")) {
			return source.replaceFirst("PT", "T");
			
		} else if (source.startsWith("TS")) {
			return source.replaceFirst("TS", "S");
			
		} else if (source.startsWith("X")) {
			return source.replaceFirst("X", "Z");
			
		}
		else return source;
	}
}
