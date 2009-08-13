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
package org.gbif.portal.web.controller.dataset;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.web.controller.search.NameSearchController;
import org.gbif.portal.web.controller.search.bean.NameSearch;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * A name search controller for datasets.
 * 
 * @author dmartin
 */
public class DatasetsNameSearchController extends NameSearchController {

	protected DataResourceManager dataResourceManager;
	
	/** non publicly disclosed keys */
	protected String providerRequestKey = "provider";
	protected String resourceRequestKey = "resource";
	protected String countryRequestKey = "country";
	protected String basisOfRecordRequestKey = "basisOfRecord";
	protected String anyOccurrenceRequestKey = "anyOccurrence";
	
	/**
	 * @see org.gbif.portal.web.controller.search.NameSearchController#handleSearch(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.ModelAndView, org.gbif.portal.web.controller.search.bean.NameSearch)
	 */
	@Override
	public void handleSearch(Map<String, String> propertiesMap,
			HttpServletRequest request, HttpServletResponse response,
			ModelAndView mav, NameSearch nameSearch) throws Exception {
		
		String datasetType = ServletRequestUtils.getStringParameter(request, "datasetType");		
		String provider = ServletRequestUtils.getStringParameter(request, providerRequestKey);
		String isoCountryCode = ServletRequestUtils.getStringParameter(request, countryRequestKey);
		String basisOfRecordCode = ServletRequestUtils.getStringParameter(request, basisOfRecordRequestKey);
		boolean anyOccurrence = ServletRequestUtils.getBooleanParameter(request, anyOccurrenceRequestKey, false);

		SearchResultsDTO searchResults = null;
		
		if(StringUtils.isNotEmpty(datasetType)){
			if("network".equalsIgnoreCase(datasetType)){
				searchResults = dataResourceManager.findResourceNetworks(nameSearch.getQuery(), !nameSearch.isExactMatchOnly(), null, null, nameSearch.getSearchConstraints());
			} else if("provider".equalsIgnoreCase(datasetType)){
				searchResults = dataResourceManager.findDataProviders(nameSearch.getQuery(), !nameSearch.isExactMatchOnly(), isoCountryCode, null, nameSearch.getSearchConstraints());
			} else {
				searchResults = dataResourceManager.findDataResources(nameSearch.getQuery(), !nameSearch.isExactMatchOnly(), provider, basisOfRecordCode, null, nameSearch.getSearchConstraints());				
			}
		} else {
			//search all
			searchResults = dataResourceManager.findDatasets(nameSearch.getQuery(), !nameSearch.isExactMatchOnly(), anyOccurrence, false, nameSearch.getSearchConstraints());
		}
		mav.addObject(searchResultsModelKey, searchResults);
	}

	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}

	/**
	 * @param providerRequestKey the providerRequestKey to set
	 */
	public void setProviderRequestKey(String providerRequestKey) {
		this.providerRequestKey = providerRequestKey;
	}

	/**
	 * @param resourceRequestKey the resourceRequestKey to set
	 */
	public void setResourceRequestKey(String resourceRequestKey) {
		this.resourceRequestKey = resourceRequestKey;
	}
}