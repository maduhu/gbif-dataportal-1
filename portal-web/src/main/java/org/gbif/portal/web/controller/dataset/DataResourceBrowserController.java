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

import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.web.controller.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for Browsing Data Providers and Resources.
 * 
 * @author dmartin
 */
public class DataResourceBrowserController extends RestController {

	/** Provides the data resource/provider */
	protected DataResourceManager dataResourceManager;
	/** Model Key for the data resource matches */
	protected String dataResourceModelKey = "dataResources";	
	
	protected DataProviderDTO nubDataProvider;
	protected List<DataResourceDTO> nubResources;
	
	/** Whether to hide the nub provider/resources from the view */
	protected boolean hideNub = true;

	/**
   * @see org.gbif.portal.web.controller.RestController#handleRequest(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  @Override
  public ModelAndView handleRequest(Map<String, String> propertiesMap, HttpServletRequest request,
      HttpServletResponse response) throws Exception {
		try {
			ModelAndView mav = resolveAndCreateView(propertiesMap, request, false); 
			List<DataResourceDTO> resources = dataResourceManager.getAllDataResources();
			mav.addObject(dataResourceModelKey, resources);
			return mav;
		} catch (Exception e){
			logger.error(e.getMessage(),e);
		}
		return null;
	}

	/**
	 * Remove the nub provider/resources from the datasets list.
	 * 
	 * @param providers
	 * @param resources
	 */
	protected void removeNubProviderAndResources(List<DataProviderDTO> providers, List<DataResourceDTO> resources) {
		if(nubDataProvider==null){
			try {
				nubDataProvider = dataResourceManager.getNubDataProvider();
				if(nubDataProvider!=null){
					providers.remove(nubDataProvider);
					nubResources = dataResourceManager.getDataResourcesForProvider(nubDataProvider.getKey());
					if(nubResources!=null)
						resources.removeAll(nubResources);
				}
			} catch (ServiceException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}

	/**
	 * @param dataResourceModelKey the dataResourceModelKey to set
	 */
	public void setDataResourceModelKey(String dataResourceModelKey) {
		this.dataResourceModelKey = dataResourceModelKey;
	}
}