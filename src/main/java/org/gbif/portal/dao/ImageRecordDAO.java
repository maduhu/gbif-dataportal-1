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

import org.gbif.portal.model.ImageRecord;

/**
 * The Image Record DAO
 * @author Donald Hobern
 */
public interface ImageRecordDAO {
	/**
	 * Creates the record
	 * @param ImageRecord To create
	 * @return The id of the created record
	 */
	public long create(ImageRecord imageRecord);
	
	/**
	 * Creates the record if the given record has no ID, otherwise will update
	 * @param ImageRecord To create or update
	 * @return The id of the created record
	 */
	public long updateOrCreate(ImageRecord imageRecord);
	
	/**
	 * Deletes the record
	 * @param ImageRecord To delete
	 */
	public void delete(ImageRecord imageRecord);
	
	/**
	 * Gets the records associated with the specified OccurrenceRecord
	 * @param occurrenceRecordId identifier of OccurrenceRecord
	 * @return The records or an empty list
	 */
	public List<ImageRecord> findByOccurrenceId(long occurrenceId);

	/**
	 * Gets the record matching the paramaters
	 * @param dataResourceId identifier of DataResource
	 * @param taxonConceptId identifier of TaxonConcept
	 * @param url URL
	 * @return The matching record or null
	 */
	public ImageRecord findByDataResourceAndTaxonConceptAndUrl(long dataResourceId, long taxonConceptId, String url);
	
	/**
	 * @param resourceAccessPointId To see if it has any records for
	 * @return true if there are any occurrences with a record
	 */
	public boolean hasImageRecords(long resourceAccessPointId);
}

