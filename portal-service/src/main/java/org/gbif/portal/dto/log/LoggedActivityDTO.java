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
 * An activity with a start and end event.
 * 
 * @author dmartin
 */
public class LoggedActivityDTO implements Serializable, Comparable {

	private static final long serialVersionUID = -5850515186552950945L;

	private String dataProviderKey;
	private String dataProviderName;
	private String dataResourceKey;
	private String dataResourceName;	
	private String eventName;
	private Long logGroup;
	private int startEventId;
	private int endEventId;
	private Date startDate;
	private Long durationInMillisecs;
	private Date endDate;

	public LoggedActivityDTO(){}
	
	/**
	 * @param dataProviderKey
	 * @param dataProviderName
	 * @param dataResourceKey
	 * @param dataResourceName
	 * @param eventName
	 * @param logGroup
	 * @param startEventId
	 * @param endEventId
	 * @param startDate
	 * @param durationInMillisecs
	 * @param endDate
	 */
	public LoggedActivityDTO(String dataProviderKey, String dataProviderName, String dataResourceKey, String dataResourceName, String eventName, Long logGroup, int startEventId, int endEventId, Date startDate,  Date endDate, Long durationInMillisecs) {
		this.dataProviderKey = dataProviderKey;
		this.dataProviderName = dataProviderName;
		this.dataResourceKey = dataResourceKey;
		this.dataResourceName = dataResourceName;
		this.eventName = eventName;
		this.logGroup = logGroup;
		this.startEventId = startEventId;
		this.endEventId = endEventId;
		this.startDate = startDate;
		this.endDate = endDate;		
		this.durationInMillisecs = durationInMillisecs;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the endEventId
	 */
	public int getEndEventId() {
		return endEventId;
	}
	/**
	 * @param endEventId the endEventId to set
	 */
	public void setEndEventId(int endEventId) {
		this.endEventId = endEventId;
	}
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the startEventId
	 */
	public int getStartEventId() {
		return startEventId;
	}
	/**
	 * @param startEventId the startEventId to set
	 */
	public void setStartEventId(int startEventId) {
		this.startEventId = startEventId;
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
	 * @return the logGroup
	 */
	public Long getLogGroup() {
		return logGroup;
	}
	/**
	 * @param logGroup the logGroup to set
	 */
	public void setLogGroup(Long logGroup) {
		this.logGroup = logGroup;
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
	 * @return the durationInMillisecs
	 */
	public Long getDurationInMillisecs() {
		return durationInMillisecs;
	}
	/**
	 * @param durationInMillisecs the durationInMillisecs to set
	 */
	public void setDurationInMillisecs(Long durationInMillisecs) {
		this.durationInMillisecs = durationInMillisecs;
	}
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object object) {
		if(object!=null && object instanceof LoggedActivityDTO){
			LoggedActivityDTO laDTO = (LoggedActivityDTO) object;
			if(laDTO.getLogGroup()!=null && this.getLogGroup()!=null)
				return laDTO.getLogGroup().compareTo(this.getLogGroup());
		}
		return -1;
	}
}