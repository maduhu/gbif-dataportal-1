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

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.web.controller.RestController;
import org.gbif.portal.web.controller.search.bean.NameSearch;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * A name search controller supporting simple name searching via a
 * REST style interface. 
 * 
 * @see http://wiki.gbif.org/dadiwiki/wikka.php?wakka=NameSearchAPI
 * 
 * @author dmartin
 */
public abstract class NameSearchController extends RestController {
	
	/** query request key */
	protected String queryRequestKey = "query";
	
	/** callback request key */
	protected String callbackRequestKey = "callback";
	
	/** the default sub view to use */
	protected String defaultSubView;
	
	/** defaults */
	protected String defaultReturnType = "name";
	protected int defaultMaxResults = 10;
	protected int defaultStartIndex = 0;	
	
	/** The absolute limit on max results returned */
	protected int maxResultsLimit = 1000;
	
	/** request keys */
	protected String soundexRequestKey = "soundex";
	protected String returnTypeRequestKey = "returnType";
	protected String maxResultsRequestKey = "maxResults";
	protected String startIndexRequestKey = "startIndex";
	
	/** the request key for view */
	protected String viewRequestKey = "view";
	
	/** Model key for search results */
	protected String searchResultsModelKey = "searchResults";
	
	/** exact matches only */
	protected String exactMatchOnlyRequestKey = "exactOnly";

	/** The help sub view */
	protected String helpSubview = "help";	

	/** Supported return types */
	protected List<String> supportedReturnTypes;
	
	/** Whether to use case insensitive view resolving */
	protected boolean caseInsensitive = true;
	
	protected boolean allowEmptyQuery = false;
	
	/**
	 * @see org.gbif.portal.web.controller.RestController#handleRequest(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ModelAndView handleRequest(Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String query = ServletRequestUtils.getStringParameter(request, queryRequestKey);
		
		if(StringUtils.isEmpty(query) && !allowEmptyQuery){
			return getDefaultModelAndView(propertiesMap, request, response);
		}
		
		NameSearch nameSearch = new NameSearch();
		nameSearch.setQuery(query);
		
		//get request properties
		String returnType = ServletRequestUtils.getStringParameter(request, returnTypeRequestKey, defaultReturnType);
		String view = ServletRequestUtils.getStringParameter(request, viewRequestKey, defaultView);
		
		nameSearch.setReturnType(returnType);
		String callback = ServletRequestUtils.getStringParameter(request, callbackRequestKey);
		nameSearch.setCallback(callback);
		nameSearch.setExactMatchOnly(ServletRequestUtils.getBooleanParameter(request, exactMatchOnlyRequestKey, false));
		nameSearch.setSoundex(ServletRequestUtils.getBooleanParameter(request, soundexRequestKey, false));
		
		//initialise search constraints		
		int startIndex = ServletRequestUtils.getIntParameter(request, startIndexRequestKey, defaultStartIndex);		
		int maxResults = ServletRequestUtils.getIntParameter(request, maxResultsRequestKey, defaultMaxResults);
		if(maxResults>maxResultsLimit)
			maxResults=maxResultsLimit;
		SearchConstraints sc = new SearchConstraints(startIndex, maxResults);
		nameSearch.setSearchConstraints(sc);
		
		//resolve the view
		ModelAndView mav = resolveAndCreateView(viewNamePrefix, returnType, view);
		
		//add callback if supplied
		if(callback!=null)
			mav.addObject(callbackRequestKey, callback);
		
		//handle search
		handleSearch(propertiesMap, request, response, mav, nameSearch);
		return mav;
	}

	/**
	 * Returns the view name constructed from properties in the request.
	 * 
	 * @param propertiesMap
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ModelAndView getDefaultModelAndView(Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView(viewNamePrefix+"."+helpSubview);
	}

	/**
	 * Method that handles the query to the service layer.
	 * 
	 * @param propertiesMap
	 * @param request
	 * @param response
	 * @param mav
	 * @param nameSearch
	 * @return ModelAndV
	 * @throws Exception
	 */
	public abstract void handleSearch(Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response, ModelAndView mav, NameSearch nameSearch) throws Exception;
	
	/**
	 * @see org.gbif.portal.web.controller.RestController#resolveAndCreateView(java.util.Map, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected ModelAndView resolveAndCreateView(String prefix, String returnType, String view) {
		if(view==null){
			view = defaultSubView;
		}

		//to lower case
		if(caseInsensitive){
			view = StringUtils.lowerCase(view);
		}
			
		if(!supportedSubViews.contains(view))
			view = defaultSubView;

//		if(caseInsensitive){
//			returnType = StringUtils.lowerCase(returnType);
//		}		
		
		if(returnType==null || !supportedReturnTypes.contains(returnType))
			returnType = defaultReturnType;
		
		String resolvedViewName = prefix+"."+returnType+"."+view;
		logger.debug(resolvedViewName);
		return new ModelAndView(resolvedViewName);
	}
	
	/**
	 * @param defaultMaxResults the defaultMaxResults to set
	 */
	public void setDefaultMaxResults(int defaultMaxResults) {
		this.defaultMaxResults = defaultMaxResults;
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
	 * @param defaultSubView the defaultSubView to set
	 */
	public void setDefaultSubView(String defaultSubView) {
		this.defaultSubView = defaultSubView;
	}

	/**
	 * @param exactMatchOnlyRequestKey the exactMatchOnlyRequestKey to set
	 */
	public void setExactMatchOnlyRequestKey(String exactMatchOnlyRequestKey) {
		this.exactMatchOnlyRequestKey = exactMatchOnlyRequestKey;
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
	 * @param queryRequestKey the queryRequestKey to set
	 */
	public void setQueryRequestKey(String queryRequestKey) {
		this.queryRequestKey = queryRequestKey;
	}

	/**
	 * @param returnTypeRequestKey the returnTypeRequestKey to set
	 */
	public void setReturnTypeRequestKey(String returnTypeRequestKey) {
		this.returnTypeRequestKey = returnTypeRequestKey;
	}

	/**
	 * @param searchResultsModelKey the searchResultsModelKey to set
	 */
	public void setSearchResultsModelKey(String searchResultsModelKey) {
		this.searchResultsModelKey = searchResultsModelKey;
	}

	/**
	 * @param startIndexRequestKey the startIndexRequestKey to set
	 */
	public void setStartIndexRequestKey(String startIndexRequestKey) {
		this.startIndexRequestKey = startIndexRequestKey;
	}

	/**
	 * @param viewRequestKey the viewRequestKey to set
	 */
	public void setViewRequestKey(String viewRequestKey) {
		this.viewRequestKey = viewRequestKey;
	}

	/**
	 * @param soundexRequestKey the soundexRequestKey to set
	 */
	public void setSoundexRequestKey(String soundexRequestKey) {
		this.soundexRequestKey = soundexRequestKey;
	}

	/**
	 * @param supportedReturnTypes the supportedReturnTypes to set
	 */
	public void setSupportedReturnTypes(List<String> supportedReturnTypes) {
		this.supportedReturnTypes = supportedReturnTypes;
	}

	/**
	 * @param callbackRequestKey the callbackRequestKey to set
	 */
	public void setCallbackRequestKey(String callbackRequestKey) {
		this.callbackRequestKey = callbackRequestKey;
	}

	/**
	 * @param helpSubview the helpSubview to set
	 */
	public void setHelpSubview(String helpSubview) {
		this.helpSubview = helpSubview;
	}

	/**
	 * @param caseInsensitive the caseInsensitive to set
	 */
	public void setCaseInsensitive(boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}

	/**
   * @return the allowEmptyQuery
   */
  public boolean isAllowEmptyQuery() {
  	return allowEmptyQuery;
  }

	/**
   * @param allowEmptyQuery the allowEmptyQuery to set
   */
  public void setAllowEmptyQuery(boolean allowEmptyQuery) {
  	this.allowEmptyQuery = allowEmptyQuery;
  }
}