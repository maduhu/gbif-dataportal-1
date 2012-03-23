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
package org.gbif.portal.model.geospatial;

import org.gbif.portal.model.BaseObject;

/**
 * Model object that represents an ip range and the country associated with this ip range.
 * 
 * @author dmartin
 */
public class IPCountry extends BaseObject {

	protected String start;
	protected String end;
	protected Country country;
	protected String isoCountryCode;
	protected String startLong;
	protected String endLong;
	
	/**
	 * @return the country
	 */
	public Country getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(Country country) {
		this.country = country;
	}
	/**
	 * @return the end
	 */
	public String getEnd() {
		return end;
	}
	/**
	 * @param end the end to set
	 */
	public void setEnd(String end) {
		this.end = end;
	}
	/**
	 * @return the endLong
	 */
	public String getEndLong() {
		return endLong;
	}
	/**
	 * @param endLong the endLong to set
	 */
	public void setEndLong(String endLong) {
		this.endLong = endLong;
	}
	/**
	 * @return the isoCountryCode
	 */
	public String getIsoCountryCode() {
		return isoCountryCode;
	}
	/**
	 * @param isoCountryCode the isoCountryCode to set
	 */
	public void setIsoCountryCode(String isoCountryCode) {
		this.isoCountryCode = isoCountryCode;
	}
	/**
	 * @return the start
	 */
	public String getStart() {
		return start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(String start) {
		this.start = start;
	}
	/**
	 * @return the startLong
	 */
	public String getStartLong() {
		return startLong;
	}
	/**
	 * @param startLong the startLong to set
	 */
	public void setStartLong(String startLong) {
		this.startLong = startLong;
	}
}