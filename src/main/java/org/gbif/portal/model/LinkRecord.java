/**
 * 
 */
package org.gbif.portal.model;


/**
 * Represents the  data captured for a single link
 * 
 * @author Donald Hobern
 */
public class LinkRecord extends ModelObject {
	/**
	 * Generated
	 */
	private static final long serialVersionUID = 8073930435377102050L;
	
    protected long dataResourceId;
    protected long occurrenceId;
    protected long taxonConceptId;
    protected String rawLinkType;
    protected int linkType;
    protected String url;
    protected String description;
        
    /**
     * Default
     */
    public LinkRecord() {    	
    }
    
	/**
	 * Convenience
	 */
	public LinkRecord(long dataResourceId, long occurrenceId, long taxonConceptId, String rawLinkType, int linkType, String url, String description) {
		this.dataResourceId = dataResourceId;
		this.occurrenceId = occurrenceId;
		this.taxonConceptId = taxonConceptId;
		this.rawLinkType = rawLinkType;
		this.linkType = linkType;
		this.url = url;
		this.description = description;
	}
	/**
	 * Convenience
	 */
	public LinkRecord(long id, long dataResourceId, long occurrenceId, long taxonConceptId, String rawLinkType, int linkType, String url, String description) {
		this.id = id;
		this.dataResourceId = dataResourceId;
		this.occurrenceId = occurrenceId;
		this.taxonConceptId = taxonConceptId;
		this.rawLinkType = rawLinkType;
		this.linkType = linkType;
		this.url = url;
		this.description = description;
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
	 * @return Returns the linkType.
	 */
	public int getLinkType() {
		return linkType;
	}

	/**
	 * @param linkType The linkType to set.
	 */
	public void setLinkType(int linkType) {
		this.linkType = linkType;
	}

	/**
	 * @return Returns the occurrenceId.
	 */
	public long getOccurrenceId() {
		return occurrenceId;
	}

	/**
	 * @param occurrenceId The occurrenceId to set.
	 */
	public void setOccurrenceId(long occurrenceId) {
		this.occurrenceId = occurrenceId;
	}

	/**
	 * @return Returns the rawLinkType.
	 */
	public String getRawLinkType() {
		return rawLinkType;
	}

	/**
	 * @param rawLinkType The rawLinkType to set.
	 */
	public void setRawLinkType(String rawLinkType) {
		this.rawLinkType = rawLinkType;
	}

	/**
	 * @return Returns the taxonConceptId.
	 */
	public long getTaxonConceptId() {
		return taxonConceptId;
	}

	/**
	 * @param taxonConceptId The taxonConceptId to set.
	 */
	public void setTaxonConceptId(long taxonConceptId) {
		this.taxonConceptId = taxonConceptId;
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

}