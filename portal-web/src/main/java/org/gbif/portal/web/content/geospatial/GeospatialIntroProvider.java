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
package org.gbif.portal.web.content.geospatial;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.geospatial.CountryDTO;
import org.gbif.portal.service.GeospatialManager;
import org.gbif.portal.util.request.IPUtils;
import org.gbif.portal.web.content.ContentProvider;
import org.gbif.portal.web.content.ContentView;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Provides geospatial introduction information to the ContentView 
 * 
 * @author dmartin
 */
public class GeospatialIntroProvider implements ContentProvider {

	protected Log logger = LogFactory.getLog(GeospatialIntroProvider.class);		
	protected GeospatialManager geospatialManager;
	protected String userCountryModelKey = "userCountry";
	/**
	 * @see org.gbif.portal.web.content.ContentProvider#addContent(org.gbif.portal.web.content.ContentView)
	 */
	public void addContent(ContentView contentView, HttpServletRequest request, HttpServletResponse response) {
		
		String remoteAddr = request.getRemoteAddr();
		if(logger.isDebugEnabled()){
			logger.debug("Remote user address:"+ remoteAddr);
		}
		if(remoteAddr!=null && IPUtils.isValidRemoteAddress(remoteAddr)){
			Locale locale = RequestContextUtils.getLocale(request);
			CountryDTO country = geospatialManager.getCountryForIPAddress(remoteAddr, locale);
			if(country!=null){
				contentView.addObject(userCountryModelKey, country);
			}
		}
	}

	/**
	 * @param geospatialManager The geospatialManager to set.
	 */
	public void setGeospatialManager(GeospatialManager geospatialManager) {
		this.geospatialManager = geospatialManager;
	}

	/**
	 * @param userCountryModelKey the userCountryModelKey to set
	 */
	public void setUserCountryModelKey(String userCountryModelKey) {
		this.userCountryModelKey = userCountryModelKey;
	}
}