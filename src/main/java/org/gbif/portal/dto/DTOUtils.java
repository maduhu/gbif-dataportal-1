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
package org.gbif.portal.dto;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.model.BaseObject;
/**
 * A set of utility methods for creating / populating dtos and dto result sets. 
 * These methods will typically be called by DTOFactory implementations in an attempt to keep
 * these factory classes thin.
 * 
 * These methods set the upper and lower bounds in the resultsets and the next value if the results
 * have been truncated. It also sets the hasMoreResults field based on whether or not
 * the number of results returned exceeds the maxResults value passed in. For this
 * to work the DAO method called needs to be supplied with a "maxResults+1" value so that
 * it can be ascertained if there are more results to come.
 * 
 * @author dmartin
 */
public class DTOUtils {

	protected static Log logger = LogFactory.getLog(DTOUtils.class);

	/**
	 * Populates a SearchResultsDTO for this set of model objects by calling the correct
	 * DTO Factory. Sets the First, Last and Next Keys
	 * 
	 * @param modelObjects the objects to create DTOs for
	 * @param maxResults the max results to include in the SearchResultsDTO
	 * @return SearchResultsDTO
	 */
	public static void populate(DTOFactory dtoFactory, SearchResultsDTO searchResultsDTO, List modelObjects, Integer maxResults){

		searchResultsDTO.setHasMoreResults(modelObjects.size()>maxResults);
		int resultsetSize = modelObjects.size() > maxResults ? maxResults : modelObjects.size();
		if(logger.isDebugEnabled()){
			logger.debug("Number of results: "+modelObjects.size());
			logger.debug("max results: "+maxResults);
			logger.debug("resultset size: "+resultsetSize);			
		}

		//set key boundaries
		if(resultsetSize>0){
			if(modelObjects.get(0) instanceof BaseObject) {
				searchResultsDTO.setFirstKey(((BaseObject)modelObjects.get(0)).getId().toString());
				searchResultsDTO.setLastKey(((BaseObject)modelObjects.get(resultsetSize-1)).getId().toString());
				if(searchResultsDTO.hasMoreResults)
					searchResultsDTO.setNextKey(((BaseObject)modelObjects.get(resultsetSize)).getId().toString());
			} else if (modelObjects.get(0) instanceof Object[]) {
				if(((Object[]) modelObjects.get(0))[0] !=null)
					searchResultsDTO.setFirstKey(((Object[]) modelObjects.get(0))[0].toString());
				if(((Object[])modelObjects.get(resultsetSize-1))[0]!=null)
					searchResultsDTO.setLastKey(((Object[])modelObjects.get(resultsetSize-1))[0].toString());
				if(searchResultsDTO.hasMoreResults && ((Object[])modelObjects.get(resultsetSize))[0]!=null )
					searchResultsDTO.setNextKey(((Object[])modelObjects.get(resultsetSize))[0].toString());				
			} else if (modelObjects.get(0) instanceof String) {
				searchResultsDTO.setFirstKey((String) modelObjects.get(0));
				searchResultsDTO.setLastKey((String)modelObjects.get(resultsetSize-1));
				if(searchResultsDTO.hasMoreResults)
					searchResultsDTO.setNextKey((String)modelObjects.get(resultsetSize));				
			}			
		}		
		
		//copy into DTOs
		for (int i=0; i<resultsetSize; i++) 
			searchResultsDTO.addResult(dtoFactory.createDTO(modelObjects.get(i)));

		//debug
		debugSearchResults(searchResultsDTO);
	}

	/**
	 * Populates a SearchResultsDTO for this set of objects. This should be used for lists
	 * of primitive types such as strings.
	 * 
	 * Sets the First, Last and Next Keys.
	 * 
	 * @param modelObjects the objects to create DTOs for
	 * @param maxResults the max results to include in the SearchResultsDTO
	 * @return SearchResultsDTO
	 */
	public static SearchResultsDTO createSearchResultsDTO(List objects, Integer maxResults){
		SearchResultsDTO searchResultsDTO = new SearchResultsDTO();
		DTOUtils.populate(searchResultsDTO, objects, maxResults);
		return searchResultsDTO;
	}
	
	/**
	 * Populates a SearchResultsDTO for this set of objects. This should be used for lists
	 * of primitive types such as strings.
	 * 
	 * Sets the First, Last and Next Keys.
	 * 
	 * @param modelObjects the objects to create DTOs for
	 * @param maxResults the max results to include in the SearchResultsDTO
	 * @return SearchResultsDTO
	 */
	public static void populate(SearchResultsDTO searchResultsDTO, List objects, Integer maxResults){

		searchResultsDTO.setHasMoreResults(objects.size()>maxResults);
		int resultsetSize = objects.size() > maxResults ? maxResults : objects.size();
		if(logger.isDebugEnabled()){
			logger.debug("Number of results: "+objects.size());
			logger.debug("max results: "+maxResults);
			logger.debug("resultset size: "+resultsetSize);			
		}

		//set key boundaries
		if(resultsetSize>0){
			searchResultsDTO.setFirstKey(objects.get(0).toString());
			searchResultsDTO.setLastKey(objects.get(resultsetSize-1).toString());
			if(searchResultsDTO.hasMoreResults)
				searchResultsDTO.setNextKey(objects.get(resultsetSize).toString());
		}		
		
		//copy into DTOs
		for (int i=0; i<resultsetSize; i++) {
			searchResultsDTO.addResult(objects.get(i));
		}
		//debug
		debugSearchResults(searchResultsDTO);
	}	
	
	private static void debugSearchResults(SearchResultsDTO searchResultsDTO){
		if(logger.isDebugEnabled()){
			logger.debug("First key: "+searchResultsDTO.getFirstKey());
			logger.debug("Last key: "+searchResultsDTO.getLastKey());
			logger.debug("Next key: "+searchResultsDTO.getNextKey());
			logger.debug("Returning result - has more results ?: "+searchResultsDTO.hasMoreResults());
			logger.debug("Returning result set size: "+searchResultsDTO.getResults().size());
		}			
	}
}