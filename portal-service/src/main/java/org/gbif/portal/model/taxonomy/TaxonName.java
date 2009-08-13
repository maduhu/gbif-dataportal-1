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

import java.util.HashSet;
import java.util.Set;

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
public class TaxonName extends TaxonNameLite{

	/** The generic */
	protected String generic;
	/** The specific ephithet for this name */
	protected String specificEpithet;
	/** The searchable version of canonical name */
	protected String searchableCanonical;
	/** The author of this name */
	protected String author;
	/** The taxon rank */
	protected TaxonRank taxonRank;
	/**The set of TaxonConcepts this name is associated with */
	protected Set<TaxonConcept> taxonConcepts = new HashSet<TaxonConcept>();
	
	/**
	 * @return the specificEpithet
	 */
	public String getSpecificEpithet() {
		return specificEpithet;
	}
	/**
	 * @param specificEpithet the specificEpithet to set
	 */
	public void setSpecificEpithet(String specificEpithet) {
		this.specificEpithet = specificEpithet;
	}	
	
	/**
     * @hibernate.property
     * 	column="searchable_canonical"
     *  not-null="false"
	 * 
	 * @return the searchableCanonical
	 */
	public String getSearchableCanonical() {
		return searchableCanonical;
	}
	/**
	 * @param searchableCanonical the searchableCanonical to set
	 */
	public void setSearchableCanonical(String searchableCanonical) {
		this.searchableCanonical = searchableCanonical;
	}
	/**
	 * @hibernate.property
	 * 	column="author"
	 * 	not-null="false"
	 */
	public String getAuthor() {
		return this.author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the taxonRank
	 */
	public TaxonRank getTaxonRank() {
		return taxonRank;
	}
	/**
	 * @param taxonRank the taxonRank to set
	 */
	public void setTaxonRank(TaxonRank taxonRank) {
		this.taxonRank = taxonRank;
	}	
	
	/**
	 * @hibernate.set
	 * 	lazy="true"
	 * 	cascade="save-update"
	 * 	inverse="true"
	 * @hibernate.key
	 * 	column="taxon_name_id"
	 * @hibernate.one-to-many
	 * 	class="org.gbif.portal.model.taxonomy.TaxonConcept"
	 */
	public Set<TaxonConcept> getTaxonConcepts() {
		return taxonConcepts;
	}
	protected void setTaxonConcepts(Set<TaxonConcept> taxonConcepts) {
		this.taxonConcepts = taxonConcepts;
	}
	
	/**
	 * @see org.gbif.model.BaseObject#equals()
	 */
	public boolean equals(Object object) {
		if (object instanceof TaxonName) {
			return super.equals(object);
		}
		return false;
	}
	/**
	 * @return the generic
	 */
	public String getGeneric() {
		return generic;
	}
	/**
	 * @param generic the generic to set
	 */
	public void setGeneric(String generic) {
		this.generic = generic;
	}
}