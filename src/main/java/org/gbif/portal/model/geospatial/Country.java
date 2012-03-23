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

import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * Holds details of a Country including statistics data regarding
 * the number of occurrence records  the system holds for a particular
 * nation.
 *
 * NOTE:The identifier for this hibernate object isnt the countryId but the IsoCountryCode.
 * This is to enable joins with the OccurrenceRecord on isoCountryCode.
 * 
 * @author dmartin
 *
 * @hibernate.class
 * 	table="country"
 */
public class Country {

	private static final long serialVersionUID = 2140562027917908894L;
	/** The ISO Country Code for this model. Used as the identifier in this model object */
	protected String isoCountryCode;
	/** The country id  - internal id */
	protected Long countryId;	
	/** The number of occurrences records this data resource provides */
	protected Integer occurrenceCount;
	/** The number of occurrences records with geo reference data  this data resource provides */
	protected Integer occurrenceCoordinateCount;
	/** The number of species this data resource provides */
	protected Integer speciesCount;
	/** The 5 continent model code (at time of writing) */
	protected String continentCode;
	/** The UN regions */
	protected String region;
	/** The minimum latitude for this country */
	protected Float minLatitude;
	/** The maximum latitude for this country */
	protected Float maxLatitude;	
	/** The minimum longitude for this country */
	protected Float minLongitude;
	/** The maximum longitude for this country */
	protected Float maxLongitude;		
	/** The names for this country */
	protected Set<CountryName> countryNames;
	
	/**
	 * @hibernate.id 
	 *	generator-class="assigned" 
	 *	unsaved-value="null"
	 *	column="iso_country_code"
	 * 
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
	 * @hibernate.property
	 * 	column="id" 
	 *
	 * @return the id
	 */
	public Long getCountryId() {
		return countryId;
	}

	/**
	 * @param id the id to set
	 */
	public void setCountryId(Long id) {
		this.countryId = id;
	}

	/**
	 * @hibernate.property
	 * 	column="continent_code" 
	 * 
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
	 * @hibernate.property
	 * 	column="occurrence_coordinate_count" 
	 * 
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
	 * @hibernate.property
	 * 	column="occurrence_count" 
	 * 
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
	 * @hibernate.property
	 * 	column="species_count" 
	 * 
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
	 * @hibernate.set
	 * 	lazy="true"
	 * 	cascade="save-update"
	 * 	inverse="true"
	 * @hibernate.key
	 * 	column="iso_country_code"
	 * @hibernate.one-to-many
	 * 	class="org.gbif.portal.model.geospatial.CountryName"
	 * 
	 * @return the countryNames
	 */
	public Set<CountryName> getCountryNames() {
		return countryNames;
	}

	/**
	 * @param countryNames the countryNames to set
	 */
	public void setCountryNames(Set<CountryName> countryNames) {
		this.countryNames = countryNames;
	}

	/**
	 * @hibernate.property
	 * 	column="max_latitude" 
	 *
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
	 * @hibernate.property
	 * 	column="max_longitude" 
	 * 
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
	 * @hibernate.property
	 * 	column="min_latitude" 
	 * 
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
	 * @hibernate.property
	 * 	column="min_longitude" 
	 * 
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
	 * Returns true if object is an instance of BaseObject and 
	 * the identifier value is equal.
	 * Required by ORMS for caching/collections performance.
	 * @see java.lang.Object#equals(Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (object instanceof Country) {
			Country other = (Country) object;
			if (getIsoCountryCode() != null) {
				return getIsoCountryCode().equals(other.getIsoCountryCode());
			}
			else if (this == object) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}	
}