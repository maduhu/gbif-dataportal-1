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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Defines Taxonomic Concept details. This will typically be returned by drilldown
 * type service layer methods as it contains more detail than <code>BriefTaxonConceptDTO</code>
 *
 * @see BriefTaxonConceptDTO
 * @see TaxonConceptDTOFactory
 * 
 * @author dmartin
 */
public class TaxonConceptDTO extends BriefTaxonConceptDTO {
	
	public static int CONCEPT_STATUS_ACCEPTED_CONCEPT = 0;
	public static int CONCEPT_STATUS_AMBIGUOUS_SYNONYM = 1;
	public static int CONCEPT_STATUS_MISAPPLIED_NAME = 2;
	public static int CONCEPT_STATUS_PROVISIONALLY_APPLIED_NAME = 3;
	public static int CONCEPT_STATUS_SYNONYM = 4;
	
	/** serial version id */
	private static final long serialVersionUID = 9059518926593233240L;
	/** The scientific name */
	protected String scientificName;
	/** The author string */	
	protected String author;
	/** The publication key */
	protected String publicationKey;	
	/** The publication name */
	protected String publicationName;	
	/** The data resource key */
	protected String dataResourceKey;	
	/** The data resource name */
	protected String dataResourceName;	
	/** The data provider key */
	protected String dataProviderKey;	
	/** The data provider name */
	protected String dataProviderName;	
	/** The kingdom */
	protected String kingdom;
	/** The phylum division */
	protected String phylumDivision;
	/** The class */
	protected String klass;
	/** The order*/
	protected String order;
	/** The family */
	protected String family;
	/** The genus */
	protected String genus;
	/** The species */
	protected String species;
	/** @deprecated should be using partner concept key and isNubConcept property
	 * The key for the nub concept for this concept */
	protected String nubConceptKey;
	/** The accepted concept key, null if this concept is accepted */
	protected String acceptedConceptKey;		
	/** The accepted taxon name, null if this concept is accepted */
	protected String acceptedTaxonName;		
	/** concept status, defaults to accepted */
	protected int conceptStatus = CONCEPT_STATUS_ACCEPTED_CONCEPT;
	/** A common name associated with this taxon concept **/
	protected String commonName;
	/** A common name associated with this taxon concept */
	protected String commonNameLanguage;
	/** A common name associated with this taxon concept */
	protected int childCount;
	/** Status of the taxon concept **/
	protected String status;
	/** Citation for the taxon's data resource **/
	protected String dataResourceCitation;	
	
	/**
	 * @return the scientificName
	 */
	public String getName() {
		return scientificName;
	}
	/**
	 * @return the scientificName
	 */
	public String getScientificName() {
		return scientificName;
	}
	/**
	 * @param scientificName the scientificName to set
	 */
	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}
	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	/**
	 * @return the dataProviderKey
	 */
	public String getDataProviderKey() {
		return dataProviderKey;
	}
	/**
	 * @param dataProviderKey the dataProviderKey to set
	 */
	public void setDataProviderKey(String dataProviderKey) {
		this.dataProviderKey = dataProviderKey;
	}
	/**
	 * @return the dataProviderName
	 */
	public String getDataProviderName() {
		return dataProviderName;
	}
	/**
	 * @param dataProviderName the dataProviderName to set
	 */
	public void setDataProviderName(String dataProviderName) {
		this.dataProviderName = dataProviderName;
	}
	/**
	 * @return the dataResourceKey
	 */
	public String getDataResourceKey() {
		return dataResourceKey;
	}
	/**
	 * @param dataResourceKey the dataResourceKey to set
	 */
	public void setDataResourceKey(String dataResourceKey) {
		this.dataResourceKey = dataResourceKey;
	}
	/**
	 * @return the dataResourceName
	 */
	public String getDataResourceName() {
		return dataResourceName;
	}
	/**
	 * @param dataResourceName the dataResourceName to set
	 */
	public void setDataResourceName(String dataResourceName) {
		this.dataResourceName = dataResourceName;
	}
	/**
	 * @return the publicationKey
	 */
	public String getPublicationKey() {
		return publicationKey;
	}
	/**
	 * @param publicationKey the publicationKey to set
	 */
	public void setPublicationKey(String publicationKey) {
		this.publicationKey = publicationKey;
	}
	/**
	 * @return the publicationName
	 */
	public String getPublicationName() {
		return publicationName;
	}
	/**
	 * @param publicationName the publicationName to set
	 */
	public void setPublicationName(String publicationName) {
		this.publicationName = publicationName;
	}
	/**
	 * @return the family
	 */
	public String getFamily() {
		return family;
	}
	/**
	 * @param family the family to set
	 */
	public void setFamily(String family) {
		this.family = family;
	}
	/**
	 * @return the genus
	 */
	public String getGenus() {
		return genus;
	}
	/**
	 * @param genus the genus to set
	 */
	public void setGenus(String genus) {
		this.genus = genus;
	}
	/**
	 * @return the kingdom
	 */
	public String getKingdom() {
		return kingdom;
	}
	/**
	 * @param kingdom the kingdom to set
	 */
	public void setKingdom(String kingdom) {
		this.kingdom = kingdom;
	}
	/**
	 * @return the klass
	 */
	public String getKlass() {
		return klass;
	}
	/**
	 * @param klass the klass to set
	 */
	public void setKlass(String klass) {
		this.klass = klass;
	}
	/**
	 * @return the order
	 */
	public String getOrder() {
		return order;
	}
	/**
	 * @param order the order to set
	 */
	public void setOrder(String order) {
		this.order = order;
	}
	/**
	 * @return the phylumDivision
	 */
	public String getPhylumDivision() {
		return phylumDivision;
	}
	/**
	 * @param phylumDivision the phylumDivision to set
	 */
	public void setPhylumDivision(String phylumDivision) {
		this.phylumDivision = phylumDivision;
	}
	/**
	 * @return the species
	 */
	public String getSpecies() {
		return species;
	}
	/**
	 * @param species the species to set
	 */
	public void setSpecies(String species) {
		this.species = species;
	}
	/**
	 * @deprecated 
	 * @return the nubConceptKey
	 */
	public String getNubConceptKey() {
		return nubConceptKey;
	}
	/**
	 * @deprecated
	 * @param nubConceptKey the nubConceptKey to set
	 */
	public void setNubConceptKey(String nubConceptKey) {
		this.nubConceptKey = nubConceptKey;
	}		
	/**
	 * @return the acceptedConceptKey
	 */
	public String getAcceptedConceptKey() {
		return acceptedConceptKey;
	}
	/**
	 * @param acceptedConceptKey the acceptedConceptKey to set
	 */
	public void setAcceptedConceptKey(String acceptedConceptKey) {
		this.acceptedConceptKey = acceptedConceptKey;
	}
	/**
	 * @return the acceptedTaxonName
	 */
	public String getAcceptedTaxonName() {
		return acceptedTaxonName;
	}
	/**
	 * @param acceptedTaxonName the acceptedTaxonName to set
	 */
	public void setAcceptedTaxonName(String acceptedTaxonName) {
		this.acceptedTaxonName = acceptedTaxonName;
	}
	/**
	 * @return the conceptStatus
	 */
	public int getConceptStatus() {
		return conceptStatus;
	}
	/**
	 * @param conceptStatus the conceptStatus to set
	 */
	public void setConceptStatus(int conceptStatus) {
		this.conceptStatus = conceptStatus;
	}
	/**
	 * @return the commonName
	 */
	public String getCommonName() {
		return commonName;
	}
	/**
	 * @param commonName the commonName to set
	 */
	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}
	/**
	 * @return the commonNameLanguage
	 */
	public String getCommonNameLanguage() {
		return commonNameLanguage;
	}
	/**
	 * @param commonNameLanguage the commonNameLanguage to set
	 */
	public void setCommonNameLanguage(String commonNameLanguage) {
		this.commonNameLanguage = commonNameLanguage;
	}
	/**
	 * For performance reasons this may or may not be populated
	 * 
	 * @return the childCount
	 */
	public Integer getChildCount() {
		return childCount;
	}
	/**
	 * @param childCount the childCount to set
	 */
	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the dataResourceCitation
	 */
	public String getDataResourceCitation() {
		return dataResourceCitation;
	}
	/**
	 * @param dataResourceCitation the dataResourceCitation to set
	 */
	public void setDataResourceCitation(String dataResourceCitation) {
		this.dataResourceCitation = dataResourceCitation;
	}	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}	
}