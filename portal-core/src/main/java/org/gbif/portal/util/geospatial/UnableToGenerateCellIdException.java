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

package org.gbif.portal.util.geospatial;

/**
 * Used to indicate that a Cell Id cannot be instanciated
 * @author tim
 */
public class UnableToGenerateCellIdException extends Exception {

	private static final long serialVersionUID = 5154386920306317433L;

	/**
	 * @param arg0 The message
	 */
	public UnableToGenerateCellIdException(String arg0) {
		super(arg0);	
	}
}
