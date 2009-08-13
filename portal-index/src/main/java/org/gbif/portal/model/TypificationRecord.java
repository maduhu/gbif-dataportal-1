/**
 * 
 */
package org.gbif.portal.model;


/**
 * Represents the data captured for a single typification
 * 
 * @author Donald Hobern
 */
public class TypificationRecord extends ModelObject {
	
	/**
	 * Generated 
	 */
	private static final long serialVersionUID = -1295644535147103982L;
	
    protected long dataResourceId;
    protected long occurrenceId;
    protected long taxonNameId;
    protected String scientificName;
    protected String publication;
    protected String typeStatus;
    protected String notes;
        
    /**
     * Default
     */
    public TypificationRecord() {    	
    }
    
	/**
	 * Convenience
	 */
	public TypificationRecord(long dataResourceId, long occurrenceId, long taxonNameId, String scientificName, String publication, String typeStatus, String notes) {
		this.dataResourceId = dataResourceId;
		this.occurrenceId = occurrenceId;
		this.taxonNameId = taxonNameId;
		this.scientificName = scientificName;
		this.publication = publication;
		this.typeStatus = typeStatus;
		this.notes = notes;
	}
	/**
	 * Convenience
	 */
	public TypificationRecord(long id, long dataResourceId, long occurrenceId, long taxonNameId, String scientificName, String publication, String typeStatus, String notes) {
		this.id = id;
		this.dataResourceId = dataResourceId;
		this.occurrenceId = occurrenceId;
		this.taxonNameId = taxonNameId;
		this.scientificName = scientificName;
		this.publication = publication;
		this.typeStatus = typeStatus;
		this.notes = notes;
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
	 * @return Returns the notes.
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes The notes to set.
	 */
	public void setNotes(String notes) {
		this.notes = notes;
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
	 * @return Returns the publication.
	 */
	public String getPublication() {
		return publication;
	}

	/**
	 * @param publication The publication to set.
	 */
	public void setPublication(String publication) {
		this.publication = publication;
	}

	/**
	 * @return Returns the scientificName.
	 */
	public String getScientificName() {
		return scientificName;
	}

	/**
	 * @param scientificName The scientificName to set.
	 */
	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}

	/**
	 * @return Returns the taxonNameId.
	 */
	public long getTaxonNameId() {
		return taxonNameId;
	}

	/**
	 * @param taxonNameId The taxonNameId to set.
	 */
	public void setTaxonNameId(long taxonNameId) {
		this.taxonNameId = taxonNameId;
	}

	/**
	 * @return Returns the typeStatus.
	 */
	public String getTypeStatus() {
		return typeStatus;
	}

	/**
	 * @param typeStatus The typeStatus to set.
	 */
	public void setTypeStatus(String typeStatus) {
		this.typeStatus = typeStatus;
	}
}