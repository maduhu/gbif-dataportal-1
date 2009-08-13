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

import java.util.Locale;
import java.util.Map;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

/**
 * A view resolver that maps the view names to a view in its set of mappings.
 * 
 * It is intended the viewMappings are configured in spring.
 * 
 * @author dmartin
 */
public class GenericViewResolver extends AbstractCachingViewResolver implements Ordered {
	
	/** Allows the order of this resolver to be configurable */
	protected int order = Integer.MAX_VALUE;	
	/** The view mappings this resolver supports */
	protected Map<String, View> viewMappings;
	
	/**
	 * Returns a view matching the supplied name, null if non matching to allow chaining.
	 * 
	 * @see org.springframework.web.servlet.view.AbstractCachingViewResolver#loadView(java.lang.String, java.util.Locale)
	 */
	@Override
	protected View loadView(String viewName, Locale locale) throws Exception {
		if(logger.isDebugEnabled())
			logger.debug("view name: "+viewName);
		if(viewName!=null){
			if(viewMappings.containsKey(viewName.toLowerCase())){
				logger.debug("view found, returning view");
				View theView = viewMappings.get(viewName.toLowerCase());
				return theView;
			}
		}
		logger.debug("view not found in mappings, returning null");
		return null;
	}
	
	/**
	 * @param viewMappings the viewMappings to set
	 */
	public void setViewMappings(Map<String, View> viewMappings) {
		this.viewMappings = viewMappings;
	}

	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}
}