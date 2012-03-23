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
package org.gbif.portal.service.triplet.impl;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dao.SimpleQueryDAO;
import org.gbif.portal.dto.DTOFactory;
import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.io.ResultsOutputter;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.triplet.TripletQueryManager;
import org.gbif.portal.service.util.PropertyStoreQueryBuilder;

/**
 * Implementation of TripletQueryManager that provides support for paging.
 * 
 * @author trobertson
 * @author dmartin
 */
public class TripletQueryManagerImpl implements TripletQueryManager {
	
	protected static Log logger = LogFactory.getLog(TripletQueryManagerImpl.class);

	/**The default maximum results to retrieve**/
	public static final int DEFAULT_MAX_RESULTS=1000;	
	
	/** Underlying DAO to send the query to*/
	protected SimpleQueryDAO simpleQueryDAO;
	
	/** For building the queries passed in */
	protected PropertyStoreQueryBuilder queryBuilder;
	
	/**the DTOFactory to use **/
	protected DTOFactory dtoFactory;
	
	/** Max results to be returned */
	protected int maxResults = DEFAULT_MAX_RESULTS;

	/**
	 * Using the supplied criteria, performs the search and returns the brief version of the results.
	 * 
	 * Where applicable applies less than and greater than triplets to enable paging across results.
	 * 
	 * @param criteria A List of property store keyed predicates
	 * @param matchAll indicates if all the triplets criteria should be matched
	 * @param searchConstraints the Constraints to be applied to this search
	 * @return The brief version of the results
	 */
	@SuppressWarnings("unchecked")
	public SearchResultsDTO doTripletQuery(List<PropertyStoreTripletDTO> criteria, boolean matchAll, SearchConstraints constraints) throws ServiceException{
		
		if(criteria==null || criteria.size()==0)
			return new SearchResultsDTO();

		boolean descending = false;
		if(logger.isDebugEnabled()){
			logger.debug("Constraints: = "+constraints);
		}
		
		List<PropertyStoreTripletDTO> fullCriteria = new LinkedList<PropertyStoreTripletDTO>();
		fullCriteria.addAll(criteria);
		
		String query = queryBuilder.buildQuery(fullCriteria, matchAll, descending);
		if(logger.isDebugEnabled())
			logger.debug("Built query : "+query);
		
		List<Object> parameters = new LinkedList<Object>();
		for (PropertyStoreTripletDTO criterion : fullCriteria) 
			parameters.add(criterion.getObject());
		
		int pageSize = constraints.getMaxResults();
		
		//queries for pageSize+1. if pageSize+1 records are returned
		//then we know we have more results to query for
		if(logger.isDebugEnabled()){
			logger.debug("Querying with start[" + constraints.getStartIndex() + "] limit[" + (pageSize+1)+ "]");
		}
		List results = simpleQueryDAO.getByQuery(query, parameters, constraints.getStartIndex(), pageSize+1);
		if(logger.isDebugEnabled()){
			if(results.size()>0)
				logger.debug("First result: "+results.get(0));
		}
		
		SearchResultsDTO searchResults = dtoFactory.createResultsDTO(results, pageSize);
		if(logger.isDebugEnabled()){
			logger.debug("Result set size returned from query: "+results.size());
			logger.debug("Page Size: "+pageSize);
			logger.debug("Returning result set hasMore: "+searchResults.hasMoreResults());
			logger.debug("Returning result set size: "+searchResults.getResults().size());
		}
		
		return searchResults;
	}

	/**
	 * @see org.gbif.portal.service.triplet.TripletQueryManager#formatTripletQueryRequest(java.util.List, boolean, org.gbif.portal.dto.util.SearchConstraints, org.gbif.portal.io.ResultsOutputter)
	 */
	public void formatTripletQueryRequest(List<PropertyStoreTripletDTO> criteria, boolean matchAll, SearchConstraints constraints, ResultsOutputter resultsFormatter) throws ServiceException {
		
		List<PropertyStoreTripletDTO> fullCriteria = new LinkedList<PropertyStoreTripletDTO>();
		fullCriteria.addAll(criteria);
		
		String query = queryBuilder.buildQuery(fullCriteria, matchAll, false);
		if(logger.isDebugEnabled())
			logger.debug("Built query : "+query);
		
		List<Object> parameters = new LinkedList<Object>();
		for (PropertyStoreTripletDTO criterion : fullCriteria) 
			parameters.add(criterion.getObject());
		
		try {
			//write out the results
			simpleQueryDAO.outputResultsForQuery(query, parameters, constraints.getStartIndex(), constraints.getMaxResults(), resultsFormatter);
		} catch (IOException e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e);
		}
	}
	
	/**
	 * @param queryBuilder The queryBuilder to set.
	 */
	public void setQueryBuilder(
			PropertyStoreQueryBuilder occurrenceQueryBuilder) {
		this.queryBuilder = occurrenceQueryBuilder;
	}

	/**
	 * @param dtoFactory the dtoFactory to set
	 */
	public void setDtoFactory(DTOFactory dtoFactory) {
		this.dtoFactory = dtoFactory;
	}

	/**
	 * @param maxResults the maxResults to set
	 */
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	/**
	 * @param simpleQueryDAO the simpleQueryDAO to set
	 */
	public void setSimpleQueryDAO(SimpleQueryDAO simpleQueryDAO) {
		this.simpleQueryDAO = simpleQueryDAO;
	}
}