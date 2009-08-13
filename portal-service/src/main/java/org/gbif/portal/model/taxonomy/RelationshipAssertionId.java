/**
 * 
 */
package org.gbif.portal.model.taxonomy;

import java.io.Serializable;

/**
 * This is required as there is currently no id column on the relationship_assertion table.
 * 
 * @author dmartin
 */
public class RelationshipAssertionId implements Serializable {
	
	private static final long serialVersionUID = -3986099775017187002L;

	protected int toConceptId;
	protected int fromConceptId;
	protected int relationshipTypeValue;
	
	/**
	 * @return the fromConceptId
	 */
	public int getFromConceptId() {
		return fromConceptId;
	}
	/**
	 * @param fromConceptId the fromConceptId to set
	 */
	public void setFromConceptId(int fromConceptId) {
		this.fromConceptId = fromConceptId;
	}
	/**
	 * @return the relationshipTypeValue
	 */
	public int getRelationshipTypeValue() {
		return relationshipTypeValue;
	}
	/**
	 * @param relationshipTypeValue the relationshipTypeValue to set
	 */
	public void setRelationshipTypeValue(int relationshipTypeValue) {
		this.relationshipTypeValue = relationshipTypeValue;
	}
	/**
	 * @return the toConceptId
	 */
	public int getToConceptId() {
		return toConceptId;
	}
	/**
	 * @param toConceptId the toConceptId to set
	 */
	public void setToConceptId(int toConceptId) {
		this.toConceptId = toConceptId;
	}
}