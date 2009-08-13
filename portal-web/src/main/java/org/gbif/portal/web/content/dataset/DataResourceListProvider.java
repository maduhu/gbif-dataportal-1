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
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.web.content.ContentProvider;
import org.gbif.portal.web.content.ContentView;

/**
 * The decorator that will put the DataResource list into the model
 * 
 * @author trobertson
 */
public class DataResourceListProvider implements ContentProvider {

	protected static Log log = LogFactory.getLog(DataResourceListProvider.class);
	/** Service managers **/
	protected DataResourceManager dataResourceManager;
	/** The key that is used for placing the data resources into the model **/
	public static final String MODEL_DATA_RESOURCES_KEY = "dataResources";
	
	/**
	 * Puts the DataResourceList into the ContentView
	 * 
	 * @see org.gbif.portal.web.content.ContentProvider#addContent(org.gbif.portal.web.content.ContentView, java.lang.Object)
	 */
	public void addContent(ContentView cc, HttpServletRequest request, HttpServletResponse response) {
//		List<DataResourceDTO> results = dataResourceManager.getDataResources();
//		log.info("Adding " + results.size() + " DataResources to the model...");
//		cc.addObject(DataResourceListProvider.MODEL_DATA_RESOURCES_KEY, results);
	}

	/**
	 * @param dataResourceManager The dataResourceManager to set.
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}
}