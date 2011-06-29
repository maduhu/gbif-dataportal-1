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
package org.gbif.portal.service;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.gbif.portal.dto.CountDTO;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.occurrence.TypificationRecordDTO;
import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.dto.taxonomy.CommonNameDTO;
import org.gbif.portal.dto.taxonomy.RelationshipAssertionDTO;
import org.gbif.portal.dto.taxonomy.RemoteConceptDTO;
import org.gbif.portal.dto.taxonomy.TaxonConceptDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.dto.util.TaxonRankType;

/**
 * Service interface for Taxonomic Interface methods. This is the public interface
 * providing methods for retrieving Taxonomic Concepts that have been indexed within the portal.
 * 
 * These methods will for the most part return TaxonConceptDTO or a SearchResultsDTO which contains
 * a collection of TaxonConceptDTOs.
 * 
 * ServiceExceptions are thrown by these methods to indicate a failure to retrieve the data due to
 * a network/database connection or a data integrity problem. ServiceExceptions are not thrown to indicate
 * for example a bad argument value.
 * 
 * The Nub taxonomy is the taxonomy that is the authorative taxonomy for a particular deployment of the portal.
 * 
 * Search methods in general require a SearchConstraints object and return a SearchResultsDTO. This is to
 * enable limiting results and paging through results (see <code>SearchConstraints.startIndex</code> 
 * and <code>SearchConstraints.maxResults</code> ).
 * 
 * @see BriefTaxonConceptDTO
 * @see TaxonConceptDTO
 * @see CommonNameDTO
 * @see SearchResultsDTO
 * 
 * @author dmartin
 */
public interface TaxonomyManager {
	
	/**
	 * Returns the TaxonConcept for the specified key value. Returns null if there is not
	 * a Taxon Concept for the supplied key or if the supplied key is invalid. For queries that only
	 * require a name or parent id please use <code>getBriefTaxonConceptFor</code> for performance
	 * reasons.
	 * 
	 * @param taxonConceptKey The taxon concept key 
	 * @return TaxonConceptDTO containing details of this concept, null if unable to find concept for this key
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public TaxonConceptDTO getTaxonConceptFor(String taxonConceptKey) throws ServiceException;
	
	/**
	 * Returns the TaxonConcept for the specified key value. Returns null if there is not
	 * a Taxon Concept for the supplied key or if the supplied key is invalid. For queries that only
	 * require a name or parent id please use <code>getBriefTaxonConceptFor</code> for performance
	 * reasons.
	 * 
	 * @param taxonConceptKey The taxon concept key 
	 * @param isoLanguageCode To preferentially build the details based on a language (e.g. common name) 
	 * @return TaxonConceptDTO containing details of this concept, null if unable to find concept for this key
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public TaxonConceptDTO getTaxonConceptFor(String taxonConceptKey, String isoLanguageCode) throws ServiceException;

	/**
	 * Returns a brief version TaxonConcept for the specified key value. Returns null if there is not
	 * a Taxon Concept for the supplied key or if the supplied key is invalid.
	 * This should be used if the user only requires a small amount of information on this taxon.
	 * 
	 * @param taxonConceptKey The taxon concept key 
	 * @return TaxonConceptDTO containing details of this concept, null if unable to find concept for this key
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public BriefTaxonConceptDTO getBriefTaxonConceptFor(String taxonConceptKey) throws ServiceException;	
	
	/**
	 * Returns the TaxonConcepts for the specified key value. Returns null if there is not
	 * a Taxon Concept for the supplied remote Id or if the supplied key is invalid.
	 * 
	 * @param remoteId The remote Id
	 * @return List<TaxonConceptDTO> containing details of this concepts, null if unable to find concept for this key
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public List<TaxonConceptDTO> getTaxonConceptForRemoteId(String remoteId) throws ServiceException;	

	/**
	 * Returns the TaxonConcepts for the specified key value. Returns null if there is not
	 * a Taxon Concept for the supplied remote Id or if the supplied key is invalid.
	 * 
	 * @param dataProviderKey
	 * @param dataResourceKey
	 * @param remoteId The remote Id
	 * @return List<TaxonConceptDTO> containing details of this concepts, null if unable to find concept for this key
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public TaxonConceptDTO getTaxonConceptForRemoteId(String dataProviderKey, String dataResourceKey, String remoteId) throws ServiceException;	
	
	/**
	 * Returns the Nub Taxon Concepts for the specified TaxonConcept. This is the concept that is accepted as the
	 * authoritive concept within this portal.
	 * 
	 * @param taxonConceptKey The taxon concept key
	 * @return TaxonConceptDTO the Nub TaxonConcept for this concept, null if does not exist 
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public TaxonConceptDTO getNubTaxonConceptFor(String taxonConceptKey) throws ServiceException;	
	
	/**
	 * Returns the Taxon Concepts associated with the specified nub TaxonConcept.
	 * 
	 * @param nubConceptKey The nub taxon concept key
	 * @return List<TaxonConceptDTO> list of TaxonConcepts for this nub concept 
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public List<TaxonConceptDTO> getTaxonConceptsForNubTaxonConcept(String nubConceptKey) throws ServiceException;	

	/**
	 * Returns the Taxon Concepts for the specified TaxonConcept. This is the concept that is accepted as the
	 * authoritive concept within this portal.
	 * 
	 * @param taxonConceptKey The taxon concept key
	 * @return TaxonConceptDTO the Nub TaxonConcept for this concept, null if does not exist 
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public List<TaxonConceptDTO> getAuthoritativeTaxonConceptsForNubTaxonConcept(String nubConceptKey) throws ServiceException;

	/**
	 * Returns the Parent TaxonConcept for the TaxonConcept with the specified key value. 
	 * Returns null if there is not a Parent Taxon Concept for the supplied key or if the supplied key 
	 * is invalid.
	 * 
	 * @param taxonConceptKey The taxon concept key 
	 * @return TaxonConceptDTO containing ParentConcept details, null if Parent Concept does not exist
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public TaxonConceptDTO getParentConceptFor(String taxonConceptKey) throws ServiceException;	
	
	/**
	 * Returns the Child Concepts for the TaxonConcept with the specified key value.
	 * 
	 * @param taxonConceptKey The taxon concept key 
	 * @return list of BriefTaxonConceptDTO objects for the Child Concepts
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public List<BriefTaxonConceptDTO> getChildConceptsFor(String taxonConceptKey, boolean allowUnconfirmed) throws ServiceException;		

	/**
	 * Returns the Child Concepts for the TaxonConcept with the specified key value.
	 * 
	 * @param taxonConceptKey The taxon concept key 
	 * @return list of BriefTaxonConceptDTO objects for the Child Concepts
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public List<BriefTaxonConceptDTO> getChildConceptsFor(String taxonConceptKey, String isoCountryCode, boolean allowUnconfirmed) throws ServiceException;		
	
	/**
	 * Returns the full classication given the supplied taxon concept key. 
	 * This will include all ascendents and all direct descendents if descend is true.
	 * The list is order by rank in descending order (starting with highest rank).
	 * When the supplied taxonConceptKey is null, the highest concepts for a Data resource or provider
	 * will be returned
	 * 
	 * @param dataResourceKey the key of the data resource to use
	 * @param taxonConceptKey the key of a taxon concept to retrieve the classification for, nullable.
	 * @param retrieveChildren if set to true will gather child concepts as well as parent concepts
	 * @param isoCountryCode if retrieveChildren is set to true, setting isoCountryCode will only bring back child concept with occurrence data in this country
	 * @return List of BriefTaxonConcept for the full tree for this concept
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */	
	public List<BriefTaxonConceptDTO> getClassificationFor(String taxonConceptKey, boolean includeHigherTaxa, boolean retrieveChildren, 
			String isoCountryCode, boolean retrieveChildCounts, boolean allowUnconfirmed) throws ServiceException; 

	/**
	 * Returns the full classification given the supplied taxon concept key. 
	 * This will include all ascendents and all direct descendents if descend is true.
	 * The list is order by rank in descending order (starting with highest rank).
	 * When the supplied taxonConceptKey is null, the highest concepts for a Data resource or provider
	 * will be returned
	 * 
	 * @param dataResourceKey the key of the data resource to use
	 * @param taxonConceptKey the key of a taxon concept to retrieve the classification for, nullable.
	 * @param retrieveChildren if set to true will gather child concepts as well as parent concepts
	 * @param isoCountryCode if retrieveChildren is set to true, setting isoCountryCode will only bring back child concept with occurrence data in this country
	 * @return List of BriefTaxonConcept for the full tree for this concept
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */	
	public List<BriefTaxonConceptDTO> getClassificationFor(String taxonConceptKey, boolean retrieveChildren, String isoCountryCode, 
			boolean allowUnconfirmed) throws ServiceException; 
	
	/**
	 * Returns a list of TaxonConcepts that are the root concepts for the taxonomy of the 
	 * data provider or data resource provided.
	 * 
	 * A root concept is a concept with no parent concept. This would typically be a higher concept
	 * of rank kingdom for example.
	 * 
	 * @param dataProviderKey The Data Provider to search, nullable
	 * @param dataResourceKey The Data Resource to search, nullable
	 * @return List of BriefTaxonConceptDTOs ordered alphabetically by Scientific Name  
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public List<BriefTaxonConceptDTO> getRootTaxonConceptsForTaxonomy(String dataProviderKey, String dataResourceKey) throws ServiceException;		

	/**
	 * Returns a list of TaxonConcepts that are the root concepts for the taxonomy of a country
	 * 
	 * A root concept is a concept with no parent concept. This would typically be a higher concept
	 * of rank kingdom for example.
	 * 
	 * @param isoCountryCode The country to search, nullable
	 * @return List of BriefTaxonConceptDTOs ordered alphabetically by Scientific Name  
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public List<BriefTaxonConceptDTO> getRootTaxonConceptsForCountry(String isoCountryCode) throws ServiceException;			

	/**
	 * Returns the root concept rank for a taxonomy. For taxonomies with root concepts of multiple ranks
	 * a null will be returned. 
	 * 
	 * @param dataProviderKey The Data Provider to search, nullable
	 * @param dataResourceKey The Data Resource to search, nullable
	 * @return TaxonRankType the root concept rank
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public TaxonRankType getRootConceptRankForTaxonomy(String dataProviderKey, String dataResourceKey) throws ServiceException;			
	
	/**
	 * Returns the total number of TaxonConcept records within the system.
	 * 
	 * @return the Taxon Concept count.
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public int getTotalTaxonConceptCount() throws ServiceException;	
	
	/**
	 * Returns the number of TaxonConcepts matching the specified search criteria. This could be used to
	 * retrieve a concept count for a particular data resource by supplyinga data resource key and providing a null nameStub.
	 * Supplying a null dataResourceKey will result in counting across taxonomies.
	 * 
	 * @param nameStub The name stub to use for name matching, nullable
	 * @param fuzzy If fuzzy matching is to be used for the name
	 * @param dataProviderKey The data provider to search, nullable
	 * @param dataResourceKey The data resource to search, nullable
	 * @return the concept count
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public int getTaxonConceptCount(String nameStub, boolean fuzzy, String dataProviderKey, String dataResourceKey) throws ServiceException;	
	
	/**
	 * Returns the number of TaxonConcept records for the supplied dataResourceKey and taxonRank.
	 * Supplying a null dataResourceKey will result in counting across taxonomies. This does not give
	 * a count of distinct concepts across the taxonomies i.e. the same concept may appear in multiple
	 * taxonomies but will be treated as distinct concepts.
	 * 
	 * This method attempts to recognise the supplied string as a rank name. Examples would be
	 * "kingdom", "phylum", "subspecies".
	 * 
	 * @param taxonRank the taxon rank as a String
	 * @param higherThanSuppliedRank true to include ranks higher than the rank given in the count
	 * false to include ranks lower then the given rank in the count
	 * null to count at the given rank only
	 * @param dataProviderKey The Data Provider to search, nullable
	 * @param dataResourceKey the key for a Data Resource, nullable
	 * @return the concept count
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 * @throws IllegalArgumentException for a unrecognised string taxonRank value
	 */
	public int getTaxonConceptCountForRank(String taxonRank, Boolean higherThanSuppliedRank, String dataProviderKey, String dataResourceKey) throws ServiceException;			
	
	/**
	 * Returns a list of TaxonConcepts matching the specified search criteria.
	 * 
	 * @param scientificName The name to use for name matching
	 * @param fuzzy If fuzzy matching is to be used for the name
	 * @param rank  A recognised rank name, nullable
	 * @param dataProviderKey the key of the data provider to search, nullable
	 * @param dataResourceKey the key of the data resource to search, nullable
	 * @param resourceNetworkKey the key of the resource network to search, nullable
	 * @param hostIsoCountryCode the ISO code for the country where the provider is based, nullable
	 * @param isoLanguageCode the language code to make decisions based on the users preferred language (e.g. select the 
	 * most appropriate common name), nullable
	 * @param modifiedSince only return records modified after this date, nullable
	 * @param allowUnconfirmed whether to allow unconfirmed names
	 * @param searchConstraints the SearchConstraints to use
	 * @return SearchResultsDTO contains a list of TaxonConceptDTOs
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public SearchResultsDTO findTaxonConcepts(String scientificName, boolean fuzzy, String rank, String dataProviderKey, String dataResourceKey, 
			String resourceNetworkKey, String hostIsoCountryCode, String languageCode, Date modifiedSince,  boolean allowUnconfirmed, 
			boolean sortAlphabetically, SearchConstraints searchConstraints) throws ServiceException;		

	/**
	 * Returns a list of TaxonConcepts matching the specified search criteria.
	 * 
	 * @param scientificName The name to use for name matching
	 * @param specificEpithet The specific epithet to match
	 * @param fuzzy If fuzzy matching is to be used for the name
	 * @param rank  A recognised rank name, nullable
	 * @param dataProviderKey the key of the data provider to search, nullable
	 * @param dataResourceKey the key of the data resource to search, nullable
	 * @param resourceNetworkKey the key of the resource network to search, nullable
	 * @param hostIsoCountryCode the ISO code for the country where the provider is based, nullable
	 * @param modifiedSince only return records modified after this date, nullable
	 * @param searchConstraints the SearchConstraints to use
	 * @return SearchResultsDTO contains a list of TaxonConceptDTOs
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public SearchResultsDTO findSpeciesConcepts(String scientificName, String specificEpithet, boolean fuzzy, String dataProviderKey, 
			String dataResourceKey, Date modifiedSince, SearchConstraints searchConstraints) throws ServiceException;		
	
	/**
	 * Counts TaxonConcepts matching the specified search criteria.
	 * 
	 * @param scientificName The name stub to use for name matching
	 * @param fuzzy If fuzzy matching is to be used for the name
	 * @param rank  A recognised rank name, nullable
	 * @param dataProviderKey the key of the data provider to search, nullable
	 * @param dataResourceKey the key of the data resource to search, nullable
	 * @param resourceNetworkKey the key of the resource network to search, nullable
	 * @param hostIsoCountryCode the ISO code for the country where the provider is based, nullable
	 * @param modifiedSince only return records modified after this date, nullable
	 * @return SearchResultsDTO contains a list of TaxonConceptDTOs
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public Long countTaxonConcepts(String scientificName, boolean fuzzy, String rank, String dataProviderKey, 
			String dataResourceKey, String resourceNetworkKey, String hostIsoCountryCode, Date modifiedSince) throws ServiceException;		

	/**
	 * Returns the Concepts with the same scientific name and taxonomic rank as the 
	 * taxon concept with the supplied key.
	 * 
	 * @param taxonConceptKey the taxon concept key 
	 * @param dataProviderKey the data provider to search within, nullable
	 * @param dataResourceKey the data resource to search within, nullable
	 * @param searchConstraints the search constraints to use
	 * @return SearchResultsDTO containing TaxonConceptDTO objects for the matching Taxon Concepts
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */	
	public SearchResultsDTO findTaxonConceptsWithSameScientificNameAndRankAs(String taxonConceptKey, String dataProviderKey, 
			String dataResourceKey, SearchConstraints searchConstraints) throws ServiceException;
	
	/**
	 * Returns a list of TaxonConcepts that are linked to the supplied Common Name.
	 * 
	 * @param nameStub The name stub to use for name matching
	 * @param fuzzy If fuzzy matching is to be used for the name
	 * @param searchConstraints the search constraints to use 
	 * @return SearchResultsDTO containing matching BriefTaxonConceptDTO
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public SearchResultsDTO findTaxonConceptsForCommonName(String commonNameStub, boolean fuzzy, SearchConstraints searchConstraints) throws ServiceException;	

	/**
	 * Returns a list of matching Scientific Names strings for the specified rank.
	 * 
	 * @param name the partial name to search for
	 * @param fuzzy whether to match partial names
	 * @param taxonRankType the rank to search for
	 * @param whether to look for names higher than the specified rank or below. nullable
	 * @param dataProviderKey the data provider key to use, nullable
	 * @param dataResourceKey the data resource key to use, nullable
	 * @param searchConstraints the search constraints to use
	 * @return SearchResultsDTO containing matching name strings.  The encapsulated object is List<String>
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public SearchResultsDTO findMatchingScientificNames(String name, boolean fuzzy, TaxonRankType taxonRankType, Boolean higherThanRankSupplied, 
			boolean soundex, String dataProviderKey, String dataResourceKey, boolean allowUnconfirmed, SearchConstraints searchConstraints) throws ServiceException;		
	
	/**
	 * Returns a list of matching Common Names strings.
	 * 
	 * @param name the partial name to search for
	 * @param fuzzy whether to perform a wildcard search or not
	 * @param searchConstraints the search constraints to use 
	 * @return SearchResultsDTO containing matching common name strings
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public SearchResultsDTO findMatchingCommonNames(String name, boolean fuzzy, SearchConstraints searchConstraints) throws ServiceException;	
	
	/**
	 * Retrieve the common name for the supplied key.
	 * 
	 * @param commonNameKey
	 * @return CommonNameDTO for this common name, null if doesnt exist
	 */
	public CommonNameDTO getCommonNameFor(String commonNameKey) throws ServiceException;
	
	/**
	 * Retrieves a list of urls of images for the supplied scientific name.
	 * 
	 * @param scientificName
	 * @param searchConstraints
	 * @return
	 * @throws ServiceException
	 */
	public SearchResultsDTO findImagesFor(String taxonConceptKey, SearchConstraints searchConstraints) throws ServiceException;	
	
	/**
	 * Counts the number of child concepts of this concept at the supplied rank.
	 * This service layer method only supports the major ranks (i.e. phylum, order, class, family, genus and species)
	 * for the <code>childConceptRank</code> argument and the supplied concept key must be
	 * a concept this is a major rank (kingdom->genus).
	 * 
	 * @param taxonConceptKey
	 * @param taxonRankType
	 * @return
	 */
	public int countChildConceptsFor(String taxonConceptKey, TaxonRankType childConceptRank, boolean countSynonyms, boolean onlyCountAccepted, 
			boolean allowUnconfirmed) throws ServiceException;
	
	/**
	 * Returns true if the supplied string could be a valid Taxon Concept Key. This
	 * method does not verify a concept exists for this key, merely that the supplied
	 * key is of the correct format.
	 * 
	 * @see getTaxonConceptFor(String)
	 * @return true if the supplied key is a valid key
	 */
	public boolean isValidTaxonConceptKey(String taxonConceptKey) throws ServiceException;	
	
	/**
	 * Returns list of RelationshipAssertionDTOs for a given from taxon key.
	 * 
	 * @param fromTaxonConceptKey
	 * @return list of relationship assertion DTOs
	 */
	public List<RelationshipAssertionDTO> findRelationshipAssertionsForFromTaxonConcept(String fromTaxonConceptKey) throws ServiceException;	
	
	/**
	 * Returns list of RelationshipAssertionDTOs for a given to taxon key.
	 * 
	 * @param toTaxonConceptKey
	 * @return list of relationship assertion DTOs
	 */
	public List<RelationshipAssertionDTO> findRelationshipAssertionsForToTaxonConcept(String toTaxonConceptKey) throws ServiceException;	

	/**
	 * Returns list of CommonNameDTOs for a given to taxon key.
	 * 
	 * @param taxonConceptKey
	 * @return list of common name DTOs
	 */
	public List<CommonNameDTO> findCommonNamesForTaxonConcept(String taxonConceptKey, SearchConstraints searchConstraints) throws ServiceException;
	
	/**
	 * Returns list of urls for a given to taxon key.
	 * 
	 * @param taxonConceptKey
	 * @return list string urls
	 */
	public List<String> findRemoteUrlFor(String taxonConceptKey) throws ServiceException;	

	/**
	 * Find all remote concepts for a taxon concept
	 * 
	 * @param taxonConceptKey
	 * @return
	 * @throws ServiceException
	 */
	public List<RemoteConceptDTO> findRemoteConceptsFor(String taxonConceptKey) throws ServiceException;
	
  /**
   * Returns the typification records for all the taxonNames that are partners of the supplied nub concept.
   * All names are used, to ensure that any author variations within the nub concept are returned
   * 
   * @param nubConceptKey The nubConceptKey key 
   * @return List of TypificationRecordDTOs for ALL the names behind the nub concept
   * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
   */
  public List<TypificationRecordDTO> getTypificationRecordsForPartnersOfTaxonConcept(String nubConceptKey) throws ServiceException;
  
  /**
   * Returns the typification records for the given concept
   * 
   * @param conceptKey The nubConceptKey key 
   * @return List of TypificationRecordDTOs for the concepts
   * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
   */
  public List<TypificationRecordDTO> getTypificationRecordsForTaxonConcept(String conceptKey) throws ServiceException;
  
	/**
	 * Retrieves a count against all countries for this taxon concept.
	 * @param taxonConcept
	 * @return
	 * @throws ServiceException
	 */
	public List<CountDTO> getCountryCountsForTaxonConcept(String taxonConceptKey, Locale locale) throws ServiceException;
}