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

import org.gbif.portal.model.taxonomy.TaxonName;
import org.gbif.portal.model.taxonomy.TaxonRank;

/**
 * The DAO for the TaxonName model object
 * 
 * @author dbarnier
 */
public interface TaxonNameDAO {

	/**
	 * Find matching scientific names.
	 * 
	 * @param name the name to search for
	 * @param taxonRank the taxonomic rank to search against
	 * @param soundex whether to use a soundex
	 * @param startIndex
	 * @param maxResults
	 * @return a results dto of strings
	 */
	public List<String> findScientificNames(String name, boolean fuzzy, TaxonRank taxonRank, Boolean higherThanRankSupplied, boolean soundex, int startIndex, int maxResults);	
	
	/**
	 * Find matching scientific names within a taxonomy.
	 * 
	 * @param name the name to search for
	 * @param taxonRank the taxonomic rank to search against
	 * @param soundex whether to use a soundex
	 * @param dataProviderId
	 * @param dataResourceId
	 * @param startIndex
	 * @param maxResults
	 * @return a results dto of strings
	 */
	public List<String> findScientificNamesInTaxonomy(String name, boolean fuzzy, TaxonRank taxonRank, Boolean higherThanRankSupplied, boolean soundex, Long dataProviderId, Long dataResourceId, boolean allowUnconfirmed, int startIndex, int maxResults);	
	
	/**
	 * Retrieve a taxon name for the supplied id.
	 * 
	 * @param taxonNameId The id to retrieve the name for
	 * @return The taxonName
	 */
	public TaxonName getTaxonNameFor(long taxonNameId);
}