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

package org.gbif.portal.util.propertystore.impl.spring;

import java.util.List;
import java.util.Map;

import org.gbif.portal.util.propertystore.MisconfiguredPropertyException;
import org.gbif.portal.util.propertystore.PropertyNotFoundException;
import org.gbif.portal.util.propertystore.PropertyStore;

/**
 * An implementation of the PropertyStore that used spring
 * beans configuration files for defining the mapped properties
 * 
 * @author Tim Robertson
 */
public class PropertyStoreImpl implements PropertyStore {

	/**
	 * The namespace specific property bags that the PS holds
	 */
	protected Map<String, PropertyBag> namespaceMappings; 
	
	/**
	 * Using the namespaceMappings, returns the defined property or throws a PropertyNotFoundException
	 * @see org.gbif.mhf.propertystore.PropertyStore#getProperty(java.lang.String, java.lang.String)
	 */
	public Object getProperty(String namespace, String key) throws PropertyNotFoundException {
		PropertyBag properties = namespaceMappings.get(namespace);
		if (properties == null) {
			throw new PropertyNotFoundException(namespace);
		}
		
		Object property = properties.getProperty(key);
		if (property == null) {
			throw new PropertyNotFoundException(namespace, key);
		}		
		return property;
	}
	
	/** 
	 * @see org.gbif.portal.util.propertystore.PropertyStore#getProperty(java.lang.String, java.lang.String, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public Object getProperty(String namespace, String key, Class expectedType) throws PropertyNotFoundException, MisconfiguredPropertyException {
		Object result = getProperty(namespace, key);
		if (!expectedType.isAssignableFrom(result.getClass())) {
			throw new MisconfiguredPropertyException(namespace, key, expectedType, result.getClass());
		}
		return result;
	}
	
	/**
	 * @see org.gbif.mhf.propertystore.PropertyStore#propertySupported(java.lang.String, java.lang.String)
	 */
	public boolean propertySupported(String namespace, String key) {
		PropertyBag properties = namespaceMappings.get(namespace);
		if (properties == null) {
			return false;
		}
		
		Object property = properties.getProperty(key);
		if (property == null) {
			return false;
		}		
		return true;
	}

	/**
	 * @see org.gbif.portal.util.propertystore.PropertyStore#propertySupported(java.util.List, java.lang.String)
	 */
	public boolean propertySupported(List<String> namespaces, String key) {
		for(String namespace:namespaces){
			if(propertySupported(namespace, key))
				return true;
		}
		return false;
	}	
	
	/**
	 * @see org.gbif.portal.util.propertystore.PropertyStore#getProperty(java.util.List, java.lang.String)
	 */
	public Object getProperty(List<String> namespaces, String key) throws PropertyNotFoundException {
		Object result = null;
		for (String namespace : namespaces) {
			try {
				result = getProperty(namespace, key);
			} catch (PropertyNotFoundException e) {
			}			
			if (result != null) {
				return result;
			}
		}
		throw new PropertyNotFoundException(namespaces, key);
	}

	/**
	 * @see org.gbif.portal.util.propertystore.PropertyStore#getProperty(java.util.List, java.lang.String, java.lang.Class)
	 */
	public Object getProperty(List<String> namespaces, String key, Class expectedType) throws PropertyNotFoundException, MisconfiguredPropertyException {
		Object result = null;
		if (namespaces == null) {
			throw new PropertyNotFoundException("Property not found since namespaces are not supplied");
		}
		for (String namespace : namespaces) {
			try {
				result = getProperty(namespace, key, expectedType);
			} catch (PropertyNotFoundException e) {
			}
			if (result != null) {
				return result;
			}
		}
		throw new PropertyNotFoundException(namespaces, key);
	}
	
	/**
	 * @return Returns the namespaceMappings.
	 */
	public Map<String, PropertyBag> getNamespaceMappings() {
		return namespaceMappings;
	}

	/**
	 * @param namespaceMappings The namespaceMappings to set.
	 */
	public void setNamespaceMappings(Map<String, PropertyBag> namespaceMappings) {
		this.namespaceMappings = namespaceMappings;
	}
}