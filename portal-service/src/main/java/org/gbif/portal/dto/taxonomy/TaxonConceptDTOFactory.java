/***************************************************************************
 * Copyright (C) 2006 Global Biodiversity Information Facility Secretariat.
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
package org.gbif.portal.dto.taxonomy;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.DTOUtils;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.model.resources.DataProvider;
import org.gbif.portal.model.resources.DataResource;
import org.gbif.portal.model.taxonomy.CommonName;
import org.gbif.portal.model.taxonomy.RelationshipAssertion;
import org.gbif.portal.model.taxonomy.TaxonConcept;
import org.gbif.portal.model.taxonomy.TaxonConceptLite;

/**
 * TaxonConceptDTOFactory
 * 
 * Factory to create TaxonConceptDTO objects.
 * 
 * @see BriefTaxonConceptDTOFactory
 * 
 * @author dbarnier
 * @author dmartin
 */
public class TaxonConceptDTOFactory extends BriefTaxonConceptDTOFactory {
	
	protected Log logger = LogFactory.getLog(getClass());
	
	/** The factory can be instantiated with a language code */
	protected String isoLanguageCode;
	
	/**
	 * Default 
	 */
	public TaxonConceptDTOFactory() {}
	
	/**
	 * For when the factory must prioritise the language
	 * @param isoLanguageCode
	 */
	public TaxonConceptDTOFactory(String isoLanguageCode) {
		this.isoLanguageCode = isoLanguageCode;
	}
	
	/**
	 * @see org.gbif.portal.dto.DTOFactory#createResultsDTO(java.util.List, java.lang.Integer)
	 */
	public SearchResultsDTO createResultsDTO(List modelObjects, Integer maxResults, String isoLanguageCode) {
		SearchResultsDTO searchResultsDTO =  new SearchResultsDTO();
		if(modelObjects!=null)		
			DTOUtils.populate(this, searchResultsDTO, modelObjects, maxResults);
		return searchResultsDTO;
	}

	/**
	 * @see org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTOFactory#createDTO(java.lang.Object)
	 */
	@Override
	public Object createDTO(Object modelObject) {
		return createDTO(modelObject, false);
	}
	
	/**
	 * @see org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTOFactory#createDTO(java.lang.Object)
	 */
	@Override
	public Object createDTO(Object modelObject, boolean allowLazyLoading) {
		if(modelObject==null)
			return null;
		TaxonConcept taxonConcept = (TaxonConcept) modelObject;
		TaxonConceptDTO taxonConceptDTO = new TaxonConceptDTO();
		initialBriefTaxonConcept(taxonConcept, taxonConceptDTO, allowLazyLoading);
		//set more data provider
		DataProvider dataProvider = taxonConcept.getDataProvider();
		if(dataProvider!=null){
			taxonConceptDTO.setDataProviderKey(dataProvider.getId().toString());
			taxonConceptDTO.setDataProviderName(dataProvider.getName());
		}
		//set more data resource
		DataResource dataResource = taxonConcept.getDataResource();
		if(dataResource!=null){
			taxonConceptDTO.setDataResourceKey(dataResource.getId().toString());
			taxonConceptDTO.setDataResourceName(dataResource.getName());
			taxonConceptDTO.setDataResourceCitation(dataResource.getCitation());
			taxonConceptDTO.setTaxonomicPriority(dataResource.getTaxonomicPriority());
		}
		//kingdom
		if(taxonConcept.getKingdomConcept()!=null){
			taxonConceptDTO.setKingdom(taxonConcept.getKingdomConcept().getTaxonNameLite().getCanonical());
		}
		//phylum
		if(taxonConcept.getPhylumConcept()!=null){
			taxonConceptDTO.setPhylumDivision(taxonConcept.getPhylumConcept().getTaxonNameLite().getCanonical());
		}
		//order		
		if(taxonConcept.getOrderConcept()!=null){
			taxonConceptDTO.setOrder(taxonConcept.getOrderConcept().getTaxonNameLite().getCanonical());
		}
		//class			
		if(taxonConcept.getClassConcept()!=null){
			taxonConceptDTO.setKlass(taxonConcept.getClassConcept().getTaxonNameLite().getCanonical());
		}
		//family				
		if(taxonConcept.getFamilyConcept()!=null){
			taxonConceptDTO.setFamily(taxonConcept.getFamilyConcept().getTaxonNameLite().getCanonical());
		}			
		//genus
		if(taxonConcept.getGenusConcept()!=null){
			taxonConceptDTO.setGenus(taxonConcept.getGenusConcept().getTaxonNameLite().getCanonical());
		}
		//species		
		if(taxonConcept.getSpeciesConcept()!=null){
			taxonConceptDTO.setSpecies(taxonConcept.getSpeciesConcept().getTaxonNameLite().getCanonical());
		}

		//TODO Something to keep an eye on - involves lazy loading of the accepted concept
		Set<RelationshipAssertion> ras = taxonConcept.getRelationshipAssertions();
		if(ras!=null && !ras.isEmpty()){
			Iterator raIter = ras.iterator();
			while(raIter.hasNext()){
				RelationshipAssertion ra = (RelationshipAssertion) raIter.next();
				TaxonConceptLite tcLite = ra.getToConcept();
				if(tcLite.isAccepted()){
					taxonConceptDTO.setStatus("not accepted");
					taxonConceptDTO.setAcceptedConceptKey(tcLite.getId().toString());
					taxonConceptDTO.setAcceptedTaxonName(tcLite.getTaxonNameLite().getCanonical());
					taxonConceptDTO.setConceptStatus(ra.getRelationshipType().getValue());
					break;
				}
			}
		}	
		
		//TODO Something to keep an eye on - involves lazy loading of the accepted concept
		Set<CommonName> commonNames = taxonConcept.getCommonNames();
		if(commonNames!=null && !commonNames.isEmpty()) {
			boolean commonNameSet = false;
			if (isoLanguageCode != null) {
				if(logger.isDebugEnabled())
					logger.debug("Trying to get a common name for code: " + isoLanguageCode);
				for (CommonName cn : commonNames) {
					if (isoLanguageCode.equalsIgnoreCase(cn.getIsoLanguageCode())) {
						if(logger.isDebugEnabled())
							logger.debug("Common name in required language found");
						taxonConceptDTO.setCommonName(cn.getName());
						taxonConceptDTO.setCommonNameLanguage(cn.getLanguage());
						commonNameSet = true;
						break;
					}
				}
			} 
			if (!commonNameSet) {
				if(logger.isDebugEnabled())
					logger.debug("Either no isoLanguageCode given[" + isoLanguageCode + "] or no common name found - using first");
				// just use the first name retrieved
				CommonName commonName = commonNames.iterator().next();
				taxonConceptDTO.setCommonName(commonName.getName());
				taxonConceptDTO.setCommonNameLanguage(commonName.getLanguage());
			}
		}
		
		//status
		if(taxonConcept.getTaxonomicPriority().intValue() > 10)
			taxonConceptDTO.setStatus("unconfirmed");
		else
			taxonConceptDTO.setStatus("accepted");		
		
		//author
		taxonConceptDTO.setAuthor(taxonConcept.getTaxonName().getAuthor());
		
		//nub concept key
		if(taxonConcept.getIsNubConcept()!=null && !taxonConcept.getIsNubConcept() && taxonConcept.getPartnerConceptId()!=null){
			taxonConceptDTO.setNubConceptKey(taxonConcept.getPartnerConceptId().toString());
		}
		return taxonConceptDTO;
	}
}