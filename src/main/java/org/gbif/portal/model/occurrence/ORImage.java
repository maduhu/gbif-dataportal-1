package org.gbif.portal.model.occurrence;

import org.gbif.portal.model.BaseObject;

/**
 * A light weight object for retrieving the associated image url for a occurrence record.
 * 
 * @author dmartin
 * @see ImageRecord
 */
public class ORImage extends BaseObject {
	
	/** The associated OccurrenceRecord */
	protected Long occurrenceRecordId;
	/** The type status */
	protected String url;
	
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