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

import org.gbif.portal.model.TaxonName;

/**
 * Defines the data access operations related to the taxon name
 * @author trobertson
 */
public interface TaxonNameDAO {
	/**
	 * Creates the record
	 * @param taxonName To create
	 * @return The id of the created record
	 */
	public long create(TaxonName taxonName);
	
	/**
	 * Creates the record or updates it if the ID is set
	 * @param taxonName To create or update
	 * @return The id of the created record
	 */
	public long createOrUpdate(TaxonName taxonName);
	
	/**
	 * Deletes the record
	 * @param taxonName To delete
	 */
	public void delete(long id);
	
	/**
	 * Get a unique TaxonName
	 * @param canonical Canonical form 
	 * @param author Author
	 * @param rank Rank 
	 * @return The unique name
	 */
	public TaxonName getUnique(String canonical, String author, int rank);
		
	/**
	 * Get TaxonName by id
	 * @param id TaxonName id 
	 * @return The name
	 */
	public TaxonName getById(long id);
	
	/**
	 * Sets the searchable canonical
	 * @param id To set on
	 * @param searchableCanonical The new value
	 */
	public void setSearchableCanonical(long id, String searchableCanonical);
	
	/**
	 * For paging through all taxon names
	 * @param start To start at
	 * @param limit The limit
	 * @return The list of names
	 */
	public List<TaxonName> getTaxonName(long start, int limit);
	
	/**
	 * Gets the names by the canonical down to the lowest rank provided (e.g. Kingdom to Genus)
	 * @param canonical To search for
	 * @param lowestRankInclusive The lowest rank
	 * @return The list of names
	 */
	public List<TaxonName> getByCanonicalAndLowestRank(String canonical, int lowestRankInclusive);
		
}
