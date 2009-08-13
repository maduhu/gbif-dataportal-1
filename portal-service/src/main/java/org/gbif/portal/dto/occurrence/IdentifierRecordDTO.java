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
public class IdentifierRecordDTO {

	/** The key for the record */
	protected String key;
	/** The key for the data provider record */
	protected String dataProviderKey;
	/** The key for the data resource record */
	protected String dataResourceKey;
	/** The key for the occurrence record */
	protected String occurrenceRecordKey;
	/** Identifier type */
	protected Integer identifierType;
	/** Identifier */
	protected String identifier;
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
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}
	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	/**
	 * @return the identifierType
	 */
	public Integer getIdentifierType() {
		return identifierType;
	}
	/**
	 * @param identifierType the identifierType to set
	 */
	public void setIdentifierType(Integer identifierType) {
		this.identifierType = identifierType;
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

}