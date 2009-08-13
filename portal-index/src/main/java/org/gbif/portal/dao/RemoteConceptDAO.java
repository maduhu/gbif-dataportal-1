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
package org.gbif.portal.dao;

import java.util.List;

import org.gbif.portal.model.RemoteConcept;

/**
 * The Remote Concept DAO
 * @author Donald Hobern
 */
public interface RemoteConceptDAO {
	/**
	 * Creates the record
	 * @param RemoteConcept To create
	 * @return The id of the created record
	 */
	public long create(RemoteConcept remoteConcept);
	
	/**
	 * Creates the record if the given record has no ID, otherwise will update
	 * @param RemoteConcept To create or update
	 * @return The id of the created record
	 */
	public long updateOrCreate(RemoteConcept remoteConcept);
	
	/**
	 * Deletes the record
	 * @param RemoteConcept To delete
	 */
	public void delete(RemoteConcept remoteConcept);
	
	/**
	 * Gets the records associated with the specified TaxonConcept
	 * @param taxonConceptId identifier of TaxonConcept
	 * @return The records or empty list
	 */
	public List<RemoteConcept> findByTaxonConceptId(long taxonConceptId);

	/**
	 * Gets the records associated with the specified remote id and data resource id
	 * @param remoteId identifier
	 * @param idType identifier type
	 * @param dataResource Id
	 * @return The records or empty list
	 */
	public List<RemoteConcept> findByRemoteIdAndIdTypeAndDataResourceId(String remoteId, long idType, long dataResourceId);

	/**
	 * Remove old remote concepts 
	 * 
	 * @param timer Earliest timestamp for keeping remote concepts for a resource
	 */
	void deleteRemoteConceptsOlderThan(Long dataResourceId, Long timer);
}