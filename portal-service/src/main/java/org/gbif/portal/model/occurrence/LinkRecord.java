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
package org.gbif.portal.model.occurrence;

import org.gbif.portal.model.BaseObject;

/**
 * OccurrenceRecord Model Object represents a parsed OccurreceRecord. 
 *
 * @author Donald Hobern
 */
public class LinkRecord extends BaseObject {
	
	/** The resource within the provider that contains this record */
	protected Long dataResourceId;	
	/** The associated OccurrenceRecord */
	protected Long occurrenceRecordId;	
	/** Concept within the taxonomy */
	protected Long taxonConceptId;
	/** The image type */
	protected LinkType linkType;
	/** Image URL */
	protected String url;
	/** Description of resource */
	protected String description;
	/**
	 * @return the dataResourceId
	 */
	public Long getDataResourceId() {
		return dataResourceId;
	}
	/**
	 * @param dataResourceId the dataResourceId to set
	 */
	public void setDataResourceId(Long dataResourceId) {
		this.dataResourceId = dataResourceId;
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
	public LinkType getLinkType() {
		return linkType;
	}
	/**
	 * @param linkType the linkType to set
	 */
	public void setLinkType(LinkType linkType) {
		this.linkType = linkType;
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
	/**
	 * @return the taxonConceptId
	 */
	public Long getTaxonConceptId() {
		return taxonConceptId;
	}
	/**
	 * @param taxonConceptId the taxonConceptId to set
	 */
	public void setTaxonConceptId(Long taxonConceptId) {
		this.taxonConceptId = taxonConceptId;
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
}