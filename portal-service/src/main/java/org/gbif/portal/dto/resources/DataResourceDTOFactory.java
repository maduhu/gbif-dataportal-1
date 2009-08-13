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

package org.gbif.portal.dto.resources;

import org.gbif.portal.dto.BaseDTOFactory;
import org.gbif.portal.model.occurrence.BasisOfRecord;
import org.gbif.portal.model.resources.DataProvider;
import org.gbif.portal.model.resources.DataResource;
import org.gbif.portal.model.taxonomy.TaxonRank;
import org.springframework.beans.BeanUtils;

/**
 * Creates DataResourceDTO from DataResource model objects.
 *
 * @author Dave Martin
 */
public class DataResourceDTOFactory extends BaseDTOFactory {

	/**
	 * @see org.gbif.portal.dto.DTOFactory#createDTO(java.lang.Object)
	 */
	public Object createDTO(Object modelObject) {
		if(modelObject==null)
			return null;		
		DataResource dataResource = (DataResource) modelObject;
		DataResourceDTO dataResourceDTO = new DataResourceDTO();
		String[] ignores= new String[]{"key", "dataProviderKey", "dataProviderName", "basisOfRecord", "rootTaxonRank"};
		BeanUtils.copyProperties(dataResource, dataResourceDTO, ignores);
		dataResourceDTO.setKey(dataResource.getId().toString());
		DataProvider dataProvider = dataResource.getDataProvider();
		if(dataProvider!=null){
			dataResourceDTO.setDataProviderKey(dataProvider.getId().toString());		
			dataResourceDTO.setDataProviderName(dataProvider.getName());
		}
		if(dataResource.getBasisOfRecord()!=null){
			dataResourceDTO.setBasisOfRecord(dataResource.getBasisOfRecord().getName());
		} else {
			dataResourceDTO.setBasisOfRecord(BasisOfRecord.UNKNOWN.getName());
		}
		TaxonRank rootTaxonRank = dataResource.getRootTaxonRank();
		if (rootTaxonRank != null) {
			dataResourceDTO.setRootTaxonRank(rootTaxonRank.getName());
		}
		return dataResourceDTO;
	}
}
