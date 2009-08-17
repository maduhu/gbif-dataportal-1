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
package org.gbif.portal.dto.occurrence;

import org.gbif.portal.dto.BaseDTOFactory;
import org.gbif.portal.model.occurrence.BasisOfRecord;
import org.gbif.portal.model.occurrence.OccurrenceRecord;

/**
 * BriefTaxonConceptDTOFactory
 * 
 * Factory to create BriefOccurrenceDTO objects.
 * 
 * @author dmartin
 */
public class BriefOccurrenceRecordDTOFactory extends BaseDTOFactory {
	
	/**
	 * @see org.gbif.portal.dto.DTOFactory#createDTO(org.gbif.portal.model.BaseObject)
	 */
	public Object createDTO(Object modelObject) {
		if(modelObject==null)
			return null;
		
		BriefOccurrenceRecordDTO dto = new BriefOccurrenceRecordDTO();
		populateBriefOccurrenceRecord(modelObject, dto);
		return dto;
	}
	
	/**
	 * Sets the values in a BriefOccurrenceRecordDTO
	 * @param source model Object
	 * @param target briefTaxonConceptDTO
	 */
	protected void populateBriefOccurrenceRecord(Object source, BriefOccurrenceRecordDTO target){
		
		OccurrenceRecord or = null;		
		
		if(source instanceof Object[]){
			Object[] modelObjects = (Object[]) source;
			//very much tied to the  order
			//oc, oc.taxonName.canonical, oc.dataResource.name, oc.dataProvider.name
			or = (OccurrenceRecord) modelObjects[0];
			target.setTaxonName((String) modelObjects[1]);
			target.setDataResourceName((String) modelObjects[2]);
			target.setDataProviderName((String) modelObjects[3]);
		} else {
			 or = (OccurrenceRecord) source;
			 target.setDataProviderName(or.getDataProvider().getName());
			 target.setDataResourceName(or.getDataResource().getName());
			 target.setDataResourceRights(or.getDataResource().getRights());
			 target.setTaxonName(or.getTaxonName().getCanonical());
		}			
		
		target.setKey(or.getId().toString());
		if(or.getInstitutionCode()!=null)
			target.setInstitutionCode(or.getInstitutionCode().getCode());
		if(or.getCollectionCode()!=null)
			target.setCollectionCode(or.getCollectionCode().getCode());
		if(or.getCatalogueNumber()!=null)
			target.setCatalogueNumber(or.getCatalogueNumber().getCode());

		target.setDataProviderKey(or.getDataProviderId().toString());
		target.setDataResourceKey(or.getDataResourceId().toString());
		target.setTaxonConceptKey(or.getTaxonConceptId().toString());
		//this is lazy loading the taxon name - one to watch and remove if possible!
		if(or.getTaxonNameId()!=null){
			target.setTaxonNameKey(or.getTaxonNameId().toString());
		}
		target.setIsoCountryCode(or.getIsoCountryCode());
		target.setLatitude(or.getLatitude());
		target.setLongitude(or.getLongitude());
		target.setOccurrenceDate(or.getOccurrenceDate());
//		target.setYear(source.getYear());
//		target.setMonth(source.getMonth());
		target.setTaxonomicIssue(or.getTaxonomicIssue());
		target.setGeospatialIssue(or.getGeospatialIssue());
		target.setOtherIssue(or.getOtherIssue());
		if(or.getBasisOfRecord()!=null){
			target.setBasisOfRecord(or.getBasisOfRecord().getName());
		} else {
			target.setBasisOfRecord(BasisOfRecord.UNKNOWN.getName());
		}		
	}
}