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

import org.gbif.portal.dto.BaseDTOFactory;
import org.gbif.portal.dto.util.TaxonRankType;
import org.gbif.portal.model.taxonomy.TaxonConcept;
import org.gbif.portal.model.taxonomy.TaxonConceptLite;

/**
 * BriefTaxonConceptDTOFactory
 * 
 * Factory to create ClassificationDTO objects.
 * 
 * @author dmartin
 */
public class BriefTaxonConceptDTOFactory extends BaseDTOFactory {

	/**
	 * @see org.gbif.portal.dto.DTOFactory#createDTO(org.gbif.portal.model.BaseObject)
	 */
	public Object createDTO(Object modelObject) {
		if(modelObject==null)
			return null;
		BriefTaxonConceptDTO briefTaxonConceptDTO = new BriefTaxonConceptDTO();
		initialBriefTaxonConcept(modelObject, briefTaxonConceptDTO, false);
		return briefTaxonConceptDTO;
	}
		
	
	/**
	 * @see org.gbif.portal.dto.DTOFactory#createDTO(org.gbif.portal.model.BaseObject)
	 */
	public Object createDTO(Object modelObject, boolean allowLazyLoading) {
		if(modelObject==null)
			return null;
		BriefTaxonConceptDTO briefTaxonConceptDTO = new BriefTaxonConceptDTO();
		initialBriefTaxonConcept(modelObject, briefTaxonConceptDTO, allowLazyLoading);
		return briefTaxonConceptDTO;
	}
	
	/**
	 * Sets the values in a BriefTaxonConceptDTO
	 * @param taxonConcept
	 * @param briefTaxonConceptDTO
	 */
	protected void initialBriefTaxonConcept(Object modelObject, BriefTaxonConceptDTO briefTaxonConceptDTO, boolean allowLazyLoading){
		if(modelObject instanceof TaxonConceptLite){
			initializeWithLite((TaxonConceptLite) modelObject, briefTaxonConceptDTO, allowLazyLoading);
		} else if(modelObject instanceof TaxonConcept){
			initializeWithFull((TaxonConcept) modelObject, briefTaxonConceptDTO, allowLazyLoading);
		}
	}
	
	/**
	 * Initialise the lite version.
	 * 
	 * @param taxonConcept
	 * @param briefTaxonConceptDTO
	 */
	private void initializeWithLite(TaxonConceptLite taxonConcept, BriefTaxonConceptDTO briefTaxonConceptDTO, boolean allowLazyLoading){
		briefTaxonConceptDTO.setKey(taxonConcept.getId().toString());
		briefTaxonConceptDTO.setRank(taxonConcept.getTaxonRank().getName());
		briefTaxonConceptDTO.setRankValue(taxonConcept.getTaxonRank().getValue());		
		briefTaxonConceptDTO.setTaxonName(taxonConcept.getTaxonNameLite().getCanonical());
				
		if(TaxonRankType.isRecognisedMajorRank(taxonConcept.getTaxonRank().getName(), true, true)) //set the Rank Code if it is a mayor rank
			briefTaxonConceptDTO.setRankCode(taxonConcept.getTaxonRank().getName().substring(0, 1).toUpperCase() + taxonConcept.getTaxonRank().getName().substring(1));		
		
		if(taxonConcept.getParentConceptId()!=null){
			briefTaxonConceptDTO.setParentConceptKey(taxonConcept.getParentConceptId().toString());
		}
		//set parent id if available
		if(taxonConcept.getPartnerConceptId()!=null){
			briefTaxonConceptDTO.setPartnerConceptKey(taxonConcept.getPartnerConceptId().toString());
		}
		if(taxonConcept.getIsNubConcept()!=null)
			briefTaxonConceptDTO.setIsNubConcept(taxonConcept.getIsNubConcept());
		else
			briefTaxonConceptDTO.setIsNubConcept(false);
		briefTaxonConceptDTO.setAccepted(taxonConcept.isAccepted());
		briefTaxonConceptDTO.setTaxonomicPriority(taxonConcept.getTaxonomicPriority());
		
		if(allowLazyLoading)
			briefTaxonConceptDTO.setChildCount(taxonConcept.getChildConcepts().size());
	}
	
	/**
	 * Initialise the full version.
	 * 
	 * @param taxonConcept
	 * @param briefTaxonConceptDTO
	 */
	private void initializeWithFull(TaxonConcept taxonConcept, BriefTaxonConceptDTO briefTaxonConceptDTO, boolean allowLazyLoading){
		briefTaxonConceptDTO.setKey(taxonConcept.getId().toString());
		briefTaxonConceptDTO.setRank(taxonConcept.getTaxonRank().getName());
		briefTaxonConceptDTO.setRankValue(taxonConcept.getTaxonRank().getValue());		
		briefTaxonConceptDTO.setTaxonName(((TaxonConcept)taxonConcept).getTaxonName().getCanonical());
				
		if(taxonConcept.getParentConceptId()!=null){
			briefTaxonConceptDTO.setParentConceptKey(taxonConcept.getParentConceptId().toString());
		}
		//set parent id if available
		if(taxonConcept.getPartnerConceptId()!=null){
			briefTaxonConceptDTO.setPartnerConceptKey(taxonConcept.getPartnerConceptId().toString());
		}
		if(taxonConcept.getIsNubConcept()!=null)
			briefTaxonConceptDTO.setIsNubConcept(taxonConcept.getIsNubConcept());
		else
			briefTaxonConceptDTO.setIsNubConcept(false);
		briefTaxonConceptDTO.setAccepted(taxonConcept.isAccepted());
		briefTaxonConceptDTO.setTaxonomicPriority(taxonConcept.getTaxonomicPriority());

		if(taxonConcept.getKingdomConceptId()!=null)
			briefTaxonConceptDTO.setKingdomConceptKey(taxonConcept.getKingdomConceptId().toString());
		
		if(taxonConcept.getPhylumConceptId()!=null)
			briefTaxonConceptDTO.setPhylumConceptKey(taxonConcept.getPhylumConceptId().toString());
		
		if(taxonConcept.getOrderConceptId()!=null)
			briefTaxonConceptDTO.setOrderConceptKey(taxonConcept.getOrderConceptId().toString());
		
		if(taxonConcept.getClassConceptId()!=null)
			briefTaxonConceptDTO.setClassConceptKey(taxonConcept.getClassConceptId().toString());
		
		if(taxonConcept.getFamilyConceptId()!=null)
			briefTaxonConceptDTO.setFamilyConceptKey(taxonConcept.getFamilyConceptId().toString());
		
		if(taxonConcept.getGenusConceptId()!=null)
			briefTaxonConceptDTO.setGenusConceptKey(taxonConcept.getGenusConceptId().toString());
		
		if(taxonConcept.getSpeciesConceptId()!=null)
			briefTaxonConceptDTO.setSpeciesConceptKey(taxonConcept.getSpeciesConceptId().toString());
		
		if(allowLazyLoading)
			briefTaxonConceptDTO.setChildCount(taxonConcept.getChildConcepts().size());
	}
}