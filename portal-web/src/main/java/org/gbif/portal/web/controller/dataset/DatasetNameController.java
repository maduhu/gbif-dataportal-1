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

import org.gbif.portal.dto.KeyValueDTO;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.web.controller.RestKeyValueController;
import org.springframework.web.servlet.ModelAndView;

/**
 * /dataset/ajax/provider/ - returns all providers
 *   
 * /dataset/ajax/provider/12 - returns all the resources under provider 12
 * 
 * @author dmartin
 */
public class DatasetNameController extends RestKeyValueController {

	protected String providerRequestKey="provider";
	
	protected String networkRequestKey="network";

	protected String resultsModelKey="kvps";	
	
	protected DataResourceManager dataResourceManager;
	
	/**
	 * @see org.gbif.portal.web.controller.RestController#handleRequest(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ModelAndView handleRequest(Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String dataProviderKey = propertiesMap.get(providerRequestKey);
		String resourceNetworkKey = propertiesMap.get(networkRequestKey);
		
		ModelAndView mav = new ModelAndView(viewNamePrefix);
		List<KeyValueDTO> kvps = null;
		if(dataProviderKey!=null){
			kvps = dataResourceManager.getDataResourceList(null, dataProviderKey);
		} else if (resourceNetworkKey!=null){
			kvps = dataResourceManager.getDataResourceList(resourceNetworkKey, null);
		}
		mav.addObject(resultsModelKey, kvps);
		return mav;
	}
	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}
	/**
	 * @param providerRequestKey the providerRequestKey to set
	 */
	public void setProviderRequestKey(String providerRequestKey) {
		this.providerRequestKey = providerRequestKey;
	}
}