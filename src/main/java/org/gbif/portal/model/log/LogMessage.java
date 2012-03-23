/***************************************************************************
 * Copyright (C) 2005 Global Biodiversity Information Facility Secretariat.
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
package org.gbif.portal.model.log;

import java.util.Date;

import org.gbif.portal.model.BaseObject;
import org.gbif.portal.model.occurrence.OccurrenceRecord;
import org.gbif.portal.model.resources.DataProvider;
import org.gbif.portal.model.resources.DataResource;
import org.gbif.portal.model.taxonomy.TaxonConceptLite;

/**
 * Model object for log messages.
 * 
 * @author dmartin
 */
public class LogMessage extends BaseObject {

	protected Long portalInstanceId;
	protected Long logGroupId;
	protected Integer eventId;
	protected Long level;
	protected Long dataProviderId;
	protected DataProvider dataProvider;
	protected Long dataResourceId;	
	protected DataResource dataResource;
	protected Long occurrenceId;
	protected OccurrenceRecord occurrenceRecord;	
	protected Long taxonConceptId;
	protected TaxonConceptLite taxonConceptLite;
	protected Long userId;
	protected String message;
	protected Long count;
	protected Date timestamp;
	
	/**
	 * @return the count
	 */
	public Long getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(Long count) {
		this.count = count;
	}
	/**
	 * @return the dataProvider
	 */
	public DataProvider getDataProvider() {
		return dataProvider;
	}
	/**
	 * @param dataProvider the dataProvider to set
	 */
	public void setDataProvider(DataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}
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
	 * @return the level
	 */
	public Long getLevel() {
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(Long level) {
		this.level = level;
	}
	/**
	 * @return the logGroupId
	 */
	public Long getLogGroupId() {
		return logGroupId;
	}
	/**
	 * @param logGroupId the logGroupId to set
	 */
	public void setLogGroupId(Long logGroupId) {
		this.logGroupId = logGroupId;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the occurrenceId
	 */
	public Long getOccurrenceId() {
		return occurrenceId;
	}
	/**
	 * @param occurrenceId the occurrenceRecordId to set
	 */
	public void setOccurrenceId(Long occurrenceId) {
		this.occurrenceId = occurrenceId;
	}
	/**
	 * @return the portalInstanceId
	 */
	public Long getPortalInstanceId() {
		return portalInstanceId;
	}
	/**
	 * @param portalInstanceId the portalInstanceId to set
	 */
	public void setPortalInstanceId(Long portalInstanceId) {
		this.portalInstanceId = portalInstanceId;
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
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
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
	/**
	 * @return the eventId
	 */
	public Integer getEventId() {
		return eventId;
	}
	/**
	 * @param eventId the eventId to set
	 */
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
}