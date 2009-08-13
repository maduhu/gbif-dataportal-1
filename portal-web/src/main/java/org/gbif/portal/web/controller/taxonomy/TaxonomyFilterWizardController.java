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
package org.gbif.portal.web.controller.taxonomy;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.KeyValueDTO;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.web.filter.FilterDTO;
import org.gbif.portal.web.filter.FilterMapWrapper;
import org.gbif.portal.web.filter.FilterUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * Filter Wizard Controller for Taxonomy Filters.
 *
 * @author Dave Martin
 */
public class TaxonomyFilterWizardController implements Controller {

	protected static Log logger = LogFactory.getLog(TaxonomyFilterWizardController.class);	
	protected FilterMapWrapper filterMapWrapper;
	/** service layer managers */
	protected TaxonomyManager taxonomyManager;
	protected DataResourceManager dataResourceManager;
	/** the data resource taxon filter */
	protected FilterDTO dataResourceTaxonFilter;
	
	/**
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String filterId = request.getParameter("filterId");
		String currValue = request.getParameter("currValue");
		FilterDTO filterDTO = FilterUtils.getFilterById(filterMapWrapper.getFilters(), filterId);
		String viewName = filterDTO.getWizardView();
		ModelAndView mav = new ModelAndView(viewName);
		if(logger.isDebugEnabled()){
			logger.debug("filter id:"+filterId);
			logger.debug("current value:"+currValue);
			logger.debug("wizard view name:"+viewName);
		}
		
		//check for dataResource filter
		if(filterId.equals(dataResourceTaxonFilter.getId()) ){
			List<KeyValueDTO> dataProviderList = dataResourceManager.getDataProviderList();
			mav.addObject("dataProviders", dataProviderList);
			List<KeyValueDTO> resourceNetworkList = dataResourceManager.getResourceNetworkList();
			mav.addObject("networks", resourceNetworkList);
		}

		return mav;
	}

	/**
	 * @param dataResourceTaxonFilter the dataResourceTaxonFilter to set
	 */
	public void setDataResourceTaxonFilter(FilterDTO dataResourceTaxonFilter) {
		this.dataResourceTaxonFilter = dataResourceTaxonFilter;
	}

	/**
	 * @param filterMapWrapper the filterMapWrapper to set
	 */
	public void setFilterMapWrapper(FilterMapWrapper filterMapWrapper) {
		this.filterMapWrapper = filterMapWrapper;
	}

	/**
	 * @param taxonomyManager the taxonomyManager to set
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
	}

	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}
}