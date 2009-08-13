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
package org.gbif.portal.harvest.taxonomy;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.model.TaxonName;

/**
 * A class that is loaded with a RegEx and will create a taxon name based on the configured group id's
 * to extract from the regex.
 * 
 * Prenormalisation may occur if set, but defaults to no denormalisation.
 *  
 * @author trobertson
 */
public class RegularExpressionToTaxonName {
	/**
	 * Logger
	 */
	protected Log logger = LogFactory.getLog(RegularExpressionToTaxonName.class);
	
	/**
	 * The Reg Expression to match with
	 */
	protected String regex;
	
	/**
	 * The compiled pattern for the regex
	 */
	protected Pattern pattern;	
	
	/**
	 * If the name should be turned into lower case prior to reg ex evaluation
	 */
	public static final String PRENORMALISE_LOWER_CASE = "LOWER_CASE";
	
	/**
	 * If the name should be turned into Sentence case prior to reg ex evaluation
	 */
	public static final String PRENORMALISE_SENTENCED_CASE = "SENTENCE_CASE";
	
	/**
	 * May be set to one of the types to prenormalise strings
	 */
	protected String prenormalisation;
	
	/**
	 * The groups to extract from the RegEx and set
	 */
	protected int supraGenericGroup = -1;
	protected int genericGroup = -1;
	protected int infraGenericGroup = -1;
	protected int specificGroup = -1;
	protected int infraSpecificGroup = -1;
	protected int infraSpecificMarkerGroup = -1;
	protected int authorGroup = -1;
	
	/**
	 * Canonical can be made up of a space seperated concatination of groups
	 */
	protected List<Integer> canonicalGroups;	
	
	/**
	 * If set, will be set in the taxon name 
	 */
	protected int type = -1;
	protected int rank = -1;
	
	/**
	 * Can be used instead of a group for the canonical
	 * The canonical group overrides this if it is set
	 */
	protected boolean setCanonical = true;
	
	/**
	 * Parses the name based on the configuration
	 * @param name To parse
	 * @param parsedName to set the atomised parts on
	 * @return boolean true if parsed or false if the reg ex doesn't match it
	 */
	public boolean parse(String name, TaxonName parsedName) {
		return parse(name, parsedName, null);
	}
	
	/**
	 * Parses the name based on the configuration
	 * @param name To parse
	 * @param parsedName to set the atomised parts on
	 * @param suppliedRank rank supplied by caller
	 * @return boolean true if parsed or false if the reg ex doesn't match it
	 */
	public boolean parse(String name, TaxonName parsedName, Integer suppliedRank) {

		name = prepareName(name);
		
		// todo tidy...
		if (name == null) {
			return false;
		}

		Matcher matcher = pattern.matcher(name);
		if (matcher.matches()) {
			logger.debug("Matched [" + name + "] using pattern [" + pattern.pattern() + "]");
			try {
				if (setCanonical) {
					parsedName.setCanonical(name);
				}
				if (supraGenericGroup > 0) {
					parsedName.setSupraGeneric(matcher.group(supraGenericGroup));
				}
				if (genericGroup > 0) {
					parsedName.setGeneric(matcher.group(genericGroup));
				}
				if (infraGenericGroup > 0) {
					parsedName.setInfraGeneric(matcher.group(infraGenericGroup));
				}
				if (specificGroup > 0) {
					parsedName.setSpecific(matcher.group(specificGroup));
				}
				if (infraSpecificGroup > 0) {
					parsedName.setInfraSpecific(matcher.group(infraSpecificGroup));
				}
				if (infraSpecificMarkerGroup > 0) {
					parsedName.setInfraSpecificMarker(matcher.group(infraSpecificMarkerGroup));
				}
				if (authorGroup > 0) {
					parsedName.setAuthor(matcher.group(authorGroup));
				}
				if (canonicalGroups != null) {
					StringBuffer sb = new StringBuffer(" ");
					for (Integer i : canonicalGroups) {
						sb.append(matcher.group(i));
						sb.append(" ");
					}
					parsedName.setCanonical(sb.toString().trim());
				}
				if (type > 0) {
					parsedName.setType(type);
				}
				if (suppliedRank != null) {
					parsedName.setRank(suppliedRank);
				} else if (rank > 0) {
					parsedName.setRank(rank);
				}
				
				return true;
				
			} catch (RuntimeException e) {
				logger.error(
						"Error parsing name from [" + name + "] using regex["
								+ getRegex() + "] - returning false", e);
			}
		} else {
			logger.debug("Did not match [" + name + "] using pattern [" + pattern.pattern() + "]");
		}
		return false;
	}

	protected String prepareName(String name) {
		name = StringUtils.trimToNull(name);
		
		if (prenormalisation != null && name != null) {
			String oldName = name;
			if (prenormalisation.equals(RegularExpressionToTaxonName.PRENORMALISE_LOWER_CASE)) {
				name = name.toLowerCase();
			} else if (prenormalisation.equals(RegularExpressionToTaxonName.PRENORMALISE_SENTENCED_CASE)) {
				name = StringUtils.capitalize(name.toLowerCase());
			}
			logger.debug("prenormalisation [" + prenormalisation + "] of " + oldName + ": " + name);			
		}
		
		if (name != null) {
			// sometimes there are incorrectly: 
			//   Genus species(Author1) Author2
			// lets add a space before all "(" and after ")" 
			//   (doubles will immediately be stripped after)
			name = name.replaceAll("\\(", " \\(");
			name = name.replaceAll("\\)", "\\) ");
			
			// strip double ups
			name = name.replaceAll(" +", " ");
			name=name.trim();
			
			
			// strip diareses
			name = name.replace("ä", "a");
			name = name.replace("ë", "e");
			name = name.replace("ï", "i");
			name = name.replace("ö", "o");
			name = name.replace("ü", "u");
			name = name.replace("ÿ", "y");
			
			name = name.replaceAll(" x ", " � ");
			name = name.replaceAll(" X ", " � ");
			name = name.replaceAll(" \\* ", " � ");
		}
		
		return name;
	}

	/**
	 * @return Returns the authorGroup.
	 */
	public int getAuthorGroup() {
		return authorGroup;
	}

	/**
	 * @param authorGroup The authorGroup to set.
	 */
	public void setAuthorGroup(int authorGroup) {
		this.authorGroup = authorGroup;
	}

	/**
	 * @return Returns the genericGroup.
	 */
	public int getGenericGroup() {
		return genericGroup;
	}

	/**
	 * @param genericGroup The genericGroup to set.
	 */
	public void setGenericGroup(int genericGroup) {
		this.genericGroup = genericGroup;
	}

	/**
	 * @return Returns the rank.
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * @param rank The rank to set.
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * @return Returns the infraGenericGroup.
	 */
	public int getInfraGenericGroup() {
		return infraGenericGroup;
	}

	/**
	 * @param infraGenericGroup The infraGenericGroup to set.
	 */
	public void setInfraGenericGroup(int infraGenericGroup) {
		this.infraGenericGroup = infraGenericGroup;
	}

	/**
	 * @return Returns the infraSpecificGroup.
	 */
	public int getInfraSpecificGroup() {
		return infraSpecificGroup;
	}

	/**
	 * @param infraSpecificGroup The infraSpecificGroup to set.
	 */
	public void setInfraSpecificGroup(int infraSpecificGroup) {
		this.infraSpecificGroup = infraSpecificGroup;
	}

	/**
	 * @return Returns the infraSpecificMarkerGroup.
	 */
	public int getInfraSpecificMarkerGroup() {
		return infraSpecificMarkerGroup;
	}

	/**
	 * @param infraSpecificMarkerGroup The infraSpecificMarkerGroup to set.
	 */
	public void setInfraSpecificMarkerGroup(int infraSpecificMarkerGroup) {
		this.infraSpecificMarkerGroup = infraSpecificMarkerGroup;
	}

	/**
	 * @return Returns the pattern.
	 */
	public Pattern getPattern() {
		return pattern;
	}

	/**
	 * @param pattern The pattern to set.
	 */
	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	/**
	 * @return Returns the regex.
	 */
	public String getRegex() {
		return regex;
	}

	/**
	 * @param regex The regex to set.
	 */
	public void setRegex(String regex) {
		this.regex = regex;
		pattern = Pattern.compile(regex);
	}

	/**
	 * @return Returns the specificGroup.
	 */
	public int getSpecificGroup() {
		return specificGroup;
	}

	/**
	 * @param specificGroup The specificGroup to set.
	 */
	public void setSpecificGroup(int specificGroup) {
		this.specificGroup = specificGroup;
	}

	/**
	 * @return Returns the supraGenericGroup.
	 */
	public int getSupraGenericGroup() {
		return supraGenericGroup;
	}

	/**
	 * @param supraGenericGroup The supraGenericGroup to set.
	 */
	public void setSupraGenericGroup(int supraGenericGroup) {
		this.supraGenericGroup = supraGenericGroup;
	}

	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return Returns the setCanonical.
	 */
	public boolean isSetCanonical() {
		return setCanonical;
	}

	/**
	 * @param setCanonical The setCanonical to set.
	 */
	public void setSetCanonical(boolean setCanonical) {
		this.setCanonical = setCanonical;
	}

	/**
	 * @return Returns the prenormalisation.
	 */
	public String getPrenormalisation() {
		return prenormalisation;
	}

	/**
	 * @param prenormalisation The prenormalisation to set.
	 */
	public void setPrenormalisation(String prenormalisation) {
		this.prenormalisation = prenormalisation;
	}

	/**
	 * @return Returns the canonicalGroups.
	 */
	public List<Integer> getCanonicalGroups() {
		return canonicalGroups;
	}

	/**
	 * @param canonicalGroups The canonicalGroups to set.
	 */
	public void setCanonicalGroups(List<Integer> canonicalGroups) {
		this.canonicalGroups = canonicalGroups;
	}
}
