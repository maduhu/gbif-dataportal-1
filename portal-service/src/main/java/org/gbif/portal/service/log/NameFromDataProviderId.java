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

import org.gbif.portal.dao.resources.DataProviderDAO;
import org.gbif.portal.model.resources.DataProvider;

/**
 * Gets the name from the data provider id
 * @author trobertson
 */
public class NameFromDataProviderId extends AbstractLoggableFromPredicateAndObject {
	/** 
	 * DAO
	 */
	protected DataProviderDAO dataProviderDAO;
	
	/**
	 * @see org.gbif.portal.service.log.LoggableFromPredicateAndObject#getLoggable(Object, java.lang.Object)
	 */
	public String getLoggable(Object predicate, Object object) {
		DataProvider dp = dataProviderDAO.getDataProviderFor(parseKey(object));
		if (dp!=null) {
			logger.debug("Found a data provider to use for logging: " + dp.getName());
			return dp.getName();
		} else {
			return null;
		}
	}

	/**
	 * @return Returns the dataProviderDAO.
	 */
	public DataProviderDAO getDataProviderDAO() {
		return dataProviderDAO;
	}

	/**
	 * @param dataProviderDAO The dataProviderDAO to set.
	 */
	public void setDataProviderDAO(DataProviderDAO dataProviderDAO) {
		this.dataProviderDAO = dataProviderDAO;
	}
	
}