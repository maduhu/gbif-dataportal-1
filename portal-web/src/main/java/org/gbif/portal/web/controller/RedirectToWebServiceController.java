/***************************************************************************
 * Copyright (C) 2006 Global Biodiversity Information Facility Secretariat.
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

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * Simple controller for forwarding a request on to a web service.
 * 
 * @author dmartin
 */
public class RedirectToWebServiceController extends RestController {

	protected String urlPrefix;
	protected Map<String, String> extraParams;
	protected Map<String, String> patternPrefix;
	
	/**
	 * @see org.gbif.portal.web.controller.RestController#handleRequest(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ModelAndView handleRequest(Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(urlPrefix);
		addPropertiesToRequest(propertiesMap, request);
		for (String key: extraParams.keySet())
			request.setAttribute(key, extraParams.get(key));
		requestDispatcher.forward(request, response);
		return null;
	}

	/**
	 * @see org.gbif.portal.web.controller.RestController#addPropertiesToRequest(java.util.Map, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected void addPropertiesToRequest(Map<String, String> properties, HttpServletRequest request) {
		for (String key: properties.keySet()){
			String propertyValue = properties.get(key);
			if(patternPrefix!=null && patternPrefix.containsKey(key)){
				String pattern = patternPrefix.get(key); 
				if(StringUtils.isNotEmpty(propertyValue) && propertyValue.indexOf(pattern)==0){
					propertyValue = propertyValue.substring(pattern.length());
				}
			}
			request.setAttribute(key, propertyValue);
		}
	}

	/**
	 * @param extraParams the extraParams to set
	 */
	public void setExtraParams(Map<String, String> extraParams) {
		this.extraParams = extraParams;
	}

	/**
	 * @param urlPrefix the urlPrefix to set
	 */
	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}

	/**
	 * @param patternPrefix the patternPrefix to set
	 */
	public void setPatternPrefix(Map<String, String> patternPrefix) {
		this.patternPrefix = patternPrefix;
	}
}