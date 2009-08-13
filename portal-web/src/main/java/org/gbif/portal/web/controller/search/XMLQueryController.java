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
package org.gbif.portal.web.controller.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.occurrence.OccurrenceRecordDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.dto.taxonomy.TaxonConceptDTO;
import org.gbif.portal.dto.util.BoundingBoxDTO;
import org.gbif.portal.dto.util.EntityType;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.dto.util.TaxonRankType;
import org.gbif.portal.dto.util.TimePeriodDTO;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.GeospatialManager;
import org.gbif.portal.service.OccurrenceManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.web.controller.search.bean.TaxonConceptStats;
import org.gbif.portal.web.util.QueryHelper;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * A controller to support legacy third party integration tools.
 * 
 * @author dmartin
 */
public class XMLQueryController extends MultiActionController {

	protected TaxonomyManager taxonomyManager;
	protected OccurrenceManager occurrenceManager;
	protected DataResourceManager dataResourceManager;
	protected GeospatialManager geospatialManager;
	protected QueryHelper queryHelper;
	
	/**
	 * Performs a taxon search.
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView query(HttpServletRequest request, HttpServletResponse response){
		
		String query = request.getParameter("query");
		String taxonConceptKey = null;
		String scientificName = null;
		boolean fuzzy = false;

		try {
			Integer taxonConceptId  = Integer.parseInt(query);
			taxonConceptKey = query;
		} catch (NumberFormatException e){
			//expected behaviour
			fuzzy = queryHelper.hasWildcards(query);
			scientificName = queryHelper.removeWildcards(query);
		}		
		
		int maxResults = ServletRequestUtils.getIntParameter(request,"maxResults", 10);
		int startIndex = ServletRequestUtils.getIntParameter(request,"startIndex", 0);
		
		SearchConstraints sc = new SearchConstraints(startIndex,maxResults);
		try {
			
			List modelObjects = null;
			
			if(taxonConceptKey==null){
				DataProviderDTO dataProvider = dataResourceManager.getNubDataProvider();
				SearchResultsDTO searchResults = taxonomyManager.findTaxonConcepts(scientificName, fuzzy, null, dataProvider.getKey(), null, null, null, null, null, false, false, sc);
				modelObjects = searchResults.getResults();
			} else {
				TaxonConceptDTO taxonConcept = taxonomyManager.getTaxonConceptFor(taxonConceptKey);
				modelObjects = new ArrayList<TaxonConceptDTO>();
				if(taxonConcept!=null){
					modelObjects.add(taxonConcept);
				}
			}
			
			ModelAndView mav = new ModelAndView("xmlQuery");
			
			List<TaxonConceptStats> stats = new ArrayList<TaxonConceptStats>();
			for(Object modelObject : modelObjects){
				BriefTaxonConceptDTO btcDTO = (BriefTaxonConceptDTO) modelObject;
				Integer occurrenceCount = null;
				if(TaxonRankType.isGenusOrLowerRank(btcDTO.getRank())){
					occurrenceCount = occurrenceManager.countOccurrenceRecords(null, null, null, btcDTO.getKey(), null, null, null, null, null, (BoundingBoxDTO) null, (TimePeriodDTO) null, (Date) null, false);
				}
				int occurrenceGeoereferencedCount = geospatialManager.countGeoreferencedPointsFor(EntityType.TYPE_TAXON, btcDTO.getKey());
				TaxonConceptStats tcStats = new TaxonConceptStats(btcDTO.getKey(), occurrenceCount, occurrenceGeoereferencedCount); 
				stats.add(tcStats);
			}
			
			mav.addObject("searchResults", modelObjects);
			mav.addObject("stats", stats);
			return mav;
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * Performs a occurrence search.
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView queryGIS(HttpServletRequest request, HttpServletResponse response){
		
		String query = request.getParameter("query");
		String taxonConceptKey = null;
		String scientificName = null;

		try {
			Integer taxonConceptId   = Integer.parseInt(query);
			taxonConceptKey = query;
		} catch (NumberFormatException e){
			//expected behaviour
			scientificName = queryHelper.removeWildcards(query);
		}
		
		SearchResultsDTO searchResults = null;
		try {
			searchResults = occurrenceManager.findOccurrenceRecords(null, null, null, taxonConceptKey, scientificName, null, null, null, null, null, null, null, true, new SearchConstraints(0,10000));
			List<OccurrenceRecordDTO> occurrenceRecords = searchResults.getResults();
			Collections.sort(occurrenceRecords, new Comparator<OccurrenceRecordDTO>(){
				public int compare(OccurrenceRecordDTO or1, OccurrenceRecordDTO or2) {
					return or1.getNubTaxonConceptKey().compareTo(or2.getNubTaxonConceptKey());
				}
			});
			
			List<TaxonConceptDTO> concepts = new ArrayList<TaxonConceptDTO>();
			List<Integer> recordCounts = new ArrayList<Integer>(); 
			String currTaxonConceptKey = "";
			int currentCount = 0;
			for(OccurrenceRecordDTO orDTO: occurrenceRecords){
				if(!currTaxonConceptKey.equals(orDTO.getNubTaxonConceptKey())){
					TaxonConceptDTO taxonConcept = taxonomyManager.getTaxonConceptFor(orDTO.getNubTaxonConceptKey());
					concepts.add(taxonConcept);
					currTaxonConceptKey = taxonConcept.getKey();
					recordCounts.add(new Integer(currentCount));
					currentCount=0;
				}
				currentCount++;
			}
			recordCounts.add(new Integer(currentCount));
			
			ModelAndView mav = new ModelAndView("xmlQueryGIS");
			mav.addObject("searchResults", searchResults);
			mav.addObject("concepts", concepts);
			mav.addObject("recordCounts", recordCounts);
			return mav;			
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * @param taxonomyManager the taxonomyManager to set
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
	}

	/**
	 * @param occurrenceManager the occurrenceManager to set
	 */
	public void setOccurrenceManager(OccurrenceManager occurrenceManager) {
		this.occurrenceManager = occurrenceManager;
	}

	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}

	/**
	 * @param geospatialManager the geospatialManager to set
	 */
	public void setGeospatialManager(GeospatialManager geospatialManager) {
		this.geospatialManager = geospatialManager;
	}

	/**
	 * @param queryHelper the queryHelper to set
	 */
	public void setQueryHelper(QueryHelper queryHelper) {
		this.queryHelper = queryHelper;
	}
}