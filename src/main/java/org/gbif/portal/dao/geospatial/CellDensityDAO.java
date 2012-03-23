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
package org.gbif.portal.dao.geospatial;

import java.util.List;
import java.util.Set;

import org.gbif.portal.model.ModelEntityType;
import org.gbif.portal.model.geospatial.CellDensity;

/**
 * The DAO for the CellDensity model object
 * 
 * @author trobertson
 * @author dmartin
 */
public interface CellDensityDAO {
	
	/**
	 * Gets the ordered list of cellDensities
	 * @param entityType To return
	 * @param entityId The entity id within the type
	 * @return list of matching cell densities
	 */
	public List<CellDensity> getCellDensities(ModelEntityType entityType, List<Long> entityIds);
	
	/**
	 * Gets the ordered list of cellDensities, for the cell ids provided
	 * @param entityType To return
	 * @param entityId The entity id within the type
	 * @param cellIds of interest
	 * @return list of matching cell densities
	 */
	public List<CellDensity> getCellDensities(ModelEntityType entityType, List<Long> entityIds, Set<Integer> cellIds);
	
	/**
	 * Gets the ordered list of cellDensities
	 * @param entityType To return
	 * @param entityId The entity id within the type
	 * @return list of matching cell densities
	 */
	public List<CellDensity> getCellDensities(ModelEntityType entityType, List<Long> entityIds, int minCellId, int maxCellId);	
	
	/**
	 * Gets the sum of the cell densities
	 * 
	 * @param entityType To return
	 * @param entityId The entity id within the type
	 * @return list of matching cell densities
	 */
	public int getCellDensitiesTotal(ModelEntityType entityType, List<Long> entityIds);	
	
	/**
	 * Gets the sum of the cell densities
	 * 
	 * @param entityType To return
	 * @param entityId The entity id within the type
	 * @return list of matching cell densities
	 */
	public int getCellDensitiesTotal(ModelEntityType entityType, List<Long> entityIds, int minCellId, int maxCellId);

	/**
	 * Get totals per country of this entity.
	 * 
	 * @param met
	 * @param entityId
	 * @return
	 */
	public List<Object[]> getTotalsPerCountry(ModelEntityType met, Long entityId);

	/**
	 * Get totals per country of this entity.
	 * 
	 * @param met
	 * @param entityId
	 * @return
	 */
	public List<Object[]> getTotalsPerRegion(ModelEntityType met, Long entityId);	
}