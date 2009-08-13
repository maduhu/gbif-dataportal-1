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
 * Composite class of Occurrence Record DTO and its Raw equivalent in one handy container.
 * 
 * @author dmartin
 */
public class ExtendedOccurrenceRecordDTO extends OccurrenceRecordDTO {

	private static final long serialVersionUID = 7143341047354294350L;

	/** RawOccurrenceRecordDTO */
	protected RawOccurrenceRecordDTO rawOccurrenceRecordDTO;
	/** The type status for this record */
	protected String typeStatus;
	/** The image url record */
	protected String imageUrl;
	/** The guid for this record */
	protected String guid;
	/** The field number for this record */
	protected String fieldNumber;
	/** The collector number for this record */
	protected String collectorNumber;
	
	/**
	 * @return the rawOccurrenceRecordDTO
	 */
	public RawOccurrenceRecordDTO getRawOccurrenceRecordDTO() {
		return rawOccurrenceRecordDTO;
	}

	/**
	 * @param rawOccurrenceRecordDTO the rawOccurrenceRecordDTO to set
	 */
	public void setRawOccurrenceRecordDTO(
			RawOccurrenceRecordDTO rawOccurrenceRecordDTO) {
		this.rawOccurrenceRecordDTO = rawOccurrenceRecordDTO;
	}

	/**
	 * @return the collectorNumber
	 */
	public String getCollectorNumber() {
		return collectorNumber;
	}

	/**
	 * @param collectorNumber the collectorNumber to set
	 */
	public void setCollectorNumber(String collectorNumber) {
		this.collectorNumber = collectorNumber;
	}

	/**
	 * @return the fieldNumber
	 */
	public String getFieldNumber() {
		return fieldNumber;
	}

	/**
	 * @param fieldNumber the fieldNumber to set
	 */
	public void setFieldNumber(String fieldNumber) {
		this.fieldNumber = fieldNumber;
	}

	/**
	 * @return the guid
	 */
	public String getGuid() {
		return guid;
	}

	/**
	 * @param guid the guid to set
	 */
	public void setGuid(String guid) {
		this.guid = guid;
	}

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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
}