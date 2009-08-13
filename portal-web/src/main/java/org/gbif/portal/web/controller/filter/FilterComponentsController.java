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
package org.gbif.portal.web.controller.filter;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.web.filter.CriteriaDTO;
import org.gbif.portal.web.filter.CriteriaUtil;
import org.gbif.portal.web.filter.CriterionDTO;
import org.gbif.portal.web.filter.FilterMapWrapper;
import org.gbif.portal.web.util.QueryHelper;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Filter Controller used for AJAX request for refreshing components of the 
 * filter searching.
 * 
 * @author dmartin
 */
public class FilterComponentsController extends MultiActionController {
	
	protected FilterMapWrapper filterMapWrapper;	
	protected String criteriaRequestKey="criteria";
	protected String filtersRequestKey="filters";
	protected String filterMapLayerRoot = "filter"; 
	protected String constructedFiltersView = "constructedFilters";
	protected String filterHelpView;
	protected String filterAction;
	protected String filterActionModelKey="filterAction";
	protected String filterOptions;
	protected String filterIndexModelKey="filterIndex";
	
	/**
	 * Refresh or constructed the view of constructed filters.
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView refreshFilters(HttpServletRequest request, HttpServletResponse response) {
		//retrieve the criteria from the request
		CriteriaDTO criteria  = CriteriaUtil.getCriteriaAndPopulate(request, filterMapWrapper.getFilters());
		if(criteria.getCriteria().isEmpty())
			return new ModelAndView(filterHelpView);
    	return constructFiltersView(criteria, null);
	}	

	/**
	 * Method intended to be called via AJAX used to add a new filter or update an existing filter.
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView of the containing constructed filters
	 */
	public ModelAndView addOrUpdateFilters(HttpServletRequest request, HttpServletResponse response) {
		String subject = request.getParameter("newSubject");
		String predicate = request.getParameter("newPredicate");
		String value = request.getParameter("newValue");
		value = QueryHelper.tidyValue(value);
		CriterionDTO criterionDTO = new CriterionDTO(subject, predicate, value);
		CriteriaDTO criteria = CriteriaUtil.getCriteria(request, filterMapWrapper.getFilters());
		//retrieve the criteria from the request
		criteria.getCriteria().add(criterionDTO);
		Locale locale = RequestContextUtils.getLocale(request);
		CriteriaUtil.populateCriteria(filterMapWrapper.getFilters(), criteria, locale);
		criteria.setCriteria(CriteriaUtil.removeDuplicates(criteria.getCriteria()));
		CriteriaUtil.orderByCategorySubjectAndValue(criteria);
		Integer index = criteria.getCriteria().indexOf(criterionDTO);
    	return constructFiltersView(criteria, index);
	}

	/**
	 * Construct the Model for the filters view.
	 * 
	 * @param criteria
	 * @param filterIndex, nullable
	 * @return
	 */
	public ModelAndView constructFiltersView(CriteriaDTO criteria, Integer filterIndex){
		ModelAndView mav = new ModelAndView(constructedFiltersView);
    	mav.addObject(criteriaRequestKey, criteria);
    	mav.addObject(filtersRequestKey, filterMapWrapper.getFilters());
    	if(filterIndex!=null)
    		mav.addObject(filterIndexModelKey, new Integer(filterIndex));
    	mav.addObject(filterActionModelKey, filterAction);
    	return mav;
	}
	
	/**
	 * @param criteriaRequestKey the criteriaRequestKey to set
	 */
	public void setCriteriaRequestKey(String criteriaRequestKey) {
		this.criteriaRequestKey = criteriaRequestKey;
	}

	/**
	 * @param filterMapLayerRoot the filterMapLayerRoot to set
	 */
	public void setFilterMapLayerRoot(String filterMapLayerRoot) {
		this.filterMapLayerRoot = filterMapLayerRoot;
	}

	/**
	 * @param filterMapWrapper the filterMapWrapper to set
	 */
	public void setFilterMapWrapper(FilterMapWrapper filterMapWrapper) {
		this.filterMapWrapper = filterMapWrapper;
	}

	/**
	 * @param filtersRequestKey the filtersRequestKey to set
	 */
	public void setFiltersRequestKey(String filtersRequestKey) {
		this.filtersRequestKey = filtersRequestKey;
	}

	/**
	 * @param filterAction the filterAction to set
	 */
	public void setFilterAction(String filterAction) {
		this.filterAction = filterAction;
	}

	/**
	 * @param constructedFiltersView the constructedFiltersView to set
	 */
	public void setConstructedFiltersView(String constructedFiltersView) {
		this.constructedFiltersView = constructedFiltersView;
	}

	/**
	 * @param filterActionModelKey the filterActionModelKey to set
	 */
	public void setFilterActionModelKey(String filterActionModelKey) {
		this.filterActionModelKey = filterActionModelKey;
	}

	/**
	 * @param filterHelpView the filterHelpView to set
	 */
	public void setFilterHelpView(String filterHelpView) {
		this.filterHelpView = filterHelpView;
	}

	/**
	 * @param filterIndexModelKey the filterIndexModelKey to set
	 */
	public void setFilterIndexModelKey(String filterIndexModelKey) {
		this.filterIndexModelKey = filterIndexModelKey;
	}

	/**
	 * @param filterOptions the filterOptions to set
	 */
	public void setFilterOptions(String filterOptions) {
		this.filterOptions = filterOptions;
	}
}