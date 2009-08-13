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
package org.gbif.portal.harvest.workflow.activity.occurrence;

import org.gbif.portal.model.RawOccurrenceRecord;
import org.gbif.portal.util.log.GbifLogMessageDAO;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * @author trobertson
 */
public class DeleteOccurrenceExtractMessages extends BaseActivity implements
		Activity {
	
	/**
	 * DAOs
	 */
	protected GbifLogMessageDAO gbifLogMessageDAO;
	protected String contextKeyRawOccurrenceRecord;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		RawOccurrenceRecord ror = (RawOccurrenceRecord) context.get(getContextKeyRawOccurrenceRecord(), RawOccurrenceRecord.class, true);
		int count = gbifLogMessageDAO.deleteExtractEventsByOccurrenceId(ror.getId());
		logger.info("Deleted [" + count +"] extract messages for occurrence record: " + ror.getId());
		return context;
	}

	/**
	 * @return Returns the contextKeyRawOccurrenceRecord.
	 */
	public String getContextKeyRawOccurrenceRecord() {
		return contextKeyRawOccurrenceRecord;
	}

	/**
	 * @param contextKeyRawOccurrenceRecord The contextKeyRawOccurrenceRecord to set.
	 */
	public void setContextKeyRawOccurrenceRecord(
			String contextKeyRawOccurrenceRecord) {
		this.contextKeyRawOccurrenceRecord = contextKeyRawOccurrenceRecord;
	}

	/**
	 * @return Returns the gbifLogMessageDAO.
	 */
	public GbifLogMessageDAO getGbifLogMessageDAO() {
		return gbifLogMessageDAO;
	}

	/**
	 * @param gbifLogMessageDAO The gbifLogMessageDAO to set.
	 */
	public void setGbifLogMessageDAO(GbifLogMessageDAO gbifLogMessageDAO) {
		this.gbifLogMessageDAO = gbifLogMessageDAO;
	}
}
