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


/**
 * Full Taxon concept
 * @author trobertson
 */
public class TaxonConcept extends TaxonConceptLite {
	/**
	 * Generated 
	 */
	private static final long serialVersionUID = 7863610041509642934L;
	
	protected Long kingdomConceptId;
	protected Long phylumConceptId;
	protected Long classConceptId;
	protected Long orderConceptId;
	protected Long familyConceptId;
	protected Long genusConceptId;
	protected Long speciesConceptId;
	/**
	 * @return Returns the classConceptId.
	 */
	public Long getClassConceptId() {
		return classConceptId;
	}
	/**
	 * @param classConceptId The classConceptId to set.
	 */
	public void setClassConceptId(Long classConceptId) {
		this.classConceptId = classConceptId;
	}
	/**
	 * @return Returns the familyConceptId.
	 */
	public Long getFamilyConceptId() {
		return familyConceptId;
	}
	/**
	 * @param familyConceptId The familyConceptId to set.
	 */
	public void setFamilyConceptId(Long familyConceptId) {
		this.familyConceptId = familyConceptId;
	}
	/**
	 * @return Returns the genusConceptId.
	 */
	public Long getGenusConceptId() {
		return genusConceptId;
	}
	/**
	 * @param genusConceptId The genusConceptId to set.
	 */
	public void setGenusConceptId(Long genusConceptId) {
		this.genusConceptId = genusConceptId;
	}
	/**
	 * @return Returns the kingdomConceptId.
	 */
	public Long getKingdomConceptId() {
		return kingdomConceptId;
	}
	/**
	 * @param kingdomConceptId The kingdomConceptId to set.
	 */
	public void setKingdomConceptId(Long kingdomConceptId) {
		this.kingdomConceptId = kingdomConceptId;
	}
	/**
	 * @return Returns the orderConceptId.
	 */
	public Long getOrderConceptId() {
		return orderConceptId;
	}
	/**
	 * @param orderConceptId The orderConceptId to set.
	 */
	public void setOrderConceptId(Long orderConceptId) {
		this.orderConceptId = orderConceptId;
	}
	/**
	 * @return Returns the phylumConceptId.
	 */
	public Long getPhylumConceptId() {
		return phylumConceptId;
	}
	/**
	 * @param phylumConceptId The phylumConceptId to set.
	 */
	public void setPhylumConceptId(Long phylumConceptId) {
		this.phylumConceptId = phylumConceptId;
	}
	/**
	 * @return Returns the speciesConceptId.
	 */
	public Long getSpeciesConceptId() {
		return speciesConceptId;
	}
	/**
	 * @param speciesConceptId The speciesConceptId to set.
	 */
	public void setSpeciesConceptId(Long speciesConceptId) {
		this.speciesConceptId = speciesConceptId;
	}
	
	
}
