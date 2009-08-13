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
package org.gbif.portal.web.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;

/**
 * Filter utils. Includes utilities for adding warnings to the request and selecting filters
 * by id.
 * 
 * @author dmartin
 */
public class FilterUtils {
	
	public static final String filterWarningsRequestKey = "filterWarnings";
	public static final String fatalFilterWarningsRequestKey = "fatalFilterWarnings";

	protected static String filterOrPredicate = "filter.or";
	
	/**
	 * Retrieves a Filter by its id.
	 * @param filterId
	 * @return the filter
	 */
	public static FilterDTO getFilterById(List<FilterDTO> filters, String filterId){
		for(FilterDTO filterDTO: filters){
			if (filterId.equals(filterDTO.getId()))
				return filterDTO;
		}
		return null;
	}	
	
	/**
	 * Creates a description for this query.
	 * 
	 * @param filters
	 * @param criteria
	 * @param messageSource
	 * @param locale
	 * @return
	 */
	public static String getQueryDescription(List<FilterDTO> filters, CriteriaDTO criteria, MessageSource messageSource, Locale locale){
		StringBuffer sb = new StringBuffer();
		String lastSubject = "";
		
		for(CriterionDTO criterionDTO: criteria.getCriteria()){
			if(sb.length()>0)
				sb.append(", ");
			
			if(!lastSubject.equals(criterionDTO.getSubject())){
				lastSubject = criterionDTO.getSubject();
				FilterDTO filter = FilterUtils.getFilterById(filters, criterionDTO.getSubject());
				String displayName = messageSource.getMessage(filter.getDisplayName(), null, locale);
				sb.append(displayName);
				sb.append(' ');
				PredicateDTO predicate = filter.getPredicates().get(Integer.parseInt(criterionDTO.getPredicate()));
				String predicateDisplayName = messageSource.getMessage(predicate.getValue(), null, locale);
				sb.append(predicateDisplayName);
			} else {
				sb.append(' ');
				sb.append(messageSource.getMessage(filterOrPredicate, null, locale));
			}
			
			sb.append(' ');
			String object = messageSource.getMessage(criterionDTO.getDisplayValue(), null, criterionDTO.getDisplayValue(), locale);
			sb.append(object);
		}
		
		return sb.toString();
	}

	/**
	 * Add filter warnings to this request.
	 * 
	 * @param warning
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public static void addFilterWarning(HttpServletRequest request, String warning){
		List<String> warnings = (List<String>) request.getAttribute(filterWarningsRequestKey);
		if(warnings==null){
			warnings = new ArrayList<String>();
			request.setAttribute(filterWarningsRequestKey, warnings);
		}
		warnings.add(warning);
	}
	
	/**
	 * Add filter warnings to this request.
	 * 
	 * @param warning
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getFilterWarnings(HttpServletRequest request){
		List<String> warnings = (List<String>) request.getAttribute(filterWarningsRequestKey);
		if(warnings==null){
			warnings = new ArrayList<String>();
			request.setAttribute(filterWarningsRequestKey, warnings);
		}
		return warnings;
	}	
	
	/**
	 * Add fatal filter warnings to this request. This should indicate that the query should not be performed.
	 * 
	 * @param warning
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public static void addFatalFilterWarning(HttpServletRequest request, String warning){
		List<String> warnings = (List<String>) request.getAttribute(fatalFilterWarningsRequestKey);
		if(warnings==null){
			warnings = new ArrayList<String>();
			request.setAttribute(fatalFilterWarningsRequestKey, warnings);
		}
		warnings.add(warning);
	}	
	
	/**
	 * Add filter warnings to this request.
	 * 
	 * @param warning
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getFatalFilterWarnings(HttpServletRequest request){
		List<String> warnings = (List<String>) request.getAttribute(fatalFilterWarningsRequestKey);
		if(warnings==null){
			warnings = new ArrayList<String>();
			request.setAttribute(fatalFilterWarningsRequestKey, warnings);
		}
		return warnings;
	}	
	
	/**
	 * Checks for the existence of fatal warnings. If any present returns true.
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isFilterQueryRequestValid(HttpServletRequest request){
		List<String> warnings = (List<String>) request.getAttribute(fatalFilterWarningsRequestKey);
		if(warnings!=null && !warnings.isEmpty())
			return false;
		return true;		
	}

	/**
	 * @param filterOrPredicate the filterOrPredicate to set
	 */
	public static void setFilterOrPredicate(String filterOrPredicate) {
		FilterUtils.filterOrPredicate = filterOrPredicate;
	}
}