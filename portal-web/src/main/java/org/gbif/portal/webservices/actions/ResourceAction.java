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
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.dto.resources.PropertyStoreNamespaceDTO;
import org.gbif.portal.dto.resources.ResourceAccessPointDTO;
import org.gbif.portal.dto.resources.ResourceNetworkDTO;
import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.dto.taxonomy.TaxonConceptDTO;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.webservices.util.GbifWebServiceException;

/**
 * @author
 *
 */
public class ResourceAction extends Action {

	/* (non-Javadoc)
	 * @see org.gbif.portal.service.ProviderManager
	 */
	protected DataResourceManager dataResourceManager;
	
	protected TaxonomyManager taxonomyManager;

	public static Log log = LogFactory.getLog(ResourceAction.class);

	/**
	 * Gets the template of the Resource Action.
	 * 
	 * @param parameterMap
	 * @return String with the template
	 */
	public String getTemplate(Map<String,Object> parameterMap)
	{
		ResourceParameters params = null;
		
		try {
			params = new ResourceParameters(parameterMap, pathMapping);
			
			switch (params.getRequestType()) {
			case LIST:
				return "org/gbif/portal/ws/dataresource/dataresource.vm";
			case GET:
				return "org/gbif/portal/ws/dataresource/dataresource-get.vm";
			case COUNT:
				return "org/gbif/portal/ws/dataresource/dataresource-count.vm";
			case HELP:
				return "org/gbif/portal/ws/dataresource/dataresource.vm";
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
	 * Counts the number of Data Resources
	 * 
	 * @param params
	 * @return
	 * @throws GbifWebServiceException
	 */
	public Map<String,Object> countResourceRecords(ResourceParameters params) 
	throws GbifWebServiceException {
		
		Map<String,String> headerMap;
		Map<String,String> parameterMap;
		Map<String, String> summaryMap=null;
		
		Map<String,Object> results = new HashMap<String,Object>();
		Long recordCount = 0L;
		
		headerMap = returnHeader(params,true);
		parameterMap = returnParameters(params.getParameterMap(null));			
		
		try {
			recordCount = dataResourceManager.countDataResources(
					params.getName(),
					true,
					params.getProviderKey(),
					params.getBasisOfRecordCode(),
					params.getModifiedSince());
			
			summaryMap = returnSummary(recordCount);
			
			results.put("count", recordCount);
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
	 * Gets a Data Resource given some parameters
	 * 
	 * @param params
	 * @return
	 * @throws GbifWebServiceException
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getResourceRecord(ResourceParameters params) throws GbifWebServiceException {

		Map<String,String> headerMap;
		Map<String,String> parameterMap;
		Map<String, String> summaryMap=null;
		
		Map<String,Object> results = new HashMap<String,Object>();
		
		Set<DataResourceDTO> list = new HashSet<DataResourceDTO>();

		headerMap = returnHeader(params,true);
		parameterMap = returnParameters(params.getParameterMap(null));			
		
		//Map with the Provider name as the key
		Map<String, Set<Map<String,Object>>> dataProviderMap = new HashMap<String, Set<Map<String,Object>>>();
		
		try {
			DataResourceDTO dataResourceDTO 
				= dataResourceManager.getDataResourceFor(params.getKey());
			
			List<DataResourceDTO> set = new ArrayList<DataResourceDTO>();
			set.add(dataResourceDTO);
			summaryMap = returnSummary(params, set, true);			

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
			
			if(dataProviderMap.get(dataResourceDTO.getDataProviderKey()) != null)
			{
				Set<Map<String, Object>> dataResourceMapListForProvider = dataProviderMap.get(dataResourceDTO.getDataProviderKey());
				dataResourceMapListForProvider.add(dataResourceMap);
				dataProviderMap.put(dataResourceDTO.getDataProviderKey(), dataResourceMapListForProvider);
			}
			else
			{
				Set<Map<String, Object>> dataResourceMapListForProvider = new HashSet<Map<String, Object>>();
				dataResourceMapListForProvider.add(dataResourceMap);
				dataProviderMap.put(dataResourceDTO.getDataProviderKey(), dataResourceMapListForProvider);
			}			
			
			//creates the set of TaxonConcept maps
			Set<Map<String, Object>> taxonConceptMapSet = new TreeSet<Map<String, Object>>(new Comparator() {
				public int compare(Object a, Object b) {
					TaxonConceptDTO taxonConcept1 = (TaxonConceptDTO) ((Map)a).get("taxonConceptDTO");
					TaxonConceptDTO taxonConcept2 = (TaxonConceptDTO) ((Map)b).get("taxonConceptDTO");
					String key1 = taxonConcept1.getTaxonName();
					String key2 = taxonConcept2.getTaxonName();
					return key1.compareToIgnoreCase(key2);
				}
			});				
			
			//get taxon concepts
			List<BriefTaxonConceptDTO> rootConcepts = taxonomyManager.getRootTaxonConceptsForTaxonomy(null, dataResourceDTO.getKey());
			if (rootConcepts != null && rootConcepts.size() > 0) {
				//TaxonConcepts taxonConcepts = resource.addNewTaxonConcepts();
				
				for (BriefTaxonConceptDTO rootConcept : rootConcepts) {
					
					TaxonConceptDTO rootDto = taxonomyManager.getTaxonConceptFor(rootConcept.getKey());
					
					Map<String,Object> taxonConceptMap = new HashMap<String,Object>();		 //add a new taxon concept map
					taxonConceptMap.put("taxonConceptDTO", rootDto);
					
					taxonConceptMapSet.add(taxonConceptMap);
				}
			}
			
			results.put("count", 1);
			results.put("results", dataProviderMap);
			results.put("taxonConceptMapSet", taxonConceptMapSet);
			results.put("headerMap", headerMap);
			results.put("parameterMap", parameterMap);
			results.put("summaryMap", summaryMap);
			
			
			
		} catch (ServiceException se) {
			log.error("Data service error: " + se.getMessage(), se);
			throw new GbifWebServiceException("Data service problems - " + se.getMessage());
		}
		catch (Exception se) {
			log.error("Data service error: " + se.getMessage(), se);
			throw new GbifWebServiceException("Data service problems - " + se.toString());
		}			

			
		return results;
	}

	/**
	 * Gets a list of Data Resources given some parameters
	 * 
	 * @param params
	 * @return
	 * @throws GbifWebServiceException
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> findResourceRecords(ResourceParameters params) 
	throws GbifWebServiceException {
		
		Map<String,String> headerMap;
		Map<String,String> parameterMap;
		Map<String, String> summaryMap;
		
		SearchResultsDTO searchResultsDTO = null;
		List<DataResourceDTO> dataResourceDTOList = null;
		
		Map<String,Object> results = new HashMap<String,Object>();
		
		Map<String, Set<Map<String,Object>>> dataProviderMap = new HashMap<String, Set<Map<String,Object>>>();
			
		headerMap = returnHeader(params,true);
		parameterMap = returnParameters(params.getParameterMap(null));			
		
		try {
			
			searchResultsDTO = dataResourceManager.findDataResources(
					params.getName(),
					true,
					params.getProviderKey(),
					params.getBasisOfRecordCode(),
					params.getModifiedSince(),
					params.getSearchConstraints()); 

			//get the DataResource list for the criteria given
			dataResourceDTOList = (List<DataResourceDTO>)searchResultsDTO.getResults();	
			
			summaryMap = returnSummary(params, dataResourceDTOList, true);
			
			//get the DataProvider list for the criteria given
			//dataProviderList = (List<DataProviderDTO>)searchResultsDTO.getResults();

			//for storing all the DataResources along with its elements
			Set<Map<String, Object>> dataResourceMapList = new HashSet<Map<String, Object>>();			
			
			dataProviderMap = new HashMap<String, Set<Map<String,Object>>>();
			
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
				
				if(dataProviderMap.get(dataResourceDTO.getDataProviderKey()) != null)
				{
					Set<Map<String, Object>> dataResourceMapListForProvider = dataProviderMap.get(dataResourceDTO.getDataProviderKey());
					dataResourceMapListForProvider.add(dataResourceMap);
					dataProviderMap.put(dataResourceDTO.getDataProviderKey(), dataResourceMapListForProvider);
				}
				else
				{
					Set<Map<String, Object>> dataResourceMapListForProvider = new TreeSet<Map<String, Object>>(new Comparator() {
						public int compare(Object a, Object b) {
							DataResourceDTO dataResource1 = (DataResourceDTO) ((Map)a).get("dataResourceDTO");
							DataResourceDTO dataResource2 = (DataResourceDTO) ((Map)b).get("dataResourceDTO");
							String key1 = dataResource1.getName().toString();
							String key2 = dataResource2.getName().toString();
							return key1.compareToIgnoreCase(key2);
						}
					});
					dataResourceMapListForProvider.add(dataResourceMap);
					dataProviderMap.put(dataResourceDTO.getDataProviderKey(), dataResourceMapListForProvider);
				}
				
			}	
			
			results.put("count", 1);
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
	/**
	 * @param taxonomyManager the taxonomyManager to set
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
	}
}
