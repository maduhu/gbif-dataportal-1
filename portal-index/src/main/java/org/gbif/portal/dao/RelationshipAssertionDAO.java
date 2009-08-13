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
package org.gbif.portal.dao;

import java.util.List;

import org.gbif.portal.model.RelationshipAssertion;


/**
 * The RA DAO
 * @author trobertson
 */
public interface RelationshipAssertionDAO {
	public static final int TYPE_AMBIGUOUS_SYNONYM = 1;
	public static final int TYPE_MISAPPLIED_NAME = 2;
	public static final int TYPE_PROVISIONALLY_APPLIED_NAME = 3;
	public static final int TYPE_SYNONYM = 4;
	
	
	/**
	 * Creates the RA
	 * @param fromConcept
	 * @param toConcept
	 * @param type
	 */
	public void create(long fromConcept, long toConcept, int type);

	/**
	 * @param id
	 * @return
	 */
	public List<RelationshipAssertion> getRelationshipAssertionsForFromConcept(Long id);
}
