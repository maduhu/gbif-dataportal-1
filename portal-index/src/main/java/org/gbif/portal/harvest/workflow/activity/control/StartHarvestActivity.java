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
package org.gbif.portal.harvest.workflow.activity.control;

import java.util.Date;

import org.gbif.portal.dao.ResourceAccessPointDAO;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * Sets the params in the DB that are necessary when harvesting 
 * 
 * @author trobertson
 */
public class StartHarvestActivity extends BaseActivity {
	/**
	 * DAOs
	 */
	protected ResourceAccessPointDAO resourceAccessPointDAO;
	
	/**
	 * Context keys
	 */
	protected String contextKeyRAPId = "resourceAccessPointId";
	
	/* (non-Javadoc)
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	public ProcessContext execute(ProcessContext context) throws Exception {
		long id = (Long) context.get(contextKeyRAPId, Long.class, true);
		resourceAccessPointDAO.setStartHarvest(new Date(), id);
		return context;
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
}
