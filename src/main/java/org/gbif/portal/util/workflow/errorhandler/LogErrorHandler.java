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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.util.workflow.ErrorHandler;
import org.gbif.portal.util.workflow.ProcessContext;



/**
 * Simply logs the error
 * @author trobertson
 */
public class LogErrorHandler implements ErrorHandler {
	/**
	 * Logger
	 */
    protected static Log log = LogFactory.getLog(LogErrorHandler.class);
	
	/**
	 * Context key for the RequestConfiguration
	 */
	protected String contextKeyRequestConfiguration;    
	
	/**
	 * Context key for the Report
	 */
	protected String contextKeyReport;
	/**
	 * The name of this bean
	 */
    protected String beanName;
    
    /**
     * If the workflow should continue or not
     */
    protected boolean shouldStopOnError = true; 

    /**
     *  @see org.gbif.portal.util.workflow.ErrorHandler#handleError(org.gbif.workflow.ProcessContext, java.lang.Throwable)
     */
    public void handleError(ProcessContext processContext, Throwable th) {
    	log.error(th.getMessage(), th);
    	processContext.setStopProcess(shouldStopOnError);
    }

    /**
     * @see org.springframework.beans.factory.BeanNameAware#setBeanName(java.lang.String)
     */
    public void setBeanName(String beanName) {
        this.beanName = beanName;

    }

	/**
	 * @return Returns the contextKeyRequestConfiguration.
	 */
	public String getContextKeyRequestConfiguration() {
		return contextKeyRequestConfiguration;
	}

	/**
	 * @param contextKeyRequestConfiguration The contextKeyRequestConfiguration to set.
	 */
	public void setContextKeyRequestConfiguration(
			String contextKeyRequestConfiguration) {
		this.contextKeyRequestConfiguration = contextKeyRequestConfiguration;
	}

	/**
	 * @return Returns the contextKeyReport.
	 */
	public String getContextKeyReport() {
		return contextKeyReport;
	}

	/**
	 * @param contextKeyReport The contextKeyReport to set.
	 */
	public void setContextKeyReport(String contextKeyReport) {
		this.contextKeyReport = contextKeyReport;
	}

	/**
	 * @return Returns the shouldStopOnError.
	 */
	public boolean isShouldStopOnError() {
		return shouldStopOnError;
	}

	/**
	 * @param shouldStopOnError The shouldStopOnError to set.
	 */
	public void setShouldStopOnError(boolean shouldStopOnError) {
		this.shouldStopOnError = shouldStopOnError;
	}
}
