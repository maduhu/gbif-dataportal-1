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
package org.gbif.portal.dao.resources;

import java.util.Date;
import java.util.List;

import org.gbif.portal.model.resources.DataProvider;

/**
 * DAO methods for the DataProvider entity.
 * 
 * @author dmartin
 */
public interface DataProviderDAO {
	
	/**
	 * Returns the DataProvider with the specified id value.
	 * 
	 * @param dataProviderId The id of the data provider
	 * @return The DataProvider object for this id.
	 */
	public DataProvider getDataProviderFor(long dataProviderId);

	/**
	 * 
	 * @param email
	 * @return
	 */
	public List<DataProvider> getDataProviderAssociatedWithUser(String emailUsername, final String emailDomain);
	
	/**
	 * Retrieves a list of DataProviders with DataResources that 
	 * collectively provide a shared taxonomy.
	 * 
	 * @return list of Data Resources
	 */
	public List<DataProvider> getDataProvidersOfferingTaxonomies();
	
	/**
	 * Retrieve a list of data providers matching the supplied name 
	 *
	 * @param nameStub the name to search for
	 * @param fuzzy whether to do a wildcard search
	 * @param startIndex the result to start at
	 * @param maxResults max number of results to return
	 * @return a list of DataProvider model objects
	 */
	public List<DataProvider> findDataProviders(String nameStub, boolean fuzzy, String isoCountryCode, Date modifiedSince, int startIndex, int maxResults);

	/**
	 * Count data providers matching the supplied name 
	 *
	 * @param nameStub the name to search for
	 * @param fuzzy whether to do a wildcard search
	 * @param startIndex the result to start at
	 * @param maxResults max number of results to return
	 * @return count of DataProvider model objects
	 */
	public Long countDataProviders(String nameStub, boolean fuzzy, String isoCountryCode, Date modifiedSince);

	/**
	 * Returns a count of the number of data providers
	 * within the system. 
	 * 
	 * @return a count of the number of data providers 
	 */
	public int getTotalDataProviderCount();

	/**
	 * Retrieve a key value list of data providers.
	 * @return key value list of data providers.
	 */
	public List getDataProviderList();
	
	/**
	 * Retrieves counts of occurrences records supplied for this country.
	 * @param isoCountryCode
	 * @return a list of providers and counts.
	 */
	public List<Object[]> getDataProviderCountsForCountry(String isoCountryCode);	
	
	/**
	 * Retrieve the total number of records in hosted in a country.
	 * @return a total
	 */
	public int getDataProviderCountsForHostCountry(String isoCountryCode, boolean georeferenced);
	
	/**
	 * TODO move to better home.
	 * 
	 * Retrieve kvps of system details.
	 */
	public List<Object[]> getSystemDetails();

	/**
	 * Retrieve all data providers. Potentially expensive.
	 * @return
	 */
	public List<DataProvider> getAllDataProviders();
	
	/**
	 * Retrieves a list of rollover dates.
	 * @return
	 */
	public List<Date> getRolloverDates();

	/**
	 * Retrieve a list of providers not associated with a host country.
	 * @param withOccurrencesOnly
	 * @return
	 */
	public List<DataProvider> getInternationalDataProviders(boolean withOccurrencesOnly);
}