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
package org.gbif.portal.dao;

import org.gbif.portal.model.OccurrenceRecord;

/**
 * The Occurrence Record DAO
 * @author trobertson
 */
public interface OccurrenceRecordDAO {
	/**
	 * Creates the record
	 * @param OccurrenceRecord To create
	 * @return The id of the created record
	 */
	public long create(OccurrenceRecord occurrenceRecord);
	
	/**
	 * Updates
	 * @param OccurrenceRecord To update
	 * @return The id of the created record
	 */
	public long update(OccurrenceRecord occurrenceRecord);
	
	/**
	 * Gets the unique record for the occurrence id
	 * @param id uniquely identifying the record
	 * @return The occurrence record
	 */
	public OccurrenceRecord getById(long id);
	
	/**
	 * gets the count by the nubId
	 */
	public int countByNubId(long nubId);
	
	/**
	 * gets the count by the nubId and only the geospatial occurrences
	 */
	public int countByNubIdGeospatial(long nubId);
	
	
}
