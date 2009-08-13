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

import java.util.List;
import java.util.Set;

import org.gbif.portal.dao.resources.DataResourceDAO;
import org.gbif.portal.dto.KeyValueDTO;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.resources.DataProviderAgentDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.dto.resources.ResourceNetworkDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.model.resources.DataResource;
import org.gbif.portal.model.resources.ResourceAccessPoint;

/**
 * Tests for the DataResourceManager service interface.
 * 
 * @see DataResourceManager
 *
 * @author Dave Martin
 */
public class DataResourceManagerTest extends AbstractServiceTest {

	public void testGetDataResourceFor() throws Exception {
		DataResourceManager dataResourceManager = (DataResourceManager) getBean("dataResourceManager");
		DataResourceDTO dataResourceDTO = dataResourceManager.getDataResourceFor("1");
		logger.info("Retrieved DTO:"+dataResourceDTO);
	}
	
	public void testGetDataProviderFor() throws Exception {
		DataResourceManager dataResourceManager = (DataResourceManager) getBean("dataResourceManager");
		DataProviderDTO dataProviderDTO = dataResourceManager.getDataProviderFor("1");
		logger.info("Retrieved DTO:"+dataProviderDTO);
	}
	
	public void testGetResourceNetworkFor() throws Exception {
		DataResourceManager dataResourceManager = (DataResourceManager) getBean("dataResourceManager");
		ResourceNetworkDTO resourceNetworkDTO = dataResourceManager.getResourceNetworkFor("1");
		logger.info("Retrieved DTO:"+resourceNetworkDTO);
	}
	
	public void testGetNewestDataResource() throws Exception {
		DataResourceManager dataResourceManager = (DataResourceManager) getBean("dataResourceManager");
		DataResourceDTO dataResourceDTO = dataResourceManager.getNewestDataResource();
		logger.info("Retrieved DTO:"+dataResourceDTO);		
	}

	public void testGetTotalDataResourceCount() throws Exception {
		DataResourceManager dataResourceManager = (DataResourceManager) getBean("dataResourceManager");
		int  count = dataResourceManager.getTotalDataResourceCount();
		logger.info("Retrieved count:"+count);				
	}
	
	public void testGetTotalDataProviderCount() throws Exception {
		DataResourceManager dataResourceManager = (DataResourceManager) getBean("dataResourceManager");
		int  count = dataResourceManager.getTotalDataProviderCount();
		logger.info("Retrieved count:"+count);				
	}
	
	public void testGetDataProvidersOfferingTaxonomies() throws Exception {
		DataResourceManager dataResourceManager = (DataResourceManager) getBean("dataResourceManager");
		List<DataProviderDTO>  dataProviders = dataResourceManager.getDataProvidersOfferingTaxonomies();
		logger.info("Retrieved data providers count:"+dataProviders.size());
		if(dataProviders.size()>0)
			logger.info("First Data Provider: "+dataProviders.get(0));
		dataProviders = dataResourceManager.getDataProvidersOfferingTaxonomies();
		logger.info("Retrieved data providers count:"+dataProviders.size());
	}	
	
	public void testGetDataResourcesWithInferredTaxonomies() throws Exception {
		DataResourceManager dataResourceManager = (DataResourceManager) getBean("dataResourceManager");
		List<DataResourceDTO>  dataResources = dataResourceManager.getDataResourcesWithInferredTaxonomies();
		logger.info("Retrieved data resources count:"+dataResources.size());
		if(dataResources.size()>0)
			logger.info("First Data Resource: "+dataResources.get(0));
		dataResources = dataResourceManager.getDataResourcesWithInferredTaxonomies();
		logger.info("Retrieved data resources count:"+dataResources.size());
	}	
	
	public void testGetNubDataProvider() throws Exception {
		DataResourceManager dataResourceManager = (DataResourceManager) getBean("dataResourceManager");
		DataProviderDTO dataProviderDTO = dataResourceManager.getNubDataProvider();
		logger.info("Retrieved DTO:"+dataProviderDTO);
	}	
	
	public void testFindDataResourcesAndProviders() throws Exception {
		DataResourceManager dataResourceManager = (DataResourceManager) getBean("dataResourceManager");
		SearchResultsDTO searchResultDTO = dataResourceManager.findDatasets("A", true, false, true, new SearchConstraints(0, 10));
		List results = searchResultDTO.getResults();
		for (Object result: results)
			logger.info(result);
	}
	
	public void testFindDataProviders() throws Exception {
		DataResourceManager dataResourceManager = (DataResourceManager) getBean("dataResourceManager");
		SearchResultsDTO searchResultDTO = dataResourceManager.findDataProviders("C", true, null, null, new SearchConstraints(0, 10));
		List results = searchResultDTO.getResults();
		for (Object result: results)
			logger.info(result);
	}	
	
	public void testFindDataProviderForEmail() throws Exception {
		
		DataResourceManager dataResourceManager = (DataResourceManager) getBean("dataResourceManager");
		List<DataProviderDTO> dps = dataResourceManager.findDataProvidersForUser("gbif0001@wdcm.nig.ac.jp");
		for (Object result: dps)
			logger.info(result);
	}		
	
	public void testFindDataResources() throws Exception {
		DataResourceManager dataResourceManager = (DataResourceManager) getBean("dataResourceManager");
		SearchResultsDTO searchResultDTO = dataResourceManager.findDataResources("C", true, null, "unknown", null, new SearchConstraints(0, 10));
		List results = searchResultDTO.getResults();
		for (Object result: results){
			logger.info(result);
		}
	}		

	public void testGetDataResourcesForResourceNetwork() throws Exception {
		DataResourceManager dataResourceManager = (DataResourceManager) getBean("dataResourceManager");
		List<DataResourceDTO>  dataResources = dataResourceManager.getDataResourcesForResourceNetwork("4");
		logger.info("Retrieved data resources count:"+dataResources.size());
		if(dataResources.size()>0)
			logger.info("First Data Resource: "+dataResources.get(0));
	}		

	public void testGetResourceNetworksForDataResource() throws Exception {
		DataResourceManager dataResourceManager = (DataResourceManager) getBean("dataResourceManager");
		List<ResourceNetworkDTO>  resourceNetworks = dataResourceManager.getResourceNetworksForDataResource("9");
		logger.info("Retrieved resource networks count:"+resourceNetworks.size());
		if(resourceNetworks.size()>0)
			logger.info("First Resource Network: "+resourceNetworks.get(0));
	}		
	
	public void testGetAgentsForDataProvider() throws Exception {
		DataResourceManager dataResourceManager = (DataResourceManager) getBean("dataResourceManager");
		List<DataProviderAgentDTO>  agents = dataResourceManager.getAgentsForDataProvider("2");
		logger.info("Retrieved resource networks count:"+agents.size());
		if(agents.size()>0)
			logger.info("First Agent: "+agents.get(0));
	}		
	
	/**
	 * Tests the navigation from DataResource through ResourceAccessPoint and PropertyStoreNamespaces within the RAP
	 * @throws Exception On error
	 */	
	public void testGetPropertyStoreNamespace() throws Exception {
		DataResourceDAO dataResourceDAO = (DataResourceDAO) getBean("dataResourceDAORO");
		DataResource dataResource = dataResourceDAO.getDataResourceFor(41);
		if (dataResource != null) {
			logger.info("Retrieved data resource: " + dataResource.getName());
			Set<ResourceAccessPoint> raps = dataResource.getResourceAccessPoints();
			logger.info("ResourceAccessPoints: " + raps.size());
			for (ResourceAccessPoint rap : raps) {
				logger.info(" - namespaces: " + rap.getPropertyStoreNamespaces());		
			}
		}
	}
	
	public void testGetDataProviderList() throws Exception {
		DataResourceManager dataResourceManager = (DataResourceManager) getBean("dataResourceManager");
		List<KeyValueDTO> dpList = dataResourceManager.getDataProviderList();
		logger.info("Retrieved data providers:"+dpList.size());
		if(dpList.size()>0)
			logger.info("First Agent: "+dpList.get(0));
	}
	
	public void testGetDataResourceList() throws Exception {
		DataResourceManager dataResourceManager = (DataResourceManager) getBean("dataResourceManager");
		List<KeyValueDTO> dpList = dataResourceManager.getDataResourceList("1", null);
		logger.info("Retrieved data providers:"+dpList.size());
		if(dpList.size()>0)
			logger.info("First Agent: "+dpList.get(0));
	}		
	
}