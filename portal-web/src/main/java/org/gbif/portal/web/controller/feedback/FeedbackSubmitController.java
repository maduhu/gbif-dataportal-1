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
package org.gbif.portal.web.controller.feedback;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.util.EntityType;
import org.gbif.portal.model.log.User;
import org.gbif.portal.service.LogManager;
import org.gbif.portal.util.log.GbifLogMessage;
import org.gbif.portal.util.log.LogGroup;
import org.gbif.portal.web.controller.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * A Controller for dealing with the submition of user feedback
 *
 * @author Tim Robertson
 * @author jcuadra
 */
public class FeedbackSubmitController extends RestController {
	/**
	 * Logger
	 */
	protected Log logger = LogFactory.getLog(FeedbackSubmitController.class);
	
	/** The Managers */
	LogManager logManager;
	
	/** The request keys **/
	protected String feedbackOnURLRequestKey = "feedbackOnURL";
	protected String occurrenceRecordKeyRequestKey = "occurrenceRecordKey";
	protected String taxonConceptKeyRequestKey = "taxonConceptKey";
	protected String dataProviderKeyRequestKey = "dataProviderKey";
	protected String dataResourceKeyRequestKey = "dataResourceKey";
	protected String submitterNameRequestKey = "submitterName";
	protected String submitterEmailRequestKey = "submitterEmail";
	protected String feedbackDetailsRequestKey = "feedbackDetails";
	
	/** The view names */
	protected String feedbackSuccessViewName = "feedbackSuccessView";
	protected String feedbackVerificationViewName = "feedbackVerificationView";
	
	/** The request keys **/
	protected String conceptTypeRequestKey = "conceptType";
	protected String conceptKeyRequestKey = "conceptKey";
	
	/**
	 * @see org.gbif.portal.web.controller.RestController#handleRequest(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView handleRequest(Map<String, String> properties, HttpServletRequest request, HttpServletResponse response) throws Exception {
		/*
		logger.debug("URL: " + request.getParameter(feedbackOnURLRequestKey));
		logger.debug("OR: " + request.getParameter(occurrenceRecordKeyRequestKey));
		logger.debug("TC: " + request.getParameter(taxonConceptKeyRequestKey));
		logger.debug("DP: " + request.getParameter(dataProviderKeyRequestKey));
		logger.debug("DR: " + request.getParameter(dataResourceKeyRequestKey));
		logger.debug("Name: " + request.getParameter(submitterNameRequestKey));
		logger.debug("Email: " + request.getParameter(submitterEmailRequestKey));
		logger.debug("Detail: " + request.getParameter(feedbackDetailsRequestKey));
		*/

		String conceptTypeAsString = properties.get(conceptTypeRequestKey);
		String conceptKey = properties.get(conceptKeyRequestKey);
		EntityType conceptType = EntityType.entityTypesByName.get(conceptTypeAsString);
		
		String messageDetail = request.getParameter(feedbackDetailsRequestKey);
		String userName = request.getParameter(submitterNameRequestKey);
		String userEmail = request.getParameter(submitterEmailRequestKey);
		
		LogGroup logGroup = logManager.startLogGroup();
		try {
			GbifLogMessage message = null;
			
			String userKey = logManager.getUserKeyFor(userName, userEmail);
			
			//creates the log message
			if (conceptType.equals(EntityType.TYPE_TAXON)) {
				logger.debug("User feeding back information for taxon");
				message = logManager.createTaxonFeedbackMessage(logGroup, userKey, conceptKey, messageDetail);
				
			} else if(conceptType.equals(EntityType.TYPE_DATA_RESOURCE)) {
				logger.debug("User feeding back information for resource");
				message = logManager.createResourceFeedbackMessage(logGroup, userKey, conceptKey, messageDetail);
				
			} else if(conceptType.equals(EntityType.TYPE_OCCURRENCE)) {
				logger.debug("User feeding back information for occurrence");
				message = logManager.createOccurrenceFeedbackMessage(logGroup, userKey, conceptKey, messageDetail);
			}
			
			//log the message
			if (message!=null) {
				logger.info(message);
			}			
			
			//checks if user is verified, and decide if a verification or feedback msg should be sent
			if(logManager.isVerifiedUser(userKey)) {
				logManager.sendFeedbackOrVerificationMessages(message, true);
				return new ModelAndView(feedbackSuccessViewName, feedbackOnURLRequestKey, request.getParameter(feedbackOnURLRequestKey));
			}
			else {
				logManager.sendFeedbackOrVerificationMessages(message, false);
				return new ModelAndView(feedbackVerificationViewName, feedbackOnURLRequestKey, request.getParameter(feedbackOnURLRequestKey));
			}

		} finally {
			logManager.endLogGroup(logGroup);
		}
	}

	/**
	 * @return Returns the logManager.
	 */
	public LogManager getLogManager() {
		return logManager;
	}

	/**
	 * @param logManager The logManager to set.
	 */
	public void setLogManager(LogManager logManager) {
		this.logManager = logManager;
	}
}