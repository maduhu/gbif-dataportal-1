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
package org.gbif.portal.harvest.workflow.activity.dos;

import org.gbif.portal.dao.ResourceAccessPointDAO;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * An activity that will stop the workflow if it appears that the URL is going to be hit with a DOS
 * attack 
 *
 * @author trobertson
 */
public class DOSThrottleActivity extends BaseActivity {
	/**
	 * The context key for the endpoint url
	 */
	protected String contextKeyURL;
	
	/**
	 * The context key for the RAP id
	 */
	protected String contextKeyResourceAccessPointId;					 
	
	/**
	 * DAOs
	 */
	protected ResourceAccessPointDAO resourceAccessPointDAO;
	
    /**
     * @see org.gbif.portal.util.workflow.BaseMapContextActivity#doExecute(org.gbif.portal.util.workflow.MapContext)
     */
    @SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
    	String url = (String) context.get(getContextKeyURL(), String.class, true);
    	long id = (Long) context.get(getContextKeyResourceAccessPointId(), Long.class, true);
    	if (resourceAccessPointDAO.isURLBeingHarvested(url, id)) {
    		logger.warn("Cancelling workflow as URL is in use...");
    		launchWorkflow(context, null);
    		context.setStopProcess(true);
    	}
		return context;
    }

	/**
	 * @return Returns the contextKeyURL.
	 */
	public String getContextKeyURL() {
		return contextKeyURL;
	}

	/**
	 * @param contextKeyURL The contextKeyURL to set.
	 */
	public void setContextKeyURL(String contextKeyURL) {
		this.contextKeyURL = contextKeyURL;
	}

	/**
	 * @return Returns the resourceAccessPointDAO.
	 */
	public ResourceAccessPointDAO getResourceAccessPointDAO() {
		return resourceAccessPointDAO;
	}

	/**
	 * @param resourceAccessPointDAO The resourceAccessPointDAO to set.
	 */
	public void setResourceAccessPointDAO(
			ResourceAccessPointDAO resourceAccessPointDAO) {
		this.resourceAccessPointDAO = resourceAccessPointDAO;
	}

	/**
	 * @return Returns the contextKeyResourceAccessPointId.
	 */
	public String getContextKeyResourceAccessPointId() {
		return contextKeyResourceAccessPointId;
	}

	/**
	 * @param contextKeyResourceAccessPointId The contextKeyResourceAccessPointId to set.
	 */
	public void setContextKeyResourceAccessPointId(
			String contextKeyResourceAccessPointId) {
		this.contextKeyResourceAccessPointId = contextKeyResourceAccessPointId;
	}
}