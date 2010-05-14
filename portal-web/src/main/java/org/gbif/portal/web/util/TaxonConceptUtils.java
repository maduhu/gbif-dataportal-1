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
package org.gbif.portal.web.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.dto.taxonomy.RelationshipAssertionDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.dto.util.TaxonRankType;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.web.controller.taxonomy.bean.SpecialTreeNode;
import org.gbif.portal.web.filter.FilterUtils;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.ServletRequestUtils;

/**
 * Utilities methods for accessing TaxonConcepts.
 * 
 * @author dmartin
 */
public class TaxonConceptUtils {
	
	protected static Log logger = LogFactory.getLog(TaxonConceptUtils.class);
	
	/** The taxonomy manager to use */
	protected TaxonomyManager taxonomyManager;
	
	/** The i18n message source to use */
	protected MessageSource messageSource;
	
	/** The priority threshold to use */
	protected int taxonomicPriorityThreshold  = 20;

	/** Request param for unconfirmedNames */
	protected String unconfirmedNamesRequestParam="unconfirmedNames";
	
	/** Unconfirmed names */
	protected String unconfirmedNamesI18nKey="taxonomy.browser.unconfirmed.names";	
	
	/** i18n key for the synonyms (plural) warning */
	protected String synonymsWarning="warnings.filter.scientific.name.synonyms";
	
	/** i18n key for a synonym warning */
	protected String synonymWarning="warnings.filter.scientific.name.synonym";	
	
	/** Message key for the accepted concept warning */
	protected String acceptedConceptWarning ="warnings.filter.scientific.name.accepted";
	
	/** Message key for the unsupported rank warning */
	protected String unsupportedRankWarning ="warnings.filter.scientific.name.unsupportedrank";	
	
	/**
	 * Retrieve a list of concepts for the supplied common name, mapping 
	 * 
	 * @param scientificName
	 * @param dataProviderKey
	 * @param dataResourceKey
	 * @param allowUnconfirmedNames
	 * @param maxResults
	 * @param warnings
	 * @param locale
	 * @return the matched concepts
	 */
	public List<BriefTaxonConceptDTO> getTaxonConceptForSciName(String scientificName, String dataProviderKey, String dataResourceKey,
			boolean allowUnconfirmedNames, int maxResults, List<String> warnings, List<String> fatalWarnings, Locale locale) throws ServiceException {
		
		SearchResultsDTO results = taxonomyManager.findTaxonConcepts(scientificName, 
				false, null, dataProviderKey, dataResourceKey, 
				null, null, null, null, allowUnconfirmedNames,false, 
				new SearchConstraints(0, maxResults));

		//need to map results to subject
		boolean addWarnings = warnings!=null;
		List<BriefTaxonConceptDTO> concepts = new ArrayList<BriefTaxonConceptDTO>();
		
		if(results!=null && !results.isEmpty()){
			
			concepts = results.getResults();
			
			//remove this triplet - as we now how concepts to search against
			for (BriefTaxonConceptDTO tcDTO: concepts){
				//get the rank and find the appropriate subject
				String rank = tcDTO.getRank();
				
				//note we only support mapping of major ranks
				String conceptKey = tcDTO.getKey();
				
				//retrieve the nub concept id - so that synonyms are matched
				//hence if the nub concept for Felis concolor is returned, the speciesConceptKey
				//does in fact point to Puma concolor
				if(TaxonRankType.isRecognisedMajorRank(rank)){
					
					String acceptedConceptKey = null;
					
					try {
						acceptedConceptKey = (String) BeanUtils.getProperty(tcDTO, rank+"ConceptKey");
					} catch (Exception e) {
						logger.debug(e.getMessage(),e);
					}
					
					if(acceptedConceptKey!=null && addWarnings){
							
						if(conceptKey!=null && acceptedConceptKey!=null && !conceptKey.equals(acceptedConceptKey)){
							conceptKey = acceptedConceptKey;
							BriefTaxonConceptDTO briefAcceptedDTO = taxonomyManager.getBriefTaxonConceptFor(acceptedConceptKey);
							String[] objects = new String[3];
							objects[0] = briefAcceptedDTO.getRank();
							objects[1] = briefAcceptedDTO.getTaxonName();	
							objects[2] = tcDTO.getTaxonName();	
							String warningMessage = messageSource.getMessage(acceptedConceptWarning, objects, locale);
							warnings.add(warningMessage);
						} else if(conceptKey!=null){
							//check for synonyms and add warning if necessary
							addSynonymWarnings(warnings, locale, tcDTO);
						}
					}
				} else if(!TaxonRankType.SUB_SPECIES_STR.equals(rank)) {
					// add unsupported rank warnings
					if(addWarnings){
						String[] objects = new String[2];
						objects[0] = tcDTO.getRank();
						objects[1] = tcDTO.getTaxonName();
						String warningMessage = messageSource.getMessage(unsupportedRankWarning, objects, locale);
						//if this is the only concept returned add a fatal warning
						if(concepts.size()==1 || allConceptsAreNonMajorRank(concepts)){
							fatalWarnings.add(warningMessage);
						} else {
							warnings.add(warningMessage);
						}
					}
				}		
			}
		}
		return concepts;
	}
	
	/**
	 * Returns true if all concepts are at non major ranks.
	 * @param concepts
	 * @return
	 */
	public static boolean allConceptsAreNonMajorRank(List<BriefTaxonConceptDTO> concepts) {
		for(BriefTaxonConceptDTO concept: concepts){
			if(TaxonRankType.isRecognisedMajorRank(concept.getRank(), true))
				return false;
		}
		return true;
	}	
	
	/**
	 * Organise concepts, separating unconfirmed concepts.
	 * 
	 * @param request
	 * @param taxonConceptKey
	 * @param selectedConcept
	 * @param concepts
	 * @throws ServiceException
	 */
	public void organiseUnconfirmedNames(HttpServletRequest request, BriefTaxonConceptDTO selectedConcept, List<BriefTaxonConceptDTO> concepts, List<BriefTaxonConceptDTO> childConcepts) throws ServiceException {
		organiseUnconfirmedNames(request, selectedConcept, concepts, childConcepts, taxonomicPriorityThreshold);
	}	
	
	/**
	 * Organise concepts, separating unconfirmed concepts.
	 * 
	 * @param request
	 * @param taxonConceptKey
	 * @param selectedConcept
	 * @param concepts
	 * @throws ServiceException
	 */
	public void organiseUnconfirmedNames(HttpServletRequest request, BriefTaxonConceptDTO selectedConcept, List<BriefTaxonConceptDTO> concepts, List<BriefTaxonConceptDTO> childConcepts, int taxonomicPriorityThreshold) throws ServiceException {
		
		if(!selectedConcept.getIsNubConcept()){
			concepts.addAll(childConcepts);
			return;
		}

		//find position of unconfirmed name
		int higherConceptUnconfirmed = containsUnconfirmedName(concepts, taxonomicPriorityThreshold);
		
		if(higherConceptUnconfirmed!=-1){
			//if any of the higher concepts including the selected concept are unconfirmed - insert a Unconfirmed node
			BriefTaxonConceptDTO unconfirmedConcept = concepts.get(higherConceptUnconfirmed);
			SpecialTreeNode treeNode = new SpecialTreeNode();
			treeNode.setDisplayName(unconfirmedNamesI18nKey);
			treeNode.setExpandRequestParameter(unconfirmedNamesRequestParam);
			treeNode.setKey(unconfirmedNamesRequestParam);
			treeNode.setHigherTaxa(true);
			unconfirmedConcept.setParentConceptKey(unconfirmedNamesRequestParam);
			if(higherConceptUnconfirmed!=0){
				BriefTaxonConceptDTO parentOfUnconfirmedConcept = concepts.get(higherConceptUnconfirmed-1);
				treeNode.setParentConceptKey(parentOfUnconfirmedConcept.getKey());
			}
			//insert node at specified position			
			concepts.add(higherConceptUnconfirmed, treeNode);
			//add all child concepts
			concepts.addAll(childConcepts);
			
		} else {
			//check children for unconfirmed-ness and group into a Special Node if any exist
			List<BriefTaxonConceptDTO> kosherChildConcepts = new ArrayList<BriefTaxonConceptDTO>();
			List<BriefTaxonConceptDTO> unconfirmedNameConcepts = new ArrayList<BriefTaxonConceptDTO>();

			//sort children
			for(BriefTaxonConceptDTO concept: childConcepts){
				if(concept.getTaxonomicPriority()> taxonomicPriorityThreshold)
					unconfirmedNameConcepts.add(concept);
				else
					kosherChildConcepts.add(concept);
			}
			
			//add all kosher concepts
			concepts.addAll(kosherChildConcepts);
			//add unconfirmed name concept				
			if(!unconfirmedNameConcepts.isEmpty()){
				BriefTaxonConceptDTO unconfirmedExample = unconfirmedNameConcepts.get(0);
				SpecialTreeNode treeNode = new SpecialTreeNode();
				treeNode.setDisplayName(unconfirmedNamesI18nKey);
				treeNode.setParentConceptKey(unconfirmedExample.getParentConceptKey());
				treeNode.setChildren(unconfirmedNameConcepts);
				boolean showUnconfirmedNames = ServletRequestUtils.getBooleanParameter(request, unconfirmedNamesRequestParam, false);
				treeNode.setShowChildren(showUnconfirmedNames);
				treeNode.setExpandRequestParameter(unconfirmedNamesRequestParam);
				concepts.add(treeNode);
			}
		}
	}

	/**
	 * Returns the position of the first unconfirmed name in the hierarchy.
	 * @return index of first unconfirmed concept. -1 if none found. 
	 */
	private int containsUnconfirmedName(List<BriefTaxonConceptDTO> concepts, int taxonPriorityThreshold) {
		for(BriefTaxonConceptDTO concept: concepts){
			if(concept.getTaxonomicPriority()>taxonPriorityThreshold)
				return concepts.indexOf(concept);
		}
		return -1;
	}		
	
	/**
	 * Returns a string of the form "Kingdom:Animalia".
	 * @param concept
	 * @return
	 */
	public static String getFormattedRankAndName(BriefTaxonConceptDTO concept){
		StringBuffer sb = new StringBuffer();
		sb.append(StringUtils.capitalize(concept.getRank()));
		sb.append(":");
		sb.append(concept.getTaxonName());		
		return sb.toString();
	}
	
	/**
	 * Retrieves a list of known synonyms for this concept.
	 * 
	 * @param taxonConceptKey
	 * @return List
	 * @throws ServiceException
	 */
	public List<RelationshipAssertionDTO> retrieveSynonyms(String taxonConceptKey) throws ServiceException {
		//add relationships to this concept
		List<RelationshipAssertionDTO> toRas = taxonomyManager.findRelationshipAssertionsForToTaxonConcept(taxonConceptKey);
		List<RelationshipAssertionDTO> synonyms = new ArrayList<RelationshipAssertionDTO>();
		for(RelationshipAssertionDTO ra: toRas){
			if(ra.getRelationshipType()==RelationshipAssertionDTO.RELATIONSHIP_TYPE_SYNONYM
			    || ra.getRelationshipType()==RelationshipAssertionDTO.RELATIONSHIP_TYPE_AMBIGUOUS_SYNONYM)
				synonyms.add(ra);
		}
		return synonyms;
	}	

	/**
	 * Add synonym warnings if this concept has synonyms.
	 * 
	 * @param request
	 * @param locale
	 * @param tcDTO
	 * @param conceptKey
	 * @throws ServiceException
	 */
	public void addSynonymWarnings(HttpServletRequest request, Locale locale, BriefTaxonConceptDTO tcDTO) throws ServiceException {
		List<String> warnings = FilterUtils.getFilterWarnings(request);
		addSynonymWarnings(warnings, locale, tcDTO);
	}
	
	/**
	 * Add synonym warnings if this concept has synonyms.
	 * 
	 * @param request
	 * @param locale
	 * @param tcDTO
	 * @param conceptKey
	 * @throws ServiceException
	 */
	public void addSynonymWarnings(List<String> warnings, Locale locale, BriefTaxonConceptDTO tcDTO) throws ServiceException {
		if(warnings==null)
			return;
		
		List<RelationshipAssertionDTO> raDTOs = retrieveSynonyms(tcDTO.getKey());
        if(raDTOs!=null){
            for (int i = 0; i < raDTOs.size(); i++) {
                    if (raDTOs.get(i).getFromTaxonName().equals(tcDTO.getTaxonName()))
                            raDTOs.remove(i);
            }
            if(raDTOs.size()>0){
                    StringBuffer synonymList = new StringBuffer();
                    for(int i=0; i<raDTOs.size(); i++){
                            if(i>0)
                                    synonymList.append(", ");
                            synonymList.append(raDTOs.get(i).getFromTaxonName());
                    }
                    String warningMessage = null;
                    Object[] objects = new Object[2];
                    //the accepted name
                    objects[0] = tcDTO.getTaxonName();
                    //the synonym list
                    objects[1] = synonymList.toString();
                    
                    if(raDTOs.size()>1)
                            warningMessage = messageSource.getMessage(synonymsWarning, objects, locale);
                    else
                            warningMessage = messageSource.getMessage(synonymWarning, objects, locale);
                    
                    warnings.add(warningMessage);
            }
        }
	}	
	
	/**
	 * @param taxonomyManager the taxonomyManager to set
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @param synonymsWarning the synonymsWarning to set
	 */
	public void setSynonymsWarning(String synonymsWarning) {
		this.synonymsWarning = synonymsWarning;
	}

	/**
	 * @param synonymWarning the synonymWarning to set
	 */
	public void setSynonymWarning(String synonymWarning) {
		this.synonymWarning = synonymWarning;
	}

	/**
	 * @param taxonomicPriorityThreshold the taxonomicPriorityThreshold to set
	 */
	public void setTaxonomicPriorityThreshold(int taxonomicPriorityThreshold) {
		this.taxonomicPriorityThreshold = taxonomicPriorityThreshold;
	}

	/**
	 * @param unconfirmedNamesI18nKey the unconfirmedNamesI18nKey to set
	 */
	public void setUnconfirmedNamesI18nKey(String unconfirmedNamesI18nKey) {
		this.unconfirmedNamesI18nKey = unconfirmedNamesI18nKey;
	}

	/**
	 * @param unconfirmedNamesRequestParam the unconfirmedNamesRequestParam to set
	 */
	public void setUnconfirmedNamesRequestParam(String unconfirmedNamesRequestParam) {
		this.unconfirmedNamesRequestParam = unconfirmedNamesRequestParam;
	}
}