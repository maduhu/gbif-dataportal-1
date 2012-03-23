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
 * Indicates that the query was unable to be built
 * Note that it is a RuntimeException
 * 
 * @author trobertson
 */
public class UnableToBuildQueryException extends RuntimeException {

	private static final long serialVersionUID = 1457948428880616589L;

	/**
	 * @param message To use
	 * @param cause To use
	 */
	public UnableToBuildQueryException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public UnableToBuildQueryException(String message) {
		super(message);
	}

}
