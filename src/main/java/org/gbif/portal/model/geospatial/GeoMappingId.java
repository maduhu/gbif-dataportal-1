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

import java.io.Serializable;

/**
 * Geo Mapping Id
 * 
 * @author dmartin
 */
public class GeoMappingId implements Serializable {
	
	private static final long serialVersionUID = -6737100121725499109L;
	protected Long geoRegionId;
	protected Long occurrenceRecordId;
	
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