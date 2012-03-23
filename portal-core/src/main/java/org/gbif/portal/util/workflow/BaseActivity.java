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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.util.propertystore.PropertyStore;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Abstract implemention of activity designed for
 * re-use by business purpose specific Activities
 *
 * @author trobertson
 *         [based on the article by Steve Dodge: http://www.javaworld.com/javaworld/jw-04-2005/jw-0411-spring.html]
 */
public abstract class BaseActivity implements Activity, InitializingBean {
  protected ErrorHandler errorHandler;
  protected String beanName;
  protected ApplicationContext applicationContext;

  /**
   * Logger
   */
  protected Log logger = LogFactory.getLog(getClass());

  /**
   * A child workflow that an activity can launch
   */
  protected Processor workflow;

  /**
   * PropertyStore workflow name
   * Can be used INSTEAD of the workflow directly, for a key to lookup the child
   * workflow during runtime
   */
  protected String psWorkflowKey;

  /**
   * The keys to seed the child concept with
   */
  protected List<String> seedDataContextKeys = new LinkedList<String>();

  /**
   * The data to pull from the child context when complete
   */
  protected List<String> resultDataContextKeys = new LinkedList<String>();

  /**
   * The property store
   */
  protected PropertyStore propertyStore;

  /**
   * The namespaces in use
   */
  protected String contextKeyPsNamespaces;

  /**
   * Builds a Map of seed data from the seedDataContextKeys
   *
   * @return The map ready for seed data
   */
  protected Map<String, Object> buildWorkflowSeedData(ProcessContext context) {
    Map<String, Object> seed = new HashMap<String, Object>();
    for (String key : seedDataContextKeys) {
      seed.put(key, context.get(key));
    }
    return seed;
  }

  /**
   * Copies the resulting child data to the parent context
   *
   * @param parent
   * @param child
   */
  @SuppressWarnings("unchecked")
  protected void copyWorkflowResultData(ProcessContext parent, ProcessContext child) {
    for (String key : resultDataContextKeys) {
      logger.debug("Copying to parent key[" + key + "]");
      parent.put(key, child.get(key));
    }
  }

  /**
   * Build the seed data and launch the child workflow should it exist
   * When finished copy the data back in
   *
   * @param context
   * @param seedData extra seed data to add
   * @return The context
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  protected ProcessContext launchWorkflow(ProcessContext context, Map<String, Object> seedData) throws Exception {
    if (workflow != null) {
      Map<String, Object> seed = buildWorkflowSeedData(context);
      if (seedData != null) {
        seed.putAll(seedData);
      }
      ProcessContext child = workflow.doActivities(seed);
      copyWorkflowResultData(context, child);
    } else if (psWorkflowKey != null) {
      List<String> namespaces = (List<String>) context.get(getContextKeyPsNamespaces(), List.class, true);
      Processor workflow = (Processor) propertyStore.getProperty(namespaces, getPsWorkflowKey(), Processor.class);
      Map<String, Object> seed = buildWorkflowSeedData(context);
      if (seedData != null) {
        seed.putAll(seedData);
      }
      ProcessContext child = workflow.doActivities(seed);
      copyWorkflowResultData(context, child);
    }

    return context;
  }

  /**
   * Called after the properties have been set, ensuring that only the workflow, or psWorkflowKey
   * have been used
   *
   * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
   */
  public void afterPropertiesSet() throws Exception {
    if (getWorkflow() != null && getPsWorkflowKey() != null) {
      throw new UnsatisfiedDependencyException("BaseActivity", beanName, "workflow/psWorkflowKey",
        "Only the workflow OR the psWorkflowKey may be used - NOT both");
    }
    if (getPsWorkflowKey() != null && (getPropertyStore() == null || getContextKeyPsNamespaces() == null)) {
      throw new UnsatisfiedDependencyException("BaseActivity", beanName, "propertyStore",
        "The property store must be wired, and the contextKeyPsNamespaces set when using the psWorkflowKey");
    }
  }

  /**
   * @return Returns the applicationContext.
   */
  public ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  /**
   * @param applicationContext The applicationContext to set.
   */
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * @return Returns the beanName.
   */
  public String getBeanName() {
    return beanName;
  }

  /**
   * @param beanName The beanName to set.
   */
  public void setBeanName(String beanName) {
    this.beanName = beanName;
  }

  /**
   * @return Returns the errorHandler.
   */
  public ErrorHandler getErrorHandler() {
    return errorHandler;
  }

  /**
   * @param errorHandler The errorHandler to set.
   */
  public void setErrorHandler(ErrorHandler errorHandler) {
    this.errorHandler = errorHandler;
  }


  /**
   * @return Returns the workflow.
   */
  public Processor getWorkflow() {
    return workflow;
  }

  /**
   * @param workflow The workflow to set.
   */
  public void setWorkflow(Processor workflow) {
    this.workflow = workflow;
  }

  /**
   * @return Returns the resultDataContextKeys.
   */
  public List<String> getResultDataContextKeys() {
    return resultDataContextKeys;
  }

  /**
   * @param resultDataContextKeys The resultDataContextKeys to set.
   */
  public void setResultDataContextKeys(List<String> resultDataContextKeys) {
    this.resultDataContextKeys = resultDataContextKeys;
  }

  /**
   * @return Returns the seedDataContextKeys.
   */
  public List<String> getSeedDataContextKeys() {
    return seedDataContextKeys;
  }

  /**
   * @param seedDataContextKeys The seedDataContextKeys to set.
   */
  public void setSeedDataContextKeys(List<String> seedDataContextKeys) {
    this.seedDataContextKeys = seedDataContextKeys;
  }

  /**
   * @return Returns the psWorkflowKey.
   */
  public String getPsWorkflowKey() {
    return psWorkflowKey;
  }

  /**
   * @param psWorkflowKey The psWorkflowKey to set.
   */
  public void setPsWorkflowKey(String psWorkflowKey) {
    this.psWorkflowKey = psWorkflowKey;
  }

  /**
   * @return Returns the propertyStore.
   */
  public PropertyStore getPropertyStore() {
    return propertyStore;
  }

  /**
   * @param propertyStore The propertyStore to set.
   */
  public void setPropertyStore(PropertyStore propertyStore) {
    this.propertyStore = propertyStore;
  }

  /**
   * @return Returns the contextKeyPsNamespaces.
   */
  public String getContextKeyPsNamespaces() {
    return contextKeyPsNamespaces;
  }

  /**
   * @param contextKeyPsNamespaces The contextKeyPsNamespaces to set.
   */
  public void setContextKeyPsNamespaces(String contextKeyPsNamespaces) {
    this.contextKeyPsNamespaces = contextKeyPsNamespaces;
  }
}
