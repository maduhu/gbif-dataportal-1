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

import java.util.Set;

import org.gbif.portal.dto.BaseDTOFactory;
import org.gbif.portal.model.occurrence.IdentifierRecord;
import org.gbif.portal.model.occurrence.IdentifierType;
import org.gbif.portal.model.occurrence.ORImage;
import org.gbif.portal.model.occurrence.OccurrenceRecord;
import org.gbif.portal.model.occurrence.RawOccurrenceRecord;
import org.gbif.portal.model.occurrence.TypeStatus;

/**
 * Creates extended dtos using the 2 factories for OccurrenceRecordDTO and RawOccurrenceRecordDTO.
 * 
 * @author dmartin
 */
public class ExtendedOccurrenceRecordDTOFactory extends OccurrenceRecordDTOFactory {

	protected OccurrenceRecordDTOFactory occurrenceRecordDTOFactory;
	protected RawOccurrenceRecordDTOFactory rawOccurrenceRecordDTOFactory;
	
	/**
	 * @see org.gbif.portal.dto.DTOFactory#createDTO(java.lang.Object)
	 */
	public Object createDTO(Object modelObject) {

		if(modelObject==null)
			return null;
		
		if(modelObject instanceof Object[])
		{	
			ExtendedOccurrenceRecordDTO dto = new ExtendedOccurrenceRecordDTO();
			OccurrenceRecord occurrenceRecord = new OccurrenceRecord();
			RawOccurrenceRecord rawOccurrenceRecord = new RawOccurrenceRecord();
			
			Object[] modelObjects = (Object[]) modelObject;
		
			occurrenceRecord = (OccurrenceRecord) modelObjects[2];
			rawOccurrenceRecord = (RawOccurrenceRecord) modelObjects[3];

			//setting the RawOccurrenceRecordDTO
			RawOccurrenceRecordDTO rawOccurrenceRecordDTO = (RawOccurrenceRecordDTO)rawOccurrenceRecordDTOFactory.createDTO(rawOccurrenceRecord);
			dto.setRawOccurrenceRecordDTO(rawOccurrenceRecordDTO);
			
			populateOccurrenceRecord(occurrenceRecord, dto);
			
			//setting the ExtendedOccurrenceRecordDTO variables
			//dto.setTypeStatus();
			//dto.setImageUrl();
			//dto.setGuid();
			//dto.setFieldNumber();
			//dto.setCatalogueNumber();
			
			return dto;
		}		
		
		if(modelObject instanceof OccurrenceRecord){
			
			//create occurrence record dto
			OccurrenceRecord or = (OccurrenceRecord) modelObject;
			ExtendedOccurrenceRecordDTO extOrDTO = new ExtendedOccurrenceRecordDTO();
			occurrenceRecordDTOFactory.populateOccurrenceRecord(or, extOrDTO);
			if(allowLazyLoading){
				Set<ORImage> orImage = or.getOrImage();
				if(orImage!=null && !orImage.isEmpty())
					extOrDTO.setImageUrl(orImage.iterator().next().getUrl());
				Set<TypeStatus> ts = or.getTypeStatus();
				if(ts!=null && !ts.isEmpty())
					extOrDTO.setTypeStatus(ts.iterator().next().getTypeStatus());
				
				//set identifiers
				Set<IdentifierRecord> irs = or.getIdentifierRecords();
				if(irs!=null){
					for(IdentifierRecord ir: irs){
						if(ir.getIdentifierType().equals(IdentifierType.COLLECTORNUMBER)){
							extOrDTO.setCollectorNumber(ir.getIdentifier());
						} else if(ir.getIdentifierType().equals(IdentifierType.FIELDNUMBER)){
							extOrDTO.setFieldNumber(ir.getIdentifier());
						} else if (ir.getIdentifierType().equals(IdentifierType.GUID)){
							extOrDTO.setGuid(ir.getIdentifier());
						}
					}
				}
			}
			
			//create raw occurrence record dto
			RawOccurrenceRecord ror = or.getRawOccurrenceRecord();
			RawOccurrenceRecordDTO rorDTO = (RawOccurrenceRecordDTO) rawOccurrenceRecordDTOFactory.createDTO(ror);
			extOrDTO.setRawOccurrenceRecordDTO(rorDTO);
			return extOrDTO;
		}
		return null;
	}

	/**
	 * @param occurrenceRecordDTOFactory the occurrenceRecordDTOFactory to set
	 */
	public void setOccurrenceRecordDTOFactory(
			OccurrenceRecordDTOFactory occurrenceRecordDTOFactory) {
		this.occurrenceRecordDTOFactory = occurrenceRecordDTOFactory;
	}

	/**
	 * @param rawOccurrenceRecordDTOFactory the rawOccurrenceRecordDTOFactory to set
	 */
	public void setRawOccurrenceRecordDTOFactory(
			RawOccurrenceRecordDTOFactory rawOccurrenceRecordDTOFactory) {
		this.rawOccurrenceRecordDTOFactory = rawOccurrenceRecordDTOFactory;
	}
}