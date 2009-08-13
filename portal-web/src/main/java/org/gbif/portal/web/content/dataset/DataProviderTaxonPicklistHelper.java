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
package org.gbif.portal.web.content.dataset;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.web.content.filter.PicklistHelper;

/**
 * Provides a picklist of data providers providing taxonomies.
 * 
 * @author dmartin
 */
public class DataProviderTaxonPicklistHelper implements PicklistHelper {

	protected static Log logger = LogFactory.getLog(DataProviderTaxonPicklistHelper.class);	
	
	/** Used to retrieve the list of providers */
	protected DataResourceManager dataResourceManager;
	
	/**
	 * @see org.gbif.portal.web.content.filter.PicklistHelper#getPicklist(java.util.Locale)
	 */
	public Map<String, String> getPicklist(HttpServletRequest request, Locale locale) {

		Map<String, String> dataProviderTaxonomies = new LinkedHashMap<String,String>();
		List<DataProviderDTO> dataProviders;
		try {
			dataProviders = dataResourceManager.getDataProvidersOfferingTaxonomies();
			for(DataProviderDTO dp: dataProviders)
				dataProviderTaxonomies.put(dp.getKey(), dp.getName());
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
		}
		return dataProviderTaxonomies;
	}

	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}
}
