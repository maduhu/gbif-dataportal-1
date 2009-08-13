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
package org.gbif.portal.dao.taxonomy;

import java.util.List;

import org.gbif.portal.model.taxonomy.RemoteConcept;

/**
 * DAO interface for remote concepts.
 * 
 * @author dmartin
 */
public interface RemoteConceptDAO {
	
	/**
	 * Retrieve remote urls for this taxon concept.
	 * 
	 * @param taxonConceptId
	 * @return
	 */
	public List<String> findRemoteUrlFor(long taxonConceptId);
	
	/**
	 * Retrieve remote concepts for this taxon concept.
	 * 
	 * @param taxonConceptId
	 * @return
	 */
	public List<RemoteConcept> findRemoteConceptsFor(long taxonConceptId);
}
