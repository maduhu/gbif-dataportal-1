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
import org.gbif.portal.model.resources.DataResourceAgent;

/**
 * Creates DataProviderDTO from DataProvider model objects.
 *
 * @author Donald Hobern
 */
public class DataResourceAgentDTOFactory extends BaseDTOFactory {

	/**
	 * @see org.gbif.portal.dto.DTOFactory#createDTO(java.lang.Object)
	 */
	public Object createDTO(Object modelObject) {
		if(modelObject==null)
			return null;
		DataResourceAgent dataResourceAgent = (DataResourceAgent) modelObject;
		DataResourceAgentDTO dataResourceAgentDTO = new DataResourceAgentDTO();
		dataResourceAgentDTO.setKey(dataResourceAgent.getId().toString());
		dataResourceAgentDTO.setDataResourceKey(dataResourceAgent.getDataResource().getId().toString());
		dataResourceAgentDTO.setAgentKey(dataResourceAgent.getAgent().getId().toString());
		dataResourceAgentDTO.setAgentName(dataResourceAgent.getAgent().getName());
		dataResourceAgentDTO.setAgentAddress(dataResourceAgent.getAgent().getAddress());
		dataResourceAgentDTO.setAgentEmail(dataResourceAgent.getAgent().getEmail());
		dataResourceAgentDTO.setAgentTelephone(dataResourceAgent.getAgent().getTelephone());
		if(dataResourceAgent.getAgentType()!=null){
			dataResourceAgentDTO.setAgentType(dataResourceAgent.getAgentType().getValue());
			dataResourceAgentDTO.setAgentTypeName(dataResourceAgent.getAgentType().getName());
		}
		return dataResourceAgentDTO;
	}
}
