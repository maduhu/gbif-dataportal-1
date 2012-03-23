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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gbif.portal.dao.RawOccurrenceRecordDAO;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * An activity that will page through the raw taxonomy for a resource access point
 * and stick the K,P,C,O,F,G,SN to the context and start a child workflow
 * @author trobertson
 */
public class DataResourceInRORPagingActivity extends BaseActivity implements
		Activity {
	/**
	 * The dao 
	 */
	protected RawOccurrenceRecordDAO rawOccurrenceRecordDAO;
	
	/**
	 * Context Keys
	 */
	protected String contextKeyResourceAccessPointId = "resourceAccessPointId";
	protected String contextKeyDataResourceId = "dataResourceId";
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		Long resourceAccessPointId = (Long) context.get(getContextKeyResourceAccessPointId(), Long.class, true);
		
		logger.info("Determining DataResources for resourceAccessPointID:" + resourceAccessPointId);
		List<Long> resourceIds = rawOccurrenceRecordDAO.getDataResourceIdsFor(resourceAccessPointId);
		
		logger.info("Resource access point has " + resourceIds.size() + " DataResources");
		long time = System.currentTimeMillis();
		for (Long resourceId : resourceIds) {
			
			Map<String, Object> seed = new HashMap<String, Object>();
			seed.put(getContextKeyDataResourceId(), resourceId);
			
			logger.info("Extracting the dataResourceId[" + resourceId + "]");
			
			time = System.currentTimeMillis();
			// launch the workflow to handle the classification
			launchWorkflow(context, seed);
			logger.info("Finished extracting the dataResourceId[" + resourceId + "] in [" +((1 + System.currentTimeMillis() - time)/1000) + " secs]");
			
		}
		return context;
	}

	public RawOccurrenceRecordDAO getRawOccurrenceRecordDAO() {
		return rawOccurrenceRecordDAO;
	}

	public void setRawOccurrenceRecordDAO(
			RawOccurrenceRecordDAO rawOccurrenceRecordDAO) {
		this.rawOccurrenceRecordDAO = rawOccurrenceRecordDAO;
	}

	public String getContextKeyResourceAccessPointId() {
		return contextKeyResourceAccessPointId;
	}

	public void setContextKeyResourceAccessPointId(
			String contextKeyResourceAccessPointId) {
		this.contextKeyResourceAccessPointId = contextKeyResourceAccessPointId;
	}

	public String getContextKeyDataResourceId() {
		return contextKeyDataResourceId;
	}

	public void setContextKeyDataResourceId(String contextKeyDataResourceId) {
		this.contextKeyDataResourceId = contextKeyDataResourceId;
	}
}
