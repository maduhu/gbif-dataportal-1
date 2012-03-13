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
package org.gbif.portal.util.mhf.criteria;

/**
 * A simple triplet criteria 
 * 	e.g.
 * 		ScientificName equals Puma*
 * @author trobertson
 */
public class TripletCriteria extends Criteria {
	protected String type = "TRIPLET";
	protected String subject;
	protected String predicate;
	protected String object;
	
	public TripletCriteria(Object subject, Object predicate, Object object) {
		this.subject = subject.toString();
		this.predicate = predicate.toString();
		this.object = object.toString();
		
	}
	
	/**
	 * @return Returns the object.
	 */
	public String getObject() {
		return object;
	}
	/**
	 * @param object The object to set.
	 */
	public void setObject(String object) {
		this.object = object;
	}
	/**
	 * @return Returns the predicate.
	 */
	public String getPredicate() {
		return predicate;
	}
	/**
	 * @param predicate The predicate to set.
	 */
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	/**
	 * @return Returns the subject.
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject The subject to set.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
}
