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

import org.gbif.portal.model.BaseObject;

/**
 * A Geographic Region. Could be a Protected Area, a National Park or a political region
 * within a country.
 * 
 * @author dmartin
 */
public class GeoRegion extends BaseObject {

	protected String name;
	/** The ISO Country Code for this model. Used as the identifier in this model object */
	protected String isoCountryCode;
	/** The number of occurrences records this data resource provides */
	protected Integer occurrenceCount;
	/** The number of occurrences records with geo reference data  this data resource provides */
	protected Integer occurrenceCoordinateCount;
	/** The type for this region */
	protected Integer regionType;
	/** The number of species this data resource provides */
	protected Integer speciesCount;
	/** The minimum latitude for this country */
	protected Float minLatitude;
	/** The maximum latitude for this country */
	protected Float maxLatitude;	
	/** The minimum longitude for this country */
	protected Float minLongitude;
	/** The maximum longitude for this country */
	protected Float maxLongitude;

	/**
	 * @return the isoCountryCode
	 */
	public String getIsoCountryCode() {
		return isoCountryCode;
	}
	/**
	 * @param isoCountryCode the isoCountryCode to set
	 */
	public void setIsoCountryCode(String isoCountryCode) {
		this.isoCountryCode = isoCountryCode;
	}
	/**
	 * @return the occurrenceCount
	 */
	public Integer getOccurrenceCount() {
		return occurrenceCount;
	}
	/**
	 * @param occurrenceCount the occurrenceCount to set
	 */
	public void setOccurrenceCount(Integer occurrenceCount) {
		this.occurrenceCount = occurrenceCount;
	}
	/**
	 * @return the occurrenceCoordinateCount
	 */
	public Integer getOccurrenceCoordinateCount() {
		return occurrenceCoordinateCount;
	}
	/**
	 * @param occurrenceCoordinateCount the occurrenceCoordinateCount to set
	 */
	public void setOccurrenceCoordinateCount(Integer occurrenceCoordinateCount) {
		this.occurrenceCoordinateCount = occurrenceCoordinateCount;
	}
	/**
	 * @return the speciesCount
	 */
	public Integer getSpeciesCount() {
		return speciesCount;
	}
	/**
	 * @param speciesCount the speciesCount to set
	 */
	public void setSpeciesCount(Integer speciesCount) {
		this.speciesCount = speciesCount;
	}
	/**
	 * @return the minLatitude
	 */
	public Float getMinLatitude() {
		return minLatitude;
	}
	/**
	 * @param minLatitude the minLatitude to set
	 */
	public void setMinLatitude(Float minLatitude) {
		this.minLatitude = minLatitude;
	}
	/**
	 * @return the maxLatitude
	 */
	public Float getMaxLatitude() {
		return maxLatitude;
	}
	/**
	 * @param maxLatitude the maxLatitude to set
	 */
	public void setMaxLatitude(Float maxLatitude) {
		this.maxLatitude = maxLatitude;
	}
	/**
	 * @return the minLongitude
	 */
	public Float getMinLongitude() {
		return minLongitude;
	}
	/**
	 * @param minLongitude the minLongitude to set
	 */
	public void setMinLongitude(Float minLongitude) {
		this.minLongitude = minLongitude;
	}
	/**
	 * @return the maxLongitude
	 */
	public Float getMaxLongitude() {
		return maxLongitude;
	}
	/**
	 * @param maxLongitude the maxLongitude to set
	 */
	public void setMaxLongitude(Float maxLongitude) {
		this.maxLongitude = maxLongitude;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return
	 */
	public Integer getRegionType() {
		return regionType;
	}
	/**
	 * @param regionType
	 */
	public void setRegionType(Integer regionType) {
		this.regionType = regionType;
	}		
}