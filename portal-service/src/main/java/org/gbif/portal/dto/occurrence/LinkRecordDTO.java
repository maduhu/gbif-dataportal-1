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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * ImageRecordDTO. Image record information
 * 
 * @author Donald Hobern
 */
public class LinkRecordDTO {

	/** The key for the record */
	protected String key;
	/** The key for the data resource record */
	protected String dataResourceKey;
	/** The key for the occurrence record */
	protected String occurrenceRecordKey;
	/** Concept within the taxonomy */
	protected String taxonConceptKey;
	/** Link type */
	protected Integer linkType;
	/** URL */
	protected String url;
	/** Description */
	protected String description;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the linkType
	 */
	public Integer getLinkType() {
		return linkType;
	}
	/**
	 * @param linkType the linkType to set
	 */
	public void setLinkType(Integer linkType) {
		this.linkType = linkType;
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
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}