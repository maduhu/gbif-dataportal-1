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

import java.util.List;

import org.gbif.portal.dao.resources.DataResourceDAO;
import org.gbif.portal.model.resources.DataResource;

/**
 * Gets the name from the data resource id
 * @author trobertson
 */
public class NameFromDataResourceId extends AbstractLoggableFromPredicateAndObject {
	/** 
	 * DAO
	 */
	protected DataResourceDAO dataResourceDAO;
	
	/**
	 * @see org.gbif.portal.service.log.LoggableFromPredicateAndObject#getLoggable(Object, java.lang.Object)
	 */
	public String getLoggable(Object predicate, Object object) {
		// Some preprocessors turn queries into lists of IDs and a select ... in()
		if (object instanceof List) {
			StringBuffer sb = new StringBuffer();
			for (Object key : ((List)object)) {
				String loggable = loggable(key);
				sb.append("("+loggable+")");				
			}
			return sb.toString();
		} else {
			return loggable(object);
		}		
	}

	protected String loggable(Object object) {
		DataResource dr = dataResourceDAO.getDataResourceFor(parseKey(object));
		if (dr!=null) {
			logger.debug("Found a data resource to use for logging: " + dr.getName());
			return dr.getName();
		} else {
			return null;
		}
	}

	/**
	 * @return Returns the dataResourceDAO.
	 */
	public DataResourceDAO getDataResourceDAO() {
		return dataResourceDAO;
	}

	/**
	 * @param dataResourceDAO The dataResourceDAO to set.
	 */
	public void setDataResourceDAO(DataResourceDAO dataResourceDAO) {
		this.dataResourceDAO = dataResourceDAO;
	}
	
}