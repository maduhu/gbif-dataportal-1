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
package org.gbif.portal.web.filter;

/**
 * Wraps a key and value for displaying in front end.<br>
 * The id is the value passed in the URL.
 * key currently maps to a predicate key understood by service layer 
 * e.g. SERVICE.QUERY.PREDICATE.EQUAL <br>
 * value is what is displayed.
 */
public class PredicateDTO {
	/**The id for this predicate**/
	private String id;
	/**The key for this predicate e.g. SERVICE.QUERY.PREDICATE.EQUAL**/
	private String key;
	
	private String value;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}	
}
