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
package org.gbif.portal.harvest.workflow.activity.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * Using the format configured, takes a date from the context if it is found, formats it and 
 * puts it into the context under the new key.
 * 
 * @author trobertson
 */
public class FormatDateActivity extends BaseActivity {
	/**
	 * Context keys
	 */
	protected String contextKeyDate;
	protected String contextKeyNewDate;
	
	/**
	 * The format
	 */
	protected String format;
	
	
	/* (non-Javadoc)
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		Date date = (Date) context.get(getContextKeyDate(), Date.class, false);
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			String formatedDate = sdf.format(date);
			context.put(contextKeyNewDate, formatedDate);
		}
		return context;
	}


	/**
	 * @return Returns the contextKeyDate.
	 */
	public String getContextKeyDate() {
		return contextKeyDate;
	}


	/**
	 * @param contextKeyDate The contextKeyDate to set.
	 */
	public void setContextKeyDate(String contextKeyDate) {
		this.contextKeyDate = contextKeyDate;
	}


	/**
	 * @return Returns the contextKeyNewDate.
	 */
	public String getContextKeyNewDate() {
		return contextKeyNewDate;
	}


	/**
	 * @param contextKeyNewDate The contextKeyNewDate to set.
	 */
	public void setContextKeyNewDate(String contextKeyNewDate) {
		this.contextKeyNewDate = contextKeyNewDate;
	}


	/**
	 * @return Returns the format.
	 */
	public String getFormat() {
		return format;
	}


	/**
	 * @param format The format to set.
	 */
	public void setFormat(String format) {
		this.format = format;
	}
}
