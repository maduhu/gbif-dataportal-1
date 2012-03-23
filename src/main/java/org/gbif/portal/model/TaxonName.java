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
package org.gbif.portal.model;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * A bean to represent a taxon name in whole or part
 * @author trobertson
 */
public class TaxonName implements Serializable{
	/**
	 * Generated 
	 */
	private static final long serialVersionUID = -3005075738383480628L;
	
	protected Long id;
	protected String canonical;
	protected String supraGeneric;
	protected String generic;
	protected String infraGeneric;
	protected String specific;
	protected String infraSpecific;
	protected String infraSpecificMarker;
	protected String author;
	protected int rank=-1;
	protected Integer type=0; // inicator for hybrid, cultivar etc
	protected String searchableCanonical;	//a canonical stripped of vowels and repeated consonants
	
	public TaxonName() {		
	}
	
	public TaxonName(String canonical, String author, int rank) {
		this.canonical = canonical;
		this.author = author;
		this.rank = rank;
	}
	
	/**
	 * @return A full version of the taxon name for logging
	 */
	public String toFullString() {
		 return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
	       append("canonical", canonical).
	       append("author", author).
	       append("rank", rank).
	       append("supraGeneric", supraGeneric).
	       append("generic", generic).
	       append("infraGeneric", infraGeneric).
	       append("specific", specific).
	       append("infraSpecific", infraSpecific).
	       append("infraSpecificMarker", infraSpecificMarker).
	       append("type", type).
	       append("searchableCanonical", searchableCanonical).	       
	       toString();	
	}
	
	/**
	 * @return A short version of the taxon name (Canonical, Author, Rank)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		 return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
	       append("canonical", canonical).
	       append("author", author).
	       append("rank", rank).
	       toString();	
	}
	
	/**
	 * TaxonName is considered equal if the canonical, author and rank are the same
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object target) {
		if (target instanceof TaxonName) {
			TaxonName targetName = (TaxonName) target;
			if (StringUtils.equals(getCanonical(), targetName.getCanonical())
				&& StringUtils.equals(getAuthor(), targetName.getAuthor())
				&& getRank() == targetName.getRank()) {
				return true;
			}
		}
		return false;
		
	}


	/**
	 * @return Returns the author.
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @param author The author to set.
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	/**
	 * @return Returns the canonical.
	 */
	public String getCanonical() {
		return canonical;
	}
	/**
	 * @param canonical The canonical to set.
	 */
	public void setCanonical(String canonical) {
		this.canonical = canonical;
	}
	/**
	 * @return Returns the generic.
	 */
	public String getGeneric() {
		return generic;
	}
	/**
	 * @param generic The generic to set.
	 */
	public void setGeneric(String generic) {
		this.generic = generic;
	}
	/**
	 * @return Returns the infraGeneric.
	 */
	public String getInfraGeneric() {
		return infraGeneric;
	}
	/**
	 * @param infraGeneric The infraGeneric to set.
	 */
	public void setInfraGeneric(String infraGeneric) {
		this.infraGeneric = infraGeneric;
	}
	/**
	 * @return Returns the infraSpecific.
	 */
	public String getInfraSpecific() {
		return infraSpecific;
	}
	/**
	 * @param infraSpecific The infraSpecific to set.
	 */
	public void setInfraSpecific(String infraSpecific) {
		this.infraSpecific = infraSpecific;
	}
	/**
	 * @return Returns the infraSpecificMarker.
	 */
	public String getInfraSpecificMarker() {
		return infraSpecificMarker;
	}
	/**
	 * @param infraSpecificMarker The infraSpecificMarker to set.
	 */
	public void setInfraSpecificMarker(String infraSpecificMarker) {
		this.infraSpecificMarker = infraSpecificMarker;
	}
	/**
	 * @return Returns the specific.
	 */
	public String getSpecific() {
		return specific;
	}
	/**
	 * @param specific The specific to set.
	 */
	public void setSpecific(String specific) {
		this.specific = specific;
	}
	/**
	 * @return Returns the supraGeneric.
	 */
	public String getSupraGeneric() {
		return supraGeneric;
	}
	/**
	 * @param supraGeneric The supraGeneric to set.
	 */
	public void setSupraGeneric(String supraGeneric) {
		this.supraGeneric = supraGeneric;
	}
	/**
	 * @return Returns the rank.
	 */
	public int getRank() {
		return rank;
	}
	/**
	 * @param rank The rank to set.
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * @return Returns the id.
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
	 * @return Returns the type.
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * @return the searchableCanonical
	 */
	public String getSearchableCanonical() {
		return searchableCanonical;
	}

	/**
	 * @param searchableCanonical the searchableCanonical to set
	 */
	public void setSearchableCanonical(String searchableCanonical) {
		this.searchableCanonical = searchableCanonical;
	}
}