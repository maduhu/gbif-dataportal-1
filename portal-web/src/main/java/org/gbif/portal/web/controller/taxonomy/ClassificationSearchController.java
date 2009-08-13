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
package org.gbif.portal.web.controller.taxonomy;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.dto.taxonomy.TaxonConceptDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.web.controller.search.NameSearchController;
import org.gbif.portal.web.controller.search.bean.NameSearch;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * A classification search controller that supports simple constructed urls and sends back a classification for a species.
 * 
 * @author dmartin
 */
public class ClassificationSearchController extends NameSearchController {

	protected TaxonomyManager taxonomyManager;
	protected DataResourceManager dataResourceManager;
	
	protected String classificationRequestKey = "classification";
	protected String providerIdRequestKey = "providerId";
	protected String resourceIdRequestKey = "resourceId";
	protected String rankRequestKey = "rank";
	protected String allowUnconfirmedRequestKey = "allowUnconfirmed";
	protected String isoCountryCodeRequestKey = "isoCountryCode";
	protected String retrieveChildrenRequestKey = "retrieveChildren";
	protected String ascendingRequestKey = "ascending";
	protected String includeCountsRequestKey = "includeCounts";
	protected String includeHigherRanksRequestKey = "includeHigherRanks";
	
	/**
	 * @see org.gbif.portal.web.controller.search.NameSearchController#handleSearch(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.ModelAndView, org.gbif.portal.web.controller.search.bean.NameSearch)
	 */
	@Override
	public void handleSearch(Map<String, String> propertiesMap,
			HttpServletRequest request, HttpServletResponse response,
			ModelAndView mav, NameSearch nameSearch) throws Exception {

		String query = nameSearch.getQuery();
		String conceptKey = null;
		String scientificName = null;
		
		try{
			Integer.parseInt(query);
			conceptKey=query;
		} catch(NumberFormatException e){
			scientificName = query;
		}
		
		String isoCountryCode = ServletRequestUtils.getStringParameter(request, isoCountryCodeRequestKey);
		boolean retrieveChildren = ServletRequestUtils.getBooleanParameter(request, retrieveChildrenRequestKey, true);
		boolean ascending = ServletRequestUtils.getBooleanParameter(request, ascendingRequestKey, false);
		boolean allowUnconfirmed = ServletRequestUtils.getBooleanParameter(request, allowUnconfirmedRequestKey, false);
		boolean includeCounts = ServletRequestUtils.getBooleanParameter(request, includeCountsRequestKey, false);
		boolean includeHigherRanks = ServletRequestUtils.getBooleanParameter(request, includeHigherRanksRequestKey, true);
		String view = ServletRequestUtils.getStringParameter(request, viewRequestKey, defaultSubView);
		
		//special case for pygmy - as higher ranks arent required, and counts are compulsory
		if("pygmy".equalsIgnoreCase(view)){
			includeCounts=true;
			includeHigherRanks=false;
		}
		
		//if descending does it make sense to provide child concepts ?
		if(ascending){
			retrieveChildren = false;	
		}
		
		//the taxonomic provider
		String taxonomicProvider = null;
		//the concepts to render
		List<BriefTaxonConceptDTO> concepts = null;
		//selected concept
		BriefTaxonConceptDTO selectedConcept = null;

		//if a concept is selected
		if(StringUtils.isNotEmpty(conceptKey)){

			//get classification
			concepts = taxonomyManager.getClassificationFor(conceptKey, includeHigherRanks, retrieveChildren, isoCountryCode, includeCounts, allowUnconfirmed);
			//find selected concept
			selectedConcept = getSelectedConcept(concepts, conceptKey);
			
			if(concepts!=null && !concepts.isEmpty()){
				
				BriefTaxonConceptDTO concept = concepts.get(0);
				TaxonConceptDTO tc = taxonomyManager.getTaxonConceptFor(concept.getKey());
				DataResourceDTO dataResource = dataResourceManager.getDataResourceFor(tc.getDataResourceKey());
				
				if(concept.getIsNubConcept()){
					taxonomicProvider = dataResourceManager.getNubDataProvider().getName();
				} else if (dataResource.isSharedTaxonomy()){
					DataProviderDTO dataProvider = dataResourceManager.getDataProviderFor(tc.getDataProviderKey());						
					taxonomicProvider = dataProvider.getName();
				} else {
					taxonomicProvider = dataResource.getName();						
				}
			}
		} else {
			
			if(StringUtils.isNotEmpty(isoCountryCode)){
				//if the iso country code is supplied then it has to be the nub
				DataProviderDTO nubProvider = dataResourceManager.getNubDataProvider();
				if(StringUtils.isNotEmpty(scientificName)){
					
					SearchResultsDTO searchResults = taxonomyManager.findTaxonConcepts(scientificName, false, null, nubProvider.getKey(), null, null, null, null, null, allowUnconfirmed, false, new SearchConstraints(0,1));
					if(!searchResults.isEmpty()){
						selectedConcept = (BriefTaxonConceptDTO) searchResults.get(0);
						concepts = taxonomyManager.getClassificationFor(selectedConcept.getKey(), includeHigherRanks, retrieveChildren, isoCountryCode, includeCounts, allowUnconfirmed);
					}
				} else {
					concepts = taxonomyManager.getRootTaxonConceptsForCountry(isoCountryCode);
				}
				taxonomicProvider = nubProvider.getName();

			} else {

				//resolve the classification
				Object dataset = getSpecifiedClassification(request);
				String dataProviderKey = null; 
				String dataResourceKey = null;
				
				//find the dataset to use
				if(dataset instanceof DataProviderDTO){
					DataProviderDTO dataProviderDTO = (DataProviderDTO) dataset;
					dataProviderKey = dataProviderDTO.getKey();
					taxonomicProvider = dataProviderDTO.getName();
				} else if(dataset instanceof DataResourceDTO){
					dataResourceKey = ((DataResourceDTO) dataset).getKey();
					taxonomicProvider = dataResourceManager.getNubDataProvider().getName();
				}
				
				//if a sci name is provided search for this
				if(StringUtils.isNotEmpty(scientificName)){
					SearchResultsDTO searchResults = taxonomyManager.findTaxonConcepts(scientificName, false, null, dataProviderKey, dataResourceKey, null, null, null, null, allowUnconfirmed, false, new SearchConstraints(0,1));
					if(!searchResults.isEmpty()){
//						searchResults = taxonomyManager.findTaxonConceptsForCommonName(scientificName, false, new SearchConstraints(0,1))
						selectedConcept = (BriefTaxonConceptDTO) searchResults.get(0);
						concepts = taxonomyManager.getClassificationFor(selectedConcept.getKey(), includeHigherRanks, retrieveChildren, isoCountryCode, includeCounts, allowUnconfirmed);
					}
				} else {
					concepts = taxonomyManager.getRootTaxonConceptsForTaxonomy(dataProviderKey, dataResourceKey);
				}
			}
		}
		
		if(ascending){
			//re-sort
			Collections.reverse(concepts);
		}
		
		mav.addObject("includeCounts", includeCounts);
		
		if(taxonomicProvider!=null)
			mav.addObject("taxonomicProvider", taxonomicProvider);
		if(concepts!=null)
			mav.addObject("concepts", concepts);
		if(selectedConcept!=null)
			mav.addObject("selectedConcept", selectedConcept);
	}

	protected Object getSpecifiedClassification(HttpServletRequest request) throws Exception{
		String classificationName = ServletRequestUtils.getStringParameter(request, classificationRequestKey);
		String dataProviderKey = ServletRequestUtils.getStringParameter(request, providerIdRequestKey);
		String dataResourceKey = ServletRequestUtils.getStringParameter(request, resourceIdRequestKey);
		Object dataset = null;
		
		//search for classification
		if(classificationName!=null){
			SearchResultsDTO searchResultsDTO = dataResourceManager.findDatasets(classificationName, true, false, false, new SearchConstraints(0,1));
			if(!searchResultsDTO.isEmpty()){
				dataset = searchResultsDTO.get(0);
					return dataset;
			} 
		} else if (dataProviderKey!=null){
			dataset = dataResourceManager.getDataProviderFor(dataProviderKey);
		} else if (dataResourceKey!=null){
			dataset = dataResourceManager.getDataResourceFor(dataResourceKey);
		}

		if(dataset!=null)
			return dataset;
		
		//if no other classification resolved use the nub
		return dataResourceManager.getNubDataProvider();
	}
	
	/**
	 * @see org.gbif.portal.web.controller.search.NameSearchController#getDefaultModelAndView()
	 */
	@Override
	protected ModelAndView getDefaultModelAndView(Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Object dataset = getSpecifiedClassification(request);
		//the concepts to render
		List<BriefTaxonConceptDTO> concepts = null;		
		String isoCountryCode = ServletRequestUtils.getStringParameter(request, isoCountryCodeRequestKey);
		
		String dataProviderKey = null; 
		String dataResourceKey = null;		
		String taxonomicProvider = null;

		if(StringUtils.isNotEmpty(isoCountryCode)){
			//if the iso country code is supplied then it has to be the nub
			concepts = taxonomyManager.getRootTaxonConceptsForCountry(isoCountryCode);
			taxonomicProvider = dataResourceManager.getNubDataProvider().getName();

		} else {	
		
			if(dataset instanceof DataProviderDTO){
				DataProviderDTO dataProviderDTO = (DataProviderDTO) dataset;
				dataProviderKey = dataProviderDTO.getKey();
				taxonomicProvider = dataProviderDTO.getName();
			} else if(dataset instanceof DataResourceDTO){
				DataResourceDTO dataResourceDTO = ((DataResourceDTO) dataset);
				dataResourceKey = dataResourceDTO.getKey();
				taxonomicProvider = dataResourceDTO.getName();
			}		
		
			concepts = taxonomyManager.getRootTaxonConceptsForTaxonomy(dataProviderKey, dataResourceKey);
		}
		
		String returnType = ServletRequestUtils.getStringParameter(request, returnTypeRequestKey, defaultReturnType);
		String view = ServletRequestUtils.getStringParameter(request, viewRequestKey, defaultView);
		String callback = ServletRequestUtils.getStringParameter(request, callbackRequestKey, "");
		ModelAndView mav = resolveAndCreateView(viewNamePrefix, returnType, view);
		mav.addObject("taxonomicProvider", taxonomicProvider);
		mav.addObject("concepts", concepts);
		mav.addObject("callback", callback);
		return mav;
	}	
	
	
	private BriefTaxonConceptDTO getSelectedConcept(List<BriefTaxonConceptDTO> concepts, String conceptKey) {
		for(BriefTaxonConceptDTO concept: concepts){
			if(concept.getKey().equals(conceptKey))
				return concept;
		}
		return null;
	}

	/**
	 * @param allowUnconfirmedRequestKey the allowUnconfirmedRequestKey to set
	 */
	public void setAllowUnconfirmedRequestKey(String allowUnconfirmedRequestKey) {
		this.allowUnconfirmedRequestKey = allowUnconfirmedRequestKey;
	}

	/**
	 * @param classificationRequestKey the classificationRequestKey to set
	 */
	public void setClassificationRequestKey(String classificationRequestKey) {
		this.classificationRequestKey = classificationRequestKey;
	}

	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}

	/**
	 * @param isoCountryCodeRequestKey the isoCountryCodeRequestKey to set
	 */
	public void setIsoCountryCodeRequestKey(String isoCountryCodeRequestKey) {
		this.isoCountryCodeRequestKey = isoCountryCodeRequestKey;
	}

	/**
	 * @param providerIdRequestKey the providerIdRequestKey to set
	 */
	public void setProviderIdRequestKey(String providerIdRequestKey) {
		this.providerIdRequestKey = providerIdRequestKey;
	}

	/**
	 * @param rankRequestKey the rankRequestKey to set
	 */
	public void setRankRequestKey(String rankRequestKey) {
		this.rankRequestKey = rankRequestKey;
	}

	/**
	 * @param resourceIdRequestKey the resourceIdRequestKey to set
	 */
	public void setResourceIdRequestKey(String resourceIdRequestKey) {
		this.resourceIdRequestKey = resourceIdRequestKey;
	}

	/**
	 * @param retrieveChildrenRequestKey the retrieveChildrenRequestKey to set
	 */
	public void setRetrieveChildrenRequestKey(String retrieveChildrenRequestKey) {
		this.retrieveChildrenRequestKey = retrieveChildrenRequestKey;
	}

	/**
	 * @param taxonomyManager the taxonomyManager to set
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
	}
}