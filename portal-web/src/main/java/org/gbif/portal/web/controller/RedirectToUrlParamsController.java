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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.util.path.PathMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * Uses the supplied properties to determine the method to invoke or view to direct to.
 * Adds resolved property names to the request and resolves the view name using the configured
 * viewNamePrefix and the retrieve subview name.  supportedPaths and urlRoot may be supplied
 * together in a PathMapping.
 * 
 * @author dmartin
 */
public class RedirectToUrlParamsController extends RestController {
	
	/** The controller to forward the request to */
	protected Controller targetController;

	protected PathMapping pathMapping;
	
	/**
	 * Uses the supplied properties to determine the method to invoke or view to direct to.
	 * Adds resolved property names to the request and resolves the view name using the configured
	 * viewNamePrefix and the retrieve subview name.
	 * 
	 * @param properties
	 * @param request
	 * @param response
	 * @return a ModelAndView
	 */
	public ModelAndView handleRequest(Map<String, String> properties, HttpServletRequest request, HttpServletResponse response) throws Exception{
		if(logger.isDebugEnabled())
			logger.debug("Rewritten properties: " + properties);
		addPropertiesToRequest(properties, request);
		if(logger.isDebugEnabled())
			logger.debug("Rewritten properties: " + request.getParameterMap());
		return targetController.handleRequest(request, response);
	}

	/**
	 * @return the targetController
	 */
	public Controller getTargetController() {
		return targetController;
	}

	/**
	 * @param targetController the targetController to set
	 */
	public void setTargetController(Controller targetController) {
		this.targetController = targetController;
	}

	/**
	 * @return the pathMapping
	 */
	public PathMapping getPathMapping() {
		return pathMapping;
	}

	/**
	 * @param pathMapping the pathMapping to set
	 */
	public void setPathMapping(PathMapping pathMapping) {
		this.pathMapping = pathMapping;
		setSupportedPatterns((pathMapping == null) ? null : pathMapping.getSupportedPatterns());
		setUrlRoot((pathMapping == null) ? null : pathMapping.getUrlRoot());
	}
}