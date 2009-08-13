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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.gbif.portal.model.IntegerEnumType;
import org.hibernate.HibernateException;

/**
 * TaxonRank
 *
 * Enumerated representation of the TaxonRank data model concept.
 * http://wiki.gbif.org/dadiwiki/wikka.php?wakka=TaxonRank
 *
 * @author dbarnier
 */
public class TaxonRank extends IntegerEnumType {
	
	static final long serialVersionUID = -3550235810048076915L;

	private static Map<String, TaxonRank> rankMap = new HashMap<String, TaxonRank>();
	
	// under no circumstances should these EVER be altered when the DB
	// has data in it! (new ones can be created)
	public static final TaxonRank SUPER_KINGDOM = new TaxonRank("superkingdom", 800);
	public static final TaxonRank KINGDOM = new TaxonRank("kingdom", 1000);
	public static final TaxonRank SUB_KINGDOM = new TaxonRank("subkingdom", 1200);
	public static final TaxonRank SUPER_PHYLUM = new TaxonRank("superphylum", 1800);
	public static final TaxonRank PHYLUM = new TaxonRank("phylum", 2000);
	public static final TaxonRank SUB_PHYLUM = new TaxonRank("subphylum", 2200);
	public static final TaxonRank SUPER_CLASS = new TaxonRank("superclass", 2800);
	public static final TaxonRank CLASS = new TaxonRank("class", 3000);
	public static final TaxonRank SUB_CLASS = new TaxonRank("subclass", 3200);
	public static final TaxonRank INFRA_CLASS = new TaxonRank("infraclass", 3350);
	public static final TaxonRank SUPER_ORDER = new TaxonRank("superorder", 3800);
	public static final TaxonRank ORDER = new TaxonRank("order", 4000);
	public static final TaxonRank SUB_ORDER = new TaxonRank("suborder", 4200);
	public static final TaxonRank INFRA_ORDER = new TaxonRank("infraorder", 4350);
	public static final TaxonRank PARV_ORDER = new TaxonRank("parvorder", 4400);
	public static final TaxonRank SUPER_FAMILY = new TaxonRank("superfamily", 4500);
	public static final TaxonRank FAMILY = new TaxonRank("family", 5000);
	public static final TaxonRank SUB_FAMILY = new TaxonRank("subfamily", 5500);
	public static final TaxonRank TRIBE = new TaxonRank("tribe", 5600);
	public static final TaxonRank SUB_TRIBE = new TaxonRank("subtribe", 5700);
	public static final TaxonRank GENUS = new TaxonRank("genus", 6000);
	public static final TaxonRank NOTHOGENUS = new TaxonRank("nothogenus", 6001);
	public static final TaxonRank SUB_GENUS = new TaxonRank("subgenus", 6500);
	public static final TaxonRank SECTION = new TaxonRank("section", 6600);
	public static final TaxonRank SUB_SECTION = new TaxonRank("subsection", 6700);
	public static final TaxonRank SERIES = new TaxonRank("series", 6800);
	public static final TaxonRank SUB_SERIES = new TaxonRank("subseries", 6900);
	public static final TaxonRank SPECIES_GROUP = new TaxonRank("species group", 6950);
	public static final TaxonRank SPECIES_SUBGROUP = new TaxonRank("species subgroup", 6975);
	public static final TaxonRank SPECIES = new TaxonRank("species", 7000);
	public static final TaxonRank NOTHOSPECIES = new TaxonRank("nothospecies", 7001);
	public static final TaxonRank SUBSPECIES = new TaxonRank("subspecies", 8000);
	public static final TaxonRank NOTHOSUBSPECIES = new TaxonRank("nothosubspecies", 8001);
	public static final TaxonRank VARIETY = new TaxonRank("variety", 8010);
	public static final TaxonRank NOTHOVARIETY = new TaxonRank("nothovariety", 8011);
	public static final TaxonRank FORM = new TaxonRank("form", 8020);
	public static final TaxonRank NOTHOFORM = new TaxonRank("nothoform", 8021);
	public static final TaxonRank BIOVAR = new TaxonRank("biovar", 8030);
	public static final TaxonRank SEROVAR = new TaxonRank("serovar", 8040);
	public static final TaxonRank CULTIVAR = new TaxonRank("cultivar", 8050);	
	public static final TaxonRank PATHOVAR = new TaxonRank("pathovar", 8080);	
	
	// THESE AREN'T RANKS AS SUCH... WHAT DO WE DO?
	public static final TaxonRank INFRASPECIFIC = new TaxonRank("infraspecific", 8090);
	public static final TaxonRank ABERRATION = new TaxonRank("aberration", 8100);	
	public static final TaxonRank MUTATION = new TaxonRank("mutation", 8110);
	public static final TaxonRank RACE = new TaxonRank("race", 8120);
	public static final TaxonRank CONFERSUBSPECIES = new TaxonRank("confersubspecies", 8130);
	public static final TaxonRank FORMASPECIALIS = new TaxonRank("formaspecialis", 8140);
	public static final TaxonRank HYBRID = new TaxonRank("hybrid", 8150);	
	
	public static final TaxonRank UNKNOWN = new TaxonRank("unranked", 0);
	
	public TaxonRank() {
		//default constructor, required by hibernate
	}
	
	private TaxonRank(String name, int value) {
		super(name, Integer.valueOf(value));
		
		rankMap.put(name, this);
	}
	
	/**
	 * Utility method to return the enumerated instance for the specified name
	 * @param name The enumerated name value
	 * @return The enumerated instance
	 */
	public static final TaxonRank getTaxonRank(String name) {
		TaxonRank rank = null;
		if (name != null) {
			rank = rankMap.get(name);
		}
		if (rank == null) {
			rank = UNKNOWN;
		}
		return rank;
	}
	
	/**
	 * This override is to ensure that we can handle unranked taxa in the middle of the tree without adding them all to this type.
	 * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)
	 * @override
	 */
	public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
	throws HibernateException, SQLException {
		Object o = super.nullSafeGet(rs, names, owner);
		if (o == null) {
			Integer enumCode = (Integer)getNullableType().nullSafeGet(rs, names[0]);
			if (enumCode != null) {
				o = new TaxonRank("Unranked taxon", enumCode);
			}
		}
		return o;
	}
}