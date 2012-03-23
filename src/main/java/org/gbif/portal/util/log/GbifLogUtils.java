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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * Tools to support GBIF logging
 *
 * @author Donald Hobern
 */
public class GbifLogUtils {

  /**
   * Logger
   */
  protected static Log logger = LogFactory.getLog(GbifLogUtils.class);

  /**
   * Context keys
   */
  protected String contextKeyDataProviderId;
  protected String contextKeyDataResourceId;
  protected String contextKeyOccurrenceId = "occurrenceId";
  protected String contextKeyLogGroup;

  public GbifLogMessage createGbifLogMessage(ProcessContext context, LogEvent event) {
    return createGbifLogMessage(context, event, null);
  }

  public GbifLogMessage createGbifLogMessage(ProcessContext context, LogEvent event, String messageText) {
    GbifLogMessage message = new GbifLogMessage();
    if (context != null) {
      try {
        Long dataProviderId = (Long) context.get(getContextKeyDataProviderId(), Long.class, false);
        Long dataResourceId = (Long) context.get(getContextKeyDataResourceId(), Long.class, false);
        Long occurrenceId = (Long) context.get(getContextKeyOccurrenceId(), Long.class, false);
        message.setDataProviderId(dataProviderId);
        message.setDataResourceId(dataResourceId);
        message.setOccurrenceId(occurrenceId);
        LogGroup logGroup = (LogGroup) context.get(getContextKeyLogGroup(), LogGroup.class, false);
        message.setLogGroup(logGroup);
      } catch (Exception e) {
        logger.debug(e.getMessage(), e);
      }
    } else {
      logger.debug("Supplied ProcessContext is null, omitting properties from log message.");
    }
    message.setEvent(event);
    message.setMessage(messageText);

    return message;
  }

  public GbifLogMessage createGbifLogMessage(LogGroup logGroup, LogEvent event) {
    GbifLogMessage message = new GbifLogMessage();
    message.setLogGroup(logGroup);
    message.setEvent(event);
    return message;
  }

  public boolean endLogGroup(LogGroup logGroup) {
    if (logGroup != null && !logGroup.isEnded()) {
      logger.info(createGbifLogMessage(logGroup, LogEvent.LOGGROUP_CLOSE));
      logGroup.end();
    }
    return true;
  }

  public synchronized LogGroup startLogGroup() {
    return new LogGroup();
  }

  /**
   * @return the contextKeyDataProviderId
   */
  public String getContextKeyDataProviderId() {
    return contextKeyDataProviderId;
  }

  /**
   * @param contextKeyDataProviderId the contextKeyDataProviderId to set
   */
  public void setContextKeyDataProviderId(String contextKeyDataProviderId) {
    this.contextKeyDataProviderId = contextKeyDataProviderId;
  }

  /**
   * @return the contextKeyDataResourceId
   */
  public String getContextKeyDataResourceId() {
    return contextKeyDataResourceId;
  }

  /**
   * @param contextKeyDataResourceId the contextKeyDataResourceId to set
   */
  public void setContextKeyDataResourceId(String contextKeyDataResourceId) {
    this.contextKeyDataResourceId = contextKeyDataResourceId;
  }

  /**
   * @return the contextKeyLogGroup
   */
  public String getContextKeyLogGroup() {
    return contextKeyLogGroup;
  }

  /**
   * @param contextKeyLogGroup the contextKeyLogGroup to set
   */
  public void setContextKeyLogGroup(String contextKeyLogGroup) {
    this.contextKeyLogGroup = contextKeyLogGroup;
  }

  /**
   * @return Returns the contextKeyOccurrenceId.
   */
  public String getContextKeyOccurrenceId() {
    return contextKeyOccurrenceId;
  }

  /**
   * @param contextKeyOccurrenceId The contextKeyOccurrenceId to set.
   */
  public void setContextKeyOccurrenceId(String contextKeyOccurrenceId) {
    this.contextKeyOccurrenceId = contextKeyOccurrenceId;
  }
}
