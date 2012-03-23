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
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Simple interceptor that checks the supplied key value is a valid
 * key. The argumentNo property indicates the index of the argument
 * to test.
 * 
 * @author dmartin
 */
public class KeyParserInterceptor implements MethodInterceptor {
	
	/** serial version id **/
	private static final long serialVersionUID = -3844521419652055150L;
	protected static Log logger = LogFactory.getLog(KeyParserInterceptor.class);		
	/** the argument to check the validity of **/
	protected int argumentNo = 0;	
	/**
	 * Checks the supplied key is a valid not empty assignable key.
	 * If it not then an IllegalArgumentException is thrown.
	 * 
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		logger.debug("Checking key validity");
		Object[] arguments = invocation.getArguments();
		String keyAsString = (String) arguments[argumentNo];
		if(StringUtils.isNotEmpty(keyAsString)){
			try {
				Long.parseLong(keyAsString);
				return invocation.proceed();
			} catch (NumberFormatException e){
				//expected behaviour for invalid keys
			}
		}
		throw new IllegalArgumentException("Invalid key supplied: "+ keyAsString);
	}

	/**
	 * @param argumentNo the argumentNo to set
	 */
	public void setArgumentNo(int argumentNo) {
		this.argumentNo = argumentNo;
	}
}