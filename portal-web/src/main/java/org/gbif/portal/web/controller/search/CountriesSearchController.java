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

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.service.GeospatialManager;
import org.gbif.portal.service.ServiceException;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * 
 * @author dmartin
 */
public class CountriesSearchController extends SimpleSearchPagingController {

	protected GeospatialManager geospatialManager;
	/**
	 * @see org.gbif.portal.web.controller.search.SimpleSearchPagingController#doQuery(java.lang.String, org.gbif.portal.dto.util.SearchConstraints, java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public boolean doQuery(String searchString,
			SearchConstraints searchConstraints,
			Map<String, String> propertiesMap, HttpServletRequest request,
			HttpServletResponse response) throws ServiceException {
		Locale locale = RequestContextUtils.getLocale(request);
		SearchResultsDTO searchResults = geospatialManager.findCountries(searchString, true, true, false, locale, searchConstraints);
		request.setAttribute(searchResultsRequestKey, searchResults);
		return searchResults.hasMoreResults();
	}
	/**
	 * @param geospatialManager the geospatialManager to set
	 */
	public void setGeospatialManager(GeospatialManager geospatialManager) {
		this.geospatialManager = geospatialManager;
	}
}
