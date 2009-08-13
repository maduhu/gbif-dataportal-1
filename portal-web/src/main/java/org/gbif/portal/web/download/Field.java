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
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;

/**
 * Represents a field to be downloaded. 
 * 
 * @author dmartin
 */
public class Field {
	
	protected static Log logger = LogFactory.getLog(Field.class);	

	protected String fieldName;
	protected String fieldI18nNameKey;
	protected boolean useI18nOnValue = false;
	protected String i18nPrefix = null;
	protected boolean removeNewLines = true;
	
	/**
	 * Gets the field value applying i18n if necessary.
	 * 
	 * @param messageSource
	 * @param locale
	 * @param propertyValue
	 * @return property value
	 */
	public String getFieldValue(MessageSource messageSource, Locale locale, String propertyValue) {
		
		String formattedValue = propertyValue;
		
		if(useI18nOnValue && propertyValue!=null){
			formattedValue = messageSource.getMessage(i18nPrefix+"."+propertyValue, null, locale);
		} else {
			if(removeNewLines)
				formattedValue = propertyValue.replaceAll("\n", "");
		}
		return formattedValue;
	}	
	
	/**
	 * Create a value for this field
	 * @param request
	 * @param bean
	 * @return
	 */
	public String getRenderValue (HttpServletRequest request, MessageSource messageSource, Locale locale, Object bean){
		try {
			String propertyValue = BeanUtils.getProperty(bean, fieldName);
			return getFieldValue(messageSource, locale, propertyValue);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}		
	}
	
	/**
	 * @return the fieldi18nNameKey
	 */
	public String getFieldI18nNameKey() {
		return fieldI18nNameKey;
	}
	/**
	 * @param fieldi18nNameKey the fieldi18nNameKey to set
	 */
	public void setFieldI18nNameKey(String fieldI18nNameKey) {
		this.fieldI18nNameKey = fieldI18nNameKey;
	}
	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}
	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	/**
	 * @return the useI18nOnValue
	 */
	public boolean isUseI18nOnValue() {
		return useI18nOnValue;
	}
	/**
	 * @param useI18nOnValue the useI18nOnValue to set
	 */
	public void setUseI18nOnValue(boolean useI18nOnValue) {
		this.useI18nOnValue = useI18nOnValue;
	}
	/**
	 * @return the i18nPrefix
	 */
	public String getI18nPrefix() {
		return i18nPrefix;
	}
	/**
	 * @param prefix the i18nPrefix to set
	 */
	public void setI18nPrefix(String prefix) {
		i18nPrefix = prefix;
	}
	/**
	 * @param removeNewLines the removeNewLines to set
	 */
	public void setRemoveNewLines(boolean removeNewLines) {
		this.removeNewLines = removeNewLines;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}