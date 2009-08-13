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
 * This is a DTO for an occurrence record for KML.
 * 
 * @author jcuadra
 */
public class KmlOccurrenceRecordDTO implements Comparable, Serializable{
	
	/** serial version id */
	private static final long serialVersionUID = 6363084781677208848L;
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
	/** The scientific name */
	protected String taxonName;	
	/** The date of this occurrence */
	protected Date occurrenceDate;
	/** The latitude of this occurrence */
	protected Float latitude;
	/** The longitude of this occurrence */	
	protected Float longitude;
	/** The institution code for this occurrence */		
	protected String institutionCode;
	/** The collection code for this occurrence */		
	protected String collectionCode;
	/** The catalogue number for this occurrence */		
	protected String catalogueNumber;
	/** Nub taxonomy concept if it exists */
	protected String nubTaxonConceptKey;
	/** Taxon concept id **/
	protected String taxonConceptKey;
	/** Citable agent for the data resource **/
	protected String dataResourceCitableAgent;
	/** Taxon name for the nub concept **/
	protected String nubTaxonConceptName;
	
	
	
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
	 * @return the nubTaxonConceptKey
	 */
	public String getNubTaxonConceptKey() {
		return nubTaxonConceptKey;
	}

	/**
	 * @param nubTaxonConceptKey the nubTaxonConceptKey to set
	 */
	public void setNubTaxonConceptKey(String nubTaxonConceptKey) {
		this.nubTaxonConceptKey = nubTaxonConceptKey;
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
	 * @return the dataResourceCitableAgent
	 */
	public String getDataResourceCitableAgent() {
		return dataResourceCitableAgent;
	}

	/**
	 * @param dataResourceCitableAgent the dataResourceCitableAgent to set
	 */
	public void setDataResourceCitableAgent(String dataResourceCitableAgent) {
		this.dataResourceCitableAgent = dataResourceCitableAgent;
	}

	/**
	 * @return the nubTaxonConceptName
	 */
	public String getNubTaxonConceptName() {
		return nubTaxonConceptName;
	}

	/**
	 * @param nubTaxonConceptName the nubTaxonConceptName to set
	 */
	public void setNubTaxonConceptName(String nubTaxonConceptName) {
		this.nubTaxonConceptName = nubTaxonConceptName;
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
	public int compareTo(Object kmlOccurrenceRecordDTO) {
		if(kmlOccurrenceRecordDTO==null || !(kmlOccurrenceRecordDTO instanceof KmlOccurrenceRecordDTO))
			return -1;
		if(((KmlOccurrenceRecordDTO) kmlOccurrenceRecordDTO).getKey()==null)
			return -1;
		return ((KmlOccurrenceRecordDTO) kmlOccurrenceRecordDTO).getKey().compareTo(key);
	}	
}