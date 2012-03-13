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

/**
 * @author trobertson 
 * [based on the article by Steve Dodge: http://www.javaworld.com/javaworld/jw-04-2005/jw-0411-spring.html]
 */
public interface Processor {
    /**
     * Abstract method used to kickoff the processing of work flow activities.
     * Each processor implementation should implement doActivities as a means
     * of carrying out the activities wired to the workflow process.
     */
    public void doActivities() throws Exception;
    
    /**
     * Abstract method used to kickoff the processing of work flow activities.
     * Each processor implementation should implement doActivities as a means
     * of carrying out the activities wired to the workflow process.  This version
     * of doActivities is designed to be called from some external entity, e.g.
     * listening a JMS queue.  That external entitiy would proved the seed data.
     * 
     * @param seedData - data necessary for the workflow process to start execution
     */
    public ProcessContext doActivities(Map seedData) throws Exception ;

    /**
     * Sets the collection of Activities to be executed by the Workflow Process
     * 
     * @param activities ordered collection (List) of activities to be executed by the processor
     */
    public void setActivities(List activities);
    
    /**
     * Sets the error handling for the overall processor
     * @param defaultErrorHandler To use when no other handle has dealt with the exception
     */
    public void setDefaultErrorHandler(ErrorHandler defaultErrorHandler);
}