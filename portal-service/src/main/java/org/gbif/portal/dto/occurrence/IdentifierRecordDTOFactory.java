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
import org.gbif.portal.model.occurrence.IdentifierRecord;

/**
 * BriefTaxonConceptDTOFactory
 * 
 * Factory to create BriefOccurrenceDTO objects.
 * 
 * @author dmartin
 */
public class IdentifierRecordDTOFactory extends BaseDTOFactory {
	
	/**
	 * @see org.gbif.portal.dto.DTOFactory#createDTO(org.gbif.portal.model.BaseObject)
	 */
	public Object createDTO(Object modelObject) {
		if(modelObject==null)
			return null;
		IdentifierRecord lr = (IdentifierRecord) modelObject;
		IdentifierRecordDTO dto = new IdentifierRecordDTO();
		populateIdentifierRecord(lr, dto);
		return dto;
	}
	
	/**
	 * Sets the values in a BriefOccurrenceRecordDTO
	 * @param source model Object
	 * @param target briefTaxonConceptDTO
	 */
	protected void populateIdentifierRecord(IdentifierRecord source, IdentifierRecordDTO target){
		target.setKey(source.getId().toString());
		if(source.getDataResourceId()!=null)
			target.setDataResourceKey(source.getDataResourceId().toString());
		if(source.getOccurrenceRecordId()!=null)
			target.setOccurrenceRecordKey(source.getOccurrenceRecordId().toString());
		if(source.getIdentifierType()!=null)
			target.setIdentifierType(source.getIdentifierType().getValue());
		target.setIdentifier(source.getIdentifier());
	}
}