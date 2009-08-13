/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gbif.portal.web.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * An Spring AbstractMessageSource implementation that uses properties.loadFromXML to enable 
 * loading of messages from XML files. This is required for UTF-8 support. 
 * 
 * @see ReloadableResourceBundleMessageSource
 * @see java.util.ResourceBundle
 * @see java.text.MessageFormat
 * 
 */
public class PropertiesMessageSource extends org.springframework.context.support.AbstractMessageSource implements BeanClassLoaderAware {

	protected ClassLoader classLoader;
	
	/** The properties/xml files to load */
	private String[] basenames = new String[0];

	/** Cache of locales */
	protected Map<String, Map<String, Properties>> loadedProperties = new HashMap<String, Map<String, Properties>>();
	
	/** Cache of message format */
	protected Map<Locale, Map<String, MessageFormat>> cachedMessageFormat = new HashMap<Locale, Map<String, MessageFormat>> ();
	
	/** The default language if not supplied */
	protected String defaultLanguage = "en";

	/** Whether to cache message format objects */
	protected boolean useCaching = true;
	
	/** 
	 * Whether or not to resolve from the default locale if the message is 
	 * unavailable for the selected locale.
	 */
	protected boolean resolveFromDefaultIfNull = false;
	
	/**
	 * @see org.springframework.context.support.AbstractMessageSource#resolveCode(java.lang.String, java.util.Locale)
	 */
	@Override
	protected MessageFormat resolveCode(String code, Locale locale) {
		
		if(locale==null){
			locale = new Locale(defaultLanguage);
		}

		if(useCaching){
			MessageFormat messageFormat = retrieveCachedMessageFormat(code, locale);
			if(messageFormat!=null){
				return messageFormat;
			}
		}
		
		//retrieve all the properties for this locale
		Map<String, Properties> localeProperties = retrieveLocale(locale);
		
		//retrieve the message
		String message = findMessageCode(code, localeProperties);
		
		//if this message unavailable for this locale, look for the message in the default locale
		if(message==null && resolveFromDefaultIfNull && !defaultLanguage.equals(locale.getLanguage())){
			Locale defaultLocale = new Locale(defaultLanguage);
			Map<String, Properties> defaulLocaleProperties = retrieveLocale(defaultLocale);
			message = findMessageCode(code, defaulLocaleProperties);			
		}
		
		if(message!=null){
			MessageFormat messageFormat = createMessageFormat(message, locale);
			if(useCaching){
				cacheMessageFormat(code, locale, messageFormat);
			}
			return messageFormat;
		} 

		return null;
	}

	/**
	 * Cache this Message Format object.
	 * 
	 * @param code
	 * @param locale
	 * @param messageFormat
	 */
	private void cacheMessageFormat(String code, Locale locale,
			MessageFormat messageFormat) {
		Map<String, MessageFormat> retrievedCache = cachedMessageFormat.get(locale);
		if(retrievedCache==null){
			retrievedCache = new HashMap<String, MessageFormat>();
			cachedMessageFormat.put(locale, retrievedCache);
		}
		retrievedCache.put(code, messageFormat);
	}

	/**
	 * Returns the stored message format.
	 * 
	 * @param code
	 * @param locale
	 * @return MessageFormat if cached, null otherwise
	 */
	private MessageFormat retrieveCachedMessageFormat(String code, Locale locale) {
		Map<String, MessageFormat> retrievedCache = cachedMessageFormat.get(locale);
		if(retrievedCache!=null){
			return retrievedCache.get(code);
		}
		return null;
	}

	/**
	 * Find the message for this code from the locale properties.
	 * 
	 * @param code
	 * @param localeProperties
	 * @return
	 */
	private String findMessageCode(String code, Map<String, Properties> localeProperties) {
		String message = null;
		for(String baseName: localeProperties.keySet()){
			Properties properties = localeProperties.get(baseName);
			message = properties.getProperty(code);
			if(message!=null)
				break;
		}
		return message;
	}

	/**
	 * Retrieves the locale
	 * @param locale
	 * @return
	 */
	private Map<String, Properties> retrieveLocale(Locale locale) {
		Map<String, Properties> localeProperties = loadedProperties.get(locale.getLanguage());
		if(localeProperties!=null){
			return localeProperties;
		}
		
		//initialise the locale
		localeProperties = new HashMap<String, Properties>();
		if(logger.isDebugEnabled()){
			logger.debug("Initialising locale: "+locale.getDisplayName());
		}
		
		for(String basename: basenames){
			
			boolean isXml = false;
			
			//look for a properties file
			InputStream inputStream = classLoader.getResourceAsStream(basename+"_"+locale+".properties");
			
			//look for a XML properties file
			if(inputStream==null){
				inputStream = classLoader.getResourceAsStream(basename+"_"+locale+".xml");
				isXml = true;
			}
			
			//if both are null look for the default locale
			if(inputStream==null){
				inputStream = classLoader.getResourceAsStream(basename+".properties");
				isXml = false;
			}
			
			//look for a XML properties file
			if(inputStream==null){
				inputStream = classLoader.getResourceAsStream(basename+".xml");
				isXml = true;
			}
			
			//if can't find throw exception
			if(inputStream==null){
				throw new IllegalArgumentException("No properties file found for basename '"+basename+"' with the locale '"+locale+"'");
			}
			
			Properties properties = new Properties();
			try {
				if(isXml){
					properties.loadFromXML(inputStream);
				} else {
					properties.load(inputStream);
				}
			} catch (IOException e) {
				throw new IllegalArgumentException(e);
			}
			
			localeProperties.put(basename, properties);
		}
		//add to cached locales
		loadedProperties.put(locale.getLanguage(), localeProperties);
		return localeProperties;
	}

	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	/**
	 * @return the classLoader
	 */
	public ClassLoader getClassLoader() {
		return classLoader;
	}

	/**
	 * @param classLoader the classLoader to set
	 */
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	/**
	 * @param basenames the basenames to set
	 */
	public void setBasenames(String[] basenames) {
		this.basenames = basenames;
	}

	/**
	 * @param defaultLanguage the defaultLanguage to set
	 */
	public void setDefaultLanguage(String defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}

	/**
	 * @param resolveFromDefaultIfNull the resolveFromDefaultIfNull to set
	 */
	public void setResolveFromDefaultIfNull(boolean resolveFromDefaultIfNull) {
		this.resolveFromDefaultIfNull = resolveFromDefaultIfNull;
	}

	/**
	 * @param useCaching the useCaching to set
	 */
	public void setUseCaching(boolean useCaching) {
		this.useCaching = useCaching;
	}
}