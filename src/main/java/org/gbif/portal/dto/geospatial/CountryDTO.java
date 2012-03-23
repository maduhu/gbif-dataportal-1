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
package org.gbif.portal.dto.geospatial;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * A DTO that holds the details of a country.
 * 
 * Note: the name supplied is locale specific.
 * 
 * @author dmartin
 */
public class CountryDTO {

	/** The key for this country */
	protected String key;
	/** The iso country code for this country */
	protected String isoCountryCode;
	/** The locale specific name for this country */
	protected String name;
	/** The number of occurrences records this data resource provides */
	protected Integer occurrenceCount;
	/** The number of occurrences records with geo reference data  this data resource provides */
	protected Integer occurrenceCoordinateCount;
	/** The number of species this data resource provides */
	protected Integer speciesCount;
	/** The continent code */
	protected String continentCode;
	/** The UN region */
	protected String region;
	/** The minimum latitude for this country */
	protected Float minLatitude;
	/** The maximum latitude for this country */
	protected Float maxLatitude;	
	/** The minimum longitude for this country */
	protected Float minLongitude;
	/** The maximum longitude for this country */
	protected Float maxLongitude;	
	/** The String used to identify the supplied - this will be null when not used in the context of a search */
	protected String interpretedFrom;
	
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
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
	 * @return the continentCode
	 */
	public String getContinentCode() {
		return continentCode;
	}
	/**
	 * @param continentCode the continentCode to set
	 */
	public void setContinentCode(String continentCode) {
		this.continentCode = continentCode;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
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
	 * @return Returns the region.
	 */
	public String getRegion() {
		return region;
	}
	/**
	 * @param region The region to set.
	 */
	public void setRegion(String region) {
		this.region = region;
	}
	/**
	 * @return the interpretedFrom
	 */
	public String getInterpretedFrom() {
		return interpretedFrom;
	}
	/**
	 * @param interpretedFrom the interpretedFrom to set
	 */
	public void setInterpretedFrom(String interpretedFrom) {
		this.interpretedFrom = interpretedFrom;
	}		
}