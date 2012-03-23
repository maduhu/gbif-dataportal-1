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

import org.gbif.portal.model.TaxonConcept;
import org.gbif.portal.model.TaxonConceptLite;
import org.gbif.portal.model.TaxonName;

/**
 * Defines the data access operations related to the taxon concept
 * @author trobertson
 */
public interface TaxonConceptDAO {

	/**
 	 * Special priority value for concepts created only to allow for disambiguation between other concepts
 	 */
	public static final int DISAMBIGUATION_PRIORITY = 10000;

	/**
	 * Returns the List of classifications for the given canonical, author and rank according to the 
	 * data resource - must not return disambiguation concepts
	 * @param canonical Name
	 * @param author Which can be null
	 * @param rank That should be used for the name
	 * @param dataResourceId Within which to search
	 * @return The list of classification lists
	 */
	List<List<TaxonName>> getClassificationsOf(String canonical, String author, int rank, long dataResourceId);
	
	/**
	 * Returns the List of classifications for the given canonical and rank according to the 
	 * data resource - must not return disambiguation concepts
	 * @param canonical Name
	 * @param rank That should be used for the name
	 * @param dataResourceId Within which to search
	 * @return The list of classification lists - Note, this is a list of TaxonConcept
	 */
	List<List<TaxonConceptLite>> getClassificationsOf(String canonical, int rank, long dataResourceId);
	
	/**
	 * Returns the List of classifications for the given rank according to the data resource.  Can be controlled to 
	 * return anything of lower rank also - must not return disambiguation concepts
	 * @param rank That should be used for the search
	 * @param dataResourceId Within which to search
	 * @param includeLowerTaxa True if you wish Taxa that are lower to be returned
	 * @param minimumId The minimum id to search from
	 * @param maxResults To limit the results
	 * @return The list of classification lists
	 */
	List<List<TaxonConceptLite>> getClassificationsOf(int rank, long dataResourceId, boolean includeLowerTaxa, long minimumId, int maxResults);
	
	/**
	 * Returns the List of classifications for the given rank according to the data resource.  Can be controlled to 
	 * return anything of lower rank also - must not return disambiguation concepts
	 * @param rank That should be used for the search
	 * @param dataResourceId Within which to search
	 * @param includeLowerTaxa True if you wish Taxa that are lower to be returned
	 * @param accepted Whether the concepts are accepted or not 
	 * @param minimumId The minimum id to search from
	 * @param maxResults To limit the results
	 * @return The list of classification lists
	 * @return
	 */
	List<List<TaxonConceptLite>> getClassificationsOf(int rank, long dataResourceId, boolean includeLowerTaxa, boolean accepted, long minimumId, int maxResults);
	
	
	/**
	 * Returns the List of classifications for the given rank according to the data resource.  Can be controlled to 
	 * return anything of lower rank also - must not return disambiguation concepts - and will allow you to specify only unpartnered
	 * concepts only if required 
	 * @param rank That should be used for the search
	 * @param dataResourceId Within which to search
	 * @param includeLowerTaxa True if you wish Taxa that are lower to be returned
	 * @param accepted Whether the concepts are accepted or not 
	 * @param minimumId The minimum id to search from
	 * @param maxResults To limit the results
	 * @param unpartneredOnly To limit the results to only non partnered concepts 
	 * @return The list of classification lists
	 * @return
	 */
	List<List<TaxonConceptLite>> getClassificationsOf(int rank, long dataResourceId, boolean includeLowerTaxa, boolean accepted, long minimumId, int maxResults, boolean unpartneredOnly);
	
	/**
	 * Returns the List of classifications for the given canonical, author and rank according to the 
	 * data provider - must not return disambiguation concepts
	 * @param canonical Name
	 * @param author Which can be null
	 * @param rank That should be used for the name
	 * @param dataProviderId Within which to search
	 * @return The list of classification lists - Note, this is a list of TaxonConcept
	 */
	List<List<TaxonConceptLite>> getClassificationsWithinProviderOf(String canonical, String author, int rank, long dataProviderId);
	
	/**
	 * Returns the List of classifications for the given canonical and rank according to the 
	 * data provider - must not return disambiguation concepts 
	 * This is more lenient than than the method using the author
	 * @param canonical Name
	 * @param rank That should be used for the name
	 * @param dataProviderId Within which to search
	 * @return The list of classification lists - Note, this is a list of TaxonConcept
	 */
	List<List<TaxonConceptLite>> getClassificationsWithinProviderOf(String canonical, int rank, long dataProviderId);
	
	/**
	 * Find any existing disambiguation concept for the given name and resource
	 * @param canonical
	 * @param dataResourceId
	 * @return
	 */
	TaxonConcept getDisambiguationConcept(String canonical, long dataResourceId);
	
	/**
	 * Gets the list of distinct ranks found with the resource
	 * @param dataResourceId To get the ranks of
	 * @return The list of ranks or an empty list
	 */
	List<Integer> getRanksWithinResource(long dataResourceId);
	
	/**
	 * Gets the list of distinct ranks found with the resource that have concepts
	 * that are unpartnered
	 * @param dataResourceId To get the ranks of
	 * @return The list of ranks or an empty list
	 */
	List<Integer> getUnpartneredRanksWithinResource(long dataResourceId);
	
	/**
	 * Gets a classification List for the concept id 
	 * E.g. this will return the ordered higher taxa down to the provided concept
	 * E.g. Pass in Puma and you may return "Animalia, Chordata, Puma" 
	 * @param conceptId To get the classification of
	 * @return The ordered classification of TaxonName
	 * @deprecated This will be deprecated with preference to use of getClassificationConcepts
	 */
	public List<TaxonName> getClassification(long conceptId);
	
	/**
	 * Gets a classification list for the concept id but returning the TaxonConceptLite
	 * @param conceptId To get the classification of
	 * @return The ordered classification list containing the TaxonConcepts
	 */
	public List<TaxonConceptLite> getClassificationConcepts(long conceptId);
	
	/**
	 * Get's a lite taxon concept for the id provided 
	 * @param conceptId To get the lite concept of
	 * @return The lite TaxonConcept
	 */
	public TaxonConceptLite getTaxonConceptLite(long conceptId);
	
	/**
	 * Get's a taxon concept for the id provided 
	 * @param conceptId To get the concept of
	 * @return The TaxonConcept
	 */
	public TaxonConcept getTaxonConcept(long conceptId);
	
	/**
	 * Creates the taxon concept, and it's taxon name if it needs creating
	 * @param concept To create
	 * @return The id of the created concept
	 */
	public long create(TaxonConcept concept);

	/**
	 * Creates the taxon concept, and it's taxon name if it needs creating
	 * @param concept To create
	 * @return The id of the created concept
	 */
	public void remove(long conceptId);	
	
	/**
	 * Gets a taxon concept with the given parameters, including one with a parent in the given list
	 * @param canonical Name 
	 * @param author Name
	 * @param rank For the concept
	 * @param dataResourceId Within which is being worked
	 * @param parentIds Searches for a concept with a parent id in the list, if there is a list provided
	 * @return The concept or null
	 */
	public TaxonConcept getTaxonConcept(String canonical, String author, int rank, long dataResourceId, List<Long> parentIds);
	
	/**
	 * Gets a taxon concept with the given parameters, including one with a parent in the given list
	 * @param canonical Name 
	 * @param rank For the concept
	 * @param dataResourceId Within which is being worked
	 * @param parentIds Searches for a concept with a parent id in the list, if there is a list provided
	 * @return The concept or null
	 */
	public TaxonConcept getTaxonConcept(String canonical, int rank, long dataResourceId, List<Long> parentIds);

	/**
	 * Gets the taxon concepts with the given parameters
	 * @param canonical Name 
	 * @param dataResourceId Within which is being worked
	 * @return The concepts or empty list
	 */
	public List<TaxonConcept> getTaxonConcepts(String canonical, long dataResourceId);	
	
	/**
	 * Hi speed update
	 * @param targetConcept that is being altered
	 * @param newParentId New parent id
	 */
	public void updateParent(long targetConcept, long newParentId);
	
	public void updateParentWhereParentId(long oldParentId, long newParentId);
	
	/**
	 * Hi speed update
	 * @param targetConcept that is being altered
	 * @param newNubConceptId New partner id
	 */
	public void updatePartnerConcept(long targetConcept, long newNubConceptId);
	
	/**
	 * Hi speed update
	 * @param targetConcept To update
	 * @param newParentId New parent
	 * @param kingdomConceptId The id
	 * @param phylumConceptId The id
	 * @param classConceptId The id
	 * @param orderConceptId The id
	 * @param familyConceptId The id
	 * @param genusConceptId The id
	 * @param speciesConceptId The id
	 */
	public void updateParentAndDenormalised(long targetConcept, 
			long newParentId,
			Long kingdomConceptId,
			Long phylumConceptId,
			Long classConceptId,
			Long orderConceptId,
			Long familyConceptId,
			Long genusConceptId,
			Long speciesConceptId);
	
	/**
	 * Hi speed update
	 * @param targetConcept that is being altered
	 * @param id To set
	 */
	public void updateKingdomConcept(Long targetConcept, long id);
	
	/**
	 * Hi speed update
	 * @param targetConcept that is being altered
	 * @param id To set
	 */
	public void updatePhylumConcept(Long targetConcept, long id);
	
	/**
	 * Hi speed update
	 * @param targetConcept that is being altered
	 * @param id To set
	 */
	public void updateClassConcept(Long targetConcept, long id);
	
	/**
	 * Hi speed update
	 * @param targetConcept that is being altered
	 * @param id To set
	 */
	public void updateOrderConcept(Long targetConcept, long id);
	
	/**
	 * Hi speed update
	 * @param targetConcept that is being altered
	 * @param id To set
	 */
	public void updateFamilyConcept(Long targetConcept, long id);
	
	/**
	 * Hi speed update
	 * @param targetConcept that is being altered
	 * @param id To set
	 */
	public void updateGenusConcept(Long targetConcept, long id);
	
	/**
	 * Hi speed update
	 * @param targetConcept that is being altered
	 * @param id To set
	 */
	public void updateSpeciesConcept(Long targetConcept, long id);
	
	/**
	 * Hi speed update
	 * @param targetConcept that is being altered
	 * @param rank To set
	 */
	public void updateRank(Long targetConcept, long rank);

	/**
	 * Link taxon concepts with a parent taxon concept 
	 * 
	 * @param parentConceptId
	 * @param childConceptIds
	 */
	public void linkTaxonConceptsToParent(Long parentConceptId, List<Long> childConceptIds);

	/**
 	 * If taxon_name and taxon_concept records have rank 0 but their parents have
	 * assigned ranks, set the rank to a value lower than the parent rank.
	 * @param dataResourceId
	 * @return true if records are changed
	 */
	boolean updateUnknownRanks(Long dataResourceId);

	/**
	 * Set the secondary flag on all taxon concepts within a resource which do not have any
	 * associated occurrence records  
	 * @param dataResourceId
	 */
	void markConceptsWithoutOccurrenceRecordsAsSecondary(long dataResourceId);

	/**
	 * Set the is_accepted flag for a concept
	 * @param taxonConceptId
	 * @param accepted
	 */
	void updateAccepted(Long taxonConceptId, boolean accepted);

	/**
	 * Get the taxon concept associated with the given data provider id and remote id 
	 * @param dataProviderId
	 * @param nameCode
	 * @return
	 */
	TaxonConceptLite getTaxonConceptByDataProviderIdAndRemoteId(Long dataProviderId, String nameCode);
}
