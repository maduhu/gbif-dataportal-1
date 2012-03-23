package org.gbif.portal.model.taxonomy;

/**
 * Relationship Assertion Model Object
 * 
 * @author dmartin
 */
public class RelationshipAssertion {

	/**
	 * The composite identifier for the RelationshipAssertion
	 */
	protected RelationshipAssertionId identifer;
	protected TaxonConceptLite fromConcept;
	protected TaxonConceptLite toConcept;
	protected RelationshipType relationshipType;
	
	/**
	 * @return the fromConcept
	 */
	public TaxonConceptLite getFromConcept() {
		return fromConcept;
	}
	/**
	 * @param fromConcept the fromConcept to set
	 */
	public void setFromConcept(TaxonConceptLite fromConcept) {
		this.fromConcept = fromConcept;
	}
	/**
	 * @return the relationshipType
	 */
	public RelationshipType getRelationshipType() {
		return relationshipType;
	}
	/**
	 * @param relationshipType the relationshipType to set
	 */
	public void setRelationshipType(RelationshipType relationshipType) {
		this.relationshipType = relationshipType;
	}
	/**
	 * @return the toConcept
	 */
	public TaxonConceptLite getToConcept() {
		return toConcept;
	}
	/**
	 * @param toConcept the toConcept to set
	 */
	public void setToConcept(TaxonConceptLite toConcept) {
		this.toConcept = toConcept;
	}
	/**
	 * @return the identifer
	 */
	public RelationshipAssertionId getIdentifer() {
		return identifer;
	}
	/**
	 * @param identifer the identifer to set
	 */
	public void setIdentifer(RelationshipAssertionId identifer) {
		this.identifer = identifer;
	}
}