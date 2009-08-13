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
package org.gbif.portal.dao;

import java.io.IOException;
import java.util.List;

import org.gbif.portal.io.ResultsOutputter;
/**
 * Extended by DAO interfaces which provide a simple query methods.
 * 
 * @author dmartin
 */
public interface SimpleQueryDAO {

	/**
	 * Executes the supplied query returning a list of results. 
	 * 
	 * @param query To execute
	 * @param parameters To bind to the query (can be null)
	 * @param startIndex The index of the first result
	 * @param maxResults The max results that should be returned
	 * @return The results for the given query
	 */
	public List getByQuery(String query, List<Object> parameters, final Integer startIndex, Integer maxResults);
	
	
	
	public void outputResultsForQuery(String query, List<Object> parameters, final Integer startIndex, Integer maxResults, ResultsOutputter resultsFormatter) throws IOException;	
}