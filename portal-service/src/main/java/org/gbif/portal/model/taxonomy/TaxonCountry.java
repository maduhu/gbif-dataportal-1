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

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A join entity for taxon and country. Represents occurrences of a taxon in a
 * particular country with a count.
 * 
 * @author dmartin
 */
public class TaxonCountry extends HibernateDaoSupport {

	protected TaxonCountryId key;
	
	protected TaxonConceptLite taxonConceptLite;
	
	protected int count;
	
	/**
	 * @return the taxonConceptLite
	 */
	public TaxonConceptLite getTaxonConceptLite() {
		return taxonConceptLite;
	}

	/**
	 * @param taxonConceptLite the taxonConceptLite to set
	 */
	public void setTaxonConceptLite(TaxonConceptLite taxonConceptLite) {
		this.taxonConceptLite = taxonConceptLite;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the key
	 */
	public TaxonCountryId getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(TaxonCountryId key) {
		this.key = key;
	}	
}