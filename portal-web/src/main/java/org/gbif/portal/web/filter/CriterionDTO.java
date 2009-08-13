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
 * A Criterion for a search. This criterion has a triplet form with a subject, predicate and value.
 * This DTO contains display parameters to allow an application
 * to order/group/display a set of criterion.
 *
 * @author Dave Martin
 */
public class CriterionDTO {

	/** The Subject value for this criterion */
	private String subject;
	/** The Predicate value */
	private String predicate;
	/** The Value for this criterion */
	private String value;
	/** The Display Value for this criterion */
	private String displayValue;	
	/** Group will denote where in the page it is rendered */
	private String group;
	
	public CriterionDTO(){}
	
	/**
	 * Initialises the type predicate and value for this Criterion.
	 * @param subject
	 * @param predicate
	 * @param value
	 */
	public CriterionDTO(String subject, String predicate, String value){
		this.subject = subject;
		this.predicate = predicate;
		this.value = value;
	}
	
	/**
	 * Initialises the type predicate and value and group for this Criterion.
	 * @param subject
	 * @param predicate
	 * @param value
	 * @param group
	 */
	public CriterionDTO(String subject, String predicate, String value, String group){
		this (subject, predicate, value);
		this.group = group;
	}	

	/**
	 * Initialises the type predicate and value and group for this Criterion.
	 * @param subject
	 * @param predicate
	 * @param value
	 * @param group
	 * @param displayValue
	 */
	public CriterionDTO(String subject, String predicate, String value, String group, String displayValue){
		this (subject, predicate, value, group);
		this.displayValue = displayValue;
	}		
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if(object == null)
			return false;
		if(object instanceof CriterionDTO){		
			CriterionDTO criterionDTO = (CriterionDTO) object;
			if (!nullSafeEquals(criterionDTO.getSubject(), this.subject))
				return false;
			if (!nullSafeEquals(criterionDTO.getPredicate(), this.predicate))
				return false;	
			if (!nullSafeEquals(criterionDTO.getValue(), this.value))
				return false;
			if (!nullSafeEquals(criterionDTO.getGroup(), this.group))
				return false;			
			return true;
		}
		return false;
	}
	
	public boolean nullSafeEquals(String original, String comparedTo){
		if (original==null && comparedTo==null)
			return true;
		if (original!=null && comparedTo==null)
			return false;		
		if (original==null && comparedTo!=null)
			return false;			
		return original.equals(comparedTo);
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return subject.hashCode()+predicate.hashCode()+value.hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("subject=");
		sb.append(subject);
		sb.append(", predicate=");
		sb.append(predicate);
		sb.append(", value=");
		sb.append(value);
		sb.append(", group=");
		sb.append(group);
		return sb.toString();
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
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}	
	
	/**
	 * @return the displayValue
	 */
	public String getDisplayValue() {
		return displayValue;
	}

	/**
	 * @param displayValue the displayValue to set
	 */
	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}
}