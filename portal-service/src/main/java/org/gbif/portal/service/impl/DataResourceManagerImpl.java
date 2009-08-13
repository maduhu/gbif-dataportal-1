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
package org.gbif.portal.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dao.occurrence.ImageRecordDAO;
import org.gbif.portal.dao.resources.AgentDAO;
import org.gbif.portal.dao.resources.DataProviderDAO;
import org.gbif.portal.dao.resources.DataResourceDAO;
import org.gbif.portal.dao.resources.PropertyStoreNamespaceDAO;
import org.gbif.portal.dao.resources.ResourceAccessPointDAO;
import org.gbif.portal.dao.resources.ResourceNetworkDAO;
import org.gbif.portal.dto.CountDTO;
import org.gbif.portal.dto.CountDTOFactory;
import org.gbif.portal.dto.DTOFactory;
import org.gbif.portal.dto.KeyValueDTO;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.occurrence.ImageRecordDTO;
import org.gbif.portal.dto.resources.AgentDTO;
import org.gbif.portal.dto.resources.AgentDTOFactory;
import org.gbif.portal.dto.resources.DataProviderAgentDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.resources.DataProviderDTOFactory;
import org.gbif.portal.dto.resources.DataResourceAgentDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.dto.resources.DataResourceDTOFactory;
import org.gbif.portal.dto.resources.PropertyStoreNamespaceDTO;
import org.gbif.portal.dto.resources.ResourceAccessPointDTO;
import org.gbif.portal.dto.resources.ResourceNetworkDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.model.occurrence.BasisOfRecord;
import org.gbif.portal.model.resources.DataProvider;
import org.gbif.portal.model.resources.DataProviderAgent;
import org.gbif.portal.model.resources.DataResource;
import org.gbif.portal.model.resources.DataResourceAgent;
import org.gbif.portal.model.resources.PropertyStoreNamespace;
import org.gbif.portal.model.resources.ResourceAccessPoint;
import org.gbif.portal.model.resources.ResourceNetwork;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.ServiceException;

/**
 * An implementation of the DataResourceManager interface that makes use of the
 * DAO layer objects for data access.
 *
 * @author Dave Martin
 */
public class DataResourceManagerImpl implements DataResourceManager {

	protected static Log logger = LogFactory.getLog(DataResourceManagerImpl.class);	

	/**The DAO for accessing Data Resources **/
	protected DataResourceDAO dataResourceDAO;
	/**The DAO for accessing Data Providers **/	
	protected DataProviderDAO dataProviderDAO;
	/**The DAO for accessing Resource Networks **/	
	protected ResourceNetworkDAO resourceNetworkDAO;
	/**The DAO for accessing Resource Access Points **/	
	protected ResourceAccessPointDAO resourceAccessPointDAO;
	/**The DAO for accessing Property Store Namespaces **/	
	protected PropertyStoreNamespaceDAO propertyStoreNamespaceDAO;
	/**The DAO for accessing Resource Networks **/	
	protected ImageRecordDAO imageRecordDAO;
	/**The DAO for accessing Agents **/	
	protected AgentDAO agentDAO;
	
	/**The DTO factory for creating Data Resource DTOs **/
	protected DTOFactory dataResourceDTOFactory;	
	/**The DTO factory for creating Data Provider DTOs **/
	protected DTOFactory dataProviderDTOFactory;		
	/**The DTO factory for creating Resource Network DTOs **/
	protected DTOFactory resourceNetworkDTOFactory;		
	/**The DTO factory for creating ResourceAccessPoint DTOs **/
	protected DTOFactory resourceAccessPointDTOFactory;		
	/**The DTO factory for creating PropertyStoreNamespace DTOs **/
	protected DTOFactory propertyStoreNamespaceDTOFactory;		
	/**The DTO factory for creating image record DTOs **/
	protected DTOFactory imageRecordDTOFactory;		
	/**The DTO factory for creating Provider Agent DTOs **/
	protected DTOFactory dataProviderAgentDTOFactory;		
	/**The DTO factory for creating Resource Agent DTOs **/
	protected DTOFactory dataResourceAgentDTOFactory;		
	/**The DTO factory for creating Key Value DTOs **/
	protected DTOFactory keyValueDTOFactory;	
	
	/**The Data Provider id of the nub taxonomy**/
	protected long nubDataProviderId;
	
	/**
	 * @see org.gbif.portal.service.DataResourceManager#getAllDataProviders()
	 */
	public List<DataProviderDTO> getAllDataProviders() throws ServiceException {
		List<DataProvider> dps = dataProviderDAO.getAllDataProviders();
		return dataProviderDTOFactory.createDTOList(dps);
	}

	/**
	 * @see org.gbif.portal.service.DataResourceManager#getAllDataResources()
	 */
	public List<DataResourceDTO> getAllDataResources() throws ServiceException {
		List<DataResource> drs = dataResourceDAO.getAllDataResources();
		return dataResourceDTOFactory.createDTOList(drs);		
	}	
	
	/**
	 * @see org.gbif.portal.service.DataResourceManager#getDataResourceFor(java.lang.String)
	 */
	public DataResourceDTO getDataResourceFor(String dataResourceKey)
			throws ServiceException {
		Long dataResourceId = parseKey(dataResourceKey);
		DataResource dataResource = dataResourceDAO.getDataResourceFor(dataResourceId);
		return (DataResourceDTO) dataResourceDTOFactory.createDTO(dataResource);
	}

	/**
	 * @see org.gbif.portal.service.DataResourceManager#getDataProviderFor(java.lang.String)
	 */
	public DataProviderDTO getDataProviderFor(String dataProviderKey) throws ServiceException {
		Long dataProviderId = parseKey(dataProviderKey);
		DataProvider dataProvider = dataProviderDAO.getDataProviderFor(dataProviderId);
		return (DataProviderDTO) dataProviderDTOFactory.createDTO(dataProvider);	
	}

	/**
	 * @see org.gbif.portal.service.DataResourceManager#getResourceNetworkFor(java.lang.String)
	 */
	public ResourceNetworkDTO getResourceNetworkFor(String resourceNetworkKey) throws ServiceException {
		Long resourceNetworkId = parseKey(resourceNetworkKey);
		ResourceNetwork resourceNetwork = resourceNetworkDAO.getResourceNetworkFor(resourceNetworkId);
		return (ResourceNetworkDTO) resourceNetworkDTOFactory.createDTO(resourceNetwork);	
	}

	/**
	 * @see org.gbif.portal.service.DataResourceManager#getDataResourcesForProvider(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<DataResourceDTO> getDataResourcesForProvider(String dataProviderKey) throws ServiceException {
		Long dataProviderId = parseKey(dataProviderKey);
		List<DataResource> dataResources = dataResourceDAO.getDataResourcesForProvider(dataProviderId);
		return (List<DataResourceDTO>)dataResourceDTOFactory.createDTOList(dataResources);
	}
	
	/**
	 * @see org.gbif.portal.service.DataResourceManager#getDataResourcesForResourceNetwork(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<DataResourceDTO> getDataResourcesForResourceNetwork(String resourceNetworkKey) throws ServiceException {
		Long resourceNetworkId = parseKey(resourceNetworkKey);
		List<DataResource> dataResources = dataResourceDAO.getDataResourcesForResourceNetwork(resourceNetworkId);
		return (List<DataResourceDTO>)dataResourceDTOFactory.createDTOList(dataResources);
	}
	
	/**
	 * @see org.gbif.portal.service.DataResourceManager#getResourceNetworksForDataResource(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<ResourceNetworkDTO> getResourceNetworksForDataResource(String dataResourceKey) throws ServiceException {	
		Long dataResourceId = parseKey(dataResourceKey);
		List<ResourceNetwork> resourceNetworks = resourceNetworkDAO.getResourceNetworksForDataResource(dataResourceId);
		return (List<ResourceNetworkDTO>)resourceNetworkDTOFactory.createDTOList(resourceNetworks);
	}

	/**
	 * @see org.gbif.portal.service.DataResourceManager#getPropertyStoreNamespacesForResourceAccesspoint(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<PropertyStoreNamespaceDTO> getPropertyStoreNamespacesForResourceAccessPoint(String resourceAccessPointKey) throws ServiceException {
		Long resourceAccessPointId = parseKey(resourceAccessPointKey);
		List<PropertyStoreNamespace> propertyStoreNamespaces = propertyStoreNamespaceDAO.getPropertyStoreNamespacesForResourceAccessPoint(resourceAccessPointId);
		return (List<PropertyStoreNamespaceDTO>)propertyStoreNamespaceDTOFactory.createDTOList(propertyStoreNamespaces);
	}

	/**
	 * @see org.gbif.portal.service.DataResourceManager#getResourceAccessPointsForDataResource(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<ResourceAccessPointDTO> getResourceAccessPointsForDataResource(String dataResourceKey) throws ServiceException {
		Long dataResourceId = parseKey(dataResourceKey);
		List<ResourceAccessPoint> resourceAccessPoints = resourceAccessPointDAO.getResourceAccessPointsForDataResource(dataResourceId);
		return (List<ResourceAccessPointDTO>)resourceAccessPointDTOFactory.createDTOList(resourceAccessPoints);
	}

	/**
	 * @see org.gbif.portal.service.DataResourceManager#getNubDataProvider()
	 */
	public DataProviderDTO getNubDataProvider() throws ServiceException {
		DataProvider dataProvider = dataProviderDAO.getDataProviderFor(nubDataProviderId);
		return (DataProviderDTO) dataProviderDTOFactory.createDTO(dataProvider);		
	}	
	
	/**
	 * @see org.gbif.portal.service.DataResourceManager#getNewestDataResource()
	 */
	public DataResourceDTO getNewestDataResource() throws ServiceException {
		DataResource dataResource = dataResourceDAO.getLastDataResourceAdded();
		return (DataResourceDTO) dataResourceDTOFactory.createDTO(dataResource);	
	}

	/**
	 * @see org.gbif.portal.service.DataResourceManager#getDataProvidersOfferingTaxonomies()
	 */
	@SuppressWarnings("unchecked")
	public List<DataProviderDTO> getDataProvidersOfferingTaxonomies() throws ServiceException {
		logger.debug("Retrieving Data Providers offering taxonomies");
		List<DataProvider> dataProviders= dataProviderDAO.getDataProvidersOfferingTaxonomies();
		return (List<DataProviderDTO>) dataProviderDTOFactory.createDTOList(dataProviders);
	}

	/**
	 * @see org.gbif.portal.service.DataResourceManager#getDataResourcesWithInferredTaxonomies()
	 */
	@SuppressWarnings("unchecked")
	public List<DataResourceDTO> getDataResourcesWithInferredTaxonomies() throws ServiceException {
		logger.debug("Retrieving Data Resource offering taxonomies");
		List<DataResource> dataResources = dataResourceDAO.getDataResourcesWithNonSharedTaxonomies();
		return  (List<DataResourceDTO>) dataResourceDTOFactory.createDTOList(dataResources);
	}	
	
	/**
	 * @see org.gbif.portal.service.DataResourceManager#getTotalDataProviderCount()
	 */
	public int getTotalDataProviderCount() throws ServiceException {
		return dataProviderDAO.getTotalDataProviderCount();
	}

	/**
	 * @see org.gbif.portal.service.DataResourceManager#getTotalDataResourceCount()
	 */
	public int getTotalDataResourceCount() throws ServiceException {
		return dataResourceDAO.getTotalDataResourceCount();
	}	

	/**
	 * @see org.gbif.portal.service.DataResourceManager#findDatasets(java.lang.String, boolean, org.gbif.portal.dto.util.SearchConstraints)
	 */
	@SuppressWarnings("unchecked")
	public SearchResultsDTO findDatasets(String nameStub, boolean fuzzy, boolean anyOccurrence, boolean includeCountrySearch, SearchConstraints searchConstraints) {
		List resources = dataResourceDAO.findDataResourcesAndProvidersAndNetworks(nameStub, fuzzy, anyOccurrence, includeCountrySearch, searchConstraints.getStartIndex(), searchConstraints.getMaxResults()+1);
		List resourceDTOList = new ArrayList();
		for(Object resource: resources){
			if (resource instanceof DataResource)
				resourceDTOList.add(dataResourceDTOFactory.createDTO(resource));
			else if(resource instanceof DataProvider)
				resourceDTOList.add(dataProviderDTOFactory.createDTO(resource));
			else if(resource instanceof ResourceNetwork)
				resourceDTOList.add(resourceNetworkDTOFactory.createDTO(resource));
		}
		SearchResultsDTO searchResultsDTO = new SearchResultsDTO();
		searchResultsDTO.setResults(resourceDTOList, searchConstraints.getMaxResults());
		return searchResultsDTO;
	}

	/**
	 * @see org.gbif.portal.service.DataResourceManager#findDataProviders(java.lang.String, boolean, java.lang.String, java.util.Date, org.gbif.portal.dto.util.SearchConstraints)
	 */
	public SearchResultsDTO findDataProviders(
			String providerName, 
			boolean fuzzy,
			String isoCountryCode,
			Date modifiedSince,
			SearchConstraints searchConstraints) {
		List<DataProvider> providers = dataProviderDAO.findDataProviders(providerName, fuzzy, isoCountryCode, modifiedSince, searchConstraints.getStartIndex(), searchConstraints.getMaxResults()+1);
		return dataProviderDTOFactory.createResultsDTO(providers, searchConstraints.getMaxResults());
	}	
	
	/**
	 * @see org.gbif.portal.service.DataResourceManager#countDataProviders(java.lang.String, boolean, java.lang.String, java.util.Date)
	 */
	public Long countDataProviders(
			String providerName, 
			boolean fuzzy, 
			String isoCountryCode, 
			Date modifiedSince) {
		Long recordCount = dataProviderDAO.countDataProviders(providerName, fuzzy, isoCountryCode, modifiedSince);
		return recordCount; 
	}

	/**
	 * @see org.gbif.portal.service.DataResourceManager#findResourceNetworks(java.lang.String, boolean, java.lang.String, java.util.Date, org.gbif.portal.dto.util.SearchConstraints)
	 */
	public SearchResultsDTO findResourceNetworks(
			String networkName, 
			boolean fuzzy,
			String code,
			Date modifiedSince,
			SearchConstraints searchConstraints) {
		List<ResourceNetwork> networks = resourceNetworkDAO.findResourceNetworks(networkName, fuzzy, code, modifiedSince, searchConstraints.getStartIndex(), searchConstraints.getMaxResults()+1);
		return resourceNetworkDTOFactory.createResultsDTO(networks, searchConstraints.getMaxResults());
	}	
	
	/**
	 * @see org.gbif.portal.service.DataResourceManager#countResourceNetworks(java.lang.String, boolean, java.lang.String, java.util.Date)
	 */
	public Long countResourceNetworks(
			String networkName, 
			boolean fuzzy, 
			String code, 
			Date modifiedSince) {
		return resourceNetworkDAO.countResourceNetworks(networkName, fuzzy, code, modifiedSince);
	}

	/**
	 * @see org.gbif.portal.service.DataResourceManager#getDataProviderList()
	 */
	@SuppressWarnings("unchecked")
	public List<KeyValueDTO> getDataProviderList() {
		List dataProviderKVs = dataProviderDAO.getDataProviderList();
		return keyValueDTOFactory.createDTOList(dataProviderKVs);
	}

	/**
	 * @see org.gbif.portal.service.DataResourceManager#getDataResourceList(java.lang.String)
	 */
	@SuppressWarnings({ "unchecked", "unchecked" })
	public List<KeyValueDTO> getDataResourceList(String resourceNetworkKey, String dataProviderKey) {
		Long resourceNetworkId = parseKey(resourceNetworkKey);
		Long dataProviderId = parseKey(dataProviderKey);
		List dataResourceKVs = null;
		if(resourceNetworkId!=null){
			List<DataResource> dataResources = dataResourceDAO.getDataResourcesForResourceNetwork(resourceNetworkId);
			List<KeyValueDTO> kvps = new ArrayList<KeyValueDTO>();
			for (DataResource dr: dataResources){
				StringBuffer sb = new StringBuffer(dr.getName());
				if(dr.getDataProvider()!=null){
					sb.append(" - ");
					sb.append(dr.getDataProvider().getName());
				}
				kvps.add(new KeyValueDTO(dr.getId().toString(), sb.toString()));
			}
			return kvps;
		} else {
			dataResourceKVs = dataResourceDAO.getDataResourceList(dataProviderId);
			return keyValueDTOFactory.createDTOList(dataResourceKVs);
		}
	}
	
	/**
	 * @see org.gbif.portal.service.DataResourceManager#getResourceNetworkList()
	 */
	@SuppressWarnings("unchecked")
	public List<KeyValueDTO> getResourceNetworkList() {
		List resourceNetworkKVs = resourceNetworkDAO.getResourceNetworkList();
		return keyValueDTOFactory.createDTOList(resourceNetworkKVs);
	}		

	/**
	 * @see org.gbif.portal.service.DataResourceManager#getAgentsForDataProvider(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<DataProviderAgentDTO> getAgentsForDataProvider(String dataProviderKey) {
		List<DataProviderAgent> agents = agentDAO.getAgentsForDataProvider(parseKey(dataProviderKey));
		return dataProviderAgentDTOFactory.createDTOList(agents);
	}

	/**
	 * @see org.gbif.portal.service.DataResourceManager#getAgentsForDataResource(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<DataResourceAgentDTO> getAgentsForDataResource(String dataResourceKey) {
		List<DataResourceAgent> agents = agentDAO.getAgentsForDataResource(parseKey(dataResourceKey));
		return dataResourceAgentDTOFactory.createDTOList(agents);
	}
	
	/**
	 * @see org.gbif.portal.service.DataResourceManager#findDataResources(java.lang.String, boolean, org.gbif.portal.dto.util.SearchConstraints)
	 */
	public SearchResultsDTO findDataResources(
			String resourceName, 
			boolean fuzzy, 
			String providerKey,
			String basisOfRecordCode, 
			Date modifiedSince, 
			SearchConstraints searchConstraints) 
				throws ServiceException {
		BasisOfRecord basisOfRecord = null;
		if (basisOfRecordCode != null) {
			basisOfRecord = BasisOfRecord.getBasisOfRecord(basisOfRecordCode);
			
			if (basisOfRecord == null) {
				throw new ServiceException("No basis of record found for code " + basisOfRecordCode);
			}
		}

		DataProvider dataProvider = null;
		if (providerKey != null) {
			Long dataProviderId = parseKey(providerKey);
			dataProvider = dataProviderDAO.getDataProviderFor(dataProviderId);
			
			if (dataProvider == null) {
				throw new ServiceException("No DataProvider found for key " + providerKey);
			}
		}

		List<DataResource> resources = dataResourceDAO.findDataResources(resourceName, fuzzy, dataProvider, basisOfRecord, modifiedSince, searchConstraints.getStartIndex(), searchConstraints.getMaxResults()+1);
		return dataResourceDTOFactory.createResultsDTO(resources, searchConstraints.getMaxResults());
	}
	
	/**
	 * @see org.gbif.portal.service.DataResourceManager#getDataResourceImageUrlFor(java.lang.String)
	 */
	public ImageRecordDTO getDataResourceImageFor(String imageKey) {
		return (ImageRecordDTO) imageRecordDTOFactory.createDTO(imageRecordDAO.getImageRecordFor(parseKey(imageKey)));
	}	
	
	/**
	 * @see org.gbif.portal.service.DataResourceManager#findDataResources(java.lang.String, boolean, org.gbif.portal.dto.util.SearchConstraints)
	 */
	public Long countDataResources(
			String resourceName, 
			boolean fuzzy, 
			String providerKey,
			String basisOfRecordCode, 
			Date modifiedSince)
				throws ServiceException {
		BasisOfRecord basisOfRecord = null;
		if (basisOfRecordCode != null) {
			basisOfRecord = BasisOfRecord.getBasisOfRecord(basisOfRecordCode);
			
			if (basisOfRecord == null) {
				throw new ServiceException("No basis of record found for code " + basisOfRecordCode);
			}
		}

		DataProvider dataProvider = null;
		if (providerKey != null) {
			Long dataProviderId = parseKey(providerKey);
			dataProvider = dataProviderDAO.getDataProviderFor(dataProviderId);
			
			if (dataProvider == null) {
				throw new ServiceException("No DataProvider found for key " + providerKey);
			}
		}
		
		Long recordCount = dataResourceDAO.countDataResources(resourceName, fuzzy, dataProvider, basisOfRecord, modifiedSince);
		return recordCount;
	}

	/**
	 * @see org.gbif.portal.service.DataResourceManager#findDataProvidersForUser(java.lang.String)
	 */
	public List<DataProviderDTO> findDataProvidersForUser(String email)
			throws ServiceException {
		
		int indexOfAt = email.indexOf('@');
		if(indexOfAt<0){
			return new ArrayList<DataProviderDTO>();
		}

		//this could be dodgy - eg. dmartin@gbif.org and dmartin2@gbif.org
		String username = email.substring(0, indexOfAt)+'%';
		String domain = '%'+email.substring(indexOfAt+1);
		
		List<DataProvider> dps = dataProviderDAO.getDataProviderAssociatedWithUser(username, domain);
		return dataProviderDTOFactory.createDTOList(dps);
	}
	
	/**
	 * @see org.gbif.portal.service.DataResourceManager#getTotalCountHostedby(java.lang.String, boolean)
	 */
	public int getTotalCountHostedby(String isoCountryCode,
			boolean georeferenced) throws ServiceException {
		return dataProviderDAO.getDataProviderCountsForHostCountry(isoCountryCode, georeferenced);
	}	
	
	/**
	 * @see org.gbif.portal.service.DataResourceManager#getAgentsForEmailAddress(java.lang.String)
	 */
	public List<AgentDTO> getAgentsForEmailAddress(String email) {
		List agents = agentDAO.getAgentsForEmailAddress(email);
		return new AgentDTOFactory().createDTOList(agents);
	}	

	/**
	 * @see org.gbif.portal.service.DataResourceManager#getDatasetAlphabet()
	 */
	public  List<Character> getDatasetAlphabet() {
		return dataResourceDAO.getDatasetAlphabet();
	}
	
	/**
	 * @see org.gbif.portal.service.DataResourceManager#getDataResourceWithOccurrencesFor(java.lang.String, java.lang.String, boolean)
	 */
	public List<CountDTO> getDataResourceWithOccurrencesFor(
			String taxonConceptKey, String rank, boolean georeferenced)
			throws ServiceException {
		
		List<Object[]> results = dataResourceDAO.getDataResourceWithOccurrencesFor(parseKey(taxonConceptKey), rank, georeferenced);
		return (new CountDTOFactory()).createDTOList(results);
	}	
	
	/**
	 * @see org.gbif.portal.service.DataResourceManager#getInternationalDataProviders()
	 */
	public List<DataProviderDTO> getInternationalDataProviders() {
		return dataProviderDTOFactory.createDTOList(dataProviderDAO.getInternationalDataProviders(true));
  }	
	
	/**
	 * @see org.gbif.portal.service.DataResourceManager#isValidDataResourceKey(java.lang.String)
	 */
	public boolean isValidDataResourceKey(String dataResourceKey) {
		return parseKey(dataResourceKey)!=null;
	}	

	/**
	 * @see org.gbif.portal.service.DataResourceManager#isValidDataProviderKey(java.lang.String)
	 */
	public boolean isValidDataProviderKey(String dataProviderKey) {
		return parseKey(dataProviderKey)!=null;
	}	
	
	/**
	 * Parses the supplied key. Returns null if supplied string invalid
	 * @param key
	 * @return a concept key. Returns null if supplied string invalid key
	 */
	protected static Long parseKey(String key){
		Long parsedKey = null;
		try {
			parsedKey = Long.parseLong(key);
		} catch (NumberFormatException e){
			//expected behaviour for invalid keys
		}
		return parsedKey;
	}

	/**
	 * @param dataProviderDAO the dataProviderDAO to set
	 */
	public void setDataProviderDAO(DataProviderDAO dataProviderDAO) {
		this.dataProviderDAO = dataProviderDAO;
	}

	/**
	 * @param dataResourceDAO the dataResourceDAO to set
	 */
	public void setDataResourceDAO(DataResourceDAO dataResourceDAO) {
		this.dataResourceDAO = dataResourceDAO;
	}

	/**
	 * @param resourceNetworkDAO the resourceNetworkDAO to set
	 */
	public void setResourceNetworkDAO(ResourceNetworkDAO resourceNetworkDAO) {
		this.resourceNetworkDAO = resourceNetworkDAO;
	}

	/**
	 * @param dataResourceDTOFactory the dataResourceDTOFactory to set
	 */
	public void setDataResourceDTOFactory(
			DataResourceDTOFactory dataResourceDTOFactory) {
		this.dataResourceDTOFactory = dataResourceDTOFactory;
	}

	/**
	 * @param dataProviderDTOFactory the dataProviderDTOFactory to set
	 */
	public void setDataProviderDTOFactory(
			DataProviderDTOFactory dataProviderDTOFactory) {
		this.dataProviderDTOFactory = dataProviderDTOFactory;
	}

	/**
	 * @param resourceNetworkDTOFactory the resourceNetworkDTOFactory to set
	 */
	public void setResourceNetworkDTOFactory(DTOFactory resourceNetworkDTOFactory) {
		this.resourceNetworkDTOFactory = resourceNetworkDTOFactory;
	}

	/**
	 * @param nubDataProviderId the nubDataProviderId to set
	 */
	public void setNubDataProviderId(long nubDataProviderId) {
		this.nubDataProviderId = nubDataProviderId;
	}

	/**
	 * @param dataProviderDTOFactory the dataProviderDTOFactory to set
	 */
	public void setDataProviderDTOFactory(DTOFactory dataProviderDTOFactory) {
		this.dataProviderDTOFactory = dataProviderDTOFactory;
	}

	/**
	 * @param dataResourceDTOFactory the dataResourceDTOFactory to set
	 */
	public void setDataResourceDTOFactory(DTOFactory dataResourceDTOFactory) {
		this.dataResourceDTOFactory = dataResourceDTOFactory;
	}

	/**
	 * @param imageRecordDAO the imageRecordDAO to set
	 */
	public void setImageRecordDAO(ImageRecordDAO imageRecordDAO) {
		this.imageRecordDAO = imageRecordDAO;
	}

	/**
	 * @param imageRecordDTOFactory the imageRecordDTOFactory to set
	 */
	public void setImageRecordDTOFactory(DTOFactory imageRecordDTOFactory) {
		this.imageRecordDTOFactory = imageRecordDTOFactory;
	}
	
	/**
	 * @param agentDAO the agentDAO to set
	 */
	public void setAgentDAO(AgentDAO agentDAO) {
		this.agentDAO = agentDAO;
	}

	/**
	 * @param dataProviderAgentDTOFactory the dataProviderAgentDTOFactory to set
	 */
	public void setDataProviderAgentDTOFactory(
			DTOFactory dataProviderAgentDTOFactory) {
		this.dataProviderAgentDTOFactory = dataProviderAgentDTOFactory;
	}

	/**
	 * @param dataResourceAgentDTOFactory the dataResourceAgentDTOFactory to set
	 */
	public void setDataResourceAgentDTOFactory(
			DTOFactory dataResourceAgentDTOFactory) {
		this.dataResourceAgentDTOFactory = dataResourceAgentDTOFactory;
	}

	/**
	 * @param keyValueDTOFactory the keyValueDTOFactory to set
	 */
	public void setKeyValueDTOFactory(DTOFactory keyValueDTOFactory) {
		this.keyValueDTOFactory = keyValueDTOFactory;
	}

	/**
	 * @param propertyStoreNamespaceDAO the propertyStoreNamespaceDAO to set
	 */
	public void setPropertyStoreNamespaceDAO(
			PropertyStoreNamespaceDAO propertyStoreNamespaceDAO) {
		this.propertyStoreNamespaceDAO = propertyStoreNamespaceDAO;
	}

	/**
	 * @param resourceAccessPointDAO the resourceAccessPointDAO to set
	 */
	public void setResourceAccessPointDAO(
			ResourceAccessPointDAO resourceAccessPointDAO) {
		this.resourceAccessPointDAO = resourceAccessPointDAO;
	}

	/**
	 * @param propertyStoreNamespaceDTOFactory the propertyStoreNamespaceDTOFactory to set
	 */
	public void setPropertyStoreNamespaceDTOFactory(
			DTOFactory propertyStoreNamespaceDTOFactory) {
		this.propertyStoreNamespaceDTOFactory = propertyStoreNamespaceDTOFactory;
	}

	/**
	 * @param resourceAccessPointDTOFactory the resourceAccessPointDTOFactory to set
	 */
	public void setResourceAccessPointDTOFactory(
			DTOFactory resourceAccessPointDTOFactory) {
		this.resourceAccessPointDTOFactory = resourceAccessPointDTOFactory;
	}
}