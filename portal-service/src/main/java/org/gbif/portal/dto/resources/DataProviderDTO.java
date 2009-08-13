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
package org.gbif.portal.dto.resources;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This is for transferring data about a data provider
 * 
 * @author akalufya
 * @author dmartin
 */
public class DataProviderDTO implements Serializable{
	
	private static final long serialVersionUID = 8302101475374702873L;
	/**The data provider key**/
	protected String key;	
	/**The data provider uuid**/
	protected String uuid;		
	/**The data provider name**/
	protected String name;
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
	/** The number of higher concepts this data resource provides */
	protected Integer higherConceptCount;
	/** The number of species this data resource provides */
	protected Integer speciesCount;	
	/** The ISO country code for the provider location */
	protected String isoCountryCode;	
	/** The GBIF participant approving the provider */
	protected String gbifApprover;	
	/** The number of data resources provided by this provider */
	protected Integer dataResourceCount;
	/** The creation date for the provider object */
	protected Date created;	
	/** The last modification date for the provider object */
	protected Date modified;	
	/** The deletion date for the provider object */
	protected Date deleted;	
	
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
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if(object==null)
			return false;
		if(object instanceof DataProviderDTO){
			DataProviderDTO dr = (DataProviderDTO) object;
			if(dr.getKey()==null && this.getKey()==null)
				return true;
			if(dr.getKey()==null || this.getKey()==null)
				return true;
			return this.getKey().equals(dr.getKey());
		}
		return false;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}	
}