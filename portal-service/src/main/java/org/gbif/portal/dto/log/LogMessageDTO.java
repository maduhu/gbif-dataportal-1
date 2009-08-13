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
package org.gbif.portal.dto.log;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for log messages.
 * 
 * @author dmartin
 */
public class LogMessageDTO implements Serializable {
	
	private static final long serialVersionUID = -8698346964707632138L;

	protected String key;
	protected String portalInstanceKey;
	protected Long logGroupId;
	protected Integer eventId;
	protected String eventName;
	protected Long level;
	protected String dataProviderKey;
	protected String dataProviderName;
	protected String dataResourceKey;	
	protected String dataResourceName;
	protected String occurrenceKey;
	protected String taxonConceptKey;
	protected String taxonName;
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
	 * @return the dataProviderName
	 */
	public String getDataProviderName() {
		return dataProviderName;
	}
	/**
	 * @param dataProviderName the dataProviderName to set
	 */
	public void setDataProviderName(String dataProviderName) {
		this.dataProviderName = dataProviderName;
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
	 * @return the dataResourceName
	 */
	public String getDataResourceName() {
		return dataResourceName;
	}
	/**
	 * @param dataResourceName the dataResourceName to set
	 */
	public void setDataResourceName(String dataResourceName) {
		this.dataResourceName = dataResourceName;
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
	 * @return the occurrenceKey
	 */
	public String getOccurrenceKey() {
		return occurrenceKey;
	}
	/**
	 * @param occurrenceKey the occurrenceKey to set
	 */
	public void setOccurrenceKey(String occurrenceKey) {
		this.occurrenceKey = occurrenceKey;
	}
	/**
	 * @return the portalInstanceKey
	 */
	public String getPortalInstanceKey() {
		return portalInstanceKey;
	}
	/**
	 * @param portalInstanceKey the portalInstanceKey to set
	 */
	public void setPortalInstanceKey(String portalInstanceKey) {
		this.portalInstanceKey = portalInstanceKey;
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
	 * @return the taxonName
	 */
	public String getTaxonName() {
		return taxonName;
	}
	/**
	 * @param taxonName the taxonName to set
	 */
	public void setTaxonName(String taxonName) {
		this.taxonName = taxonName;
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
	 * @return the eventName
	 */
	public String getEventName() {
		return eventName;
	}
	/**
	 * @param eventName the eventName to set
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
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