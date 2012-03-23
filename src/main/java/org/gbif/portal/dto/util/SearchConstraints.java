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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * SearchConstraints
 * 
 * Utility class to encapsulate a set search constraints for 
 * a trace service layer method.
 * 
 * @author dmartin
 */
public class SearchConstraints {
	
	/** The record to start the search from. */
	protected Integer startIndex=0;
	/** The max results to be returned**/
	protected Integer maxResults=20;

	/**
	 * Default Constructor
	 */
	public SearchConstraints() {}
	
	/**
	 * Initialises the startIndex & maxResults.
	 * @param startIndex
	 * @param maxResults
	 */
	public SearchConstraints(Integer startIndex, Integer maxResults) {
		this.startIndex = startIndex;
		this.maxResults = maxResults;
	}	
	
	/**
	 * @return the maxResults
	 */
	public Integer getMaxResults() {
		return maxResults;
	}

	/**
	 * @param maxResults the maxResults to set
	 */
	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}

	/**
	 * @return the startIndex
	 */
	public Integer getStartIndex() {
		return startIndex;
	}

	/**
	 * @param startIndex the startIndex to set
	 */
	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}