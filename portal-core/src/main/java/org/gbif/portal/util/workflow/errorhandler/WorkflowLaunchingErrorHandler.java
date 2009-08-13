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
package org.gbif.portal.util.workflow.errorhandler;

import java.util.HashSet;
import java.util.Set;

import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ErrorHandler;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * Launches the configured workflow if the error is in the configured Set of Error Types.
 * @author trobertson
 */
public class WorkflowLaunchingErrorHandler extends BaseActivity implements ErrorHandler {
	/**
	 * Set of Error classes that if the error is in, the workflow will launch
	 */
	protected Set<String> errorsWarrantingWorkflowLaunch = new HashSet<String>();
	
	/**
	 * Should the error not be in the set, this controls whether it should be swallowed
	 * or passed n
	 */
	protected boolean swallowOtherErrors = false;
	
	/**
	 * Launches the configured workflow 
	 * @see org.gbif.portal.util.workflow.ErrorHandler#handleError(org.gbif.portal.util.workflow.ProcessContext, java.lang.Throwable)
	 */
	public void handleError(ProcessContext context, Throwable th) throws Exception {
		String errorThrown = th.getClass().getCanonicalName();
		if (getErrorsWarrantingWorkflowLaunch().contains(errorThrown)) {
			logger.debug("Handling error: " + errorThrown);
			launchWorkflow(context, null);
			context.setStopProcess(true);
			
		} else if (swallowOtherErrors) {
			logger.info("Ignoring error [" + th.getClass() + "]");
		} else {
			if (th instanceof Exception) {
				throw ((Exception) th);
			}
			throw new Exception(th);
		}
	}

	/**
	 * Does nothing
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	public ProcessContext execute(ProcessContext context) throws Exception {
		logger.warn("An error handler cannot be used as an activity!");
		return context;
	}
	
	/**
	 * Does nothing, wihout calling super
	 * @see org.gbif.portal.util.workflow.BaseActivity#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * @return the errorsWarrantingWorkflowLaunch
	 */
	public Set<String> getErrorsWarrantingWorkflowLaunch() {
		return errorsWarrantingWorkflowLaunch;
	}

	/**
	 * @param errorsWarrantingWorkflowLaunch the errorsWarrantingWorkflowLaunch to set
	 */
	public void setErrorsWarrantingWorkflowLaunch(
			Set<String> errorsWarrantingWorkflowLaunch) {
		this.errorsWarrantingWorkflowLaunch = errorsWarrantingWorkflowLaunch;
	}

	/**
	 * @return the swallowOtherErrors
	 */
	public boolean isSwallowOtherErrors() {
		return swallowOtherErrors;
	}

	/**
	 * @param swallowOtherErrors the swallowOtherErrors to set
	 */
	public void setSwallowOtherErrors(boolean swallowOtherErrors) {
		this.swallowOtherErrors = swallowOtherErrors;
	}
}