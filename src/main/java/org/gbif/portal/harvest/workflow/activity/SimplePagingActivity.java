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

import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ContextCorruptException;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * Repeatedly calls the child process until the isFinished is true.
 * The finished flag is established by checking a concept in the context
 * It can be configured to stop after a certain number of iterations.
 *
 * @author trobertson
 */
public class SimplePagingActivity extends BaseActivity {
	/**
	 * The concept that must exist and be set to false to continue
	 */
	protected String contextKeyRecordsFinished;
	
	/**
	 * The context key to get the count to check to see if this has hit some configurable hard limit
	 * If this is null (default), then no hard limit is used
	 */
	protected String contextKeyLoopCount;
	
	/**
	 * The number of loops that this is allowed to perform.
	 * This is only used when the contextKeyLoopCount is not null
	 * Default for this maximum is 10
	 */
	protected int maximumLoopCountToPerform = 10;
	
    /**
	 * @see org.gbif.portal.util.workflow.BaseActivity#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		if (getContextKeyLoopCount() != null) {
			logger.warn("Simple paging activity has been configured with a maximum loop limit of " + getMaximumLoopCountToPerform());
		}
	}

	/**
     * @see org.gbif.portal.util.workflow.BaseMapContextActivity#doExecute(org.gbif.portal.util.workflow.MapContext)
     */
    @SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
    	while (shouldContinue(launchWorkflow(context, null))) {
    		// don't do this - it'll be done when the http connection
    		// goes into wait state anyways - i've tested it
    		// by profiling ;o)
    		//System.gc();
       	}
		return context;
    }
    
    /**
     * @param context To check the flag in
     * @return True if it should call again
     * @throws ContextCorruptException If the context has the flag in the wrong form
     */
    @SuppressWarnings("unchecked")
	protected boolean shouldContinue(ProcessContext context) throws ContextCorruptException {
		if (getContextKeyLoopCount() != null) {
			Integer loopCount = (Integer) context.get(getContextKeyLoopCount(), Integer.class, false);
			if (loopCount == null) {
				context.put(getContextKeyLoopCount(), 1);
			} else {
				loopCount = loopCount.intValue() + 1;
				context.put(getContextKeyLoopCount(), loopCount);
				if (loopCount>=getMaximumLoopCountToPerform()) {
					logger.info("Maximum loop count of "+ getMaximumLoopCountToPerform() +" has been reached - stopping");
					return false;
				}
			}
		}
    	
		logger.info("End of records: " + context.get(getContextKeyRecordsFinished(), String.class, false));
    	String flag = (String) context.get(getContextKeyRecordsFinished(), String.class, false);
    	if (flag != null && flag.equalsIgnoreCase("FALSE")) {
    		return true;
    	} else {
    		return false;
    	}
    }

	/**
	 * @return Returns the contextKeyRecordsFinished.
	 */
	public String getContextKeyRecordsFinished() {
		return contextKeyRecordsFinished;
	}

	/**
	 * @param contextKeyRecordsFinished The contextKeyRecordsFinished to set.
	 */
	public void setContextKeyRecordsFinished(String contextKeyRecordsFinished) {
		this.contextKeyRecordsFinished = contextKeyRecordsFinished;
	}

	/**
	 * @return the contextKeyLoopCount
	 */
	public String getContextKeyLoopCount() {
		return contextKeyLoopCount;
	}

	/**
	 * @param contextKeyLoopCount the contextKeyLoopCount to set
	 */
	public void setContextKeyLoopCount(String contextKeyLoopCount) {
		this.contextKeyLoopCount = contextKeyLoopCount;
	}

	/**
	 * @return the maximumLoopCountToPerform
	 */
	public int getMaximumLoopCountToPerform() {
		return maximumLoopCountToPerform;
	}

	/**
	 * @param maximumLoopCountToPerform the maximumLoopCountToPerform to set
	 */
	public void setMaximumLoopCountToPerform(int maximumLoopCountToPerform) {
		this.maximumLoopCountToPerform = maximumLoopCountToPerform;
	}

}