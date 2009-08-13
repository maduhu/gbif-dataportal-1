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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.TaxonomyManager;

/**
 * Paging common name search.
 * 
 * @author dmartin
 */
public class CommonNameSearchController extends SimpleSearchPagingController{

	protected DataResourceManager dataResourceManager;
	protected TaxonomyManager taxonomyManager;
	
	@Override
	public boolean doQuery(String searchString, SearchConstraints searchConstraints, Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		SearchResultsDTO searchResults = taxonomyManager.findTaxonConceptsForCommonName(searchString, true, searchConstraints);
		SearchResultsDTO endingWithResults = taxonomyManager.findTaxonConceptsForCommonName("% "+searchString, true, searchConstraints);
		searchResults.addAll(endingWithResults);
		request.setAttribute(searchResultsRequestKey, searchResults);
		return searchResults.hasMoreResults();
	}

	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}

	/**
	 * @param taxonomyManager the taxonomyManager to set
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
	}
}
