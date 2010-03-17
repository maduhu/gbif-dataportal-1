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

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.dto.CountDTO;
import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.geospatial.CellDensityDTO;
import org.gbif.portal.dto.geospatial.CountryDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.dto.util.BoundingBoxDTO;
import org.gbif.portal.dto.util.EntityType;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.GeospatialManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.service.triplet.TripletQueryManager;
import org.gbif.portal.util.geospatial.CellIdUtils;
import org.gbif.portal.util.geospatial.LatLongBoundingBox;
import org.gbif.portal.util.propertystore.PropertyStore;
import org.gbif.portal.web.content.filter.FilterContentProvider;
import org.gbif.portal.web.content.filter.PagingTripletQueryProvider;
import org.gbif.portal.web.content.geospatial.BoundingBoxFilterHelper;
import org.gbif.portal.web.content.map.MapContentProvider;
import org.gbif.portal.web.download.Field;
import org.gbif.portal.web.filter.CriteriaDTO;
import org.gbif.portal.web.filter.CriteriaUtil;
import org.gbif.portal.web.filter.CriterionDTO;
import org.gbif.portal.web.filter.FilterDTO;
import org.gbif.portal.web.filter.FilterMapWrapper;
import org.gbif.portal.web.filter.FilterUtils;
import org.gbif.portal.web.util.QueryHelper;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;
/**
 * Controller for Filter queries. The Controller is responsible for retrieving
 * the search criteria from the request, executing the query and then sending 
 * results to the correct view.
 * 
 * @author dmartin
 */
public class OccurrenceFilterController extends MultiActionController {

	/** The property store to use to produce the queries */
	protected PropertyStore webappPropertyStore;
	/** The property store namespace to use to produce the queries */
	protected String namespace;
	
	/** Query helper used to produce triplets from urls strings */
	protected QueryHelper queryHelper;
	
	/** Service managers */
	protected TripletQueryManager tripletQueryManager;
	protected TripletQueryManager countsQueryManager;
	protected TripletQueryManager mapLayerQueryManager;
	protected GeospatialManager geospatialManager;
	protected DataResourceManager dataResourceManager;
	protected TaxonomyManager taxonomyManager;

	/** Triplet query provider that provides paging over occurrence results */
	protected PagingTripletQueryProvider pagingTripletQueryProvider;
	
	/** Fields available for download */
	protected List<Field> mandatoryDownloadFields;
	protected List<Field> taxonomyDownloadFields;
	protected List<Field> geospatialDownloadFields;	
	protected List<Field> datasetDownloadFields;
	
	/** Species count limit for the species list view */
	protected int speciesCountLimit = 1000;

	public String minXRequestKey = "minX";
	public String minYRequestKey = "minY";
	public String maxXRequestKey = "maxX";
	public String maxYRequestKey= "maxY";	

	/** Filter stuff */
	protected FilterMapWrapper occurrenceFilters;
	protected FilterDTO dataResourceFilter;
	protected FilterDTO dataResourceIdFilter;
	protected FilterDTO dataProviderIdFilter;
	protected FilterDTO countryFilter;
	protected FilterDTO regionFilter;
	protected FilterDTO scientificNameFilter;
	protected FilterDTO classificationFilter;
	protected FilterDTO boundingBoxFilter;
	protected FilterDTO georeferencedFilter;
	protected FilterDTO geoConsistencyFilter;	

	/** Content providers */
	protected FilterContentProvider filterContentProvider;
	protected MapContentProvider mapContentProvider;	
	
	protected String returnPredicateSubject = "SERVICE.QUERY.PREDICATE.RETURN";
	protected String selectFieldSubject = "SERVICE.QUERY.SUBJECT.SELECTFIELD";	

	/** The max number of records to count up to before display "over x records" */
	protected int maxCount=1000;	

	/** Parameter for the selected data providers */
	protected String dataProviderParameterKey="dp";
	/** Parameter for the selected data resources */
	protected String dataResourceParameterKey="dr";
	/** Parameter for the selected countries */
	protected String countryParameterKey="cn";
	
	/** The catch all entity key */
	protected String catchAllEntityKey = "0";
	
	/** View names */
	protected String occurrenceFilterProviderCountsView = "occurrenceFilterProviderCounts";	
	protected String occurrenceFilterResourceCountsView = "occurrenceFilterResourceCounts";
	protected String occurrenceFilterCountryCountsView = "occurrenceFilterCountryCounts"; 
	protected String occurrenceFilterSpeciesCountsView ="occurrenceFilterSpeciesCounts";
	protected String downloadSpreadsheetView = "occurrenceDownloadSpreadsheet";
	protected String downloadXMLView = "occurrenceDownloadXML";
	protected String defaultView;
	protected String defaultMapView;
	
	/** Model key for the last edited criterion */
	protected String currentCriterionModelKey = "currentCriterion";
	
	/** Parameter indicating if counts are available to view for rendering */
	protected String countsAvailableModelKey = "countsAvailable";

	/** The results limit for a distinct query */
	protected String resultsLimitModelKey = "resultsLimit";
	
	/** Model key for the retrieved counts */
	protected String countsModelKey = "counts";

	/** Model key for the results */
	protected String resultsModelKey="results";

	/** Model key for the criteria */
	protected String criteriaRequestKey="criteria";
	
	/** Model key for the points totals */
	protected String pointsTotalRequestKey="pointsTotal";
	
	/** Model key for the available filters for the drop down */
	protected String filtersRequestKey="filters";
	
	/** The map layer root */
	protected String filterMapLayerRoot = "filter"; 
	
	/** Request parameter giving the requested format */
	protected String formatRequestParam = "format";
	
	/** Download redirect path */
	protected String downloadRedirectPath = "startDownload.htm";
	
	/** The results limit for a distinct query */
	protected String messageSourceKey = "messageSource";
	
	/** Message source for i18n lookups */
	protected MessageSource messageSource;
	
	/** The root url for search wizards */ 
	protected String rootWizardUrl;

	/** The root url for search wizards */ 
	protected String rootWizardUrlModelKey = "rootWizardUrl";
	
	/** Number of sample results to display */
	protected int maxSampleResults = 5;
	
	protected List<String> geoFormats;
	
	/**
	 * Determines the view based on the map provided, and if none found defaults to the 
	 * "occurrenceFilter" view.
	 * @param request To get the view from
	 * @param response To write to
	 * @return The model and view for the filter params provided
	 */
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//retrieve the criteria from the request
		CriteriaDTO criteria  = CriteriaUtil.getCriteria(request,occurrenceFilters.getFilters());
		if(request.getCharacterEncoding() == null) {
			for(int c = 0; c < criteria.size(); c++) {
				criteria.get(c).setValue(URLEncoder.encode(criteria.get(c).getValue(), "ISO-8859-1"));
				criteria.get(c).setValue(URLDecoder.decode(criteria.get(c).getValue(), "UTF-8"));
				criteria.get(c).setDisplayValue(URLEncoder.encode(criteria.get(c).getDisplayValue(), "ISO-8859-1"));
				criteria.get(c).setDisplayValue(URLDecoder.decode(criteria.get(c).getDisplayValue(), "UTF-8"));
				request.setCharacterEncoding("UTF-8");
			}
		}
		
		//check for data provider ids		
		String[] dataProviderIds = request.getParameterValues(dataProviderParameterKey);
		if(dataProviderIds!=null && dataProviderIds.length>0){
			for (int i=0; i<dataProviderIds.length; i++){
				CriterionDTO criterionDTO = new CriterionDTO(dataProviderIdFilter.getId(), dataProviderIdFilter.getDefaultPredicateId(), dataProviderIds[i]);
				criteria.getCriteria().add(criterionDTO);
			}
		}
		//check for data resource ids
		String[] dataResourceIds = request.getParameterValues(dataResourceParameterKey);
		if(dataResourceIds!=null && dataResourceIds.length>0){
			for (int i=0; i<dataResourceIds.length; i++){
				CriterionDTO criterionDTO = new CriterionDTO(dataResourceIdFilter.getId(), dataResourceIdFilter.getDefaultPredicateId(), dataResourceIds[i]);
				criteria.getCriteria().add(criterionDTO);
			}
		}
		//check for country ids		
		String[] countryIds = request.getParameterValues(countryParameterKey);
		if(countryIds!=null && countryIds.length>0){
			for (int i=0; i<countryIds.length; i++){
				CriterionDTO criterionDTO = new CriterionDTO(countryFilter.getId(), countryFilter.getDefaultPredicateId(), countryIds[i]);
				criteria.getCriteria().add(criterionDTO);
			}
		}
		Locale locale = RequestContextUtils.getLocale(request);
		CriteriaUtil.populateCriteria(occurrenceFilters.getFilters(), criteria, locale);
		criteria.setCriteria(CriteriaUtil.removeDuplicates(criteria.getCriteria()));
		
		//convert to triplets
		List<PropertyStoreTripletDTO> triplets = queryHelper.getTriplets(occurrenceFilters.getFilters(), criteria, request, response);
    	ModelAndView mav = new ModelAndView("occurrenceSearchWithEditorPane");		
		
		//check for a set filter
		String subject = request.getParameter("newFilterSubject");
		String predicate = request.getParameter("newFilterPredicate");
		String value = request.getParameter("newFilterValue");
		value = QueryHelper.tidyValue(value);
		
		if(subject!=null && predicate!=null && value!=null){
			//initialise the filter
			CriterionDTO criterion = new CriterionDTO(subject, predicate, value);
			CriteriaUtil.setCriterionDisplayValue(occurrenceFilters.getFilters(), criterion, locale);
			mav.addObject(currentCriterionModelKey, criterion);
			FilterDTO filter = FilterUtils.getFilterById(occurrenceFilters.getFilters(), criterion.getSubject());
			if(filter.getFilterHelper()!=null)
				filter.getFilterHelper().addCriterion2Request(criterion, mav, request);
		}
		
		//do the query 
		if(FilterUtils.isFilterQueryRequestValid(request)){
			SearchResultsDTO searchResults = tripletQueryManager.doTripletQuery(triplets, true, new SearchConstraints(0, maxSampleResults));
			mav.addObject(resultsModelKey, searchResults.getResults());		
		}
		//construct the model
    	mav.addObject(criteriaRequestKey, criteria);
    	mav.addObject(filtersRequestKey, occurrenceFilters.getFilters());
    	mav.addObject(messageSourceKey, messageSource);
    	mav.addObject(rootWizardUrlModelKey, rootWizardUrl);
    	return mav;
	}
	
	/**
	 * Determines the view based on the map provided, and if none found defaults to the 
	 * "occurrenceFilter" view.
	 * @param request To get the view from
	 * @param response To write to
	 * @return The model and view for the filter params provided
	 */
	public ModelAndView searchWithTable(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//retrieve the criteria from the request
		CriteriaDTO criteria = CriteriaUtil.getCriteriaAndPopulate(request, occurrenceFilters.getFilters());
		//convert to triplets
		List<PropertyStoreTripletDTO> triplets = queryHelper.getTriplets(occurrenceFilters.getFilters(), criteria, request, response);
    	ModelAndView mav = new ModelAndView("occurrenceSearchWithTable");		
		//do the query - pagingTripletQueryProvider will store the results within the request
		pagingTripletQueryProvider.doTripletQuery(triplets, criteria.isMatchAll(), request, response);
		//construct the model
    	mav.addObject(criteriaRequestKey, criteria);
    	mav.addObject(filtersRequestKey, occurrenceFilters.getFilters());
    	mav.addObject(messageSourceKey, messageSource);
    	return mav;
	}

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
		CriteriaDTO criteria  = CriteriaUtil.getCriteriaAndPopulate(request, occurrenceFilters.getFilters());
		ModelAndView mav = new ModelAndView(downloadSpreadsheetView);
		mav.addObject("mandatoryFields", mandatoryDownloadFields);
		mav.addObject("taxonomyFields", taxonomyDownloadFields);
		mav.addObject("geospatialFields", geospatialDownloadFields);
		mav.addObject("datasetFields", datasetDownloadFields);
		mav.addObject("searchId", System.currentTimeMillis());
    	mav.addObject(criteriaRequestKey, criteria);
    	mav.addObject(filtersRequestKey, occurrenceFilters.getFilters());
    	return mav;
	}
	
	/**
	 * Redirects to a virtual file path.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView downloadResults(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		StringBuffer sb = new StringBuffer();
		sb.append(downloadRedirectPath);
		sb.append("?");
		//reconstruct the url with parameters and some meddling
		Enumeration<String> parameterNames = request.getParameterNames();
		
		String searchId = ServletRequestUtils.getStringParameter(request, "searchId", Long.toString(System.currentTimeMillis()));
		sb.append("searchId");
		sb.append("=");
		sb.append(searchId);
		sb.append("&");
		
		while(parameterNames.hasMoreElements()){
			
			sb.append("&");
			String paramName = parameterNames.nextElement();
			String paramValue = request.getParameter(paramName);
			
			if("criteria".equals(paramName)){
				CriteriaDTO criteria  = CriteriaUtil.getCriteria(paramValue, occurrenceFilters.getFilters(), null);

				//if its a format requiring georeferencing - add criteria if not there
				String format = request.getParameter("format");
				if(format!=null && geoFormats!=null && geoFormats.contains(format)){
					criteria.add(new CriterionDTO("28","0","0"));
				}				
				sb.append("criteria="+URLEncoder.encode(CriteriaUtil.getUrl(criteria), "UTF-8"));
			} else if(!"searchId".equals(paramName)){
				sb.append(paramName);
				sb.append("=");
				sb.append(paramValue);
			}
		}
		return new ModelAndView(new RedirectView(sb.toString()));
	}
	
	/**
	 * @see org.springframework.web.servlet.mvc.multiaction.MultiActionController#handleNoSuchRequestHandlingMethod(org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException exception, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return search(request, response);
	}		

	/**
	 * Determines the view based on the map provided, and if none found defaults to the 
	 * "occurrenceFilter" view.
	 * @param request To get the view from
	 * @param response To write to
	 * @return The model and view for the filter params provided
	 */
	public ModelAndView occurrenceCount(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//retrieve the criteria from the request
		CriteriaDTO criteria  = CriteriaUtil.getCriteriaAndPopulate(request,occurrenceFilters.getFilters());
		ModelAndView mav = new ModelAndView("occurrenceCountView");		
		//convert to triplets
		List<PropertyStoreTripletDTO> triplets = queryHelper.getTriplets(occurrenceFilters.getFilters(), criteria, request, response);
		triplets.add(new PropertyStoreTripletDTO(queryHelper.getQueryNamespace(), selectFieldSubject, returnPredicateSubject, "SERVICE.OCCURRENCE.QUERY.RETURNFIELDS.ID"));
		//do the query 
		SearchResultsDTO searchResults = countsQueryManager.doTripletQuery(triplets, true, new SearchConstraints(0, maxCount+1));
		Integer recordCount = (Integer) searchResults.getResults().size();
		//construct the model
    	mav.addObject(criteriaRequestKey, criteria);
    	mav.addObject(filtersRequestKey, occurrenceFilters.getFilters());
    	mav.addObject("maxCount", maxCount);
    	mav.addObject("recordCount", recordCount);
    	return mav;
	}
	
	/**
	 * Determines the view based on the map provided, and if none found defaults to the 
	 * "occurrenceFilter" view.
	 * @param request To get the view from
	 * @param response To write to
	 * @return The model and view for the filter params provided
	 */
	public ModelAndView actionsOccurrenceCount(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//retrieve the criteria from the request
		CriteriaDTO criteria  = CriteriaUtil.getCriteriaAndPopulate(request,occurrenceFilters.getFilters());
		ModelAndView mav = new ModelAndView("actionsOccurrenceCountView");		
		//convert to triplets
		List<PropertyStoreTripletDTO> triplets = queryHelper.getTriplets(occurrenceFilters.getFilters(), criteria, request, response);
		triplets.add(new PropertyStoreTripletDTO(queryHelper.getQueryNamespace(), selectFieldSubject, returnPredicateSubject, "SERVICE.OCCURRENCE.QUERY.RETURNFIELDS.ID"));
		//do the query 
		SearchResultsDTO searchResults = countsQueryManager.doTripletQuery(triplets, true, new SearchConstraints(0, maxCount+1));
		Integer recordCount = (Integer) searchResults.getResults().size();
		//construct the model
    	mav.addObject(criteriaRequestKey, criteria);
    	mav.addObject(filtersRequestKey, occurrenceFilters.getFilters());
    	mav.addObject("maxCount", maxCount);
    	mav.addObject("recordCount", recordCount);
    	return mav;
	}	
	
	/**
	 * Prototype full screen bounding box wizard.
	 * 
	 * TODO the switching to full to small.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView boundingBoxFullScreen(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//retrieve the criteria from the request
		CriteriaDTO criteria  = CriteriaUtil.getCriteriaAndPopulate(request,occurrenceFilters.getFilters());
		//convert to triplets
    	ModelAndView mav = new ModelAndView("boundingBoxFull");		
		//do the query 
    	mav.addObject(criteriaRequestKey, criteria);
    	mav.addObject(filtersRequestKey, occurrenceFilters.getFilters());
    	mav.addObject("fullScreen", Boolean.TRUE);
    	
    	//add bounding box to context
    	String currValue = request.getParameter("currValue");
    	if(StringUtils.isNotEmpty(currValue)){
			LatLongBoundingBox llbb = BoundingBoxFilterHelper.getLatLongBoundingBox(currValue);
			if(llbb!=null)
				mav.addObject("boundingBox", llbb);
    	}
    	return mav;
	}	
	
	/**
	 * Determines the view based on the map provided, and if none found defaults to the 
	 * "occurrenceFilter" view.
	 * 
	 * TODO Add a switch so that if the filter search consists of 1 entity type (taxon, resource, provider, network) and/or just
	 * geospatial filters, make use of cell density tables
	 * 
	 * @param request To get the view from
	 * @param response To write to
	 * @return The model and view for the filter params provided
	 * @throws Exception On error in underlying layers
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView searchWithMap(HttpServletRequest request, HttpServletResponse response) throws Exception {	

		ModelAndView mav = new ModelAndView("occurrenceSearchWithMap");		
		//retrieve the criteria from the request
		CriteriaDTO criteria  = CriteriaUtil.getCriteriaAndPopulate(request, occurrenceFilters.getFilters());

		List<PropertyStoreTripletDTO> triplets = queryHelper.getTriplets(occurrenceFilters.getFilters(), criteria, request, response);
		
		//if query suitable for cell density, switch over 
		if(filterContentProvider.isQuerySuitableForCellDensity(criteria)){
			mapQueryToCellDensities(request, criteria);
		} else {
			
			//add extra triplets to say we require only geospatially aware records
			filterContentProvider.addGeoreferencedOnlyOccurrenceTriplets(triplets, queryHelper.getQueryNamespace());
			
			//choose the correct return fields
			PropertyStoreTripletDTO returnTypeTriplet = new PropertyStoreTripletDTO(queryHelper.getQueryNamespace(), selectFieldSubject, returnPredicateSubject, "SERVICE.OCCURRENCE.QUERY.RETURNFIELDS.MAPLAYERCOUNTS");
			triplets.add(returnTypeTriplet);
			
			//do the query 
			SearchConstraints constraints = new SearchConstraints(0, 64800);
			SearchResultsDTO searchResults = mapLayerQueryManager.doTripletQuery(triplets, criteria.isMatchAll(), constraints);
	    	
			//get a points total
			List<CellDensityDTO> cellDensities = searchResults.getResults();
			int pointsTotal = MapContentProvider.getPointsTotal(cellDensities);
	    	mav.addObject(pointsTotalRequestKey, pointsTotal);
	    	
    		//get a bounding box for the specified zoom level
    		BoundingBoxDTO bbDTO = mapContentProvider.getSpecifiedViewArea(request);
    		
    		//iterate through drawing up a total of the points that are viewable
    		int[] minMaxCellIds = CellIdUtils.getMinMaxCellIdsForBoundingBox(bbDTO.getLeft(), bbDTO.getLower(), bbDTO.getRight(), bbDTO.getUpper());
    		
    		int viewablePoints = 0;
    		for(CellDensityDTO cd: cellDensities){
    			if(CellIdUtils.isCellIdInBoundingBox(cd.getCellId(), minMaxCellIds[0], minMaxCellIds[1]))
    				viewablePoints +=cd.getCount();
    		}
    		mav.addObject("viewablePoints", viewablePoints);
	    	
	    	//set the map layer content
	    	mapContentProvider.addMapContent(request, filterMapLayerRoot, CriteriaUtil.getUrl(criteria), cellDensities, bbDTO);
		}
		
    	mav.addObject(criteriaRequestKey, criteria);
    	mav.addObject(filtersRequestKey, occurrenceFilters.getFilters());
    	return mav;
	}

	/**
	 * Maps a criteria query to a entity map layer.
	 * 
	 * @param request
	 * @param criteria
	 * @throws ServiceException
	 */
	private void mapQueryToCellDensities(HttpServletRequest request, CriteriaDTO criteria) throws ServiceException {
		
		logger.debug("Mapping: switching to using cell densities");
		request.setAttribute("noGeospatialIssues", true);
		
		//get bounding box criterion
		CriterionDTO boundingBoxCriterion = filterContentProvider.getBoundingBoxCriterion(criteria);
		//get entity type
		EntityType entityType = filterContentProvider.getCellDensityEntityType(criteria);
		//get list of ids
		List<String> entityKeys = filterContentProvider.getCellDensityEntityIds(criteria);
		
		BoundingBoxDTO bbDTO = null;
		
		//if bounding box selected and entity type selected
		if(boundingBoxCriterion!=null && entityType!=null && !entityType.equals(EntityType.TYPE_ALL)){
			logger.debug("Mapping: found entity type and bounding box");
			if(logger.isDebugEnabled())
				logger.debug("Mapping: using entityType:"+entityType);
			bbDTO = BoundingBoxFilterHelper.getBoundingBoxDTO(boundingBoxCriterion.getValue());
			mapContentProvider.addPointsTotalsToRequest(request, entityType, entityKeys, bbDTO);
			
		} else if (boundingBoxCriterion!=null) {
			logger.debug("Mapping: found bounding box - using catch all entity type");
			bbDTO = BoundingBoxFilterHelper.getBoundingBoxDTO(boundingBoxCriterion.getValue());
			//add points totals
			mapContentProvider.addPointsTotalsToRequest(request, EntityType.TYPE_ALL, catchAllEntityKey, bbDTO);
			
		} else {
			if(logger.isDebugEnabled())
				logger.debug("Mapping: using entityType:"+entityType);
			//add points totals
			mapContentProvider.addPointsTotalsToRequest(request, entityType, entityKeys, null);
		}
		
		List<CellDensityDTO> cellDensities = null;
		
		if(bbDTO==null)
			cellDensities = geospatialManager.get1DegCellDensities(entityType, entityKeys);
		else
			cellDensities = geospatialManager.get1DegCellDensities(entityType, entityKeys, bbDTO);
		
		//add the map layer information to the request.
    	String criteriaUrl = CriteriaUtil.getUrl(criteria);
		mapContentProvider.addMapContent(request, filterMapLayerRoot, criteriaUrl, cellDensities, bbDTO);
	}
	
	/**
	 * Retrieves the counts against the providers for this set of criteria
	 *
	 * @param request
	 * @param response
	 * @return ModelAndView which contains the provider list and counts
	 */
	public ModelAndView searchProviders (HttpServletRequest request, HttpServletResponse response) throws ServiceException{
		//interrogate the criteria - if it only contains a country filter then switch
		CriteriaDTO criteriaDTO = CriteriaUtil.getCriteria(request, occurrenceFilters.getFilters());
		if(criteriaDTO.size()==0){
			logger.debug("Switching to using service layer method getAllDataProviders");
			ModelAndView mav = new ModelAndView(occurrenceFilterProviderCountsView);
			List<DataProviderDTO> dps = dataResourceManager.getAllDataProviders();
	    	mav.addObject(countsModelKey, dps);
	    	mav.addObject(resultsModelKey, dps);			
	    	//add filters
	    	mav.addObject(filtersRequestKey, occurrenceFilters.getFilters());  	
	    	mav.addObject(criteriaRequestKey, criteriaDTO);
	    	mav.addObject(countsAvailableModelKey, true);
	    	return mav;
		}
		if(criteriaDTO.size()==1){
			logger.debug("Switching to using service layer method getDataProviderCountsForCountry");
			CriterionDTO criterionDTO = criteriaDTO.get(0);
			FilterDTO filterDTO = FilterUtils.getFilterById(occurrenceFilters.getFilters(), criterionDTO.getSubject());	
			if(filterDTO.getSubject().equals(countryFilter.getSubject())){
				List<CountDTO> counts = geospatialManager.getDataProviderCountsForCountry(criterionDTO.getValue());
		    	ModelAndView mav = new ModelAndView(occurrenceFilterProviderCountsView);
		    	mav.addObject(countsModelKey, counts);
		    	mav.addObject(resultsModelKey, counts);
		    	//add filters
		    	mav.addObject(filtersRequestKey, occurrenceFilters.getFilters());  	
		    	mav.addObject(criteriaRequestKey, criteriaDTO);
		    	mav.addObject(countsAvailableModelKey, true);
		    	return mav;
			}
		}		
		int totalNoOfDataProviders = dataResourceManager.getTotalDataProviderCount();
		return getCountsView(request, response, "SERVICE.OCCURRENCE.QUERY.RETURNFIELDS.PROVIDERCOUNTS", occurrenceFilterProviderCountsView, new SearchConstraints(0, totalNoOfDataProviders));		
	}

	/**
	 * Retrieves the counts against the providers for this set of criteria
	 *
	 * @param request
	 * @param response
	 * @return ModelAndView which contains the provider list and counts
	 */
	public ModelAndView searchResources (HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		//interrogate the criteria - if it only contains a country filter then switch
		CriteriaDTO criteriaDTO = CriteriaUtil.getCriteria(request, occurrenceFilters.getFilters());
		if(criteriaDTO.size()==0){
			logger.debug("Switching to using service layer method getAllDataResources");
			ModelAndView mav = new ModelAndView(occurrenceFilterResourceCountsView);
			List<DataResourceDTO> drs = dataResourceManager.getAllDataResources();
	    	mav.addObject(countsModelKey, drs);
	    	mav.addObject(resultsModelKey, drs);			
	    	//add filters
	    	mav.addObject(filtersRequestKey, occurrenceFilters.getFilters());  	
	    	mav.addObject(criteriaRequestKey, criteriaDTO);
	    	mav.addObject(countsAvailableModelKey, true);
	    	return mav;
		}		
		if(criteriaDTO.size()==1){
			logger.debug("Switching to using service layer method getDataResourceCountsForCountry");			
			CriterionDTO criterionDTO = criteriaDTO.get(0);
			FilterDTO filterDTO = FilterUtils.getFilterById(occurrenceFilters.getFilters(), criterionDTO.getSubject());	
			if(filterDTO.getSubject().equals(countryFilter.getSubject())){
				List<CountDTO> counts = geospatialManager.getDataResourceCountsForCountry(criterionDTO.getValue(), false);
		    	ModelAndView mav = new ModelAndView(occurrenceFilterResourceCountsView);
		    	mav.addObject(countsModelKey, counts);
		    	mav.addObject(resultsModelKey, counts);
		    	//add filters
		    	mav.addObject(filtersRequestKey, occurrenceFilters.getFilters());  	
		    	mav.addObject(criteriaRequestKey, criteriaDTO);
		    	mav.addObject(countsAvailableModelKey, true);
		    	return mav;
			}
		}
		int totalNoOfDataResource = dataResourceManager.getTotalDataResourceCount();	
		return getCountsView(request, response, "SERVICE.OCCURRENCE.QUERY.RETURNFIELDS.RESOURCECOUNTS", occurrenceFilterResourceCountsView, new SearchConstraints(0,totalNoOfDataResource));
	}	
	
	/**
	 * Retrieves the counts against the countries for this set of criteria

	 * @param request
	 * @param response
	 * @return ModelAndView which contains the provider list and counts
	 */
	public ModelAndView searchCountries (HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		//interrogate the criteria - if it only contains a taxon filter then switch
		CriteriaDTO criteriaDTO = CriteriaUtil.getCriteria(request, occurrenceFilters.getFilters());
		if(criteriaDTO.size()==1){
			logger.debug("Switching to using service layer method getCountryCountsForTaxonConcept");			
			CriterionDTO criterionDTO = criteriaDTO.get(0);
			FilterDTO filterDTO = FilterUtils.getFilterById(occurrenceFilters.getFilters(), criterionDTO.getSubject());	
			if(filterDTO.getSubject().equals(classificationFilter.getSubject())){
				List<CountDTO> counts = taxonomyManager.getCountryCountsForTaxonConcept(criterionDTO.getValue(), RequestContextUtils.getLocale(request));
	    	ModelAndView mav = new ModelAndView(occurrenceFilterCountryCountsView);
	    	mav.addObject(countsModelKey, counts);
	    	mav.addObject(resultsModelKey, counts);
	    	//add filters
	    	mav.addObject(filtersRequestKey, occurrenceFilters.getFilters());  	
	    	mav.addObject(criteriaRequestKey, criteriaDTO);
	    	mav.addObject(countsAvailableModelKey, true);
	    	return mav;
			}
		}		
		int totalNoOfCountries = geospatialManager.getTotalCountryCount();		
		return getCountryCountsView(request, response, "SERVICE.OCCURRENCE.QUERY.RETURNFIELDS.COUNTRYCOUNTS", occurrenceFilterCountryCountsView, new SearchConstraints(0, totalNoOfCountries));
	}	

	/**
	 * Retrieves the counts against the species for this set of criteria

	 * @param request
	 * @param response
	 * @return ModelAndView which contains the provider list and counts
	 */
	public ModelAndView downloadSpecies (HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		
		ModelAndView mav =  getCountsView(request, response, "SERVICE.OCCURRENCE.QUERY.RETURNFIELDS.SPECIESCOUNTS", occurrenceFilterSpeciesCountsView, new SearchConstraints(0, speciesCountLimit));
		mav.addObject(resultsLimitModelKey, speciesCountLimit);
		return mav;
	}	
	
	/**
	 * Performs the count triplet query and returns the generic count view for occurrences.
	 * 
	 * @param triplets
	 * @return
	 */
	private ModelAndView getCountsView(HttpServletRequest request, HttpServletResponse response, String returnFieldsKey, String viewName, SearchConstraints searchConstraints) throws ServiceException{
		CriteriaDTO criteriaDTO = CriteriaUtil.getCriteria(request,occurrenceFilters.getFilters());
		List<PropertyStoreTripletDTO> triplets = queryHelper.getTriplets(occurrenceFilters.getFilters(), criteriaDTO, request, response);		
		triplets.add(new PropertyStoreTripletDTO(queryHelper.getQueryNamespace(), selectFieldSubject, returnPredicateSubject, returnFieldsKey));
		SearchResultsDTO searchResults = countsQueryManager.doTripletQuery(triplets, criteriaDTO.isMatchAll(), searchConstraints);
    	ModelAndView mav = new ModelAndView(viewName);
    	mav.addObject(countsModelKey, searchResults);
    	mav.addObject(resultsModelKey, searchResults.getResults());
    	//add filters
    	mav.addObject(filtersRequestKey, occurrenceFilters.getFilters());  	
    	mav.addObject(criteriaRequestKey, criteriaDTO);
    	return mav;
	}	
	
	/**
	 * Performs the count triplet query and returns the generic count view for occurrences.
	 * 
	 * @param triplets
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ModelAndView getCountryCountsView(HttpServletRequest request, HttpServletResponse response, String returnFieldsKey, String viewName, SearchConstraints searchConstraints) throws ServiceException{
		CriteriaDTO criteriaDTO = CriteriaUtil.getCriteria(request,occurrenceFilters.getFilters());
		List<PropertyStoreTripletDTO> triplets = queryHelper.getTriplets(occurrenceFilters.getFilters(), criteriaDTO, request, response);		
		triplets.add(new PropertyStoreTripletDTO(queryHelper.getQueryNamespace(), selectFieldSubject, returnPredicateSubject, returnFieldsKey));
		SearchResultsDTO searchResults = countsQueryManager.doTripletQuery(triplets, criteriaDTO.isMatchAll(), searchConstraints);
		
		List<CountDTO> countDTOs = (List<CountDTO>)searchResults.getResults();
		final Locale locale = RequestContextUtils.getLocale(request);

		Collections.sort(countDTOs, new Comparator() {
			
			public int compare(Object count1, Object count2) {
				CountDTO countDTO1 = (CountDTO)count1;
				CountDTO countDTO2 = (CountDTO)count2;
				
				CountryDTO countryDTO1 = geospatialManager.getCountryForIsoCountryCode(countDTO1.getKey(), locale);
				CountryDTO countryDTO2 = geospatialManager.getCountryForIsoCountryCode(countDTO2.getKey(), locale);
				
				if(countryDTO1 == null)
					return 1;
				if(countryDTO2 == null)
					return -1;				
				
				return countryDTO1.getName().compareToIgnoreCase(countryDTO2.getName());
			}
		});
		
    	ModelAndView mav = new ModelAndView(viewName);
    	mav.addObject(countsModelKey, searchResults);
    	mav.addObject(resultsModelKey, countDTOs);
    	//add filters
    	mav.addObject(filtersRequestKey, occurrenceFilters.getFilters());  	
    	mav.addObject(criteriaRequestKey, criteriaDTO);
    	return mav;
	}		

	/**
	 * Redirect to filter view with a bounding box set along with the supplied criteria.
	 * 
	 * @param request
	 * @param response
	 * @param criteria
	 * @return ModelAndView, filter view
	 */
	public ModelAndView boundingBoxWithCriteria(HttpServletRequest request, HttpServletResponse response){
		CriteriaDTO criteria = CriteriaUtil.getCriteria(request, occurrenceFilters.getFilters());
		float minx = ServletRequestUtils.getFloatParameter(request, minXRequestKey, 0);
		float miny = ServletRequestUtils.getFloatParameter(request, minYRequestKey,0);
		float maxx =ServletRequestUtils.getFloatParameter(request, maxXRequestKey,0);
		float maxy = ServletRequestUtils.getFloatParameter(request, maxYRequestKey,0);
		criteria.getCriteria().add(new CriterionDTO(boundingBoxFilter.getId(), boundingBoxFilter.getDefaultPredicateId(), BoundingBoxFilterHelper.getBoundingBoxQueryString(minx, miny, maxx, maxy)));
		//this should remove old bounding boxes if they exist
		CriteriaUtil.checkFilterConstraints(occurrenceFilters.getFilters(), criteria);
		return new ModelAndView(new RedirectView("searchWithTable.htm?"+CriteriaUtil.getUrl(criteria)));
	}	

	/**
	 * @param countsQueryManager the countsQueryManager to set
	 */
	public void setCountsQueryManager(TripletQueryManager countsQueryManager) {
		this.countsQueryManager = countsQueryManager;
	}

	/**
	 * @param namespace the namespace to set
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * @param queryHelper the queryHelper to set
	 */
	public void setQueryHelper(QueryHelper queryHelper) {
		this.queryHelper = queryHelper;
	}

	/**
	 * @param webappPropertyStore the webappPropertyStore to set
	 */
	public void setWebappPropertyStore(PropertyStore webappPropertyStore) {
		this.webappPropertyStore = webappPropertyStore;
	}

	/**
	 * @param countryFilter the countryFilter to set
	 */
	public void setCountryFilter(FilterDTO countryFilter) {
		this.countryFilter = countryFilter;
	}

	/**
	 * @param dataResourceFilter the dataResourceFilter to set
	 */
	public void setDataResourceFilter(FilterDTO dataResourceFilter) {
		this.dataResourceFilter = dataResourceFilter;
	}

	/**
	 * @param filterMapLayerRoot the filterMapLayerRoot to set
	 */
	public void setFilterMapLayerRoot(String filterMapLayerRoot) {
		this.filterMapLayerRoot = filterMapLayerRoot;
	}

	/**
	 * @param maxCount the maxCount to set
	 */
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	/**
	 * @param pointsTotalRequestKey the pointsTotalRequestKey to set
	 */
	public void setPointsTotalRequestKey(String pointsTotalRequestKey) {
		this.pointsTotalRequestKey = pointsTotalRequestKey;
	}

	/**
	 * @param resultsModelKey the resultsModelKey to set
	 */
	public void setResultsModelKey(String resultsModelKey) {
		this.resultsModelKey = resultsModelKey;
	}

	/**
	 * @param returnPredicateSubject the returnPredicateSubject to set
	 */
	public void setReturnPredicateSubject(String returnPredicateSubject) {
		this.returnPredicateSubject = returnPredicateSubject;
	}

	/**
	 * @param scientificNameFilter the scientificNameFilter to set
	 */
	public void setScientificNameFilter(FilterDTO scientificNameFilter) {
		this.scientificNameFilter = scientificNameFilter;
	}

	/**
	 * @param selectFieldSubject the selectFieldSubject to set
	 */
	public void setSelectFieldSubject(String selectFieldSubject) {
		this.selectFieldSubject = selectFieldSubject;
	}

	/**
	 * @param defaultView the defaultView to set
	 */
	public void setDefaultView(String defaultView) {
		this.defaultView = defaultView;
	}

	/**
	 * @param filterContentProvider the filterContentProvider to set
	 */
	public void setFilterContentProvider(FilterContentProvider filterContentProvider) {
		this.filterContentProvider = filterContentProvider;
	}

	/**
	 * @param mapContentProvider the mapContentProvider to set
	 */
	public void setMapContentProvider(MapContentProvider mapContentProvider) {
		this.mapContentProvider = mapContentProvider;
	}

	/**
	 * @param criteriaRequestKey the criteriaRequestKey to set
	 */
	public void setCriteriaRequestKey(String criteriaRequestKey) {
		this.criteriaRequestKey = criteriaRequestKey;
	}

	/**
	 * @param filtersRequestKey the filtersRequestKey to set
	 */
	public void setFiltersRequestKey(String filtersRequestKey) {
		this.filtersRequestKey = filtersRequestKey;
	}

	/**
	 * @param tripletQueryProvider the tripletQueryProvider to set
	 */
	public void setPagingTripletQueryProvider(PagingTripletQueryProvider pagingTripletQueryProvider) {
		this.pagingTripletQueryProvider = pagingTripletQueryProvider;
	}

	/**
	 * @param defaultMapView the defaultMapView to set
	 */
	public void setDefaultMapView(String defaultMapView) {
		this.defaultMapView = defaultMapView;
	}

	/**
	 * @param mapLayerQueryManager the mapLayerQueryManager to set
	 */
	public void setMapLayerQueryManager(TripletQueryManager mapLayerQueryManager) {
		this.mapLayerQueryManager = mapLayerQueryManager;
	}

	/**
	 * @param tripletQueryManager the tripletQueryManager to set
	 */
	public void setTripletQueryManager(TripletQueryManager tripletQueryManager) {
		this.tripletQueryManager = tripletQueryManager;
	}
	
	/**
	 * @param dataResourceIdFilter the dataResourceIdFilter to set
	 */
	public void setDataResourceIdFilter(FilterDTO dataResourceIdFilter) {
		this.dataResourceIdFilter = dataResourceIdFilter;
	}

	/**
	 * @param occurrenceFilters the occurrenceFilters to set
	 */
	public void setOccurrenceFilters(FilterMapWrapper occurrenceFilters) {
		this.occurrenceFilters = occurrenceFilters;
	}

	/**
	 * @param dataProviderIdFilter the dataProviderIdFilter to set
	 */
	public void setDataProviderIdFilter(FilterDTO dataProviderIdFilter) {
		this.dataProviderIdFilter = dataProviderIdFilter;
	}

	/**
	 * @param countryParameterKey the countryParameterKey to set
	 */
	public void setCountryParameterKey(String countryParameterKey) {
		this.countryParameterKey = countryParameterKey;
	}

	/**
	 * @param currentCriterionModelKey the currentCriterionModelKey to set
	 */
	public void setCurrentCriterionModelKey(String currentCriterionModelKey) {
		this.currentCriterionModelKey = currentCriterionModelKey;
	}

	/**
	 * @param dataProviderParameterKey the dataProviderParameterKey to set
	 */
	public void setDataProviderParameterKey(String dataProviderParameterKey) {
		this.dataProviderParameterKey = dataProviderParameterKey;
	}

	/**
	 * @param dataResourceParameterKey the dataResourceParameterKey to set
	 */
	public void setDataResourceParameterKey(String dataResourceParameterKey) {
		this.dataResourceParameterKey = dataResourceParameterKey;
	}

	/**
	 * @param geospatialManager the geospatialManager to set
	 */
	public void setGeospatialManager(GeospatialManager geospatialManager) {
		this.geospatialManager = geospatialManager;
	}

	/**
	 * @param catchAllEntityKey the catchAllEntityKey to set
	 */
	public void setCatchAllEntityKey(String catchAllEntityKey) {
		this.catchAllEntityKey = catchAllEntityKey;
	}

	/**
	 * @param countsAvailableModelKey the countsAvailableModelKey to set
	 */
	public void setCountsAvailableModelKey(String countsAvailableModelKey) {
		this.countsAvailableModelKey = countsAvailableModelKey;
	}

	/**
	 * @param countsModelKey the countsModelKey to set
	 */
	public void setCountsModelKey(String countsModelKey) {
		this.countsModelKey = countsModelKey;
	}

	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}

	/**
	 * @param occurrenceFilterCountryCountsView the occurrenceFilterCountryCountsView to set
	 */
	public void setOccurrenceFilterCountryCountsView(
			String occurrenceFilterCountryCountsView) {
		this.occurrenceFilterCountryCountsView = occurrenceFilterCountryCountsView;
	}

	/**
	 * @param occurrenceFilterResourceCountsView the occurrenceFilterResourceCountsView to set
	 */
	public void setOccurrenceFilterResourceCountsView(
			String occurrenceFilterResourceCountsView) {
		this.occurrenceFilterResourceCountsView = occurrenceFilterResourceCountsView;
	}

	/**
	 * @param occurrenceFilterSpeciesCountsView the occurrenceFilterSpeciesCountsView to set
	 */
	public void setOccurrenceFilterSpeciesCountsView(
			String occurrenceFilterSpeciesCountsView) {
		this.occurrenceFilterSpeciesCountsView = occurrenceFilterSpeciesCountsView;
	}

	/**
	 * @param maxXRequestKey the maxXRequestKey to set
	 */
	public void setMaxXRequestKey(String maxXRequestKey) {
		this.maxXRequestKey = maxXRequestKey;
	}

	/**
	 * @param maxYRequestKey the maxYRequestKey to set
	 */
	public void setMaxYRequestKey(String maxYRequestKey) {
		this.maxYRequestKey = maxYRequestKey;
	}

	/**
	 * @param minXRequestKey the minXRequestKey to set
	 */
	public void setMinXRequestKey(String minXRequestKey) {
		this.minXRequestKey = minXRequestKey;
	}

	/**
	 * @param minYRequestKey the minYRequestKey to set
	 */
	public void setMinYRequestKey(String minYRequestKey) {
		this.minYRequestKey = minYRequestKey;
	}

	/**
	 * @param occurrenceFilterProviderCountsView the occurrenceFilterProviderCountsView to set
	 */
	public void setOccurrenceFilterProviderCountsView(
			String occurrenceFilterProviderCountsView) {
		this.occurrenceFilterProviderCountsView = occurrenceFilterProviderCountsView;
	}

	/**
	 * @param resultsLimitModelKey the resultsLimitModelKey to set
	 */
	public void setResultsLimitModelKey(String resultsLimitModelKey) {
		this.resultsLimitModelKey = resultsLimitModelKey;
	}

	/**
	 * @param speciesCountLimit the speciesCountLimit to set
	 */
	public void setSpeciesCountLimit(int speciesCountLimit) {
		this.speciesCountLimit = speciesCountLimit;
	}

	/**
	 * @param datasetFields the datasetFields to set
	 */
	public void setDatasetDownloadFields(List<Field> datasetDownloadFields) {
		this.datasetDownloadFields = datasetDownloadFields;
	}

	/**
	 * @param geospatialFields the geospatialFields to set
	 */
	public void setGeospatialDownloadFields(List<Field> geospatialFields) {
		this.geospatialDownloadFields = geospatialFields;
	}

	/**
	 * @param taxonomyFields the taxonomyFields to set
	 */
	public void setTaxonomyDownloadFields(List<Field> taxonomyDownloadFields) {
		this.taxonomyDownloadFields = taxonomyDownloadFields;
	}

	/**
	 * @param mandatoryDownloadFields the mandatoryDownloadFields to set
	 */
	public void setMandatoryDownloadFields(List<Field> mandatoryDownloadFields) {
		this.mandatoryDownloadFields = mandatoryDownloadFields;
	}

	/**
	 * @param downloadRedirectPath the downloadRedirectPath to set
	 */
	public void setDownloadRedirectPath(String downloadRedirectPath) {
		this.downloadRedirectPath = downloadRedirectPath;
	}

	/**
	 * @param formatRequestParam the formatRequestParam to set
	 */
	public void setFormatRequestParam(String formatRequestParam) {
		this.formatRequestParam = formatRequestParam;
	}

	/**
	 * @param downloadSpreadsheetView the downloadSpreadsheetView to set
	 */
	public void setDownloadSpreadsheetView(String downloadSpreadsheetView) {
		this.downloadSpreadsheetView = downloadSpreadsheetView;
	}

	/**
	 * @param downloadXMLView the downloadXMLView to set
	 */
	public void setDownloadXMLView(String downloadXMLView) {
		this.downloadXMLView = downloadXMLView;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @param messageSourceKey the messageSourceKey to set
	 */
	public void setMessageSourceKey(String messageSourceKey) {
		this.messageSourceKey = messageSourceKey;
	}

	/**
	 * @param rootWizardUrl the rootWizardUrl to set
	 */
	public void setRootWizardUrl(String rootWizardUrl) {
		this.rootWizardUrl = rootWizardUrl;
	}

	/**
	 * @param regionFilter the regionFilter to set
	 */
	public void setRegionFilter(FilterDTO regionFilter) {
		this.regionFilter = regionFilter;
	}

	/**
	 * @param rootWizardUrlModelKey the rootWizardUrlModelKey to set
	 */
	public void setRootWizardUrlModelKey(String rootWizardUrlModelKey) {
		this.rootWizardUrlModelKey = rootWizardUrlModelKey;
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
	 * @param taxonomyManager the taxonomyManager to set
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
	}

	/**
	 * @param coordinateIssuesFilter the coordinateIssuesFilter to set
	 */
	public void setGeoConsistencyFilter(FilterDTO coordinateIssuesFilter) {
		this.geoConsistencyFilter = coordinateIssuesFilter;
	}

	/**
	 * @param coordinateStatusFilter the coordinateStatusFilter to set
	 */
	public void setGeoreferencedFilter(FilterDTO coordinateStatusFilter) {
		this.georeferencedFilter = coordinateStatusFilter;
	}

	/**
	 * @param maxSampleResults the maxSampleResults to set
	 */
	public void setMaxSampleResults(int maxSampleResults) {
		this.maxSampleResults = maxSampleResults;
	}

	/**
	 * @param geoFormats the geoFormats to set
	 */
	public void setGeoFormats(List<String> geoFormats) {
		this.geoFormats = geoFormats;
	}	
}