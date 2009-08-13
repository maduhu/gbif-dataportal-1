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
package org.gbif.portal.registration;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.registration.model.ResourceDetail;
import org.gbif.portal.util.mhf.message.Message;
import org.gbif.portal.util.mhf.message.MessageAccessException;
import org.gbif.portal.util.mhf.message.MessageUtils;
import org.gbif.portal.util.propertystore.MisconfiguredPropertyException;
import org.gbif.portal.util.propertystore.PropertyNotFoundException;
import org.gbif.portal.util.propertystore.PropertyStore;

/**
 * Utilities that will, using the property store allow the required values to be
 * pulled out of metadata for syncing with the UDDI registry
 * @author trobertson
 */
public class ResourceExtractionUtils {
	/**
	 * Utils
	 */
	protected MessageUtils messageUtils;
	protected PropertyStore propertyStore;
	protected Log logger = LogFactory.getLog(ResourceExtractionUtils.class.getName());
	protected String psPropertyPrefix = "METADATA.RESOURCE";
	
	/**
	 * Retrieves a list of properties for the resource detail that are mapped.
	 * @param psNamespaces
	 * @return
	 */
	public List<String> getMappedBeanPropertiesForNamespace(List<String> psNamespaces){
		List<String> mappedBeanProperties = new ArrayList<String>();
		PropertyDescriptor[] pds = org.springframework.beans.BeanUtils.getPropertyDescriptors(ResourceDetail.class);
		for(PropertyDescriptor pd: pds){
			String psPropertyName = psPropertyPrefix+"."+pd.getName().toUpperCase();
			if(messageUtils.isPropertyMapped(psNamespaces, psPropertyName)){
				mappedBeanProperties.add(pd.getName());
			}
		}
		return mappedBeanProperties;
	}
	
	/**
	 * Using the property store, a list of "registry" resources are extracted from the message 
	 * @param psNamespaces That identify the message
	 * @param message To extract the values from 
	 * @return The resources the provider offers at the url
	 * @throws MessageAccessException 
	 * @throws MisconfiguredPropertyException 
	 * @throws PropertyNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	public List<ResourceDetail> getResourcesFromMetadata(List<String> psNamespaces, Message message) throws PropertyNotFoundException, MisconfiguredPropertyException, MessageAccessException {
		List<ResourceDetail> results = new LinkedList<ResourceDetail>();
		List<Message> resourceMessages = (List<Message>) messageUtils.extractSubMessageList(message, psNamespaces, psPropertyPrefix, true);
		
		PropertyDescriptor[] pds = org.springframework.beans.BeanUtils.getPropertyDescriptors(ResourceDetail.class);
		
		for (Message resourceMessage : resourceMessages) {
			ResourceDetail resource = new ResourceDetail();

			for(PropertyDescriptor pd: pds){
				String psPropertyName = psPropertyPrefix+"."+pd.getName().toUpperCase();
				String propertyValue = extractMessageProperty(resourceMessage, psNamespaces, psPropertyName);
				if(StringUtils.isNotEmpty(propertyValue)){
					try{
						Class propertyType = pd.getPropertyType();
						if(propertyType.equals(Integer.class)){
							pd.getWriteMethod().invoke(resource, Integer.parseInt(propertyValue));							
						} else if(propertyType.equals(Long.class)) {
							pd.getWriteMethod().invoke(resource, Long.parseLong(propertyValue));
						} else if(propertyType.equals(Boolean.class)) {
							pd.getWriteMethod().invoke(resource, Boolean.parseBoolean(propertyValue));							
						} else {
							pd.getWriteMethod().invoke(resource, propertyValue);	
						}
					} catch(Exception e){
						logger.debug(e.getMessage(), e);
					}
				}
			}
			if(logger.isDebugEnabled()){
				logger.debug("Adding resource from metadata: " + resource.getName());
			}
			results.add(resource);
		}
		return results;
	}

	/**
	 * Retrieve a property from the message, handling any parsing / xpath errors
	 * @param resourceMessage
	 * @param psNamespaces
	 * @param psPropertyName
	 * @return property value, null if not found
	 */
	private String extractMessageProperty(Message resourceMessage, List<String> psNamespaces, String psPropertyName)  {
		try {
			return messageUtils.extractConceptAsString(resourceMessage, psNamespaces, psPropertyName, true);
		} catch(Exception e){
			//this is expected behaviour for properties not available in certain protocols
			logger.debug(e.getMessage());
		}
		return null;
	}

	/**
	 * @return Returns the messageUtils.
	 */
	public MessageUtils getMessageUtils() {
		return messageUtils;
	}

	/**
	 * @param messageUtils The messageUtils to set.
	 */
	public void setMessageUtils(MessageUtils messageUtils) {
		this.messageUtils = messageUtils;
	}

	/**
	 * @return Returns the propertyStore.
	 */
	public PropertyStore getPropertyStore() {
		return propertyStore;
	}

	/**
	 * @param propertyStore The propertyStore to set.
	 */
	public void setPropertyStore(PropertyStore propertyStore) {
		this.propertyStore = propertyStore;
	}

	/**
	 * @param psPropertyPrefix the psPropertyPrefix to set
	 */
	public void setPsPropertyPrefix(String psPropertyPrefix) {
		this.psPropertyPrefix = psPropertyPrefix;
	}
}
