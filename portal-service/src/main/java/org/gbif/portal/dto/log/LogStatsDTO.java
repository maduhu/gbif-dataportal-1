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

/**
 * A Generic dto for logging statistics.
 * 
 * @author dmartin
 */
public class LogStatsDTO implements Serializable {

	private static final long serialVersionUID = 4389944622057555223L;
	/** The entity key */
	private String entityKey;
	/** The entity name */
	private String entityName;
	/** The log event id */
	private Integer eventId;
	/** The log event name see LogEvent */	
	private String eventName;
	/** The number of times this event happened */
	private Integer eventCount = new Integer(0);
	/** The total count for this event  - eg. number of occurrences returned*/
	private Integer count = null;
	
	public LogStatsDTO() {}

	public LogStatsDTO(String entityKey, String entityName, Integer eventId){
		this.entityKey = entityKey;
		this.entityName = entityName;
		this.eventId = eventId;
	}
	
	public LogStatsDTO(String entityKey, String entityName, Integer eventId, String eventName, Integer eventCount, Integer count){
		this.entityKey = entityKey;
		this.entityName = entityName;
		this.eventId = eventId;
		this.eventName = eventName;
		this.eventCount = eventCount;
		this.count = count;
	}
	/**
	 * @return the count
	 */
	public Integer getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(Integer count) {
		this.count = count;
	}
	/**
	 * @return the entityKey
	 */
	public String getEntityKey() {
		return entityKey;
	}
	/**
	 * @param entityKey the entityKey to set
	 */
	public void setEntityKey(String entityKey) {
		this.entityKey = entityKey;
	}
	/**
	 * @return the entityName
	 */
	public String getEntityName() {
		return entityName;
	}
	/**
	 * @param entityName the entityName to set
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	/**
	 * @return the eventCount
	 */
	public Integer getEventCount() {
		return eventCount;
	}
	/**
	 * @param eventCount the eventCount to set
	 */
	public void setEventCount(Integer eventCount) {
		this.eventCount = eventCount;
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
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj==null)
			return false;
		
		//TODO handle nulls properly
		if(obj instanceof LogStatsDTO){
			LogStatsDTO ls = (LogStatsDTO) obj;
			if(ls.getEventId()!=null && ls.getEventId().equals(this.eventId)
				&& 
				ls.getEntityKey()!=null && ls.getEntityKey().equals(this.getEntityKey())
				)
				return true;
		}
		return false;
	}
}