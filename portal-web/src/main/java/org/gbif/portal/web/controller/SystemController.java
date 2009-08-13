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
package org.gbif.portal.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.dto.KeyValueDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.SystemManager;
import org.gbif.portal.web.content.map.MapContentProvider;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * A Controller for System actions.
 * 
 * @author dmartin
 */
public class SystemController extends MultiActionController {

	protected String defaultView="systemDetails";
	
	protected SystemManager systemManager;

	protected DataResourceManager dataResourceManager;	
	
	protected MapContentProvider mapContentProvider;
	
	/**
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView systemDetails(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		ModelAndView mav = new ModelAndView(defaultView);
		return viewSystemDetails(mav, request, response);
	}
	
	public ModelAndView clearCache(HttpServletRequest request, HttpServletResponse response) throws Exception {
		systemManager.clearCache();
		ModelAndView mav = new ModelAndView(defaultView);
		mav.addObject("cacheCleared", true);
		return viewSystemDetails(mav, request, response);
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView viewSystemDetails(ModelAndView mav, HttpServletRequest request, HttpServletResponse response){
		List<KeyValueDTO> kvps = null;
		
		try {
			kvps = systemManager.getSystemDetails().getResults();
		} catch (Exception e){
			logger.error(e.getMessage(), e);
			kvps = new ArrayList<KeyValueDTO>();
		}
		
		String mapLayerUrl = mapContentProvider.getMapLayerServerURL();
		kvps.add(new KeyValueDTO("Map Layer Url", mapLayerUrl));
		String mapServerUrl = mapContentProvider.getDefaultMapServerValue();
		kvps.add(new KeyValueDTO("Map Server Url", mapServerUrl));
		try{
			DataProviderDTO dataProvider = dataResourceManager.getNubDataProvider();
			if(dataProvider!=null)
				kvps.add(new KeyValueDTO("Nub Data Provider Details", dataProvider.toString()));
			else
				kvps.add(new KeyValueDTO("Nub Data Provider Details", "Unavailable"));
		} catch(ServiceException e){
			kvps.add(new KeyValueDTO("Nub Data Provider Details", "Unavailable"));
		}
		mav.addObject("systemProperties", kvps);
		return mav;		
	}

	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}

	/**
	 * @param defaultView the defaultView to set
	 */
	public void setDefaultView(String defaultView) {
		this.defaultView = defaultView;
	}

	/**
	 * @param mapContentProvider the mapContentProvider to set
	 */
	public void setMapContentProvider(MapContentProvider mapContentProvider) {
		this.mapContentProvider = mapContentProvider;
	}

	/**
	 * @param systemManager the systemManager to set
	 */
	public void setSystemManager(SystemManager systemManager) {
		this.systemManager = systemManager;
	}
}