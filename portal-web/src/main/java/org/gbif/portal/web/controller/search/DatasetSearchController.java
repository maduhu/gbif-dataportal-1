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
package org.gbif.portal.web.controller.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.dto.resources.ResourceNetworkDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.ServiceException;

/**
 * Simple controller for paging over dataset
 * 
 * @author dmartin
 */
public class DatasetSearchController extends SimpleSearchPagingController {

	protected DataResourceManager dataResourceManager;
	
	/**
	 * @see org.gbif.portal.web.controller.search.SimpleSearchPagingController#doQuery(java.lang.String, org.gbif.portal.dto.util.SearchConstraints, java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public boolean doQuery(String searchString,
			SearchConstraints searchConstraints,
			Map<String, String> propertiesMap, HttpServletRequest request,
			HttpServletResponse response) throws ServiceException {
		
		SearchResultsDTO resourceResultsDTO = dataResourceManager.findDatasets(searchString, true, true, false, searchConstraints);
		//split into 3 lists - networks, providers and resources
		List<Object> resourceMatches = resourceResultsDTO.getResults();
		List<DataProviderDTO> providers = new ArrayList<DataProviderDTO>();
		List<DataResourceDTO> resources = new ArrayList<DataResourceDTO>();
		List<ResourceNetworkDTO> networks = new ArrayList<ResourceNetworkDTO>();

		for (Object resourceMatch: resourceMatches){
			if(resourceMatch instanceof DataResourceDTO)
				resources.add((DataResourceDTO)resourceMatch);
			if(resourceMatch instanceof DataProviderDTO)
				providers.add((DataProviderDTO)resourceMatch); 
			if(resourceMatch instanceof ResourceNetworkDTO)
				networks.add((ResourceNetworkDTO)resourceMatch); 			
		}
		request.setAttribute(searchResultsRequestKey, resourceResultsDTO);
		request.setAttribute("dataProviders", providers);
		request.setAttribute("dataResources", resources);
		request.setAttribute("resourceNetworks", networks);		
		
		return resourceResultsDTO.hasMoreResults();
	}

	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}
}