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
import org.gbif.portal.model.resources.DataResource;

/**
 * OccurrenceRecord Model Object represents a parsed OccurreceRecord. 
 *
 * @author Donald Hobern
 */
public class TypificationRecord extends BaseObject {
	
	/** The resource within the provider that contains this record */
	protected Long dataResourceId;	
	/** The resource within the provider that contains this record */
	protected DataResource dataResource;	
	/** The associated OccurrenceRecord */
	protected Long occurrenceRecordId;	
	protected OccurrenceRecord occurrenceRecord;
	/** Name within the taxonomy */
	protected Long taxonNameId;
	/** The scientific name */
	protected String scientificName;
	/** The publication */
	protected String publication;
	/** The type status */	
	protected String typeStatus;

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
	 * @return the taxonNameId
	 */
	public Long getTaxonNameId() {
		return taxonNameId;
	}
	/**
	 * @param taxonNameId the taxonNameId to set
	 */
	public void setTaxonNameId(Long taxonNameId) {
		this.taxonNameId = taxonNameId;
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
	 * @return Returns the dataResource.
	 */
	public DataResource getDataResource() {
		return dataResource;
	}
	/**
	 * @param dataResource The dataResource to set.
	 */
	public void setDataResource(DataResource dataResource) {
		this.dataResource = dataResource;
	}
	/**
	 * @return Returns the occurrenceRecord.
	 */
	public OccurrenceRecord getOccurrenceRecord() {
		return occurrenceRecord;
	}
	/**
	 * @param occurrenceRecord The occurrenceRecord to set.
	 */
	public void setOccurrenceRecord(OccurrenceRecord occurrenceRecord) {
		this.occurrenceRecord = occurrenceRecord;
	}
}