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
package org.gbif.portal.service.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.util.SearchConstraints;

/**
 * Checks the validity of the SearchConstraints supplied and where
 * applicable modifies the values.
 * 
 * @author dmartin
 */
public class SearchConstraintsCheckerInterceptor implements MethodInterceptor {
	
	protected static Log logger = LogFactory.getLog(KeyParserInterceptor.class);		
	/** The default max result size to use **/
	public final int DEFAULT_MAX_RESULTS = 1000;
	/** The default start index to use. The default is 0 **/
	public final int DEFAULT_START_INDEX = 0;	
	/** The max result size to use. All Search Results will be limited by this**/
	protected int maxResults = DEFAULT_MAX_RESULTS;
	
	/**
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		logger.debug("Checking search constraints validity");
		Object[] args = invocation.getArguments();
		//if the last argument is a search constraints check it
		if(args[args.length-1] instanceof SearchConstraints) {
			SearchConstraints searchConstraints = (SearchConstraints) args[args.length-1];
			if(searchConstraints==null)
				throw new IllegalArgumentException("Method requires a non-null SearchConstraints");
			
			Integer maxResults = searchConstraints.getMaxResults();
			//if null or a value that exceeds the configured max, set to default
			if(maxResults==null || maxResults>this.maxResults){
				searchConstraints.setMaxResults(this.maxResults);
			}
			//if null or a silly value			
			if(searchConstraints.getStartIndex()==null || searchConstraints.getStartIndex()<0)
				searchConstraints.setStartIndex(DEFAULT_START_INDEX);
		}
		return invocation.proceed();
	}

	/**
	 * @param maxResults the maxResults to set
	 */
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}
}
