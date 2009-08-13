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
package org.gbif.portal.web.view.search;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.dto.resources.ResourceNetworkDTO;
import org.gbif.portal.dto.taxonomy.TaxonConceptDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.GeospatialManager;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.web.util.QueryHelper;
import org.gbif.portal.web.view.WidgetControllerSupport;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Widgets for blanket search screen.
 * 
 * @author dmartin
 */
public class SearchWidgetController extends WidgetControllerSupport {
	
	/** Search string attribute - would ideally be spring configured but how? */
	public static final String SEARCH_STRING_ATTR = "searchString";
	public static final Integer DEFAULT_MAX_RESULTS = 10;
	public static final Integer DEFAULT_START_INDEX_RESULTS = 0;
	/** Whether to allow unconfirmed names for name resolving */
	protected boolean allowUnconfirmedNames = true;	
	protected int minLengthToSort = 6;
	
	/**
	 * Adds content for the taxon name search widget. The ordering should be like so:
	 * 
	 * 1. Exact scientific name match
	 * 2. Exact specific epithet matches
	 * 3. Other scientific name matches
	 * 
	 * TODO Add Scientific name parsing
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void addTaxa(HttpServletRequest request, HttpServletResponse response)  throws Exception{
		
		String searchString = getSearchString(request);		

		TaxonomyManager taxonomyManager = (TaxonomyManager) getWebAppContext(request).getBean("taxonomyManager");
		DataResourceManager dataResourceManager = (DataResourceManager) getWebAppContext(request).getBean("dataResourceManager");

		//use the nub provider
		DataProviderDTO nubProvider = dataResourceManager.getNubDataProvider();
		String providerKey = null;
		if(nubProvider!=null && nubProvider.getKey()!=null)
			providerKey = nubProvider.getKey();
		
		//exact scientific name matches
		SearchResultsDTO exactTaxonMatches = taxonomyManager.findTaxonConcepts(searchString, false, null, providerKey, null, null, null, RequestContextUtils.getLocale(request).getLanguage(), null, allowUnconfirmedNames, false, new SearchConstraints(0,100));
		request.setAttribute("exactTaxonMatches", exactTaxonMatches);
		
		//exact specific epithet matches
		SearchResultsDTO exactEpithetMatches = taxonomyManager.findSpeciesConcepts(null, searchString, false, providerKey, null, null, new SearchConstraints(0,100));
		request.setAttribute("exactEpithetMatches", exactEpithetMatches);
		
		int matchesTotal = exactTaxonMatches.size() + exactEpithetMatches.size();
		boolean hasMore = false; //shall we provide a paging link
		
		SearchConstraints searchConstraints = getSearchConstraints(request);
		
		if(matchesTotal<=DEFAULT_MAX_RESULTS){

			boolean sortAlpha = false;
			if(searchString!=null && searchString.length()>minLengthToSort){
				sortAlpha = true;
			}
			
			//exact scientific name matches
			SearchResultsDTO fuzzyTaxonMatches = taxonomyManager.findTaxonConcepts(searchString, true, null, providerKey, null, null, null, RequestContextUtils.getLocale(request).getLanguage(), null, allowUnconfirmedNames, sortAlpha, searchConstraints);
			//remove the exact matches
			for (Object exactMatch: exactTaxonMatches){
				if(fuzzyTaxonMatches.contains(exactMatch))
					fuzzyTaxonMatches.remove(exactMatch);
			}
			request.setAttribute("fuzzyTaxonMatches", fuzzyTaxonMatches);
			matchesTotal += fuzzyTaxonMatches.size();
			hasMore = fuzzyTaxonMatches.hasMoreResults();
		} else {
			hasMore = true;
		}
		
		// if there are still no results, then try a soundex
		if(matchesTotal==0){
			logger.debug("No names found, trying the SoundEx search");
			SearchResultsDTO soundExMatches = taxonomyManager.findMatchingScientificNames(searchString, true, null, null, true, providerKey, null, true, new SearchConstraints(0, DEFAULT_MAX_RESULTS));
			request.setAttribute("soundexNameMatches", soundExMatches);
			matchesTotal = soundExMatches.size();
			// it's just a suggestion list so no need for paging
			hasMore = false;
		}
		
		if(matchesTotal==0){
			//match on remote concept ids
			List<TaxonConceptDTO> taxonConcepts = taxonomyManager.getTaxonConceptForRemoteId(searchString);
			request.setAttribute("remoteConceptMatches", taxonConcepts);	
		}
		
		request.setAttribute("moreTaxaMatches", hasMore);
	}
	
	/**
	 * Adds the content for the common names widget. The ordering should be like so:
	 * 
	 * 1. Common names starting with search string
	 * 2. Common names with other words starting with search string (e.g. return "African Elephant" if user searches for "elephant")
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void addCommonNames(HttpServletRequest request, HttpServletResponse response)  throws Exception{
		String searchString = getSearchString(request);	
		TaxonomyManager taxonomyManager = (TaxonomyManager) getWebAppContext(request).getBean("taxonomyManager");

		boolean hasMore = false;	
		
		//get exact common name matches - show all exact matches
		SearchResultsDTO exactCommonNameMatches = taxonomyManager.findTaxonConceptsForCommonName(searchString, false, new SearchConstraints(0, 100));		
		request.setAttribute("exactCommonNameMatches", exactCommonNameMatches);
		
		//get fuzzy matches
		SearchConstraints searchConstraints = getSearchConstraints(request);		
		searchConstraints.setMaxResults(searchConstraints.getMaxResults()+exactCommonNameMatches.size());
		SearchResultsDTO fuzzyCommonNameMatches = taxonomyManager.findTaxonConceptsForCommonName(searchString, true, searchConstraints);
		//remove duplicate matches
		for (Object exactMatch: exactCommonNameMatches){
			if(fuzzyCommonNameMatches.contains(exactMatch))
				fuzzyCommonNameMatches.remove(exactMatch);
		}		
		request.setAttribute("fuzzyCommonNameMatches", fuzzyCommonNameMatches);
		if(fuzzyCommonNameMatches.hasMoreResults())
			hasMore = true;
		
		//get ending with matches
		//TODO this could be very slow - may require a db change with the splitting out of common name
		searchConstraints.setMaxResults(searchConstraints.getMaxResults()+exactCommonNameMatches.size());
		SearchResultsDTO endingWithCommonNameMatches = taxonomyManager.findTaxonConceptsForCommonName("% "+searchString, true, searchConstraints);
		request.setAttribute("endingWithCommonNameMatches", endingWithCommonNameMatches);
		if(endingWithCommonNameMatches.hasMoreResults())
			hasMore = true;

		//indicate if paging is required
		request.setAttribute("moreCommonNameMatches", hasMore);
	}
	
	/**
	 * Adds the content for the countries widget.
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void addCountries(HttpServletRequest request, HttpServletResponse response)  throws Exception{
		String searchString = getSearchString(request);	
		GeospatialManager geospatialManager = (GeospatialManager) getWebAppContext(request).getBean("geospatialManager");	
		Locale locale = RequestContextUtils.getLocale(request);
		SearchResultsDTO geospatialMatches = geospatialManager.findCountries(searchString, true, true, false, locale, getSearchConstraints(request));
		request.setAttribute("countryMatches", geospatialMatches);
		request.setAttribute("moreCountryMatches", geospatialMatches.hasMoreResults());
	}
	
	/**
	 * Adds the content for the Datasets widget.
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void addDatasets(HttpServletRequest request, HttpServletResponse response)  throws Exception{
		String searchString = getSearchString(request);	
		DataResourceManager dataResourceManager = (DataResourceManager) getWebAppContext(request).getBean("dataResourceManager");
		
		SearchResultsDTO resourceResultsDTO = dataResourceManager.findDatasets(searchString, true, true, true, getSearchConstraints(request));
		//split into 3 lists - providers and resources
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
		request.setAttribute("datasetMatches", resourceResultsDTO);
		request.setAttribute("dataProviders", providers);
		request.setAttribute("dataResources", resources);
		request.setAttribute("resourceNetworks", networks);		
		
		request.setAttribute("moreDatasetMatches", resourceResultsDTO.hasMoreResults());	
	}	

	/**
	 * Replaces wildcard with supported service layer wildcard
	 * 
	 * @param request
	 * @return
	 */
	public String getSearchString(HttpServletRequest request){
		String searchString = (String) request.getAttribute(SEARCH_STRING_ATTR);		
		if(StringUtils.isNotEmpty(searchString)){
			try {
				searchString = URLDecoder.decode(searchString, "UTF-8");
				request.setAttribute(SEARCH_STRING_ATTR, searchString);
				searchString = QueryHelper.tidyValue(searchString);
			} catch (UnsupportedEncodingException e){
				logger.debug( e.getMessage(), e);
			}
			searchString = searchString.replace('*', '%');
		}
		if(logger.isDebugEnabled())
			logger.debug("search string: "+searchString);
		return searchString;
	}
	
	/**
	 * Creates search constraints based on the request.
	 * 
	 * @param request
	 * @return SearchConstraints
	 */
	public SearchConstraints getSearchConstraints(HttpServletRequest request){
		String maxResultsAsString = (String) request.getAttribute("maxResults");
		Integer maxResults = null;
		if(StringUtils.isNotEmpty(maxResultsAsString)){
			try {
				maxResults = Integer.parseInt(maxResultsAsString);
			} catch (NumberFormatException e){
				logger.debug(e);
			}
		}
		if(maxResults==null)
			maxResults = DEFAULT_MAX_RESULTS;
		String startIndexAsString =(String) request.getAttribute("startIndex");
		Integer startIndex = null;
		if(StringUtils.isNotEmpty(startIndexAsString)){
			try {
				startIndex = Integer.parseInt(startIndexAsString);
			} catch (NumberFormatException e){
				logger.debug(e);
			}
		}
		if(startIndex==null)
			startIndex = DEFAULT_START_INDEX_RESULTS;
		
		return new SearchConstraints(startIndex, maxResults);
	}

	/**
	 * @param minLengthToSort the minLengthToSort to set
	 */
	public void setMinLengthToSort(int minLengthToSort) {
		this.minLengthToSort = minLengthToSort;
	}
}