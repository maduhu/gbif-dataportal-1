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
package org.gbif.portal.web.content;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The original thinking was that ContentProviders are decorators of of requests and models. 
 * They have *evolved* into being Helper classes reused across Controller implementations.
 * However the ContentProvider interface is useful for populating a simple page with content
 * such as the front page - i.e pages/points in the webapp without any associated state.
 * 
 * @author dmartin
 */
public interface ContentProvider {
	
	/**
	 * Add information to this Content Model
	 * @param cc the ContentView to provide content for.
	 * @param a request object, could be a Command Object or a HttpServletRequest
	 */
	public void addContent(ContentView cc, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
