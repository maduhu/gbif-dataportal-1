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
package org.gbif.portal.dao.log;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.gbif.portal.io.ResultsOutputter;
import org.gbif.portal.model.log.LogMessage;

/**
 * DAO interface for log messages
 * 
 * @author dmartin
 */
public interface LogMessageDAO {

	/**
	 * Find log messages with the following properties
	 * 
	 * @param dataProviderId
	 * @param dataResourceId
	 * @param userKey
	 * @param occurrenceRecordId
	 * @param taxonConceptId
	 * @param eventId
	 * @param minLogLevel
	 * @param logGroupId
	 * @param startDate
	 * @param endDate
	 * @param startIndex
	 * @param maxResults
	 * @return List of Log Message objects
	 */
	public List<LogMessage> findMessages(Long dataProviderId,
			Long dataResourceId, Long userId, Long occurrenceRecordId,
			Long taxonConceptId, Integer eventId, Integer minEventId,
			final Integer maxEventId, Long minLogLevel, Long logGroupId,
			Date startDate, Date endDate, int startIndex, int maxResults);

	/**
	 * Output the results of the query using the results outputter.
	 * 
	 * @param resultsOutputter
	 * @param dataProviderId
	 * @param dataResourceId
	 * @param userId
	 * @param occurrenceRecordId
	 * @param taxonConceptId
	 * @param eventId
	 * @param minEventId
	 * @param maxEventId
	 * @param minLogLevel
	 * @param logGroupId
	 * @param startDate
	 * @param endDate
	 * @param startIndex
	 * @param maxResults
	 * @throws IOException
	 */
	public void outputMessages(ResultsOutputter resultsOutputter,
			Long dataProviderId, Long dataResourceId, Long userId,
			Long occurrenceRecordId, Long taxonConceptId, Integer eventId,
			Integer minEventId, Integer maxEventId, Long minLogLevel,
			Long logGroupId, Date startDate, Date endDate, int startIndex,
			int maxResults) throws IOException;

	/**
	 * Count log messages with the following properties
	 * 
	 * @param dataProviderId
	 * @param dataResourceId
	 * @param userKey
	 * @param occurrenceRecordId
	 * @param taxonConceptId
	 * @param eventId
	 * @param minLogLevel
	 * @param logGroupId
	 * @param startDate
	 * @param endDate
	 * @param startIndex
	 * @param maxResults
	 * @return List of Log Message objects
	 */
	public int countMessages(Long dataProviderId, Long dataResourceId,
			Long userId, Long occurrenceRecordId, Long taxonConceptId,
			Integer eventId, Integer minEventId, Integer maxEventId,
			Long minLogLevel, Long logGroupId, Date startDate, Date endDate,
			int startIndex, int maxResults);

	/**
	 * Count the messages grouping by data resource.
	 * 
	 * @param dataProviderId
	 * @param dataResourceId
	 * @param userId
	 * @param occurrenceRecordId
	 * @param taxonConceptId
	 * @param eventId
	 * @param minEventId
	 * @param maxEventId
	 * @param minLogLevel
	 * @param logGroupId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Object[]> countMessagesByDataResource(Long dataProviderId,
			Long dataResourceId, Long userId, Long occurrenceRecordId,
			Long taxonConceptId, Integer eventId, Integer minEventId,
			Integer maxEventId, Long minLogLevel, Long logGroupId,
			Date startDate, Date endDate);

	/**
	 * Retrieve log messages matching the supplied ids.
	 * 
	 * @param dataProviderId
	 * @param dataResourceId
	 * @param eventIds
	 * @return log messages
	 */
	public List<LogMessage> getLogMessagesForEventIds(
			final Long dataProviderId, final Long dataResourceId,
			final List<Integer> eventIds);

	/**
	 * @return
	 */
	public Date getEarliestLogMessageDate();
	
	/**
	 * @return
	 */
	public Date getLatestLogMessageDate();
}