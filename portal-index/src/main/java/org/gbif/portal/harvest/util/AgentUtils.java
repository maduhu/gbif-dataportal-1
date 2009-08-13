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
package org.gbif.portal.harvest.util;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dao.AgentDAO;
import org.gbif.portal.model.Agent;

/**
 * Utilities for synchronising and handling Agents to the database
 * @author trobertson
 */
public class AgentUtils {
	/**
	 * DAOs
	 */
	protected AgentDAO agentDAO;
	
	/**
	 * To map to the integer types
	 */
	protected Map<String, Integer> agentTypeMapping;

	/**
	 * Logger
	 */
	protected Log logger = LogFactory.getLog(AgentUtils.class);
	
	/**
	 * @param name Of the agent
	 * @param address Of the agent
	 * @param emailOf the agent
	 * @param dataResourceId To ensure it is attached to
	 */
	public void ensureAgentIsAttachedToResource(String name, String address, String email, String telephone, String agentType, long dataResourceId) {
		name = StringUtils.trimToNull(name);
		address = StringUtils.trimToNull(address);
		email = StringUtils.trimToNull(email);
		telephone = StringUtils.trimToNull(telephone);
		
		if (name!=null && email!=null) {
			Agent agent = agentDAO.getByNameAndEmail(name, email);
			long id = -1;
			Integer type = agentTypeMapping.get(agentType);
			if (type==null) {
				type = new Integer(0); //  unknown
			}
			if (agent == null) {
				agent = new Agent();
				agent.setAddress(address);
				agent.setEmail(email);
				agent.setName(name);
				agent.setTelephone(telephone);
				logger.info("Creating new agent: " + agent);
				id = agentDAO.create(agent);
			} else {
				logger.info("Updating agent: " + agent);
				agentDAO.updateOrCreate(agent);
				id = agent.getId();
			}
			if (!agentDAO.isAgentAssociatedWithResource(dataResourceId, id, type)) {
				agentDAO.associateAgentWithResource(dataResourceId, id, type);
			}
		}
	}
	
	/**
	 * @param name Of the agent
	 * @param address Of the agent
	 * @param emailOf the agent
	 * @param dataProviderId To ensure it is attached to
	 * @return true if it was added, or false if it was updated or not added for some reason (e.g. no type)
	 */
	public boolean ensureAgentIsAttachedToProvider(String name, String address, String email, String telephone, String agentType, long dataProviderId) {
		Agent agent = agentDAO.getByNameAndEmail(name, email);
		long id = -1;
		Integer type = agentTypeMapping.get(agentType);
		if (type!=null) {
			if (agent == null) {
				agent = new Agent();
				agent.setAddress(address);
				agent.setEmail(email);
				agent.setName(name);
				agent.setTelephone(telephone);
				
				id = agentDAO.create(agent);
			} else {
				id = agent.getId();
				// TODO - update here!!!
				logger.warn("Need to code for updating the agent details - TODO!");
			}
			if (!agentDAO.isAgentAssociatedWithProvider(dataProviderId, id, type)) {
				agentDAO.associateAgentWithProvider(dataProviderId, id, type);
				return true;
			} else {
				return false;
			}
		} else {
			logger.warn("Agent type not supported: " + agentType);
			return false;
		}
	}
	
	/**
	 * @return Returns the agentDAO.
	 */
	public AgentDAO getAgentDAO() {
		return agentDAO;
	}

	/**
	 * @param agentDAO The agentDAO to set.
	 */
	public void setAgentDAO(AgentDAO agentDAO) {
		this.agentDAO = agentDAO;
	}

	/**
	 * @return Returns the agentTypeMapping.
	 */
	public Map<String, Integer> getAgentTypeMapping() {
		return agentTypeMapping;
	}

	/**
	 * @param agentTypeMapping The agentTypeMapping to set.
	 */
	public void setAgentTypeMapping(Map<String, Integer> agentTypeMapping) {
		this.agentTypeMapping = agentTypeMapping;
	}

}
