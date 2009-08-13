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
package org.gbif.portal.web.controller.search.bean;

import org.gbif.portal.dto.util.SearchConstraints;

/**
 * @author dmartin
 */
public class NameSearch {

	protected String query;
	protected String returnType;
	protected String callback;
	protected boolean exactMatchOnly;
	protected boolean soundex;
	protected SearchConstraints searchConstraints;
	
	/**
	 * @return the callback
	 */
	public String getCallback() {
		return callback;
	}
	/**
	 * @param callback the callback to set
	 */
	public void setCallback(String callback) {
		this.callback = callback;
	}
	/**
	 * @return the exactMatchOnly
	 */
	public boolean isExactMatchOnly() {
		return exactMatchOnly;
	}
	/**
	 * @param exactMatchOnly the exactMatchOnly to set
	 */
	public void setExactMatchOnly(boolean exactMatchOnly) {
		this.exactMatchOnly = exactMatchOnly;
	}
	/**
	 * @return the returnType
	 */
	public String getReturnType() {
		return returnType;
	}
	/**
	 * @param returnType the returnType to set
	 */
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	/**
	 * @return the soundex
	 */
	public boolean isSoundex() {
		return soundex;
	}
	/**
	 * @param soundex the soundex to set
	 */
	public void setSoundex(boolean soundex) {
		this.soundex = soundex;
	}
	/**
	 * @return the searchConstraints
	 */
	public SearchConstraints getSearchConstraints() {
		return searchConstraints;
	}
	/**
	 * @param searchConstraints the searchConstraints to set
	 */
	public void setSearchConstraints(SearchConstraints searchConstraints) {
		this.searchConstraints = searchConstraints;
	}
	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}
	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}
}