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
package org.gbif.portal.web.controller.feedback;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.service.LogManager;
import org.gbif.portal.web.controller.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * A Controller for dealing with the submition of user verification
 *
 * @author Tim Robertson
 */
public class FeedbackVerficationController extends RestController {
	/**
	 * Logger
	 */
	protected Log logger = LogFactory.getLog(FeedbackVerficationController.class);
	
	/** The Managers */
	LogManager logManager;
	
	/** The view names */
	protected String verificationSuccessViewName = "verificationSuccessView";
	protected String verificationFailureViewName = "verificationFailureView";
	
	/** The request keys **/
	protected String userKeyRequestKey = "userKey";
	protected String userCodeRequestKey = "userCode";
	protected String userNameRequestKey = "userName";
	
	/**
	 * @see org.gbif.portal.web.controller.RestController#handleRequest(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView handleRequest(Map<String, String> properties, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userKey = properties.get(userKeyRequestKey);
		String userCode = properties.get(userCodeRequestKey);
		String userName = logManager.authoriseUser(userKey, userCode);
		if (userName != null) {
			return new ModelAndView(verificationSuccessViewName, userNameRequestKey, userName);
		} else {
			return new ModelAndView(verificationFailureViewName);
		}
		
	}

	/**
	 * @return Returns the logManager.
	 */
	public LogManager getLogManager() {
		return logManager;
	}

	/**
	 * @param logManager The logManager to set.
	 */
	public void setLogManager(LogManager logManager) {
		this.logManager = logManager;
	}
}