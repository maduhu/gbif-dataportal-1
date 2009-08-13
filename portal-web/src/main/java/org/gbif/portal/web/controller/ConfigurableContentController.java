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

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.web.content.ContentProvider;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * A Controller that is configured to use one or more content providers to 
 * provide the required objects for the model.
 *  
 * @author dmartin
 */
public class ConfigurableContentController implements Controller  {
	
	protected static Log logger = LogFactory.getLog(ConfigurableContentController.class);
	/**The configured content providers**/
	protected List<ContentProvider> contentProviders;
	/**the view to send the request to **/
	protected String viewName;	
	/** A configured set of attributes to add to the request */
	protected Map<String, Object> attributesToAdd;	

	/**
	 * Calls each content provider in the configured order and creates a model and view of the configured name.
	 * 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView(viewName);
		ContentUtil.addContent(mav, contentProviders, request, response);
		if(attributesToAdd!=null){
			for (String key : attributesToAdd.keySet()) {
				Object theObject = attributesToAdd.get(key);
				mav.addObject(key, theObject);
			}
		}
		return mav;
	}	
	
	/**
	 * @param contentProviders the contentProviders to set
	 */
	public void setContentProviders(List<ContentProvider> contentProviders) {
		this.contentProviders = contentProviders;
	}

	/**
	 * @param viewName the viewName to set
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * @param attributesToAdd the attributesToAdd to set
	 */
	public void setAttributesToAdd(Map<String, Object> attributesToAdd) {
		this.attributesToAdd = attributesToAdd;
	}
}