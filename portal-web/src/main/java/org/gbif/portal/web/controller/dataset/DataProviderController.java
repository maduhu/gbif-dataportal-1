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

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.resources.DataProviderAgentDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.dto.util.EntityType;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.util.log.GbifLogMessage;
import org.gbif.portal.util.log.LogEvent;
import org.gbif.portal.web.content.map.MapContentProvider;
import org.gbif.portal.web.controller.RestController;
import org.gbif.portal.web.util.UserUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * A Controller for rendering Data Provider views.
 *
 * @author Dave Martin
 */
public class DataProviderController extends RestController {
	
	/** The Taxonomy Manager */
	protected TaxonomyManager taxonomyManager;
	/** The Data Resource Manager */
	protected DataResourceManager dataResourceManager;
	/** The MapContentProvider */
	protected MapContentProvider mapContentProvider;	
	/** The data resource request key */ 
	protected String providerRequestKey = "provider";

	/** The data provider model key */ 
	protected String dataProviderModelKey = "dataProvider";
	/** Agents for this provider */ 
	protected String dataProviderAgentsModelKey="agents";
	/** The nub data provider model key */ 
	protected String nubDataProviderModelKey = "nubDataProvider";	
	/** The data resources model key */ 
	protected String dataResourcesModelKey = "dataResources";	
	/** The root concepts model key */ 
	protected String rootConceptsModelKey = "rootConcepts";	
	/** the basis of records values for a taxonomic data resource */
	protected List<String> taxonomyBasisOfRecordValues;
	
	/** User Utils */
	protected UserUtils userUtils;
	
	/**
	 * @see org.gbif.portal.web.controller.RestController#handleRequest(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ModelAndView handleRequest(Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String dataProviderKey = propertiesMap.get(providerRequestKey);	
		if (StringUtils.isNotEmpty(dataProviderKey)){
			if(dataResourceManager.isValidDataProviderKey(dataProviderKey)){
				return view(dataProviderKey, propertiesMap, request, response);
			} else {
				String providerName = decodeParameter(dataProviderKey);
				SearchConstraints searchConstraints = new SearchConstraints();
				SearchResultsDTO searchResultsDTO = dataResourceManager.findDataProviders(providerName, false, null, null, searchConstraints);
				List<DataProviderDTO> dataProviders = searchResultsDTO.getResults();
				if (dataProviders.size() ==1){
					DataProviderDTO dataProvider = dataProviders.get(0);
					return view(dataProvider, propertiesMap, request, response);	
				}
				//resolver view?
			}
		}
		return redirectToDefaultView();
	}

	/**
	 * Create a view the data resource with the supplied id.
	 * 
	 * @param dataProviderKey
	 * @param request
	 * @param response
	 * @return a ModelAndView for this data provider
	 * @throws Exception
	 */
	public ModelAndView view(String dataProviderKey, Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		DataProviderDTO dataProvider = dataResourceManager.getDataProviderFor(dataProviderKey);
		if(dataProvider==null)
			return redirectToDefaultView();
		return view(dataProvider, propertiesMap, request, response);
	}
	
	/**
	 * Create a view the data resource with the supplied Data Provider.
	 * 
	 * @param dataProvider
	 * @param request
	 * @param response
	 * @return a ModelAndView for this dataset
	 * @throws Exception
	 */
	public ModelAndView view(DataProviderDTO dataProvider, Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//ModelAndView mav = new ModelAndView(dataProviderViewModelName);
		ModelAndView mav = resolveAndCreateView(propertiesMap, request, false);
		mav.addObject(dataProviderModelKey, dataProvider);
		mav.addObject(nubDataProviderModelKey, dataResourceManager.getNubDataProvider());
		// add the data resources for this provider
		List<DataResourceDTO> dataResources = dataResourceManager.getDataResourcesForProvider(dataProvider.getKey());
		mav.addObject(dataResourcesModelKey, dataResources);
		
		if(isTaxonomicProvider(dataResources)){
			mav.addObject("isTaxonomicProvider", true);
		}
		
		
//		List<BriefTaxonConceptDTO> rootConcepts = taxonomyManager.getRootTaxonConceptsForTaxonomy(dataProvider.getKey(), null);
//		if(logger.isDebugEnabled())
//			logger.debug("rootConcepts: "+rootConcepts.size());
//		mav.addObject(rootConceptsModelKey, rootConcepts);
		mapContentProvider.addMapContentForEntity(request, EntityType.TYPE_DATA_PROVIDER, dataProvider.getKey());

		List<DataProviderAgentDTO> dataProviderAgents = dataResourceManager.getAgentsForDataProvider(dataProvider.getKey());
		mav.addObject(dataProviderAgentsModelKey, dataProviderAgents);
		
		logUsage(request, dataProvider);
		return mav;
	}

	/**
	 * @param dataProvider
	 */
	protected void logUsage(HttpServletRequest request, DataProviderDTO dataProvider) {
		GbifLogMessage gbifMessage = new GbifLogMessage();
		gbifMessage.setEvent(LogEvent.USAGE_DATAPROVIDER_METADATA_VIEW);
		gbifMessage.setDataProviderId(parseKey(dataProvider.getKey()));
		gbifMessage.setTimestamp(new Date());
		gbifMessage.setRestricted(false);
		gbifMessage.setMessage("Provider metadata viewed");
		userUtils.logUsage(logger, gbifMessage, request);
	}	
	/**
	 * Parses the supplied key. Returns null if supplied string invalid
	 * @param key
	 * @return a concept key. Returns null if supplied string invalid key
	 */
	protected static Long parseKey(String key){
		Long parsedKey = null;
		try {
			parsedKey = Long.parseLong(key);
		} catch (NumberFormatException e){
			//expected behaviour for invalid keys
		}
		return parsedKey;
	}
	
	
	private boolean isTaxonomicProvider(List<DataResourceDTO> dataResources) {
		if(taxonomyBasisOfRecordValues==null || taxonomyBasisOfRecordValues.isEmpty())
			return false;
		
		boolean hasTaxonomicResources = false;
		
		for(DataResourceDTO dataResource: dataResources){
			hasTaxonomicResources = true;
			if(!taxonomyBasisOfRecordValues.contains(dataResource.getBasisOfRecord()))
				return false;
		}
		return hasTaxonomicResources;
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
	
	/**
	 * @param mapContentProvider the mapContentProvider to set
	 */
	public void setMapContentProvider(MapContentProvider mapContentProvider) {
		this.mapContentProvider = mapContentProvider;
	}

	/**
	 * @param providerRequestKey the providerRequestKey to set
	 */
	public void setProviderRequestKey(String providerRequestKey) {
		this.providerRequestKey = providerRequestKey;
	}

	/**
	 * @param dataProviderModelKey the dataProviderModelKey to set
	 */
	public void setDataProviderModelKey(String dataProviderModelKey) {
		this.dataProviderModelKey = dataProviderModelKey;
	}

	/**
	 * @param dataResourcesModelKey the dataResourcesModelKey to set
	 */
	public void setDataResourcesModelKey(String dataResourcesModelKey) {
		this.dataResourcesModelKey = dataResourcesModelKey;
	}

	/**
	 * @param nubDataProviderModelKey the nubDataProviderModelKey to set
	 */
	public void setNubDataProviderModelKey(String nubDataProviderModelKey) {
		this.nubDataProviderModelKey = nubDataProviderModelKey;
	}

	/**
	 * @param rootConceptsModelKey the rootConceptsModelKey to set
	 */
	public void setRootConceptsModelKey(String rootConceptsModelKey) {
		this.rootConceptsModelKey = rootConceptsModelKey;
	}

	/**
	 * @param taxonomyBasisOfRecordValues the taxonomyBasisOfRecordValues to set
	 */
	public void setTaxonomyBasisOfRecordValues(
			List<String> taxonomyBasisOfRecordValues) {
		this.taxonomyBasisOfRecordValues = taxonomyBasisOfRecordValues;
	}

	/**
	 * @param dataProviderAgentsModelKey the dataProviderAgentsModelKey to set
	 */
	public void setDataProviderAgentsModelKey(String dataProviderAgentsModelKey) {
		this.dataProviderAgentsModelKey = dataProviderAgentsModelKey;
	}

	/**
	 * @param userUtils the userUtils to set
	 */
	public void setUserUtils(UserUtils userUtils) {
		this.userUtils = userUtils;
	}
}