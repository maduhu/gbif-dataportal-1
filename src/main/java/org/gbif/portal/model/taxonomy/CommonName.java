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

package org.gbif.portal.model.taxonomy;

import org.gbif.portal.model.BaseObject;

/**
 * A Common Name associated with a taxonomic concept.
 * 
 * @author dmartin
 */
public class CommonName extends BaseObject {
	
	/** The taxon concept that this is joined to  */
	protected TaxonConcept taxonConcept;
	/** The taxon concept that this is joined to  */
	protected long taxonConceptId;
	/** The actual name */
	protected String name;
	/** The ISO Country code */
	protected String isoLanguageCode;
	/** The language */
	protected String language;
	
	/**
	 * @return the isoCountryCode
	 */
	public String getIsoLanguageCode() {
		return isoLanguageCode;
	}
	/**
	 * @param isoCountryCode the isoCountryCode to set
	 */
	public void setIsoLanguageCode(String isoCountryCode) {
		this.isoLanguageCode = isoCountryCode;
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
	 * @return the taxonConcept
	 */
	public TaxonConcept getTaxonConcept() {
		return taxonConcept;
	}
	/**
	 * @param taxonConcept the taxonConcept to set
	 */
	public void setTaxonConcept(TaxonConcept taxonConcept) {
		this.taxonConcept = taxonConcept;
	}
	/**
	 * @return the taxonConceptId
	 */
	public long getTaxonConceptId() {
		return taxonConceptId;
	}
	/**
	 * @param taxonConceptId the taxonConceptId to set
	 */
	public void setTaxonConceptId(long taxonConceptId) {
		this.taxonConceptId = taxonConceptId;
	}
}