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

package org.gbif.portal.webservices.util;

/**
 * This exception is to be thrown when anything undesirable happens during
 * processing SOAP web service requests. It may be used in future to whatever
 * needed to be done under respective circumstances  
 * @author Ali Kalufya  
 */

@SuppressWarnings("serial")
public class GbifWebServiceException extends Exception {

	/**
	 * 
	 */
	public GbifWebServiceException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public GbifWebServiceException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public GbifWebServiceException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public GbifWebServiceException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
