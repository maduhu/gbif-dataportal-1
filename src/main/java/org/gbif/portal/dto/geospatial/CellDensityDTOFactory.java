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
package org.gbif.portal.dto.geospatial;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.BaseDTOFactory;
import org.gbif.portal.model.geospatial.CellDensity;
import org.gbif.portal.model.geospatial.CentiCellDensity;

/**
 * Factory of CellDensityDTO from the CellDensity model object
 * 
 * @author trobertson
 */
public class CellDensityDTOFactory extends BaseDTOFactory {
	/**
	 * Logger
	 */
	protected static Log logger = LogFactory.getLog(CellDensityDTOFactory.class);
	
	/**
	 * @see org.gbif.portal.dto.DTOFactory#createDTO(java.lang.Object)
	 */
	public Object createDTO(Object modelObject) {
		if (modelObject instanceof CellDensity) {
			CellDensityDTO target = new CellDensityDTO();
			CellDensity source = (CellDensity) modelObject;
			target.setCellId(source.getIdentifier().getCellId());
			target.setCount(source.getCount());
			return target;
		} else if (modelObject instanceof Object[]){
			// Filter searches for example can produce this
			Object[] point = (Object[]) modelObject;
			CellDensityDTO target = new CellDensityDTO();
			target.setCellId(Integer.parseInt(point[0].toString()));
			target.setCount(Integer.parseInt(point[1].toString()));
			return target;
		} else {
			return null;
		}
	}
	
	/**
	 * Creates the list of cellDensity objects from a list of array
	 * array[0] is the cell id
	 * array[1] is the count
	 * @return the list
	 */
	public List<CellDensityDTO> createDTOListFromArray(List arrayObjects) {
		List<CellDensityDTO> results = new LinkedList<CellDensityDTO>();
		for (Object o : arrayObjects) {
			try {
				Object[] asArray = (Object[]) o;
				if (asArray.length != 2) {
					logger.warn("Ignoring record as not an integer array of size 2.  Size: " + asArray.length);
				} else {
					try {
						results.add(new CellDensityDTO((Short) asArray[0], ((BigInteger) asArray[1]).intValue()));
					} catch (ClassCastException e) {
						logger.warn("Ignoring record as not an integer", e);
					}
				}
			} catch (ClassCastException e) {
				logger.warn("Ignoring record as not an integer array", e);
			}
		}
		return results;
	}
	
	/**
	 * Creates the list of cellDensity objects from a list of CentiCellDensity objects
	 * @return the list
	 */
	public List<CellDensityDTO> createDTOListFromCentiCellList(List<CentiCellDensity> centiCells) {
		List<CellDensityDTO> results = new LinkedList<CellDensityDTO>();
		for (CentiCellDensity centiCell : centiCells) {
			results.add(new CellDensityDTO(centiCell.getIdentifier().getCentiCellId(), centiCell.getCount()));
		}
		return results;
	}
}