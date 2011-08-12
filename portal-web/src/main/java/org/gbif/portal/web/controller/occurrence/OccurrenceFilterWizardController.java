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
package org.gbif.portal.web.controller.occurrence;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.KeyValueDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.GeospatialManager;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.util.geospatial.LatLongBoundingBox;
import org.gbif.portal.web.content.geospatial.BoundingBoxFilterHelper;
import org.gbif.portal.web.filter.FilterDTO;
import org.gbif.portal.web.filter.FilterMapWrapper;
import org.gbif.portal.web.filter.FilterUtils;
import org.gbif.portal.web.util.TaxonConceptUtils;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * Filter Wizard Controller for Occurrrence Filters.
 *
 * @author Dave Martin
 */
public class OccurrenceFilterWizardController implements Controller {

	protected static Log logger = LogFactory.getLog(OccurrenceFilterWizardController.class);	
	protected FilterMapWrapper filterMapWrapper;

	protected TaxonomyManager taxonomyManager;
	protected TaxonConceptUtils taxonConceptUtils;
	protected DataResourceManager dataResourceManager;
	protected GeospatialManager geospatialManager;
	
	protected FilterDTO classificationFilter;
	protected FilterDTO boundingBoxFilter;
	protected FilterDTO dataResourceIdFilter;
	protected FilterDTO occurrenceDateFilter;
	protected FilterDTO yearRangeFilter;
	protected FilterDTO geoRegionFilter;
	protected MessageSource messageSource;
	
	/**
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String filterId = request.getParameter("filterId");
		String currValue = request.getParameter("currValue");
		FilterDTO filterDTO = FilterUtils.getFilterById(filterMapWrapper.getFilters(), filterId);
		String viewName = filterDTO.getWizardView();
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("messageSource", messageSource);
		
		if(logger.isDebugEnabled()){
			logger.debug("filter id:"+filterId);
			logger.debug("current value:"+currValue);
			logger.debug("wizard view name:"+viewName);
		}
		
		//check for bounding box filter
		if(filterId.equals(boundingBoxFilter.getId()) && StringUtils.isNotEmpty(currValue)){
			LatLongBoundingBox llbb = BoundingBoxFilterHelper.getLatLongBoundingBox(currValue);
			if(llbb!=null)
				mav.addObject("boundingBox", llbb);
		}
		
		//check for classification filter
		if(filterId.equals(classificationFilter.getId()) ){
			DataProviderDTO dataProvider = dataResourceManager.getNubDataProvider();
			List<BriefTaxonConceptDTO> concepts = null;
			if(StringUtils.isNotEmpty(currValue)){
				concepts = taxonomyManager.getClassificationFor(currValue, false, null, true);
				List<BriefTaxonConceptDTO> childConcepts = taxonomyManager.getChildConceptsFor(currValue, true);
				BriefTaxonConceptDTO selectedConcept = taxonomyManager.getBriefTaxonConceptFor(currValue);
				taxonConceptUtils.organiseUnconfirmedNames(request, selectedConcept, concepts, childConcepts);
				mav.addObject("selectedConcept", selectedConcept);
			} else {
			  // we always use the nub resource 
				concepts = taxonomyManager.getRootTaxonConceptsForTaxonomy(null,"1");
			}
			if(dataProvider!=null)
				mav.addObject("dataProvider", dataProvider)	;
			if(concepts!=null)
				mav.addObject("concepts", concepts);
		}
		
		//check for dataResource filter
		if(filterId.equals(dataResourceIdFilter.getId()) ){
			List<KeyValueDTO> dataProviderList = dataResourceManager.getDataProviderList();
			mav.addObject("dataProviders", dataProviderList);
			List<KeyValueDTO> resourceNetworkList = dataResourceManager.getResourceNetworkList();
			mav.addObject("networks", resourceNetworkList);
		}
		
		//check for geoRegion filter
		if(filterId.equals(geoRegionFilter.getId()) ){
			List<KeyValueDTO> countryList = geospatialManager.getCountryList();
			mav.addObject("countries", countryList);
		}

		//check for occurrence date filter
		if(filterId.equals(occurrenceDateFilter.getId()) ){
			Date today = new Date(System.currentTimeMillis());
			mav.addObject("today", today);
			Date lastWeek = DateUtils.addDays(today, -6);
			mav.addObject("lastWeek", lastWeek);
			Date oneMonthAgo = DateUtils.addMonths(today, -1);
			mav.addObject("oneMonthAgo", oneMonthAgo);
			Date sixMonthsAgo = DateUtils.addMonths(today, -5);
			mav.addObject("sixMonthsAgo", sixMonthsAgo);
			Date oneYearAgo = DateUtils.addYears(today, -1);
			mav.addObject("oneYearAgo", oneYearAgo);
			Date fiveYearsAgo = DateUtils.addYears(today, -5);
			mav.addObject("fiveYearsAgo", fiveYearsAgo);
			Date tenYearsAgo = DateUtils.addYears(today, -10);
			mav.addObject("tenYearsAgo", tenYearsAgo);
		}		
		
		//check for year range filter
		if(filterId.equals(yearRangeFilter.getId())){
			Date today = new Date(System.currentTimeMillis());
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.setTime(today);
			int thisYear = calendar.get(Calendar.YEAR);
			mav.addObject("thisYear", thisYear);
		}
		
		mav.addObject("filterId", filterId);
		return mav;
	}

	/**
	 * @param filterMapWrapper the filterMapWrapper to set
	 */
	public void setFilterMapWrapper(FilterMapWrapper filterMapWrapper) {
		this.filterMapWrapper = filterMapWrapper;
	}

	/**
	 * @param taxonomyManager the taxonomyManager to set
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
	}

	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}

	/**
	 * @return the geospatialManager
	 */
	public GeospatialManager getGeospatialManager() {
		return geospatialManager;
	}

	/**
	 * @param geospatialManager the geospatialManager to set
	 */
	public void setGeospatialManager(GeospatialManager geospatialManager) {
		this.geospatialManager = geospatialManager;
	}

	/**
	 * @param boundingBoxFilter the boundingBoxFilter to set
	 */
	public void setBoundingBoxFilter(FilterDTO boundingBoxFilter) {
		this.boundingBoxFilter = boundingBoxFilter;
	}

	/**
	 * @param classificationFilter the classificationFilter to set
	 */
	public void setClassificationFilter(FilterDTO classificationFilter) {
		this.classificationFilter = classificationFilter;
	}

	/**
	 * @param dataResourceIdFilter the dataResourceIdFilter to set
	 */
	public void setDataResourceIdFilter(FilterDTO dataResourceIdFilter) {
		this.dataResourceIdFilter = dataResourceIdFilter;
	}

	/**
	 * @param occurrenceDateFilter the occurrenceDateFilter to set
	 */
	public void setOccurrenceDateFilter(FilterDTO occurrenceDateFilter) {
		this.occurrenceDateFilter = occurrenceDateFilter;
	}

	/**
	 * @param taxonConceptUtils the taxonConceptUtils to set
	 */
	public void setTaxonConceptUtils(TaxonConceptUtils taxonConceptUtils) {
		this.taxonConceptUtils = taxonConceptUtils;
	}

	/**
	 * @param yearRangeFilter the yearRangeFilter to set
	 */
	public void setYearRangeFilter(FilterDTO yearRangeFilter) {
		this.yearRangeFilter = yearRangeFilter;
	}

	/**
	 * @param geoRegionFilter the geoRegionFilter to set
	 */
	public void setGeoRegionFilter(FilterDTO geoRegionFilter) {
		this.geoRegionFilter = geoRegionFilter;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}