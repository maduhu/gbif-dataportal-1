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

package org.gbif.portal.dto.util;

import java.util.Date;

import org.gbif.portal.service.ServiceException;

/**
 * A DTO to represent a duration in time.  The duration has a start 
 * java.util.Date and an end java.util.Date 
 * 
 * @author Ali Kalufya
 * @author trobertson
 */
public class TimePeriodDTO {
	
	/** The start of this time period duration */
	protected Date startPeriod; 
	/** The end of this time period duration */
	protected Date endPeriod;

	public TimePeriodDTO() {}	
	
	/**
	 * Constructs with start and end dates
	 * @param startPeriod The start of this TimePeriod 
	 * 	(use null to indicate no start - e.g. before a date)
	 * @param endPeriod The end of this TimePeriod 
	 *  (use null to indicate no end - e.g. after a date)
	 */
	public TimePeriodDTO(Date startPeriod, Date endPeriod) {
		this.startPeriod = startPeriod;
		this.endPeriod = endPeriod;
	}


	/**
	 * Verifies that time period is valid
	 */
	public void checkValidity() throws ServiceException {
		if (startPeriod != null && endPeriod != null && endPeriod.before(startPeriod))
			throw new ServiceException("endDate earlier than startDate");
	}

	/**
	 * @return Returns the endPeriod.
	 */
	public Date getEndPeriod() {
		return endPeriod;
	}

	/**
	 * @param endPeriod The endPeriod to set.
	 */
	public void setEndPeriod(Date endPeriod) {
		this.endPeriod = endPeriod;
	}

	/**
	 * @return Returns the startPeriod.
	 */
	public Date getStartPeriod() {
		return startPeriod;
	}

	/**
	 * @param startPeriod The startPeriod to set.
	 */
	public void setStartPeriod(Date startPeriod) {
		this.startPeriod = startPeriod;
	}
}