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

import java.util.Date;

import org.gbif.portal.dto.BaseDTOFactory;
import org.gbif.portal.model.occurrence.BasisOfRecord;
import org.gbif.portal.model.occurrence.OccurrenceRecord;

/**
 * KmlOccurrenceDTOFactory
 * 
 * Factory to create KmlOccurrenceDTO objects.
 * 
 * @author jcuadra
 */
public class KmlOccurrenceRecordDTOFactory extends BaseDTOFactory {
	
	/**
	 * @see org.gbif.portal.dto.DTOFactory#createDTO(org.gbif.portal.model.BaseObject)
	 */
	public Object createDTO(Object modelObject) {
		
		if(modelObject==null)
			return null;
		
		KmlOccurrenceRecordDTO dto = new KmlOccurrenceRecordDTO();
		populateKmlOccurrenceRecord(modelObject, dto);
		return dto;
	}
	
	/**
	 * Sets the values in a KmlOccurrenceRecordDTO
	 * @param source model Object
	 * @param target kmlOccurrenceRecordDTO
	 */
	protected void populateKmlOccurrenceRecord(Object source, KmlOccurrenceRecordDTO target){
				
		if(source instanceof Object[]){
			Object[] modelObjects = (Object[]) source;
			
			target.setKey( ((Long) modelObjects[0]).toString());
			target.setLatitude((Float) modelObjects[1]);
			target.setLongitude((Float) modelObjects[2]);
			
			target.setDataProviderKey( ((Long) modelObjects[3]).toString());
			target.setDataResourceKey( ((Long) modelObjects[4]).toString());
			target.setDataProviderName((String) modelObjects[5]);
			target.setDataResourceName((String) modelObjects[6]);
			
			target.setInstitutionCode((String) modelObjects[7]);
			target.setCollectionCode((String) modelObjects[8]);
			target.setCatalogueNumber((String) modelObjects[9]);
			
			target.setOccurrenceDate((Date) modelObjects[10]);
			
			target.setTaxonName((String) modelObjects[11]);
			
			target.setNubTaxonConceptKey( ((Long) modelObjects[12]).toString());
			
			target.setTaxonConceptKey( ((Long) modelObjects[13]).toString());
			
			target.setDataResourceCitableAgent((String) modelObjects[14]);
			
			target.setNubTaxonConceptName((String) modelObjects[15]);
		}
		
		if(source instanceof OccurrenceRecord)
		{
			OccurrenceRecord occurrenceRecord = (OccurrenceRecord) source;
			
			target.setKey(occurrenceRecord.getId().toString());
			target.setLatitude(occurrenceRecord.getLatitude());
			target.setLatitude(occurrenceRecord.getLongitude());
			
			target.setDataProviderKey(occurrenceRecord.getDataProviderId().toString());
			target.setDataResourceKey(occurrenceRecord.getDataResourceId().toString());
			target.setDataProviderName(occurrenceRecord.getDataProvider().getName());
			target.setDataResourceName(occurrenceRecord.getDataResource().getName());
			
			target.setInstitutionCode(occurrenceRecord.getInstitutionCode().getCode());
			target.setCollectionCode(occurrenceRecord.getCollectionCode().getCode());
			target.setCatalogueNumber(occurrenceRecord.getCatalogueNumber().getCode());
			
			target.setOccurrenceDate(occurrenceRecord.getOccurrenceDate());
			
			target.setTaxonName(occurrenceRecord.getTaxonName().getCanonical());
			
			target.setNubTaxonConceptKey(occurrenceRecord.getNubTaxonConceptId().toString());
			
			target.setTaxonConceptKey(occurrenceRecord.getTaxonConcept().getId().toString());
			
			target.setDataResourceCitableAgent(occurrenceRecord.getDataResource().getCitableAgent());
			
			target.setNubTaxonConceptName(occurrenceRecord.getNubTaxonConcept().getTaxonName().getCanonical());
		}
		
	}
}