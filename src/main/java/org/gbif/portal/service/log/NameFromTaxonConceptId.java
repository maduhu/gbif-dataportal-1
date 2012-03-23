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
package org.gbif.portal.service.log;

import org.gbif.portal.dao.taxonomy.TaxonConceptDAO;
import org.gbif.portal.model.taxonomy.TaxonConcept;

/**
 * Gets the name from the taxon concept
 * @author trobertson
 */
public class NameFromTaxonConceptId extends AbstractLoggableFromPredicateAndObject {
	/** 
	 * DAO
	 */
	protected TaxonConceptDAO taxonConceptDAO;
	
	/**
	 * @see org.gbif.portal.service.log.LoggableFromPredicateAndObject#getLoggable(Object, java.lang.Object)
	 */
	public String getLoggable(Object predicate, Object object) {
		TaxonConcept tc = taxonConceptDAO.getTaxonConceptFor(parseKey(object));
		if (tc!=null) {
			logger.debug("Found a taxon concept to use for logging: " + tc.getTaxonName().getCanonical());
			return tc.getTaxonName().getCanonical();
		} else {
			return null;
		}
	}

	/**
	 * @return Returns the taxonConceptDAO.
	 */
	public TaxonConceptDAO getTaxonConceptDAO() {
		return taxonConceptDAO;
	}

	/**
	 * @param taxonConceptDAO The taxonConceptDAO to set.
	 */
	public void setTaxonConceptDAO(TaxonConceptDAO taxonConceptDAO) {
		this.taxonConceptDAO = taxonConceptDAO;
	}
	
}