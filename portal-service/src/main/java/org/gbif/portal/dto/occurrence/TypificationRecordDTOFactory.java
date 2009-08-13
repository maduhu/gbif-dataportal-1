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
import org.gbif.portal.model.occurrence.TypificationRecord;
import org.gbif.portal.model.resources.DataProvider;
import org.gbif.portal.model.resources.DataResource;

/**
 * BriefTaxonConceptDTOFactory
 * 
 * Factory to create TypificationRecordDTO objects.
 * 
 * @author Donald Hobern
 */
public class TypificationRecordDTOFactory extends BaseDTOFactory {
	
	/**
	 * @see org.gbif.portal.dto.DTOFactory#createDTO(org.gbif.portal.model.BaseObject)
	 */
	public Object createDTO(Object modelObject) {
		if(modelObject==null)
			return null;
		TypificationRecord tr = (TypificationRecord) modelObject;
		TypificationRecordDTO dto = new TypificationRecordDTO();
		populateTypificationRecord(tr, dto);
		return dto;
	}
	
	/**
	 * Sets the values in a BriefOccurrenceRecordDTO
	 * @param source model Object
	 * @param target briefTaxonConceptDTO
	 */
	protected void populateTypificationRecord(TypificationRecord source, TypificationRecordDTO target){
		target.setKey(source.getId().toString());
		DataResource dr = source.getDataResource();
		if(dr!=null){
			target.setDataResourceKey(dr.getId().toString());
			target.setDataResourceName(dr.getName());
			if(allowLazyLoading){
				DataProvider dp = dr.getDataProvider();
				target.setDataProviderKey(dp.getId().toString());
				target.setDataProviderName(dp.getName());
			}
		}
		target.setOccurrenceRecordKey(source.getOccurrenceRecordId().toString());
		target.setOccurrenceRecordInstitutionCode(source.getOccurrenceRecord().getInstitutionCode().getCode());
		target.setOccurrenceRecordCollectionCode(source.getOccurrenceRecord().getCollectionCode().getCode());
		target.setOccurrenceRecordCatalogueNumber(source.getOccurrenceRecord().getCatalogueNumber().getCode());
		target.setTaxonNameKey(source.getTaxonNameId().toString());
		target.setScientificName(source.getScientificName());
		target.setPublication(source.getPublication());
		target.setTypeStatus(source.getTypeStatus());
	}
}