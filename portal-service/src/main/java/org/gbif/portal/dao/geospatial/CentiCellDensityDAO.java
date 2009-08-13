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

import org.gbif.portal.model.ModelEntityType;
import org.gbif.portal.model.geospatial.CentiCellDensity;

/**
 * The DAO for the CentiCellDensity model object
 * 
 * @author dhobern
 */
public interface CentiCellDensityDAO {
	
	/**
	 * Gets the ordered list of centiCellDensities
	 * @param entityType To return
	 * @param entityId The entity id within the type
	 * @return Th list of matching cell densities
	 */
	public List<CentiCellDensity> getCentiCellDensities(ModelEntityType entityType, List<Long> entityIds, long cellId);
	
}