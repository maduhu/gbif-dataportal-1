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

package org.gbif.portal.dto.taxonomy;

import java.io.Serializable;

/**
 * DTO for a relationship assertion.
 * 
 * @author Donald Hobern
 */
public class RelationshipAssertionDTO implements Serializable {

	public static final int RELATIONSHIP_TYPE_AMBIGUOUS_SYNONYM = 1;
	public static final int RELATIONSHIP_TYPE_MISAPPLIED_NAME = 2;
	public static final int RELATIONSHIP_TYPE_PROVISIONALLY_APPLIED_NAME = 3;
	public static final int RELATIONSHIP_TYPE_SYNONYM = 4;
	
	/**
	 * Generated
	 */
	private static final long serialVersionUID = 1114429730640146602L;
	
	private String fromTaxonConceptKey;
	private String fromTaxonName;
	private String fromTaxonRank;
	private String toTaxonConceptKey;
	private String toTaxonName;
	private String toTaxonRank;
	private String relationshipTypeName;
	private int relationshipType;

	/**
	 * @return the fromTaxonConceptKey
	 */
	public String getFromTaxonConceptKey() {
		return fromTaxonConceptKey;
	}

	/**
	 * @param fromTaxonConceptKey the fromTaxonConceptKey to set
	 */
	public void setFromTaxonConceptKey(String fromTaxonConceptKey) {
		this.fromTaxonConceptKey = fromTaxonConceptKey;
	}

	/**
	 * @return the fromTaxonName
	 */
	public String getFromTaxonName() {
		return fromTaxonName;
	}

	/**
	 * @param fromTaxonName the fromTaxonName to set
	 */
	public void setFromTaxonName(String fromTaxonName) {
		this.fromTaxonName = fromTaxonName;
	}

	/**
	 * @return the relationshipType
	 */
	public int getRelationshipType() {
		return relationshipType;
	}

	/**
	 * @param relationshipType the relationshipType to set
	 */
	public void setRelationshipType(int relationshipType) {
		this.relationshipType = relationshipType;
	}

	/**
	 * @return the toTaxonConceptKey
	 */
	public String getToTaxonConceptKey() {
		return toTaxonConceptKey;
	}

	/**
	 * @param toTaxonConceptKey the toTaxonConceptKey to set
	 */
	public void setToTaxonConceptKey(String toTaxonConceptKey) {
		this.toTaxonConceptKey = toTaxonConceptKey;
	}

	/**
	 * @return the toTaxonName
	 */
	public String getToTaxonName() {
		return toTaxonName;
	}

	/**
	 * @param toTaxonName the toTaxonName to set
	 */
	public void setToTaxonName(String toTaxonName) {
		this.toTaxonName = toTaxonName;
	}

	/**
	 * @return the relationshipTypeName
	 */
	public String getRelationshipTypeName() {
		return relationshipTypeName;
	}

	/**
	 * @param relationshipTypeName the relationshipTypeName to set
	 */
	public void setRelationshipTypeName(String relationshipTypeName) {
		this.relationshipTypeName = relationshipTypeName;
	}

	/**
   * @return the fromTaxonRank
   */
  public String getFromTaxonRank() {
  	return fromTaxonRank;
  }

	/**
   * @param fromTaxonRank the fromTaxonRank to set
   */
  public void setFromTaxonRank(String fromTaxonRank) {
  	this.fromTaxonRank = fromTaxonRank;
  }

	/**
   * @return the toTaxonRank
   */
  public String getToTaxonRank() {
  	return toTaxonRank;
  }

	/**
   * @param toTaxonRank the toTaxonRank to set
   */
  public void setToTaxonRank(String toTaxonRank) {
  	this.toTaxonRank = toTaxonRank;
  }
}