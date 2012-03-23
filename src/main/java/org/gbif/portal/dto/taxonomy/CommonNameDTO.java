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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * DTO for a common name.
 * 
 * @author Dave Martin
 */
public class CommonNameDTO implements Serializable {

	private static final long serialVersionUID = -1829479679826244134L;

	private String key;
	private String name;
	private String language;
	private String taxonConceptKey;
	private String taxonName;
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}
	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the taxonConceptKey
	 */
	public String getTaxonConceptKey() {
		return taxonConceptKey;
	}
	/**
	 * @param taxonConceptKey the taxonConceptKey to set
	 */
	public void setTaxonConceptKey(String taxonConceptKey) {
		this.taxonConceptKey = taxonConceptKey;
	}
	/**
	 * @return the taxonName
	 */
	public String getTaxonName() {
		return taxonName;
	}
	/**
	 * @param taxonName the taxonName to set
	 */
	public void setTaxonName(String taxonName) {
		this.taxonName = taxonName;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj==null)
			return false;
		if(obj instanceof CommonNameDTO){
			CommonNameDTO commonNameDTO = (CommonNameDTO) obj;
			if(commonNameDTO.getKey().equals(this.getKey()))
			  return true;
			if(commonNameDTO.getName().equals(this.getName())
				&& commonNameDTO.getLanguage().equals(this.getLanguage())
				&& commonNameDTO.getTaxonConceptKey().equals(this.getTaxonConceptKey()))
				return true;
		}
		return false;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}