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
package org.gbif.portal.dto.resources;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The DataResourceDTO. Contains the details of a data resource
 * that the system is serving data from.
 * 
 * @author dbarnier
 * @author dmartin
 */
public class DataResourceDTO implements Serializable {

	private static final long serialVersionUID = -7437757832188632369L;
	/**the key for this data resource **/
	protected String key;
	/**the name for this data resource **/
	protected String name;
	/**the description for this data resource **/
	protected String description;
	/**the website URL for this data resource **/
	protected String websiteUrl;
	/**the logo URL for this data resource **/
	protected String logoUrl;
	/**the rights for the data resource **/
	protected String rights;
	/**the citation for the data resource **/	
	protected String citation;
	/**the citable agent for the data resource **/	
	protected String citableAgent;
	/**Indicates if this Data Resource has its own taxonomy or is part of a shared taxonomy**/
	protected boolean sharedTaxonomy;
	/**the key of the data provider for this data resource **/
	protected String dataProviderKey;
	/**the name of the data provider for this data resource **/
	protected String dataProviderName;
	/** The number of occurrences records this data resource provides */
	protected Integer occurrenceCount;
	/** The number of occurrences records with geo reference data  this data resource provides */
	protected Integer occurrenceCoordinateCount;
	/** The number of occurrences records with geo reference data and no issues this data resource provides */
	protected Integer occurrenceCleanGeospatialCount;
	/** The number of occurrences records that should be provided by the data provider for this resource (as set in UDDI) */
	protected Integer providerRecordCount;	
	/** The number of concepts this data resource provides */
	protected Integer conceptCount;
	/** The number of higher concepts this data resource provides */
	protected Integer higherConceptCount;
	/** The number of species this data resource provides */
	protected Integer speciesCount;
	/** The number of higher concepts this data resource provides */
	protected Integer taxonomicPriority;
	/** The basis of record */
	protected String basisOfRecord;
	/** The root taxon rank for this resource as a simple string*/
	protected String rootTaxonRank;	
	/** The root taxon name for this resource*/
	protected String rootTaxonName;	
	/** The scope continent for this resource*/
	protected String scopeContinentCode;	
	/** The scope country for this resource*/
	protected String scopeCountryCode;	
	/** The creation date for the resource object */
	protected Date created;	
	/** The last modification date for the resource object */
	protected Date modified;	
	/** The deletion date for the resource object */
	protected Date deleted;	
	/** Indicates if a default citation should override the data resource's citation **/
	protected boolean overrideCitation;
	
	/**
	 * @return the citation
	 */
	public String getCitation() {
		return citation;
	}
	/**
	 * @param citation the citation to set
	 */
	public void setCitation(String citation) {
		this.citation = citation;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the rights
	 */
	public String getRights() {
		return rights;
	}
	/**
	 * @param rights the rights to set
	 */
	public void setRights(String rights) {
		this.rights = rights;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	/**
	 * @return the sharedTaxonomy
	 */
	public boolean isSharedTaxonomy() {
		return sharedTaxonomy;
	}
	/**
	 * @param sharedTaxonomy the sharedTaxonomy to set
	 */
	public void setSharedTaxonomy(boolean sharedTaxonomy) {
		this.sharedTaxonomy = sharedTaxonomy;
	}
	/**
	 * @return the conceptCount
	 */
	public Integer getConceptCount() {
		return conceptCount;
	}
	/**
	 * @param conceptCount the conceptCount to set
	 */
	public void setConceptCount(Integer conceptCount) {
		this.conceptCount = conceptCount;
	}
	/**
	 * @return the occurrenceCcoordinatesCount
	 */
	public Integer getOccurrenceCoordinateCount() {
		return occurrenceCoordinateCount;
	}
	/**
	 * @param occurrenceCcoordinateCount the occurrenceCcoordinateCount to set
	 */
	public void setOccurrenceCoordinateCount(Integer occurrenceCoordinateCount) {
		this.occurrenceCoordinateCount = occurrenceCoordinateCount;
	}
	/**
	 * @return the occurrenceCount
	 */
	public Integer getOccurrenceCount() {
		return occurrenceCount;
	}
	/**
	 * @param occurrenceCount the occurrenceCount to set
	 */
	public void setOccurrenceCount(Integer occurrenceCount) {
		this.occurrenceCount = occurrenceCount;
	}
	/**
	 * @return the speciesCount
	 */
	public Integer getSpeciesCount() {
		return speciesCount;
	}
	/**
	 * @param speciesCount the speciesCount to set
	 */
	public void setSpeciesCount(Integer speciesCount) {
		this.speciesCount = speciesCount;
	}
	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}
	/**
	 * @return the deleted
	 */
	public Date getDeleted() {
		return deleted;
	}
	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}
	/**
	 * @return the modified
	 */
	public Date getModified() {
		return modified;
	}
	/**
	 * @param modified the modified to set
	 */
	public void setModified(Date modified) {
		this.modified = modified;
	}
	/**
	 * @return the basisOfRecord
	 */
	public String getBasisOfRecord() {
		return basisOfRecord;
	}
	/**
	 * @param basisOfRecord the basisOfRecord to set
	 */
	public void setBasisOfRecord(String basisOfRecord) {
		this.basisOfRecord = basisOfRecord;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the logoUrl
	 */
	public String getLogoUrl() {
		return logoUrl;
	}
	/**
	 * @param logoUrl the logoUrl to set
	 */
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	/**
	 * @return the higherConceptCount
	 */
	public Integer getHigherConceptCount() {
		return higherConceptCount;
	}
	/**
	 * @param higherConceptCount the higherConceptCount to set
	 */
	public void setHigherConceptCount(Integer higherConceptCount) {
		this.higherConceptCount = higherConceptCount;
	}
	/**
	 * @return the citableAgent
	 */
	public String getCitableAgent() {
		return citableAgent;
	}
	/**
	 * @param citableAgent the citableAgent to set
	 */
	public void setCitableAgent(String citableAgent) {
		this.citableAgent = citableAgent;
	}
	/**
	 * @return the occurrenceCleanGeospatialCount
	 */
	public Integer getOccurrenceCleanGeospatialCount() {
		return occurrenceCleanGeospatialCount;
	}
	/**
	 * @param occurrenceCleanGeospatialCount the occurrenceCleanGeospatialCount to set
	 */
	public void setOccurrenceCleanGeospatialCount(
			Integer occurrenceCleanGeospatialCount) {
		this.occurrenceCleanGeospatialCount = occurrenceCleanGeospatialCount;
	}
	/**
	 * @return the rootTaxonName
	 */
	public String getRootTaxonName() {
		return rootTaxonName;
	}
	/**
	 * @param rootTaxonName the rootTaxonName to set
	 */
	public void setRootTaxonName(String rootTaxonName) {
		this.rootTaxonName = rootTaxonName;
	}
	/**
	 * @return the rootTaxonRank
	 */
	public String getRootTaxonRank() {
		return rootTaxonRank;
	}
	/**
	 * @param rootTaxonRank the rootTaxonRank to set
	 */
	public void setRootTaxonRank(String rootTaxonRank) {
		this.rootTaxonRank = rootTaxonRank;
	}
	/**
	 * @return the scopeContinentCode
	 */
	public String getScopeContinentCode() {
		return scopeContinentCode;
	}
	/**
	 * @param scopeContinentCode the scopeContinentCode to set
	 */
	public void setScopeContinentCode(String scopeContinentCode) {
		this.scopeContinentCode = scopeContinentCode;
	}
	/**
	 * @return the scopeCountryCode
	 */
	public String getScopeCountryCode() {
		return scopeCountryCode;
	}
	/**
	 * @param scopeCountryCode the scopeCountryCode to set
	 */
	public void setScopeCountryCode(String scopeCountryCode) {
		this.scopeCountryCode = scopeCountryCode;
	}
	/**
	 * @return the providerRecordCount
	 */
	public Integer getProviderRecordCount() {
		return providerRecordCount;
	}
	/**
	 * @param providerRecordCount the providerRecordCount to set
	 */
	public void setProviderRecordCount(Integer providerRecordCount) {
		this.providerRecordCount = providerRecordCount;
	}
	/**
	 * @return the websiteUrl
	 */
	public String getWebsiteUrl() {
		return websiteUrl;
	}
	/**
	 * @param websiteUrl the websiteUrl to set
	 */
	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
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
	 * @return the overrideCitation
	 */
	public boolean isOverrideCitation() {
		return overrideCitation;
	}
	/**
	 * @param overrideCitation the overrideCitation to set
	 */
	public void setOverrideCitation(boolean overrideCitation) {
		this.overrideCitation = overrideCitation;
	}
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if(object==null)
			return false;
		if(object instanceof DataResourceDTO){
			DataResourceDTO dr = (DataResourceDTO) object;
			if(dr.getKey()==null && this.getKey()==null)
				return true;
			if(dr.getKey()==null || this.getKey()==null)
				return true;
			return this.getKey().equals(dr.getKey());
		}
		return false;
	}
}