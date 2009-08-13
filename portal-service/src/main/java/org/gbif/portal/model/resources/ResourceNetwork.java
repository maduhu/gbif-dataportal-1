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
 * ResourceNetwork
 * 
 * Represents a resource network which may include data resources from 
 * various resource networks 
 * 
 * Object representation of the ResourceNetwork data model concept.
 * 
 * @author Donald Hobern
 * 
 * @hibernate.class
 * 	table="resource_network"
 */
public class ResourceNetwork extends BaseObject {
	
	/**The resource network name**/
	protected String name;
	/**The resource network short code**/
	protected String code;
	/**The resource network description**/	
	protected String description;
	/**The resource network address**/	
	protected String address;
	/**The resource network website url**/	
	protected String websiteUrl;
	/**The resource network logo url**/	
	protected String logoUrl;
	/**The resource network email**/	
	protected String email;
	/**The resource network telephone**/	
	protected String telephone;
	/** The number of occurrences records this resource network provides */
	protected Integer occurrenceCount;
	/** The number of occurrences records with geo reference data this resource network provides */
	protected Integer occurrenceCoordinateCount;
	/** The number of concepts this resource network provides */
	protected Integer conceptCount;
	/** The number of species this resource network provides */
	protected Integer speciesCount;	
	/** The number of data resources this provider hass */
	protected Integer dataResourceCount;		
	/** The participant resources in this network */
	protected Set<NetworkMembership> networkMemberships;
	/** The creation date for the provider object */
	protected Date created;	
	/** The last modification date for the provider object */
	protected Date modified;	
	/** The deletion date for the provider object */
	protected Date deleted;	

	/**
	 * Default Constructor.
	 */
	public ResourceNetwork(){}
	
	/**
	 * Initialises the id and name only.
	 */
	public ResourceNetwork(long id, String name){
		this.id = id;
		this.name = name;
	}	

	/**
	 * Initialises the id and name only.
	 */
	public ResourceNetwork(long id, String name, String code, String description, String websiteUrl, String logoUrl){
		this.id = id;
		this.name = name;
		this.code = code;
		this.description = description;
		this.websiteUrl = websiteUrl;
		this.logoUrl = logoUrl;
	}

	/**
	 * Initialises the id and name and counts.
	 */
	public ResourceNetwork(long id, String name, Integer occurrenceCount, Integer occurrenceCoordinateCount, Integer conceptCount, Integer speciesCount, Integer dataResourceCount){
		this.id = id;
		this.name = name;
		this.occurrenceCount = occurrenceCount;
		this.occurrenceCoordinateCount = occurrenceCoordinateCount;
		this.conceptCount = conceptCount;
		this.speciesCount = speciesCount;
		this.dataResourceCount = dataResourceCount;
	}	
	
	/**
	 * Initialises the id and name and counts.
	 */
	public ResourceNetwork(long id, String name, String code, Integer occurrenceCount, Integer occurrenceCoordinateCount, Integer conceptCount, Integer speciesCount, Integer dataResourceCount){
		this.id = id;
		this.code = code;
		this.name = name;
		this.occurrenceCount = occurrenceCount;
		this.occurrenceCoordinateCount = occurrenceCoordinateCount;
		this.conceptCount = conceptCount;
		this.speciesCount = speciesCount;
		this.dataResourceCount = dataResourceCount;
	}	
	
	/**
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
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
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
}