/***************************************************************************
 * Copyright (C) 2006 Global Biodiversity Information Facility Secretariat.
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
package org.gbif.portal.service;

/**
 * ServiceExceptions are thrown by Service Layer methods to indicate a failure to retrieve the data due to
 * a network/database connection or a data integrity problem. ServiceExceptions are not thrown to indicate
 * for example a bad argument value.
 * 
 * @author dbarnier
 * @author dmartin
 */
public class ServiceException extends Exception {

	/** serial version id */
	private static final long serialVersionUID = 1603997124495990950L;

	/**
	 * Default Constructor.
	 */
	public ServiceException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ServiceException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ServiceException(Throwable cause) {
		super(cause);
	}
}