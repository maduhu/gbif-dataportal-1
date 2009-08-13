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
package org.gbif.portal.model;
/**
 * BaseObject
 * 
 * Base class to be extended by all Model objects.
 * Extending classes are required to implement equals and hashCode methods.
 * 
 * @author dbarnier
 */
public abstract class BaseObject implements Comparable {
	
	/** The primary key */
	protected Long id;

	/**
	 * @return Returns the id.
	 * @hibernate.id 
	 *	generator-class="native" 
	 *	unsaved-value="null"
	 *	column="id"
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Returns true if object is an instance of BaseObject and 
	 * the identifier value is equal.
	 * Required by ORMS for caching/collections performance.
	 * @see java.lang.Object#equals(Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (object instanceof BaseObject) {
			BaseObject other = (BaseObject)object;
			if (getId() != null) {
				return getId().equals(other.getId());
			}
			else if (this == object) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns hashcode of identifier value where possible.
	 * Required by ORMS for caching/collections performance.
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (getId() != null) {
			return getId().hashCode();
		}
		return super.hashCode();
	}

	/**
	 * @see java.lang.Comparable#compareTo()
	 */
	public int compareTo(Object object) {
		BaseObject other = (BaseObject)object;
		if (getId() != null) {
			return getId().compareTo(other.getId());
		}
		return -1;
	}	
}