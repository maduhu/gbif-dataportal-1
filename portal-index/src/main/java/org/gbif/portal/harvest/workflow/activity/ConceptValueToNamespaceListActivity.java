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
package org.gbif.portal.harvest.workflow.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gbif.portal.util.mhf.message.Message;
import org.gbif.portal.util.mhf.message.MessageUtils;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * An activity that will look for a concept in a message and convert it to a list of 
 * property store namespaces.
 * 
 * Used for example in a DIGiR metadata response to determine the namespaces that
 * the metadata is describing
 * 
 * @author trobertson
 */
public class ConceptValueToNamespaceListActivity extends BaseActivity implements
		Activity {	
	/**
	 * Context Keys
	 */
	protected String contextKeyMessage;
	protected String contextKeyPsNamespaces;
	
	/**
	 * The property store key for the concept to extract
	 */
	protected String propertyStoreKeyConcept;
	
	/**
	 * The mapping to use
	 */
	Map<Set<String>, List<String>> mapping = new HashMap<Set<String>, List<String>>();
	
	/**
	 * Utils
	 */
	protected MessageUtils messageUtils;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		List<String> psNamespaces = (List<String>)context.get(getContextKeyPsNamespaces(), List.class, true);
		Message message = (Message) context.get(getContextKeyMessage(), Message.class, true);
		String concept = messageUtils.extractConceptAsString(message, psNamespaces, propertyStoreKeyConcept, true);
		
		boolean found = false;
		Set<Set<String>> keys = mapping.keySet();
		for (Set<String> key : keys) {
			if (key.contains(concept)) {
				logger.info("Setting new property store namespaces: " + mapping.get(concept));
				context.put(getContextKeyPsNamespaces(), mapping.get(key));
				found = true;
			}
		}
		
		if (!found) {
			logger.warn(concept + " is not mapped to a PS namespace list");
		}
		
		return context;
	}

	/**
	 * @return Returns the contextKeyMessage.
	 */
	public String getContextKeyMessage() {
		return contextKeyMessage;
	}

	/**
	 * @param contextKeyMessage The contextKeyMessage to set.
	 */
	public void setContextKeyMessage(String contextKeyMessage) {
		this.contextKeyMessage = contextKeyMessage;
	}

	/**
	 * @return Returns the contextKeyPsNamespaces.
	 */
	public String getContextKeyPsNamespaces() {
		return contextKeyPsNamespaces;
	}

	/**
	 * @param contextKeyPsNamespaces The contextKeyPsNamespaces to set.
	 */
	public void setContextKeyPsNamespaces(String contextKeyPsNamespaces) {
		this.contextKeyPsNamespaces = contextKeyPsNamespaces;
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
	 * @return Returns the propertyStoreKeyConcept.
	 */
	public String getPropertyStoreKeyConcept() {
		return propertyStoreKeyConcept;
	}

	/**
	 * @param propertyStoreKeyConcept The propertyStoreKeyConcept to set.
	 */
	public void setPropertyStoreKeyConcept(String propertyStoreKeyConcept) {
		this.propertyStoreKeyConcept = propertyStoreKeyConcept;
	}

	/**
	 * @return Returns the mapping.
	 */
	public Map<Set<String>, List<String>> getMapping() {
		return mapping;
	}

	/**
	 * @param mapping The mapping to set.
	 */
	public void setMapping(Map<Set<String>, List<String>> mapping) {
		this.mapping = mapping;
	}
}
