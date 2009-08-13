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
package org.gbif.portal.dto.util;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * Enumerated type for taxonomic ranks. Used for taxonomy
 * related query services that require specifying a taxonomic
 * rank as a search parameter.
 * 
 * @author dmartin
 */
public class TaxonRankType implements Serializable{
	
	private static final long serialVersionUID = -6258074583579311345L;

	/** the name of this rank */
	private String name;
	/** Code for this rank in the enumeration in the TCS schema */
	private String code;
	private int value;

	public static final String KINGDOM_STR = "kingdom";
	public static final String PHYLUM_STR = "phylum";
	public static final String CLASS_STR = "class";
	public static final String ORDER_STR = "order";
	public static final String SUPER_FAMILY_STR = "superfamily";	
	public static final String FAMILY_STR = "family";
	public static final String GENUS_STR = "genus";
	public static final String SPECIES_STR = "species";	
	public static final String SUB_SPECIES_STR = "subspecies";	
	public static final String VARIETY_STR = "variety";	
	public static final String FORM_STR = "form";	
	
	public static final TaxonRankType KINGDOM = new TaxonRankType(KINGDOM_STR, "Kingdom", 1000);
	public static final TaxonRankType PHYLUM = new TaxonRankType(PHYLUM_STR, "Phylum", 2000);
	public static final TaxonRankType CLASS = new TaxonRankType(CLASS_STR, "Class", 3000);
	public static final TaxonRankType ORDER = new TaxonRankType(ORDER_STR, "Order", 4000);
	public static final TaxonRankType SUPER_FAMILY = new TaxonRankType(SUPER_FAMILY_STR, "Superfamily", 4500);
	public static final TaxonRankType FAMILY = new TaxonRankType(FAMILY_STR, "Family", 5000);
	public static final TaxonRankType GENUS = new TaxonRankType(GENUS_STR, "Genus", 6000);		
	public static final TaxonRankType SPECIES = new TaxonRankType(SPECIES_STR, "Species", 7000);
	public static final TaxonRankType SUB_SPECIES = new TaxonRankType(SUB_SPECIES_STR, "Subspecies", 8000);
	public static final TaxonRankType VARIETY = new TaxonRankType(VARIETY_STR, "Variety", 8010);
	public static final TaxonRankType FORM = new TaxonRankType(FORM_STR, "Form", 8020);
	
	/**
	 * Private constructor to keep type enumerated.
	 * 
	 * @param name
	 */
	private TaxonRankType(String name, String code, int value){
		this.name=name;
		this.code=code;
		this.value=value;
	}

	/**
	 * Is the supplied name a recognised rank.
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isRecognisedRank(String name){
		TaxonRankType rankType = getRank(name);
		return rankType != null;
	}
	
	/**
	 * Is the supplied name a recognised rank.
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isRecognisedMajorRank(String name){
		if(StringUtils.isEmpty(name))
			return false;
		if (name.equalsIgnoreCase(KINGDOM.getName()))
			return true;
		if (name.equalsIgnoreCase(PHYLUM.getName()))
			return true;
		if (name.equalsIgnoreCase(CLASS.getName()))
			return true;
		if (name.equalsIgnoreCase(ORDER.getName()))
			return true;
		if (name.equalsIgnoreCase(FAMILY.getName()))
			return true;
		if (name.equalsIgnoreCase(GENUS.getName()))
			return true;
		if (name.equalsIgnoreCase(SPECIES.getName()))
			return true;
		return false;
	}	
	
	/**
	 * Is the supplied name a recognised rank.
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isRecognisedMajorRank(String name, boolean allowSubspecies){
		if(StringUtils.isEmpty(name))
			return false;
		if (allowSubspecies && name.equalsIgnoreCase(SUB_SPECIES.getName()))
			return true;
		return isRecognisedMajorRank(name);
	}	
	
	/**
	 * Is the supplied name a recognised rank.
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isRecognisedMajorRank(String name, boolean allowSubspecies, boolean allowSuperfamily){
		if(StringUtils.isEmpty(name))
			return false;
		if (allowSubspecies && name.equalsIgnoreCase(SUB_SPECIES.getName()))
			return true;
		if(allowSuperfamily && name.equalsIgnoreCase(SUPER_FAMILY.getName()))
			return true;
		return isRecognisedMajorRank(name);
	}		
	
	/**
	 * Gets the enum type for this name.
	 * 
	 * @param name
	 * @return TaxonRankType for this name
	 */
	public static TaxonRankType getRank(String name){
		if (name == null) 
			return null;
		if (name.equalsIgnoreCase(KINGDOM.getName()))
			return KINGDOM;
		if (name.equalsIgnoreCase(PHYLUM.getName()))
			return PHYLUM;
		if (name.equalsIgnoreCase(CLASS.getName()))
			return CLASS;
		if (name.equalsIgnoreCase(ORDER.getName()))
			return ORDER;
		if (name.equalsIgnoreCase(SUPER_FAMILY.getName()))
			return SUPER_FAMILY;		
		if (name.equalsIgnoreCase(FAMILY.getName()))
			return FAMILY;
		if (name.equalsIgnoreCase(GENUS.getName()))
			return GENUS;
		if (name.equalsIgnoreCase(SPECIES.getName()))
			return SPECIES;
		if (name.equalsIgnoreCase(SUB_SPECIES.getName()))
			return SUB_SPECIES;		
		return null;	
	}
	
	/**
	 * Returns true if the supplied rank is genus or lower.
	 * @param rank
	 * @return
	 */
	public static boolean isGenusOrLowerRank(String rank){
		if(StringUtils.isEmpty(rank))
			return false;
		return (rank.equals(TaxonRankType.SPECIES_STR) || 
				rank.equals(TaxonRankType.SUB_SPECIES_STR) || 
				rank.equals(TaxonRankType.GENUS_STR));		
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}	
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}
}