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

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.log.LogStatsDTO;
import org.gbif.portal.dto.log.LoggedActivityDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.io.ResultsOutputter;
import org.gbif.portal.util.log.GbifLogMessage;
import org.gbif.portal.util.log.LogGroup;

/**
 * Tools to support GBIF logging
 * 
 * Note:
 * This encorporates the User creation at present at the user is only used at present to track the 
 * user logging the message.  This will be refactored when the user can log in.
 * 
 * @author Donald Hobern
 */
public interface LogManager {
	
	/**
	 * Create a GbifLogMessage for a user feedback message related to an OccurrenceRecord.
	 * 
	 * @param logGroup
	 * @param userKey
	 * @param occurrenceKey
	 * @param messageText
	 * @return
	 */
	public GbifLogMessage createOccurrenceFeedbackMessage(LogGroup logGroup, 
			  String userKey, 
			  String occurrenceKey, 
			  String messageText);
	
	/**
	 * Create a GbifLogMessage for a user feedback message related to an OccurrenceRecord.
	 * 
	 * @param logGroup
	 * @param userName
	 * @param userEmail
	 * @param occurrenceKey
	 * @param messageText
	 * @return
	 */
	public GbifLogMessage createOccurrenceFeedbackMessage(LogGroup logGroup, 
			  String userName,
			  String userEmail,
			  String occurrenceKey, 
			  String messageText);
	
	/**
	 * Create a GbifLogMessage for a user feedback message related to a TaxonConcept.
	 * 
	 * @param logGroup
	 * @param userKey
	 * @param taxonConceptKey
	 * @param messageText
	 * @return
	 */
	public GbifLogMessage createTaxonFeedbackMessage(LogGroup logGroup, 
			  String userKey, 
			  String taxonConceptKey, 
			  String messageText);
	
	/**
	 * Create a GbifLogMessage for a user feedback message related to a TaxonConcept.
	 * 
	 * @param logGroup
	 * @param userName
	 * @param userEmail
	 * @param taxonConceptKey
	 * @param messageText
	 * @return
	 */
	public GbifLogMessage createTaxonFeedbackMessage(LogGroup logGroup, 
			  String userName,
			  String userEmail,
			  String taxonConceptKey, 
			  String messageText);
	
	/**
	 * Create a GbifLogMessage for a user feedback message related to a DataResource.
	 * 
	 * @param logGroup
	 * @param userKey
	 * @param dataResourceKey
	 * @param messageText
	 * @return
	 */
	public GbifLogMessage createResourceFeedbackMessage(LogGroup logGroup, 
			  String userKey, 
			  String dataResourceKey, 
			  String messageText);
	
	/**
	 * Create a GbifLogMessage for a user feedback message related to a DataResource.
	 * 
	 * @param logGroup
	 * @param userKey
	 * @param dataResourceKey
	 * @param messageText
	 * @return
	 */
	public GbifLogMessage createResourceFeedbackMessage(LogGroup logGroup, 
			  String userName,
			  String userEmail,
			  String dataResourceKey, 
			  String messageText);

	/**
	 * Retrieve a list of messages for the supplied critieria.
	 * 
	 * @param dataProviderKey not nullable
	 * @param dataResourceKey
	 * @param userKey
	 * @param occurrenceKey
	 * @param taxonConceptKey
	 * @param date
	 * @param searchConstraints
	 * @return SearchResultsDTO
	 */
	public SearchResultsDTO findLogMessagesFor(
			String dataProviderKey, 
			String dataResourceKey, 
			String userKey, 
			String occurrenceKey, 
			String taxonConceptKey, 
			String eventKey, 
			String minEventKey, 
			String maxEventKey, 			
			Long minLogLevel, 
			String logGroupKey, 
			Date startDate, 
			Date endDate, 
			SearchConstraints searchConstraints);

	/**
	 * Retrieve a list of messages for the supplied critieria.
	 * 
	 * @param dataProviderKey not nullable
	 * @param dataResourceKey
	 * @param userKey
	 * @param occurrenceKey
	 * @param taxonConceptKey
	 * @param date
	 * @param searchConstraints
	 * @return SearchResultsDTO
	 */
	public void formatLogMessagesFor(
			String dataProviderKey, 
			String dataResourceKey, 
			String userKey, 
			String occurrenceKey, 
			String taxonConceptKey, 
			String eventKey, 
			String minEventKey, 
			String maxEventKey, 			
			Long minLogLevel, 
			String logGroupKey, 
			Date startDate, 
			Date endDate, 
			SearchConstraints searchConstraints, ResultsOutputter resultsOutputter) throws IOException;	
	
	/**
	 * Retrieve the logging statistics for each data resource based on the supplied 
	 * criteria.
	 * 
	 * Note: the resulting query may take considerable time and should not necessarily
	 * by used synchronously.
	 * 
	 * @param dataProviderKey
	 * @param dataResourceKey
	 * @param userKey
	 * @param occurrenceKey
	 * @param taxonConceptKey
	 * @param eventKey
	 * @param minEventKey
	 * @param maxEventKey
	 * @param minLogLevel
	 * @param logGroupKey
	 * @param startDate
	 * @param endDate
	 * @return a list of Log stats DTOs
	 * @throws IOException
	 */
	public List<LogStatsDTO> getDataResourceLogStatsFor(
			String dataProviderKey,
			String dataResourceKey,
			String userKey,
			String occurrenceKey, 
			String taxonConceptKey,
			String eventKey,
			String minEventKey, 
			String maxEventKey, 			
			Long minLogLevel,
			String logGroupKey, 
			Date startDate,
			Date endDate) throws IOException;		
	
	/**
	 * Get the indexing history for the specified provider/resource. 
	 * 
	 * @param providerKey
	 * @param resourceKey
	 * @return
	 */
	public List<LoggedActivityDTO> getIndexingHistory(String providerKey, String resourceKey);

	/**
	 * Get the indexing history for the specified provider/resource. 
	 * 
	 * @param providerKey
	 * @param resourceKey
	 * @return
	 */
	public List<LoggedActivityDTO> getHarvestingHistory(String providerKey, String resourceKey);	
	
	/**
	 * Get the indexing history for the specified provider/resource. 
	 * 
	 * @param providerKey
	 * @param resourceKey
	 * @return
	 */
	public List<LoggedActivityDTO> getExtractionHistory(String providerKey, String resourceKey);		
	
	/**
	 * Retrieve a list of messages for the supplied critieria.
	 * 
	 * @param dataProviderKey not nullable
	 * @param dataResourceKey
	 * @param userKey
	 * @param occurrenceKey
	 * @param taxonConceptKey
	 * @param date
	 * @param searchConstraints
	 * @return SearchResultsDTO
	 */
	public int countLogMessagesFor(
			String dataProviderKey, 
			String dataResourceKey, 
			String userKey, 
			String occurrenceKey, 
			String taxonConceptKey, 
			String eventKey, 
			String minEventKey, 
			String maxEventKey, 			
			Long minLogLevel, 
			String logGroupKey, 
			Date startDate, 
			Date endDate, 
			SearchConstraints searchConstraints);	
	
	/**
	 * Open a log group
	 * 
	 * @param logGroup
	 * @return
	 */
	public LogGroup startLogGroup();
	
	/**
	 * Gets the userKey associated with the username/useremail combination
	 * 
	 * @param userName
	 * @param userEmail
	 * @return
	 */
	public String getUserKeyFor(String userName, String userEmail);
	
	/**
	 * Checks whether the userKey belongs to a verified user (verified=be able to send feedback messages through the Data Portal) 
	 * 
	 * @param userKey
	 * @return
	 */
	public boolean isVerifiedUser(String userKey);
	
	/**
	 * Sends feedback or verification email messages
	 * 
	 * @param message
	 * @param isVerified
	 */
	public void sendFeedbackOrVerificationMessages(GbifLogMessage message, boolean isVerified);

	/**
	 * Close a log group
	 * 
	 * @return
	 */
	public boolean endLogGroup(LogGroup logGroup);
	
	/**
	 * Authorises the user, if the code matches that required for the key
	 * (@see UserUtils)
	 * @param key User key
	 * @param code The code required for authorisation
	 * @return The user name if successful, or null otherwise
	 */
	public String authoriseUser(String key, String code);

	/**
	 * Retrieves the date for the earliest log message currently
	 * available in the system.
	 * 
	 * @return Date for this message
	 */
	public Date getEarliestLogMessageDate();	

	/**
	 * Retrieves the date for the last log message currently
	 * available in the system.
	 * 
	 * @return Date for this message
	 */
	public Date getLatestLogMessageDate();		
}