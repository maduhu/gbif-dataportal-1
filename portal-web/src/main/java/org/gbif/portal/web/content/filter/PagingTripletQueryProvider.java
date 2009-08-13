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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.service.triplet.TripletQueryManager;
import org.gbif.portal.util.propertystore.PropertyStore;
import org.gbif.portal.web.content.ContentView;
import org.gbif.portal.web.content.PaginatedResultSet;
import org.gbif.portal.web.content.PagingQueryContentProvider;
import org.gbif.portal.web.filter.CriteriaDTO;
import org.gbif.portal.web.filter.CriteriaUtil;
import org.gbif.portal.web.filter.FilterDTO;
import org.gbif.portal.web.filter.FilterMapWrapper;
import org.gbif.portal.web.util.QueryHelper;

/**
 * Content Provider for Filter Search Views. This provider takes criteria from the
 * specified request and converts to triplets. It then uses the triplet query manager
 * to execute the supplied query.
 * 
 * This implementation uses the PaginatedResultSet for paging functionality.
 * 
 * @author dmartin
 * 
 * @see PaginatedResultSet
 * @see TripletQueryManager
 */
public class PagingTripletQueryProvider extends PagingQueryContentProvider {

	protected static Log log = LogFactory.getLog(PagingTripletQueryProvider.class);

	/** For query utilities */
	protected QueryHelper queryHelper;
	
	/** The triplet query namespace */
	protected String namespace; 	
	/** PropertyStore that provides the filters to use */
	protected PropertyStore webappPropertyStore; 	
	/** The set of filters used to reconcile the criteria into triplets */
	protected String filterSet;
	/** The Service Manager providing the query functionality */
	protected TripletQueryManager tripletQueryManager;

	/** Session key use to store the triplets for the query */
	protected String tripletsSessionKey = "triplets";
	
	/** Model key use to store the filters */
	protected String filtersModelKey = "filters";
	/** Model key use to store the criteria */
	protected String criteriaModelKey = "criteria";
	
	/**
	 * @see org.gbif.portal.web.content.ContentProvider#addContent(org.gbif.portal.web.content.ContentView, java.lang.Object)
	 */
	public void addContent(ContentView cc, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CriteriaDTO criteriaDTO = CriteriaUtil.getCriteria(request, getFilters());	
		//has criteria changed? if so search again
		List<PropertyStoreTripletDTO> triplets = queryHelper.getTriplets(getFilters(), criteriaDTO, request, response);
		TripletQuery query = createTripletQuery(triplets, criteriaDTO.isMatchAll());
		//do the query
		doQuery(query, request, response);
		//add extra params to model
    	cc.addObject(filtersModelKey, getFilters());
    	cc.addObject(criteriaModelKey, criteriaDTO);
	}

	/**
	 * Do triplet query. Convienience method for implementations that need to adjust the triplets before executing the query. 
	 * 
	 * @param triplets
	 * @param criteriaDTO
	 * @param request
	 * @param response
	 */
	public void doTripletQuery(List<PropertyStoreTripletDTO> triplets, boolean matchAll, HttpServletRequest request, HttpServletResponse response) throws Exception {
		TripletQuery query = createTripletQuery(triplets, matchAll);
		//do the query
		doQuery(query, request, response);
	}
	
	/**
	 * Initialise a TripletQuery object.
	 * 
	 * @param triplets
	 * @param matchAll
	 * @return
	 */
	public TripletQuery createTripletQuery(List<PropertyStoreTripletDTO> triplets, boolean matchAll){
    	//create a query object for the Paginated List
    	TripletQuery tripletQuery = new TripletQuery();
    	SearchConstraints searchConstraints = new SearchConstraints();
    	tripletQuery.setSearchConstraints(searchConstraints);
    	tripletQuery.setTriplets(triplets);
    	tripletQuery.setTripletQueryManager(tripletQueryManager);
    	tripletQuery.setMatchAll(matchAll);
    	return tripletQuery;
	}
	
	
	private List<FilterDTO> getFilters(){
    	FilterMapWrapper filterMapWrapper = (FilterMapWrapper) webappPropertyStore.getProperty(namespace, filterSet); 
    	return filterMapWrapper.getFilters();
	}	
	
	/**
	 * @param filterSet the filterSet to set
	 */
	public void setFilterSet(String filterSet) {
		this.filterSet = filterSet;
	}

	/**
	 * @param namespace the namespace to set
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * @param tripletQueryManager the tripletQueryManager to set
	 */
	public void setTripletQueryManager(TripletQueryManager tripletQueryManager) {
		this.tripletQueryManager = tripletQueryManager;
	}

	/**
	 * @param webappPropertyStore the webappPropertyStore to set
	 */
	public void setWebappPropertyStore(PropertyStore webappPropertyStore) {
		this.webappPropertyStore = webappPropertyStore;
	}

	/**
	 * @param criteriaModelKey the criteriaModelKey to set
	 */
	public void setCriteriaModelKey(String criteriaModelKey) {
		this.criteriaModelKey = criteriaModelKey;
	}

	/**
	 * @param filtersModelKey the filtersModelKey to set
	 */
	public void setFiltersModelKey(String filtersModelKey) {
		this.filtersModelKey = filtersModelKey;
	}

	/**
	 * @param queryHelper the queryHelper to set
	 */
	public void setQueryHelper(QueryHelper queryHelper) {
		this.queryHelper = queryHelper;
	}

	/**
	 * @param tripletsSessionKey the tripletsSessionKey to set
	 */
	public void setTripletsSessionKey(String tripletsSessionKey) {
		this.tripletsSessionKey = tripletsSessionKey;
	}
}