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

package org.gbif.portal.dao.taxonomy;

import java.util.List;

import org.gbif.portal.model.taxonomy.CommonName;

/**
 * DAO for CommonName objects.
 * 
 * @author dmartin
 */
public interface CommonNameDAO {
	
	/**
	 * Retrieves the common name for the supplied id.
	 * @param commonNameId
	 * @return CommonName for this id
	 */
	public CommonName getCommonNameFor(long commonNameId);

	/**
	 * Find matching common names for the supplied partial name.
	 * 
	 * @param name
	 * @param fuzzy
	 * @param startIndex
	 * @param maxResults
	 * @return list of common names
	 */
	public List<String> findCommonNames(String name, boolean fuzzy, int startIndex, int maxResults);

	/**
	 * Retrieve common names for the supplied taxon name.
	 * 
	 * @param name
	 * @param fuzzy
	 * @param startIndex
	 * @param maxResults
	 * @return
	 */
	public List<CommonName> findCommonNamesFetchingCorrespondingTaxonName(String name, boolean fuzzy, int startIndex, int maxResults);
	
	/**
	 * Retrieve common names for the supplied taxon concept id.
	 * 
	 * @param taxonConceptId
	 * @param fuzzy
	 * @param startIndex
	 * @param maxResults
	 * @return
	 */
	public List<CommonName> getCommonNamesFor(long taxonConceptId, boolean fuzzy, int startIndex, int maxResults);

}