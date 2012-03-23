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
package org.gbif.portal.util.log;

import org.apache.log4j.Level;

import java.util.Date;


/**
 * Base implementation of a log message for use with GbifLog instances.
 *
 * @author Donald Hobern
 */
public class GbifLogMessage {

  /**
   * Identifier for persisted message
   */
  protected long id;

  /**
   * Identifier for portal instance logging message
   */
  protected long portalInstanceId;

  /**
   * Log group (used to associate a set of records deriving from
   * the same activity)
   */
  protected LogGroup logGroup;

  /**
   * Event type identifier
   */
  protected LogEvent event = LogEvent.UNKNOWN;

  /**
   * Event type identifier
   */
  protected Level level = Level.INFO;

  /**
   * User identifier where appropriate
   */
  protected Long userId;

  /**
   * Data provider identifier
   */
  protected Long dataProviderId;

  /**
   * Data resource identifier
   */
  protected Long dataResourceId;

  /**
   * Taxon concept identifier
   */
  protected Long taxonConceptId;

  /**
   * Taxon concept identifier
   */
  protected Long occurrenceId;

  /**
   * Message string
   */
  protected String message;

  /**
   * Messages should be counted as a group per logGroupId and eventId
   * combination
   */
  protected boolean countOnly;

  /**
   * Count of messages within a log group
   */
  protected int count;

  /**
   * Timestamp for persisted messages
   */
  protected Date timestamp;

  /**
   * Message should only be visible to data provider and GBIF admin.
   */
  protected boolean restricted;

  public GbifLogMessage() {
  }

  public GbifLogMessage(LogGroup logGroup) {
    this.logGroup = logGroup;
  }

  public GbifLogMessage(LogGroup logGroup, LogEvent event) {
    this.logGroup = logGroup;
    this.event = event;
  }

  public GbifLogMessage(LogGroup logGroup, LogEvent event, String message) {
    this.logGroup = logGroup;
    this.event = event;
    this.message = message;
  }

  public GbifLogMessage(LogGroup logGroup, LogEvent event, String message, boolean restricted) {
    this.logGroup = logGroup;
    this.event = event;
    this.message = message;
    this.restricted = restricted;
  }

  public GbifLogMessage(long id, long portalInstanceId, long logGroupId,
                        Integer eventId, int levelId, Long dataProviderId,
                        Long dataResourceId, Long occurrenceId,
                        Long taxonConceptId, long userId, String message,
                        Boolean restricted, int count, Date timestamp) {
    this.id = id;
    this.portalInstanceId = portalInstanceId;
    this.logGroup = new LogGroup(logGroupId);
    this.event = LogEvent.get(eventId);
    this.level = Level.toLevel(levelId);
    this.dataProviderId = dataProviderId;
    this.dataResourceId = dataResourceId;
    this.occurrenceId = occurrenceId;
    this.taxonConceptId = taxonConceptId;
    this.userId = userId;
    this.message = message;
    this.restricted = restricted;
    this.count = count;
    this.timestamp = timestamp;
  }

  /**
   * @return the countOnly
   */
  public boolean isCountOnly() {
    return countOnly;
  }

  /**
   * @param countOnly the countOnly to set
   */
  public void setCountOnly(boolean countOnly) {
    this.countOnly = countOnly;
  }

  /**
   * @return the dataProviderId
   */
  public Long getDataProviderId() {
    return dataProviderId;
  }

  /**
   * @param dataProviderId the dataProviderId to set
   */
  public void setDataProviderId(Long dataProviderId) {
    this.dataProviderId = dataProviderId;
  }

  /**
   * @return the dataResourceId
   */
  public Long getDataResourceId() {
    return dataResourceId;
  }

  /**
   * @param dataResourceId the dataResourceId to set
   */
  public void setDataResourceId(Long dataResourceId) {
    this.dataResourceId = dataResourceId;
  }

  /**
   * @return the logGroup
   */
  public LogGroup getLogGroup() {
    return logGroup;
  }

  /**
   * @param logGroup the logGroup to set
   */
  public void setLogGroup(LogGroup logGroup) {
    this.logGroup = logGroup;
  }

  /**
   * @return the occurrenceRecordId
   */
  public Long getOccurrenceId() {
    return occurrenceId;
  }

  /**
   * @param occurrenceRecordId the occurrenceRecordId to set
   */
  public void setOccurrenceId(Long occurrenceId) {
    this.occurrenceId = occurrenceId;
  }

  /**
   * @return the restricted
   */
  public boolean isRestricted() {
    return restricted;
  }

  /**
   * @param restricted the restricted to set
   */
  public void setRestricted(boolean restricted) {
    this.restricted = restricted;
  }

  /**
   * @return the taxonConceptId
   */
  public Long getTaxonConceptId() {
    return taxonConceptId;
  }

  /**
   * @param taxonConceptId the taxonConceptId to set
   */
  public void setTaxonConceptId(Long taxonConceptId) {
    this.taxonConceptId = taxonConceptId;
  }

  /**
   * @return the userId
   */
  public Long getUserId() {
    return userId;
  }

  /**
   * @param userId the userId to set
   */
  public void setUserId(Long userId) {
    this.userId = userId;
  }

  /**
   * @return the portalInstanceId
   */
  public long getPortalInstanceId() {
    return portalInstanceId;
  }

  /**
   * @param portalInstanceId the portalInstanceId to set
   */
  public void setPortalInstanceId(long portalInstanceId) {
    this.portalInstanceId = portalInstanceId;
  }

  /**
   * @return the event
   */
  public LogEvent getEvent() {
    return event;
  }

  /**
   * @param event the event to set
   */
  public void setEvent(LogEvent event) {
    if (event == null) {
      event = LogEvent.UNKNOWN;
    }
    this.event = event;
  }

  /**
   * @return the level
   */
  public Level getLevel() {
    return level;
  }

  /**
   * @param level the level to set
   */
  public void setLevel(Level level) {
    if (level == null) {
      level = Level.INFO;
    }
    this.level = level;
  }

  /**
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * @param message the message to set
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * @return the id
   */
  public long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * @return the timestamp
   */
  public Date getTimestamp() {
    return timestamp;
  }

  /**
   * @param timestamp the timestamp to set
   */
  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * @return the count
   */
  public int getCount() {
    return count;
  }

  /**
   * @param count the count to set
   */
  public void setCount(int count) {
    this.count = count;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append(event);

    if (message != null) {
      sb.append(" ");
      sb.append(message);
    }

    return sb.toString();
  }
}
