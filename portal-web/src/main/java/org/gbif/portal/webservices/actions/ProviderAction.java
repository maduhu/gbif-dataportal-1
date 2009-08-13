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

package org.gbif.portal.webservices.actions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.dto.resources.PropertyStoreNamespaceDTO;
import org.gbif.portal.dto.resources.ResourceAccessPointDTO;
import org.gbif.portal.dto.resources.ResourceNetworkDTO;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.webservices.util.GbifWebServiceException;

/**
 * @author
 *
 */
public class ProviderAction extends Action {

	/* (non-Javadoc)
	 * @see org.gbif.portal.service.ProviderManager
	 */
	protected DataResourceManager dataResourceManager;

	public static Log log = LogFactory.getLog(ProviderAction.class);

	/**
	 * Gets the template of the Provider Action.
	 * 
	 * @param parameterMap
	 * @return String with the template
	 */
	public String getTemplate(Map<String,Object> parameterMap)
	{
		ProviderParameters params = null;
		
		try {
			params = new ProviderParameters(parameterMap, pathMapping);
			
			switch (params.getRequestType()) {
			case LIST:
				return "org/gbif/portal/ws/dataprovider/dataprovider.vm";
			case GET:
				return "org/gbif/portal/ws/dataprovider/dataprovider.vm";
			case COUNT:
				return "org/gbif/portal/ws/dataprovider/dataprovider-count.vm";
			case HELP:
				return "org/gbif/portal/ws/dataprovider/dataprovider.vm";
			default:
				return null;
			}
		} catch (Exception se) {
			log.error("Unregistered data service error: " + se.getMessage(), se);
			if (params == null)
				return null;//gbifMappingFactory.getGbifResponseDocument(parameterMap, se);
			else
				return null;//gbifMappingFactory.getGbifResponseDocument(params, se);
		}
		
	}

	/**
	 * Counts the number of Data Provider records
	 * 
	 * @param params
	 * @return
	 * @throws GbifWebServiceException
	 */
	public Map<String,Object> countProviderRecords(ProviderParameters params) 
	throws GbifWebServiceException {
		
		Map<String,String> headerMap;
		Map<String,String> parameterMap;
		Map<String, String> summaryMap=null;
		
		Map<String,Object> results = new HashMap<String,Object>();
		Long recordCount = 0L;
		
		headerMap = returnHeader(params,true);
		parameterMap = returnParameters(params.getParameterMap(null));			
		
		try {
			recordCount = dataResourceManager.countDataProviders(
					params.getName(),
					true,
					params.getIsoCountryCode(),
					params.getModifiedSince());
			
			summaryMap = returnSummary(recordCount);
			
			results.put("headerMap", headerMap);
			results.put("parameterMap", parameterMap);
			results.put("summaryMap", summaryMap);
			results.put("count", recordCount);
			return results;
			
		} catch (ServiceException se) {
			log.error("Unregistered data service error: " + se.getMessage(), se);
			throw new GbifWebServiceException("Data service problems - " + se.getMessage());
		}
		catch (Exception se) {
			log.error("Data service error: " + se.getMessage(), se);
			throw new GbifWebServiceException("Data service problems - " + se.toString());
		}			
	}

	/**
	 * Gets a Data Provider given some parameters
	 * 
	 * @param params
	 * @return
	 * @throws GbifWebServiceException
	 */
	public Map<String,Object> getProviderRecord(ProviderParameters params) throws GbifWebServiceException {
		
		Map<String,String> headerMap;
		Map<String,String> parameterMap;
		Map<String, String> summaryMap=null;
		
		Map<String,Object> results = new HashMap<String,Object>();
				
		Set<DataProviderDTO> list = new HashSet<DataProviderDTO>();
		
		headerMap = returnHeader(params, true);
		parameterMap = returnParameters(params.getParameterMap(null));			
		
		try {
			DataProviderDTO dataProviderDTO 
				= dataResourceManager.getDataProviderFor(params.getKey());
			
			List<DataProviderDTO> set = new ArrayList<DataProviderDTO>();
			set.add(dataProviderDTO);
			summaryMap = returnSummary(params, set, true);			
			
			//obtain the DataResourceDTOs for this DataProvider
			List<DataResourceDTO> dataResourceDTOList = dataResourceManager.getDataResourcesForProvider(dataProviderDTO.getKey());
			
			//for storing all the DataResources along with its elements
			Set<Map<String, Object>> dataResourceMapList = new HashSet<Map<String, Object>>();
			
			Map<DataProviderDTO, Set<Map<String,Object>>> dataProviderMap = new HashMap<DataProviderDTO, Set<Map<String,Object>>>();
			
			//iterate over the DataResource list to obtain the ResourceNetworks and ResourceAccessPoints for each DR
			for(DataResourceDTO dataResourceDTO: dataResourceDTOList)
			{
				Map<ResourceAccessPointDTO, List<PropertyStoreNamespaceDTO>> resourceAccessPointMap = new HashMap<ResourceAccessPointDTO, List<PropertyStoreNamespaceDTO>>();
				Map<String, Object> dataResourceMap = new HashMap<String,Object>();
				
				List<ResourceAccessPointDTO> resourceAccessPointDTOList = dataResourceManager.getResourceAccessPointsForDataResource(dataResourceDTO.getKey());
				List<ResourceNetworkDTO> resourceNetworkDTOList = dataResourceManager.getResourceNetworksForDataResource(dataResourceDTO.getKey());
				
				//iterate over the ResourceAccessPoints to obtain the PropertyStoreNamespaces
				for(ResourceAccessPointDTO resourceAccessPointDTO: resourceAccessPointDTOList)
				{
					List<PropertyStoreNamespaceDTO> propertyStoreNamespaceDTOList = dataResourceManager.getPropertyStoreNamespacesForResourceAccessPoint(resourceAccessPointDTO.getKey());
					
					//associate each ResourceAccessPointDTO to a list of PropertyStoreNamespace
					resourceAccessPointMap.put(resourceAccessPointDTO, propertyStoreNamespaceDTOList);
				}	
				
				dataResourceMap.put("dataResourceDTO", dataResourceDTO);
				dataResourceMap.put("resourceNetworkDTOList", resourceNetworkDTOList);
				dataResourceMap.put("resourceAccessPointMap", resourceAccessPointMap);
				
				//add the data resource map to the list
				dataResourceMapList.add(dataResourceMap);
			}
			
			dataProviderMap.put(dataProviderDTO, dataResourceMapList);
	
			results.put("headerMap", headerMap);
			results.put("parameterMap", parameterMap);				
			results.put("count", 1);
			results.put("results", dataProviderMap);
			results.put("summaryMap", summaryMap);
			return results;
						
			
		} catch (ServiceException se) {
			log.error("Data service error: " + se.getMessage(), se);
			throw new GbifWebServiceException("Data service problems - " + se.getMessage());
		}			
		catch (Exception se) {
			log.error("Data service error: " + se.getMessage(), se);
			throw new GbifWebServiceException("Data service problems - " + se.toString());
		}			
	}

	/**
	 * Gets a list of Data Providers given some parameters
	 * 
	 * @param params
	 * @return
	 * @throws GbifWebServiceException
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> findProviderRecords(ProviderParameters params) 
	throws GbifWebServiceException {
		
		Map<String,String> headerMap;
		Map<String,String> parameterMap;
		Map<String, String> summaryMap;
		
		SearchResultsDTO searchResultsDTO = null;
		List<DataProviderDTO> dataProviderList = null;
		
		Map<String,Object> results = new HashMap<String,Object>();
		
		//a Tree Map to be able to arrange providers by their name
		Map<DataProviderDTO, Set<Map<String,Object>>> dataProviderMap = new TreeMap<DataProviderDTO, Set<Map<String,Object>>>(new Comparator() {
			public int compare(Object a, Object b) {
				DataProviderDTO dataProvider1 = (DataProviderDTO) a;
				DataProviderDTO dataProvider2 = (DataProviderDTO) b;
				String key1 = dataProvider1.getName().toString();
				String key2 = dataProvider2.getName().toString();
				return key1.compareToIgnoreCase(key2);
			}
		});
		
		headerMap = returnHeader(params,true);
		parameterMap = returnParameters(params.getParameterMap(null));				
		
		try {
			searchResultsDTO = dataResourceManager.findDataProviders(
					params.getName(),
					true,
					params.getIsoCountryCode(),
					params.getModifiedSince(),
					params.getSearchConstraints()); 

			//get the DataProvider list for the criteria given
			dataProviderList = (List<DataProviderDTO>)searchResultsDTO.getResults();

			summaryMap = returnSummary(params, dataProviderList, true);
			
			dataProviderMap = new TreeMap<DataProviderDTO, Set<Map<String,Object>>>(new Comparator() {
				public int compare(Object a, Object b) {
					DataProviderDTO dataProvider1 = (DataProviderDTO) a;
					DataProviderDTO dataProvider2 = (DataProviderDTO) b;
					String key1 = dataProvider1.getName().toString();
					String key2 = dataProvider2.getName().toString();
					return key1.compareToIgnoreCase(key2);
				}
			});
			
			//iterate over the DataProvider list to obtain the DataResources for each DP
			for(DataProviderDTO dataProviderDTO: dataProviderList)
			{
				//obtain the DataResourceDTOs for this DataProvider
				List<DataResourceDTO> dataResourceDTOList = dataResourceManager.getDataResourcesForProvider(dataProviderDTO.getKey());
				
				//for storing all the DataResources along with its elements
				Set<Map<String, Object>> dataResourceMapList = new TreeSet<Map<String, Object>>(new Comparator() {
					public int compare(Object a, Object b) {
						DataResourceDTO dataResource1 = (DataResourceDTO) ((Map)a).get("dataResourceDTO");
						DataResourceDTO dataResource2 = (DataResourceDTO) ((Map)b).get("dataResourceDTO");
						String key1 = dataResource1.getName().toString();
						String key2 = dataResource2.getName().toString();
						return key1.compareToIgnoreCase(key2);
					}
				});
				
				//iterate over the DataResource list to obtain the ResourceNetworks and ResourceAccessPoints for each DR
				for(DataResourceDTO dataResourceDTO: dataResourceDTOList)
				{
					Map<ResourceAccessPointDTO, List<PropertyStoreNamespaceDTO>> resourceAccessPointMap = new HashMap<ResourceAccessPointDTO, List<PropertyStoreNamespaceDTO>>();
					Map<String, Object> dataResourceMap = new HashMap<String,Object>();
					
					List<ResourceAccessPointDTO> resourceAccessPointDTOList = dataResourceManager.getResourceAccessPointsForDataResource(dataResourceDTO.getKey());
					List<ResourceNetworkDTO> resourceNetworkDTOList = dataResourceManager.getResourceNetworksForDataResource(dataResourceDTO.getKey());
					
					//iterate over the ResourceAccessPoints to obtain the PropertyStoreNamespaces
					for(ResourceAccessPointDTO resourceAccessPointDTO: resourceAccessPointDTOList)
					{
						List<PropertyStoreNamespaceDTO> propertyStoreNamespaceDTOList = dataResourceManager.getPropertyStoreNamespacesForResourceAccessPoint(resourceAccessPointDTO.getKey());
						
						//associate each ResourceAccessPointDTO to a list of PropertyStoreNamespace
						resourceAccessPointMap.put(resourceAccessPointDTO, propertyStoreNamespaceDTOList);
					}	
					
					dataResourceMap.put("dataResourceDTO", dataResourceDTO);
					dataResourceMap.put("resourceNetworkDTOList", resourceNetworkDTOList);
					dataResourceMap.put("resourceAccessPointMap", resourceAccessPointMap);
					
					//add the data resource map to the list
					dataResourceMapList.add(dataResourceMap);
				}
				
				dataProviderMap.put(dataProviderDTO, dataResourceMapList);
				
			}
			
			results.put("count", searchResultsDTO.getResults().size());
			results.put("results", dataProviderMap);
			results.put("headerMap", headerMap);
			results.put("parameterMap", parameterMap);
			results.put("summaryMap", summaryMap);
			return results;
			
		} catch (ServiceException se) {
			log.error("Unregistered data service error: " + se.getMessage(), se);
			throw new GbifWebServiceException("Data service problems - " + se.getMessage());
		}		
		catch (Exception se) {
			log.error("Data service error: " + se.getMessage(), se);
			throw new GbifWebServiceException("Data service problems - " + se.toString());
		}			
	}	

	/**
	 * @return the ProviderManager
	 */
	public DataResourceManager getDataResourceManager() {
		return dataResourceManager;
	}

	/**
	 * @param ProviderManager the ProviderManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}
}
