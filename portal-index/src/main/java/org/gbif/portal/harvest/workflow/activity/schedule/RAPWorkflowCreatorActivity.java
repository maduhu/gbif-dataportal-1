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
package org.gbif.portal.harvest.workflow.activity.schedule;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.gbif.portal.dao.ResourceAccessPointDAO;
import org.gbif.portal.model.ResourceAccessPoint;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;
import org.gbif.portal.util.workflow.quartz.WorkflowLauncherJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.helpers.TriggerUtils;

/**
 * An activity to create a quartz job to launch a workflow that is based on the resource
 * access point.  Any other QRTZ triggers that exist for the same activity are deleted.
 * There can be only one trigger for a job in our scheduling.
 *
 * @author trobertson
 */
public class RAPWorkflowCreatorActivity extends BaseActivity {
	/**
	 * The scheduler
	 */
	protected Scheduler scheduler;
	
	/**
	 * The context key to use for the resource access point
	 */
	protected String contextKeyResourceAccessPointId;
	
	/**
	 * This is the parameter that should be used from the resource access
	 * point, to determine when the workflow should launch.  For example, 
	 * if you set this to "harvestInterval" then this activity would try and 
	 * read the property "harvestInterval" from the resource access point and 
	 * use that value.  It is assumed that the result is an int and represents the 
	 * days to use.
	 * Note that this can be overriden using the secondsFromNowToFire
	 */
	protected String rapParamForScheduleInDays;
	
	/**
	 * This is what the QRTZ job group and trigger group will become.
	 * Suggest using "harvest", "metadata", "inventory" etc...  
	 */
	protected String jobType;
	
	/**
	 * The workflow key in the PS to launch
	 */
	protected String workflowKeyToLaunch;
	
	/**
	 * If this is set, then the rapParamForScheduleInDays is IGNORED!!!
	 * This would be set for example to reschedule a 
	 */
	protected Long secondsFromNowToFire = null;
	
	/**
	 * DAOs
	 */
	protected ResourceAccessPointDAO resourceAccessPointDAO;
	
    /**
     * @see org.gbif.portal.util.workflow.BaseMapContextActivity#doExecute(org.gbif.portal.util.workflow.MapContext)
     */
    @SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
    	if ("true".equalsIgnoreCase(System.getProperty("manual"))) {
    		logger.info("Running in manual mode - no auto scheduling will occur");
    		return context;
    	}
    	
    	// both trigger and job get same details to keep it simple
    	String jobGroup = jobType;
    	String triggerGroup = jobGroup;
    	// both the job and trigger name get the same values
    	String jobName = "rap:" + (Long)context.get(contextKeyResourceAccessPointId, Long.class, true);
    	String triggerName = jobName;
    	
    	scheduler.deleteJob(jobName, jobGroup);
    	JobDetail jobDetail = scheduler.getJobDetail(jobName, jobGroup);
    	
    	if (jobDetail == null) {
    		jobDetail = new JobDetail(jobName,jobGroup,WorkflowLauncherJob.class);
        	// set the job data
        	// since we only do GBIF workflows, we just put in the GBIF namespace
        	JobDataMap jdm = new JobDataMap();
        	Map<String, Object> seed = new HashMap<String, Object>();
    		seed.put(contextKeyResourceAccessPointId, context.get(contextKeyResourceAccessPointId));    		
        	jdm.put(WorkflowLauncherJob.SEED_DATA_KEY, seed);
        	jdm.put(WorkflowLauncherJob.PROPERTY_STORE_KEY_WORKFLOW_KEY, getWorkflowKeyToLaunch());
        	List<String> namespaces = new LinkedList<String>();
        	namespaces.add("http://www.gbif.org/portal/index/1.0");
        	jdm.put(WorkflowLauncherJob.PROPERTY_STORE_NAMESPACES_KEY, namespaces);        	
        	jobDetail.setJobDataMap(jdm);
    	}
    	
		// remove any triggers
    	scheduler.unscheduleJob(triggerName, triggerGroup);
    	
    	Date toStart = null;
    	logger.info("secondsFromNowToFire: " + secondsFromNowToFire);
    	if (secondsFromNowToFire != null) {
    		toStart = new Date(System.currentTimeMillis() + (secondsFromNowToFire*1000));
    	} else {
    		// use RAP DAO to get the field
    		long id = (Long)context.get(contextKeyResourceAccessPointId, Long.class, true);
    		ResourceAccessPoint rap = resourceAccessPointDAO.getById(id);
    		
    		Object daysAsObject = PropertyUtils.getSimpleProperty(rap, rapParamForScheduleInDays);
    		if (daysAsObject == null) {
    			logger.warn("'" + rapParamForScheduleInDays + "' appears to be null for rap id[ " + id + "] and will not be scheduled");
    		} else if (daysAsObject instanceof Integer) {
    			int days = (Integer) daysAsObject;
    			if (days == 0) {
    				logger.warn("'" + rapParamForScheduleInDays + "' has a 0 value for rap id[ " + id + "] and will not be scheduled");    				
    			} else {
    				logger.info("Scheduling '" + rapParamForScheduleInDays + "' for rap id[" + id + "] for "+ days + " days");
    				Calendar calendar = new GregorianCalendar();
    				calendar.add(Calendar.DATE, days);
    				toStart = calendar.getTime();
    			}
    		} else {
    			logger.warn("'" + rapParamForScheduleInDays + "' has returned an " + daysAsObject + " and required an Int. Rap id[ " + id + "] and will not be scheduled");
    		}
    	}
    	
    	if (toStart != null) {
	    	Trigger trigger = new SimpleTrigger(triggerName, triggerGroup, toStart);
			trigger.setJobName(jobName);
			trigger.setJobGroup(jobGroup);
	    	logger.info("scheduling " + jobGroup + " " + jobName);
			scheduler.scheduleJob(jobDetail,trigger);
    	}
    	
    	return context;
    }
    static int i=0;
	/**
	 * @return Returns the jobType.
	 */
	public String getJobType() {
		return jobType;
	}

	/**
	 * @param jobType The jobType to set.
	 */
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	/**
	 * @return Returns the rapParamForScheduleInDays.
	 */
	public String getRapParamForScheduleInDays() {
		return rapParamForScheduleInDays;
	}

	/**
	 * @param rapParamForScheduleInDays The rapParamForScheduleInDays to set.
	 */
	public void setRapParamForScheduleInDays(String rapParamForScheduleInDays) {
		this.rapParamForScheduleInDays = rapParamForScheduleInDays;
	}

	/**
	 * @return Returns the scheduler.
	 */
	public Scheduler getScheduler() {
		return scheduler;
	}

	/**
	 * @param scheduler The scheduler to set.
	 */
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * @return Returns the workflowKeyToLaunch.
	 */
	public String getWorkflowKeyToLaunch() {
		return workflowKeyToLaunch;
	}

	/**
	 * @param workflowKeyToLaunch The workflowKeyToLaunch to set.
	 */
	public void setWorkflowKeyToLaunch(String workflowKeyToLaunch) {
		this.workflowKeyToLaunch = workflowKeyToLaunch;
	}

	/**
	 * @return Returns the secondsFromNowToFire.
	 */
	public Long getSecondsFromNowToFire() {
		return secondsFromNowToFire;
	}

	/**
	 * @param secondsFromNowToFire The secondsFromNowToFire to set.
	 */
	public void setSecondsFromNowToFire(Long secondsFromNowToFire) {
		this.secondsFromNowToFire = secondsFromNowToFire;
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