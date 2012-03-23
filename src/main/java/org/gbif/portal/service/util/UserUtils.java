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
package org.gbif.portal.service.util;

/**
 * Maps a special code for a user name and email 
 * which must be supplied to authorise a user
 * @author trobertson
 */
public class UserUtils {
	/**
	 * @return the code
	 */
	public static int getCodeFor(String name, String email) {
		return Math.abs((name + " - " + email).hashCode());
	}
	
	/**
	 * @return the code
	 */
	public static boolean isValidCodeCodeFor(String name, String email, int code) {
		int valid = Math.abs((name + " - " + email).hashCode());
		return valid == code;
	}
	
	/**
	 * @return the code
	 */
	public static boolean isValidCodeCodeFor(String name, String email, String code) {
		try {
			int codeAsInt = Integer.parseInt(code);
			return isValidCodeCodeFor(name, email, codeAsInt);
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	
}
