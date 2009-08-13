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
package org.gbif.portal.web.download;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;

/**
 * Custom Field class for generating urls.
 * 
 * @author dmartin
 */
public class UrlField extends Field { 

	protected static Log logger = LogFactory.getLog(UrlField.class);
	protected String prefix;
	protected String beanProperty;
	protected String postfix;
	protected String protocol ="http://";
	
	public String getFieldValue(String hostUrl, MessageSource messageSource, Locale locale, String propertyValue) {
		StringBuffer sb = new StringBuffer();
		sb.append(hostUrl);
		sb.append(prefix);
		if(propertyValue!=null)
			sb.append(propertyValue);
		sb.append(postfix);
		return sb.toString();		
	}
	
	@Override
	public String getRenderValue(HttpServletRequest request, MessageSource messageSource, Locale locale, Object bean) {
		
		String propertyValue = null;
		try {
			propertyValue = BeanUtils.getProperty(bean, beanProperty);
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			return null;
		}
		StringBuffer sb = new StringBuffer();
		String host = request.getHeader("host");
		sb.append(protocol);
		sb.append(host);
		sb.append(request.getContextPath());
		sb.append(prefix);
		sb.append(propertyValue);
		sb.append(postfix);
		return sb.toString();
	}

	/**
	 * @return the beanProperty
	 */
	public String getBeanProperty() {
		return beanProperty;
	}

	/**
	 * @param beanProperty the beanProperty to set
	 */
	public void setBeanProperty(String beanProperty) {
		this.beanProperty = beanProperty;
	}

	/**
	 * @return the postfix
	 */
	public String getPostfix() {
		return postfix;
	}

	/**
	 * @param postfix the postfix to set
	 */
	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
}