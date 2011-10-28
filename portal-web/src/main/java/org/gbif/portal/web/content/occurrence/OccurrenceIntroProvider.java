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
package org.gbif.portal.web.content.occurrence;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.service.OccurrenceManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.web.content.ContentProvider;
import org.gbif.portal.web.content.ContentView;
/**
 * Occurrence Introduction Details content provider
 * This will add the occurrence statistics to the model ready for display  
 *
 * @author Dave Martin
 */
public class OccurrenceIntroProvider implements ContentProvider {

	protected static Log log = LogFactory.getLog(OccurrenceIntroProvider.class);
	/** Managers */
	protected OccurrenceManager occurrenceManager;
	
	/**
	 * Adds the occurrence count to the request.
	 * Should there be an error getting the count, it will be handled here 
	 * and the count will be returned as 0
	 */
	public void addContent(ContentView cc, HttpServletRequest request, HttpServletResponse response) {
		try {
			int totalOccurrenceRecords = occurrenceManager.getTotalOccurrenceRecordCount();
			int totalGeoreferencedRecords = occurrenceManager.getTotalGeoreferencedOccurrenceRecordCount();
			cc.addObject("totalOccurrenceRecords", totalOccurrenceRecords);
			cc.addObject("totalGeoreferencedOccurrenceRecords", totalGeoreferencedRecords);   
		} catch (ServiceException e) {
			// flag in the logs
			log.error("Occurrence count cannot be found, setting to 0", e);
			cc.addObject("totalOccurrenceRecords", 0);
			cc.addObject("totalGeoreferencedOccurrenceRecords", 0);   
		}
	}

	/**
	 * @param occurrenceManager The occurrenceManager to set.
	 */
	public void setOccurrenceManager(OccurrenceManager occurrenceManager) {
		this.occurrenceManager = occurrenceManager;
	}
}