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

/**
 * Defines the data access operations that are required for denormalising a taxonomy
 * @author trobertson
 */
public interface TaxonomyDenormaliserDAO {
	/**
	 * Clears all the denormalised data 
	 * @param resourceId To clear
	 */
	public void clearDenormalisedDataForResource(long resourceId);
	
	/**
	 * Gets the ranks for the resource
	 * @param resourceId
	 * @return The ranks
	 */
	public List<Integer> getDistinctRanksForResource(long providerId);
	
	/**
	 * Gets the ranks for the provider
	 * @param providerId
	 * @return The ranks
	 */
	public List<Integer> getDistinctRanksForProvider(long providerId);
	
	/**
	 * Clears all the denormalised data 
	 * @param providerId To clear
	 */
	public void clearDenormalisedDataForProvider(long providerId);
	
	/**
	 * Copies the parent denormalised values for the provider, taking into account it's rank to not
	 * override anything that it may have (E.g. if it is a major rank, then it's self pointer will not be 
	 * overridden)
	 * @param providerId To work within
	 * @param rankOfConcept The rank of the child concept
	 */
	public void copyParentDenormalisationForRankAndForProvider(long providerId, int rankOfConcept);
	
	/**
	 * Copies the parent denormalised values for the provider, taking into account it's rank to not
	 * override anything that it may have (E.g. if it is a mafor rank, then it's self pointer will not be 
	 * overridden)
	 * @param providerId To work within
	 * @param rankOfConcept The rank of the child concept
	 */
	public void copyParentDenormalisationForRankAndForResource(long providerId, int rankOfConcept);
}
