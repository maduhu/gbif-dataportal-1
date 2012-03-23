/***************************************************************************
 * Copyright (C) 2006 Global Biodiversity Information Facility Secretariat.
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
package org.gbif.portal.dao.geospatial;

import java.util.List;
import java.util.Locale;

import org.gbif.portal.model.geospatial.Country;

/**
 * The DAO for the Country model object. 
 * 
 * @author dmartin
 */
public interface CountryDAO {

	/**
	 * Retrieve the country for the supplied Id.
	 * 
	 * @param countryId the internal system id for this country
	 * @param locale the locale to use
	 * @return Object[2], Object[0]=Country, Object[1]=locale specific name/  
	 */
	public Object getCountryFor(long countryId, Locale locale);

	/**
	 * Retrieve the country for the ISO Country code.
	 * 
	 * @param countryId the internal system id for this country
	 * @param locale the locale to use
	 * @return Object[2], Object[0]=Country, Object[1]=locale specific name/  
	 */
	public Object getCountryForIsoCountryCode(String isoCountryCode, Locale locale);
	
	/**
	 * Retrieve a list countries with matching the name supplied.
	 * 
	 * @param nameStub
	 * @param fuzzy
	 * @param locale the locale to use
	 * @return SearchResultsDTO containing CountryDTO objects.
	 */
	public List<Object[]> findCountriesFor(String nameStub, boolean fuzzy, boolean anyOccurrence, boolean onlySearchInLocale, Locale locale, int startIndex, int maxResults);

	/**
	 * Retrieves all the countries within the system.
	 * 
	 * @param locale
	 * @return List of Countries 
	 */
	public List<Country> findAllCountries(Locale locale);	
	
	/**
	 * Retrieves a list of distinct first characters from the names of the countries using the specified locale.
	 * 
	 * @param locale
	 * @return List of Country and CountryName objects
	 */
	public List<Character> getCountryAlphabet(final Locale locale);

	/**
	 * Retrieves all the countries starting with the supplied char. 
	 * 
	 * @param theChar
	 * @param locale
	 * @return List of Country and CountryName objects
	 */
	public List getCountriesFor(char theChar, boolean allowAdditionalSorting, Locale locale);

	/**
	 * Returns a count of the total number of countries
	 * 
	 * @return count of the total number of countries 
	 */	
	public int getTotalCountryCount(); 
	
	/**
	 * @return The (distinct) list of ISO 2 digit codes for countries that have providers
	 */
	public List<String> getHostCountryISOCountryCodes();

	/**
	 * Retrieves the country for the supplied IP address.
	 * 
	 * @param ipAddress
	 * @param locale
	 * @return 
	 */
	public Object getCountryForIP(String ipAddress, Locale locale);

	/**
	 * Get countries for region
	 * 
	 * @param regionCode
	 * @param locale
	 * @return
	 */
	public List<Object[]> getCountriesForRegion(String regionCode, Locale locale);
	
	/**
	 * Retrieve counts for countries providing data for the country
	 * 
	 * @param isoCountryCode
	 * @param geoRefOnly
	 * @return
	 */
	public List<Object[]> getCountryCountsForCountry(String isoCountryCode, boolean geoRefOnly, Locale locale);	
	
	/**
	 * Use the taxon_country table to retrieve the countries with data for this taxon.
	 * 
	 * @param taxonConceptId
	 * @param locale
	 * @return
	 */
	public List<Object[]> getCountryCountsForTaxonConcept(long taxonConceptId,Locale locale);
	
	/**
	 * Retrieve a key value list of countries
	 * 
	 * @return key values of countries
	 */
	public List getCountryList();
}