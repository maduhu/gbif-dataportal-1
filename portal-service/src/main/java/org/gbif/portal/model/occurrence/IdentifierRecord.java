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
public class IdentifierRecord extends BaseObject {
	/** The provider of the record*/
	protected Long dataProviderId;
	/** The resource within the provider that contains this record */
	protected Long dataResourceId;	
	/** The associated OccurrenceRecord */
	protected Long occurrenceRecordId;	
	/** The identifier type */
	protected IdentifierType identifierType;
	/** Identifier */
	protected String identifier;
	/**
	 * @return the dataProviderId
	 */
	public Long getDataProviderId() {
		return dataProviderId;
	}
	/**
	 * @param dataProviderId the dataProviderId to set
	 */
	public void setDataProviderId(Long dataProviderId) {
		this.dataProviderId = dataProviderId;
	}
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
	public IdentifierType getIdentifierType() {
		return identifierType;
	}
	/**
	 * @param identifierType the identifierType to set
	 */
	public void setIdentifierType(IdentifierType identifierType) {
		this.identifierType = identifierType;
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
}