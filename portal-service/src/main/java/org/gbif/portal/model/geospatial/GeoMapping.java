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
package org.gbif.portal.model.geospatial;

import org.gbif.portal.model.occurrence.OccurrenceRecord;

/**
 * A join entity between GeoRegion and OccurrenceRecord.
 * 
 * @author davejmartin
 */
public class GeoMapping {

	protected GeoMappingId identifier;
	protected Long geoRegionId;
	protected Long occurrenceRecordId;	
	protected OccurrenceRecord occurrenceRecord;
	protected GeoRegion geoRegion;
	
	/**
	 * @return the identifier
	 */
	public GeoMappingId getIdentifier() {
		return identifier;
	}
	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(GeoMappingId identifier) {
		this.identifier = identifier;
	}
	/**
	 * @return the occurrenceRecord
	 */
	public OccurrenceRecord getOccurrenceRecord() {
		return occurrenceRecord;
	}
	/**
	 * @param occurrenceRecord the occurrenceRecord to set
	 */
	public void setOccurrenceRecord(OccurrenceRecord occurrenceRecord) {
		this.occurrenceRecord = occurrenceRecord;
	}
	/**
	 * @return the geoRegion
	 */
	public GeoRegion getGeoRegion() {
		return geoRegion;
	}
	/**
	 * @param geoRegion the geoRegion to set
	 */
	public void setGeoRegion(GeoRegion geoRegion) {
		this.geoRegion = geoRegion;
	}
	/**
	 * @return the geoRegionId
	 */
	public Long getGeoRegionId() {
		return geoRegionId;
	}
	/**
	 * @param geoRegionId the geoRegionId to set
	 */
	public void setGeoRegionId(Long geoRegionId) {
		this.geoRegionId = geoRegionId;
	}
	/**
	 * @return the occurrenceRecordId
	 */
	public Long getOccurrenceRecordId() {
		return occurrenceRecordId;
	}
	/**
	 * @param occurrenceRecordId the occurrenceRecordId to set
	 */
	public void setOccurrenceRecordId(Long occurrenceRecordId) {
		this.occurrenceRecordId = occurrenceRecordId;
	}
}