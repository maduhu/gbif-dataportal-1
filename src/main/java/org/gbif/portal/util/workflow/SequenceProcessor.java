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
package org.gbif.portal.util.workflow;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A simple workflow that is initialised with a List of Activities 
 * which are processed in sequence
 * 
 * The context 
 * 
 * @author trobertson 
 * [based on the article by Steve Dodge: http://www.javaworld.com/javaworld/jw-04-2005/jw-0411-spring.html]
 */
public class SequenceProcessor extends BaseProcessor {
	
	/**
	 * Logger
	 */
	private static Log log = LogFactory.getLog(SequenceProcessor.class);
	
	/**
	 * To instantiate for the Processor
	 */
	protected String processContextName;
	
	/**
	 * The timestamp log level you wish to use
	 * null = no debug level
	 * "DEBUG" = log at debug level
	 * "INFO" = log at info level
	 * "WARN" = log at warn level
	 * "ERROR" = log at error level 
	 */
	protected String profileLevel = null;
	
	/**
	 * Minimum length for an activity to log
	 */
	protected int profileMinimumLength = 0;
	
	/**
	 * Starts the workflow with no seeddata and no intial context
	 * @see SequenceProcessor.doActivities(ProcessContext parentContext, Object seedData)
	 */
	public void doActivities() throws Exception {
		doActivities(null);
	}
	
	/**
	 * Instanciates a new context.
	 * Loads in any parent context data.
	 * Loads in any seedData
	 * Loops over the activities executing them
	 * 
	 * @see org.gbif.portal.util.workflow.Processor#doActivities(org.gbif.portal.util.workflow.ProcessContext, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext doActivities(Map seedData) throws Exception {
		//retrieve a new instance of the Workflow ProcessContext
		ProcessContext context = null;

		try {
			log.debug(getBeanName() + " processor is running...");
			
			
			// profile("Workflow starting");
			
			context = createContext();			

			// load any seed data
			if (seedData != null) {
				context.putAll(seedData);
			}
			
			for (Activity activity : (List<Activity>)getActivities()) {
				/*
				This for some reason does not work with commons logging
				if (log.isDebugEnabled()) {
					log.debug("Running activity:" + activity.getBeanName()
							+ " using arguments:" + context);
				}
				*/
				long time = System.currentTimeMillis();
				try {
					context = activity.execute(context);

				} catch (Throwable th) {
					ErrorHandler errorHandler = activity.getErrorHandler();
					if (errorHandler == null) {
						log.debug("No error handler for this action, running the default errorhandler and aborting processing...");
						getDefaultErrorHandler().handleError(context, th);
						break;
					} else {
						log.debug("Running error handler and continuing...");
						errorHandler.handleError(context, th);
					}
				}
				profile(activity.getClass().getSimpleName(), (System.currentTimeMillis() - time));
				//ensure its ok to continue the process
				if (processShouldStop(context, activity))
					break;
			}			
		} catch (Exception e) {
			log.info("Unhandled error caught in the workflow: Running the default error handler and finishing", e);
			try {
				getDefaultErrorHandler().handleError(context, e);
			} catch (Exception e1) {
				log.error("Default error handler for workflow threw exception", e1);
			}
		}
				
		return context;
	}
	
	/**
	 * Logs the profile message if profiling is configured
	 * @param message To log
	 * @param timeInMillis 
	 */
	protected void profile(String activity, Long length) {
		if (profileLevel != null && length >= profileMinimumLength) {
			profile(activity + " took " + length + " msecs");
		}
	}

	/**
	 * Logs the profile message if profiling is configured
	 * @param message To log
	 * @param timeInMillis 
	 */
	protected void profile(String message) {
		if (profileLevel != null) {
			if (profileLevel.equalsIgnoreCase("DEBUG")) {
				log.debug(message);
			} else if (profileLevel.equalsIgnoreCase("INFO")) {
				log.info(message);
			} else if (profileLevel.equalsIgnoreCase("WARN")) {
				log.warn(message);
			} else if (profileLevel.equalsIgnoreCase("ERROR")) {
				log.error(message);
			}
		}
	}

	/**
	 * Determine if the process should stop
	 * 
	 * @param context the current process context
	 * @param activity the current activity in the iteration
	 */
	protected boolean processShouldStop(ProcessContext context, Activity activity) {
		if (context != null && context.isStopProcess()) {
			log.info("Interrupted workflow as requested by:"
					+ activity.getBeanName());
			return true;
		}
		return false;
	}

	/**
	 * Using the spring factory, creates a context of the type configured with
	 * @return A context created as specified in the configuration
	 */
	protected ProcessContext createContext() {
		return (ProcessContext) getBeanFactory().getBean(processContextName);
	}

	/**
	 * @return Returns the processContextName.
	 */
	public String getProcessContextName() {
		return processContextName;
	}

	/**
	 * @param processContextName The processContextName to set.
	 */
	public void setProcessContextName(String processContextName) {
		this.processContextName = processContextName;
	}

	/**
	 * @return Returns the profileLevel.
	 */
	public String getProfileLevel() {
		return profileLevel;
	}

	/**
	 * @param profileLevel The profileLevel to set.
	 */
	public void setProfileLevel(String profileLevel) {
		this.profileLevel = profileLevel;
	}

	/**
	 * @return the profileMinimumLength
	 */
	public int getProfileMinimumLength() {
		return profileMinimumLength;
	}

	/**
	 * @param profileMinimumLength the profileMinimumLength to set
	 */
	public void setProfileMinimumLength(int profileMinimumLength) {
		this.profileMinimumLength = profileMinimumLength;
	}
}