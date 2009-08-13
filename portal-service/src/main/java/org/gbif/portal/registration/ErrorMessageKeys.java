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
package org.gbif.portal.registration;

/**
 * Holds the error keys for the resource bundle
 * 
 * @author trobertson
 */
public interface ErrorMessageKeys {
	public static final String MUST_BE_SPECIFIED = "errors.must.be.specified";
	public static final String UDDI_COMMUNICATION_ERROR = "errors.uddi.communication";
	public static final String USERNAME_IN_USE = "errors.username.in.use";
	public static final String INVALID_VALUE = "errors.invalid";	
	public static final String MUST_BE_MINIMIUM_LENGTH = "errors.tooshort";
	public static final String CONTAINS_INVALID_CHARS = "errors.invalid.chars";
	public static final String INVALID_LATITUDE = "errors.invalid.latitude";
	public static final String INVALID_LONGITUDE = "errors.invalid.longitude";
	public static final String WEST_GREATER_THAN_EAST  = "errors.west.greater.east";
	public static final String SOUTH_GREATER_THAN_NORTH = "errors.south.greater.north";
}
