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
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.dto.util.TaxonRankType;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.web.controller.RestKeyValueController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Simple controller for taxon name and concept searches. Initial intention was support for
 * ajax autocomplete methods.
 * 
 * Note: this is in use by third parties
 * 
 * @author dmartin
 * @deprecated
 */
public class TaxonNameSearchController extends RestKeyValueController {

	/** Service layer manager for queries */
	protected TaxonomyManager taxonomyManager;

	/** The maximum number of results to display */
	protected int maxResults = 10;
	
	protected String scientificNameKeyword = "scientificName";

	protected String defaultNameSearchView = "ajaxTaxonName";
	
	protected String searchResultsModelKey = "searchResults";
	/** Whether to match unconfirmed names */
	protected boolean allowUnconfirmedNames = true;	
	
	/**
	 * @see org.gbif.portal.web.controller.RestController#handleRequest(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ModelAndView handleRequest(Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String query = request.getParameter("query");
		String view = propertiesMap.get("view");
		String returnType = propertiesMap.get("returnType");
		if("concept".equals(returnType)){
			if("ajaxMapUrls".equals(view))
				return handleSingleConceptNameSearch(propertiesMap, request, response, query, view);
			return handleConceptSearch(propertiesMap, request, response, query, view);
		} else if("commonName".equals(returnType)) {
			return handleCommonNameSearch(propertiesMap, request, response, query, view);
		} else if("name".equals(returnType)) {
			return handleNameSearch(propertiesMap, request, response, query, view);
		} else {
			return handleScientificNameSearch(propertiesMap, request, response, query, view);
		}
	}

	private ModelAndView handleSingleConceptNameSearch(Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response, String query, String view) throws Exception {
		String rank = propertiesMap.get("rank");
		String provider = propertiesMap.get("provider");
		String resource = propertiesMap.get("resource");
		ModelAndView mav = new ModelAndView(view);
		SearchResultsDTO searchResults = taxonomyManager.findTaxonConceptsForCommonName(query, true, new SearchConstraints(0,1)); 
		if(searchResults.isEmpty()){
			searchResults = taxonomyManager.findTaxonConcepts(query, true, rank, provider, resource, null, null, RequestContextUtils.getLocale(request).getLanguage(), null, allowUnconfirmedNames, false, new SearchConstraints(0,1));
		}
		mav.addObject(searchResultsModelKey, searchResults);
		return mav;	
	}

	/**
	 * Retrieves a list of matching common names. 
	 * 
	 * @param propertiesMap
	 * @param request
	 * @param response
	 * @param query
	 * @param view
	 * @return
	 */
	public  ModelAndView handleCommonNameSearch(Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response, String query, String view) {
		ModelAndView mav = new ModelAndView(view);
		try {
			SearchResultsDTO searchResults = taxonomyManager.findMatchingCommonNames(query, true, new SearchConstraints(0,maxResults));
			mav.addObject(searchResultsModelKey, searchResults);
		} catch (ServiceException e){
			logger.error(e.getMessage(), e);
		}
		return mav;
	}

	/**
	 * Handle concept searches
	 * 
	 * @param propertiesMap
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView handleConceptSearch(Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response, String query, String view) throws Exception {
		String rank = propertiesMap.get("rank");
		String provider = propertiesMap.get("provider");
		String resource = propertiesMap.get("resource");
		ModelAndView mav = new ModelAndView(view);
		SearchResultsDTO searchResults = taxonomyManager.findTaxonConcepts(query, true, rank, provider, resource, null, null, RequestContextUtils.getLocale(request).getLanguage(), null, allowUnconfirmedNames, false, new SearchConstraints(0,maxResults));
		if(searchResults!=null && searchResults.isEmpty()){
			searchResults = taxonomyManager.findTaxonConceptsForCommonName(query, true, new SearchConstraints(0,maxResults));
		}
		mav.addObject(searchResultsModelKey, searchResults);
		return mav;
	}

	/**
	 * Handle simple name searches
	 * 
	 * @param propertiesMap
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView handleScientificNameSearch(Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response, String query, String view) throws Exception {

		String rank = propertiesMap.get("rank");
		String provider = propertiesMap.get("provider");
		String resource = propertiesMap.get("resource");		
		
		if(StringUtils.isNotEmpty(view))
			view = defaultNameSearchView;
		
		ModelAndView mav = new ModelAndView(view);

		SearchResultsDTO searchResults = null;
		if(rank!=null && rank.equals(scientificNameKeyword)){
			searchResults = taxonomyManager.findMatchingScientificNames(query, true, TaxonRankType.GENUS, Boolean.FALSE, false, provider, resource, true, new SearchConstraints(0,maxResults));
		} else {
			searchResults = taxonomyManager.findMatchingScientificNames(query, true, TaxonRankType.getRank(rank), null, false, provider, resource, true, new SearchConstraints(0,maxResults));
		}
		mav.addObject(searchResultsModelKey, searchResults);
		return mav;
	}
	
	/**
	 * Handle simple name searches
	 * 
	 * @param propertiesMap
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView handleNameSearch(Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response, String query, String view) throws Exception {
		String provider = propertiesMap.get("provider");
		String resource = propertiesMap.get("resource");		
		
		if(StringUtils.isNotEmpty(view))
			view = defaultNameSearchView;
		
		ModelAndView mav = new ModelAndView(view);
		SearchResultsDTO sciNameResults = taxonomyManager.findMatchingScientificNames(query, true, null, null, false, provider, resource, true, new SearchConstraints(0,maxResults));
		if(sciNameResults.size()<maxResults){
			//add common names if not enough
			SearchResultsDTO commonNameResults = taxonomyManager.findMatchingCommonNames(query, true, new SearchConstraints(0,maxResults));
			List<String> sciNames = sciNameResults.getResults();
			List<String> commonNames = commonNameResults.getResults();
			
			//add common names to make up max results, but dont add duplicates
			for(String commonName: commonNames){
				if(sciNames.size()<maxResults)
					if(!sciNames.contains(commonName))
						sciNames.add(commonName);
			}
			Collections.sort(sciNames);
		}
		mav.addObject(searchResultsModelKey, sciNameResults);
		return mav;
	}	
	
	/**
	 * @param taxonomyManager the taxonomyManager to set
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
	}
}