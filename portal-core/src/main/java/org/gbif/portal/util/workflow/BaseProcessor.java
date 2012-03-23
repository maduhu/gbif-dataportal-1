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

import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.List;

/**
 * Base class for all Workflow Processors.  Responsible of keeping track of an ordered collection
 * of {@link Activity Activities}
 *
 * @author trobertson
 *         [based on the article by Steve Dodge: http://www.javaworld.com/javaworld/jw-04-2005/jw-0411-spring.html]
 * @see Activity
 */
public abstract class BaseProcessor implements InitializingBean, BeanNameAware, BeanFactoryAware, Processor {
  private BeanFactory beanFactory;
  private String beanName;
  private List activities;
  private ErrorHandler defaultErrorHandler;

  /**
   * Called after the properties have been set, Ensures the list of activities
   * is not empty and each activity is supported by this Workflow Processor
   *
   * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
   */
  public void afterPropertiesSet() throws Exception {

    if (!(beanFactory instanceof ListableBeanFactory))
      throw new BeanInitializationException("The workflow processor [" + beanName + "] " +
        "is not managed by a ListableBeanFactory, please re-deploy using some dirivative of ListableBeanFactory such " +
        "as" +
        "ClassPathXmlApplicationContext ");

    if (activities == null || activities.isEmpty())
      throw new UnsatisfiedDependencyException(getBeanDesc(), beanName, "activities",
        "No activities were wired for this workflow");
  }

  /**
   * Returns the bean description if the current bean factory allows it.
   *
   * @return spring bean description configure via the spring description tag
   */
  protected String getBeanDesc() {
    return (beanFactory instanceof ConfigurableListableBeanFactory) ?
      ((ConfigurableListableBeanFactory) beanFactory).getBeanDefinition(beanName).getResourceDescription()
      : "Workflow Processor: " + beanName;

  }

  /**
   * @return Returns the activities.
   */
  public List getActivities() {
    return activities;
  }

  /**
   * @param activities The activities to set.
   */
  public void setActivities(List activities) {
    this.activities = activities;
  }

  /**
   * @return Returns the beanFactory.
   */
  public BeanFactory getBeanFactory() {
    return beanFactory;
  }

  /**
   * @param beanFactory The beanFactory to set.
   */
  public void setBeanFactory(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
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
   * @return Returns the defaultErrorHandler.
   */
  public ErrorHandler getDefaultErrorHandler() {
    return defaultErrorHandler;
  }

  /**
   * @param defaultErrorHandler The defaultErrorHandler to set.
   */
  public void setDefaultErrorHandler(ErrorHandler defaultErrorHandler) {
    this.defaultErrorHandler = defaultErrorHandler;
  }
}
