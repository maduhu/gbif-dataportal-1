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


/**
 * ImageRecordDTO. Image record information
 * 
 * @author Donald Hobern
 */
public class TypificationRecordDTO {

	/** The key for the record */
	protected String key;
	/** The key for the data provider record */
	protected String dataProviderKey;
	/** The provider name */
	protected String dataProviderName;
	/** The key for the data resource record */
	protected String dataResourceKey;
	/** The data resource name */
	protected String dataResourceName;
	/** The key for the occurrence record */
	protected String occurrenceRecordKey;
	protected String occurrenceRecordInstitutionCode;
	protected String occurrenceRecordCollectionCode;
	protected String occurrenceRecordCatalogueNumber;
	
	/** Concept within the taxonomy */
	protected String taxonNameKey;
	/** Concept within the taxonomy */
	protected String scientificName;
	/** Concept within the taxonomy */
	protected String publication;
	/** Concept within the taxonomy */
	protected String typeStatus;

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
	 * @return the occurrenceRecordKey
	 */
	public String getOccurrenceRecordKey() {
		return occurrenceRecordKey;
	}
	/**
	 * @param occurrenceRecordKey the occurrenceRecordKey to set
	 */
	public void setOccurrenceRecordKey(String occurrenceRecordKey) {
		this.occurrenceRecordKey = occurrenceRecordKey;
	}
	/**
	 * @return the publication
	 */
	public String getPublication() {
		return publication;
	}
	/**
	 * @param publication the publication to set
	 */
	public void setPublication(String publication) {
		this.publication = publication;
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
	 * @return the typeStatus
	 */
	public String getTypeStatus() {
		return typeStatus;
	}
	/**
	 * @param typeStatus the typeStatus to set
	 */
	public void setTypeStatus(String typeStatus) {
		this.typeStatus = typeStatus;
	}
	/**
	 * @return Returns the dataProviderName.
	 */
	public String getDataProviderName() {
		return dataProviderName;
	}
	/**
	 * @param dataProviderName The dataProviderName to set.
	 */
	public void setDataProviderName(String dataProviderName) {
		this.dataProviderName = dataProviderName;
	}
	/**
	 * @return Returns the dataResourceName.
	 */
	public String getDataResourceName() {
		return dataResourceName;
	}
	/**
	 * @param dataResourceName The dataResourceName to set.
	 */
	public void setDataResourceName(String dataResourceName) {
		this.dataResourceName = dataResourceName;
	}
	/**
	 * @return Returns the occurrenceRecordCatalogueNumber.
	 */
	public String getOccurrenceRecordCatalogueNumber() {
		return occurrenceRecordCatalogueNumber;
	}
	/**
	 * @param occurrenceRecordCatalogueNumber The occurrenceRecordCatalogueNumber to set.
	 */
	public void setOccurrenceRecordCatalogueNumber(
			String occurrenceRecordCatalogueNumber) {
		this.occurrenceRecordCatalogueNumber = occurrenceRecordCatalogueNumber;
	}
	/**
	 * @return Returns the occurrenceRecordCollectionCode.
	 */
	public String getOccurrenceRecordCollectionCode() {
		return occurrenceRecordCollectionCode;
	}
	/**
	 * @param occurrenceRecordCollectionCode The occurrenceRecordCollectionCode to set.
	 */
	public void setOccurrenceRecordCollectionCode(
			String occurrenceRecordCollectionCode) {
		this.occurrenceRecordCollectionCode = occurrenceRecordCollectionCode;
	}
	/**
	 * @return Returns the occurrenceRecordInstitutionCode.
	 */
	public String getOccurrenceRecordInstitutionCode() {
		return occurrenceRecordInstitutionCode;
	}
	/**
	 * @param occurrenceRecordInstitutionCode The occurrenceRecordInstitutionCode to set.
	 */
	public void setOccurrenceRecordInstitutionCode(
			String occurrenceRecordInstitutionCode) {
		this.occurrenceRecordInstitutionCode = occurrenceRecordInstitutionCode;
	}
}