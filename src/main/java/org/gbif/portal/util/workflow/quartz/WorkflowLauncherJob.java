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
package org.gbif.portal.util.workflow.quartz;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.util.propertystore.PropertyStore;
import org.gbif.portal.util.workflow.Processor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;

/**
 * @author trobertson
 */
public class WorkflowLauncherJob implements Job {
  /**
   * Logger
   */
  protected Log logger = LogFactory.getLog(getClass());

  /**
   * The workflow name to launch
   */
  public static final String PROPERTY_STORE_KEY_WORKFLOW_KEY = "workflowKey";

  /**
   * The namespaces
   */
  public static final String PROPERTY_STORE_NAMESPACES_KEY = "psNamespaces";

  /**
   * The seed data
   */
  public static final String SEED_DATA_KEY = "seedDataKey";

  /**
   * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
   */
  @SuppressWarnings("unchecked")
  public void execute(JobExecutionContext quartzContext) throws JobExecutionException {
    try {
      logger.info(quartzContext.getJobDetail().getFullName() + " executing...");
      ApplicationContext context = (ApplicationContext) quartzContext.getScheduler().getContext().get
        ("applicationContext");
      PropertyStore propertyStore = (PropertyStore) context.getBean("indexPropertyStore", PropertyStore.class);
      String propertyStoreWorkflowKey = (String) quartzContext.getJobDetail().getJobDataMap().get
        (PROPERTY_STORE_KEY_WORKFLOW_KEY);
      List<String> propertyStoreNamespaces = (List<String>) quartzContext.getJobDetail().getJobDataMap().get
        (PROPERTY_STORE_NAMESPACES_KEY);
      Map<String, Object> seed = (Map<String, Object>) quartzContext.getJobDetail().getJobDataMap().get(SEED_DATA_KEY);
      Processor workflow = (Processor) propertyStore.getProperty(propertyStoreNamespaces, propertyStoreWorkflowKey,
        Processor.class);
      workflow.doActivities(seed);
      logger.info(quartzContext.getJobDetail().getFullName() + " finished");
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }
}
