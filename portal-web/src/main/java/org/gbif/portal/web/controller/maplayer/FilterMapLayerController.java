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

package org.gbif.portal.web.controller.maplayer;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.geospatial.CellDensityDTO;
import org.gbif.portal.dto.util.EntityType;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.triplet.TripletQueryManager;
import org.gbif.portal.util.propertystore.PropertyStore;
import org.gbif.portal.web.content.filter.FilterContentProvider;
import org.gbif.portal.web.filter.CriteriaDTO;
import org.gbif.portal.web.filter.CriteriaUtil;
import org.gbif.portal.web.filter.FilterDTO;
import org.gbif.portal.web.filter.FilterMapWrapper;
import org.gbif.portal.web.util.QueryHelper;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * A controller that will populate the model for the map layer requested for the given set of criteria
 * 
 * e.g.
 * <ul>
 * 		<li>/criteria[0].......</li>
 * 		<li>/criteria[0]......./tenDeg (defaults to lat=-90, long=-180)</li>
 * 		<li>/criteria[0]......./oneDeg/-90/-180</li>
 * </ul>
 * 
 * @author dmartin
 */
public class FilterMapLayerController extends AbstractMapLayerController {
	
	/**
	 * For the triplet queries
	 */
	protected QueryHelper queryHelper;
	protected String cellIdSubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.CELLID";
	protected String returnPredicateSubject = "SERVICE.QUERY.PREDICATE.RETURN";
	protected String selectFieldSubject = "SERVICE.QUERY.SUBJECT.SELECTFIELD";
	protected String occurrenceLatitudeSubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.LATITUDE";
	protected String occurrenceLongitudeSubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.LONGITUDE";
	protected String notEqualPredicate = "SERVICE.QUERY.PREDICATE.NEQUAL";
	protected String equalPredicate = "SERVICE.QUERY.PREDICATE.EQUAL";
	protected TripletQueryManager mapLayerQueryManager;
	protected FilterContentProvider filterContentProvider;
	protected String criteriaRequestKey="criteria";
	protected String filtersRequestKey="filters";
	protected PropertyStore webappPropertyStore; 
	protected String namespace;
	protected String occurrenceFilterSet = "occurrenceFilters";
	protected String queryRequestKey = "query";

	/**
	 * @see org.gbif.portal.web.controller.maplayer.AbstractMapLayerController#handleRequest(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ModelAndView handleRequest(Map<String, String> properties, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String query = properties.get(queryRequestKey);
		try {
			query = URLDecoder.decode(query, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.warn(e.getMessage(), e);
		}		
		
		CriteriaDTO criteria = CriteriaUtil.getCriteria(query, getFilters(), null);
		if(filterContentProvider.isQuerySuitableForCellDensity(criteria)){
			//redirect to entity map layer ...
			EntityType entityType = filterContentProvider.getCellDensityEntityType(criteria);
			if(logger.isDebugEnabled())
				logger.debug("Retrieved entity type: "+entityType);
			List<String> entityIds = filterContentProvider.getCellDensityEntityIds(criteria);
			StringBuffer sb = new StringBuffer();
			sb.append(request.getContextPath());
			sb.append(request.getServletPath());
			sb.append('/');
			sb.append(entityType.getName());
			String idsString = StringUtils.join(entityIds, ',');
			sb.append('/');
			if(entityType!=null && entityType.equals(EntityType.TYPE_ALL))
				sb.append("0");
			else
				sb.append(idsString);
			sb.append('/');
			String size = properties.get(sizeRequestKey);
			if(StringUtils.isNotEmpty(size)){
				String minLat = properties.get(minLatRequestKey);
				String minLong = properties.get(minLongRequestKey);
				sb.append(size);
				sb.append('/');				
				sb.append(minLat);
				sb.append('/');
				sb.append(minLong);
				sb.append('/');				
			}			
			if(logger.isDebugEnabled())
				logger.debug("Redirecting to:"+sb.toString());
			return new ModelAndView(new RedirectView(sb.toString()));
		}
		return super.handleRequest(properties, request, response);
	}

	/**
	 * Global view.
	 * 
	 * @param properties
	 * @param request
	 * @param response
	 * @return
	 */
	protected List<CellDensityDTO> get1DegCellDensities(Map<String, String> properties, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		// choose the correct return fields
		PropertyStoreTripletDTO returnTypeTriplet = new PropertyStoreTripletDTO(queryHelper.getQueryNamespace(), selectFieldSubject, returnPredicateSubject, "SERVICE.OCCURRENCE.QUERY.RETURNFIELDS.MAPLAYERCOUNTS");
		//do the query 
		SearchConstraints constraints = new SearchConstraints(0, 64800);
    	return doCellDensityTripletQuery(properties, request, response, returnTypeTriplet, new ArrayList<PropertyStoreTripletDTO>(), constraints);
	}	

	/**
	 * 10x20
	 * 
	 * @param properties
	 * @param cellIds
	 * @param request
	 * @param response
	 * @return
	 */
	protected List<CellDensityDTO> get1DegCellDensities(Map<String, String> properties, Set<Integer> cellIds, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		// choose the correct return fields
		PropertyStoreTripletDTO returnTypeTriplet = new PropertyStoreTripletDTO(queryHelper.getQueryNamespace(), selectFieldSubject, returnPredicateSubject, "SERVICE.OCCURRENCE.QUERY.RETURNFIELDS.MAPLAYERCOUNTS");
		List<PropertyStoreTripletDTO> extraTriplets = new ArrayList<PropertyStoreTripletDTO>();
		extraTriplets.add(new PropertyStoreTripletDTO(queryHelper.getQueryNamespace(), cellIdSubject, "SERVICE.QUERY.PREDICATE.IN", cellIds));
		//do the query 
		SearchConstraints constraints = new SearchConstraints(0, 200);
		//construct the modelAndView
    	return doCellDensityTripletQuery(properties, request, response, returnTypeTriplet, extraTriplets, constraints);
	}

	/**
	 * 1x2
	 * 
	 * @param properties
	 * @param request
	 * @param response
	 * @return
	 */
	protected List<CellDensityDTO> get0Point1DegCellDensities(Map<String, String> properties, int cellId, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		// choose the correct return fields
		PropertyStoreTripletDTO returnTypeTriplet = new PropertyStoreTripletDTO(queryHelper.getQueryNamespace(), selectFieldSubject, returnPredicateSubject, "SERVICE.OCCURRENCE.QUERY.RETURNFIELDS.MAPLAYERCENTICELLCOUNTS");
		List<PropertyStoreTripletDTO> extraTriplets = new ArrayList<PropertyStoreTripletDTO>();
		extraTriplets.add(new PropertyStoreTripletDTO(queryHelper.getQueryNamespace(), cellIdSubject, equalPredicate, cellId));
		//do the query 
		SearchConstraints constraints = new SearchConstraints(0, 200);
		//construct the modelAndView
    	return doCellDensityTripletQuery(properties, request, response, returnTypeTriplet, extraTriplets, constraints);
	}	

	/**
	 * Perform the Cell Density Triplet Query.
	 * 
	 * @param properties
	 * @param request
	 * @param response
	 * @param returnTypeTriplet
	 * @param extraTriplets
	 * @param searchConstraint
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CellDensityDTO> doCellDensityTripletQuery(Map<String, String> properties, HttpServletRequest request, HttpServletResponse response, PropertyStoreTripletDTO returnTypeTriplet, List<PropertyStoreTripletDTO> extraTriplets, SearchConstraints searchConstraints){
		String query = properties.get(queryRequestKey);
		try {
			query = URLDecoder.decode(query, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
		}
		CriteriaDTO criteria  = CriteriaUtil.getCriteria(query, getFilters(), null);
		// convert to triplets
		List<PropertyStoreTripletDTO> triplets = queryHelper.getTriplets(getFilters(), criteria, request, response);
		//	add extra triplets to say we require only geospatially aware records
		filterContentProvider.addGeoreferencedOnlyOccurrenceTriplets(triplets, queryHelper.getQueryNamespace());
		// choose the correct return fields
		triplets.add(returnTypeTriplet);
		triplets.addAll(extraTriplets);
		
		//do the query 
		SearchResultsDTO searchResults;
		try {
			searchResults = mapLayerQueryManager.doTripletQuery(triplets, criteria.isMatchAll(), searchConstraints);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			return new LinkedList<CellDensityDTO>();
		}
	
		//construct the modelAndView
    	return (List<CellDensityDTO>)searchResults.getResults();		
	}
	
	/**
	 * @return The filters
	 */
	private List<FilterDTO> getFilters(){
    	FilterMapWrapper filterMapWrapper = (FilterMapWrapper) webappPropertyStore.getProperty(namespace, occurrenceFilterSet); 
    	return filterMapWrapper.getFilters();
	}		
	
	/**
	 * @param queryHelper the queryHelper to set
	 */
	public void setQueryHelper(QueryHelper queryHelper) {
		this.queryHelper = queryHelper;
	}

	/**
	 * @param namespace the namespace to set
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * @param webappPropertyStore the webappPropertyStore to set
	 */
	public void setWebappPropertyStore(PropertyStore webappPropertyStore) {
		this.webappPropertyStore = webappPropertyStore;
	}

	/**
	 * @param mapLayerQueryManager the mapLayerQueryManager to set
	 */
	public void setMapLayerQueryManager(TripletQueryManager mapLayerQueryManager) {
		this.mapLayerQueryManager = mapLayerQueryManager;
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
	 * @param notEqualPredicate the notEqualPredicate to set
	 */
	public void setNotEqualPredicate(String notEqualPredicate) {
		this.notEqualPredicate = notEqualPredicate;
	}

	/**
	 * @param occurrenceFilterSet the occurrenceFilterSet to set
	 */
	public void setOccurrenceFilterSet(String occurrenceFilterSet) {
		this.occurrenceFilterSet = occurrenceFilterSet;
	}

	/**
	 * @param occurrenceLatitudeSubject the occurrenceLatitudeSubject to set
	 */
	public void setOccurrenceLatitudeSubject(String occurrenceLatitudeSubject) {
		this.occurrenceLatitudeSubject = occurrenceLatitudeSubject;
	}

	/**
	 * @param occurrenceLongitudeSubject the occurrenceLongitudeSubject to set
	 */
	public void setOccurrenceLongitudeSubject(String occurrenceLongitudeSubject) {
		this.occurrenceLongitudeSubject = occurrenceLongitudeSubject;
	}

	/**
	 * @param filterContentProvider the filterContentProvider to set
	 */
	public void setFilterContentProvider(FilterContentProvider filterContentProvider) {
		this.filterContentProvider = filterContentProvider;
	}
}