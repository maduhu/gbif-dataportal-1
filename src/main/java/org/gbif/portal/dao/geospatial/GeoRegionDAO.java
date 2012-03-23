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

import org.gbif.portal.model.geospatial.GeoRegion;
import org.gbif.portal.model.occurrence.OccurrenceRecord;
/**
 * DAO interface providing access to GeoRegion model objects
 * 
 * @author davejmartin
 */
public interface GeoRegionDAO {
	
	/**
	 * Retrieve a list of occurrences for this region.
	 * 
	 * @param geoRegionId
	 * @param startIndex
	 * @param maxResults
	 * @return list of occurrences
	 */
	public List<OccurrenceRecord> getOccurrencesForGeoRegion(Long geoRegionId, int startIndex, int maxResults);
	
	/**
	 * Get Geo Regions for Occurrence Record
	 * 
	 * @param occurrenceRecordId
	 * @return
	 */
	public List<GeoRegion> getGeoRegionsForOccurrenceRecord(Long occurrenceRecordId);
	
	/**
	 * Get the geo region for this id
	 * 
	 * @param geoRegionId
	 * @return
	 */
	public GeoRegion getGeoRegionFor(Long geoRegionId);
	
	public List<GeoRegion> getGeoRegions();
	
	/**
	 * Retrieves a list of key geo region name pairs.
	 * 
	 * @param iso country code
	 * @return
	 */
	public List getGeoRegionsForCountry(String isoCountryCode);	
}
