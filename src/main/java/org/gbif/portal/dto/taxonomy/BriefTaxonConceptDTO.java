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
package org.gbif.portal.dto.taxonomy;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Defines a TaxonConcept in its most brief form. This object
 * will be returned by Service Layer methods returning lists of concepts
 * 
 * @author dmartin
 */
public class BriefTaxonConceptDTO implements Serializable{
	
	/** serial version id */
	private static final long serialVersionUID = 6268011656521981931L;
	/** The key for this taxon concept */
	protected String key;
	/** The taxonomic rank for this taxon concept as a simple string. e.g. kingdom */	
	protected String rank;
	/** The value of this rank - e.g. Kingdom=1000, Species=7000 */	
	protected int rankValue;
	/** The rank code for this taxon concept **/
	protected String rankCode;	
	/** The scientific name excluding the author */
	protected String taxonName;
	/** The parent concept key for this taxon concept */
	protected String parentConceptKey;
	/** The partner concept key for this taxon concept */
	protected String partnerConceptKey;
	/** Whether this concept is a nub concept or not */
	protected boolean isNubConcept;
	/** Whether this concept is an accepted concept or not */
	protected boolean isAccepted;
	/** The taxononmic priority for this concept */
	protected Integer taxonomicPriority;
	/** The kingdom concept key */
	protected String kingdomConceptKey;
	/** The phylum concept key */
	protected String phylumConceptKey;
	/** The class concept key */
	protected String classConceptKey;
	/** The order concept key */
	protected String orderConceptKey;
	/** The family concept key */
	protected String familyConceptKey;
	/** The genus concept key */
	protected String genusConceptKey;
	/** The species concept key */
	protected String speciesConceptKey;
	/** The child count - may or may not be initialized for performance reasons */
	protected Integer childCount;
	
	/**
	 * @return the childCount
	 */
	public Integer getChildCount() {
		return childCount;
	}
	/**
	 * @param childCount the childCount to set
	 */
	public void setChildCount(Integer childCount) {
		this.childCount = childCount;
	}
	/**
	 * @return the scientificName
	 */
	public String getTaxonName() {
		return taxonName;
	}
	/**
	 * @param scientificName the scientificName to set
	 */
	public void setTaxonName(String scientificName) {
		this.taxonName = scientificName;
	}
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * @return the rank
	 */
	public String getRank() {
		return rank;
	}
	/**
	 * @param rank the rank to set
	 */
	public void setRank(String rank) {
		this.rank = rank;
	}
	
	/**
	 * @return the parentConceptKey
	 */
	public String getParentConceptKey() {
		return parentConceptKey;
	}
	/**
	 * @param parentConceptKey the parentConceptKey to set
	 */
	public void setParentConceptKey(String parentConceptKey) {
		this.parentConceptKey = parentConceptKey;
	}
	/**
	 * @return the isNubConcept
	 */
	public boolean getIsNubConcept() {
		return isNubConcept;
	}
	/**
	 * @param isNubConcept the isNubConcept to set
	 */
	public void setIsNubConcept(boolean isNubConcept) {
		this.isNubConcept = isNubConcept;
	}
	/**
	 * @return the partnerConceptKey
	 */
	public String getPartnerConceptKey() {
		return partnerConceptKey;
	}
	/**
	 * @param partnerConceptKey the partnerConceptKey to set
	 */
	public void setPartnerConceptKey(String partnerConceptKey) {
		this.partnerConceptKey = partnerConceptKey;
	}	
	/**
	 * @return the isAccepted
	 */
	public boolean isAccepted() {
		return isAccepted;
	}
	/**
	 * @return the isAccepted
	 */
	public boolean getIsAccepted() {
		return isAccepted;
	}
	/**
	 * @param isAccepted the isAccepted to set
	 */
	public void setIsAccepted(boolean isAccepted) {
		this.isAccepted = isAccepted;
	}
	/**
	 * @param isAccepted the isAccepted to set
	 */
	public void setAccepted(boolean isAccepted) {
		this.isAccepted = isAccepted;
	}
	/**
	 * @param isNubConcept the isNubConcept to set
	 */
	public void setNubConcept(boolean isNubConcept) {
		this.isNubConcept = isNubConcept;
	}	
	/**
	 * @return the taxonomicPriority
	 */
	public int getTaxonomicPriority() {
		return taxonomicPriority;
	}
	/**
	 * @param taxonomicPriority the taxonomicPriority to set
	 */
	public void setTaxonomicPriority(Integer taxonomicPriority) {
		this.taxonomicPriority = taxonomicPriority;
	}	
	/**
	 * @return the rankValue
	 */
	public int getRankValue() {
		return rankValue;
	}
	/**
	 * @param rankValue the rankValue to set
	 */
	public void setRankValue(int rankValue) {
		this.rankValue = rankValue;
	}
	/**
	 * @return the kingdomConceptKey
	 */
	public String getKingdomConceptKey() {
		return kingdomConceptKey;
	}
	/**
	 * @param kingdomConceptKey the kingdomConceptKey to set
	 */
	public void setKingdomConceptKey(String kingdomConceptKey) {
		this.kingdomConceptKey = kingdomConceptKey;
	}
	/**
	 * @return the classConceptKey
	 */
	public String getClassConceptKey() {
		return classConceptKey;
	}
	/**
	 * @param classConceptKey the classConceptKey to set
	 */
	public void setClassConceptKey(String classConceptKey) {
		this.classConceptKey = classConceptKey;
	}
	/**
	 * @return the familyConceptKey
	 */
	public String getFamilyConceptKey() {
		return familyConceptKey;
	}
	/**
	 * @param familyConceptKey the familyConceptKey to set
	 */
	public void setFamilyConceptKey(String familyConceptKey) {
		this.familyConceptKey = familyConceptKey;
	}
	/**
	 * @return the genusConceptKey
	 */
	public String getGenusConceptKey() {
		return genusConceptKey;
	}
	/**
	 * @param genusConceptKey the genusConceptKey to set
	 */
	public void setGenusConceptKey(String genusConceptKey) {
		this.genusConceptKey = genusConceptKey;
	}
	/**
	 * @return the orderConceptKey
	 */
	public String getOrderConceptKey() {
		return orderConceptKey;
	}
	/**
	 * @param orderConceptKey the orderConceptKey to set
	 */
	public void setOrderConceptKey(String orderConceptKey) {
		this.orderConceptKey = orderConceptKey;
	}
	/**
	 * @return the phylumConceptKey
	 */
	public String getPhylumConceptKey() {
		return phylumConceptKey;
	}
	/**
	 * @param phylumConceptKey the phylumConceptKey to set
	 */
	public void setPhylumConceptKey(String phylumConceptKey) {
		this.phylumConceptKey = phylumConceptKey;
	}
	/**
	 * @return the speciesConceptKey
	 */
	public String getSpeciesConceptKey() {
		return speciesConceptKey;
	}
	/**
	 * @param speciesConceptKey the speciesConceptKey to set
	 */
	public void setSpeciesConceptKey(String speciesConceptKey) {
		this.speciesConceptKey = speciesConceptKey;
	}
	/**
	 * @return the rankCode
	 */
	public String getRankCode() {
		return rankCode;
	}
	/**
	 * @param rankCode the rankCode to set
	 */
	public void setRankCode(String rankCode) {
		this.rankCode = rankCode;
	}	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj==null)
			return false;
		if(obj instanceof BriefTaxonConceptDTO){
			BriefTaxonConceptDTO briefTaxonConceptDTO = (BriefTaxonConceptDTO) obj;
			return briefTaxonConceptDTO.getKey().equals(this.getKey());
		}
		return false;
	}	
}