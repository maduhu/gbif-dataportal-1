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
package org.gbif.portal.web.controller.tutorial;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.service.LogManager;
import org.gbif.portal.web.controller.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * A Controller for dealing with the submition of user feedback
 *
 * @author Donald Hobern
 */
public class TutorialController extends RestController {
	/**
	 * Logger
	 */
	protected Log logger = LogFactory.getLog(TutorialController.class);
	
	/** The Managers */
	protected LogManager logManager;
	
	/** The view names */
	protected String tutorialViewName = "tutorialView";

	/** The page key */
	protected String tutorialPageNameKey = "tutorialPageName";

	/** The default page key */
	protected String defaultTutorialPage = "introduction";	
	
	/** The locale key */
	protected String localeKey = "localeName";
	
	/** The locale key */
	protected String defaultLocale = "en";
	
	/** The supported locale names */
	protected List<String> supportedLocales;
	
	/**
	 * @see org.gbif.portal.web.controller.RestController#handleRequest(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView handleRequest(Map<String, String> properties, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String locale = RequestContextUtils.getLocale(request).getLanguage();
		if (!supportedLocales.contains(locale)) {
			locale = defaultLocale;
		}
		String pageName = properties.get(tutorialPageNameKey);
		if(StringUtils.isEmpty(pageName)){
			properties.put(tutorialPageNameKey, defaultTutorialPage);
		}
		
		properties.put(localeKey, locale);
		
		return new ModelAndView(tutorialViewName, properties);
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

	/**
	 * @return the supportedLocales
	 */
	public List<String> getSupportedLocales() {
		return supportedLocales;
	}

	/**
	 * @param supportedLocales the supportedLocales to set
	 */
	public void setSupportedLocales(List<String> supportedLocales) {
		this.supportedLocales = supportedLocales;
	}
}