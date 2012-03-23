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
package org.gbif.portal.dto.taxonomy;

import org.gbif.portal.dto.BaseDTOFactory;
import org.gbif.portal.model.taxonomy.CommonName;

/**
 * A DTOFactory for Common Name objects. A Common Name is associated with a particular taxon concept.
 * 
 * @author Dave Martin
 */
public class CommonNameDTOFactory extends BaseDTOFactory {

	/**
	 * @see org.gbif.portal.dto.DTOFactory#createDTO(java.lang.Object)
	 */
	public Object createDTO(Object modelObject) {
		CommonName commonName = (CommonName) modelObject;
		CommonNameDTO commonNameDTO = new CommonNameDTO();
		commonNameDTO.setKey(commonName.getId().toString());
		commonNameDTO.setName(commonName.getName());
		commonNameDTO.setTaxonName(commonName.getTaxonConcept().getTaxonName().getCanonical());
		commonNameDTO.setTaxonConceptKey(String.valueOf(commonName.getTaxonConceptId()));
		//iso language code not being set at the minute
		//commonNameDTO.setLanguage(commonName.getIsoLanguageCode());
		commonNameDTO.setLanguage(commonName.getLanguage());
		return commonNameDTO;
	}
}