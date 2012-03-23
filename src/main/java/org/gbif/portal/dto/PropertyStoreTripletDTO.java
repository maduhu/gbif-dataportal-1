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
package org.gbif.portal.dto;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Indicates a 
 *   Subject (PropertyStore key), 
 *   Predicate (PropertyStore key), 
 *   Object (The value to use)
 *   Namespace (To locate the Subject and Predicate in the PropertyStore) 
 *   
 * @author trobertson
 */
public class PropertyStoreTripletDTO  implements Serializable {

	private static final long serialVersionUID = -6719094243348491208L;
	/** The property store namespace for this triplet */
	protected String namespace;
	/** The subject for this triplet */
	protected String subject;
	/** The predicate for this triplet e.g. equals */
	protected String predicate;
	/** the object for this triplet */
	protected Object object;
	/** The grouping for this triplet. Optional field used to group triplets of different subjects */
	protected String grouping;

	public PropertyStoreTripletDTO() {}

	/**
	 * Initialise this triplet.
	 * 
	 * @param namespace
	 * @param subject
	 * @param predicate
	 * @param object
	 */
	public PropertyStoreTripletDTO(String namespace, String subject, String predicate, Object object) {
		this.namespace = namespace;
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
	}

	/**
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * @param namespace the namespace to set
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(Object object) {
		this.object = object;
	}

	/**
	 * @return the predicate
	 */
	public String getPredicate() {
		return predicate;
	}

	/**
	 * @param predicate the predicate to set
	 */
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the grouping
	 */
	public String getGrouping() {
		return grouping;
	}

	/**
	 * @param grouping the grouping to set
	 */
	public void setGrouping(String grouping) {
		this.grouping = grouping;
	}	
	
	/**
	 * Comparator for sorting triplets by subject.
	 *
	 * @author dmartin
	 */
	public class SubjectComparator implements Comparator<PropertyStoreTripletDTO> {
		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(PropertyStoreTripletDTO firstTriplet, PropertyStoreTripletDTO secondTriplet) {
			if (firstTriplet==null||secondTriplet==null || firstTriplet.getSubject()==null || secondTriplet.getSubject()==null)
				return -1;
			return firstTriplet.getSubject().compareTo(secondTriplet.getSubject());
		}		
	}
	
	/**
	 * Comparator for sorting triplets by grouping and subject.
	 *
	 * @author dmartin
	 */
	public class GroupingSubjectComparator implements Comparator<PropertyStoreTripletDTO> {
		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(PropertyStoreTripletDTO firstTriplet, PropertyStoreTripletDTO secondTriplet) {
			if (firstTriplet==null||secondTriplet==null || firstTriplet.getSubject()==null || secondTriplet.getSubject()==null)
				return -1;
			
			if(firstTriplet.getGrouping()!=null && secondTriplet.getGrouping()!=null){
				if(firstTriplet.getGrouping().equals(secondTriplet.getGrouping())){
					return firstTriplet.getSubject().compareTo(secondTriplet.getSubject());
				} else {
					return firstTriplet.getGrouping().compareTo(secondTriplet.getGrouping());
				}
			}
			return firstTriplet.getSubject().compareTo(secondTriplet.getSubject());
		}		
	}	
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if(object==null)
			return false;
		if(object instanceof PropertyStoreTripletDTO){
			PropertyStoreTripletDTO triplet = (PropertyStoreTripletDTO) object;
			if(! nullSafeEquals(this.namespace, triplet.getNamespace()))
				return false;
			if(! nullSafeEquals(this.subject, triplet.getSubject()))
				return false;
			if(! nullSafeEquals(this.predicate, triplet.getPredicate()))
				return false;
			if(! nullSafeEquals(this.object, triplet.getObject()))
				return false;
			if(! nullSafeEquals(this.grouping, triplet.getGrouping()))
				return false;			
			return true;
		}
		return false;
	}
	
	public boolean nullSafeEquals(Object original, Object comparedTo){
		if (original==null && comparedTo==null)
			return true;
		if (original!=null && comparedTo==null)
			return false;		
		if (original==null && comparedTo!=null)
			return false;			
		return original.equals(comparedTo);
	}	

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}