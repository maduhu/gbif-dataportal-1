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
import org.gbif.portal.model.resources.Agent;
import org.springframework.beans.BeanUtils;

/**
 * Creates DataProviderDTO from DataProvider model objects.
 *
 * @author Donald Hobern
 */
public class AgentDTOFactory extends BaseDTOFactory {

	/**
	 * @see org.gbif.portal.dto.DTOFactory#createDTO(java.lang.Object)
	 */
	public Object createDTO(Object modelObject) {
		if(modelObject==null)
			return null;
		Agent agent = (Agent) modelObject;
		AgentDTO agentDTO = new AgentDTO();
		String[] ignores= new String[]{"key"};
		BeanUtils.copyProperties(agent, agentDTO, ignores);
		agentDTO.setKey(agent.getId().toString());
		return agentDTO;
	}
}
