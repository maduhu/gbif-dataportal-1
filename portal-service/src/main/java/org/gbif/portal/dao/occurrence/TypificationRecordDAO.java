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

import org.gbif.portal.model.occurrence.TypeStatus;
import org.gbif.portal.model.occurrence.TypificationRecord;

/**
 * DAO Implementation for accessing TypificationRecord model objects.
 * 
 * @author Donald Hobern
 */
public interface TypificationRecordDAO {
	/**
	 * Returns the TypificationRecords for an occurrence record.
	 * 
	 * @param occurrenceRecordId identifier for an occurrence record
	 * @return List of TypificationRecord objects.
	 */
	public List<TypificationRecord> getTypificationRecordsForOccurrenceRecord(long occurrenceRecordId);

	/**
	 * Returns the TypificationRecords for an occurrence record.
	 * 
	 * @param occurrenceRecordId identifier for an occurrence record
	 * @return List of TypificationRecord objects.
	 */
	public List<TypeStatus> getTypeStatusForOccurrenceRecords(List<Long> occurrenceRecordIds);
	
	/**
	 * Returns the TypificationRecords for an taxon name.  
	 * 
	 * @param taxonNameId identifier for a taxon name
	 * @return List of TypificationRecord objects.
	 */
	public List<TypificationRecord> getTypificationRecordsForTaxonName(long taxonNameId);
	
  /**
   * Returns the TypificationRecords for the partners of this taxon concept.  
   * 
   * @param taxonConceptId identifier for a taxon concept
   * @return List of TypificationRecord objects.
   */
  public List<TypificationRecord> getTypificationRecordsForNamesOfPartnersOfTaxonConcept(long taxonConceptId);
  
  /**
   * Returns the TypificationRecords for the taxon concept.  
   * 
   * @param taxonConceptId identifier for a taxon concept
   * 
   * @return List of TypificationRecord objects.
   */
  public List<TypificationRecord> getTypificationRecordsForTaxonConcept(long taxonConceptId);
}
