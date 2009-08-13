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

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.dto.geospatial.CountryDTO;
import org.gbif.portal.service.GeospatialManager;
import org.gbif.portal.web.controller.AlphabetBrowserController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * A controller for browsing countries.
 * 
 * @author dmartin
 */
public class GeographyBrowserController extends AlphabetBrowserController {

	/** Manager providing geospatial/country information */
	protected GeospatialManager geospatialManager;
	/** The Model Key for the retrieve list of countries */
	protected String countriesModelKey="countries";	
	/**
	 * @see org.gbif.portal.web.controller.AlphabetBrowserController#alphabetSearch(char, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public ModelAndView alphabetSearch(char searchChar,  ModelAndView mav, HttpServletRequest request, HttpServletResponse response) {
		Locale locale = RequestContextUtils.getLocale(request);
		List<CountryDTO> countries = geospatialManager.getCountriesFor(searchChar, true, locale);
		mav.addObject(countriesModelKey, countries);
		return mav;
	}

	@Override
	public List<Character> retrieveAlphabet(HttpServletRequest request, HttpServletResponse response) {
		Locale locale = RequestContextUtils.getLocale(request);
		return geospatialManager.getCountryAlphabet(locale);
	}	
	
	/**
	 * @param geospatialManager the geospatialManager to set
	 */
	public void setGeospatialManager(GeospatialManager geospatialManager) {
		this.geospatialManager = geospatialManager;
	}

	/**
	 * @param countriesModelKey the countriesModelKey to set
	 */
	public void setCountriesModelKey(String countriesModelKey) {
		this.countriesModelKey = countriesModelKey;
	}
}