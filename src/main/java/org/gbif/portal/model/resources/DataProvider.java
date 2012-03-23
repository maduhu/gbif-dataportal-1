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

import org.gbif.portal.model.BaseObject;

/**
 * DataProvider
 * 
 * Represents a GBIF Data Provider which may provide DataResources 
 * 
 * Object representation of the DataProvider data model concept.
 * http://wiki.gbif.org/dadiwiki/wikka.php?wakka=DataProvider
 * 
 * @author dbarnier
 * @author dmartin
 * 
 * @hibernate.class
 * 	table="data_provider"
 */
public class DataProvider extends BaseObject {
	
	/**The data provider name**/
	protected String name;
	/**The data provider unique identifer**/	
	protected String uuid;
	/**The data provider description**/	
	protected String description;
	/**The data provider address**/	
	protected String address;
	/**The data provider website url**/	
	protected String websiteUrl;
	/**The data provider logo url**/	
	protected String logoUrl;
	/**The data provider email**/	
	protected String email;
	/**The data provider telephone**/	
	protected String telephone;
	/** The number of occurrences records this data resource provides */
	protected Integer occurrenceCount;
	/** The number of occurrences records with geo reference data  this data resource provides */
	protected Integer occurrenceCoordinateCount;
	/** The number of concepts this data resource provides */
	protected Integer conceptCount;
	/** The number of higher concepts this data provider provides */
	protected Integer higherConceptCount;
	/** The number of species this data resource provides */
	protected Integer speciesCount;	
	/** The number of data resources this provider hass */
	protected Integer dataResourceCount;		
	/** The ISO country code for the provider location */
	protected String isoCountryCode;	
	/** The GBIF participant approving the provider */
	protected String gbifApprover;	
	/**Links to associated agents**/
	protected Set<DataProviderAgent> dataProviderAgents;
	/**Links to associated resources**/
	protected Set<DataResource> dataResources;	
	/**Links to associated raps**/
	protected Set<ResourceAccessPoint> resourceAccessPoints;
	/** The creation date for the provider object */
	protected Date created;	
	/** The last modification date for the provider object */
	protected Date modified;	
	/** The deletion date for the provider object */
	protected Date deleted;	

	/**
	 * Default Constructor.
	 */
	public DataProvider(){}
	
	/**
	 * Initialises the id and name only.
	 */
	public DataProvider(long id, String name){
		this.id = id;
		this.name = name;
	}	

	/**
	 * Initialises the id and name only.
	 */
	public DataProvider(long id, String name, Integer occurrenceCount, Integer occurrenceCoordinateCount, Integer conceptCount, Integer speciesCount, Integer dataResourceCount){
		this.id = id;
		this.name = name;
		this.occurrenceCount = occurrenceCount;
		this.occurrenceCoordinateCount = occurrenceCoordinateCount;
		this.conceptCount = conceptCount;
		this.speciesCount = speciesCount;
		this.dataResourceCount = dataResourceCount;
	}	
		

	/**
	 * Initialises loads.
	 */
	public DataProvider(long id, String name, Integer occurrenceCount, Integer occurrenceCoordinateCount, Integer conceptCount, Integer speciesCount, Integer dataResourceCount, String isoCountryCode){
		this.id = id;
		this.name = name;
		this.occurrenceCount = occurrenceCount;
		this.occurrenceCoordinateCount = occurrenceCoordinateCount;
		this.conceptCount = conceptCount;
		this.speciesCount = speciesCount;
		this.dataResourceCount = dataResourceCount;
		this.isoCountryCode = isoCountryCode;
	}	
		
	/**
	 * @hibernate.property
	 * 	column="name"
	 * 	not-null="true"
	 * 
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
	 * @hibernate.property
	 * 	column="address"
	 * 	not-null="true"
	 * 
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @hibernate.property
	 * 	column="description"
	 * 	not-null="true"
	 * 
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
	 * @hibernate.property
	 * 	column="email"
	 * 	not-null="true"
	 * 
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @hibernate.property
	 * 	column="logo_url"
	 * 	not-null="true"
	 * 
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
	 * @hibernate.property
	 * 	column="telephone"
	 * 	not-null="true"
	 * 
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}
	/**
	 * @param telephone the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	/**
	 * @hibernate.property
	 * 	column="website_url"
	 * 	not-null="true"
	 * 
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
		if (object instanceof DataProvider) {
			return super.equals(object);
		}
		return false;
	}

	/**
	 * @return the isoCountryCode
	 */
	public String getIsoCountryCode() {
		return isoCountryCode;
	}

	/**
	 * @param isoCountryCode the isoCountryCode to set
	 */
	public void setIsoCountryCode(String isoCountryCode) {
		this.isoCountryCode = isoCountryCode;
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
	 * @return the dataProviderAgents
	 */
	public Set<DataProviderAgent> getDataProviderAgents() {
		return dataProviderAgents;
	}

	/**
	 * @param dataProviderAgents the dataProviderAgents to set
	 */
	public void setDataProviderAgents(Set<DataProviderAgent> dataProviderAgents) {
		this.dataProviderAgents = dataProviderAgents;
	}

	/**
	 * @return the gbifApprover
	 */
	public String getGbifApprover() {
		return gbifApprover;
	}

	/**
	 * @param gbifApprover the gbifApprover to set
	 */
	public void setGbifApprover(String gbifApprover) {
		this.gbifApprover = gbifApprover;
	}

	/**
	 * @return the dataResourceCount
	 */
	public Integer getDataResourceCount() {
		return dataResourceCount;
	}

	/**
	 * @param dataResourceCount the dataResourceCount to set
	 */
	public void setDataResourceCount(Integer dataResourceCount) {
		this.dataResourceCount = dataResourceCount;
	}

	/**
	 * @return Returns the dataResources.
	 */
	public Set<DataResource> getDataResources() {
		return dataResources;
	}

	/**
	 * @param dataResources The dataResources to set.
	 */
	public void setDataResources(Set<DataResource> dataResources) {
		this.dataResources = dataResources;
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
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}