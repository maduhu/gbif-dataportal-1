/**
 * 
 */
package org.gbif.portal.model;

import java.util.Date;


/**
 * Represents the data captured for a single identifier
 * 
 * @author Donald Hobern
 */
public class RemoteConcept extends ModelObject {
	
	/**
	 * Generated
	 */
	private static final long serialVersionUID = -7497166409664058449L;
	
	protected long taxonConceptId;
    protected long idType;
	protected String remoteId;
	protected Date modified;
        
    /**
     * Default
     */
    public RemoteConcept() {    	
    }
    
	/**
	 * Convenience
	 */
	public RemoteConcept(long taxonConceptId, long idType, String remoteId) {
		this.taxonConceptId = taxonConceptId;
		this.idType = idType;
		this.remoteId = remoteId;
	}

	/**
	 * Convenience
	 */
	public RemoteConcept(long id, long taxonConceptId, long idType, String remoteId, Date modified) {
		this.id = id;
		this.taxonConceptId = taxonConceptId;
		this.idType = idType;
		this.remoteId = remoteId;
		this.modified = modified;

	}

	/**
	 * @return the idType
	 */
	public long getIdType() {
		return idType;
	}

	/**
	 * @param idType the idType to set
	 */
	public void setIdType(long idType) {
		this.idType = idType;
	}

	/**
	 * @return the remoteId
	 */
	public String getRemoteId() {
		return remoteId;
	}

	/**
	 * @param remoteId the remoteId to set
	 */
	public void setRemoteId(String remoteId) {
		this.remoteId = remoteId;
	}

	/**
	 * @return the taxonConceptId
	 */
	public long getTaxonConceptId() {
		return taxonConceptId;
	}

	/**
	 * @param taxonConceptId the taxonConceptId to set
	 */
	public void setTaxonConceptId(long taxonConceptId) {
		this.taxonConceptId = taxonConceptId;
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

}