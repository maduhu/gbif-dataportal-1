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

import org.gbif.portal.model.occurrence.LinkRecord;

/**
 * DAO Implementation for accessing LinkRecord model objects.
 * 
 * @author Donald Hobern
 */
public interface LinkRecordDAO {
	
	/**
	 * Returns the LinkRecords for an occurrence record.
	 * 
	 * @param occurrenceRecordId identifier for an occurrence record
	 * @return List of LinkRecord objects.
	 */
	public List<LinkRecord> getLinkRecordsForOccurrenceRecord(long occurrenceRecordId);

	/**
	 * Returns the LinkRecords for an taxon concept.  
	 * 
	 * TODO handle nub concepts properly
	 * 
	 * @param taxonConceptId identifier for a taxon concept
	 * @return List of LinkRecord objects.
	 */
	public List<LinkRecord> getLinkRecordsForTaxonConcept(long taxonConceptId);
}
