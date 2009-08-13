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

import org.gbif.portal.dto.BaseDTOFactory;
import org.gbif.portal.model.taxonomy.RelationshipAssertion;

/**
 * A DTOFactory for Relationship Assertion objects.
 * 
 * @author Donald Hobern
 */
public class RelationshipAssertionDTOFactory extends BaseDTOFactory {

	/**
	 * @see org.gbif.portal.dto.DTOFactory#createDTO(java.lang.Object)
	 */
	public Object createDTO(Object modelObject) {
		RelationshipAssertion relationshipAssertion = (RelationshipAssertion) modelObject;
		RelationshipAssertionDTO relationshipAssertionDTO = new RelationshipAssertionDTO();
		relationshipAssertionDTO.setFromTaxonConceptKey(String.valueOf(relationshipAssertion.getFromConcept().getId()));
		relationshipAssertionDTO.setFromTaxonName(relationshipAssertion.getFromConcept().getTaxonNameLite().getCanonical());
		relationshipAssertionDTO.setFromTaxonRank(relationshipAssertion.getFromConcept().getTaxonRank().getName());
		relationshipAssertionDTO.setToTaxonConceptKey(String.valueOf(relationshipAssertion.getToConcept().getId()));
		relationshipAssertionDTO.setToTaxonName(relationshipAssertion.getToConcept().getTaxonNameLite().getCanonical());
		relationshipAssertionDTO.setToTaxonRank(relationshipAssertion.getToConcept().getTaxonRank().getName());
		relationshipAssertionDTO.setRelationshipTypeName(relationshipAssertion.getRelationshipType().getName());
		relationshipAssertionDTO.setRelationshipType(relationshipAssertion.getRelationshipType().getValue());
		return relationshipAssertionDTO;
	}
}
