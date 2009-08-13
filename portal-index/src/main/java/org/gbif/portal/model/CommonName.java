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
package org.gbif.portal.model;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * A bean to represent a common name in whole or part
 * @author Donald Hobern
 */
public class CommonName implements Serializable{
	/**
	 * Generated 
	 */
	private static final long serialVersionUID = -3005075738383480628L;
	
	protected Long id;
	protected Long taxonConceptId;
	protected String name;
	protected String isoLanguageCode;
	protected String language;
	
	public CommonName() {		
	}
	
	public CommonName(Long taxonConceptId, String name, String language) {
		this.taxonConceptId = taxonConceptId;
		this.name = name;
		this.language = language;
	}
	
	public CommonName(long id, long taxonConceptId, String name, String isoLanguageCode, String language) {
		this.id = id;
		this.taxonConceptId = taxonConceptId;
		this.name = name;
		this.isoLanguageCode = isoLanguageCode;
		this.language = language;
	}

	/**
	 * @return A full version of the common name for logging
	 */
	public String toFullString() {
		 return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
	       append("taxonConceptId", taxonConceptId).
	       append("name", name).
	       append("isoLanguageCode", isoLanguageCode).
	       append("language", language).
	       toString();	
	}
	
	/**
	 * @return A short version of the common name (Name, Language)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		 return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
	       append("name", name).
	       append("language", language).
	       toString();	
	}
	
	/**
	 * CommonName is considered equal if the taxonConceptId, name and language are the same
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object target) {
		if (target instanceof CommonName) {
			CommonName targetName = (CommonName) target;
			if (StringUtils.equals(getName(), targetName.getName())
				&& StringUtils.equals(getLanguage(), targetName.getLanguage())
				&& getTaxonConceptId() == targetName.getTaxonConceptId()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the isoLanguageCode
	 */
	public String getIsoLanguageCode() {
		return isoLanguageCode;
	}

	/**
	 * @param isoLanguageCode the isoLanguageCode to set
	 */
	public void setIsoLanguageCode(String isoLanguageCode) {
		this.isoLanguageCode = isoLanguageCode;
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
	 * @return the taxonConceptId
	 */
	public Long getTaxonConceptId() {
		return taxonConceptId;
	}

	/**
	 * @param taxonConceptId the taxonConceptId to set
	 */
	public void setTaxonConceptId(Long taxonConceptId) {
		this.taxonConceptId = taxonConceptId;
	}

}