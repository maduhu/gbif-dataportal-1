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

import org.gbif.portal.dto.CountDTO;
import org.gbif.portal.dto.KeyValueDTO;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.occurrence.ImageRecordDTO;
import org.gbif.portal.dto.resources.AgentDTO;
import org.gbif.portal.dto.resources.DataProviderAgentDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.resources.DataResourceAgentDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.dto.resources.PropertyStoreNamespaceDTO;
import org.gbif.portal.dto.resources.ResourceAccessPointDTO;
import org.gbif.portal.dto.resources.ResourceNetworkDTO;
import org.gbif.portal.dto.util.SearchConstraints;

/**
 * Service interface for Data Resource Interface methods. These methods provide
 * details of data providers, resources that the system is serving data from. 
 * 
 * A Data Provider consists of one or more Data Resources. A Data Resource
 * may or may not be part of a set of resources provided by a provider.
 * 
 * The Nub Data Provider is considered to be the authoritive data provider within the system.
 *
 * @author dmartin
 */
public interface DataResourceManager {

	/**
	 * Returns the Data Resource for the specified key value. Returns null if there is not
	 * a Data Resource for the supplied key or if the supplied key is invalid.
	 * 
	 * @param dataResourceKey The data resource key 
	 * @return DataResourceDTO containing details of this datresource, null if unable to find data resource for this key
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public DataResourceDTO getDataResourceFor(String dataResourceKey) throws ServiceException;
	
	/**
	 * Returns the Data Resource for the specified key value. Returns null if there is not
	 * a Data Resource for the supplied key or if the supplied key is invalid.
	 * 
	 * @param dataResourceKey The data resource key 
	 * @return DataResourceDTO containing details of this datresource, null if unable to find data resource for this key
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public List<DataResourceDTO> getAllDataResources() throws ServiceException;	
	
	/**
	 * Returns the Data Resource for the specified key value. Returns null if there is not
	 * a Data Resource for the supplied key or if the supplied key is invalid.
	 * 
	 * @param dataResourceKey The data resource key 
	 * @return DataResourceDTO containing details of this datresource, null if unable to find data resource for this key
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public List<DataProviderDTO> getAllDataProviders() throws ServiceException;		

	/**
	 * Returns the Data Provider that is providing the Nub Taxonomy for the system. 
	 * 
	 * @return DataProviderDTO containing details of this data provider, null if their isnt a nub provider
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public DataProviderDTO getNubDataProvider() throws ServiceException;
	
	/**
	 * Returns the Data Provider for the specified key value. Returns null if there is not
	 * a Data Provider for the supplied key or if the supplied key is invalid.
	 * 
	 * @param dataProviderKey The data provider key 
	 * @return DataProviderDTO containing details of this data provider, null if unable to find data provider for this key
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public DataProviderDTO getDataProviderFor(String dataProviderKey) throws ServiceException;

	/**
	 * Returns the Resource Network for the specified key value. Returns null if there is not
	 * a Resource Network for the supplied key or if the supplied key is invalid.
	 * 
	 * @param resourceNetworkKey The data provider key 
	 * @return ResourceNetworkDTO containing details of this resource network, null if unable to find resource network for this key
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public ResourceNetworkDTO getResourceNetworkFor(String dataProviderKey) throws ServiceException;
	
	/**
	 * Returns a list of Data Resources for the specified  Data Provider key value. 
	 * 
	 * @param dataProviderKey The data provider key 
	 * @return List<DataResourceDTO> containing data resources for this provider
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public List<DataResourceDTO> getDataResourcesForProvider(String dataProviderKey) throws ServiceException;	
	
	/**
	 * Returns a list of Data Resources for the specified Resource Network key value. 
	 * 
	 * @param resourceNetworkKey The resource network key 
	 * @return List<DataResourceDTO> containing data resources for this network
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public List<DataResourceDTO> getDataResourcesForResourceNetwork(String resourceNetworkKey) throws ServiceException;	
	
	/**
	 * Returns a list of Resource Networks for the specified Data Resource key value. 
	 * 
	 * @param dataResourceKey The data resource key 
	 * @return List<ResourceNetworkDTO> containing resource networks for this resource
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public List<ResourceNetworkDTO> getResourceNetworksForDataResource(String dataResourceKey) throws ServiceException;	
	
	/**
	 * Returns a list of Resource Access Points for the specified Data Resource key value. 
	 * 
	 * @param dataResourceKey The data resource key 
	 * @return List<ResourceAccessPointDTO> containing resource access points for this resource
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public List<ResourceAccessPointDTO> getResourceAccessPointsForDataResource(String dataResourceKey) throws ServiceException;	
	
	/**
	 * Returns a list of PropertyStoreNamespaces for the specified Resource Access Point key value. 
	 * 
	 * @param dataResourceKey The data resource key 
	 * @return List<PropertyStoreNamespaceDTO> containing PropertyStoreNamespaces for this resource access point
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public List<PropertyStoreNamespaceDTO> getPropertyStoreNamespacesForResourceAccessPoint(String resourceAccessPointKey) throws ServiceException;	
	
	/**
	 * Returns a list of Data Providers that offer a taxonomy.
	 * 
	 * @return a list of Data Providers offering taxonomies
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public List<DataProviderDTO> getDataProvidersOfferingTaxonomies() throws ServiceException;
	
	/**
	 * Returns a list of Data Resources that offer a taxonomy.
	 * 
	 * @return a list of Data Resources
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public List<DataResourceDTO> getDataResourcesWithInferredTaxonomies() throws ServiceException;
	
	/**
	 * Returns the Data Resource for last added to the system.
	 * 
	 * @return DataResourceDTO containing details of the latest datresource added to the system, null if system is currently empty
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public DataResourceDTO getNewestDataResource() throws ServiceException;

	/**
	 * Returns the total number of Data Resources the system is serving data from.
	 * 
	 * @return the Data Resource count.
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public int getTotalDataResourceCount() throws ServiceException;		

	/**
	 * Returns the total number records hosted by a country.
	 * Hence the number of total number of records supplied by providers
	 * in a certain country.
	 * 
	 * @param the country
	 * @param georeferenced
	 * @return the number of records hosted by a country
	 * 
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public int getTotalCountHostedby(String isoCountryCode, boolean georeferenced) throws ServiceException;			
	
	/**
	 * Returns the total number of Data Providers the system is serving data from.
	 * 
	 * @return the Data Provider count.
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public int getTotalDataProviderCount() throws ServiceException;			

	/**
	 * Retrieves the a distinct set of first characters for all Datasets.
	 * @return list of characters
	 */
	public List<Character> getDatasetAlphabet();
	
	/**
	 * Find data resources, providers and resource networks that match the supplied name. 
	 * 
	 * @param nameStub
	 * @param fuzzy whether to do a fuzzy name search or not
	 * @param anyOccurrence find matches where supplied stub appears in any part of the resource/provider name
	 * @param searchConstraints the search constraints to use
	 * @return a SearchResultsDTO that contains both DataResourceDTOs and DataProviderDTOs
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public SearchResultsDTO findDatasets(String nameStub, boolean fuzzy, boolean anyOccurrence, boolean includeCountrySearch, SearchConstraints searchConstraints);			
	
	/**
	 * Find data resources that match the following resource name. 
	 * 
	 * @param resourceName
	 * @param fuzzy whether to do a fuzzy name search or not
	 * @param basis of record code for resource
	 * @param date after which resource record was last modified
	 * @param searchConstraints
	 * @return a SearchResultsDTO containing DataResourceDTOs.
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public SearchResultsDTO findDataResources(
			String resourceName, 
			boolean fuzzy, 
			String providerKey,
			String basisOfRecordCode, 
			Date modifiedSince, 
			SearchConstraints searchConstraints)
				throws ServiceException;		

	/**
	 * Count data resources that match the following resource name. 
	 * 
	 * @param resourceName
	 * @param fuzzy whether to do a fuzzy name search or not
	 * @param basis of record code for resource
	 * @param date after which resource record was last modified
	 * @return count of DataResourceDTOs.
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public Long countDataResources(
			String resourceName, 
			boolean fuzzy, 
			String providerKey,
			String basisOfRecordCode, 
			Date modifiedSince)
				throws ServiceException;		

	/**
	 * Find data providers that match the following provider name. 
	 * 
	 * @param providerName
	 * @param fuzzy whether to do a fuzzy name search or not
	 * @param iso country code within which provider is located
	 * @param date after which provider record was last modified
	 * @param searchConstraints
	 * @return a SearchResultsDTO containing DataProviderDTOs.
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public SearchResultsDTO findDataProviders(
			String providerName, 
			boolean fuzzy, 
			String isoCountryCode, 
			Date modifiedSince, 
			SearchConstraints searchConstraints)
				throws ServiceException;		

	/**
	 * Finds data providers for the associated email address.
	 * 
	 * @param email
	 * @return list of data providers
	 */
	public List<DataProviderDTO> findDataProvidersForUser(String email) throws ServiceException;
	
	/**
	 * Count data providers that match the following provider name. 
	 * 
	 * @param providerName
	 * @param fuzzy whether to do a fuzzy name search or not
	 * @param iso country code within which provider is located
	 * @param date after which provider record was last modified
	 * @return a count of matching DataProviderDTOs.
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public Long countDataProviders(
			String providerName, 
			boolean fuzzy,
			String isoCountryCode,
			Date modifiedSince)
				throws ServiceException;		


	/**
	 * Returns a key value list of resource networks and keys.
	 * @return key value list of resource networks and keys.
	 */
	public List<KeyValueDTO> getResourceNetworkList();
	
	/**
	 * Returns a key value list of data providers and keys.
	 * @return key value list of data providers and keys.
	 */
	public List<KeyValueDTO> getDataProviderList();
	
	/**
	 * Returns a key value list of data resources and keys for the supplied dataProvider.
	 * @return key value list of data resources and keys.
	 */
	public List<KeyValueDTO> getDataResourceList(String resourceNetworkKey, String dataProviderKey);
	
	/**
	 * Find resource networks that match the following parameters. 
	 * 
	 * @param networkName 
	 * @param fuzzy whether to do a fuzzy name search or not
	 * @param short identifier code 
	 * @param date after which network record was last modified
	 * @param searchConstraints
	 * @return a SearchResultsDTO containing ResourceNetworkDTOs.
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public SearchResultsDTO findResourceNetworks(
			String networkName, 
			boolean fuzzy, 
			String code, 
			Date modifiedSince, 
			SearchConstraints searchConstraints)
				throws ServiceException;		
	
	/**
	 * An image record DTO for the supplied key
	 * 
	 * @param imageKey
	 * @return
	 */
	public ImageRecordDTO getDataResourceImageFor(String imageKey);	
	
	/**
	 * Count resource networks that match the following parameters. 
	 * @param networkName 
	 * @param fuzzy whether to do a fuzzy name search or not
	 * @param short identifier code 
	 * @param date after which network record was last modified
	 * @return a count of matching ResourceNetworkDTOs.
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public Long countResourceNetworks(
			String networkName, 
			boolean fuzzy,
			String code,
			Date modifiedSince)
				throws ServiceException;		
	
	/**
	 * Returns the list of DataResourceAgents for a data resource
	 * @param dataResourceKey
	 * @return
	 */
	public List<DataResourceAgentDTO> getAgentsForDataResource(String dataResourceKey);
	
	/**
	 * Returns the list of DataProviderAgents for a data provider
	 * @param dataProviderKey
	 * @return
	 */
	public List<DataProviderAgentDTO> getAgentsForDataProvider(String dataProviderKey);	

	/**
	 * Returns the list of DataProviderAgents for a data provider
	 * @param dataProviderKey
	 * @return
	 */
	public List<AgentDTO> getAgentsForEmailAddress(String email);	
	
	/**
	 * Retrieve a list of resources with data for the supplied taxon concept.
	 * 
	 * @param taxonConceptKey
	 * @return list of data resources with data for this concept
	 * @throws ServiceException
	 */
	public List<CountDTO> getDataResourceWithOccurrencesFor(String taxonConceptKey, String rank, boolean georeferenced) throws ServiceException;
	
	/**
	 * Retrieves a list of data providers not associated with a single host country.
	 * 
	 * @return a list of data providers not associated with a single host country.
	 */
	public List<DataProviderDTO> getInternationalDataProviders();
	
	/**
	 * Returns true if the supplied string could be a valid Data Resource Key. This
	 * method does not verify a data resource exists for this key, merely that the supplied
	 * key is of the correct format.
	 * 
	 * @see getDataResourceFor(String)
	 * @return true if the supplied key is a valid key
	 */
	public boolean isValidDataResourceKey(String dataResourceKey);

	/**
	 * Returns true if the supplied string could be a valid Data Provider Key. This
	 * method does not verify a data resource exists for this key, merely that the supplied
	 * key is of the correct format.
	 * 
	 * @see getDataProviderFor(String)
	 * @return true if the supplied key is a valid key
	 */
	public boolean isValidDataProviderKey(String dataProviderKey);
}