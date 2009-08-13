/**
 * 
 */
package org.gbif.portal.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * A ResourceAccess point encapsulates the transport information necesasry to access a 
 * DataResource.
 * 
 * It should be noted that a URL alone does not necessarily identify a resource uniquely. 
 * For example, with DiGIR many resources exist at the same URL but are identified using parameters
 * in the request that is issued.  Thus the remoteIdAtUrl exists.
 * 
 * @author tim
 */
public class ResourceAccessPoint extends ModelObject {
	/**
	 * Generated
	 */
	private static final long serialVersionUID = 6297094778121617731L;
	protected long dataResourceId;
	protected long dataProviderId;
	protected String url;
	protected String remoteIdAtUrl;
	protected String uuid;
	protected boolean supportsDateLastModified;
	protected Date dateLastHarvestStarted;
	protected Date dateLastExtractStarted;
	protected Integer intervalMetadataDays;
	protected Integer intervalHarvestDays;
	// This is indeed a seperate table, but we only care about the ORDERED values
	protected List<String> namespaces = new LinkedList<String>();
	protected Date created;
	protected Date modified;
	protected Date deleted;
	
	/**
	 * Convienience
	 */
	public ResourceAccessPoint(long id, long dataProviderId, long dataResourceId, String url, String remoteIdAtUrl, String uuid, boolean supportsDateLastModified, Date dateLastHarvestStarted, Date dateLastExtractStarted, Integer intervalMetadataDays, Integer intervalHarvestDays, Date created, Date modified, Date deleted) {
		this.id = id;
		this.dataProviderId = dataProviderId;
		this.dataResourceId = dataResourceId;
		this.url = url;
		this.remoteIdAtUrl = remoteIdAtUrl;
		this.uuid = uuid;
		this.supportsDateLastModified = supportsDateLastModified;
		this.dateLastHarvestStarted = dateLastHarvestStarted;
		this.dateLastExtractStarted = dateLastExtractStarted;
		this.intervalMetadataDays = intervalMetadataDays;
		this.intervalHarvestDays = intervalHarvestDays;
		this.created = created;
		this.modified = modified;
		this.deleted = deleted;
	}
	
	/**
	 * Default 
	 */
	public ResourceAccessPoint() {
		super();
	}

	/**
	 * @return Returns the created.
	 */
	public Date getCreated() {
		return created;
	}
	/**
	 * @param created The created to set.
	 */
	public void setCreated(Date created) {
		this.created = created;
	}
	/**
	 * @return Returns the dataResourceId.
	 */
	public long getDataResourceId() {
		return dataResourceId;
	}
	/**
	 * @param dataResourceId The dataResourceId to set.
	 */
	public void setDataResourceId(long dataResourceId) {
		this.dataResourceId = dataResourceId;
	}
	/**
	 * @return Returns the deleted.
	 */
	public Date getDeleted() {
		return deleted;
	}
	/**
	 * @param deleted The deleted to set.
	 */
	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}
	/**
	 * @return Returns the modified.
	 */
	public Date getModified() {
		return modified;
	}
	/**
	 * @param modified The modified to set.
	 */
	public void setModified(Date modified) {
		this.modified = modified;
	}
	/**
	 * @return Returns the namespaces.
	 */
	public List<String> getNamespaces() {
		return namespaces;
	}
	/**
	 * @param namespaces The namespaces to set.
	 */
	public void setNamespaces(List<String> namespaces) {
		this.namespaces = namespaces;
	}
	/**
	 * @return Returns the remoteIdAtUrl.
	 */
	public String getRemoteIdAtUrl() {
		return remoteIdAtUrl;
	}
	/**
	 * @param remoteIdAtUrl The remoteIdAtUrl to set.
	 */
	public void setRemoteIdAtUrl(String remoteIdAtUrl) {
		this.remoteIdAtUrl = remoteIdAtUrl;
	}
	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url The url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return Returns the uuid.
	 */
	public String getUuid() {
		return uuid;
	}
	/**
	 * @param uuid The uuid to set.
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	/**
	 * @return Returns the dataProviderId.
	 */
	public long getDataProviderId() {
		return dataProviderId;
	}
	/**
	 * @param dataProviderId The dataProviderId to set.
	 */
	public void setDataProviderId(long dataProviderId) {
		this.dataProviderId = dataProviderId;
	}

	/**
	 * @return Returns the supportsDateLastModified.
	 */
	public boolean isSupportsDateLastModified() {
		return supportsDateLastModified;
	}

	/**
	 * @param supportsDateLastModified The supportsDateLastModified to set.
	 */
	public void setSupportsDateLastModified(boolean supportsDateLastModified) {
		this.supportsDateLastModified = supportsDateLastModified;
	}

	/**
	 * @return Returns the dateLastExtractStarted.
	 */
	public Date getDateLastExtractStarted() {
		return dateLastExtractStarted;
	}

	/**
	 * @param dateLastExtractStarted The dateLastExtractStarted to set.
	 */
	public void setDateLastExtractStarted(Date dateLastExtractStarted) {
		this.dateLastExtractStarted = dateLastExtractStarted;
	}

	/**
	 * @return Returns the dateLastHarvestStarted.
	 */
	public Date getDateLastHarvestStarted() {
		return dateLastHarvestStarted;
	}

	/**
	 * @param dateLastHarvestStarted The dateLastHarvestStarted to set.
	 */
	public void setDateLastHarvestStarted(Date dateLastHarvestStarted) {
		this.dateLastHarvestStarted = dateLastHarvestStarted;
	}

	/**
	 * @return Returns the intervalHarvestDays.
	 */
	public Integer getIntervalHarvestDays() {
		return intervalHarvestDays;
	}

	/**
	 * @param intervalHarvestDays The intervalHarvestDays to set.
	 */
	public void setIntervalHarvestDays(Integer intervalHarvestDays) {
		this.intervalHarvestDays = intervalHarvestDays;
	}

	/**
	 * @return Returns the intervalMetadataDays.
	 */
	public Integer getIntervalMetadataDays() {
		return intervalMetadataDays;
	}

	/**
	 * @param intervalMetadataDays The intervalMetadataDays to set.
	 */
	public void setIntervalMetadataDays(Integer intervalMetadataDays) {
		this.intervalMetadataDays = intervalMetadataDays;
	}

	
}