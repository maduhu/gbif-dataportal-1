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
package org.gbif.portal.dao.occurrence;

import java.util.List;

import org.gbif.portal.model.occurrence.RawOccurrenceRecord;

/**
 * DAO Implementation for accessing OccurrenceRecord model objects.
 * 
 * @author dmartin
 */
public interface RawOccurrenceRecordDAO {
	
	/**
	 * Returns the RawOccurrenceRecord with the specified id value.
	 * 
	 * @param rawOccurrenceRecordId The id of the raw occurrence record
	 * @return The RawOccurrenceRecord object for this id.
	 */
	public RawOccurrenceRecord getRawOccurrenceRecordFor(long rawOccurrenceRecordId);
	
	/**
	 * Find Raw occurrence records.
	 * 
	 * @param dataResourceId
	 * @param catalogueNumber
	 * @return
	 */
	public List<RawOccurrenceRecord> findRawOccurrenceRecord(long dataResourceId, String catalogueNumber, int startIndex, int maxResults);
}