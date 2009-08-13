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
import org.gbif.portal.model.taxonomy.TaxonConceptLite;

/**
 * OccurrenceRecord Model Object represents a parsed OccurrenceRecord. 
 *
 * @author Donald Hobern
 */
public class ImageRecord extends BaseObject {
	
	/** The resource within the provider that contains this record */
	protected Long dataResourceId;	
	/** The data resource providing this record */
	protected DataResource dataResource;
	/** The associated OccurrenceRecord */
	protected Long occurrenceRecordId;	
	/** The associated OccurrenceRecord */
	protected OccurrenceRecord occurrenceRecord;	
	/** Concept within the taxonomy */
	protected Long taxonConceptId;
	/** Concept within the taxonomy */
	protected TaxonConceptLite taxonConceptLite;	
	/** The image type */
	protected ImageType imageType;
	/** The image type */
	protected int imageTypeValue;
	/** Image URL */
	protected String url;
	/** Description of image */
	protected String description;
	/** Rights information */
	protected String rights;
	/** HTML for display (in cases where thumbnail not possible */
	protected String htmlForDisplay;

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
	 * @return the imageType
	 */
	public ImageType getImageType() {
		return imageType;
	}
	/**
	 * @param imageType the imageType to set
	 */
	public void setImageType(ImageType imageType) {
		this.imageType = imageType;
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
	 * @return the rights
	 */
	public String getRights() {
		return rights;
	}
	/**
	 * @param rights the rights to set
	 */
	public void setRights(String rights) {
		this.rights = rights;
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
	/**
	 * @return the htmlForDisplay
	 */
	public String getHtmlForDisplay() {
		return htmlForDisplay;
	}
	/**
	 * @param htmlForDisplay the htmlForDisplay to set
	 */
	public void setHtmlForDisplay(String htmlForDisplay) {
		this.htmlForDisplay = htmlForDisplay;
	}
	/**
	 * @return the dataResource
	 */
	public DataResource getDataResource() {
		return dataResource;
	}
	/**
	 * @param dataResource the dataResource to set
	 */
	public void setDataResource(DataResource dataResource) {
		this.dataResource = dataResource;
	}
	/**
	 * @return the taxonConceptLite
	 */
	public TaxonConceptLite getTaxonConceptLite() {
		return taxonConceptLite;
	}
	/**
	 * @param taxonConceptLite the taxonConceptLite to set
	 */
	public void setTaxonConceptLite(TaxonConceptLite taxonConceptLite) {
		this.taxonConceptLite = taxonConceptLite;
	}
	/**
	 * @return the imageTypeValue
	 */
	public int getImageTypeValue() {
		return imageTypeValue;
	}
	/**
	 * @param imageTypeValue the imageTypeValue to set
	 */
	public void setImageTypeValue(int imageTypeValue) {
		this.imageTypeValue = imageTypeValue;
	}
	/**
	 * @return the occurrenceRecord
	 */
	public OccurrenceRecord getOccurrenceRecord() {
		return occurrenceRecord;
	}
	/**
	 * @param occurrenceRecord the occurrenceRecord to set
	 */
	public void setOccurrenceRecord(OccurrenceRecord occurrenceRecord) {
		this.occurrenceRecord = occurrenceRecord;
	}
}