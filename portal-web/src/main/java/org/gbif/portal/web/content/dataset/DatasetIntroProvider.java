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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.web.content.ContentProvider;
import org.gbif.portal.web.content.ContentView;

/**
 * Provides dataset introduction information to the ContentView 
 * 
 * @author dmartin
 */
public class DatasetIntroProvider implements ContentProvider {

	protected Log log = LogFactory.getLog(DatasetIntroProvider.class);		
	protected DataResourceManager dataResourceManager;
	
	/**
	 * @see org.gbif.portal.web.content.ContentProvider#addContent(org.gbif.portal.web.content.ContentView)
	 */
	public void addContent(ContentView contentView, HttpServletRequest request, HttpServletResponse response) {
		try {
			DataResourceDTO lastDataResource = dataResourceManager.getNewestDataResource();
			long dataResourceCount = dataResourceManager.getTotalDataResourceCount();
			long dataProviderCount = dataResourceManager.getTotalDataProviderCount();
			
			//long dataProviderCountryCount = publicationManager.getDataProviderCountryCount();
			
			if(lastDataResource!=null)
				contentView.addObject("latestResource", lastDataResource);
			
			contentView.addObject("dataResourceCount",dataResourceCount);
			contentView.addObject("dataProviderCount",dataProviderCount);
			//contentView.addObject("noOfCountries",dataProviderCountryCount);
		} catch (ServiceException e){
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}
}
