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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.dto.KeyValueDTO;
import org.gbif.portal.service.GeospatialManager;
import org.gbif.portal.web.controller.RestKeyValueController;
import org.jfree.util.Log;
import org.springframework.web.servlet.ModelAndView;

/**
 * /geography/ajax/country/ - returns all countries
 *   
 * /geography/ajax/country/DK - returns all the geo regions under Denmark
 * 
 * @author jcuadra
 */
public class GeographyNameController extends RestKeyValueController {

	protected String countryRequestKey="country";
	
	//protected String networkRequestKey="network";

	protected String resultsModelKey="kvps";	
	
	protected GeospatialManager geospatialManager;
	
	/**
	 * @see org.gbif.portal.web.controller.RestController#handleRequest(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ModelAndView handleRequest(Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String isoCountryCode = propertiesMap.get(countryRequestKey);
		//String resourceNetworkKey = propertiesMap.get(networkRequestKey);
		
		ModelAndView mav = new ModelAndView(viewNamePrefix);
		List<KeyValueDTO> kvps = null;
		if(isoCountryCode!=null){
			kvps = geospatialManager.getGeoRegionList(isoCountryCode);
		}
		mav.addObject(resultsModelKey, kvps);
		return mav;
	}
	
	/**
	 * @return the geospatialManager
	 */
	public GeospatialManager getGeospatialManager() {
		return geospatialManager;
	}

	/**
	 * @param geospatialManager the geospatialManager to set
	 */
	public void setGeospatialManager(GeospatialManager geospatialManager) {
		this.geospatialManager = geospatialManager;
	}	
}