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
package org.gbif.portal.model.taxonomy;

import java.util.Set;

import org.gbif.portal.model.BaseObject;

/**
 * A light version of TaxonConcept. Useful for large resultsets and joins.
 *
 * @author dmartin
 * @hibernate.class
 * 	table="taxon_concept"
 */
public class TaxonConceptLite extends BaseObject {
	
	/** The taxonomic rank for this concept */
	protected TaxonRank taxonRank = TaxonRank.UNKNOWN;	
	/** The taxonomic name for this concept */	
	protected TaxonNameLite taxonNameLite;
	/** The parent concept of this concept, null for the highest ranks */	
	protected Long parentConceptId;
	/** Whether this concept is an accepted concept or a synonym for another accepted concept */
	protected boolean isAccepted;
	/** The partner concept for this concept */		
	protected Long partnerConceptId;
	/** The taxonomic priority for this concept */		
	protected Integer taxonomicPriority;
	/** Whether this concept is a nub concept */
	protected Boolean isNubConcept = Boolean.FALSE;
	/** The Child Concepts - direct descendents only  */
	protected Set<TaxonConceptLite> childConcepts;	
	/**
	 * @return the childConcepts
	 */
	public Set<TaxonConceptLite> getChildConcepts() {
		return childConcepts;
	}
	/**
	 * @param childConcepts the childConcepts to set
	 */
	public void setChildConcepts(Set<TaxonConceptLite> childConcepts) {
		this.childConcepts = childConcepts;
	}
	/**
	 * @return the parentConceptId
	 */
	public Long getParentConceptId() {
		return parentConceptId;
	}
	/**
	 * @param parentConceptId the parentConceptId to set
	 */
	public void setParentConceptId(Long parentConceptId) {
		this.parentConceptId = parentConceptId;
	}
	/**
	 * @return the taxonNameLite
	 */
	public TaxonNameLite getTaxonNameLite() {
		return taxonNameLite;
	}
	/**
	 * @param taxonNameLite the taxonNameLite to set
	 */
	public void setTaxonNameLite(TaxonNameLite taxonNameLite) {
		this.taxonNameLite = taxonNameLite;
	}
	/**
	 * @return the taxonRank
	 */
	public TaxonRank getTaxonRank() {
		return taxonRank;
	}
	/**
	 * @param taxonRank the taxonRank to set
	 */
	public void setTaxonRank(TaxonRank taxonRank) {
		this.taxonRank = taxonRank;
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
	 * @return the isNubConcept
	 */
	public Boolean getIsNubConcept() {
		return isNubConcept;
	}
	/**
	 * @param isNubConcept the isNubConcept to set
	 */
	public void setIsNubConcept(Boolean isNubConcept) {
		this.isNubConcept = isNubConcept;
	}
	/**
	 * @return the partnerConceptId
	 */
	public Long getPartnerConceptId() {
		return partnerConceptId;
	}
	/**
	 * @param partnerConceptId the partnerConceptId to set
	 */
	public void setPartnerConceptId(Long partnerConceptId) {
		this.partnerConceptId = partnerConceptId;
	}
	/**
	 * @param isAccepted the isAccepted to set
	 */
	public void setAccepted(boolean isAccepted) {
		this.isAccepted = isAccepted;
	}
	/**
	 * @return the taxonomicPriority
	 */
	public Integer getTaxonomicPriority() {
		return taxonomicPriority;
	}
	/**
	 * @param taxonomicPriority the taxonomicPriority to set
	 */
	public void setTaxonomicPriority(Integer taxonomicPriority) {
		this.taxonomicPriority = taxonomicPriority;
	}
}