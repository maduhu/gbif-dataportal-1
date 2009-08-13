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
package org.gbif.portal.web.controller.geography;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.service.GeospatialManager;
import org.gbif.portal.web.controller.search.NameSearchController;
import org.gbif.portal.web.controller.search.bean.NameSearch;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author dave
 */
public class CountryNameSearchController extends NameSearchController {

	/** The geospatial manager used for searching */
	protected GeospatialManager geospatialManager;
	
	protected String anyOccurrenceRequestKey = "anyOccurrence";
	protected String localeRequestKey = "locale";
	protected String onlySearchInLocaleRequestKey = "onlySearchInLocale";

	/**
	 * @see org.gbif.portal.web.controller.search.NameSearchController#handleSearch(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.ModelAndView, org.gbif.portal.web.controller.search.bean.NameSearch)
	 */
	@Override
	public void handleSearch(Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response, ModelAndView mav, NameSearch nameSearch) {
		
		boolean anyOccurrence = ServletRequestUtils.getBooleanParameter(request, anyOccurrenceRequestKey, false);
		Locale locale = null;
		try {		
			String localeString = ServletRequestUtils.getStringParameter(request, localeRequestKey);
			if(localeString!=null)
				locale = new Locale(localeString);
		} catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		
		boolean onlySearchInLocale = ServletRequestUtils.getBooleanParameter(request, onlySearchInLocaleRequestKey, false);
		if(locale==null)
			onlySearchInLocale = false;
		
		Object searchResults = null;
		if(StringUtils.isNotEmpty(nameSearch.getQuery())){
			 searchResults = geospatialManager.findCountries(nameSearch.getQuery(), !nameSearch.isExactMatchOnly(), anyOccurrence, onlySearchInLocale, locale, nameSearch.getSearchConstraints());
		} else {
			searchResults = geospatialManager.findAllCountries(locale);
		}
		
		mav.addObject(searchResultsModelKey, searchResults);
	}

	/**
	 * @param geospatialManager the geospatialManager to set
	 */
	public void setGeospatialManager(GeospatialManager geospatialManager) {
		this.geospatialManager = geospatialManager;
	}

	/**
	 * @param anyOccurrenceRequestKey the anyOccurrenceRequestKey to set
	 */
	public void setAnyOccurrenceRequestKey(String anyOccurrenceRequestKey) {
		this.anyOccurrenceRequestKey = anyOccurrenceRequestKey;
	}

	/**
	 * @param localeRequestKey the localeRequestKey to set
	 */
	public void setLocaleRequestKey(String localeRequestKey) {
		this.localeRequestKey = localeRequestKey;
	}

	/**
	 * @param onlySearchInLocaleRequestKey the onlySearchInLocaleRequestKey to set
	 */
	public void setOnlySearchInLocaleRequestKey(String onlySearchInLocaleRequestKey) {
		this.onlySearchInLocaleRequestKey = onlySearchInLocaleRequestKey;
	}
}