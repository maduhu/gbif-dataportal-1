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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.dto.resources.ResourceNetworkDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.web.controller.AlphabetBrowserController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for Browsing Data Providers and Resources.
 * 
 * @author dmartin
 */
public class DatasetBrowserController extends AlphabetBrowserController {

	/** Provides the data resource/provider */
	protected DataResourceManager dataResourceManager;
	/** Model Key for the dataset matches */
	protected String datasetMatchesModelKey = "datasetMatches";
	/** Model Key for the data resource matches */
	protected String dataResourceModelKey = "dataResources";	
	/** Model Key for the data provider matches */
	protected String dataProviderModelKey = "dataProviders";	
	/** Model Key for the data provider matches */
	protected String resourceNetworksModelKey = "resourceNetworks";	
	
	protected DataProviderDTO nubDataProvider;
	protected List<DataResourceDTO> nubResources;
	
	/** Whether to hide the nub provider/resources from the view */
	protected boolean hideNub = true;
	
	/**
	 * @see org.gbif.portal.web.controller.AlphabetBrowserController#alphabetSearch(char, org.springframework.web.servlet.ModelAndView)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ModelAndView alphabetSearch(char searchChar, ModelAndView mav,  HttpServletRequest request, HttpServletResponse response) {
		SearchResultsDTO resourceResultsDTO = dataResourceManager.findDatasets(String.valueOf(searchChar), true, false, false, new SearchConstraints(0, null));
		List<Object> resourceMatches = resourceResultsDTO.getResults();

		List<DataProviderDTO> providers = new ArrayList<DataProviderDTO>();
		List<DataResourceDTO> resources = new ArrayList<DataResourceDTO>();
		List<ResourceNetworkDTO> resourceNetworks = new ArrayList<ResourceNetworkDTO>();		
		for (Object resourceMatch: resourceMatches){
			if(resourceMatch instanceof DataProviderDTO)
				providers.add((DataProviderDTO)resourceMatch); 
			if(resourceMatch instanceof DataResourceDTO)
				resources.add((DataResourceDTO)resourceMatch);
			if(resourceMatch instanceof ResourceNetworkDTO)
				resourceNetworks.add((ResourceNetworkDTO) resourceMatch);
		}
		
		if(hideNub){
			removeNubProviderAndResources(providers, resources);
		}
		mav.addObject(datasetMatchesModelKey, resourceResultsDTO);
		mav.addObject(resourceNetworksModelKey, resourceNetworks);	
		mav.addObject(dataProviderModelKey, providers);			
		mav.addObject(dataResourceModelKey, resources);	
		return mav;
	}

	/**
	 * Remove the nub provider/resources from the datasets list.
	 * 
	 * @param providers
	 * @param resources
	 */
	protected void removeNubProviderAndResources(List<DataProviderDTO> providers, List<DataResourceDTO> resources) {
		if(nubDataProvider==null){
			try {
				nubDataProvider = dataResourceManager.getNubDataProvider();
				if(nubDataProvider!=null){
					providers.remove(nubDataProvider);
					nubResources = dataResourceManager.getDataResourcesForProvider(nubDataProvider.getKey());
					if(nubResources!=null)
						resources.removeAll(nubResources);
				}
			} catch (ServiceException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	@Override
	public List<Character> retrieveAlphabet(HttpServletRequest request, HttpServletResponse response) {
		return dataResourceManager.getDatasetAlphabet();
	}		
	
	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}

	/**
	 * @param modelViewName the modelViewName to set
	 */
	public void setModelViewName(String modelViewName) {
		this.modelViewName = modelViewName;
	}

	/**
	 * @param dataProviderModelKey the dataProviderModelKey to set
	 */
	public void setDataProviderModelKey(String dataProviderModelKey) {
		this.dataProviderModelKey = dataProviderModelKey;
	}

	/**
	 * @param dataResourceModelKey the dataResourceModelKey to set
	 */
	public void setDataResourceModelKey(String dataResourceModelKey) {
		this.dataResourceModelKey = dataResourceModelKey;
	}

	/**
	 * @param datasetMatchesModelKey the datasetMatchesModelKey to set
	 */
	public void setDatasetMatchesModelKey(String datasetMatchesModelKey) {
		this.datasetMatchesModelKey = datasetMatchesModelKey;
	}
}