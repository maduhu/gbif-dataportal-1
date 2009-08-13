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
package org.gbif.portal.service.provider;

import org.gbif.portal.service.ServiceException;

/**
 * This provides services to interact directly with a Data Provider
 * 
 * It does not follow the naming convention of ___Manager to indicate that it
 * is not offering services for the underlying DB, but is  instead offering 
 * dynamic services in the data provider over http.
 * 
 * @author trobertson
 */
public interface DataProviderServices {
	
	/**
	 * For the given key (which is the local gbif key) determine the details necessary to
	 * execute a request and issue a request for the occurrence, returning the response as
	 * a nicely formatted XML string  
	 * 
	 * @param gbifOccurrenceKey To identify the record of interest
	 * @return A nicely formatted XML string of the response
	 * @throws ServiceException If DB down or bad key supplied
	 * @throws EndpointUnreachableException If Comms failed
	 * @thorws InssufficientDataInIndexException If we can't issue the request - this is a placeholder exception and should not be expected
	 */
	public String getOccurrence(String gbifOccurrenceKey) throws EndpointUnreachableException, ServiceException, InsufficientDataInIndexException;
	
	/**
	 * Generate the message used to retrieve this record from the provider.
	 * 
	 * @param gbifOccurrenceKey
	 * @return A nicely formatted XML string of the request
	 * @throws ServiceException
	 */
	public String getOccurrenceRecordRequest(String gbifOccurrenceKey) throws ServiceException;
}