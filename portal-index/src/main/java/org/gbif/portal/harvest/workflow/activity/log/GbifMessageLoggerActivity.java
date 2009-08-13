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
package org.gbif.portal.harvest.workflow.activity.log;

import java.text.MessageFormat;
import java.util.List;

import org.apache.log4j.Level;
import org.gbif.portal.dao.DataResourceDAO;
import org.gbif.portal.dao.ResourceAccessPointDAO;
import org.gbif.portal.model.DataResource;
import org.gbif.portal.model.ResourceAccessPoint;
import org.gbif.portal.util.log.GbifLogMessage;
import org.gbif.portal.util.log.GbifLogUtils;
import org.gbif.portal.util.log.LogEvent;
import org.gbif.portal.util.log.LogGroup;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * Scientific name from parts generates a scientific name in the context if
 * the parts are present but the full name is absent
 * @author Donald Hobern
 */
public class GbifMessageLoggerActivity extends BaseActivity implements Activity {	
	/**
	 * Context Keys
	 */
	protected String contextKeyDataProviderId;
	protected String contextKeyDataResourceId;
	protected String contextKeyResourceAccessPointId;
	protected String contextKeyLogGroup;
	protected String contextKeyRecordCount;
	protected String event;
	protected String level;
	protected String messageText;
	/** These are either plain string or context keys, in which case they will be resolved */ 
	protected List<String> messageTextArguments;
	protected String restricted;
	protected String endLogGroup;
	protected boolean countOnly = false;
	
	protected GbifLogUtils gbifLogUtils;

	protected DataResourceDAO dataResourceDAO;
	protected ResourceAccessPointDAO resourceAccessPointDAO;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {

		Long dataProviderId = (Long) context.get(getContextKeyDataProviderId(), Long.class, false);
		Long dataResourceId = (Long) context.get(getContextKeyDataResourceId(), Long.class, false);
		if (dataResourceId == null) {
			Long resourceAccessPointId = ((Long) context.get(getContextKeyResourceAccessPointId(), Long.class, false));
			if (resourceAccessPointId != 0) {
				ResourceAccessPoint rap = resourceAccessPointDAO.getById(resourceAccessPointId);
				if (rap != null) {
					dataResourceId = rap.getDataResourceId();
					context.put(getContextKeyDataResourceId(), dataResourceId);
					if (dataProviderId == null) {
						dataProviderId = rap.getDataProviderId();
						context.put(getContextKeyDataProviderId(), dataProviderId);
					}
				}
			}
		} else if (dataProviderId == null) {
			DataResource dr = dataResourceDAO.getById(dataResourceId);
			if (dr != null) {
				dataProviderId = dr.getDataProviderId();
				context.put(getContextKeyDataProviderId(), dataProviderId);
			}
		}

		LogGroup logGroup = ((LogGroup) context.get(getContextKeyLogGroup(), LogGroup.class, false));
		if (logGroup == null) {
			logGroup = gbifLogUtils.startLogGroup();
			context.put(getContextKeyLogGroup(), logGroup);
		}

		String logMessageText = messageText;
		if(messageTextArguments!=null && !messageTextArguments.isEmpty()){
			Object[] messageArgs =  new Object[messageTextArguments.size()];
			for(int i=0; i<messageArgs.length; i++){
				String messageArg = messageTextArguments.get(i);
				Object contextObj = context.get(messageArg);
				if(contextObj!=null){
					messageArgs[i] = contextObj;
				} else {
					messageArgs[i] = messageArg;
				}
			}
			logMessageText = MessageFormat.format(messageText, messageArgs);
		}
		
		LogEvent logEvent = LogEvent.get(event);
		GbifLogMessage gbifMessage 
			= gbifLogUtils.createGbifLogMessage(context, 
							 (logEvent == null) ? LogEvent.UNKNOWN : logEvent, 
									 logMessageText); 
		gbifMessage.setCountOnly(countOnly);
		
		if (restricted != null) {
			gbifMessage.setRestricted(new Boolean(restricted));
		}
		
		if(contextKeyRecordCount!=null){
			Integer count = ((Integer) context.get(getContextKeyRecordCount(), Integer.class, false));
			if(count!=null){
				gbifMessage.setCount(count);
			}
		}
		
		if (endLogGroup != null && new Boolean(endLogGroup)) {
			gbifLogUtils.endLogGroup(logGroup);
			context.remove(getContextKeyLogGroup());
		}

		Level logLevel;
		if (level == null) {
			logLevel = Level.INFO;
		} else {
			logLevel = Level.toLevel(level);
		}

		if (logLevel.equals(Level.TRACE)) {
			logger.trace(gbifMessage);
		} else if (logLevel.equals(Level.DEBUG)) {
			logger.debug(gbifMessage);
		} else if (logLevel.equals(Level.INFO)) {
			logger.info(gbifMessage);
		} else if (logLevel.equals(Level.WARN)) {
			logger.warn(gbifMessage);
		} else if (logLevel.equals(Level.ERROR)) {
			logger.error(gbifMessage);
		} else if (logLevel.equals(Level.FATAL)) {
			logger.fatal(gbifMessage);
		}		
		
		return context;
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
	 * @return the contextKeyResourceAccessPointId
	 */
	public String getContextKeyResourceAccessPointId() {
		return contextKeyResourceAccessPointId;
	}

	/**
	 * @param contextKeyResourceAccessPointId the contextKeyResourceAccessPointId to set
	 */
	public void setContextKeyResourceAccessPointId(String contextKeyResourceAccessPointId) {
		this.contextKeyResourceAccessPointId = contextKeyResourceAccessPointId;
	}

	/**
	 * @return the event
	 */
	public String getEvent() {
		return event;
	}

	/**
	 * @param event the event to set
	 */
	public void setEvent(String event) {
		this.event = event;
	}

	/**
	 * @return the message
	 */
	public String getMessageText() {
		return messageText;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	/**
	 * @return the restricted
	 */
	public String getRestricted() {
		return restricted;
	}

	/**
	 * @param restricted the restricted to set
	 */
	public void setRestricted(String restricted) {
		this.restricted = restricted;
	}

	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * @return the gbifLogUtils
	 */
	public GbifLogUtils getGbifLogUtils() {
		return gbifLogUtils;
	}

	/**
	 * @param gbifLogUtils the gbifLogUtils to set
	 */
	public void setGbifLogUtils(GbifLogUtils gbifLogUtils) {
		this.gbifLogUtils = gbifLogUtils;
	}

	/**
	 * @return the endLogGroup
	 */
	public String getEndLogGroup() {
		return endLogGroup;
	}

	/**
	 * @param endLogGroup the endLogGroup to set
	 */
	public void setEndLogGroup(String endLogGroup) {
		this.endLogGroup = endLogGroup;
	}

	/**
	 * @return the dataResourceDAO
	 */
	public DataResourceDAO getDataResourceDAO() {
		return dataResourceDAO;
	}

	/**
	 * @param dataResourceDAO the dataResourceDAO to set
	 */
	public void setDataResourceDAO(DataResourceDAO dataResourceDAO) {
		this.dataResourceDAO = dataResourceDAO;
	}

	/**
	 * @return the resourceAccessPointDAO
	 */
	public ResourceAccessPointDAO getResourceAccessPointDAO() {
		return resourceAccessPointDAO;
	}

	/**
	 * @param resourceAccessPointDAO the resourceAccessPointDAO to set
	 */
	public void setResourceAccessPointDAO(
			ResourceAccessPointDAO resourceAccessPointDAO) {
		this.resourceAccessPointDAO = resourceAccessPointDAO;
	}

	/**
	 * @param contextKeyRecordCount the contextKeyRecordCount to set
	 */
	public void setContextKeyRecordCount(String contextKeyRecordCount) {
		this.contextKeyRecordCount = contextKeyRecordCount;
	}

	/**
	 * @return the contextKeyRecordCount
	 */
	public String getContextKeyRecordCount() {
		return contextKeyRecordCount;
	}

	/**
	 * @param messageTextArguments the messageTextArguments to set
	 */
	public void setMessageTextArguments(List<String> messageTextArguments) {
		this.messageTextArguments = messageTextArguments;
	}

	/**
   * @param countOnly the countOnly to set
   */
  public void setCountOnly(boolean countOnly) {
  	this.countOnly = countOnly;
  }
}