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
package org.gbif.portal.web.content.taxonomy;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.taxonomy.TaxonConceptDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.web.content.filter.FilterHelper;
import org.gbif.portal.web.filter.CriterionDTO;
import org.gbif.portal.web.util.QueryHelper;
import org.springframework.web.servlet.ModelAndView;

/**
 * Filter Helper used by Taxon Rank filters to switch from a text search to a 
 * search based on taxon concept id.
 * 
 * @author dmartin
 */
public class TaxonRankFilterHelper implements FilterHelper {
	
	protected static Log logger = LogFactory.getLog(TaxonRankFilterHelper.class);	
	
	protected TaxonomyManager taxonomyManager;
	protected DataResourceManager dataResourceManager;	
	protected QueryHelper queryHelper;	
	/** allow unconfirmed names ? */
	protected boolean allowUnconfirmedNames = true;
	protected String rank;	
	protected String rankTriplet;
	protected String rankIdTriplet;
	protected String inPredicate="SERVICE.QUERY.PREDICATE.IN";
	protected int maxTaxonConcepts = 10;
	
	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#addCriterion2Request(org.gbif.portal.web.filter.CriterionDTO, org.springframework.web.servlet.ModelAndView, javax.servlet.http.HttpServletRequest)
	 */
	public void addCriterion2Request(CriterionDTO criterionDTO,
			ModelAndView mav, HttpServletRequest request) {}

	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#getDisplayValue(java.lang.String)
	 */
	public String getDisplayValue(String value, Locale locale) {
		return value;
	}

	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#preProcess(java.util.List, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	public void preProcess(List<PropertyStoreTripletDTO> triplets,
			HttpServletRequest request, HttpServletResponse response) {
		
		try {
			DataProviderDTO dataProvider = dataResourceManager.getNubDataProvider();
			
			//retrieve the SERVICE.OCCURRENCE.QUERY.SUBJECT.TR.GENUS=
			for (PropertyStoreTripletDTO triplet: triplets){
				if(triplet.getSubject().equals(rankTriplet)){
					
					String tripletValue = (String) triplet.getObject();
					//do a search for nub taxon_concept with rank of genus and name like '*'
					SearchResultsDTO taxonConcepts = taxonomyManager.findTaxonConcepts(tripletValue, queryHelper.hasWildcards(tripletValue), rank, dataProvider.getKey(), null, null, null, null, null, allowUnconfirmedNames,false, new SearchConstraints(0, maxTaxonConcepts));
					List<TaxonConceptDTO> concepts = taxonConcepts.getResults();
					if(concepts!=null && concepts.size()>0){
						//change to where SERVICE.OCCURRENCE.QUERY.SUBJECT.TR.GENUSID=
						triplet.setSubject(rankIdTriplet);
						triplet.setPredicate(inPredicate);
						List<Long> taxonConceptIds = new ArrayList<Long>();
						//FIXME this is bad because this is web layer logic which knows about the ids used in the service layer
						//however this is essentially service layer logic - but unfortunately sits in the web layer at present.
						for(TaxonConceptDTO concept: concepts){
							taxonConceptIds.add(new Long(concept.getKey()));
						}
						triplet.setObject(taxonConceptIds);
					}
				}
			}
		} catch (ServiceException e){
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#getDefaultDisplayValue(javax.servlet.http.HttpServletRequest)
	 */
	public String getDefaultDisplayValue(HttpServletRequest request) {
		return null;
	}
	
	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#getDefaultValue(javax.servlet.http.HttpServletRequest)
	 */
	public String getDefaultValue(HttpServletRequest request) {
		return null;
	}	
	
	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}

	/**
	 * @param inPredicate the inPredicate to set
	 */
	public void setInPredicate(String inPredicate) {
		this.inPredicate = inPredicate;
	}

	/**
	 * @param maxTaxonConcepts the maxTaxonConcepts to set
	 */
	public void setMaxTaxonConcepts(int maxTaxonConcepts) {
		this.maxTaxonConcepts = maxTaxonConcepts;
	}

	/**
	 * @param queryHelper the queryHelper to set
	 */
	public void setQueryHelper(QueryHelper queryHelper) {
		this.queryHelper = queryHelper;
	}

	/**
	 * @param rank the rank to set
	 */
	public void setRank(String rank) {
		this.rank = rank;
	}

	/**
	 * @param rankIdTriplet the rankIdTriplet to set
	 */
	public void setRankIdTriplet(String rankIdTriplet) {
		this.rankIdTriplet = rankIdTriplet;
	}

	/**
	 * @param rankTriplet the rankTriplet to set
	 */
	public void setRankTriplet(String rankTriplet) {
		this.rankTriplet = rankTriplet;
	}

	/**
	 * @param taxonomyManager the taxonomyManager to set
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
	}

	/**
	 * @param allowUnconfirmedNames the allowUnconfirmedNames to set
	 */
	public void setAllowUnconfirmedNames(boolean allowUnconfirmedNames) {
		this.allowUnconfirmedNames = allowUnconfirmedNames;
	}

}