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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.geospatial.CellDensityDTO;
import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.dto.util.EntityType;
import org.gbif.portal.service.GeospatialManager;
import org.gbif.portal.service.triplet.TripletQueryManager;
import org.gbif.portal.web.content.taxonomy.ScientificNameFilterHelper;
import org.gbif.portal.web.controller.search.NameSearchController;
import org.gbif.portal.web.controller.search.bean.NameSearch;
import org.gbif.portal.web.util.TaxonConceptUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * A controller for providing AJAX API style access to occurrence point data.
 * 
 * @author dmartin
 */
public class OccurrenceNameSearchController extends NameSearchController {

	/** Points occurrence triplet manager */
	protected TripletQueryManager tripletOccurrenceTaxonPointsManager;

	/** Geospatial manager */
	protected GeospatialManager geospatialManager;

	protected TaxonConceptUtils taxonConceptUtils;
	
	protected ScientificNameFilterHelper scientificNameFilterHelper;	
	
	protected String spatialDistinctRequestKey = "spatialDistinct";
	protected String cellsRequestKey = "cells";
	
	protected boolean defaultSpatiallyDistinct = false;
	
	protected int maxConceptsToMatch = 100;
	
	protected String providerRequestKey = "provider";
	protected String resourceRequestKey = "resource";
	protected String networkRequestKey = "network";
	protected String rankRequestKey = "rank";
	
	protected String sciNameSubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.SCIENTIFICNAME";
	protected String equalsPredicate="SERVICE.QUERY.PREDICATE.EQUAL";
	
	/**
	 * @see org.gbif.portal.web.controller.search.NameSearchController#handleSearch(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.ModelAndView, org.gbif.portal.web.controller.search.bean.NameSearch)
	 */
	@Override
	public void handleSearch(Map<String, String> propertiesMap,
			HttpServletRequest request, HttpServletResponse response,
			ModelAndView mav, NameSearch nameSearch) throws Exception {

		boolean spatiallyDistinct = ServletRequestUtils.getBooleanParameter(request, spatialDistinctRequestKey, defaultSpatiallyDistinct);
	
		SearchResultsDTO searchResults = null;
		if(spatiallyDistinct){

			List<BriefTaxonConceptDTO> concepts = taxonConceptUtils.getTaxonConceptForSciName(nameSearch.getQuery(), "1", 
					null, false, 100, new ArrayList<String>(), new ArrayList<String>(), null);
			
			List<String> keys = new ArrayList<String>();
			for(BriefTaxonConceptDTO concept: concepts)
				keys.add(concept.getKey());
			
			boolean renderCells = ServletRequestUtils.getBooleanParameter(request, cellsRequestKey, false);
			List<CellDensityDTO> cellDensities = geospatialManager.get1DegCellDensities(EntityType.TYPE_TAXON, keys);
			mav.addObject("cells", cellDensities);
			mav.addObject("renderCells", renderCells);
		} else {
			PropertyStoreTripletDTO triplet = new PropertyStoreTripletDTO("http://gbif.org/portal-service/2006/1.0", sciNameSubject, equalsPredicate, nameSearch.getQuery());
			List<PropertyStoreTripletDTO> triplets = new ArrayList<PropertyStoreTripletDTO>();
			triplets.add(triplet);
			
			//add the correct triplets for this query
			scientificNameFilterHelper.preProcess(triplets, request, response);
			searchResults = tripletOccurrenceTaxonPointsManager.doTripletQuery(triplets, true, nameSearch.getSearchConstraints());
			mav.addObject("points", searchResults);
		}
	}

	/**
	 * @param spatialDistinctRequestKey the spatialDistinctRequestKey to set
	 */
	public void setSpatialDistinctRequestKey(String spatialDistinctRequestKey) {
		this.spatialDistinctRequestKey = spatialDistinctRequestKey;
	}

	/**
	 * @param defaultSpatiallyDistinct the defaultSpatiallyDistinct to set
	 */
	public void setDefaultSpatiallyDistinct(boolean defaultSpatiallyDistinct) {
		this.defaultSpatiallyDistinct = defaultSpatiallyDistinct;
	}

	/**
	 * @param providerRequestKey the providerRequestKey to set
	 */
	public void setProviderRequestKey(String providerRequestKey) {
		this.providerRequestKey = providerRequestKey;
	}

	/**
	 * @param resourceRequestKey the resourceRequestKey to set
	 */
	public void setResourceRequestKey(String resourceRequestKey) {
		this.resourceRequestKey = resourceRequestKey;
	}

	/**
	 * @param networkRequestKey the networkRequestKey to set
	 */
	public void setNetworkRequestKey(String networkRequestKey) {
		this.networkRequestKey = networkRequestKey;
	}

	/**
	 * @param maxConceptsToMatch the maxConceptsToMatch to set
	 */
	public void setMaxConceptsToMatch(int maxConceptsToMatch) {
		this.maxConceptsToMatch = maxConceptsToMatch;
	}

	/**
	 * @param scientificNameFilterHelper the scientificNameFilterHelper to set
	 */
	public void setScientificNameFilterHelper(
			ScientificNameFilterHelper scientificNameFilterHelper) {
		this.scientificNameFilterHelper = scientificNameFilterHelper;
	}

	/**
	 * @param rankRequestKey the rankRequestKey to set
	 */
	public void setRankRequestKey(String rankRequestKey) {
		this.rankRequestKey = rankRequestKey;
	}

	/**
	 * @param sciNameSubject the sciNameSubject to set
	 */
	public void setSciNameSubject(String sciNameSubject) {
		this.sciNameSubject = sciNameSubject;
	}

	/**
	 * @param equalsPredicate the equalsPredicate to set
	 */
	public void setEqualsPredicate(String equalsPredicate) {
		this.equalsPredicate = equalsPredicate;
	}

	/**
	 * @param tripletOccurrenceTaxonPointsManager the tripletOccurrenceTaxonPointsManager to set
	 */
	public void setTripletOccurrenceTaxonPointsManager(
			TripletQueryManager tripletOccurrenceTaxonPointsManager) {
		this.tripletOccurrenceTaxonPointsManager = tripletOccurrenceTaxonPointsManager;
	}

	/**
	 * @param geospatialManager the geospatialManager to set
	 */
	public void setGeospatialManager(GeospatialManager geospatialManager) {
		this.geospatialManager = geospatialManager;
	}

	/**
	 * @param taxonConceptUtils the taxonConceptUtils to set
	 */
	public void setTaxonConceptUtils(TaxonConceptUtils taxonConceptUtils) {
		this.taxonConceptUtils = taxonConceptUtils;
	}

	/**
	 * @param cellsRequestKey the cellsRequestKey to set
	 */
	public void setCellsRequestKey(String cellsRequestKey) {
		this.cellsRequestKey = cellsRequestKey;
	}
}