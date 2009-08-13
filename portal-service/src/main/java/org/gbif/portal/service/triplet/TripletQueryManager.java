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
package org.gbif.portal.service.triplet;

import java.util.List;

import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.io.ResultsOutputter;
import org.gbif.portal.service.ServiceException;

/**
 * TripletQueryManager
 * 
 * Implemented by managers that provide a triplet query mechanism.
 * Although this interface is sits logically within the service layer it is not
 * intended for for use by third parties. 
 * 
 * @author trobertson
 * @author dmartin
 */
public interface TripletQueryManager {
	
	/**
	 * Using the supplied criteria, performs the search and returns the brief version of the results.
	 * 
	 * @param criteria A List of property store keyed predicates
	 * @param matchAll whether all or any of the criteria should be matched, nullable which defaults to all
	 * @return The search results object, supporting paging
	 */
	public SearchResultsDTO doTripletQuery(List<PropertyStoreTripletDTO> criteria, boolean matchAll, SearchConstraints constraints) throws ServiceException;
	
	/**
	 * Execute a triplet query using the supplied criteria and output the results using 
	 * the <code>ResultsOutputter</code> supplied. 
	 * 
	 * @param criteria
	 * @param matchAll
	 * @param constraints
	 * @param resultsFormatter
	 * @throws ServiceException
	 */
	public void formatTripletQueryRequest(List<PropertyStoreTripletDTO> criteria, boolean matchAll, SearchConstraints constraints, ResultsOutputter resultsFormatter) throws ServiceException;
}