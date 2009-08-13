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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;
import org.gbif.portal.util.workflow.quartz.WorkflowLauncherJob;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

/**
 * An activity to create a quartz job.
 * The job will be scheduled to launch an workflow with seeddata contructed from the 
 * runtime context parameters and the configuration. 
 *
 * @author trobertson
 */
public class QuartzJobToLaunchWorkflowCreatorActivity extends BaseActivity {
	/**
	 * The scheduler
	 */
	protected Scheduler scheduler;
	
	/**
	 * If set will be used, otherwise a once only trigger is created 
	 */
	protected String cronExpression;
	
	/**
	 * The context key to use for the job name
	 */
	protected String contextKeyForJobName;
	
	/**
	 * The context key to use for the trigger name
	 */
	protected String contextKeyForTriggerName;
	
	/**
	 * The job group name
	 */
	protected String jobGroupName;
	
	/**
	 * The trigger group name
	 */
	protected String triggerGroupName;	
	
	/**
	 * The trigger group name postfix key
	 */
	protected String contextKeyTriggerGroupNamePostfix;	
	
	/**
	 * The keys to pull from the context and put in the job
	 */
	List<String> contextKeysForJob = new LinkedList<String>();
	
	/**
	 * The workflow key in the PS to launch
	 */
	protected String workflowKeyToLaunch;
	
	/**
	 * If the cron is not used, this can control when the trigger should fire
	 */
	protected long secondsFromNowToFire = 0;
	
	/**
	 * If set this will override any namespaces in the context
	 */
	protected List<String> propertyStoreNamespaces;
	
    /**
     * @see org.gbif.portal.util.workflow.BaseMapContextActivity#doExecute(org.gbif.portal.util.workflow.MapContext)
     */
    @SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
    	
    	if ("true".equalsIgnoreCase(System.getProperty("manual"))) {
    		logger.info("Running in manual mode - no auto scheduling will occur");
    		return context;
    	}
    	
    	
		SimpleDateFormat sdfSec = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String time = sdfSec.format(new Date());
    	String jobName = context.get(getContextKeyForJobName(), Object.class, true).toString();
    	String triggerName = context.get(getContextKeyForTriggerName(), Object.class, true).toString() + " - " + time;
    	String triggerGroup = getTriggerGroupName();
    	if (getContextKeyTriggerGroupNamePostfix() != null) {
    		String postfix = (String) context.get(getContextKeyTriggerGroupNamePostfix(), String.class, false);
    		if (StringUtils.isNotEmpty(postfix)) {
    			triggerGroup = triggerGroup + "-" + postfix;
    		}
    		
    	}
    	
    	JobDetail jobDetail = scheduler.getJobDetail(jobName, getJobGroupName());
    	boolean jobExisted = true;
    	if (jobDetail == null) {
    		jobDetail = new JobDetail(jobName,getJobGroupName(),WorkflowLauncherJob.class);
    		jobExisted = false;
    	}
    	
    	Trigger trigger = null;    	
    	if (StringUtils.isEmpty(getCronExpression())) {
    		Date toStart = new Date(System.currentTimeMillis() + (secondsFromNowToFire*1000));
    		trigger = new SimpleTrigger(triggerName, triggerGroup, toStart);
			trigger.setJobName(jobName);
			trigger.setJobGroup(getJobGroupName());				
			trigger.setDescription("Immediate index, created on " + time);
			
    	} else {
			trigger = new CronTrigger(triggerName, triggerGroup, jobName, getJobGroupName(), getCronExpression());												
			trigger.setDescription("Cron trigger[" + getCronExpression() + "], created on " + time);
    	}
    	
    	
    	Map<String, Object> seed = new HashMap<String, Object>();
    	for (String key : contextKeysForJob) {
    		logger.info("Putting in key[" + key + "] value[" + context.get(key) + "]");
    		seed.put(key, context.get(key));    		
    	}
    	jobDetail.getJobDataMap().put(WorkflowLauncherJob.SEED_DATA_KEY, seed);
    	jobDetail.getJobDataMap().put(WorkflowLauncherJob.PROPERTY_STORE_KEY_WORKFLOW_KEY, getWorkflowKeyToLaunch());
    	
    	if (getPropertyStoreNamespaces() != null) {
        	jobDetail.getJobDataMap().put(WorkflowLauncherJob.PROPERTY_STORE_NAMESPACES_KEY, getPropertyStoreNamespaces());
        	logger.info("Putting in key[" + WorkflowLauncherJob.PROPERTY_STORE_NAMESPACES_KEY + "] value[" + getPropertyStoreNamespaces() + "]");
    	} else {
        	jobDetail.getJobDataMap().put(WorkflowLauncherJob.PROPERTY_STORE_NAMESPACES_KEY, context.get(getContextKeyPsNamespaces(), List.class, true));
        	logger.info("Putting in key[" + WorkflowLauncherJob.PROPERTY_STORE_NAMESPACES_KEY + "] value[" + context.get(getContextKeyPsNamespaces(), List.class, true) + "]");
    	}
    	
    	logger.info("Scheduling new job [" + jobDetail.getFullName() + "] with trigger[" + trigger.getFullName() + "]");
    	if (jobExisted) {
    		scheduler.scheduleJob(trigger);
    	} else {
    		scheduler.scheduleJob(jobDetail, trigger);
    	}
    	return context;
    }

	/**
	 * @return Returns the cronExpression.
	 */
	public String getCronExpression() {
		return cronExpression;
	}

	/**
	 * @param cronExpression The cronExpression to set.
	 */
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
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
	 * @return Returns the contextKeyForJobName.
	 */
	public String getContextKeyForJobName() {
		return contextKeyForJobName;
	}

	/**
	 * @param contextKeyForJobName The contextKeyForJobName to set.
	 */
	public void setContextKeyForJobName(String contextKeyForJobName) {
		this.contextKeyForJobName = contextKeyForJobName;
	}

	/**
	 * @return Returns the contextKeyForTriggerName.
	 */
	public String getContextKeyForTriggerName() {
		return contextKeyForTriggerName;
	}

	/**
	 * @param contextKeyForTriggerName The contextKeyForTriggerName to set.
	 */
	public void setContextKeyForTriggerName(String contextKeyForTriggerName) {
		this.contextKeyForTriggerName = contextKeyForTriggerName;
	}

	/**
	 * @return Returns the jobGroupName.
	 */
	public String getJobGroupName() {
		return jobGroupName;
	}

	/**
	 * @param jobGroupName The jobGroupName to set.
	 */
	public void setJobGroupName(String jobGroupName) {
		this.jobGroupName = jobGroupName;
	}

	/**
	 * @return Returns the triggerGroupName.
	 */
	public String getTriggerGroupName() {
		return triggerGroupName;
	}

	/**
	 * @param triggerGroupName The triggerGroupName to set.
	 */
	public void setTriggerGroupName(String triggerGroupName) {
		this.triggerGroupName = triggerGroupName;
	}

	/**
	 * @return Returns the contextKeysForJob.
	 */
	public List<String> getContextKeysForJob() {
		return contextKeysForJob;
	}

	/**
	 * @param contextKeysForJob The contextKeysForJob to set.
	 */
	public void setContextKeysForJob(List<String> contextKeysForJob) {
		this.contextKeysForJob = contextKeysForJob;
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
	public long getSecondsFromNowToFire() {
		return secondsFromNowToFire;
	}

	/**
	 * @param secondsFromNowToFire The secondsFromNowToFire to set.
	 */
	public void setSecondsFromNowToFire(long secondsFromNowToFire) {
		this.secondsFromNowToFire = secondsFromNowToFire;
	}

	/**
	 * @return Returns the contextKeyTriggerGroupNamePostfix.
	 */
	public String getContextKeyTriggerGroupNamePostfix() {
		return contextKeyTriggerGroupNamePostfix;
	}

	/**
	 * @param contextKeyTriggerGroupNamePostfix The contextKeyTriggerGroupNamePostfix to set.
	 */
	public void setContextKeyTriggerGroupNamePostfix(
			String contextKeyTriggerGroupNamePostfix) {
		this.contextKeyTriggerGroupNamePostfix = contextKeyTriggerGroupNamePostfix;
	}

	/**
	 * @return the propertyStoreNamespaces
	 */
	public List<String> getPropertyStoreNamespaces() {
		return propertyStoreNamespaces;
	}

	/**
	 * @param propertyStoreNamespaces the propertyStoreNamespaces to set
	 */
	public void setPropertyStoreNamespaces(List<String> propertyStoreNamespaces) {
		this.propertyStoreNamespaces = propertyStoreNamespaces;
	}
}