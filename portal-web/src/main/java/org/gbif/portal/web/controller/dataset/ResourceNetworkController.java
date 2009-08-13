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
package org.gbif.portal.web.controller.dataset;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.dto.resources.ResourceNetworkDTO;
import org.gbif.portal.dto.util.EntityType;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.web.content.map.MapContentProvider;
import org.gbif.portal.web.controller.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * A controller used to render views of a resource network.
 * 
 * @author dmartin
 */
public class ResourceNetworkController extends RestController {

	protected DataResourceManager dataResourceManager;
	
	protected MapContentProvider mapContentProvider;
	
	/** The data resource request key **/ 
	protected String networkRequestKey = "network";

	/** The resource network model key **/
	protected String resourceNetworkModelKey = "resourceNetwork";	

	/** The resource network model key **/ 
	protected String dataResourcesModelKey = "dataResources";	
	
	/**
	 * @see org.gbif.portal.web.controller.RestController#handleRequest(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ModelAndView handleRequest(Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String resourceNetworkKey = propertiesMap.get(networkRequestKey);	
		ResourceNetworkDTO resourceNetwork = dataResourceManager.getResourceNetworkFor(resourceNetworkKey);
		if(resourceNetwork!=null){
//			ModelAndView mav = new ModelAndView(resourceNetworkViewModelName);
			ModelAndView mav = resolveAndCreateView(propertiesMap, request, false);			
			mav.addObject(resourceNetworkModelKey, resourceNetwork);
			//find data resources for this resource network
			List<DataResourceDTO> dataResources = dataResourceManager.getDataResourcesForResourceNetwork(resourceNetworkKey);
			mav.addObject(dataResourcesModelKey, dataResources);
			//add map content for network 
			mapContentProvider.addMapContentForEntity(request, EntityType.TYPE_RESOURCE_NETWORK, resourceNetworkKey);
			return mav;
		}
		return redirectToDefaultView();
	}

	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}

	/**
	 * @param dataResourcesModelKey the dataResourcesModelKey to set
	 */
	public void setDataResourcesModelKey(String dataResourcesModelKey) {
		this.dataResourcesModelKey = dataResourcesModelKey;
	}

	/**
	 * @param mapContentProvider the mapContentProvider to set
	 */
	public void setMapContentProvider(MapContentProvider mapContentProvider) {
		this.mapContentProvider = mapContentProvider;
	}

	/**
	 * @param networkRequestKey the networkRequestKey to set
	 */
	public void setNetworkRequestKey(String networkRequestKey) {
		this.networkRequestKey = networkRequestKey;
	}

	/**
	 * @param resourceNetworkModelKey the resourceNetworkModelKey to set
	 */
	public void setResourceNetworkModelKey(String resourceNetworkModelKey) {
		this.resourceNetworkModelKey = resourceNetworkModelKey;
	}
}
