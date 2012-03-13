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

import java.util.LinkedList;
import java.util.List;

/**
 * A collection criteria 
 * 	e.g.
 * 		and (.,.,.,.,.)
 * @author trobertson
 */
public class CollectionCriteria extends Criteria {
	protected String type = "COLLECTION";
	protected String predicate;
	protected List<Criteria> criteria = new LinkedList<Criteria>();

	
	public CollectionCriteria(Object predicate) {
		this.predicate = predicate.toString();
	}

	/**
	 * Appends the criterion to the list of criteria
	 * @param criterion To append
	 * @return Itself
	 */
	public CollectionCriteria add(Criteria criterion) {
		this.getCriteria().add(criterion);
		return this;
	}

	/**
	 * @return Returns the criteria.
	 */
	public List<Criteria> getCriteria() {
		return criteria;
	}


	/**
	 * @param criteria The criteria to set.
	 */
	public void setCriteria(List<Criteria> criteria) {
		this.criteria = criteria;
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
