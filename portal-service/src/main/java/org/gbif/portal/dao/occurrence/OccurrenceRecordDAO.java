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

import java.util.Date;
import java.util.List;

import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.model.geospatial.CellDensity;
import org.gbif.portal.model.occurrence.BasisOfRecord;
import org.gbif.portal.model.occurrence.OccurrenceRecord;
import org.gbif.portal.model.resources.DataProvider;
import org.gbif.portal.model.resources.DataResource;
import org.gbif.portal.model.resources.ResourceNetwork;
import org.gbif.portal.model.taxonomy.TaxonConcept;

/**
 * DAO Implementation for accessing OccurrenceRecord model objects.
 * 
 * @author dmartin
 */
public interface OccurrenceRecordDAO {
	
	/**
	 * Returns the OccurrenceRecords matching the provided parameters.
	 * 
	 * @param taxonConcept The taxon concept to which the occurrence records relate
	 * @param dataProvider The data provider from which the occurrence records come
	 * @param dataResource The data resource from which the occurrence records come
	 * @param resourceNetwork The resource network from which the occurrence records come
	 * @param scientificName A scientific name (including wildcards) for search
	 * @param hostIsoCountryCode The country from which the occurrence records come
	 * @param originIsoCountryCode The country from which the occurrence records are served
	 * @param minLongitude The minimum longitude for the area from which the occurrence records come
	 * @param maxLongitude The maximum longitude for the area from which the occurrence records come
	 * @param minLatitude The minimum latitude for the area from which the occurrence records come
	 * @param maxLatitude The maximum latitude for the area from which the occurrence records come
	 * @param cellId The identifier for the one degree cell from which the occurrence records come
	 * @param startDate The first date for the period from which the occurrence records come
	 * @param endDate The last date for the period from which the occurrence records come
	 * @param basisOfRecord The basis of record for matching records
	 * @param modifiedSince The date on or after which the records were modified
	 * @param georeferencedOnly Only return records with coordinates
	 * @param searchConstraints Paging constraints
	 * @return List of matching OccurrenceRecord objects.
	 */
	public List<OccurrenceRecord> findOccurrenceRecords(TaxonConcept taxonConcept, DataProvider dataProvider, DataResource dataResource, ResourceNetwork resourceNetwork, String scientificName, String hostIsoCountryCode, String originIsoCountryCode, Float minLongitude, Float maxLongitude, Float minLatitude, Float maxLatitude, Integer cellId, Date startDate, Date endDate, BasisOfRecord basisOfRecord, Date modifiedSince, boolean georeferencedOnly, SearchConstraints searchConstraints);

	/**
	 * Counts the OccurrenceRecords matching the provided parameters.
	 * 
	 * @param taxonConcept The taxon concept to which the occurrence records relate
	 * @param dataProvider The data provider from which the occurrence records come
	 * @param dataResource The data resource from which the occurrence records come
	 * @param resourceNetwork The resource network from which the occurrence records come
	 * @param scientificName A scientific name (including wildcards) for search
	 * @param hostIsoCountryCode The country from which the occurrence records come
	 * @param originIsoCountryCode The country from which the occurrence records are served
	 * @param minLongitude The minimum longitude for the area from which the occurrence records come
	 * @param maxLongitude The maximum longitude for the area from which the occurrence records come
	 * @param minLatitude The minimum latitude for the area from which the occurrence records come
	 * @param maxLatitude The maximum latitude for the area from which the occurrence records come
	 * @param cellId The identifier for the one degree cell from which the occurrence records come
	 * @param startDate The first date for the period from which the occurrence records come
	 * @param endDate The last date for the period from which the occurrence records come
	 * @param basisOfRecord The basis of record for matching records
	 * @param modifiedSince The date on or after which the records were modified
	 * @param georeferencedOnly Only return records with coordinates
	 * @return Count of matching OccurrenceRecord objects.
	 */
	public Long countOccurrenceRecords(TaxonConcept taxonConcept, DataProvider dataProvider, DataResource dataResource, ResourceNetwork resourceNetwork, String scientificName, String hostIsoCountryCode, String originIsoCountryCode, Float minLongitude, Float maxLongitude, Float minLatitude, Float maxLatitude, Integer cellId, Date startDate, Date endDate, BasisOfRecord basisOfRecord, Date modifiedSince, boolean georeferencedOnly);

	/**
	 * Returns the OccurrenceRecord with the specified id value.
	 * 
	 * @param occurrenceRecordId The id of the occurrence record
	 * @return The OccurrenceRecord object for this id.
	 */
	public OccurrenceRecord getOccurrenceRecordFor(long occurrenceRecordId);
	
	/**
	 * Returns the OccurrenceRecord with the specified codes.
	 * 
	 * @param institutionCode
	 * @param collectionCode
	 * @param catalogueNumber
	 * @return The OccurrenceRecord object for these codes.
	 */
	public List<OccurrenceRecord> getOccurrenceRecordFor(String institutionCode,
			String collectionCode, String catalogueNumber);	
	
	/**
	 * Returns the total number of occurrence records within the system. 
	 * 
	 * @return the total number of occurrence records within the system. 
	 */
	public int getTotalOccurrenceRecordCount();
	
	/**
		 * Return the total number of occurrence records from deleted providers.
	 * 
	 * @return the total number of occurrence records from deleted providers.
	 */
	public int getTotalOccurrenceRecordCountForDeletedProviders();
	
	/**
	 * Returns a list of the centiCellId and the count for the taxonConcept within the cell
	 * id provided, or an empty list 
	 * @param taxonConceptId Of interest 
	 * @param cellId Within which the centiCellIds will be searched
	 * @return The list of arrays containing the centiCellId and the count
	 */
	public List<CellDensity> getCentiCellDensitiesForTaxonConcept(long taxonConceptId, int cellId);
	
	/**
	 * Returns a list of the centiCellId and the count for the dataProvider within the cell
	 * id provided, or an empty list 
	 * @param taxonConceptId Of interest 
	 * @param cellId Within which the centiCellIds will be searched
	 * @return The list of arrays containing the centiCellId and the count
	 */
	public List<CellDensity> getCentiCellDensitiesForDataProvider(long dataProviderId, int cellId);
	
	/**
	 * Returns a list of the centiCellId and the count for the dataResource within the cell
	 * id provided, or an empty list 
	 * @param dataResourceId Of interest 
	 * @param cellId Within which the centiCellIds will be searched
	 * @return The list of arrays containing the centiCellId and the count
	 */
	public List<CellDensity> getCentiCellDensitiesForDataResource(long dataResourceId, int cellId);
	
	/**
	 * Returns a list of the centiCellId and the count for the isoCountryCode within the cell
	 * id provided, or an empty list 
	 * @param isoCountryCode Of interest 
	 * @param cellId Within which the centiCellIds will be searched
	 * @return The list of arrays containing the centiCellId and the count
	 */
	public List<CellDensity> getCentiCellDensitiesForIsoCountryCode(String isoCountryCode, int cellId);

	/**
	 * Returns a list of centiCellIds and count for the resource network within the cell provided
	 * or an empty list
	 * 
	 * @param resourceNetworkId
	 * @param cellId Within which the centiCellIds will be searched
	 * @return The list of arrays containing the centiCellId and the count
	 */
	public List<CellDensity> getCentiCellDensitiesForResourceNetwork(long resourceNetworkId, int cellId);
}
