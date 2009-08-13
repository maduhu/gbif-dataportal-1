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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.dto.CountDTO;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.geospatial.CountryDTO;
import org.gbif.portal.dto.util.BoundingBoxDTO;
import org.gbif.portal.dto.util.EntityType;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.service.GeospatialManager;
import org.gbif.portal.web.content.map.MapContentProvider;
import org.gbif.portal.web.controller.RestController;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * A Rest controller for geographic areas and countries.
 *
 * @author Dave Martin
 */
public class GeographyController extends RestController {

	/** GeospatialManager for country queries */
	protected GeospatialManager geospatialManager;
	/** Map Content Provider for map utilities */ 
	protected MapContentProvider mapContentProvider;
	
	protected String keyRequestKey = "key";
	protected String zoomRequestKey = "zoom";	
	protected String countryModelKey = "country";
	protected String resourceCountsModelKey = "resourceCounts";
	protected String countryCountsModelKey = "countryCounts";
	protected String hostedModelKey = "hosted";
	protected boolean sortResourcesByCount = false;
	
	/**
	 * @see org.gbif.portal.web.controller.RestController#handleRequest(java.util.List, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ModelAndView handleRequest(Map<String, String> properties, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String countryKey = properties.get(keyRequestKey);
		if(StringUtils.isNotEmpty(countryKey)){
			CountryDTO country = null;
			Locale locale = RequestContextUtils.getLocale(request);
			if(geospatialManager.isValidISOCountryCode(countryKey)){
				country = geospatialManager.getCountryForIsoCountryCode(countryKey, locale);
			} else if (geospatialManager.isValidCountryKey(countryKey)){
				country = geospatialManager.getCountryFor(countryKey, locale);
			} else {
				SearchResultsDTO searchResultsDTO = geospatialManager.findCountries(countryKey, false, false, true, locale, new SearchConstraints(0, 1));
				List results = searchResultsDTO.getResults();
				if(results!=null && results.size()==1)
					country = (CountryDTO) results.get(0);
			}
			
			if(country!=null){
				//sort counts into descending order
				List<CountDTO> resourceCounts = geospatialManager.getDataResourceCountsForCountry(country.getIsoCountryCode(), true);
				List<CountDTO> countryCounts = geospatialManager.getCountryCountsForCountry(country.getIsoCountryCode(), true, locale); 
				if(sortResourcesByCount){
					Collections.sort(resourceCounts, new Comparator<CountDTO>(){
						public int compare(CountDTO o1, CountDTO o2) {
							if(o1==null || o2==null || o1.getCount()==null || o2.getCount()==null)
								return -1;
							return o1.getCount().compareTo(o2.getCount()) * -1;
						}
					});
				}
				
				boolean showHosted = ServletRequestUtils.getBooleanParameter(request, hostedModelKey, false);
				EntityType entityType = null;
				if(showHosted){
					entityType = EntityType.TYPE_HOME_COUNTRY;
				} else {
					entityType = EntityType.TYPE_COUNTRY;
				}
				
				ModelAndView mav = resolveAndCreateView(properties, request, false);
				mav.addObject(countryModelKey, country);
				mav.addObject(resourceCountsModelKey, resourceCounts);
				mav.addObject(countryCountsModelKey, countryCounts);
				
				if(logger.isDebugEnabled())
					logger.debug("Returning details of:"+country);
				
				//if zoom level not specified, jump to correct zoom level for this country
				if(!MapContentProvider.zoomLevelSpecified(request) && !showHosted && country.getMinLongitude()!=null 
						&& country.getMinLatitude()!=null && country.getMaxLongitude()!=null && country.getMaxLatitude()!=null){
					//zoom to the correct level for this country
					mapContentProvider.addMapContent(request, entityType.getName(), country.getKey(), country.getMinLongitude(), 
							country.getMinLatitude(), country.getMaxLongitude(), country.getMaxLatitude());
					mapContentProvider.addPointsTotalsToRequest(request, entityType, country.getKey(), 
							new BoundingBoxDTO(country.getMinLongitude(), country.getMinLatitude(), country.getMaxLongitude(), country.getMaxLatitude()));
				} else {
					//zoom to the level specified in the request			
					mapContentProvider.addMapContent(request, entityType.getName(), country.getKey());
					mapContentProvider.addPointsTotalsToRequest(request, entityType, country.getKey(), null);
				}
				return mav;
			}
		}
		return redirectToDefaultView();
	}

	/**
	 * @param geospatialManager the geospatialManager to set
	 */
	public void setGeospatialManager(GeospatialManager geospatialManager) {
		this.geospatialManager = geospatialManager;
	}

	/**
	 * @param mapContentProvider the mapContentProvider to set
	 */
	public void setMapContentProvider(MapContentProvider mapContentProvider) {
		this.mapContentProvider = mapContentProvider;
	}

	/**
	 * @param countryModelKey the countryModelKey to set
	 */
	public void setCountryModelKey(String countryModelKey) {
		this.countryModelKey = countryModelKey;
	}

	/**
	 * @param keyRequestKey the keyRequestKey to set
	 */
	public void setKeyRequestKey(String keyRequestKey) {
		this.keyRequestKey = keyRequestKey;
	}

	/**
	 * @param zoomRequestKey the zoomRequestKey to set
	 */
	public void setZoomRequestKey(String zoomRequestKey) {
		this.zoomRequestKey = zoomRequestKey;
	}

	/**
	 * @param resourceCountsModelKey the resourceCountsModelKey to set
	 */
	public void setResourceCountsModelKey(String resourceCountsModelKey) {
		this.resourceCountsModelKey = resourceCountsModelKey;
	}

	/**
	 * @param sortResourcesByCount the sortResourcesByCount to set
	 */
	public void setSortResourcesByCount(boolean sortResourcesByCount) {
		this.sortResourcesByCount = sortResourcesByCount;
	}
}