/**
 * 
 */
package org.gbif.portal.model;



/**
 * Represents the data captured for a single identifier
 * 
 * @author Donald Hobern
 */
public class RelationshipAssertion extends ModelObject {
	
	/**
	 * Generated
	 */
	private static final long serialVersionUID = -5632429773454713533L;
	
	protected long fromConceptId;
	protected long toConceptId;
    protected long relationshipType;
        
    /**
     * Default
     */
    public RelationshipAssertion() {    	
    }
    
	/**
	 * Convenience
	 */
	public RelationshipAssertion(long fromConceptId, long toConceptId, long relationshipType) {
		this.fromConceptId = fromConceptId;
		this.toConceptId = toConceptId;
		this.relationshipType = relationshipType;
	}

	/**
	 * @return the fromConceptId
	 */
	public long getFromConceptId() {
		return fromConceptId;
	}

	/**
	 * @param fromConceptId the fromConceptId to set
	 */
	public void setFromConceptId(long fromConceptId) {
		this.fromConceptId = fromConceptId;
	}

	/**
	 * @return the relationshipType
	 */
	public long getRelationshipType() {
		return relationshipType;
	}

	/**
	 * @param relationshipType the relationshipType to set
	 */
	public void setRelationshipType(long relationshipType) {
		this.relationshipType = relationshipType;
	}

	/**
	 * @return the toConceptId
	 */
	public long getToConceptId() {
		return toConceptId;
	}

	/**
	 * @param toConceptId the toConceptId to set
	 */
	public void setToConceptId(long toConceptId) {
		this.toConceptId = toConceptId;
	}
}