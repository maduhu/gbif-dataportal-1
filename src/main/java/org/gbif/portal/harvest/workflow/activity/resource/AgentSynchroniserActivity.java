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
package org.gbif.portal.harvest.workflow.activity.resource;

import java.util.List;

import org.gbif.portal.harvest.util.AgentUtils;
import org.gbif.portal.util.mhf.message.Message;
import org.gbif.portal.util.mhf.message.MessageUtils;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * Synchronises the agents from the message 
 * 
 * @author trobertson
 */
public class AgentSynchroniserActivity extends BaseActivity {
	/**
	 * The Message to pull data from 
	 */
	protected String contextKeyMessage;
	
	/**
	 * To get the PS namespace that we are working in 
	 */
	protected String contextKeyPsNamespaces;
	
	/**
	 * The id to attach to (provider or resource)
	 */
	protected String contextKeyId;
	
	/**
	 * Property store message accessor keys 
	 */
	protected String psKeyName;
	protected String psKeyEmail;
	protected String psKeyAddress;
	protected String psKeyTelephone;
	protected String psKeyType;
	// can be null, meaning use the message as root
	protected String psAgentRoot;
	

	/**
	 * Flag for provider (otherwise it is a resource agent)
	 */
	protected boolean providerAgent = false;
	
	/**
	 * Utils
	 */
	protected MessageUtils messageUtils;
	protected AgentUtils agentUtils;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		Message message = (Message) context.get(contextKeyMessage, Message.class, true);
		List<String> namespaces = (List<String>) context.get(contextKeyPsNamespaces, List.class, true);
		
		if (psAgentRoot == null) {
			synchroniseAgent(message, namespaces, (Long)context.get(contextKeyId, Long.class, true));
			// TODO - handle a delete perhaps? - how, do we delete any others - can we decide this here?
		} else {
			List<Message> agentMessages = messageUtils.extractSubMessageList(message, namespaces, psAgentRoot, true);
			for (Message agentMessage : agentMessages) {
				logger.debug("Handling a new agent");
				synchroniseAgent(agentMessage, namespaces, (Long)context.get(contextKeyId, Long.class, true));
			}		
			// TODO - handle a delete perhaps? leaving for now since aiming for launch...
		}
		return context;
	}

	/**
	 * @param message To extract from
	 * @param namespaces The namespaces
	 * @param id To use (resource or provider)
	 */
	private void synchroniseAgent(Message message, List<String> namespaces, long id) {
		try {
			logger.debug("Getting the agent details from the message");
			String name = messageUtils.extractConceptAsString(message, namespaces, psKeyName, true);
			String email = messageUtils.extractConceptAsString(message, namespaces, psKeyEmail, true);
			String telephone = messageUtils.extractConceptAsString(message, namespaces, psKeyTelephone, false);
			String address = messageUtils.extractConceptAsString(message, namespaces, psKeyAddress, false);
			String type = messageUtils.extractConceptAsString(message, namespaces, psKeyType, false);
			
			if (providerAgent) {
				agentUtils.ensureAgentIsAttachedToProvider(name, address, email, telephone, type, id);
			} else {
				agentUtils.ensureAgentIsAttachedToResource(name, address, email, telephone, type, id);
			}			
			
		} catch (Exception e) {
			logger.warn("Ignoring agent, and continuing processing", e);
		}
		
	}

	/**
	 * @return Returns the agentUtils.
	 */
	public AgentUtils getAgentUtils() {
		return agentUtils;
	}

	/**
	 * @param agentUtils The agentUtils to set.
	 */
	public void setAgentUtils(AgentUtils agentUtils) {
		this.agentUtils = agentUtils;
	}

	/**
	 * @return Returns the contextKeyId.
	 */
	public String getContextKeyId() {
		return contextKeyId;
	}

	/**
	 * @param contextKeyId The contextKeyId to set.
	 */
	public void setContextKeyId(String contextKeyId) {
		this.contextKeyId = contextKeyId;
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
	 * @return Returns the providerAgent.
	 */
	public boolean isProviderAgent() {
		return providerAgent;
	}

	/**
	 * @param providerAgent The providerAgent to set.
	 */
	public void setProviderAgent(boolean providerAgent) {
		this.providerAgent = providerAgent;
	}

	/**
	 * @return Returns the psAgentRoot.
	 */
	public String getPsAgentRoot() {
		return psAgentRoot;
	}

	/**
	 * @param psAgentRoot The psAgentRoot to set.
	 */
	public void setPsAgentRoot(String psAgentRoot) {
		this.psAgentRoot = psAgentRoot;
	}

	/**
	 * @return Returns the psKeyAddress.
	 */
	public String getPsKeyAddress() {
		return psKeyAddress;
	}

	/**
	 * @param psKeyAddress The psKeyAddress to set.
	 */
	public void setPsKeyAddress(String psKeyAddress) {
		this.psKeyAddress = psKeyAddress;
	}

	/**
	 * @return Returns the psKeyEmail.
	 */
	public String getPsKeyEmail() {
		return psKeyEmail;
	}

	/**
	 * @param psKeyEmail The psKeyEmail to set.
	 */
	public void setPsKeyEmail(String psKeyEmail) {
		this.psKeyEmail = psKeyEmail;
	}

	/**
	 * @return Returns the psKeyName.
	 */
	public String getPsKeyName() {
		return psKeyName;
	}

	/**
	 * @param psKeyName The psKeyName to set.
	 */
	public void setPsKeyName(String psKeyName) {
		this.psKeyName = psKeyName;
	}

	/**
	 * @return Returns the psKeyTelephone.
	 */
	public String getPsKeyTelephone() {
		return psKeyTelephone;
	}

	/**
	 * @param psKeyTelephone The psKeyTelephone to set.
	 */
	public void setPsKeyTelephone(String psKeyTelephone) {
		this.psKeyTelephone = psKeyTelephone;
	}

	/**
	 * @return Returns the psKeyType.
	 */
	public String getPsKeyType() {
		return psKeyType;
	}

	/**
	 * @param psKeyType The psKeyType to set.
	 */
	public void setPsKeyType(String psKeyType) {
		this.psKeyType = psKeyType;
	}
}
