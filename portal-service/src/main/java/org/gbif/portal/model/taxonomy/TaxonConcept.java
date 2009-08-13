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
import org.gbif.portal.model.geospatial.Country;
import org.gbif.portal.model.resources.DataProvider;
import org.gbif.portal.model.resources.DataResource;

/**
 * TaxonConcept Model Object represents a Taxonomic Concept described
 * by a particular Publication. 
 *
 * Object representation of the TaxonConcept data model concept.
 * http://wiki.gbif.org/dadiwiki/wikka.php?wakka=TaxonConcept
 *
 * @author dbarnier
 * @author dmartin
 *
 * @hibernate.class
 * 	table="taxon_concept"
 */
public class TaxonConcept extends BaseObject {

	/** The taxonomic rank for this concept */
	protected TaxonRank taxonRank = TaxonRank.UNKNOWN;	
	/** The parent concept of this concept, null for the highest ranks */	
	protected Long parentConceptId;
	/** The nub concept for this concept */		
	protected Long nubConceptId;
	/** Whether this concept is an accepted concept or a synonym for another accepted concept */
	protected boolean isAccepted;
	/** The partner concept for this concept */		
	protected Long partnerConceptId;
	/** Whether this concept is a nub concept */
	protected Boolean isNubConcept = Boolean.FALSE;	
	/** The taxonomic name for this concept */	
	protected Integer rank;	
	/** The taxonomic name for this concept */	
	protected TaxonName taxonName;
	/** The data provider of this concept */		
	protected DataProvider dataProvider;
	/** The data resource of this concept */		
	protected DataResource dataResource;
	/** The data provider of this concept */		
	protected Long dataProviderId;
	/** The data resource of this concept */		
	protected Long dataResourceId;
	/** The parent concept of this concept, null for the highest ranks */	
	protected TaxonConcept parentConcept;
	/** The kingdom concept for this concept */		
	protected TaxonConceptLite kingdomConcept;
	/** The kingdom concept for this concept */		
	protected Long kingdomConceptId;
	/** The phylum concept for this concept */		
	protected TaxonConceptLite phylumConcept;
	/** The phylum concept for this concept */		
	protected Long phylumConceptId;
	/** The class concept for this concept */		
	protected TaxonConceptLite classConcept;
	/** The class concept for this concept */		
	protected Long classConceptId;
	/** The order concept for this concept */		
	protected TaxonConceptLite orderConcept;
	/** The order concept for this concept */		
	protected Long orderConceptId;
	/** The family concept for this concept */		
	protected TaxonConceptLite familyConcept;
	/** The family concept for this concept */		
	protected Long familyConceptId;
	/** The genus concept for this concept */		
	protected TaxonConceptLite genusConcept;
	/** The genus concept for this concept */		
	protected Long genusConceptId;
	/** The species concept for this concept */		
	protected TaxonConceptLite speciesConcept;	
	/** The species concept for this concept */		
	protected Long speciesConceptId;	
	/** The partner concept for this concept */		
	protected TaxonConcept partnerConcept;	
	/** The Child Concepts - direct descendents only  */
	protected Set<TaxonConceptLite> childConcepts;
	/** The Remote Concepts for this concept  */
	protected Set<RemoteConcept> remoteConcepts;
	/** The common names for this concept  */
	protected Set<CommonName> commonNames;
	/** list of assertions for this concept **/	
	protected Set<RelationshipAssertion> relationshipAssertions;
	/** The taxonomic priority for this concept */		
	protected Integer taxonomicPriority;
	/** The common names for this concept  */
	protected Set<Country> countries;
	
	/**
	 * @return Returns the taxonName.
	 */
	public TaxonName getTaxonName() {
		return this.taxonName;
	}
	public void setTaxonName(TaxonName taxonName) {
		this.taxonName = taxonName;
	}

	/**
	 * @return the dataProvider
	 */
	public DataProvider getDataProvider() {
		return dataProvider;
	}
	/**
	 * @param dataProvider the dataProvider to set
	 */
	public void setDataProvider(DataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}
	
	/**
	 * @return the dataResource
	 */
	public DataResource getDataResource() {
		return dataResource;
	}
	/**
	 * @param dataResource the dataResource to set
	 */
	public void setDataResource(DataResource dataResource) {
		this.dataResource = dataResource;
	}

	/**
	 * @return Returns the parentConcept.
	 */
	public TaxonConcept getParentConcept() {
		return parentConcept;
	}
	public void setParentConcept(TaxonConcept parentConcept) {
		this.parentConcept = parentConcept;
	}

	/**
	 * @return Returns the kingdomConcept.
	 */
	public TaxonConceptLite getKingdomConcept() {
		return kingdomConcept;
	}
	public void setKingdomConcept(TaxonConceptLite kingdomConcept) {
		this.kingdomConcept = kingdomConcept;
	}	

	/**
	 * @return Returns the phylumConcept.
	 */
	public TaxonConceptLite getPhylumConcept() {
		return phylumConcept;
	}
	public void setPhylumConcept(TaxonConceptLite phylumConcept) {
		this.phylumConcept = phylumConcept;
	}
	
	/**
	 * @return Returns the classConcept.
	 */
	public TaxonConceptLite getClassConcept() {
		return classConcept;
	}
	public void setClassConcept(TaxonConceptLite classConcept) {
		this.classConcept = classConcept;
	}
	
	/**
	 * @return Returns the orderConcept.
	 */
	public TaxonConceptLite getOrderConcept() {
		return orderConcept;
	}
	public void setOrderConcept(TaxonConceptLite orderConcept) {
		this.orderConcept = orderConcept;
	}
	
	/**
	 * @return Returns the familyConcept.
	 */
	public TaxonConceptLite getFamilyConcept() {
		return familyConcept;
	}
	public void setFamilyConcept(TaxonConceptLite familyConcept) {
		this.familyConcept = familyConcept;
	}
	
	/**
	 * @return Returns the genusConcept.
	 */
	public TaxonConceptLite getGenusConcept() {
		return genusConcept;
	}
	public void setGenusConcept(TaxonConceptLite genusConcept) {
		this.genusConcept = genusConcept;
	}

	/**
	 * @return Returns the speciesConcept.
	 */
	public TaxonConceptLite getSpeciesConcept() {
		return speciesConcept;
	}
	public void setSpeciesConcept(TaxonConceptLite speciesConcept) {
		this.speciesConcept = speciesConcept;
	}

	/**
	 * @hibernate.set
	 * 	lazy="true"
	 * @hibernate.key
	 * 	column="parent_concept_id"
	 * @hibernate.one-to-many
	 * 	class="org.gbif.portal.model.taxonomy.TaxonConcept"
	 */
	public Set<TaxonConceptLite> getChildConcepts() {
		return childConcepts;
	}
	public void setChildConcepts(Set<TaxonConceptLite> childConcepts) {
		this.childConcepts = childConcepts;
	}	
	
	/**
	 * @hibernate.set
	 * 	lazy="true"
	 * @hibernate.key
	 * 	column="taxon_concept_id"
	 * @hibernate.one-to-many
	 * 	class="org.gbif.portal.model.taxonomy.RemoteConcept"
	 * 
	 * @return the remoteConcepts
	 */
	public Set<RemoteConcept> getRemoteConcepts() {
		return remoteConcepts;
	}
	/**
	 * @param remoteConcepts the remoteConcepts to set
	 */
	public void setRemoteConcepts(Set<RemoteConcept> remoteConcepts) {
		this.remoteConcepts = remoteConcepts;
	}
	
	/**
	 * @return the commonNames
	 */
	public Set<CommonName> getCommonNames() {
		return commonNames;
	}
	
	/**
	 * @param commonNames the commonNames to set
	 */
	public void setCommonNames(Set<CommonName> commonNames) {
		this.commonNames = commonNames;
	}

	/**
	 * @see org.gbif.model.BaseObject#equals()
	 */
	public boolean equals(Object object) {
		if (object instanceof TaxonConcept) {
			return super.equals(object);
		}
		return false;
	}
	/**
	 * @hibernate.property
	 * 	column="class_concept_id"
	 * 	not-null="false"
	 * insert="false"
	 * update="false"
	 * 
	 * @return the classConceptId
	 */
	public Long getClassConceptId() {
		return classConceptId;
	}
	/**
	 * @param classConceptId the classConceptId to set
	 */
	public void setClassConceptId(Long classConceptId) {
		this.classConceptId = classConceptId;
	}
	/**
	 * @hibernate.property
	 * 	column="family_concept_id"
	 * 	not-null="false"
	 * insert="false"
	 * update="false"
	 * 
	 * @return the familyConceptId
	 */
	public Long getFamilyConceptId() {
		return familyConceptId;
	}
	/**
	 * @param familyConceptId the familyConceptId to set
	 */
	public void setFamilyConceptId(Long familyConceptId) {
		this.familyConceptId = familyConceptId;
	}
	/**
	 * @hibernate.property
	 * 	column="genus_concept_id"
	 * 	not-null="false"
	 * insert="false"
	 * update="false"
	 * 
	 * @return the genusConceptId
	 */
	public Long getGenusConceptId() {
		return genusConceptId;
	}
	/**
	 * @param genusConceptId the genusConceptId to set
	 */
	public void setGenusConceptId(Long genusConceptId) {
		this.genusConceptId = genusConceptId;
	}
	/**
	 * @hibernate.property
	 * 	column="kingdom_concept_id"
	 * 	not-null="false"
	 * insert="false"
	 * update="false"
	 * 
	 * @return the kingdomConceptId
	 */
	public Long getKingdomConceptId() {
		return kingdomConceptId;
	}
	/**
	 * @param kingdomConceptId the kingdomConceptId to set
	 */
	public void setKingdomConceptId(Long kingdomConceptId) {
		this.kingdomConceptId = kingdomConceptId;
	}
	/**
	 * @hibernate.property
	 * 	column="order_concept_id"
	 * 	not-null="false"
	 * insert="false"
	 * update="false"
	 * 
	 * @return the orderConceptId
	 */
	public Long getOrderConceptId() {
		return orderConceptId;
	}
	/**
	 * @param orderConceptId the orderConceptId to set
	 */
	public void setOrderConceptId(Long orderConceptId) {
		this.orderConceptId = orderConceptId;
	}

	/**
	 * @hibernate.property
	 * 	column="phylum_concept_id"
	 * 	not-null="false"
	 * insert="false"
	 * update="false"
	 * 
	 * @return the phylumConceptId
	 */
	public Long getPhylumConceptId() {
		return phylumConceptId;
	}
	/**
	 * @param phylumConceptId the phylumConceptId to set
	 */
	public void setPhylumConceptId(Long phylumConceptId) {
		this.phylumConceptId = phylumConceptId;
	}
	/**
	 * @hibernate.property
	 * 	column="species_concept_id"
	 * 	not-null="false"
	 * insert="false"
	 * update="false"
	 * 
	 * @return the speciesConceptId
	 */
	public Long getSpeciesConceptId() {
		return speciesConceptId;
	}
	/**
	 * @param speciesConceptId the speciesConceptId to set
	 */
	public void setSpeciesConceptId(Long speciesConceptId) {
		this.speciesConceptId = speciesConceptId;
	}
	/**
	 * @return the rank
	 */
	public Integer getRank() {
		return rank;
	}
	/**
	 * @param rank the rank to set
	 */
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	/**
	 * @return the relationshipAssertions
	 */
	public Set<RelationshipAssertion> getRelationshipAssertions() {
		return relationshipAssertions;
	}
	/**
	 * @param relationshipAssertions the relationshipAssertions to set
	 */
	public void setRelationshipAssertions(
			Set<RelationshipAssertion> relationshipAssertions) {
		this.relationshipAssertions = relationshipAssertions;
	}
	/**
	 * @return the partnerConcept
	 */
	public TaxonConcept getPartnerConcept() {
		return partnerConcept;
	}
	/**
	 * @param partnerConcept
	 */
	public void setPartnerConcept(TaxonConcept partnerConcept) {
		this.partnerConcept = partnerConcept;
	}
	/**
	 * @return the dataResourceId
	 */
	public Long getDataResourceId() {
		return dataResourceId;
	}
	/**
	 * @param dataResourceId the dataResourceId to set
	 */
	public void setDataResourceId(Long dataResourceId) {
		this.dataResourceId = dataResourceId;
	}
	/**
	 * @return the dataProviderId
	 */
	public Long getDataProviderId() {
		return dataProviderId;
	}
	/**
	 * @param dataProviderId the dataProviderId to set
	 */
	public void setDataProviderId(Long dataProviderId) {
		this.dataProviderId = dataProviderId;
	}
	/**
	 * @return the isAccepted
	 */
	public boolean getIsAccepted() {
		return isAccepted;
	}
	/**
	 * @return the isAccepted
	 */
	public void setIsAccepted(boolean isAccepted) {
		this.isAccepted = isAccepted;
	}		
	/**
	 * @return the isAccepted
	 */
	public boolean isAccepted() {
		return isAccepted;
	}
	/**
	 * @param isAccepted the isAccepted to set
	 */
	public void setAccepted(boolean isAccepted) {
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
	 * @return the nubConceptId
	 */
	public Long getNubConceptId() {
		return nubConceptId;
	}
	/**
	 * @param nubConceptId the nubConceptId to set
	 */
	public void setNubConceptId(Long nubConceptId) {
		this.nubConceptId = nubConceptId;
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
	/**
	 * @return the countries
	 */
	public Set<Country> getCountries() {
		return countries;
	}
	/**
	 * @param countries the countries to set
	 */
	public void setCountries(Set<Country> countries) {
		this.countries = countries;
	}
}