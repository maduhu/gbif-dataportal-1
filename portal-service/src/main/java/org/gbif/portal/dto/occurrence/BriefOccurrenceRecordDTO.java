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

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This is a DTO for an occurrence record in a brief form.
 * 
 * @author dmartin
 * @author trobertson
 */
public class BriefOccurrenceRecordDTO implements Comparable, Serializable{
	
	/** serial version id */
	private static final long serialVersionUID = -7651538825048032570L;
	/** The key for this occurrence record */
	protected String key;
	/** The key for the data provider */
	protected String dataProviderKey;
	/** The data provider name */	
	protected String dataProviderName;		
	/** The key for the data resource occurrence record */
	protected String dataResourceKey;
	/** The name of the data resource occurrence record */	
	protected String dataResourceName;	
	/** The taxon concept */
	protected String taxonConceptKey;
	/** The scientific name */
	protected String taxonNameKey;
	/** The scientific name */
	protected String taxonName;
	/** The date of this occurrence */
	protected Date occurrenceDate;
	/** The latitude of this occurrence */
	protected Float latitude;
	/** The longitude of this occurrence */	
	protected Float longitude;
	/** The country of this occurrence */		
	protected String isoCountryCode;
	/** The institution code for this occurrence */		
	protected String institutionCode;
	/** The collection code for this occurrence */		
	protected String collectionCode;
	/** The catalogue number for this occurrence */		
	protected String catalogueNumber;
	/** The basis of record */
	protected String basisOfRecord;	
	/** Array of issues with taxoonomic data */
	protected Integer taxonomicIssue;	
	/** Array of issues with geospatial data */
	protected Integer geospatialIssue;
	/** Array of issues with other data */
	protected Integer otherIssue;
	/** Modification date for record */
	protected Date modified;

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
	 * @return the latitude
	 */
	public Float getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public Float getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the occurrenceDate
	 */
	public Date getOccurrenceDate() {
		return occurrenceDate;
	}

	/**
	 * @param occurrenceDate the occurrenceDate to set
	 */
	public void setOccurrenceDate(Date occurrenceDate) {
		this.occurrenceDate = occurrenceDate;
	}

	/**
	 * @return the taxonConceptKey
	 */
	public String getTaxonConceptKey() {
		return taxonConceptKey;
	}

	/**
	 * @param taxonConceptKey the taxonConceptKey to set
	 */
	public void setTaxonConceptKey(String taxonConceptKey) {
		this.taxonConceptKey = taxonConceptKey;
	}

	/**
	 * @return the taxonName
	 */
	public String getTaxonName() {
		return taxonName;
	}

	/**
	 * @param taxonName the taxonName to set
	 */
	public void setTaxonName(String taxonName) {
		this.taxonName = taxonName;
	}

	/**
	 * @return the taxonNameKey
	 */
	public String getTaxonNameKey() {
		return taxonNameKey;
	}

	/**
	 * @param taxonNameKey the taxonNameKey to set
	 */
	public void setTaxonNameKey(String taxonNameKey) {
		this.taxonNameKey = taxonNameKey;
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
	 * @return the geospatialIssue
	 */
	public Integer getGeospatialIssue() {
		return geospatialIssue;
	}

	/**
	 * @param geospatialIssue the geospatialIssue to set
	 */
	public void setGeospatialIssue(Integer geospatialIssue) {
		this.geospatialIssue = geospatialIssue;
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
	 * @return the otherIssue
	 */
	public Integer getOtherIssue() {
		return otherIssue;
	}

	/**
	 * @param otherIssue the otherIssue to set
	 */
	public void setOtherIssue(Integer otherIssue) {
		this.otherIssue = otherIssue;
	}

	/**
	 * @return the taxonomicIssue
	 */
	public Integer getTaxonomicIssue() {
		return taxonomicIssue;
	}

	/**
	 * @param taxonomicIssue the taxonomicIssue to set
	 */
	public void setTaxonomicIssue(Integer taxonomicIssue) {
		this.taxonomicIssue = taxonomicIssue;
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object briefOccurrenceRecordDTO) {
		if(briefOccurrenceRecordDTO==null || !(briefOccurrenceRecordDTO instanceof BriefOccurrenceRecordDTO))
			return -1;
		if(((BriefOccurrenceRecordDTO) briefOccurrenceRecordDTO).getKey()==null)
			return -1;
		return ((BriefOccurrenceRecordDTO) briefOccurrenceRecordDTO).getKey().compareTo(key);
	}	
}