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

import org.springframework.beans.factory.BeanNameAware;

/**
 * An defines an error handler, which is used to handle errors passed up from an activity
 * @author trobertson 
 * [based on the article by Steve Dodge: http://www.javaworld.com/javaworld/jw-04-2005/jw-0411-spring.html]
 */
public interface ErrorHandler extends BeanNameAware {
	/**
	 * Attempts to handle the error and if it can't, it throws an exception
	 * @param context That the workflow is working within
	 * @param th That is being handles
	 * @throws Exception If the handler can't handle the error
	 */
    public void handleError(ProcessContext context, Throwable th) throws Exception;
}
