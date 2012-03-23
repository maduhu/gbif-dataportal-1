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


/**
 * A lite taxon concept used for fast classification use
 * @author trobertson
 */
public class TaxonConceptLite implements Serializable {
	/**
	 * serialisable
	 */
	private static final long serialVersionUID = 4949183250088693568L;
	
	protected Long id;
	protected Long parentId;
	protected Integer rank;
	protected TaxonName taxonName;
	protected boolean isAccepted=true;
	protected Long partnerConceptId;
	protected boolean isNubConcept=false;
	protected Long dataProviderId;
	protected Long dataResourceId;
	protected Integer priority;
	protected boolean isSecondary=false;
	
	
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
	 * @return Returns the parentId.
	 */
	public Long getParentId() {
		return parentId;
	}
	/**
	 * @param parentId The parentId to set.
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	/**
	 * @return Returns the rank.
	 */
	public Integer getRank() {
		return rank;
	}
	/**
	 * @param rank The rank to set.
	 */
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	/**
	 * @return Returns the taxonName.
	 */
	public TaxonName getTaxonName() {
		return taxonName;
	}
	/**
	 * @param taxonName The taxonName to set.
	 */
	public void setTaxonName(TaxonName taxonName) {
		this.taxonName = taxonName;
	}
	/**
	 * @return Returns the isAccepted.
	 */
	public boolean isAccepted() {
		return isAccepted;
	}
	/**
	 * @param isAccepted The isAccepted to set.
	 */
	public void setAccepted(boolean isAccepted) {
		this.isAccepted = isAccepted;
	}
	/**
	 * @return Returns the dataProviderId.
	 */
	public Long getDataProviderId() {
		return dataProviderId;
	}
	/**
	 * @param dataProviderId The dataProviderId to set.
	 */
	public void setDataProviderId(Long dataProviderId) {
		this.dataProviderId = dataProviderId;
	}
	/**
	 * @return Returns the dataResourceId.
	 */
	public Long getDataResourceId() {
		return dataResourceId;
	}
	/**
	 * @param dataResourceId The dataResourceId to set.
	 */
	public void setDataResourceId(Long dataResourceId) {
		this.dataResourceId = dataResourceId;
	}
	/**
	 * @return Returns the isNubConcept.
	 */
	public boolean isNubConcept() {
		return isNubConcept;
	}
	/**
	 * @param isNubConcept The isNubConcept to set.
	 */
	public void setNubConcept(boolean isNubConcept) {
		this.isNubConcept = isNubConcept;
	}
	/**
	 * @return Returns the partnerConceptId.
	 */
	public Long getPartnerConceptId() {
		return partnerConceptId;
	}
	/**
	 * @param partnerConceptId The partnerConceptId to set.
	 */
	public void setPartnerConceptId(Long partnerConceptId) {
		this.partnerConceptId = partnerConceptId;
	}
	/**
	 * @return the priority
	 */
	public Integer getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	/**
	 * @return the isSecondary
	 */
	public boolean isSecondary() {
		return isSecondary;
	}
	/**
	 * @param isSecondary the isSecondary to set
	 */
	public void setSecondary(boolean isSecondary) {
		this.isSecondary = isSecondary;
	}
}