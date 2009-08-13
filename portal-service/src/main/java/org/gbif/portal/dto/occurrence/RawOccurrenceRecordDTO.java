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
package org.gbif.portal.dto.occurrence;

import java.util.Date;

/**
 * A DTO representing the Raw Occurrence Record
 * 
 * @author dmartin
 */
public class RawOccurrenceRecordDTO {

	private static final long serialVersionUID = 6444530612359091030L;
	/** The key for this raw occurrence record */
	protected String key;
	/** The provider of the record*/
	protected String dataProviderKey;
	/** The provider of the record*/
	protected String dataProviderName;	
	/** The resource within the provider that contains this record */
	protected String dataResourceKey;	
	/** The resource within the provider that contains this record */
	protected String dataResourceName;		
	/** The resource access point for this record */
	protected String resourceAccessPointKey;	
	/**  Institution code*/
	protected String institutionCode;
	/** Collection code */
	protected String collectionCode;
	/** Catalogue Number */
	protected String catalogueNumber;
	/** The scientific name */
	protected String scientificName;
	/** Author of the scientific name */
	protected String author;
	/** Rank of the scientific name */
	protected String rank;
	/** Kingdom */
	protected String kingdom;
	/** Phylum */
	protected String phylum;
	/** Class */
	protected String bioClass;
	/** Order */
	protected String order;
	/** Family */
	protected String family;
	/** Genus */
	protected String genus;
	/** Species */
	protected String species;
	/** Subspecies */
	protected String subspecies;
	/** Latitude */
	protected String latitude;
	/** Longitude */
	protected String longitude;
	/** Precision of latitude and longitude */
	protected String latLongPrecision;	
	/** Minimum altitude of occurrence */
	protected String minAltitude;
	/** Maximum altitude of occurrence */
	protected String maxAltitude;
	/** Precision of altitude of occurrence */
	protected String altitudePrecision;
	/** Minimum depth of occurrence */
	protected String minDepth;
	/** Maximum depth of occurrence */
	protected String maxDepth;
	/** Precision of depth of occurrence */
	protected String depthPrecision;
	/** Country of occurrence */
	protected String country;
	/** The continent/ocean of occurrence */
	protected String continentOrOcean;
	/** The state/province of occurrence */
	protected String stateOrProvince;
	/** The county of occurrence */
	protected String county;
	/** The name of the collector */
	protected String collectorName;
	/** The name of the identifier */
	protected String identifierName;
	/** The identification date */
	protected Date identificationDate;
	/** The locality name */
	protected String locality;
	/** The year of occurrence */
	protected String year;
	/** The month of occurrence */
	protected String month;
	/** The day (in month) of occurrence */
	protected String day;
	/** The basis of record */
	protected String basisOfRecord;
	/** The record created date */
	protected Date created;	
	/** The record modification date */
	protected Date modified;
	/** Formatted date of the occurrence */
	protected String occurrenceDate;	
	/**
	 * @return the altitudePrecision
	 */
	public String getAltitudePrecision() {
		return altitudePrecision;
	}
	/**
	 * @param altitudePrecision the altitudePrecision to set
	 */
	public void setAltitudePrecision(String altitudePrecision) {
		this.altitudePrecision = altitudePrecision;
	}
	/**
	 * @return the maxAltitude
	 */
	public String getMaxAltitude() {
		return maxAltitude;
	}
	/**
	 * @param maxAltitude the maxAltitude to set
	 */
	public void setMaxAltitude(String maxAltitude) {
		this.maxAltitude = maxAltitude;
	}
	/**
	 * @return the minAltitude
	 */
	public String getMinAltitude() {
		return minAltitude;
	}
	/**
	 * @param minAltitude the minAltitude to set
	 */
	public void setMinAltitude(String minAltitude) {
		this.minAltitude = minAltitude;
	}
	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	/**
	 * @return the basisOfRecord
	 */
	public String getBasisOfRecord() {
		return basisOfRecord;
	}
	/**
	 * @param basisOfRecord the basisOfRecord to set
	 */
	public void setBasisOfRecord(String basisOfRecord) {
		this.basisOfRecord = basisOfRecord;
	}
	/**
	 * @return the bioClass
	 */
	public String getBioClass() {
		return bioClass;
	}
	/**
	 * @param bioClass the bioClass to set
	 */
	public void setBioClass(String bioClass) {
		this.bioClass = bioClass;
	}
	/**
	 * @return the catalogueNumber
	 */
	public String getCatalogueNumber() {
		return catalogueNumber;
	}
	/**
	 * @param catalogueNumber the catalogueNumber to set
	 */
	public void setCatalogueNumber(String catalogueNumber) {
		this.catalogueNumber = catalogueNumber;
	}
	/**
	 * @return the collectionCode
	 */
	public String getCollectionCode() {
		return collectionCode;
	}
	/**
	 * @param collectionCode the collectionCode to set
	 */
	public void setCollectionCode(String collectionCode) {
		this.collectionCode = collectionCode;
	}
	/**
	 * @return the collectorName
	 */
	public String getCollectorName() {
		return collectorName;
	}
	/**
	 * @param collectorName the collectorName to set
	 */
	public void setCollectorName(String collectorName) {
		this.collectorName = collectorName;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return the county
	 */
	public String getCounty() {
		return county;
	}
	/**
	 * @param county the county to set
	 */
	public void setCounty(String county) {
		this.county = county;
	}
	/**
	 * @return the day
	 */
	public String getDay() {
		return day;
	}
	/**
	 * @param day the day to set
	 */
	public void setDay(String day) {
		this.day = day;
	}
	/**
	 * @return the maxDepth
	 */
	public String getMaxDepth() {
		return maxDepth;
	}
	/**
	 * @param maxDepth the maxDepth to set
	 */
	public void setMaxDepth(String maxDepth) {
		this.maxDepth = maxDepth;
	}
	/**
	 * @return the minDepth
	 */
	public String getMinDepth() {
		return minDepth;
	}
	/**
	 * @param minDepth the minDepth to set
	 */
	public void setMinDepth(String minDepth) {
		this.minDepth = minDepth;
	}
	/**
	 * @return the depthPrecision
	 */
	public String getDepthPrecision() {
		return depthPrecision;
	}
	/**
	 * @param depthPrecision the depthPrecision to set
	 */
	public void setDepthPrecision(String depthPrecision) {
		this.depthPrecision = depthPrecision;
	}
	/**
	 * @return the family
	 */
	public String getFamily() {
		return family;
	}
	/**
	 * @param family the family to set
	 */
	public void setFamily(String family) {
		this.family = family;
	}
	/**
	 * @return the genus
	 */
	public String getGenus() {
		return genus;
	}
	/**
	 * @param genus the genus to set
	 */
	public void setGenus(String genus) {
		this.genus = genus;
	}
	/**
	 * @return the institutionCode
	 */
	public String getInstitutionCode() {
		return institutionCode;
	}
	/**
	 * @param institutionCode the institutionCode to set
	 */
	public void setInstitutionCode(String institutionCode) {
		this.institutionCode = institutionCode;
	}
	/**
	 * @return the kingdom
	 */
	public String getKingdom() {
		return kingdom;
	}
	/**
	 * @param kingdom the kingdom to set
	 */
	public void setKingdom(String kingdom) {
		this.kingdom = kingdom;
	}
	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the latLongPrecision
	 */
	public String getLatLongPrecision() {
		return latLongPrecision;
	}
	/**
	 * @param latLongPrecision the latLongPrecision to set
	 */
	public void setLatLongPrecision(String latLongPrecision) {
		this.latLongPrecision = latLongPrecision;
	}
	/**
	 * @return the locality
	 */
	public String getLocality() {
		return locality;
	}
	/**
	 * @param locality the locality to set
	 */
	public void setLocality(String locality) {
		this.locality = locality;
	}
	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	/**
	 * @return the month
	 */
	public String getMonth() {
		return month;
	}
	/**
	 * @param month the month to set
	 */
	public void setMonth(String month) {
		this.month = month;
	}
	/**
	 * @return the order
	 */
	public String getOrder() {
		return order;
	}
	/**
	 * @param order the order to set
	 */
	public void setOrder(String order) {
		this.order = order;
	}
	/**
	 * @return the phylum
	 */
	public String getPhylum() {
		return phylum;
	}
	/**
	 * @param phylum the phylum to set
	 */
	public void setPhylum(String phylum) {
		this.phylum = phylum;
	}
	/**
	 * @return the rank
	 */
	public String getRank() {
		return rank;
	}
	/**
	 * @param rank the rank to set
	 */
	public void setRank(String rank) {
		this.rank = rank;
	}
	/**
	 * @return the scientificName
	 */
	public String getScientificName() {
		return scientificName;
	}
	/**
	 * @param scientificName the scientificName to set
	 */
	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}
	/**
	 * @return the species
	 */
	public String getSpecies() {
		return species;
	}
	/**
	 * @param species the species to set
	 */
	public void setSpecies(String species) {
		this.species = species;
	}
	/**
	 * @return the subspecies
	 */
	public String getSubspecies() {
		return subspecies;
	}
	/**
	 * @param subspecies the subspecies to set
	 */
	public void setSubspecies(String subspecies) {
		this.subspecies = subspecies;
	}
	/**
	 * @return the stateOrProvince
	 */
	public String getStateOrProvince() {
		return stateOrProvince;
	}
	/**
	 * @param stateOrProvince the stateOrProvince to set
	 */
	public void setStateOrProvince(String stateOrProvince) {
		this.stateOrProvince = stateOrProvince;
	}
	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}
	/**
	 * @return the dataProviderKey
	 */
	public String getDataProviderKey() {
		return dataProviderKey;
	}
	/**
	 * @param dataProviderKey the dataProviderKey to set
	 */
	public void setDataProviderKey(String dataProviderKey) {
		this.dataProviderKey = dataProviderKey;
	}
	/**
	 * @return the dataResourceKey
	 */
	public String getDataResourceKey() {
		return dataResourceKey;
	}
	/**
	 * @param dataResourceKey the dataResourceKey to set
	 */
	public void setDataResourceKey(String dataResourceKey) {
		this.dataResourceKey = dataResourceKey;
	}
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
	 * @return the resourceAccessPointKey
	 */
	public String getResourceAccessPointKey() {
		return resourceAccessPointKey;
	}
	/**
	 * @param resourceAccessPointKey the resourceAccessPointKey to set
	 */
	public void setResourceAccessPointKey(String resourceAccessPointKey) {
		this.resourceAccessPointKey = resourceAccessPointKey;
	}
	/**
	 * @return the modified
	 */
	public Date getModified() {
		return modified;
	}
	/**
	 * @param modified the modified to set
	 */
	public void setModified(Date modified) {
		this.modified = modified;
	}
	/**
	 * @return the continentOrOcean
	 */
	public String getContinentOrOcean() {
		return continentOrOcean;
	}
	/**
	 * @param continentOrOcean the continentOrOcean to set
	 */
	public void setContinentOrOcean(String continentOrOcean) {
		this.continentOrOcean = continentOrOcean;
	}
	/**
	 * @return the identificationDate
	 */
	public Date getIdentificationDate() {
		return identificationDate;
	}
	/**
	 * @param identificationDate the identificationDate to set
	 */
	public void setIdentificationDate(Date identificationDate) {
		this.identificationDate = identificationDate;
	}
	/**
	 * @return the identifierName
	 */
	public String getIdentifierName() {
		return identifierName;
	}
	/**
	 * @param identifierName the identifierName to set
	 */
	public void setIdentifierName(String identifierName) {
		this.identifierName = identifierName;
	}
	/**
	 * @return the dataProviderName
	 */
	public String getDataProviderName() {
		return dataProviderName;
	}
	/**
	 * @param dataProviderName the dataProviderName to set
	 */
	public void setDataProviderName(String dataProviderName) {
		this.dataProviderName = dataProviderName;
	}
	/**
	 * @return the dataResourceName
	 */
	public String getDataResourceName() {
		return dataResourceName;
	}
	/**
	 * @param dataResourceName the dataResourceName to set
	 */
	public void setDataResourceName(String dataResourceName) {
		this.dataResourceName = dataResourceName;
	}
	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}
	/**
	 * @return the occurrenceDate
	 */
	public String getOccurrenceDate() {
		return occurrenceDate;
	}
	/**
	 * @param occurrenceDate the occurrenceDate to set
	 */
	public void setOccurrenceDate(String occurrenceDate) {
		this.occurrenceDate = occurrenceDate;
	}	
}