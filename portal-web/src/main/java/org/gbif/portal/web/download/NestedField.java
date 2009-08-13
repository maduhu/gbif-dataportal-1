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

import java.lang.reflect.Method;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;

/**
 * Retrieves the value from a nested bean.
 *  
 * @author dmartin
 */
public class NestedField extends Field {

	protected String nestedBeanName;

	/**
	 * @see org.gbif.portal.web.download.Field#getRenderValue(javax.servlet.http.HttpServletRequest, org.springframework.context.MessageSource, java.util.Locale, java.lang.Object)
	 */
	@Override
	public String getRenderValue(HttpServletRequest request, MessageSource messageSource, Locale locale, Object bean) {
		try {
			String beanName = StringUtils.capitalize(nestedBeanName);
			Method getter = bean.getClass().getMethod("get"+beanName, (Class[]) null);
			Object nestedBean = getter.invoke(bean, (Object[]) null);
			String propertyValue = BeanUtils.getProperty(nestedBean, fieldName);
			return getFieldValue(messageSource, locale, propertyValue);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * @return the nestedBeanName
	 */
	public String getNestedBeanName() {
		return nestedBeanName;
	}

	/**
	 * @param nestedBeanName the nestedBeanName to set
	 */
	public void setNestedBeanName(String nestedBeanName) {
		this.nestedBeanName = nestedBeanName;
	}
}