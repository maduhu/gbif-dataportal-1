/***************************************************************************
 * Copyright (C) 2006 Global Biodiversity Information Facility Secretariat.
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
package org.gbif.portal.model.taxonomy;

import org.gbif.portal.model.IntegerEnumType;

/**
 * RelationshipType
 *
 * Enumerated representation of the RelationshipType data model concept.
 * http://wiki.gbif.org/dadiwiki/wikka.php?wakka=RelationshipType
 *
 * @author dbarnier
 */
public class RelationshipType extends IntegerEnumType {
	
	static final long serialVersionUID = -3550235810048076915L;
	
	// under no circumstances should these EVER be altered when the DB
	// has data in it! (new ones can be created)
	public static final RelationshipType AMBIGUOUS_SYNONYM = new RelationshipType("ambiguous synonym", 1);
	public static final RelationshipType MISAPPLIED_NAME = new RelationshipType("misapplied name", 2);
	public static final RelationshipType PROVISIONALLY_APPLIED_NAME = new RelationshipType("provisionally applied name", 3);
	public static final RelationshipType SYNONYM = new RelationshipType("synonym", 4);
	
	public RelationshipType() {
		//default constructor, required by hibernate
	}
	
	public RelationshipType(String name, int  value) {
		super(name, value);
	}
}