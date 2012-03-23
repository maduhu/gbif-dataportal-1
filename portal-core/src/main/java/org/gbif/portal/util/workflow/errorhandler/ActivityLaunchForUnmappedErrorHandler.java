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
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.ErrorHandler;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * Will launch a single activity for any errors
 *
 * @author trobertson
 */
public class ActivityLaunchForUnmappedErrorHandler implements ErrorHandler {
  /**
   * Logger
   */
  protected Log logger = LogFactory.getLog(getClass());

  /**
   * The bean name
   */
  protected String beanName;

  /**
   * The context key to put the exception name in the context
   */
  protected String contextKeyExceptionName;

  /**
   * Activity to launch
   */
  protected Activity activity;

  /**
   * indicates if the handler should stop the workflow regardless of error found or not
   */
  protected boolean shouldStopProcess = true;

  @SuppressWarnings("unchecked")
  public void handleError(ProcessContext context, Throwable th) throws Exception {
    String errorThrown = th.getClass().getCanonicalName();
    logger.info("Error details: ", th);
    logger.info("Handling: " + errorThrown + " by calling activity: " + activity.getClass());
    context.put(getContextKeyExceptionName(), errorThrown);
    activity.execute(context);
    context.setStopProcess(isShouldStopProcess());
    logger.info("Error successfully handled");
  }

  /**
   * @return Returns the activity.
   */
  public Activity getActivity() {
    return activity;
  }

  /**
   * @param activity The activity to set.
   */
  public void setActivity(Activity activity) {
    this.activity = activity;
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
   * @return Returns the contextKeyExceptionName.
   */
  public String getContextKeyExceptionName() {
    return contextKeyExceptionName;
  }

  /**
   * @param contextKeyExceptionName The contextKeyExceptionName to set.
   */
  public void setContextKeyExceptionName(String contextKeyExceptionName) {
    this.contextKeyExceptionName = contextKeyExceptionName;
  }

  /**
   * @return Returns the shouldStopProcess.
   */
  public boolean isShouldStopProcess() {
    return shouldStopProcess;
  }

  /**
   * @param shouldStopProcess The shouldStopProcess to set.
   */
  public void setShouldStopProcess(boolean shouldStopProcess) {
    this.shouldStopProcess = shouldStopProcess;
  }

}
