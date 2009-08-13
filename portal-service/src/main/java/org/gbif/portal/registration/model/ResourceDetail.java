package org.gbif.portal.registration.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Encapsulates the details for a resource.
 * In UDDI terms, this is a Service and it's TModels
 * 
 * @todo Many of the fields from the wiki are omitted at this point and will
 * be added
 * @link http://wiki.gbif.org/dadiwiki/wikka.php?wakka=RegistrationInterface
 * 
 * This is a simple java bean and hence further javadoc is omitted for properties. 
 * @author trobertson
 */	
public class ResourceDetail {
	
	protected String serviceKey;
	protected String name;
	protected String resourceType;
	protected String code;
	protected String website;
	protected String accessPoint;	
	protected String ownerName;
	protected String ownerAddress;
	protected String ownerCountry;
	protected String ownerCountryName;
	protected String description;
	protected String rights;
	protected String citation;	
	protected String usageIPR;
	protected String logoURL;
	protected List<String> relatesToCountries = new ArrayList<String>();
	protected String highestTaxa;
	protected String highestTaxaConceptId;
	protected String recordBasis;
	protected Date startDate;
	protected Date endDate;
	protected Date modified;
	protected Date created;
	protected Integer recordCount;
	protected String indexingStartTime;
	protected String indexingMaxDuration;
	protected String indexingFrequency;
	protected boolean isLivingCollection = false;
	protected String contactName;
	protected String contactEmail;
	protected String contactPhone;
	protected List<String> resourceNetworks = new ArrayList<String>();	
	
	protected Float northCoordinate;
	protected Float southCoordinate;
	protected Float eastCoordinate;
	protected Float westCoordinate;
	
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the highestTaxa.
	 */
	public String getHighestTaxa() {
		return highestTaxa;
	}
	/**
	 * @param highestTaxa The highestTaxa to set.
	 */
	public void setHighestTaxa(String highestTaxa) {
		this.highestTaxa = highestTaxa;
	}
	/**
	 * @return Returns the logoURL.
	 */
	public String getLogoURL() {
		return logoURL;
	}
	/**
	 * @param logoURL The logoURL to set.
	 */
	public void setLogoURL(String logoURL) {
		this.logoURL = logoURL;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the ownerCountry.
	 */
	public String getOwnerCountry() {
		return ownerCountry;
	}
	/**
	 * @param ownerCountry The ownerCountry to set.
	 */
	public void setOwnerCountry(String ownerCountry) {
		this.ownerCountry = ownerCountry;
	}
	/**
	 * @return Returns the ownerName.
	 */
	public String getOwnerName() {
		return ownerName;
	}
	/**
	 * @param ownerName The ownerName to set.
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	/**
	 * @return Returns the recordBasis.
	 */
	public String getRecordBasis() {
		return recordBasis;
	}
	/**
	 * @param recordBasis The recordBasis to set.
	 */
	public void setRecordBasis(String recordBasis) {
		this.recordBasis = recordBasis;
	}
	/**
	 * @return Returns the serviceKey.
	 */
	public String getServiceKey() {
		return serviceKey;
	}
	/**
	 * @param serviceKey The serviceKey to set.
	 */
	public void setServiceKey(String serviceKey) {
		this.serviceKey = serviceKey;
	}
	/**
	 * @return Returns the usageIPR.
	 */
	public String getUsageIPR() {
		return usageIPR;
	}
	/**
	 * @param usageIPR The usageIPR to set.
	 */
	public void setUsageIPR(String usageIPR) {
		this.usageIPR = usageIPR;
	}
	/**
	 * @return Returns the ownerAddress.
	 */
	public String getOwnerAddress() {
		return ownerAddress;
	}
	/**
	 * @param ownerAddress The ownerAddress to set.
	 */
	public void setOwnerAddress(String ownerAddress) {
		this.ownerAddress = ownerAddress;
	}
	/**
	 * @return Returns the highestTaxaConceptId.
	 */
	public String getHighestTaxaConceptId() {
		return highestTaxaConceptId;
	}
	/**
	 * @param highestTaxaConceptId The highestTaxaConceptId to set.
	 */
	public void setHighestTaxaConceptId(String highestTaxaConceptId) {
		this.highestTaxaConceptId = highestTaxaConceptId;
	}
	/**
	 * @return the ownerCountryName
	 */
	public String getOwnerCountryName() {
		return ownerCountryName;
	}
	/**
	 * @param ownerCountryName the ownerCountryName to set
	 */
	public void setOwnerCountryName(String ownerCountryName) {
		this.ownerCountryName = ownerCountryName;
	}
	/**
	 * @return the relatesToCountries
	 */
	public List<String> getRelatesToCountries() {
		return relatesToCountries;
	}
	/**
	 * @param relatesToCountries the relatesToCountries to set
	 */
	public void setRelatesToCountries(List<String> relatesToCountries) {
		this.relatesToCountries = relatesToCountries;
	}
	/**
	 * @return the resourceNetworks
	 */
	public List<String> getResourceNetworks() {
		return resourceNetworks;
	}
	/**
	 * @param resourceNetworks the resourceNetworks to set
	 */
	public void setResourceNetworks(List<String> resourceNetworks) {
		this.resourceNetworks = resourceNetworks;
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
	 * @return the indexingFrequency
	 */
	public String getIndexingFrequency() {
		return indexingFrequency;
	}
	/**
	 * @param indexingFrequency the indexingFrequency to set
	 */
	public void setIndexingFrequency(String indexingFrequency) {
		this.indexingFrequency = indexingFrequency;
	}
	/**
	 * @return the indexingMaxDuration
	 */
	public String getIndexingMaxDuration() {
		return indexingMaxDuration;
	}
	/**
	 * @param indexingMaxDuration the indexingMaxDuration to set
	 */
	public void setIndexingMaxDuration(String indexingMaxDuration) {
		this.indexingMaxDuration = indexingMaxDuration;
	}
	/**
	 * @return the indexingStartTime
	 */
	public String getIndexingStartTime() {
		return indexingStartTime;
	}
	/**
	 * @param indexingStartTime the indexingStartTime to set
	 */
	public void setIndexingStartTime(String indexingStartTime) {
		this.indexingStartTime = indexingStartTime;
	}
	/**
	 * @return the recordCount
	 */
	public Integer getRecordCount() {
		return recordCount;
	}
	/**
	 * @param recordCount the recordCount to set
	 */
	public void setRecordCount(Integer recordCount) {
		this.recordCount = recordCount;
	}
	/**
	 * @return the resourceType
	 */
	public String getResourceType() {
		return resourceType;
	}
	/**
	 * @param resourceType the resourceType to set
	 */
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	/**
	 * @return the accessPoint
	 */
	public String getAccessPoint() {
		return accessPoint;
	}
	/**
	 * @param accessPoint the accessPoint to set
	 */
	public void setAccessPoint(String accessPoint) {
		this.accessPoint = accessPoint;
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
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}
	/**
	 * @param website the website to set
	 */
	public void setWebsite(String website) {
		this.website = website;
	}
	/**
	 * @return the isLivingCollection
	 */
	public boolean isLivingCollection() {
		return isLivingCollection;
	}
	/**
	 * @return the isLivingCollection
	 */
	public boolean getIsLivingCollection() {
		return isLivingCollection;
	}	
	/**
	 * @param isLivingCollection the isLivingCollection to set
	 */
	public void setLivingCollection(boolean isLivingCollection) {
		this.isLivingCollection = isLivingCollection;
	}
	/**
	 * @return the contactEmail
	 */
	public String getContactEmail() {
		return contactEmail;
	}
	/**
	 * @param contactEmail the contactEmail to set
	 */
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	/**
	 * @return the contactName
	 */
	public String getContactName() {
		return contactName;
	}
	/**
	 * @param contactName the contactName to set
	 */
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	/**
	 * @return the contactPhone
	 */
	public String getContactPhone() {
		return contactPhone;
	}
	/**
	 * @param contactPhone the contactPhone to set
	 */
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the northCoordinate
	 */
	public Float getNorthCoordinate() {
		return northCoordinate;
	}
	/**
	 * @param northCoordinate the northCoordinate to set
	 */
	public void setNorthCoordinate(Float northCoordinate) {
		this.northCoordinate = northCoordinate;
	}
	/**
	 * @return the southCoordinate
	 */
	public Float getSouthCoordinate() {
		return southCoordinate;
	}
	/**
	 * @param southCoordinate the southCoordinate to set
	 */
	public void setSouthCoordinate(Float southCoordinate) {
		this.southCoordinate = southCoordinate;
	}
	/**
	 * @return the eastCoordinate
	 */
	public Float getEastCoordinate() {
		return eastCoordinate;
	}
	/**
	 * @param eastCoordinate the eastCoordinate to set
	 */
	public void setEastCoordinate(Float eastCoordinate) {
		this.eastCoordinate = eastCoordinate;
	}
	/**
	 * @return the westCoordinate
	 */
	public Float getWestCoordinate() {
		return westCoordinate;
	}
	/**
	 * @param westCoordinate the westCoordinate to set
	 */
	public void setWestCoordinate(Float westCoordinate) {
		this.westCoordinate = westCoordinate;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}	
}