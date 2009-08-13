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
import org.gbif.portal.model.taxonomy.RemoteConcept;

/**
 * A DTOFactory for Remote Concept objects. A Remote Concept is associated with a particular taxon concept.
 * 
 * @author Donald Hobern
 */
public class RemoteConceptDTOFactory extends BaseDTOFactory {

	/**
	 * @see org.gbif.portal.dto.DTOFactory#createDTO(java.lang.Object)
	 */
	public Object createDTO(Object modelObject) {
		RemoteConcept remoteConcept = (RemoteConcept) modelObject;
		RemoteConceptDTO remoteConceptDTO = new RemoteConceptDTO();
		remoteConceptDTO.setKey(remoteConcept.getId().toString());
		remoteConceptDTO.setRemoteId(remoteConcept.getRemoteId());
		remoteConceptDTO.setTaxonConceptKey(String.valueOf(remoteConcept.getTaxonConceptId()));
		remoteConceptDTO.setIdType(remoteConcept.getIdType());
		return remoteConceptDTO;
	}
}