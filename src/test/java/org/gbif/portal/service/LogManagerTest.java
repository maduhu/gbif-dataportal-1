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

import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.util.SearchConstraints;

/**
 * Junit tests for TaxonomyManager implementations.
 * 
 * @see TaxonomyManager
 * 
 * @author dmartin
 */
public class LogManagerTest extends AbstractServiceTest {

	/* DON'T SPAM PROVIDERS
	public void testCreateOccurrenceFeedbackMessage() throws Exception {
		LogManager logManager = (LogManager) getBean("logManager");
		LogGroup logGroup = logManager.startLogGroup();
		GbifLogMessage message = logManager.createOccurrenceFeedbackMessage(logGroup, "1", "1", "This is a test message - please ignore");
		logger.info(message);
		logManager.endLogGroup(logGroup);
	}	
	*/
	
	public void testFindLogMessages() throws Exception {
		LogManager logManager = (LogManager) getBean("logManager");
		try {
			SearchResultsDTO messages = logManager.findLogMessagesFor(null, null, null, null, null, null, null, null, null, null, null, null, new SearchConstraints(0,10));
			logger.info("number of messages:" + messages.size());
		} catch (Exception e) {
			//ignore
		}
	}	
}