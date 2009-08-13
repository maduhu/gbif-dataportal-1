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
/**
 * Implemented by WidgetControlllers that provide content for Widget Controls.
 *  
 * @author dmartin
 */
public interface WidgetController {
	/**
	 * Implementations will delegates to the correct widget method or will add the content required for this widget to
	 * the request.
	 * 
	 * @param widgetName The name of the widget to populate. This could be a tile definition name or a view of some sort.
	 * @param request the HttpServletRequest for this request
	 * @param response the HttpServletResponse the response for this request
	 */
	public void doPerform(String widgetName, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
