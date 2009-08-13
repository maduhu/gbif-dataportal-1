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

package org.gbif.portal.web.controller.maplayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.dto.geospatial.CellDensityDTO;
import org.gbif.portal.dto.util.EntityType;
import org.gbif.portal.service.GeospatialManager;
import org.gbif.portal.service.ServiceException;

/**
 * A controller that will populate the model for the map layer requested and return the
 * same view for each to create the tabbed data on the stream
 * 
 * Map layer urls are of the form:
 *   /type/conceptId/size/minLat/minLong
 * 
 * e.g.
 * <ul>
 * 		<li>/taxon/123</li>
 * 		<li>/country/123/tenDeg (defaults to lat=-90, long=-180)</li>
 * 		<li>/provider/123/oneDeg/-90/-180</li>
 * </ul>
 * 
 * @author tim
 * @author dave
 */
public class EntityMapLayerController extends AbstractMapLayerController {
	
	/**
	 * The manager for generating the map
	 */
	protected GeospatialManager geospatialManager;
	
	/** The mapped type */
	protected int entityType;

	/** The id request key */
	protected String idRequestKey = "id";
	
	/**
	 * @see org.gbif.portal.web.controller.maplayer.AbstractMapLayerController#get1DegCellDensities(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected List<CellDensityDTO> get1DegCellDensities(Map<String, String> properties, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		return geospatialManager.get1DegCellDensities(EntityType.entityTypes.get(entityType), getKeys(properties));
	}	

	/**
	 * @see org.gbif.portal.web.controller.maplayer.AbstractMapLayerController#get1DegCellDensities(java.util.Map, java.util.Set, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected List<CellDensityDTO> get1DegCellDensities(Map<String, String> properties, Set<Integer> cellIds, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		return geospatialManager.get1DegCellDensities(EntityType.entityTypes.get(entityType), getKeys(properties), cellIds);
	}

	/**
	 * @see org.gbif.portal.web.controller.maplayer.AbstractMapLayerController#get0Point1DegCellDensities(java.util.Map, int, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected List<CellDensityDTO> get0Point1DegCellDensities(Map<String, String> properties, int cellId, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		return geospatialManager.get0Point1DegCellDensities(EntityType.entityTypes.get(entityType), getKeys(properties), cellId);
	}	
	
	/**
	 * Retrieve a list of keys
	 * @param properties
	 * @return
	 */
	public List<String> getKeys(Map<String, String> properties){
		 String ids = properties.get(idRequestKey);
		 List<String> keys = new ArrayList<String>();
		 StringTokenizer st = new StringTokenizer(ids, ",");
		 while(st.hasMoreTokens())
			 keys.add(st.nextToken());
		 return keys;
	}	
	
	/**
	 * @param geospatialManager the geospatialManager to set
	 */
	public void setGeospatialManager(GeospatialManager geospatialManager) {
		this.geospatialManager = geospatialManager;
	}

	/**
	 * @param entityType the entityType to set
	 */
	public void setEntityType(int mappedType) {
		this.entityType = mappedType;
	}

	/**
	 * @param idRequestKey the idRequestKey to set
	 */
	public void setIdRequestKey(String idRequestKey) {
		this.idRequestKey = idRequestKey;
	}
}