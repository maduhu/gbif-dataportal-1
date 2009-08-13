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
package org.gbif.portal.service;

import java.util.Date;
import java.util.List;

import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.occurrence.BriefOccurrenceRecordDTO;
import org.gbif.portal.dto.occurrence.ExtendedOccurrenceRecordDTO;
import org.gbif.portal.dto.occurrence.IdentifierRecordDTO;
import org.gbif.portal.dto.occurrence.ImageRecordDTO;
import org.gbif.portal.dto.occurrence.KmlOccurrenceRecordDTO;
import org.gbif.portal.dto.occurrence.LinkRecordDTO;
import org.gbif.portal.dto.occurrence.OccurrenceRecordDTO;
import org.gbif.portal.dto.occurrence.RawOccurrenceRecordDTO;
import org.gbif.portal.dto.occurrence.TypificationRecordDTO;
import org.gbif.portal.dto.util.BoundingBoxDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.dto.util.TimePeriodDTO;

/**
 * Service interface for Occurrence Record Interface methods. This is the public interface
 * providing methods for retrieving Occurrence Records that have been indexed within the portal. 
 * 
 * These methods will for the most part return OccurrenceRecordDTO or a SearchResultsDTO which contains
 * a collection of OccurrenceRecordDTOs unless otherwise stated in method javadoc.
 * 
 * ServiceExceptions are thrown by these methods to indicate a failure to retrieve the data due to
 * a network/database connection or a data integrity problem. ServiceExceptions are not thrown to indicate 
 * a bad argument value.  
 * 
 * @see SearchResultsDTO
 * @see BriefOccurrenceRecordDTO
 * @see OccurrenceRecordDTO
 * 
 * @author dmartin
 */
public interface OccurrenceManager {
	
	/**
	 * Returns the Occurrence Record for the specified key value. Returns null if there is not
	 * a Occurrence Record for the supplied key or if the supplied key is invalid.
	 * 
	 * @param occurrenceRecordKey The occurrence record key 
	 * @return OccurrenceRecordDTO containing details of this occurrence record, null if unable to find record for this key
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public OccurrenceRecordDTO getOccurrenceRecordFor(String occurrenceRecordKey) throws ServiceException;
	
	/**
	 * Returns the Extended Occurrence Record for the specified key value. Returns null if there is not 
	 * a Occurrence Record for the supplied key or if the supplied key is invalid.
	 * 
	 */
	public ExtendedOccurrenceRecordDTO getExtendedOccurrenceRecordFor(String occurrenceRecordKey) throws ServiceException;	
	
	/**
	 * Returns the Occurrence Record for the specified institution code, collection code and catalogueNumber.
	 * Returns null if there isnt a occurrence record for this combination.
	 * 
	 * @param institutionCode
	 * @param collectionCode
	 * @param catalogueNumber
	 * @return OccurrenceRecordDTO containing details of this occurrence record, null if unable to find record for this key
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public List<OccurrenceRecordDTO> getOccurrenceRecordByCodes(String institutionCode, String collectionCode, String catalogueNumber) throws ServiceException;	
	
	/**
	 * Returns the list of Occurrence Records matching the specified search criteria.
	 * All values are nullable, providing no parameters will return a list of all occurrence records 
	 * in the system.
	 * 
	 * @param dataProviderKey the data provider to search, nullable
	 * @param dataResourceKey the data resource to search, nullable
	 * @param resourceNetworkKey the resource network to search, nullable
	 * @param taxonConceptKey the taxon concept key to search for, nullable
	 * @param scientificName a scientific name (including wildcards) for search
	 * @param hostIsoCountryCode the 2 digit ISO country code of the provider, nullable
	 * @param originIsoCountryCode the 2 digit ISO country code of the record, nullable
	 * @param basisOfRecordCode the basis of record code for the record, nullable
	 * @param cellId the identifier of the one degree cell for the record, nullable
	 * @param boundingBox provide a bounding box of points, nullable
	 * @param timePeriod provide a time period, nullable
	 * @param modifiedSince date on or after which records modified 
	 * @param georeferencedOnly return only georeferenced records
	 * @param searchConstraints the search constraints to use
	 * @return a SearchResultsDTO containing BriefOccurrenceRecordDTO
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public SearchResultsDTO findOccurrenceRecords(
			String dataProviderKey,
			String dataResourceKey,
			String resourceNetworkKey,
			String taxonConceptKey,	
			String scientificName,
			String hostIsoCountryCode,
			String originIsoCountryCode,
			String basisOfRecordCode,
			String cellId, 
			BoundingBoxDTO boundingBox,
			TimePeriodDTO timePeriod,
			Date modifiedSince,
			boolean georeferencedOnly,
			SearchConstraints searchConstraints)
	throws ServiceException;
	
	/**
	 * Find raw (unparsed) record data. 
	 * 
	 * @param dataResourceId
	 * @param catalogueNumber
	 * @return
	 */
	public SearchResultsDTO findRawOccurrenceRecord(String dataResourceKey,
			String catalogueNumber, SearchConstraints searchConstraints);

	/**
	 * Returns a count of Occurrence Records matching the specified search criteria.
	 * All values are nullable, providing no parameters will return a list of all occurrence records 
	 * in the system.
	 * 
	 * @param dataProviderKey the data provider to search, nullable
	 * @param dataResourceKey the data resource to search, nullable
	 * @param resourceNetworkKey the resource network to search, nullable
	 * @param taxonConceptKey the taxon concept key to search for, nullable
	 * @param scientificName a scientific name (including wildcards) for search
	 * @param hostIsoCountryCode the 2 digit ISO country code of the provider, nullable
	 * @param originIsoCountryCode the 2 digit ISO country code of the record, nullable
	 * @param basisOfRecordCode the basis of record code for the record, nullable
	 * @param cellId the identifier of the one degree cell for the record, nullable
	 * @param boundingBox provide a bounding box of points, nullable
	 * @param timePeriod provide a time period, nullable
	 * @param modifiedSince date on or after which records modified 
	 * @param georeferencedOnly return only georeferenced records
	 * @return count of matching records
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public int countOccurrenceRecords(
			String dataProviderKey,
			String dataResourceKey,
			String resourceNetworkKey,
			String taxonConceptKey,
			String ScientificName,
			String hostIsoCountryCode,
			String originIsoCountryCode,
			String basisOfRecordCode,
			String cellId, 
			BoundingBoxDTO boundingBox,
			TimePeriodDTO timePeriod,
			Date modifiedSince,
			boolean georeferencedOnly)
	throws ServiceException;
	
	/**
	 * Returns the current number of occurrence records within the system.
	 * 
	 * @return the current number of occurrence records within the system.
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public int getTotalOccurrenceRecordCount() throws ServiceException;

	/**
	 * Returns the Raw Occurrence Record for the specified key value. Returns null if there is not
	 * a Raw Occurrence Record for the supplied key or if the supplied key is invalid.
	 * 
	 * @param rawOccurrenceRecordKey The raw occurrence record key 
	 * @return RawOccurrenceRecordDTO containing details of this occurrence record, null if unable to find record for this key
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public RawOccurrenceRecordDTO getRawOccurrenceRecordFor(String rawOccurrenceRecordKey) throws ServiceException;
	
	/**
	 * Returns the image records for the specified occurrence record. 
	 * 
	 * @param occurrenceRecordKey The occurrence record key 
	 * @return List of ImageRecordDTOs for this occurrence record
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public List<ImageRecordDTO> getImageRecordsForOccurrenceRecord(String occurrenceRecordKey) throws ServiceException;
	
	/**
	 * Returns the link records for the specified occurrence record. 
	 * 
	 * @param occurrenceRecordKey The occurrence record key 
	 * @return List of LinkRecordDTOs for this occurrence record
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public List<LinkRecordDTO> getLinkRecordsForOccurrenceRecord(String occurrenceRecordKey) throws ServiceException;
	
	/**
	 * Returns the typification records for the specified occurrence record. 
	 * 
	 * @param occurrenceRecordKey The occurrence record key 
	 * @return List of TypificationRecordDTOs for this occurrence record
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public List<TypificationRecordDTO> getTypificationRecordsForOccurrenceRecord(String occurrenceRecordKey) throws ServiceException;
	
	/**
	 * Returns the identifier records for the specified occurrence record. 
	 * 
	 * @param occurrenceRecordKey The occurrence record key 
	 * @return List of IdentifierRecordDTOs for this occurrence record
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public List<IdentifierRecordDTO> getIdentifierRecordsForOccurrenceRecord(String occurrenceRecordKey) throws ServiceException;
	
	/**
	 * Returns the KmlOccurrenceRecordDTO for this key. Returns null in case there is no record associated for this key
	 * 
	 * @param occurrenceRecordKey The occurrence record key
	 * @return KmlOccurrenceRecordDTO containing kml data of this occurrence record, null if there is no record for this key
	 * @throws ServiceException
	 */
	public KmlOccurrenceRecordDTO getKmlOccurrenceRecordFor(String occurrenceRecordKey) throws ServiceException;	
	
	/**
	 * Returns true if the supplied string could be a valid Occurrence Record Key. This
	 * method does not verify a concept exists for this key, merely that the supplied
	 * key is of the correct format.
	 * 
	 * @see getOccurrenceRecordFor(String)
	 * @return true if the supplied key is a valid key
	 */
	public boolean isValidOccurrenceRecordKey(String occurrenceRecordKey);	
}