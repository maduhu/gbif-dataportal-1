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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.web.content.filter.FilterHelper;
import org.gbif.portal.web.filter.CriterionDTO;
import org.gbif.portal.web.util.QueryHelper;
import org.springframework.web.servlet.ModelAndView;

/**
 * Common name filter helper.  
 * 
 * @author dmartin
 */
public class CommonNameFilterHelper implements FilterHelper {

	protected static Log logger = LogFactory.getLog(CommonNameFilterHelper.class);
	/** The common name triplet subject */
	protected String subject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.COMMONNAME";
	/** Query helper */ 
	protected QueryHelper queryHelper;
	/** Taxonomy manager for concept lookup */
	protected TaxonomyManager taxonomyManager;
	/** Equals triplet predicate */
	protected String equalsPredicate="SERVICE.QUERY.PREDICATE.EQUAL";
	/** Max number of concepts to match. Theortical limit, there should only be a max of 4 matches as we arent doing wildcard searches. */
	protected int maxConceptsToMatch = 100;
	/** Map rank to triplet subject */
	protected Map<String, String> rankToTripletSubjectMappings;
	
	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#addCriterion2Request(org.gbif.portal.web.filter.CriterionDTO, org.springframework.web.servlet.ModelAndView, javax.servlet.http.HttpServletRequest)
	 */
	public void addCriterion2Request(CriterionDTO criterionDTO, ModelAndView mav, HttpServletRequest request) {}
	
	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#getDisplayValue(java.lang.String, java.util.Locale)
	 */
	public String getDisplayValue(String value, Locale locale) {
		return value;
	}
	
	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#preProcess(java.util.List, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void preProcess(List<PropertyStoreTripletDTO> triplets, HttpServletRequest request, HttpServletResponse response) {
		
		List<PropertyStoreTripletDTO> commonNameTriplets = new ArrayList<PropertyStoreTripletDTO>();
		for(PropertyStoreTripletDTO triplet: triplets){
			if(triplet.getSubject().equals(subject)){
				commonNameTriplets.add(triplet);
			}
		}		
		
		for (PropertyStoreTripletDTO commonNameTriplet: commonNameTriplets){

			//remove this triplet
			triplets.remove(commonNameTriplet);
			
			try {
				//find matching taxons
				SearchResultsDTO results = taxonomyManager.findTaxonConceptsForCommonName((String)commonNameTriplet.getObject(), false, new SearchConstraints(0,maxConceptsToMatch));
				//need to map results to subject
				List<BriefTaxonConceptDTO> concepts = results.getResults();
				
				for (BriefTaxonConceptDTO tcDTO: concepts){
					//get the rank and find the appropriate subject
					String rank = tcDTO.getRank();
					
					//note we only support mapping of major ranks
					String tripletSubject = rankToTripletSubjectMappings.get(rank);
					if(tripletSubject!=null){
						PropertyStoreTripletDTO psDTO = new PropertyStoreTripletDTO(commonNameTriplet.getNamespace(), tripletSubject, equalsPredicate, new Long(tcDTO.getKey()));
						if(logger.isDebugEnabled()){
							logger.debug("Adding triplet: "+psDTO);
						}
						triplets.add(psDTO);
					}
				}
			} catch (ServiceException e) {
				logger.error(e.getMessage(), e);
			}
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
	 * @param queryHelper the queryHelper to set
	 */
	public void setQueryHelper(QueryHelper queryHelper) {
		this.queryHelper = queryHelper;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @param taxonomyManager the taxonomyManager to set
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
	}

	/**
	 * @param rankToTripletSubjectMappings the rankToTripletSubjectMappings to set
	 */
	public void setRankToTripletSubjectMappings(
			Map<String, String> rankToTripletSubjectMappings) {
		this.rankToTripletSubjectMappings = rankToTripletSubjectMappings;
	}
}