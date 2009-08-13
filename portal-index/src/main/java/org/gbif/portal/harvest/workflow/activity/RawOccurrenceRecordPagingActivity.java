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
package org.gbif.portal.harvest.workflow.activity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gbif.portal.dao.RawOccurrenceRecordDAO;
import org.gbif.portal.model.RawOccurrenceRecord;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * An activity that will page RawOccurrenceRecord with the values found in the 
 * context.
 * @author trobertson
 */
public class RawOccurrenceRecordPagingActivity extends BaseActivity implements
		Activity {
	/**
	 * The dao 
	 */
	protected RawOccurrenceRecordDAO rawOccurrenceRecordDAO;
	
	/**
	 * Context Keys
	 */
	protected String contextKeyRawOccurrenceRecord;
	protected String contextKeyPageFrom;
	protected String contextKeyDataResourceId = "dataResourceId";
	
	/**
	 * Page size defaults to 10000
	 */
	protected int pageSize = 10000;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	public ProcessContext execute(ProcessContext context) throws Exception {
		Long dataResourceId = (Long) context.get(getContextKeyDataResourceId(), Long.class, true);
		Date date = (Date) context.get(getContextKeyPageFrom(), Date.class, false);
		if (date==null) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.add(Calendar.MONTH, -24);
			date =  cal.getTime();
		}
    	if ("true".equalsIgnoreCase(System.getProperty("ignore-date-last-modified"))) {
    		logger.warn("Date last modified has been set to be ignored, and records from the last 24 months will be used");
			GregorianCalendar cal = new GregorianCalendar();
			cal.add(Calendar.MONTH, -24);
			date =  cal.getTime();
    	}		
		
		long startAt = 0;
		boolean hasMore = true;
		int total = 0;
		while (hasMore) {
			long time = System.currentTimeMillis();
			logger.info("Starting page with start id: " + startAt);
			List<RawOccurrenceRecord> records = rawOccurrenceRecordDAO.getCreatedOrModifiedSince(dataResourceId.longValue(), date, startAt, pageSize+1);
			total+=records.size();
			logger.debug("Results: " + records.size());			
			if (records.size() > 0) {
				hasMore = true;
				// remove the extra page record if there is a full page
				if (records.size() == pageSize+1) {
					records.remove(records.size()-1);
				}
				startAt = records.get(records.size()-1).getId();
				for (RawOccurrenceRecord ror : records) {
					logger.debug("Handling ROR id: " + ror.getId());
					Map<String, Object> seed = new HashMap<String, Object>();
					seed.put(getContextKeyRawOccurrenceRecord(), ror);
					launchWorkflow(context, seed);
				}
				
			} else {
				hasMore = false;
				logger.debug("No more results");
			}
			
			int display = total;
			if (pageSize<total) {
				display = pageSize;
			}
			
			logger.info("Page of [" + display + "] processed in " + ((System.currentTimeMillis()+1-time)/1000) + " secs");
			logger.info("Total processed: " + total);
		}
		return context;
	}

	/**
	 * @return the contextKeyPageFrom
	 */
	public String getContextKeyPageFrom() {
		return contextKeyPageFrom;
	}

	/**
	 * @param contextKeyPageFrom the contextKeyPageFrom to set
	 */
	public void setContextKeyPageFrom(String contextKeyPageFrom) {
		this.contextKeyPageFrom = contextKeyPageFrom;
	}

	/**
	 * @return the contextKeyRawOccurrenceRecord
	 */
	public String getContextKeyRawOccurrenceRecord() {
		return contextKeyRawOccurrenceRecord;
	}

	/**
	 * @param contextKeyRawOccurrenceRecord the contextKeyRawOccurrenceRecord to set
	 */
	public void setContextKeyRawOccurrenceRecord(
			String contextKeyRawOccurrenceRecord) {
		this.contextKeyRawOccurrenceRecord = contextKeyRawOccurrenceRecord;
	}

	/**
	 * @return the rawOccurrenceRecordDAO
	 */
	public RawOccurrenceRecordDAO getRawOccurrenceRecordDAO() {
		return rawOccurrenceRecordDAO;
	}

	/**
	 * @param rawOccurrenceRecordDAO the rawOccurrenceRecordDAO to set
	 */
	public void setRawOccurrenceRecordDAO(
			RawOccurrenceRecordDAO rawOccurrenceRecordDAO) {
		this.rawOccurrenceRecordDAO = rawOccurrenceRecordDAO;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getContextKeyDataResourceId() {
		return contextKeyDataResourceId;
	}

	public void setContextKeyDataResourceId(String contextKeyDataResourceId) {
		this.contextKeyDataResourceId = contextKeyDataResourceId;
	}

}
