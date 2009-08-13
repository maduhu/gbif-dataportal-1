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

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.dto.geospatial.CellDensityDTO;
import org.gbif.portal.dto.geospatial.LatLongCellDensityDTO;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.util.geospatial.CellIdUtils;
import org.gbif.portal.util.geospatial.LatLongBoundingBox;
import org.gbif.portal.util.geospatial.UnableToGenerateCellIdException;
import org.gbif.portal.web.controller.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * A controller that will populate the model for the map layer requested and return the
 * same view for each to create the tabbed data on the stream
 * 
 * Map layer urls are of the form:
 *   /type/conceptId/size/minLat/minLong
 * 
 * e.g.
 * <ul>
 * 		<li>/<url-root>/123</li>
 * 		<li>/<url-root>/123/tenDeg (defaults to lat=-90, long=-180)</li>
 * 		<li>/<url-root>/123/oneDeg/-90/-180</li>
 * </ul>
 * 
 * @author tim
 * @author dave
 */
public abstract class AbstractMapLayerController extends RestController {
	
	/**
	 * The latitude degrees covered by the map sizes
	 */
	protected Map<String, Integer> latitudeForSize = new HashMap<String, Integer>();
	
	/**
	 * The longitude covered by the map sizes
	 */
	protected Map<String, Integer> longitudeForSize = new HashMap<String, Integer>();
	
	/** the map size request key */
	protected String sizeRequestKey = "size";
	/** Min Longitude Request Key */
	protected String minLongRequestKey = "minLong";
	/** Min Latitude Request Key */
	protected String minLatRequestKey = "minLat";
	/** Max Longitude Request Key */
	protected String maxLongRequestKey = "maxLong";
	/** Max Latitude Request Key */
	protected String maxLatRequestKey = "maxLat";
	/** the densities model key */
	protected String densitiesModelKey = "densities";
	
	protected String tenDegSizeRequestKey = "tenDeg";
	
	protected String oneDegSizeRequestKey = "oneDeg";
	
	/**
	 * Pulls out the URL values to determine what is being requested, calls the correct service layer method 
	 * and populates the view
	 * @TODO this returns null if there is a bad url, is this ok?
	 * @see org.gbif.portal.web.controller.RestController#handleRequest(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ModelAndView handleRequest(Map<String, String> properties, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(logger.isDebugEnabled())
			logger.debug("Generating map for: " + properties);
		
		// the second part of the identifier
		String size = properties.get(sizeRequestKey);
		ModelAndView mav = resolveAndCreateView(properties, request, false);
		
		// degree, decidegree, global, 10x20 or 1x2
		// TODO - this is a little messy right now.
		// when the mapping is properly cleaned up, there will be only the 
		// first "if"
		if (properties.size()==5) {
			logger.debug("Using a BB density");
			try {
				logger.debug("Using a BB density");
				int minLong = Integer.parseInt(properties.get(minLongRequestKey));
				int minLat = Integer.parseInt(properties.get(minLatRequestKey));
				int maxLong = Integer.parseInt(properties.get(maxLongRequestKey));
				int maxLat = Integer.parseInt(properties.get(maxLatRequestKey));
				Set<Integer> cellIds = CellIdUtils.getCellsEnclosedBy(minLat, maxLat, minLong, maxLong);
				if (cellIds.size()>0) {
					if (cellIds.size()<=100) {
						logger.debug("Using a decidegree count density");
						List<LatLongCellDensityDTO> results = new LinkedList<LatLongCellDensityDTO>();
						for (Integer cellId : cellIds) {
							results.addAll(getBounded0Point1WithLatLong(properties, cellId, request, response));
						}
						mav.getModel().put(densitiesModelKey, results);			
					} else {
						List<CellDensityDTO> results = get1DegCellDensities(properties, cellIds, request, response);
						// convert the results to proper lat long boxes
						List<LatLongCellDensityDTO> latLongResults = new LinkedList<LatLongCellDensityDTO>();
						for (CellDensityDTO result : results) {
							LatLongBoundingBox box = CellIdUtils.toBoundingBox(result.getCellId());
							latLongResults.add(new LatLongCellDensityDTO(box.getMinLat(), box.getMaxLat(), box.getMinLong(), box.getMaxLong(), result.getCount()));
						}
						mav.getModel().put(densitiesModelKey, latLongResults);
						
					}
				}
			} catch (NumberFormatException e) {				
			}			
			
		} else if (tenDegSizeRequestKey.equals(size)) {
			logger.debug("Using 10x20 map layer");
			List<CellDensityDTO> results = getBounded1DegData(properties, size, request, response);
			// convert the results to proper lat long boxes
			List<LatLongCellDensityDTO> latLongResults = new LinkedList<LatLongCellDensityDTO>();
			for (CellDensityDTO result : results) {
				LatLongBoundingBox box = CellIdUtils.toBoundingBox(result.getCellId());
				latLongResults.add(new LatLongCellDensityDTO(box.getMinLat(), box.getMaxLat(), box.getMinLong(), box.getMaxLong(), result.getCount()));
			}
			mav.getModel().put(densitiesModelKey, latLongResults);
			
		} else if (oneDegSizeRequestKey.equals(size)) {
			logger.debug("Using 1x2 map layer");
			mav.getModel().put(densitiesModelKey, getBounded0Point1DegData(properties, size, request, response));
			
		} else {
			logger.debug("Using global map layer");
			List<CellDensityDTO> results = get1DegCellDensities(properties, request, response);
			// convert the results to proper lat long boxes
			List<LatLongCellDensityDTO> latLongResults = new LinkedList<LatLongCellDensityDTO>();
			for (CellDensityDTO result : results) {
				LatLongBoundingBox box = CellIdUtils.toBoundingBox(result.getCellId());
				latLongResults.add(new LatLongCellDensityDTO(box.getMinLat(), box.getMaxLat(), box.getMinLong(), box.getMaxLong(), result.getCount()));
			}
			mav.getModel().put(densitiesModelKey, latLongResults);
		}
		
		//allow anything to be added to the model by subclasses
		addToModel(mav, properties, request, response);
		return mav;
	}

	/**
	 * Add to model anything that need adding for the rendered view.
	 * 
	 * @param mav
	 * @param properties
	 * @param request
	 * @param response
	 */
	private void addToModel(ModelAndView mav, Map<String, String> properties,
			HttpServletRequest request, HttpServletResponse response) {}

	/**
	 * Global.
	 * @param properties
	 * @param request
	 * @param response
	 * @return
	 */
	protected abstract List<CellDensityDTO> get1DegCellDensities(Map<String, String> properties, HttpServletRequest request, HttpServletResponse response) throws ServiceException;

	/**
	 * 10x20 - retrieve cell densities for the set of supplied cells.
	 * @param properties
	 * @param cellIds
	 * @param request
	 * @param response
	 * @return
	 */
	protected abstract List<CellDensityDTO> get1DegCellDensities(Map<String, String> properties, Set<Integer> cellIds, HttpServletRequest request, HttpServletResponse response) throws ServiceException;

	/**
	 * 1x2 - retrieve 0.1 degree cell densities for the supplied cell.
	 * @param properties
	 * @param request
	 * @param response
	 * @return
	 */
	protected abstract List<CellDensityDTO> get0Point1DegCellDensities(Map<String, String> properties, int cellId, HttpServletRequest request, HttpServletResponse response) throws ServiceException;
	
	/**
	 * Gets the map layer data using the 1 deg method, and the controller configuration (size offsets)
	 * @param properties From the URL
	 * @param entityType The type of service layer call
	 * @param conceptId To use
	 * @param size The size from the URL
	 * @return The map layer data or an empty list
	 * @throws UnableToGenerateCellIdException If the LAT LONGs are invalid
	 * @throws ServiceException If the service layer reports an error
	 */
	private List<CellDensityDTO> getBounded1DegData(Map<String, String> properties, String size, HttpServletRequest request, HttpServletResponse response) throws UnableToGenerateCellIdException, ServiceException {
		try {
			String minLongAsString = properties.get(minLongRequestKey);
			String minLatAsString = properties.get(minLatRequestKey);
			Integer latSize = latitudeForSize.get(size);
			Integer longSize = longitudeForSize.get(size);
			if (StringUtils.isEmpty(minLongAsString) || StringUtils.isEmpty(minLatAsString)
					|| latSize ==null || longSize==null) {
				return new LinkedList<CellDensityDTO>();
			}
			int minLong = Integer.parseInt(minLongAsString);
			int minLat = Integer.parseInt(minLatAsString);
			int maxLong = minLong+longSize;
			int maxLat = minLat+latSize;
			
			Set<Integer> cellIds = CellIdUtils.getCellsEnclosedBy(minLat, maxLat, minLong, maxLong);
			logCells(cellIds);
			return get1DegCellDensities(properties, cellIds, request, response);
		} catch (NumberFormatException e) {
			return new LinkedList<CellDensityDTO>();
		}
	}

	/**
	 * Gets the map layer data using the 0.1 deg method, and the controller configuration (size offsets)
	 * @param properties From the URL
	 * @param entityType The type of service layer call
	 * @param conceptId To use
	 * @param size The size from the URL
	 * @return The map layer data or an empty list
	 * @throws UnableToGenerateCellIdException If the LAT LONGs are invalid
	 * @throws ServiceException If the service layer reports an error
	 */
	private List<LatLongCellDensityDTO> getBounded0Point1DegData(Map<String, String> properties, String size, HttpServletRequest request, HttpServletResponse response) throws UnableToGenerateCellIdException, ServiceException {
		try {
			String minLongAsString = properties.get(minLongRequestKey);
			String minLatAsString = properties.get(minLatRequestKey);
			Integer latSize = latitudeForSize.get(size);
			Integer longSize = longitudeForSize.get(size);
			if (StringUtils.isEmpty(minLongAsString) || StringUtils.isEmpty(minLatAsString)
					|| latSize ==null || longSize==null) {
				return new LinkedList<LatLongCellDensityDTO>();
			}
			int minLong = Integer.parseInt(minLongAsString);
			int minLat = Integer.parseInt(minLatAsString);
			int maxLong = minLong+longSize;
			int maxLat = minLat+latSize;
			
			Set<Integer>cellIds = CellIdUtils.getCellsEnclosedBy(minLat, maxLat, minLong, maxLong);
			logCells(cellIds);
			List<LatLongCellDensityDTO> results = new LinkedList<LatLongCellDensityDTO>();
			for (Integer cellId : cellIds) {
				if(logger.isDebugEnabled())
					logger.debug("Getting 0.1 data from cellId: " + cellId);
				results.addAll(getBounded0Point1WithLatLong(properties, cellId, request, response));
			}
			return results;
			
		} catch (NumberFormatException e) {
			return new LinkedList<LatLongCellDensityDTO>();
		}
	}
	
	/**
	 * Gets the Lat/Long density 0.1 degree cells for the given parameters
	 * @param cellId To use
	 * @param conceptId Within the cell to search
	 * @param entityType The type of the searcg
	 * @return The list of values or empty list
	 * @throws ServiceException If the service layer is throwing an error
	 */
	private List<LatLongCellDensityDTO> getBounded0Point1WithLatLong(Map<String, String> properties, int cellId, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		List<CellDensityDTO> cellDensities = get0Point1DegCellDensities(properties, cellId, request, response);
		// convert to a list of Lat Long 
		List<LatLongCellDensityDTO> latLongResults = new LinkedList<LatLongCellDensityDTO>();
		for (CellDensityDTO cd : cellDensities) {
			LatLongBoundingBox box = CellIdUtils.toBoundingBox(cellId, cd.getCellId());
			latLongResults.add(new LatLongCellDensityDTO(box.getMinLat(), box.getMaxLat(), box.getMinLong(), box.getMaxLong(), cd.getCount()));
		}
		return latLongResults;
	}
	
	/**
	 * Logs the cells in an ordered manner
	 * @param toLog If debug is enabled
	 */
	private void logCells(Set<Integer> toLog) {
		if (logger.isDebugEnabled()) {
			StringBuffer sb = new StringBuffer("CellIds in use: ");
			List<Integer> ordered = new LinkedList<Integer>(toLog);
			Collections.sort(ordered);
			for (Integer i : ordered) {
				sb.append(i + ",");
			}
			logger.debug(sb);
		}
	}

	/**
	 * @param latitudeForSize the latitudeForSize to set
	 */
	public void setLatitudeForSize(Map<String, Integer> latitudeForSize) {
		this.latitudeForSize = latitudeForSize;
	}

	/**
	 * @param longitudeForSize the longitudeForSize to set
	 */
	public void setLongitudeForSize(Map<String, Integer> longitudeForSize) {
		this.longitudeForSize = longitudeForSize;
	}

	/**
	 * @param densitiesModelKey the densitiesModelKey to set
	 */
	public void setDensitiesModelKey(String densitiesModelKey) {
		this.densitiesModelKey = densitiesModelKey;
	}

	/**
	 * @param sizeRequestKey the sizeRequestKey to set
	 */
	public void setSizeRequestKey(String sizeRequestKey) {
		this.sizeRequestKey = sizeRequestKey;
	}

	/**
	 * @param minLatRequestKey the minLatRequestKey to set
	 */
	public void setMinLatRequestKey(String minLatRequestKey) {
		this.minLatRequestKey = minLatRequestKey;
	}

	/**
	 * @param minLongRequestKey the minLongRequestKey to set
	 */
	public void setMinLongRequestKey(String minLongRequestKey) {
		this.minLongRequestKey = minLongRequestKey;
	}

	/**
	 * @param oneDegSizeRequestKey the oneDegSizeRequestKey to set
	 */
	public void setOneDegSizeRequestKey(String oneDegSizeRequestKey) {
		this.oneDegSizeRequestKey = oneDegSizeRequestKey;
	}

	/**
	 * @param tenDegSizeRequestKey the tenDegSizeRequestKey to set
	 */
	public void setTenDegSizeRequestKey(String tenDegSizeRequestKey) {
		this.tenDegSizeRequestKey = tenDegSizeRequestKey;
	}
}