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
package org.gbif.portal.dto.occurrence;

import org.gbif.portal.model.occurrence.OccurrenceRecord;

/**
 * Extends BriefOccurrenceRecordDTOFactory populating additional fields
 * for the drilldown DTO OccurrenceRecordDTO.
 * 
 * @author dmartin
 */
public class OccurrenceRecordDTOFactory extends BriefOccurrenceRecordDTOFactory {

	/**
	 * @see org.gbif.portal.dto.occurrence.BriefOccurrenceRecordDTOFactory#createDTO(java.lang.Object)
	 */
	@Override
	public Object createDTO(Object modelObject) {
		if(modelObject==null)
			return null;
		OccurrenceRecord or = (OccurrenceRecord) modelObject;
		OccurrenceRecordDTO dto = new OccurrenceRecordDTO();
		populateOccurrenceRecord(or, dto);
		return dto;
	}
	
	/**
	 * Copy properties from model object to dto.
	 * @param or
	 * @param dto
	 */
	public void populateOccurrenceRecord(OccurrenceRecord or, OccurrenceRecordDTO dto){
		populateBriefOccurrenceRecord(or, dto);
		//set additional fields
//		dto.setRawOccurrenceRecordKey(or.getRawOccurrenceRecordId().toString());
//		dto.setLocality(or.getLocality());
//		dto.setProviderIsoCountryCode(or.getProviderIsoCountryCode());
		if(or.getNubTaxonConceptId()!=null)
			dto.setNubTaxonConceptKey(or.getNubTaxonConceptId().toString());
		// dto.setNubTaxonNameKey(or.getNubTaxonName().getId().toString());
		if (or.getCountry() != null) {
			dto.setRegion(or.getCountry().getRegion());
		}
		dto.setCellId(or.getCellId());
		dto.setCentiCellId(or.getCentiCellId());
		dto.setAltitudeInMetres(or.getAltitudeInMetres());
		
		if(or.getDepthInCentimetres()!=null){
			if(or.getDepthInCentimetres()!=0){
				dto.setDepthInMetres(or.getDepthInCentimetres().floatValue() / 100);
			} else {
				dto.setDepthInMetres(0f);
			}
		} 
//		dto.setLatLongPrecision(or.getLatLongPrecision());
//		dto.setDepthInMeters(or.getDepthInMeters());
//		dto.setDepthPrecision(or.getDepthPrecision());
//		dto.setAltitudeInMeters(or.getAltitudeInMeters());
//		dto.setAltitudePrecision(or.getAltitudePrecision());
//		dto.setCollectorName(or.getCollectorName());
//		dto.setLocality(or.getLocality());
//		dto.setModified(or.getModified());		
	}
}