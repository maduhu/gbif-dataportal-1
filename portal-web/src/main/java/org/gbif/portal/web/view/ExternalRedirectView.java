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
package org.gbif.portal.web.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.RedirectView;

/**
 * Provides a mechanism for redirecting to an external resource using a request attribute
 * populated by a widget controller.
 * 
 * @author dmartin
 */
public class ExternalRedirectView extends RedirectView {
	/**The attribute to retrieve the url to redirect to from the request**/
	public String externalUrlAttribute;
	/**The widget to use **/
	public String widgetName;
	/**The widgetController to use **/	
	public WidgetController widgetController;
	
	public ExternalRedirectView() {
		//to avoid IllegalArgumentExceptions - url isnt set until render is invoked
		setUrl("");
	}

	/**
	 * @see org.springframework.web.servlet.view.AbstractView#render(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void render(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//this should populate the externalUrl attribute
		widgetController.doPerform(widgetName, request, response);
		//need to add the url in here some how
		String url = (String) request.getAttribute(externalUrlAttribute);
		setUrl(url);
		super.render(model, request, response);
	}

	/**
	 * @return the externalUrlAttribute
	 */
	public String getExternalUrlAttribute() {
		return externalUrlAttribute;
	}

	/**
	 * @param externalUrlAttribute the externalUrlAttribute to set
	 */
	public void setExternalUrlAttribute(String externalUrlAttribute) {
		this.externalUrlAttribute = externalUrlAttribute;
	}

	/**
	 * @return the widgetName
	 */
	public String getWidgetName() {
		return widgetName;
	}

	/**
	 * @param widgetName the widgetName to set
	 */
	public void setWidgetName(String widgetName) {
		this.widgetName = widgetName;
	}

	/**
	 * @return the widgetController
	 */
	public WidgetController getWidgetController() {
		return widgetController;
	}

	/**
	 * @param widgetController the widgetController to set
	 */
	public void setWidgetController(WidgetController widgetController) {
		this.widgetController = widgetController;
	}
}