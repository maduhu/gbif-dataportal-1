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
package org.gbif.portal.web.content.filter;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.web.filter.CriterionDTO;
import org.springframework.web.servlet.ModelAndView;

/**
 * If a Filter is configured to use a FilterHelper, this will be called with the provided triplets
 * prior to a filter query being performed. This allows parsing of values or mapping of values to keys.
 * 
 * This also enables a triplets to be changed to actually use a different triplet or a parsed value.
 * 
 * @author dmartin
 */
public interface FilterHelper {
	
	/**
	 * Preprocess a list of PropertyStoreTripletDTO before this is used to construct a query.
	 * This allows some meddling with the triplets to allow for a more optimal query.
	 * 
	 * @param request
	 * @param response
	 * @param criterion
	 */
	public void preProcess(List<PropertyStoreTripletDTO>triplets,  HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * Used to retrieve the HTML display value for a filter.
	 * 
	 * @param value
	 * @return the value to be displayed
	 */
	public String getDisplayValue(String value, Locale locale);
	
	/**
	 * Adds the details of this criterion to the request. In most cases this is just the value to be rendered.
	 * In the case of more complex filters e.g. Bounding box this maybe a LatLongBoundingBox for example.
	 * 
	 * This is to enable the rendering of the current value of the criterion - e.g. to render the currently
	 * set bounding box - Not currently in use. 
	 * 
	 * @param criterionDTO
	 * @param request
	 * @param response
	 */
	public void addCriterion2Request(CriterionDTO criterionDTO, ModelAndView mav, HttpServletRequest request);
	
	/**
	 * Supplies a default value for this filter.
	 * 
	 * @param request
	 * @return the default value
	 */
	public String getDefaultValue(HttpServletRequest request);	
	
	/**
	 * Supplies a default display value for this filter.
	 * Only useful for filters that requirea default value once selected.
	 * 
	 * @param request
	 * @return the default display value
	 */
	public String getDefaultDisplayValue(HttpServletRequest request);
}