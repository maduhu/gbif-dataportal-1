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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.dto.taxonomy.TaxonConceptDTO;
import org.gbif.portal.dto.util.TaxonRankType;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.web.content.filter.FilterHelper;
import org.gbif.portal.web.filter.CriterionDTO;
import org.gbif.portal.web.filter.FilterUtils;
import org.gbif.portal.web.util.TaxonConceptUtils;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Filter Helper for the classification filter.
 * 
 * @author dmartin
 */
public class ClassificationFilterHelper implements FilterHelper {

	protected static Log logger = LogFactory.getLog(ClassificationFilterHelper.class);	

	protected String subject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.CLASSIFICATION";

	protected String nubSubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.NUBCONCEPTID";	
	protected String kingdomSubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.TR.KINGDOMID";
	protected String phylumSubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.TR.PHYLUMID";
	protected String classSubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.TR.CLASSID";	
	protected String orderSubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.TR.ORDERID";
	protected String familySubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.TR.FAMILYID";
	protected String genusSubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.TR.GENUSID";
	protected String speciesSubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.TR.SPECIESID";

	/** The taxonomy manager for service layer queries */
	protected TaxonomyManager taxonomyManager;
	/** Utilities for taxon concepts */
	protected TaxonConceptUtils taxonConceptUtils;
	/** Whether warnings should be added */
	protected boolean addWarnings = true;
	/** message source */
	protected MessageSource messageSource;	
	/** Message key for the accepted concept warning */
	protected String acceptedConceptWarning ="warnings.filter.scientific.name.accepted";
	/** Message key for the unsupported rank warning */
	protected String unsupportedRankWarning ="warnings.filter.scientific.name.unsupportedrank";	
	
	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#getDisplayValue(java.lang.String)
	 */
	public String getDisplayValue(String value, Locale locale) {
		try {
			BriefTaxonConceptDTO taxonConcept = taxonomyManager.getBriefTaxonConceptFor(value);
			if(taxonConcept!=null){
				StringBuffer sb = new StringBuffer();
				sb.append(StringUtils.capitalize(taxonConcept.getRank()));
				sb.append(": ");
//				boolean genusOrLower = TaxonRankType.isGenusOrLowerRank(taxonConcept.getRank());
				//TODO should this be in a getHTMLDisplayValue() method?
//				if(genusOrLower)
//					sb.append("<i>");
				sb.append(taxonConcept.getTaxonName());
//				if(genusOrLower)
//					sb.append("</i>");					
				return sb.toString();
			}	
		} catch (ServiceException e){
			logger.warn(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#preProcess(java.util.List, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void preProcess(List<PropertyStoreTripletDTO> triplets, HttpServletRequest request, HttpServletResponse response) {

		//remove classification triplets, track them in list
		List<PropertyStoreTripletDTO> classificationTriplets =  new ArrayList<PropertyStoreTripletDTO>();
		for(PropertyStoreTripletDTO triplet: triplets){
			if(subject.equals(triplet.getSubject())){
				classificationTriplets.add(triplet);
			}
		}
		triplets.removeAll(classificationTriplets);
		
		Locale locale  = RequestContextUtils.getLocale(request);
			
		//change into a rank filter
		for(PropertyStoreTripletDTO triplet: classificationTriplets){
			if(subject.equals(triplet.getSubject())){
				try {
					String conceptKey = (String) triplet.getObject();
					TaxonConceptDTO taxonConcept = taxonomyManager.getTaxonConceptFor(conceptKey);
					if(taxonConcept==null){
						logger.error("Bad concept key: "+conceptKey);
						return;
					}
					
					if(TaxonRankType.isRecognisedMajorRank(taxonConcept.getRank())){
						String acceptedConceptKey = null;
						try {
							acceptedConceptKey = (String) BeanUtils.getProperty(taxonConcept, taxonConcept.getRank()+"ConceptKey");
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
						}
						
						//check concept is accepted
						if(addWarnings && conceptKey!=null && acceptedConceptKey!=null && !conceptKey.equals(acceptedConceptKey)){
							conceptKey = acceptedConceptKey;
							BriefTaxonConceptDTO briefAcceptedDTO = taxonomyManager.getBriefTaxonConceptFor(acceptedConceptKey);
							String[] objects = new String[3];
							objects[0] = briefAcceptedDTO.getRank();
							objects[1] = briefAcceptedDTO.getTaxonName();	
							objects[2] = taxonConcept.getTaxonName();	
							String warningMessage = messageSource.getMessage(acceptedConceptWarning, objects, locale);
							FilterUtils.addFilterWarning(request,warningMessage);
						} else if(addWarnings && conceptKey!=null){
							//check for synonyms and add warning if necessary
							taxonConceptUtils.addSynonymWarnings(request, locale, taxonConcept);
						}
					}
					
					if(taxonConcept == null)
						triplet.setSubject(kingdomSubject);
					else if(taxonConcept.getRank().equals(TaxonRankType.KINGDOM_STR))
						triplet.setSubject(kingdomSubject);
					else if(taxonConcept.getRank().equals(TaxonRankType.PHYLUM_STR))
						triplet.setSubject(phylumSubject);
					else if(taxonConcept.getRank().equals(TaxonRankType.CLASS_STR))
						triplet.setSubject(classSubject);					
					else if(taxonConcept.getRank().equals(TaxonRankType.ORDER_STR))
						triplet.setSubject(orderSubject);
					else if(taxonConcept.getRank().equals(TaxonRankType.FAMILY_STR))
						triplet.setSubject(familySubject);
					else if(taxonConcept.getRank().equals(TaxonRankType.GENUS_STR))
						triplet.setSubject(genusSubject);
					else if(taxonConcept.getRank().equals(TaxonRankType.SPECIES_STR))
						triplet.setSubject(speciesSubject);
					else
						triplet.setSubject(nubSubject);
					triplet.setObject(new Long(conceptKey));
					triplets.add(triplet);
				} catch (ServiceException e) {
					logger.error(e.getMessage(), e);
				}
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
	 * @see org.gbif.portal.web.content.filter.FilterHelper#addCriterion2Request(org.gbif.portal.web.filter.CriterionDTO, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void addCriterion2Request(CriterionDTO criterionDTO, ModelAndView mav, HttpServletRequest request) {}	
	
	/**
	 * @param taxonomyManager the taxonomyManager to set
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
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
	 * @param classSubject the classSubject to set
	 */
	public void setClassSubject(String classSubject) {
		this.classSubject = classSubject;
	}

	/**
	 * @param familySubject the familySubject to set
	 */
	public void setFamilySubject(String familySubject) {
		this.familySubject = familySubject;
	}

	/**
	 * @param genusSubject the genusSubject to set
	 */
	public void setGenusSubject(String genusSubject) {
		this.genusSubject = genusSubject;
	}

	/**
	 * @param kingdomSubject the kingdomSubject to set
	 */
	public void setKingdomSubject(String kingdomSubject) {
		this.kingdomSubject = kingdomSubject;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @param nubSubject the nubSubject to set
	 */
	public void setNubSubject(String nubSubject) {
		this.nubSubject = nubSubject;
	}

	/**
	 * @param orderSubject the orderSubject to set
	 */
	public void setOrderSubject(String orderSubject) {
		this.orderSubject = orderSubject;
	}

	/**
	 * @param phylumSubject the phylumSubject to set
	 */
	public void setPhylumSubject(String phylumSubject) {
		this.phylumSubject = phylumSubject;
	}

	/**
	 * @param speciesSubject the speciesSubject to set
	 */
	public void setSpeciesSubject(String speciesSubject) {
		this.speciesSubject = speciesSubject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
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
}