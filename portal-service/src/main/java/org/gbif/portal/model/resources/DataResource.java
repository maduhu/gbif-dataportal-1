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

package org.gbif.portal.model.resources;

import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.gbif.portal.model.BaseObject;
import org.gbif.portal.model.occurrence.BasisOfRecord;
import org.gbif.portal.model.taxonomy.TaxonRank;

/**
 * DataResource
 * 
 * Object representation of the DataResource model concept.
 * <url>http://wiki.gbif.org/dadiwiki/wikka.php?wakka=DataResource</url>
 *
 * @author dbarnier
 * @author dmartin
 * 
 * @hibernate.class
 * 	table="data_resource"
 */
public class DataResource extends BaseObject {

	/**The Data Provider this resource is associated with **/
	protected Long dataProviderId;
	/**The Data Provider this resource is associated with **/
	protected DataProvider dataProvider;
	/**The resource name **/
	protected String name;
	/**The rights associated with this resource**/
	protected String rights;
	/**The citation associated with this resource**/
	protected String citation;
	/**The citable agent associated with this resource**/
	protected String citableAgent;
	/**The description associated with this resource**/
	protected String description;
	/**The website URL associated with this resource**/
	protected String websiteUrl;
	/**The logo URL associated with this resource**/
	protected String logoUrl;
	/**Indicates if this Data Resource has its own taxonomy or is part of a shared taxonomy**/
	protected boolean sharedTaxonomy;
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
	/** The taxonomic priority of this resource */
	protected Integer taxonomicPriority;	
	/** The basis of record for this data resource */
	protected BasisOfRecord basisOfRecord;	
	/** The rank of the root taxon for this data resource */
	protected TaxonRank rootTaxonRank;	
	/** The name of the root taxon for this data resource */
	protected String rootTaxonName;	
	/** The continent scope for this data resource */
	protected String scopeContinentCode;	
	/** The country scope for this data resource */
	protected String scopeCountryCode;	
	/** The creation date for the resource object */
	protected Date created;	
	/** The last modification date for the resource object */
	protected Date modified;	
	/** The deletion date for the resource object */
	protected Date deleted;
	/**Links to associated resource networks**/
	protected Set<NetworkMembership> networkMemberships;
	/**Links to associated agents**/
	protected Set<DataResourceAgent> dataResourceAgents;
	/** The resource access point **/
	protected Set<ResourceAccessPoint> resourceAccessPoints;
	/** The resource ranks **/
	protected Set<ResourceRank> resourceRanks;
	/** Override citation **/
	protected boolean overrideCitation;

	/**
	 * Default Constructor.
	 */
	public DataResource(){}
	
	/**
	 * Initialises the id and name only.
	 */
	public DataResource(long id, String name, boolean sharedTaxonomy,  Integer occurrenceCount, Integer occurrenceCoordinateCount, Integer conceptCount, Integer speciesCount, Integer dataProviderId, String dataProviderName){
		this.id = id;
		this.name = name;
		this.sharedTaxonomy = sharedTaxonomy;
		this.occurrenceCount = occurrenceCount;
		this.occurrenceCoordinateCount = occurrenceCoordinateCount;
		this.conceptCount = conceptCount;
		this.speciesCount = speciesCount;		
		this.dataProvider = new DataProvider(dataProviderId, dataProviderName);
	}		
	
	/**
	 * @hibernate.many-to-one
	 *	column="data_provider_id"
	 * 	cascade="save-update"
	 * 	not-null="false"
	 */
	public DataProvider getDataProvider() {
		return dataProvider;
	}
	public void setDataProvider(DataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}	
	
	/**
	 * @hibernate.property
	 * 	column="name"
	 * 	not-null="true"
	 */
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @hibernate.property
	 * 	column="rights"
	 * 	not-null="false"
	 */
	public String getRights() {
		return this.rights;
	}
	public void setRights(String rights) {
		this.rights = rights;
	}

	/**
	 * @hibernate.property
	 * 	column="citation"
	 * 	not-null="false"
	 */
	public String getCitation() {
		return this.citation;
	}
	public void setCitation(String citation) {
		this.citation = citation;
	}
	
	/**
	 * @hibernate.property
	 * 	column="override_citation"
	 * 
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
	 * @hibernate.property
	 * 	column="shared_taxonomy"
	 * 
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
	 * @hibernate.property
	 * 	column="concept_count"
	 * 
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
	 * @hibernate.property
	 * 	column="occurrence_coordinate_count" 
	 * 
	 * @return the occurrenceCoordinateCount
	 */
	public Integer getOccurrenceCoordinateCount() {
		return occurrenceCoordinateCount;
	}

	/**
	 * @param occurrenceCoordinateCount the occurrenceCoordinateCount to set
	 */
	public void setOccurrenceCoordinateCount(Integer occurrenceCoordinateCount) {
		this.occurrenceCoordinateCount = occurrenceCoordinateCount;
	}

	/**
	 * @hibernate.property
	 * 	column="occurrence_count" 
	 * 
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
	 * @hibernate.property
	 * 	column="species_count" 
	 * 
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
	 * @see org.gbif.model.BaseObject#equals()
	 */
	public boolean equals(Object object) {
		if (object instanceof DataResource) {
			return super.equals(object);
		}
		return false;
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
	public BasisOfRecord getBasisOfRecord() {
		return basisOfRecord;
	}

	/**
	 * @param basisOfRecord the basisOfRecord to set
	 */
	public void setBasisOfRecord(BasisOfRecord basisOfRecord) {
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
	public TaxonRank getRootTaxonRank() {
		return rootTaxonRank;
	}

	/**
	 * @param rootTaxonRank the rootTaxonRank to set
	 */
	public void setRootTaxonRank(TaxonRank rootTaxonRank) {
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
	 * @return the networkMemberships
	 */
	public Set<NetworkMembership> getNetworkMemberships() {
		return networkMemberships;
	}

	/**
	 * @param networkMemberships the networkMemberships to set
	 */
	public void setNetworkMemberships(Set<NetworkMembership> networkMemberships) {
		this.networkMemberships = networkMemberships;
	}

	/**
	 * @return Returns the resourceAccessPoints.
	 */
	public Set<ResourceAccessPoint> getResourceAccessPoints() {
		return resourceAccessPoints;
	}

	/**
	 * @param resourceAccessPoints The resourceAccessPoints to set.
	 */
	public void setResourceAccessPoints(
			Set<ResourceAccessPoint> resourceAccessPoints) {
		this.resourceAccessPoints = resourceAccessPoints;
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
	 * @return the resourceRanks
	 */
	public Set<ResourceRank> getResourceRanks() {
		return resourceRanks;
	}

	/**
	 * @param resourceRanks the resourceRanks to set
	 */
	public void setResourceRanks(Set<ResourceRank> resourceRanks) {
		this.resourceRanks = resourceRanks;
	}

	/**
	 * @return the dataResourceAgents
	 */
	public Set<DataResourceAgent> getDataResourceAgents() {
		return dataResourceAgents;
	}

	/**
	 * @param dataResourceAgents the dataResourceAgents to set
	 */
	public void setDataResourceAgents(Set<DataResourceAgent> dataResourceAgents) {
		this.dataResourceAgents = dataResourceAgents;
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}	
}