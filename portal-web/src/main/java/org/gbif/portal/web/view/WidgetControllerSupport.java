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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;
import org.gbif.portal.web.view.map.MapWidgetController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.tiles.ComponentControllerSupport;

/**
 * Abstract class to be subclassed by Controllers providing Widget functionality. A WidgetController consist of one or more
 * methods adding content to a request to be rendered by a view technology downstream. Methods providing this content
 * follow the method name convention of add<Widget-Name>. 
 * 
 * Implements the tiles interface ComponentController to allow tiles to use the controllerClass attribute to provide content for
 * a tile prior to it being rendered.
 * 
 * Also implements WidgetController interface to provide a common interface for allowing implementations
 * to populate artefacts outside of tiles.
 *
 * @see MapWidgetController
 * 
 * @author dmartin
 */
public abstract class WidgetControllerSupport extends ComponentControllerSupport implements WidgetController {	
	
	protected static Log logger = LogFactory.getLog(WidgetControllerSupport.class);	
	/**
	 * The web application context used to access beans in the spring context. 
	 * Added because the super instance of the application context isnt initialised when
	 * this class is accessed as a spring reference.
	 */
	private WebApplicationContext webAppContext;
	
	/**
	 * This delegates to the correct widget method. 
	 * @todo move to a superclass
	 * @see org.springframework.web.servlet.view.tiles.ComponentControllerSupport#doPerform(org.apache.struts.tiles.ComponentContext, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPerform(ComponentContext componentContext, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//widget is null in the case of entire page
		String widget = (String) componentContext.getAttribute("widget");
		if(logger.isDebugEnabled())
			logger.debug("widget name:"+widget);
		if(StringUtils.isNotEmpty(widget)){
			doPerform(widget, request, response);
		}
	}

	/**
	 * @see org.gbif.portal.web.view.WidgetController#doPerform(java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doPerform(String widgetName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Object[] args = new Object[]{request, response};
		widgetName = StringUtils.capitalize(widgetName);
		try {
			if(logger.isDebugEnabled())
				logger.debug("widget name:"+widgetName);
			MethodUtils.invokeMethod(this, "add"+widgetName, args);
		} catch (NoSuchMethodException e){
			logger.error("Unable to find method for widget: "+widgetName);
			logger.error("Missing method: add"+widgetName);
			logger.error(e.getMessage(), e);
		}
	}	

	/**
	 * Retrieves the web application context to allow implementations to access spring configured beans.
	 * @param request
	 * @return WebApplicationContext
	 */
	protected WebApplicationContext getWebAppContext(HttpServletRequest request){
		if(webAppContext==null){
			webAppContext=getWebApplicationContext();
			if(webAppContext==null)
				webAppContext = RequestContextUtils.getWebApplicationContext(request);
		}
		return webAppContext;
	}
}