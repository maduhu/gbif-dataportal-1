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

/**
 * Call back interface used by ContentProviders to add content to a
 * ContentView.
 * 
 * ContentView will be implemented by a class wrapping a API specific
 * Model object (e.g. ModelAndView in SpringMVC, ServletRequest in struts).
 * 
 * @author dmartin
 */
public interface ContentView {

	/**
	 * The object and its name to add to this view. 
	 * @param name
	 * @param object
	 */	
	public void addObject(String name, Object object);
}