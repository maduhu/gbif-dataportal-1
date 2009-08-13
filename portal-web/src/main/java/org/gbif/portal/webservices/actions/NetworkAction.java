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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.dto.resources.ResourceNetworkDTO;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.webservices.util.GbifWebServiceException;

/**
 * @author
 *
 */
public class NetworkAction extends Action  {

	/* (non-Javadoc)
	 * @see org.gbif.portal.service.NetworkManager
	 */
	protected DataResourceManager dataResourceManager;

	public static Log log = LogFactory.getLog(NetworkAction.class);
	
	/**
	 * Gets the template of the Network Action.
	 * 
	 * @param parameterMap
	 * @return String with the template
	 */
	public String getTemplate(Map<String,Object> parameterMap)
	{
		NetworkParameters params = null;
		
		try {
			params = new NetworkParameters(parameterMap, pathMapping);
			
			switch (params.getRequestType()) {
			case LIST:
					return "org/gbif/portal/ws/network/network.vm";
			case GET:
					return "org/gbif/portal/ws/network/network.vm";
			case COUNT:
					return "org/gbif/portal/ws/network/network-count.vm";
			case HELP:
					return "org/gbif/portal/ws/network/network.vm";	
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
	 * Count the number of resource networks according to the parameters given
	 * 
	 * @param params
	 * @return 
	 * @throws GbifWebServiceException
	 */
	public Map<String,Object> countNetworkRecords(NetworkParameters params) 
	throws GbifWebServiceException {
		
		Map<String,String> headerMap;
		Map<String,String> parameterMap;
		Map<String, String> summaryMap=null;
		
		headerMap = returnHeader(params,true);
		parameterMap = returnParameters(params.getParameterMap(null));			
		
		Map<String,Object> results = new HashMap<String,Object>();
		Long recordCount = 0L;
		
		try {
			recordCount = dataResourceManager.countResourceNetworks(
					params.getName(),
					true,
					params.getCode(),
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
	 * Gets a Resource network according to the parameters given
	 * 
	 * @param params
	 * @return The Resource and the count (1)
	 * @throws GbifWebServiceException
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getNetworkRecord(NetworkParameters params) throws GbifWebServiceException {
		
		Map<String,Object> results = new HashMap<String,Object>();
		
		Map<String,String> headerMap;
		Map<String,String> parameterMap;
		Map<String, String> summaryMap=null;
		
		headerMap = returnHeader(params,true);
		parameterMap = returnParameters(params.getParameterMap(null));		
		
		Map<ResourceNetworkDTO, Map<String,String>> resourceNetworks = new HashMap<ResourceNetworkDTO, Map<String,String>>();
		
		try {
			ResourceNetworkDTO dto 
				= dataResourceManager.getResourceNetworkFor(params.getKey());
			
			List<ResourceNetworkDTO> set = new ArrayList<ResourceNetworkDTO>();
			set.add(dto);
			summaryMap = returnSummary(params, set, true);				
			
			List<DataResourceDTO> resources = dataResourceManager.getDataResourcesForResourceNetwork(dto.getKey());

			Map<String,String> drMap = new TreeMap<String,String>(new Comparator() {
				public int compare(Object a, Object b) {
					String dataResourceName1 = (String) a;
					String dataResourceName2 = (String) b;
					return dataResourceName1.compareToIgnoreCase(dataResourceName2);
				}
			});
			
			//build a data resource map for each of the resource networks
			for(DataResourceDTO dr: resources)
			{
				drMap.put(dr.getName(), dr.getKey());
			}
			resourceNetworks.put(dto, drMap);

			results.put("results", resourceNetworks);
			results.put("count", 1);

			results.put("headerMap", headerMap);
			results.put("parameterMap", parameterMap);
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
	 * Finds Resource networks for the given parameters
	 * 
	 * @param params
	 * @return A map with the list of the resource networks and the count of them
	 * @throws GbifWebServiceException
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> findNetworkRecords(NetworkParameters params) 
	throws GbifWebServiceException {
		Map<String,Object> results = new HashMap<String,Object>();
		SearchResultsDTO searchResultsDTO = null;
		Map<ResourceNetworkDTO, Map<String,String>> resourceNetworks = new TreeMap<ResourceNetworkDTO, Map<String,String>>(new Comparator() {
			public int compare(Object a, Object b) {
				ResourceNetworkDTO resourceNetwork1 = (ResourceNetworkDTO) a;
				ResourceNetworkDTO resourceNetwork2 = (ResourceNetworkDTO) b;
				return resourceNetwork1.getName().compareToIgnoreCase(resourceNetwork2.getName());
			}
		});
		
		Map<String,String> headerMap;
		Map<String,String> parameterMap;
		Map<String, String> summaryMap;
		
		headerMap = returnHeader(params,true);
		parameterMap = returnParameters(params.getParameterMap(null));			
		
		List<ResourceNetworkDTO> list = null;
		try {
			searchResultsDTO = dataResourceManager.findResourceNetworks(
					params.getName(),
					true,
					params.getCode(),
					params.getModifiedSince(),
					params.getSearchConstraints()); 

			list = (List<ResourceNetworkDTO>)searchResultsDTO.getResults();
			
			summaryMap = returnSummary(params, list, true);
			
			//iterate over all the data resources for this resource network, add each data resource map to the resource network map
			for(ResourceNetworkDTO rn: list)
			{
				List<DataResourceDTO> resources = dataResourceManager.getDataResourcesForResourceNetwork(rn.getKey());
								
				Map<String,String> drMap = new TreeMap<String,String>(new Comparator() {
					public int compare(Object a, Object b) {
						String dataResourceName1 = (String) a;
						String dataResourceName2 = (String) b;
						return dataResourceName1.compareToIgnoreCase(dataResourceName2);
					}
				});
				
				//build a data resource map for each of the resource networks
				for(DataResourceDTO dr: resources)
				{	if(dr!=null)
						drMap.put(dr.getName(), dr.getKey());
				}
				resourceNetworks.put(rn, drMap);
			}
			results.put("results", resourceNetworks);
			results.put("count", resourceNetworks.size());
	
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
	 * @return the NetworkManager
	 */
	public DataResourceManager getDataResourceManager() {
		return dataResourceManager;
	}

	/**
	 * @param NetworkManager the NetworkManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}
}
