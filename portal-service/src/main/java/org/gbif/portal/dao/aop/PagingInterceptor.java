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
package org.gbif.portal.dao.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * An interceptor at the DAO level that intercepts a call to a method
 * that supports paging and increments max results by 1. This enables
 * a calling method to realise that there are potentially
 * more records to page through.
 * 
 * @author dmartin
 */
public class PagingInterceptor implements MethodInterceptor {

	/**
	 * Assumes the last argument is the maxResults value and increments it by 1.
	 * This enables the calling method to compare the number returned with the max results.
	 * If the number returned is greater than max results then there exist more results
	 * for this query.
	 * 
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object[] args = invocation.getArguments();
		Integer maxResults = (Integer) args[args.length-1];
		args[args.length-1] = maxResults +1;
		return invocation.proceed();
	}
}
