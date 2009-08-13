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
/**
 * Interface implemented by DTO Factories to provide standard reusable create methods. 
 * 
 * Provides three return types.<br><br>
 * <ol>
 * <li> Single DTO - used for single results.
 * <li> List of DTO - used for a complete list of results.
 * <li> SearchResultsDTO - used for Service Layer methods that may return a truncated list of results.
 * </ol>
 * 
 * @author dmartin
 */
public interface DTOFactory {

	/**
	 * Create a DTO from the model object.
	 * @param modelObject
	 * @return the populated DTO
	 */
	public Object createDTO(Object modelObject);
	
	/**
	 * Create a DTO from the model object.
	 * @param modelObjects
	 * @return a list of DTOs
	 */
	public List createDTOList(List modelObjects);		
	
	/**
	 * Create a SearchResultsDTO given the supplied model objects and max results constraints.
	 * @param modelObject
	 * @return SearchResultsDTO
	 */
	public SearchResultsDTO createResultsDTO(List modelObjects, Integer maxResults);
}
