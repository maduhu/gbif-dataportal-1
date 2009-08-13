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

import org.gbif.portal.dto.BaseDTOFactory;
import org.gbif.portal.model.occurrence.RawOccurrenceRecord;
import org.springframework.beans.BeanUtils;

/**
 * Extends RawOccurrenceRecordDTOFactory 
 * for the RawOccurrenceRecordDTO.
 * 
 * @author dhobern
 */
public class RawOccurrenceRecordDTOFactory extends BaseDTOFactory {

	public Object createDTO(Object modelObject) {
		if(modelObject==null)
			return null;		
		RawOccurrenceRecord rawOccurrenceRecord = (RawOccurrenceRecord) modelObject;
		RawOccurrenceRecordDTO rawOccurrenceRecordDTO = new RawOccurrenceRecordDTO();
		String[] ignores= new String[]{"key", "dataProviderKey", "dataProviderName", "dataResourceKey", "dataResourceName", "resourceAccessPointKey"};
		BeanUtils.copyProperties(rawOccurrenceRecord, rawOccurrenceRecordDTO, ignores);
		
		//insert a formatted date into the DTO
		String dateString = null;
		String year = rawOccurrenceRecord.getYear(); 
		String month = rawOccurrenceRecord.getMonth(); 
		String day = rawOccurrenceRecord.getDay(); 
		if (year != null && month != null && day != null) {
			StringBuffer sb = new StringBuffer();
			if (year.length() == 4) { 
				sb.append(year);
				sb.append("-");
				if (month.length() == 1) {
					month = "0" + month;
				}
				if (month.length() == 2) {
					sb.append(month);
					sb.append("-");
					if (day.length() == 1) {
						day = "0" + day;
					}
					if (day.length() == 2) {
						sb.append(day);
						dateString = sb.toString();
					}
				}
			}
		}
		rawOccurrenceRecordDTO.setOccurrenceDate(dateString);		
		
		rawOccurrenceRecordDTO.setKey(rawOccurrenceRecord.getId().toString());
		rawOccurrenceRecordDTO.setDataProviderKey(rawOccurrenceRecord.getDataProviderId().toString());
		if(rawOccurrenceRecord.getDataProvider()!=null){
			rawOccurrenceRecordDTO.setDataProviderName(rawOccurrenceRecord.getDataProvider().getName());
		}
		rawOccurrenceRecordDTO.setDataResourceKey(rawOccurrenceRecord.getDataResourceId().toString());
		if(rawOccurrenceRecord.getDataResource()!=null){
			rawOccurrenceRecordDTO.setDataResourceName(rawOccurrenceRecord.getDataResource().getName());
		}
		rawOccurrenceRecordDTO.setResourceAccessPointKey(rawOccurrenceRecord.getResourceAccessPointId().toString());
		return rawOccurrenceRecordDTO;
	}
}