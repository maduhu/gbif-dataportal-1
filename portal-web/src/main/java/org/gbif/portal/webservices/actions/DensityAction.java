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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.geospatial.CellDensityDTO;
import org.gbif.portal.dto.geospatial.CountryDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.dto.resources.ResourceNetworkDTO;
import org.gbif.portal.dto.taxonomy.TaxonConceptDTO;
import org.gbif.portal.dto.util.EntityType;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.GeospatialManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.util.geospatial.CellIdUtils;
import org.gbif.portal.util.geospatial.LatLongBoundingBox;
import org.gbif.portal.webservices.util.GbifWebServiceException;

/**
 * @author 
 *
 */
public class DensityAction extends Action {

	/* (non-Javadoc)
	 * @see org.gbif.portal.service.GeospatialManager
	 */
	protected GeospatialManager geospatialManager;

	protected DataResourceManager dataResourceManager;
	
	protected TaxonomyManager taxonomyManager;

	public static Log log = LogFactory.getLog(DensityAction.class);
	
	/**
	 * Gets the template of the Density Action.
	 * 
	 * @param parameterMap
	 * @return String with the template
	 */
	public String getTemplate(Map<String,Object> parameterMap)
	{
		DensityParameters params = null;
		
		try {
			params = new DensityParameters(parameterMap, pathMapping);
			
			switch (params.getRequestType()) {
			case LIST:
					if(params.getFormat()==DensityParameters.FORMAT_KML)
						return "org/gbif/portal/ws/density/density-kml.vm";
					else
						return "org/gbif/portal/ws/density/density.vm";
			case HELP:
					return "org/gbif/portal/ws/density/density.vm";
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

	@SuppressWarnings("unchecked")
	public Map<String,Object> findDensityRecords(DensityParameters params) 
	throws GbifWebServiceException {
		
		Map<String,Object> results = new HashMap<String,Object>();
		
		Map<String,String> headerMap;
		Map<String,String> parameterMap;
		Map<String, String> summaryMap=null;
		
		headerMap = returnHeader(params,true);
		parameterMap = returnParameters(params.getParameterMap(null));			
		
		List<CellDensityDTO> densities = null;
		
		
		
		try {
		
			String key = params.getKey();
			if (params.getEntityType() == EntityType.TYPE_COUNTRY) {
				try {
					CountryDTO country = geospatialManager.getCountryForIsoCountryCode(key, Locale.ENGLISH);
					if (country != null) {
						key = country.getKey();
					}
				} catch (Exception e) {
					key = null;
				}
			}
						
			if (key != null) {
			    densities = geospatialManager.get1DegCellDensities(params.getEntityType(), key);
			} else {
				densities = new ArrayList<CellDensityDTO>();
			}		
			
			summaryMap = returnSummary(params, densities, false);
		
			List<Map<String,String>> cellDensityMapList = new LinkedList<Map<String,String>>();			
			Map<String,String> cellDensityMap = null;
			
			//request is of KML format
			if(params.getFormat()==DensityParameters.FORMAT_KML)
			{
				String name = "GBIF Data Portal Occurrence Density Layer";
				String shortName = "Density Layer";
				
				//just customize the name for the KML document
				if (params.getEntityType() == EntityType.TYPE_COUNTRY) {
					shortName = params.getKey();
					name += " for country " + shortName;
				} else if (params.getEntityType() == EntityType.TYPE_DATA_PROVIDER) {
					DataProviderDTO provider = dataResourceManager.getDataProviderFor(params.getKey());
					shortName = provider.getName();
					name += " for data provider: " + shortName; 
				} else if (params.getEntityType() == EntityType.TYPE_DATA_RESOURCE) { 
					DataResourceDTO resource = dataResourceManager.getDataResourceFor(params.getKey());
					shortName = resource.getName();
					name += " for data resource: " + shortName + " from data provider: " + resource.getDataProviderName(); 
				} else if (params.getEntityType() == EntityType.TYPE_RESOURCE_NETWORK) { 
					ResourceNetworkDTO network = dataResourceManager.getResourceNetworkFor(params.getKey());
					shortName = network.getName();
					name += " for resource network: " + shortName; 
				} else if (params.getEntityType() == EntityType.TYPE_TAXON) {
					TaxonConceptDTO taxon = taxonomyManager.getTaxonConceptFor(params.getKey());
					shortName = taxon.getTaxonName();
					name += " for " + taxon.getRank() + ": " + shortName; 
				}
				
				//iterate over all the CellDensityDTOs and create a Map per each one
				for (CellDensityDTO dto : densities) {
										
					cellDensityMap = new TreeMap<String,String>();	
					
					//bounding box
					LatLongBoundingBox bbox = CellIdUtils.toBoundingBox(dto.getCellId());
					//folder name
					String folderName = new Integer(dto.getCount()).toString() + " record" + (dto.getCount() > 1 ? "s" : "");
					//placemark attributes
					String placemarkName = shortName + " - " + folderName;
					String placemarkVisibility = "false";
					String placemarkDescription = "<p><img src=\"" + params.getPortalRoot()
													+ "/images/gbifSmall.gif\"/></p><p><a href=\"" + getCellOccurrenceURL(params, dto, false) + "\">Download</a></p>";
					String placemarkStyleUrl = "#downArrowIcon";
					String placemarkPointCoordinates = new Double((bbox.getMinLong() + bbox.getMaxLong()) / 2).toString()
	                												+ "," + new Double((bbox.getMinLat() + bbox.getMaxLat()) / 2).toString() + ",0";
					//ground overlay attributes
					String groundOverlayVisibility = "true";
					String groundOverlayColor = getDensityColour(dto.getCount());
					String groundOverlayBoxNorth = new Float(bbox.getMaxLat()).toString();
					String groundOverlayBoxSouth = new Float(bbox.getMinLat()).toString();
					String groundOverlayBoxEast = new Float(bbox.getMaxLong()).toString();
					String groundOverlayBoxWest = new Float(bbox.getMinLong()).toString();				
					
					//fill in the map
					cellDensityMap.put("folderName", folderName);
					cellDensityMap.put("placemarkName", placemarkName);
					cellDensityMap.put("placemarkVisibility", placemarkVisibility);
					cellDensityMap.put("placemarkDescription", placemarkDescription);
					cellDensityMap.put("placemarkStyleUrl", placemarkStyleUrl);
					cellDensityMap.put("placemarkPointCoordinates", placemarkPointCoordinates);
					cellDensityMap.put("groundOverlayVisibility", groundOverlayVisibility);
					cellDensityMap.put("groundOverlayColor", groundOverlayColor);
					cellDensityMap.put("groundOverlayBoxNorth", groundOverlayBoxNorth);
					cellDensityMap.put("groundOverlayBoxSouth", groundOverlayBoxSouth);
					cellDensityMap.put("groundOverlayBoxEast", groundOverlayBoxEast);
					cellDensityMap.put("groundOverlayBoxWest", groundOverlayBoxWest);
					
					cellDensityMapList.add(cellDensityMap);
				}
				
				//Attributes for the Map being passed to the Velocity template
				results.put("documentName", name);
				results.put("documentOpen", "true");
				results.put("documentDescription", params.getStatements());
				
				results.put("documentStyleId", "downArrowIcon");
				results.put("documentStyleIconHref", "http://maps.google.com/mapfiles/kml/pal4/icon28.png");
				
				results.put("results", cellDensityMapList);
			}
			else //request is of standard format
 			{				
				String schemaLocation = params.getSchemaLocation();
				//iterate over all the CellDensityDTOs a create a Map per each one
				for(CellDensityDTO dto: densities)
				{				
					cellDensityMap = new HashMap<String,String>();	
					
					//bounding box
					LatLongBoundingBox bbox = CellIdUtils.toBoundingBox(dto.getCellId());					
					
					//Density record attributes
					String cellId = new Integer(dto.getCellId()).toString();
					String count = new Integer(dto.getCount()).toString();
					String portalUrl = getCellOccurrenceURL(params, dto, true);
					String minLatitude = new Float(bbox.getMinLat()).toString();
					String maxLatitude = new Float(bbox.getMaxLat()).toString();
					String minLongitude = new Float(bbox.getMinLong()).toString();
					String maxLongitude = new Float(bbox.getMaxLong()).toString();
					
					//fill the map
					cellDensityMap.put("cellId", cellId);
					cellDensityMap.put("count", count);
					cellDensityMap.put("portalUrl", portalUrl);
					cellDensityMap.put("minLatitude", minLatitude);
					cellDensityMap.put("maxLatitude", maxLatitude);
					cellDensityMap.put("minLongitude", minLongitude);
					cellDensityMap.put("maxLongitude", maxLongitude);
					
					cellDensityMapList.add(cellDensityMap);
				}
				
				//order all the cell density records by their CELL ID (descending order)
				Collections.sort(cellDensityMapList, new Comparator() {
					public int compare(Object a, Object b) {
						Map cellDensity1 = (Map) a;
						Map cellDensity2 = (Map) b;
						Integer key1 = Integer.parseInt(cellDensity1.get("cellId").toString());
						Integer key2 = Integer.parseInt(cellDensity2.get("cellId").toString());
						return key1.compareTo(key2);
					}
				});
				
				
				results.put("results", cellDensityMapList);

				results.put("summaryStart", new BigInteger(new Integer(params.getStartIndex()).toString()));
				results.put("summaryTotalMatched", new BigInteger(new Integer(params.getStartIndex() + densities.size()).toString()));
				results.put("summaryTotalReturned", new BigInteger(new Integer(densities.size()).toString()));
 			}
			
			results.put("headerMap", headerMap);
			results.put("parameterMap", parameterMap);
			results.put("summaryMap", summaryMap);
			
			results.put("count", densities.size());
			
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

	private String getDensityColour(int count) {
		if (count >= 100000) return "af0000cc";
		else if (count >= 10000) return "af0033ff";
		else if (count >= 1000) return "af0066ff";
		else if (count >= 100) return "af0099ff";
		else if (count >= 10) return "af00ccff";
		else return "af00ffff";
	}	
	
	private String getCellOccurrenceURL(DensityParameters params, CellDensityDTO dto, boolean isXML) {
		StringBuffer sb = new StringBuffer();
		sb.append(params.getPortalRoot());
		sb.append("/ws/rest/occurrence/list?format=");
		sb.append(params.getFormatName());
		if(isXML)
			sb.append("&amp;cellid=");
		else
			sb.append("&cellid=");
		sb.append(dto.getCellId());
//		sb.append("&minlatitude=");
//		sb.append(bbox.getMinLat());
//		sb.append("&maxlatitude=");
//		sb.append(bbox.getMaxLat());
//		sb.append("&minlongitude=");
//		sb.append(bbox.getMinLong());
//		sb.append("&maxlongitude=");
//		sb.append(bbox.getMaxLong());
		
		if(isXML)
			sb.append("&amp;");
		else
			sb.append("&");
		
		if (params.getEntityType() == EntityType.TYPE_COUNTRY) {
			sb.append("originisocountrycode=");
			sb.append(params.getKey());
		} else if (params.getEntityType() == EntityType.TYPE_DATA_RESOURCE) {
			sb.append("dataresourcekey=");
			sb.append(params.getKey());
		} else if (params.getEntityType() == EntityType.TYPE_DATA_PROVIDER) {
			sb.append("dataproviderkey=");
			sb.append(params.getKey());
		} else if (params.getEntityType() == EntityType.TYPE_RESOURCE_NETWORK) {
			sb.append("resourcenetworkkey=");
			sb.append(params.getKey());
		} else if (params.getEntityType() == EntityType.TYPE_TAXON) {
			sb.append("taxonconceptkey=");
			sb.append(params.getKey());
		}
		return sb.toString();
	}	

	/**
	 * @return the geospatialManager
	 */
	public GeospatialManager getGeospatialManager() {
		return geospatialManager;
	}

	/**
	 * @param geospatialManager the geospatialManager to set
	 */
	public void setGeospatialManager(GeospatialManager geospatialManager) {
		this.geospatialManager = geospatialManager;
	}
	/**
	 * 
	 * @param taxonomyManager the taxonomyManager to set
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
	}	
	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}	
}
