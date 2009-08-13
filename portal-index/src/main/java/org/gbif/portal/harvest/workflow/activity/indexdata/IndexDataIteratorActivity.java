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
package org.gbif.portal.harvest.workflow.activity.indexdata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gbif.portal.dao.IndexDataDAO;
import org.gbif.portal.model.IndexData;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * Gets the range values for the non finished concepts for the RAP,
 * and iterates over them calling the child process
 * @author trobertson
 */
public class IndexDataIteratorActivity extends BaseActivity {
	/**
	 * The context keys
	 */
	protected String contextKeyResourceAccessPointId;
	protected String contextKeyLowerLimit;
	protected String contextKeyUpperLimit;
	protected String contectKeyRecordProcessedCount = "recordProcessedCount";
	
	/**
	 * DAOs
	 */
	protected IndexDataDAO indexDataDAO;
	
	
	
	/**
     * @see org.gbif.portal.util.workflow.BaseMapContextActivity#doExecute(org.gbif.portal.util.workflow.MapContext)
     */
    @SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
    	long resourceAccessPointId = (Long) context.get(getContextKeyResourceAccessPointId(), Long.class, true);
    	List<IndexData> data = indexDataDAO.findByResourceAccessPointId(resourceAccessPointId);
    	
    	logger.debug("Iterating over " + data.size() + " concepts...");
    	for (IndexData dataItem : data) {
    		indexDataDAO.setStartAsNow(dataItem.getId());
    		Map<String, Object> seed  = new HashMap<String, Object>();
    		seed.put(getContextKeyLowerLimit(), dataItem.getLowerLimit());
    		seed.put(getContextKeyUpperLimit(), dataItem.getUpperLimit());
    		// Set the record count to zero
    		context.put(contectKeyRecordProcessedCount, new Integer(0));
    		ProcessContext result = launchWorkflow(context, seed);
    		
    		// we update the finished date only if it appears that we got some records.
    		// If we get a valid document back that is empty of records, but tells us some diagnostics 
    		// (e.g. their DB is down) then we need to not set this but continue
    		// This is making the terrible (TODO) assumption that the count can be retreived as the contectKeyRecordProcessedCount 
    		// at the time of writing, this is correct, but this may change (cringe)
    		if (!result.containsKey(contectKeyRecordProcessedCount)) {
    			logger.warn("No record processed count found in the Workflow Context.  NOT setting the Index Data range [id: " + dataItem.getId() + "] as finished");
    		} else {
    			Integer count = (Integer)result.get(contectKeyRecordProcessedCount, Integer.class, false);
    			if (count == null) {
    				logger.warn("No record processed count found in the Workflow Context.  NOT setting the Index Data range [id: " + dataItem.getId() + "] as finished");
    			} else if (count <= 0) {
    				logger.warn("No record processed count is " + count + ".  NOT setting the Index Data range [id: " + dataItem.getId() + "] as finished");
    			} else {
    				logger.info("The record processed count is " + count +".  Setting the Index Data range [id: " + dataItem.getId() + "] as finished");
    				indexDataDAO.setFinishedAsNow(dataItem.getId());
    			}
    		}
    		
    	}
		return context;		
	}

	/**
	 * @return Returns the contextKeyLowerLimit.
	 */
	public String getContextKeyLowerLimit() {
		return contextKeyLowerLimit;
	}

	/**
	 * @param contextKeyLowerLimit The contextKeyLowerLimit to set.
	 */
	public void setContextKeyLowerLimit(String contextKeyLowerLimit) {
		this.contextKeyLowerLimit = contextKeyLowerLimit;
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

	/**
	 * @return Returns the contextKeyUpperLimit.
	 */
	public String getContextKeyUpperLimit() {
		return contextKeyUpperLimit;
	}

	/**
	 * @param contextKeyUpperLimit The contextKeyUpperLimit to set.
	 */
	public void setContextKeyUpperLimit(String contextKeyUpperLimit) {
		this.contextKeyUpperLimit = contextKeyUpperLimit;
	}

	/**
	 * @return Returns the indexDataDAO.
	 */
	public IndexDataDAO getIndexDataDAO() {
		return indexDataDAO;
	}

	/**
	 * @param indexDataDAO The indexDataDAO to set.
	 */
	public void setIndexDataDAO(IndexDataDAO indexDataDAO) {
		this.indexDataDAO = indexDataDAO;
	}

	/**
	 * @return Returns the contectKeyRecordProcessedCount.
	 */
	public String getContectKeyRecordProcessedCount() {
		return contectKeyRecordProcessedCount;
	}

	/**
	 * @param contectKeyRecordProcessedCount The contectKeyRecordProcessedCount to set.
	 */
	public void setContectKeyRecordProcessedCount(
			String contectKeyRecordProcessedCount) {
		this.contectKeyRecordProcessedCount = contectKeyRecordProcessedCount;
	}
}