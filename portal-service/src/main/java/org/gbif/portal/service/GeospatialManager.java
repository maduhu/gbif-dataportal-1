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

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.gbif.portal.dto.CountDTO;
import org.gbif.portal.dto.KeyValueDTO;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.geospatial.CellDensityDTO;
import org.gbif.portal.dto.geospatial.CountryDTO;
import org.gbif.portal.dto.geospatial.GeoRegionDTO;
import org.gbif.portal.dto.tag.BiRelationTagDTO;
import org.gbif.portal.dto.tag.QuadRelationTagDTO;
import org.gbif.portal.dto.util.BoundingBoxDTO;
import org.gbif.portal.dto.util.EntityType;
import org.gbif.portal.dto.util.SearchConstraints;

/**
 * Service interface for geospatial methods.
 * 
 * @author trobertson
 * @author dmartin
 */
public interface GeospatialManager {

	/**
	 * Returns a key value list of geo regions and keys for the supplied isoCountryCode.
	 * @return key value list of geoRegions and names.
	 */
	public List<KeyValueDTO> getGeoRegionList(String isoCountryCode);
	

	/**
	 * Returns a key value list of countries and their ISO codes
	 * @return
	 */
	public List<KeyValueDTO> getCountryList();
	
	
	/**
	 * Retrieves the country information for the country with the supplied key.
	 * 
	 * @param countryKey the system key for this country
	 * @param locale the locale to use for retrieving locale specific information
	 * @return CountryDTO holding details of this country, null if there isnt a country for the specified key.
	 */
	public CountryDTO getCountryFor(String countryKey, Locale locale);

	/**
	 * Retrieves the country information for the country with the supplied iso country code.
	 * 
	 * @param isoCountryCode the iso country code to use
	 * @param locale the locale to use for retrieving locale specific information
	 * @return CountryDTO holding details of this country, null if there isnt a country for the specified key.
	 */
	public CountryDTO getCountryForIsoCountryCode(String isoCountryCode, Locale locale);
	
	/**
	 * Find countries with names matching the supplied name stub.
	 *  
	 * @param nameStub
	 * @param fuzzy
	 * @param anyOccurrence
	 * @param locale
	 * @param searchConstraints
	 * @return SearchResultsDTO
	 */
	public SearchResultsDTO findCountries(String nameStub, boolean fuzzy, boolean anyOccurrence, boolean onlySearchInLocale,  Locale locale, SearchConstraints searchConstraints);

	/**
	 * Find countries with names matching the supplied name stub.
	 *  
	 * @param locale
	 * @return SearchResultsDTO
	 */
	public SearchResultsDTO findAllCountries(Locale locale);	
	
	/**
	 * Gets the list of density objects for 1 degree cells for the given type and key
	 * @param type Of search, for example TYPE_TAXON
	 * @param key The second part of the density identifier, for example 123 to indicate the concept 123 
	 * within the type
	 * @return The list of density or an empty list
	 * @throws ServiceException On system error.  Invalid types or invalid keys will return an empty list
	 */
	public List<CellDensityDTO> get1DegCellDensities(EntityType type, String key) throws ServiceException;
	
	/**
	 * Get densities for matching keys.
	 * 
	 * @param type
	 * @param keys
	 * @return
	 * @throws ServiceException
	 */
	public List<CellDensityDTO> get1DegCellDensities(EntityType type, List<String> keys) throws ServiceException;

	/**
	 * Get count totals of georeferenced records per country.
	 * 
	 * @param type
	 * @param key
	 * @return
	 * @throws ServiceException
	 */
	public List<CountDTO> getTotalsPerCountry(EntityType type, String key) throws ServiceException;	

	/**
	 * Get count totals of georeferenced records per region.
	 * 
	 * @param type
	 * @param key
	 * @return
	 * @throws ServiceException
	 */
	public List<CountDTO> getTotalsPerRegion(EntityType type, String key) throws ServiceException;	
	
	/**
	 * Gets the list of density objects for 1 degree cells for the given type and key where the cell id is in the
	 * list provided  
	 * @param type Of search, for example TYPE_TAXON
	 * @param key The second part of the density identifier, for example 123 to indicate the concept 123 
	 * within the type
	 * @param cellIds The list of cells of interest
	 * @return The list of density or an empty list
	 * @throws ServiceException On system error.  Invalid types or invalid keys will return an empty list
	 */
	public List<CellDensityDTO> get1DegCellDensities(EntityType type, String key, Set<Integer> cellIds) throws ServiceException;
	
	/**
	 * Gets the list of density objects for 1 degree cells for the given type and key where the cell id is in the
	 * list provided  
	 * @param type Of search, for example TYPE_TAXON
	 * @param keys The second part of the density identifier, for example 123 to indicate the concept 123 
	 * within the type
	 * @param cellIds The list of cells of interest
	 * @return The list of density or an empty list
	 * @throws ServiceException On system error.  Invalid types or invalid keys will return an empty list
	 */	
	public List<CellDensityDTO> get1DegCellDensities(EntityType type, List<String> keys, Set<Integer> cellIds) throws ServiceException;

	/**
	 * Gets the list of density objects for 1 degree cells for the given type and key where the cell id is in the
	 * list provided  
	 * @param type Of search, for example TYPE_TAXON
	 * @param key The second part of the density identifier, for example 123 to indicate the concept 123 
	 * within the type
	 * @param boundingBox The boundingBox of interest
	 * @return The list of density or an empty list
	 * @throws ServiceException On system error.  Invalid types or invalid keys will return an empty list
	 */
	public List<CellDensityDTO> get1DegCellDensities(EntityType type, String key, BoundingBoxDTO boundingBox) throws ServiceException;
	
	/**
	 * Gets the list of density objects for 1 degree cells for the given type and key where the cell id is in the
	 * list provided  
	 * @param type Of search, for example TYPE_TAXON
	 * @param keys The second part of the density identifier, for example 123 to indicate the concept 123 
	 * within the type
	 * @param boundingBox The boundingBox of interest
	 * @return The list of density or an empty list
	 * @throws ServiceException On system error.  Invalid types or invalid keys will return an empty list
	 */	
	public List<CellDensityDTO> get1DegCellDensities(EntityType type, List<String> keys, BoundingBoxDTO boundingBox) throws ServiceException;	

	/**
	 * Gets the list of density objects for 0.1 degree cells for the given type and key where the cell id is 
	 * that given and the centi cell id is in the list provided  
	 * @param type Of search, for example TYPE_TAXON
	 * @param key The second part of the density identifier, for example 123 to indicate the concept 123 
	 * within the type
	 * @param cellId Of interest
	 * @return The list of density or an empty list
	 * @throws ServiceException On system error.  Invalid types or invalid keys will return an empty list
	 */
	public List<CellDensityDTO> get0Point1DegCellDensities(EntityType type, String key, int cellId) throws ServiceException;
	
	/**
	 * Gets the list of density objects for 0.1 degree cells for the given type and key where the cell id is 
	 * that given and the centi cell id is in the list provided  
	 * @param type Of search, for example TYPE_TAXON
	 * @param keys The second part of the density identifier, for example 123 to indicate the concept 123 
	 * within the type
	 * @param cellId Of interest
	 * @return The list of density or an empty list
	 * @throws ServiceException On system error.  Invalid types or invalid keys will return an empty list
	 */	
	public List<CellDensityDTO> get0Point1DegCellDensities(EntityType type, List<String> keys, int cellId) throws ServiceException;	

	/**
	 * Retrieves a count of georeferenced points for the given entity.
	 * 
	 * @param type Of search, for example TYPE_TAXON
	 * @throws ServiceException On system error.  Invalid types or invalid keys will return an empty list
	 */
	public int countGeoreferencedPointsFor(EntityType entityType, String entityKey) throws ServiceException;

	/**
	 * Retrieves a count of georeferenced points for the given entity.
	 * 
	 * @param type Of search, for example TYPE_TAXON
	 * @throws ServiceException On system error.  Invalid types or invalid keys will return an empty list
	 */	
	public int countGeoreferencedPointsFor(EntityType entityType, List<String> entityKeys) throws ServiceException;	
	
	/**
	 * Retrieves a count of georeferenced points for the given entity.
	 * 
	 * @param type Of search, for example TYPE_TAXON
	 * @throws ServiceException On system error.  Invalid types or invalid keys will return an empty list
	 */
	public int countGeoreferencedPointsFor(EntityType entityType, String entityKey, BoundingBoxDTO boundingBox) throws ServiceException;	
	
	/**
	 * Retrieves a count of georeferenced points for the given entity.
	 * 
	 * @param type Of search, for example TYPE_TAXON
	 * @throws ServiceException On system error.  Invalid types or invalid keys will return an empty list
	 */	
	public int countGeoreferencedPointsFor(EntityType entityType, List<String> entityKeys, BoundingBoxDTO boundingBox) throws ServiceException;	
	/**
	 * Retrieves a distinct list of the first characters for each of the country names for a given locale.
	 * 
	 * @param locale
	 * @return list of characters
	 */
	public List<Character> getCountryAlphabet(Locale locale);
	
	/**
	 * Retrieves the correct country for the given ip address.
	 * 
	 * @return CountryDTO
	 */
	public CountryDTO getCountryForIPAddress(String ipAddress, Locale locale);
	
	/**
	 * Retrieves a distinct list of the countries with the name starting with the supplied char.
	 * Additional sorting involves including countries in a resultset that do not have names that start with
	 * the supplied char but logically (or politically) should be returned with the supplied search criteria.
	 * 
	 * @param firstChar
	 * @param additionSorting whether to allow additional sorting
	 * @param locale
	 * @return list of characters
	 */
	public List<CountryDTO> getCountriesFor(Character firstChar, boolean additionSorting, Locale locale);	

	/**
	 * Retrieves a distinct list of the countries for the supplied region.
	 * 
	 * @param firstChar
	 * @param additionSorting whether to allow additional sorting
	 * @param locale
	 * @return list of characters
	 */
	public List<CountryDTO> getCountriesForRegion(String regionCode, Locale locale);		
	
	/**
	 * Returns a list of occurrence record counts for data resources in the supplied country.
	 * 
	 * @param countryKey The country key
	 * @param georeferencedOnly Whether to only count georeferenced points 
	 * @return List<CountDTO> containing data resource id, data resource name, data provider name and count
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public List<CountDTO> getDataResourceCountsForCountry(String isoCountryCode, boolean georeferencedOnly) throws ServiceException;	
	
	/**
	 * Returns a list of occurrence record counts for countries providing data for an country
	 * (e.g.: Denmark hosts occurrences that were collected in Australia)
	 * 
	 * @param isoCountryCode The country key
	 * @param geoRefOnly Whether to only count georeferenced points 
	 * @param locale current locale of the webapps
	 * @return List<CountDTO> containing iso country code, country name and count
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection
	 */
	public List<CountDTO> getCountryCountsForCountry(String isoCountryCode, boolean geoRefOnly, Locale locale) throws ServiceException;

	/**
	 * Returns a list of occurrence record counts for data resources in the supplied country.
	 * 
	 * @param countryKey The country key
	 * @param georeferencedOnly Whether to only count georeferenced points 
	 * @return List<CountDTO> containing data resource id, data resource name, data provider name and count
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public List<CountDTO> getCountriesForDataResource(String dataResourceKey, boolean geoRefOnly) throws ServiceException;		
	
	/**
	 * Returns a list of occurrence record counts for data providers in the supplied country.
	 * 
	 * @param countryKey The country key 
	 * @return List<CountDTO> containing data resource id, data resource name, data provider name and count
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public List<CountDTO> getDataProviderCountsForCountry(String isoCountryCode) throws ServiceException;	
	
	/**
	 * Returns a count of the total number of countries
	 * 
	 * @return count of the total number of countries 
	 * @throws ServiceException indicate a failure to retrieve the data due to a network/database connection 
	 */
	public int getTotalCountryCount() throws ServiceException;		
	
	/**
	 * Returns true if the supplied string could be a valid Country Key. This
	 * method does not verify a data resource exists for this key, merely that the supplied
	 * key is of the correct format.
	 * 
	 * @see getCountryFor(String)
	 * @return true if the supplied key is a valid key
	 */
	public boolean isValidCountryKey(String countryKey);	

	/**
	 * Returns true if the supplied string could be a valid ISO Country Code Key. This
	 * method does not verify a data resource exists for this key, merely that the supplied
	 * key is of the correct format.
	 * 
	 * @see getCountryFor(String)
	 * @return true if the supplied key is a valid ISO Country Code
	 */
	public boolean isValidISOCountryCode(String isoCountryCode);
	
	/**
	 * Gets the (distinct) list of 2 digit ISO country codes that have data providers associated with them
	 * @return The list of 2 digit ISO country codes having data providers
	 */
	public List<String> getHostCountryISOCountryCodes();
	
	/**
	 * Retrieve a list of tags that can be used to render a repatriation listing.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<BiRelationTagDTO> retrieveRepatTable() throws Exception;
	
	/**
	 * Retrieve host countries with data for the supplied country.
	 *  
	 * @param isoCountryCode
	 * @return
	 * @throws Exception
	 */
	public List<BiRelationTagDTO> retrieveHostCountriesWithDataFor(String isoCountryCode) throws Exception;
	
	/**
	 * Retrieve host countries with data for the supplied country.
	 *  
	 * @param isoCountryCode
	 * @return
	 * @throws Exception
	 */
	public List<BiRelationTagDTO> retrieveHostCountriesWithDataFor(List<String> isoCountryCode) throws Exception;	
	
	/**
	 * Retrieves a list of countries for a region
	 * 
	 * @param regionCountryCodes
	 * @return
	 * @throws Exception
	 */
	public List<BiRelationTagDTO> retrieveHostCountriesWithDataForRegion(List<String> regionCountryCodes) throws Exception;

	/**
	 * Retrieve the countries with data hosted by.
	 * 
	 * @param isoCountryCode
	 * @return 
	 * @throws Exception
	 */
	public List<BiRelationTagDTO> retrieveCountriesWithDataHostedBy(String isoCountryCode) throws Exception;
	
	/**
	 * Returns a breakdown of the host versus country of origin
	 * 
	 * @param hostCountryCode
	 * @param isoCountryCode
	 * @return
	 * @throws Exception
	 */
	public BiRelationTagDTO retrieveTotalRecordHostedInFor(String hostCountryCode, String isoCountryCode) throws Exception;
	
	/**
	 * Returns a breakdown for a specific host
	 * @param hostCountryCode
	 * @return
	 * @throws Exception
	 */
	public List<QuadRelationTagDTO> retrieveBreakdownForHost(String hostCountryCode) throws Exception;
	
	/**
	 * Returns a breakdown for a specific country
	 * @param isoCountryCode
	 * @return
	 * @throws Exception
	 */
	public List<QuadRelationTagDTO> retrieveBreakdownForCountry(String isoCountryCode) throws Exception;
	
	/**
	 * Returns a breakdown of the host versus country of origin
	 * 
	 * @param hostCountryCode
	 * @param isoCountryCode
	 * @return
	 * @throws Exception
	 */
	public List<QuadRelationTagDTO> retrieveBreakdownForHostCountry(String hostCountryCode, String isoCountryCode) throws Exception;
	
	
	public List<GeoRegionDTO> getAllGeoRegions();
	
	public GeoRegionDTO getGeoRegionFor(String geoRegionKey);
}