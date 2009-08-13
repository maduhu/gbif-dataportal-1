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
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.web.content.filter.FilterHelper;
import org.gbif.portal.web.filter.CriterionDTO;
import org.gbif.portal.web.filter.FilterUtils;
import org.gbif.portal.web.util.QueryHelper;
import org.gbif.portal.web.util.TaxonConceptUtils;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Scientific name filter helper. This filter helper takes a scientific name and tries to 
 * retrieve concepts of that name.
 * 
 * @author dmartin
 */
public class ScientificNameFilterHelper implements FilterHelper {

	protected static Log logger = LogFactory.getLog(ScientificNameFilterHelper.class);
	
	/** Taxonomy manager used for concept queries */
	protected TaxonomyManager taxonomyManager;
	/** Data resource manager */
	protected DataResourceManager dataResourceManager;
	/** Utilities for taxon concepts */
	protected TaxonConceptUtils taxonConceptUtils;
	/** allow unconfirmed names ? */
	protected boolean allowUnconfirmedNames = true;
	/** The query helper */
	protected QueryHelper queryHelper;
	/** The scientific name subject */
	protected String subject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.SCIENTIFICNAME";
	/** The equals predicate */
	protected String equalsPredicate="SERVICE.QUERY.PREDICATE.EQUAL";
	/** The maximum number of concepts to match */
	protected int maxConceptsToMatch = 100;
	/** Map of ranks to triplet subjects eg. kingdom -> SERVICE.OCCURRENCE.QUERY.SUBJECT.KINGDOMID */ 
	protected Map<String, String> rankToTripletSubjectMappings;
	/** Whether or not to add warnings */
	protected boolean addWarnings = true;
	/** message source */
	protected MessageSource messageSource;	
	/** Message key for the accepted concept warning */
	protected String acceptedConceptWarning ="warnings.filter.scientific.name.accepted";
	/** Message key for the unsupported rank warning */
	protected String unsupportedRankWarning ="warnings.filter.scientific.name.unsupportedrank";
	
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
		
		List<PropertyStoreTripletDTO> scientificNameTriplets = new ArrayList<PropertyStoreTripletDTO>();
		for(PropertyStoreTripletDTO triplet: triplets){
			if(triplet.getSubject().equals(subject)){
				scientificNameTriplets.add(triplet);
			}
		}		
		
		List<String> warnings = FilterUtils.getFilterWarnings(request);
		List<String> fatalWarnings = FilterUtils.getFatalFilterWarnings(request);
		
		try {
			//only search nub
			DataProviderDTO dp = dataResourceManager.getNubDataProvider();
			Locale locale = RequestContextUtils.getLocale(request);
			
			for (PropertyStoreTripletDTO scientificNameTriplet: scientificNameTriplets){
	
				String scientificName = (String) scientificNameTriplet.getObject();
				//if it doesnt have wildcards
				if(!queryHelper.hasWildcards(scientificName)){
					
					List<BriefTaxonConceptDTO> concepts = taxonConceptUtils.getTaxonConceptForSciName(scientificName, dp.getKey(), null, allowUnconfirmedNames, maxConceptsToMatch, warnings, fatalWarnings, locale);
					
					if(concepts!=null && !concepts.isEmpty()){
						
						//remove the sci name triplet, as we have matches
						triplets.remove(scientificNameTriplet);
	
						for(BriefTaxonConceptDTO concept: concepts){
							//note we only support mapping of major ranks
							String tripletSubject = rankToTripletSubjectMappings.get(concept.getRank());
							
							if(tripletSubject!=null){
								PropertyStoreTripletDTO psDTO = new PropertyStoreTripletDTO(scientificNameTriplet.getNamespace(), tripletSubject, equalsPredicate, new Long(concept.getKey()));
								if(logger.isDebugEnabled()){
									logger.debug("Adding triplet: "+psDTO);
								}
								triplets.add(psDTO);
								
								//if the taxon is not from nub provider, search for its speciesConceptKey also
								//(i.e.: search for Felis concolor returns records for Felis concolor and Puma Concolor
								if(concept.getSpeciesConceptKey()!=null && !concept.getKey().equals(concept.getSpeciesConceptKey())) {
									PropertyStoreTripletDTO psDTO2 = new PropertyStoreTripletDTO(scientificNameTriplet.getNamespace(), tripletSubject, equalsPredicate, new Long(concept.getSpeciesConceptKey()));
									triplets.add(psDTO2);
								}
							}
						}
					}
				}
			}
		
		} catch (ServiceException e) {
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
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
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

	/**
	 * @param acceptedConceptWarning the acceptedConceptWarning to set
	 */
	public void setAcceptedConceptWarning(String acceptedConceptWarning) {
		this.acceptedConceptWarning = acceptedConceptWarning;
	}

	/**
	 * @param addWarnings the addWarnings to set
	 */
	public void setAddWarnings(boolean addWarnings) {
		this.addWarnings = addWarnings;
	}

	/**
	 * @param equalsPredicate the equalsPredicate to set
	 */
	public void setEqualsPredicate(String equalsPredicate) {
		this.equalsPredicate = equalsPredicate;
	}

	/**
	 * @param maxConceptsToMatch the maxConceptsToMatch to set
	 */
	public void setMaxConceptsToMatch(int maxConceptsToMatch) {
		this.maxConceptsToMatch = maxConceptsToMatch;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @param unsupportedRankWarning the unsupportedRankWarning to set
	 */
	public void setUnsupportedRankWarning(String unsupportedRankWarning) {
		this.unsupportedRankWarning = unsupportedRankWarning;
	}

	/**
	 * @param taxonConceptUtils the taxonConceptUtils to set
	 */
	public void setTaxonConceptUtils(TaxonConceptUtils taxonConceptUtils) {
		this.taxonConceptUtils = taxonConceptUtils;
	}

	/**
	 * @param allowUnconfirmedNames the allowUnconfirmedNames to set
	 */
	public void setAllowUnconfirmedNames(boolean allowUnconfirmedNames) {
		this.allowUnconfirmedNames = allowUnconfirmedNames;
	}
}