package org.gbif.portal.service;

import java.util.Date;
import java.util.List;

import org.gbif.portal.dto.SearchResultsDTO;

/**
 * Interface for retrieving system details.
 *  
 * @author dmartin
 */
public interface SystemManager {

	/**
	 * Retrieves the details for this system as a set of key value pairs.
	 * 
	 * @return SearchResultsDTO
	 */
	public SearchResultsDTO getSystemDetails();
	
	/**
	 * Retrieves a list of all rollover dates order by last rollover date first.
	 * 
	 * @return a list of all rollover dates.
	 */
	public List<Date> retrieveRolloverDates();
	
	/**
	 * Clear service layer cache.
	 */
	public void clearCache();
	
	/**
	 * Utility for killing long running queries.
	 * 
	 * @param maxProcessLengthInSecs
	 */
	public void killLongRunningQueries(int maxProcessLengthInSecs);
}
