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

import org.apache.commons.lang.math.NumberUtils;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.web.controller.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Allows combined paging over taxon name and common name search results.
 * 
 * @author dmartin
 */
public abstract class SimpleSearchPagingController extends RestController {

	protected int maxResultsToPageOver = 25;
	
	protected String searchStringRequestKey="searchString";
	
	protected String highWaterMarkRequestKey = "highWaterMark";
	
	protected String pageNoRequestKey = "pageNo";
	
	protected String searchResultsRequestKey = "searchResults";
	
	protected String hasMoreRequestKey="hasMoreResults";
	
	/**
	 * @see org.gbif.portal.web.controller.RestController#handleRequest(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ModelAndView handleRequest(Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String searchString = propertiesMap.get(searchStringRequestKey);
		int highWaterMark = NumberUtils.toInt(propertiesMap.get(highWaterMarkRequestKey), 1);
		
		int pageNo = 1;
		try{
			pageNo = NumberUtils.toInt(propertiesMap.get(pageNoRequestKey), 1);
		} catch (NumberFormatException e){
			logger.error(e.getMessage(), e);
		}
		searchString = searchString.replace('*', '%');

		//do query
		boolean hasMore = doQuery(searchString, new SearchConstraints(maxResultsToPageOver*(pageNo-1),  maxResultsToPageOver), propertiesMap, request, response);
		
		request.setAttribute(hasMoreRequestKey, hasMore);	
		request.setAttribute(searchStringRequestKey, searchString);		
		request.setAttribute(pageNoRequestKey, pageNo);		
		
		if(pageNo>highWaterMark)
			highWaterMark = pageNo;
		if(hasMore && pageNo+1>highWaterMark)
			highWaterMark = pageNo+1;

		request.setAttribute(highWaterMarkRequestKey, highWaterMark);		
		ModelAndView mav = resolveAndCreateView(propertiesMap, request, false);
		return mav;
	}
	
	/**
	 * Do the query.
	 * 
	 * @param propertiesMap
	 * @param request
	 * @param response
	 * @return boolean indicating if there is more results to come
	 */
	public abstract boolean doQuery(String searchString, SearchConstraints searchConstraints, Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response) throws ServiceException;
	
	/**
	 * @param maxResultsToPageOver the maxResultsToPageOver to set
	 */
	public void setMaxResultsToPageOver(int maxResultsToPageOver) {
		this.maxResultsToPageOver = maxResultsToPageOver;
	}

	/**
	 * @param searchStringRequestKey the searchStringRequestKey to set
	 */
	public void setSearchStringRequestKey(String searchStringRequestKey) {
		this.searchStringRequestKey = searchStringRequestKey;
	}
}