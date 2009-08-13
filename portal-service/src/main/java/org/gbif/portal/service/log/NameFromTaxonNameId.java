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

import org.gbif.portal.dao.taxonomy.TaxonNameDAO;
import org.gbif.portal.model.taxonomy.TaxonName;

/**
 * Gets the name from the taxon name
 * @author trobertson
 */
public class NameFromTaxonNameId extends AbstractLoggableFromPredicateAndObject {
	/** 
	 * DAO
	 */
	protected TaxonNameDAO taxonNameDAO;
	
	/**
	 * @see org.gbif.portal.service.log.LoggableFromPredicateAndObject#getLoggable(Object, java.lang.Object)
	 */
	public String getLoggable(Object predicate, Object object) {
		TaxonName tn = taxonNameDAO.getTaxonNameFor(parseKey(object));
		if (tn!=null) {
			logger.debug("Found a taxon name to use for logging: " + tn.getCanonical());
			return tn.getCanonical();
		} else {
			return null;
		}
	}

	/**
	 * @return Returns the taxonNameDAO.
	 */
	public TaxonNameDAO getTaxonNameDAO() {
		return taxonNameDAO;
	}

	/**
	 * @param taxonNameDAO The taxonNameDAO to set.
	 */
	public void setTaxonNameDAO(TaxonNameDAO taxonNameDAO) {
		this.taxonNameDAO = taxonNameDAO;
	}
	
}