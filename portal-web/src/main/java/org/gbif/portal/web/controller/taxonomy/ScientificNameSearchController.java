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

import org.gbif.portal.dto.DTOUtils;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.dto.taxonomy.CommonNameDTO;
import org.gbif.portal.dto.taxonomy.TaxonConceptDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.dto.util.TaxonRankType;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.web.controller.search.NameSearchController;
import org.gbif.portal.web.controller.search.bean.NameSearch;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * A name search controller supporting scientific and common name searches.
 * 
 * @author dmartin
 */
public class ScientificNameSearchController extends NameSearchController {
	
	/** Service layer manager for queries */
	protected TaxonomyManager taxonomyManager;	
	/** Service layer manager for queries */
	protected DataResourceManager dataResourceManager;		
	
	protected String scientificNameType = "scientific";
	protected String commonNameType = "common";
	
	/** defaults */
	protected String defaultReturnType = "name";
	protected int defaultMaxResults = 10;
	protected int defaultStartIndex = 0;	
	protected String defaultPrioritise = scientificNameType;
	protected String defaultNameType = "both";

	protected String nameTypeRequestKey = "nameType";	
	
	/** The absolute limit on max results returned */
	protected int maxResultsLimit = 1000;
	
	/** Sci name specifics */
	protected String prioritiseRequestKey = "prioritise";
	/** The rank to search */
	protected String rankRequestKey = "rank";
	/** special case for rank */
	protected String scientificNameKeyword = "scientificName";
	/** allow unconfirmed flag */
	protected String allowUnconfirmedRequestKey = "allowUnconfirmed";	
	
	/** non publicly disclosed keys */
	protected String providerRequestKey = "provider";
	protected String resourceRequestKey = "resource";
	protected String providerIdRequestKey = "providerId";
	protected String resourceIdRequestKey = "resourceId";
	
	protected String nameReturnType = "name";
	protected String nameIdReturnType = "nameId";
	protected String mapReturnType = "nameIdMap";
	protected String commonNameReturnType = "commonName";
	
	protected String searchResultsModelKey = "searchResults";
	
	/** exact matches only */
	protected String exactMatchOnlyRequestKey = "exactOnly";

	@Override
	public void handleSearch(Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response, ModelAndView mav, NameSearch nameSearch) throws Exception{
		
		//get sci name specific request properties
		String rank = request.getParameter(rankRequestKey);
		boolean allowUnconfirmed = ServletRequestUtils.getBooleanParameter(request, allowUnconfirmedRequestKey, false);
		String nameType = ServletRequestUtils.getStringParameter(request, nameTypeRequestKey, defaultNameType);
		String prioritise = ServletRequestUtils.getStringParameter(request, prioritiseRequestKey, defaultPrioritise);

		//find common names using a supplied sci name
		if(commonNameReturnType.equals(nameSearch.getReturnType())){
			handleCommonNameForSciNameSearch(mav, nameSearch.getQuery(), rank, allowUnconfirmed, nameSearch.getSearchConstraints());
			return;
		}
		
		//concept search
		if(!nameReturnType.equals(nameSearch.getReturnType()) && supportedReturnTypes.contains(nameSearch.getReturnType())){
			if(commonNameType.equals(nameType)){
				handleCommonNameConceptSearch(mav, nameSearch.getQuery(), nameSearch.isExactMatchOnly(), nameSearch.isSoundex(), nameSearch.getSearchConstraints());
				return;
			} else {
				handleSciNameConceptSearch(mav, request, nameSearch.getQuery(), nameSearch.isExactMatchOnly(), nameSearch.isSoundex(), rank, allowUnconfirmed, nameSearch.getSearchConstraints());
				return;
			}
		}

		// if name, name should be unique
		if(scientificNameType.equals(nameType)){
			handleScientificNameSearch(mav, request, nameSearch.getQuery(), nameSearch.isExactMatchOnly(), nameSearch.isSoundex(), rank, allowUnconfirmed, nameSearch.getSearchConstraints());
			return;
		} else if(commonNameType.equals(nameType)){
			handleCommonNameSearch(mav, nameSearch.getQuery(), nameSearch.isExactMatchOnly(), nameSearch.getSearchConstraints());
			return;
		} else {
			handleNameSearch(mav, request, nameSearch.getQuery(), nameSearch.isExactMatchOnly(), nameSearch.isSoundex(), rank, prioritise, allowUnconfirmed, nameSearch.getSearchConstraints());
			return;
		}	
	}		
	
	/**
	 * Retrieve common names for the supplied scientific name
	 * 
	 * @param mav
	 * @param query
	 * @param rank
	 * @param allowUnconfirmed
	 * @param searchConstraints
	 * @return
	 */
	public ModelAndView handleCommonNameForSciNameSearch(ModelAndView mav, String query, String rank, boolean allowUnconfirmed, SearchConstraints searchConstraints) {
		try {
			DataProviderDTO dataProviderDTO = dataResourceManager.getNubDataProvider();			
			SearchResultsDTO sr = taxonomyManager.findTaxonConcepts(query, false, rank, dataProviderDTO.getKey(), null, null, null, null, null, allowUnconfirmed, false, new SearchConstraints(0,1));
			if(sr!=null && !sr.isEmpty()){
				TaxonConceptDTO tc = (TaxonConceptDTO) sr.get(0);
				List<CommonNameDTO> commonNames = taxonomyManager.findCommonNamesForTaxonConcept(tc.getKey(), searchConstraints);
				sr = new SearchResultsDTO();
				DTOUtils.populate(sr, commonNames, searchConstraints.getMaxResults());
				mav.addObject(searchResultsModelKey, sr);
			}
		} catch (ServiceException e){
			logger.error(e.getMessage(), e);
		}
		return mav;
	}

	/**
	 * Retrieves a list of matching common names. 
	 * 
	 * @param propertiesMap
	 * @param request
	 * @param response
	 * @param query
	 * @param b 
	 * @param view
	 * @return
	 */
	public ModelAndView handleCommonNameSearch(ModelAndView mav, String query, boolean exactMatchOnly, SearchConstraints sc) {
		try {
			SearchResultsDTO searchResults = taxonomyManager.findMatchingCommonNames(query, !exactMatchOnly, sc);
			mav.addObject(searchResultsModelKey, searchResults);
		} catch (ServiceException e){
			logger.error(e.getMessage(), e);
		}
		return mav;
	}	

	/**
	 * Retrieves a list of matching scientific names. 
	 * 
	 * @param propertiesMap
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView handleScientificNameSearch(ModelAndView mav, HttpServletRequest request, String query, boolean exactMatchOnly, boolean soundex, String rank, boolean allowUnconfirmed, SearchConstraints sc) throws Exception {

		DataProviderDTO dataProviderDTO = dataResourceManager.getNubDataProvider();
		String provider = getProviderKey(request, dataProviderDTO.getKey());
		String resource = getResourceKey(request, null);
		SearchResultsDTO searchResults = null;
		if(rank!=null && rank.equals(scientificNameKeyword)){
			searchResults = taxonomyManager.findMatchingScientificNames(query, !exactMatchOnly, TaxonRankType.GENUS, Boolean.FALSE, soundex, provider, resource, allowUnconfirmed, sc);
		} else {
			searchResults = taxonomyManager.findMatchingScientificNames(query, !exactMatchOnly, TaxonRankType.getRank(rank), null, soundex, provider, resource, allowUnconfirmed, sc);
		}
		mav.addObject(searchResultsModelKey, searchResults);
		return mav;
	}

	private String getProviderKey(HttpServletRequest request, String defaultKey) {
		String provider = ServletRequestUtils.getStringParameter(request, providerRequestKey, null);
		if(provider!=null)
			return provider;
		provider = ServletRequestUtils.getStringParameter(request, providerIdRequestKey, null);
		if(provider!=null)
			return provider;
		return defaultKey;
	}

	private String getResourceKey(HttpServletRequest request, String defaultKey) {
		String resource = ServletRequestUtils.getStringParameter(request, resourceRequestKey, null);
		if(resource!=null)
			return resource;
		resource = ServletRequestUtils.getStringParameter(request, resourceIdRequestKey, null);
		if(resource!=null)
			return resource;
		return defaultKey;
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
	public ModelAndView handleNameSearch(ModelAndView mav , HttpServletRequest request,  String query, boolean exactMatchOnly, boolean soundex, String rank, String prioritise, boolean allowUnconfirmed, SearchConstraints sc) throws Exception {

		DataProviderDTO dataProviderDTO = dataResourceManager.getNubDataProvider();
		String provider = getProviderKey(request, dataProviderDTO.getKey());
		String resource = getResourceKey(request, null);
		
		boolean prioritiseCommon = commonNameType.equals(prioritise);
		
		SearchResultsDTO searchResults = null;
		
		if(prioritiseCommon){
			searchResults = taxonomyManager.findMatchingCommonNames(query, true, sc);
		} else {
			if(rank!=null && rank.equals(scientificNameKeyword)){
				searchResults = taxonomyManager.findMatchingScientificNames(query, !exactMatchOnly, TaxonRankType.GENUS, Boolean.FALSE, soundex, provider, resource, allowUnconfirmed, sc);
			} else {
				searchResults = taxonomyManager.findMatchingScientificNames(query, !exactMatchOnly, TaxonRankType.getRank(rank), null, soundex, provider, resource, allowUnconfirmed, sc);
			}
		}
		
		SearchResultsDTO additionalResults = null;
		
		if(prioritiseCommon){
			if(rank!=null && rank.equals(scientificNameKeyword)){
				additionalResults = taxonomyManager.findMatchingScientificNames(query, !exactMatchOnly, TaxonRankType.GENUS, Boolean.FALSE, soundex, provider, resource, allowUnconfirmed, sc);
			} else {
				additionalResults = taxonomyManager.findMatchingScientificNames(query, !exactMatchOnly, TaxonRankType.getRank(rank), null, soundex, provider, resource, allowUnconfirmed, sc);
			}
		} else {
			additionalResults = taxonomyManager.findMatchingCommonNames(query, true, sc);
			
		}

		//additional results
		List<String> additionalNames = additionalResults.getResults();
		//collated results - to be returned
		List<String> collatedResults = searchResults.getResults();
		
		for(String name: additionalNames){
			if(collatedResults.size()<sc.getMaxResults()){
				if(!collatedResults.contains(name))
					collatedResults.add(name);
			} else {
				searchResults.setHasMoreResults(true);
				break;
			}
		}
		//sort alphabetically
		Collections.sort(collatedResults);
		mav.addObject(searchResultsModelKey, searchResults);
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
	public ModelAndView handleSciNameConceptSearch(ModelAndView mav, HttpServletRequest request, String query, boolean exactMatchOnly, boolean soundex, String rank, boolean allowUnconfirmedNames, SearchConstraints searchConstraints) throws Exception {
		DataProviderDTO dataProviderDTO = dataResourceManager.getNubDataProvider();
		String provider = getProviderKey(request, dataProviderDTO.getKey());
		String resource = getResourceKey(request, null);
		
		//if fuzzy, make sure exact matches are first
		SearchResultsDTO searchResults = null;
		if(!exactMatchOnly){
			SearchResultsDTO exactSearchResults = taxonomyManager.findTaxonConcepts(query, false, rank, provider, resource, null, null, RequestContextUtils.getLocale(request).getLanguage(), null, allowUnconfirmedNames, false, new SearchConstraints(0,1));			
			searchResults = taxonomyManager.findTaxonConcepts(query, true, rank, provider, resource, null, null, RequestContextUtils.getLocale(request).getLanguage(), null, allowUnconfirmedNames, false, searchConstraints);
			if(!exactSearchResults.isEmpty()){
				BriefTaxonConceptDTO exactMatch =  (BriefTaxonConceptDTO) exactSearchResults.get(0);
				//move the exact match to the front
				if(searchResults.contains(exactMatch))
					searchResults.remove(exactMatch);
				searchResults.add(0,exactMatch);
			}
		} else {
			searchResults = taxonomyManager.findTaxonConcepts(query, false, rank, provider, resource, null, null, RequestContextUtils.getLocale(request).getLanguage(), null, allowUnconfirmedNames, false, searchConstraints);
		}
		
		if(searchResults.isEmpty()){
			searchResults = taxonomyManager.findTaxonConceptsForCommonName(query, !exactMatchOnly, searchConstraints);
		}
		mav.addObject(searchResultsModelKey, searchResults);
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
	public ModelAndView handleCommonNameConceptSearch(ModelAndView mav, String query, boolean exactMatchOnly, boolean soundex, SearchConstraints searchConstraints) throws Exception {
		SearchResultsDTO searchResults = taxonomyManager.findTaxonConceptsForCommonName(query, !exactMatchOnly, searchConstraints);
		if(searchResults.isEmpty()){
			searchResults = taxonomyManager.findTaxonConceptsForCommonName(query, !exactMatchOnly, searchConstraints);
		}
		mav.addObject(searchResultsModelKey, searchResults);
		mav.addObject("showCommonName", true);
		return mav;
	}		
	
	/**
	 * @param taxonomyManager the taxonomyManager to set
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
	}

	/**
	 * @param allowUnconfirmedRequestKey the allowUnconfirmedRequestKey to set
	 */
	public void setAllowUnconfirmedRequestKey(String allowUnconfirmedRequestKey) {
		this.allowUnconfirmedRequestKey = allowUnconfirmedRequestKey;
	}

	/**
	 * @param commonNameType the commonNameType to set
	 */
	public void setCommonNameType(String commonNameType) {
		this.commonNameType = commonNameType;
	}

	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}

	/**
	 * @param defaultPrioritise the defaultPrioritise to set
	 */
	public void setDefaultPrioritise(String defaultPrioritise) {
		this.defaultPrioritise = defaultPrioritise;
	}

	/**
	 * @param mapReturnType the mapReturnType to set
	 */
	public void setMapReturnType(String mapReturnType) {
		this.mapReturnType = mapReturnType;
	}

	/**
	 * @param maxResultsLimit the maxResultsLimit to set
	 */
	public void setMaxResultsLimit(int maxResultsLimit) {
		this.maxResultsLimit = maxResultsLimit;
	}

	/**
	 * @param maxResultsRequestKey the maxResultsRequestKey to set
	 */
	public void setMaxResultsRequestKey(String maxResultsRequestKey) {
		this.maxResultsRequestKey = maxResultsRequestKey;
	}

	/**
	 * @param nameIdReturnType the nameIdReturnType to set
	 */
	public void setNameIdReturnType(String nameIdReturnType) {
		this.nameIdReturnType = nameIdReturnType;
	}

	/**
	 * @param nameReturnType the nameReturnType to set
	 */
	public void setNameReturnType(String nameReturnType) {
		this.nameReturnType = nameReturnType;
	}

	/**
	 * @param nameTypeRequestKey the nameTypeRequestKey to set
	 */
	public void setNameTypeRequestKey(String nameTypeRequestKey) {
		this.nameTypeRequestKey = nameTypeRequestKey;
	}

	/**
	 * @param prioritiseRequestKey the prioritiseRequestKey to set
	 */
	public void setPrioritiseRequestKey(String prioritiseRequestKey) {
		this.prioritiseRequestKey = prioritiseRequestKey;
	}

	/**
	 * @param providerRequestKey the providerRequestKey to set
	 */
	public void setProviderRequestKey(String providerRequestKey) {
		this.providerRequestKey = providerRequestKey;
	}

	/**
	 * @param queryRequestKey the queryRequestKey to set
	 */
	public void setQueryRequestKey(String queryRequestKey) {
		this.queryRequestKey = queryRequestKey;
	}

	/**
	 * @param rankRequestKey the rankRequestKey to set
	 */
	public void setRankRequestKey(String rankRequestKey) {
		this.rankRequestKey = rankRequestKey;
	}

	/**
	 * @param resourceRequestKey the resourceRequestKey to set
	 */
	public void setResourceRequestKey(String resourceRequestKey) {
		this.resourceRequestKey = resourceRequestKey;
	}

	/**
	 * @param scientificNameKeyword the scientificNameKeyword to set
	 */
	public void setScientificNameKeyword(String scientificNameKeyword) {
		this.scientificNameKeyword = scientificNameKeyword;
	}

	/**
	 * @param scientificNameType the scientificNameType to set
	 */
	public void setScientificNameType(String scientificNameType) {
		this.scientificNameType = scientificNameType;
	}

	/**
	 * @param commonNameReturnType the commonNameReturnType to set
	 */
	public void setCommonNameReturnType(String commonNameReturnType) {
		this.commonNameReturnType = commonNameReturnType;
	}

	/**
	 * @param defaultMaxResults the defaultMaxResults to set
	 */
	public void setDefaultMaxResults(int defaultMaxResults) {
		this.defaultMaxResults = defaultMaxResults;
	}

	/**
	 * @param defaultNameType the defaultNameType to set
	 */
	public void setDefaultNameType(String defaultNameType) {
		this.defaultNameType = defaultNameType;
	}

	/**
	 * @param defaultReturnType the defaultReturnType to set
	 */
	public void setDefaultReturnType(String defaultReturnType) {
		this.defaultReturnType = defaultReturnType;
	}

	/**
	 * @param defaultStartIndex the defaultStartIndex to set
	 */
	public void setDefaultStartIndex(int defaultStartIndex) {
		this.defaultStartIndex = defaultStartIndex;
	}

	/**
	 * @param exactMatchOnlyRequestKey the exactMatchOnlyRequestKey to set
	 */
	public void setExactMatchOnlyRequestKey(String exactMatchOnlyRequestKey) {
		this.exactMatchOnlyRequestKey = exactMatchOnlyRequestKey;
	}

	/**
	 * @param searchResultsModelKey the searchResultsModelKey to set
	 */
	public void setSearchResultsModelKey(String searchResultsModelKey) {
		this.searchResultsModelKey = searchResultsModelKey;
	}
}