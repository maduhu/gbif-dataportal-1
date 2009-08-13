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
package org.gbif.portal.dto.geospatial;

import org.gbif.portal.dto.BaseDTOFactory;
import org.gbif.portal.model.geospatial.Country;
import org.gbif.portal.model.geospatial.CountryName;
import org.springframework.beans.BeanUtils;

/**
 * Creates CountryDTOs from model objects.
 * 
 * @author davemartin
 */
public class CountryDTOFactory extends BaseDTOFactory {

	/**
	 * Supports the creation of DTO for the following:
	 * 1) Object[] {Country, CountryName}
	 * 2) Object[] {Country, (String) countryName, (String) interpretedFrom}
	 * 
	 * @see org.gbif.portal.dto.DTOFactory#createDTO(java.lang.Object)
	 */
	public Object createDTO(Object modelObject) {
		if(modelObject==null)
			return null;
		Object[] countryAndName = (Object[]) modelObject;
		Country country = (Country) countryAndName[0];
		
		
		String[] ignores= new String[]{"key", "name"};
		CountryDTO countryDTO = new CountryDTO();
		BeanUtils.copyProperties(country, countryDTO, ignores);	
		countryDTO.setKey(country.getCountryId().toString());
		countryDTO.setContinentCode(country.getContinentCode());
		countryDTO.setRegion(country.getRegion());
		countryDTO.setMinLatitude(country.getMinLatitude());
		countryDTO.setMaxLatitude(country.getMaxLatitude());
		countryDTO.setMinLongitude(country.getMinLongitude());
		countryDTO.setMaxLongitude(country.getMaxLongitude());
		
		
		if(countryAndName[1] instanceof CountryName){
			CountryName countryName = (CountryName) countryAndName[1];
			countryDTO.setName(countryName.getName());
		} else if(countryAndName[1] instanceof String){
			countryDTO.setName((String) countryAndName[1]);
		}
		if(countryAndName.length>2){
			countryDTO.setInterpretedFrom((String)countryAndName[2]);
		}
		
		return  countryDTO;
	}
}