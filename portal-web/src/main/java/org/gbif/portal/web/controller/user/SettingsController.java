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
package org.gbif.portal.web.controller.user;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.dto.KeyValueDTO;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Simple Form Controller that enables the submission of user settings.
 * 
 * @author dmartin
 */
public class SettingsController extends SimpleFormController {
	
	/** The theme resolver to use */
	protected ThemeResolver themeResolver;
	/** The locale to user */
	protected LocaleResolver localeResolver;
	
	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object commandObject, BindException bindException) throws Exception {
		Settings settings = (Settings) commandObject;
		if(settings.getTheme()!=null){
			themeResolver.setThemeName(request, response, settings.getTheme());
		}
		if(settings.getLocale()!=null){
			LocaleEditor localeEditor = new LocaleEditor();
			localeEditor.setAsText(settings.getLocale());
			localeResolver.setLocale(request, response, (Locale) localeEditor.getValue());		
		}
		
		//try and redirect to last page viewed
		List<KeyValueDTO> sessionHistory = (List<KeyValueDTO>) request.getSession().getAttribute("sessionHistory");
		if(sessionHistory!=null){
			//the last entry will probably be the settings page
			if(sessionHistory.size()>1){
				return new ModelAndView(new RedirectView(sessionHistory.get(1).getKey()));
			}
		}
		
		//otherwise return to setting page with new settings
		ModelAndView mav = new ModelAndView("settings");
		mav.addObject("settings", settings);
		return mav;	
	}

	/**
	 * Set the current theme.
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Settings settings = new Settings();
		String currentTheme = themeResolver.resolveThemeName(request);
		settings.setTheme(currentTheme);
		Locale locale = localeResolver.resolveLocale(request);
		if(locale!=null)
			settings.setLocale(locale.getLanguage());
		return settings;
	}

	/**
	 * @param themeResolver the themeResolver to set
	 */
	public void setThemeResolver(ThemeResolver themeResolver) {
		this.themeResolver = themeResolver;
	}

	/**
	 * @param localeResolver the localeResolver to set
	 */
	public void setLocaleResolver(LocaleResolver localeResolver) {
		this.localeResolver = localeResolver;
	}

	/**
	 * @return the localeResolver
	 */
	public LocaleResolver getLocaleResolver() {
		return localeResolver;
	}

	/**
	 * @return the themeResolver
	 */
	public ThemeResolver getThemeResolver() {
		return themeResolver;
	}	
}