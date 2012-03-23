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

import org.gbif.portal.model.CommonName;

/**
 * Defines the data access operations related to the taxon name
 * @author Donald Hobern
 */
public interface CommonNameDAO {
	/**
	 * Creates the record
	 * @param commonName To create
	 * @return The id of the created record
	 */
	public long create(CommonName commonName);
	
	/**
	 * Creates the record or updates it if the ID is set
	 * @param commonName To create or update
	 * @return The id of the created record
	 */
	public long createOrUpdate(CommonName commonName);
	
	/**
	 * Deletes the record
	 * @param commonName To delete
	 */
	public void delete(long id);
	
	/**
	 * Get a unique CommonName
	 * @param canonical Canonical form 
	 * @param author Author
	 * @param rank Rank 
	 * @return The unique name
	 */
	public CommonName getUnique(Long taxonConceptId, String name, String language);
		
	/**
	 * Get CommonName by id
	 * @param id CommonName id 
	 * @return The name
	 */
	public CommonName getById(long id);
		
}
