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
package org.gbif.portal.web.controller.taxonomy;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.web.download.Field;
import org.gbif.portal.web.filter.CriteriaDTO;
import org.gbif.portal.web.filter.CriteriaUtil;
import org.gbif.portal.web.filter.FilterMapWrapper;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * A controller for the taxonomy download form.
 * 
 * @author dmartin
 */
public class TaxonomyDownloadController extends MultiActionController {

	protected FilterMapWrapper taxonomyFilters;
	
	protected String downloadSpreadsheetView;
	
	protected List<Field> mandatoryDownloadFields;
	
	protected List<Field> classificationDownloadFields;
	
	protected List<Field> otherDownloadFields;
	
	protected String criteriaRequestKey = "criteria";
	
	protected String filterRequestKey = "filters";
	
	/**
	 * Determines the view based on the map provided, and if none found defaults to the 
	 * "occurrenceFilter" view.
	 * 
	 * @param request To get the view from
	 * @param response To write to
	 * @return The model and view for the filter params provided
	 */
	public ModelAndView downloadSpreadsheet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//retrieve the criteria from the request
		CriteriaDTO criteria  = CriteriaUtil.getCriteriaAndPopulate(request, taxonomyFilters.getFilters());
		ModelAndView mav = new ModelAndView(downloadSpreadsheetView);
		mav.addObject("mandatoryFields", mandatoryDownloadFields);
		mav.addObject("classificationFields", classificationDownloadFields);
		mav.addObject("otherFields", otherDownloadFields);
		mav.addObject("searchId", System.currentTimeMillis());
    	mav.addObject(criteriaRequestKey, criteria);
    	mav.addObject(filterRequestKey, taxonomyFilters.getFilters());
    	return mav;
	}

	/**
	 * @param criteriaRequestKey the criteriaRequestKey to set
	 */
	public void setCriteriaRequestKey(String criteriaRequestKey) {
		this.criteriaRequestKey = criteriaRequestKey;
	}

	/**
	 * @param downloadSpreadsheetView the downloadSpreadsheetView to set
	 */
	public void setDownloadSpreadsheetView(String downloadSpreadsheetView) {
		this.downloadSpreadsheetView = downloadSpreadsheetView;
	}

	/**
	 * @param filterRequestKey the filterRequestKey to set
	 */
	public void setFilterRequestKey(String filterRequestKey) {
		this.filterRequestKey = filterRequestKey;
	}

	/**
	 * @param mandatoryDownloadFields the mandatoryDownloadFields to set
	 */
	public void setMandatoryDownloadFields(List<Field> mandatoryDownloadFields) {
		this.mandatoryDownloadFields = mandatoryDownloadFields;
	}

	/**
	 * @param otherDownloadFields the otherDownloadFields to set
	 */
	public void setOtherDownloadFields(List<Field> otherDownloadFields) {
		this.otherDownloadFields = otherDownloadFields;
	}

	/**
	 * @param taxonomyFilters the taxonomyFilters to set
	 */
	public void setTaxonomyFilters(FilterMapWrapper taxonomyFilters) {
		this.taxonomyFilters = taxonomyFilters;
	}

	/**
	 * @param classificationDownloadFields the classificationDownloadFields to set
	 */
	public void setClassificationDownloadFields(
			List<Field> classificationDownloadFields) {
		this.classificationDownloadFields = classificationDownloadFields;
	}	
}