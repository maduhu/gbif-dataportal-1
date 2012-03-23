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

import java.util.List;

import org.gbif.portal.model.IdentifierRecord;

/**
 * The Identifier Record DAO
 * @author Donald Hobern
 */
public interface IdentifierRecordDAO {
	/**
	 * Creates the record
	 * @param IdentifierRecord To create
	 * @return The id of the created record
	 */
	public long create(IdentifierRecord identifierRecord);
	
	/**
	 * Creates the record if the given record has no ID, otherwise will update
	 * @param IdentifierRecord To create or update
	 * @return The id of the created record
	 */
	public long updateOrCreate(IdentifierRecord identifierRecord);
	
	/**
	 * Deletes the record
	 * @param IdentifierRecord To delete
	 */
	public void delete(IdentifierRecord identifierRecord);
	
	/**
	 * Gets the records associated with the specified OccurrenceRecord
	 * @param occurrenceId identifier of OccurrenceRecord
	 * @return The records or an empty list
	 */
	public List<IdentifierRecord> findByOccurrenceId(long occurrenceId);
}
