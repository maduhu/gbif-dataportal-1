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

import org.gbif.portal.model.BaseObject;

/**
 * TaxonName Model Object. Encapsulates a Scientific Name which is
 * associated with one or more taxonomic concepts. 
 *
 * Object representation of the TaxonName data model concept.
 * http://wiki.gbif.org/dadiwiki/wikka.php?wakka=TaxonName
 *
 * @author dbarnier
 * @author dmartin
 * 
 * @see TaxonConcept
 *
 * @hibernate.class
 * table="taxon_name"
 */
public class TaxonNameLite extends BaseObject {

	/** The canonical name **/
	protected String canonical;
	
	/**
     * @hibernate.property
     * 	column="canonical"
     *  not-null="false"
	 */
    public String getCanonical() {
        return this.canonical;
    }
	public void setCanonical(String canonical) {
		this.canonical = canonical;
	}

	/**
	 * @see org.gbif.model.BaseObject#equals()
	 */
	public boolean equals(Object object) {
		if (object instanceof TaxonNameLite) {
			return super.equals(object);
		}
		return false;
	}
}