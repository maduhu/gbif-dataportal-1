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
package org.gbif.portal.dao;

import org.gbif.portal.model.Agent;

/**
 * Methods for dealing Agent model objects
 * @author trobertson
 */
public interface AgentDAO {
	/**
	 * @param agent To create
	 * @return The id of the newly created agent
	 */
	public long create(Agent agent);
	
	/**
	 * @param agent To create or update
	 * @return The id of the newly created agent or the id of the one updated
	 */
	public long updateOrCreate(Agent agent);
	
	/**
	 * Gets the Agent for the given name and email
	 * @param name for the agent
	 * @param email for the agent
	 * @return The Adgent or null
	 */
	public Agent getByNameAndEmail(String name, String email);
	
	/**
	 * Determines if the agent is with the resource
	 * @param dataResourceId 
	 * @param agentId The id
	 * @param agentType The type
	 */
	public boolean isAgentAssociatedWithResource(long dataResourceId, long agentId, int agentType);
	
	/**
	 * Determines if the agent is with the provider
	 * @param dataProviderId To join to
	 * @param agentId The id
	 * @param agentType The type
	 */
	public boolean isAgentAssociatedWithProvider(long dataProviderId, long agentId, int agentType);
	
	/**
	 * Joins the agent to the resource
	 * @param dataResourceId To join to
	 * @param agentId The id
	 * @param agentType The type
	 */
	public void associateAgentWithResource(long dataResourceId, long agentId, int agentType);
	
	/**
	 * Joins the agent to the provider
	 * @param dataProviderId To join to
	 * @param agentId The id
	 * @param agentType The type
	 */
	public void associateAgentWithProvider(long dataProviderId, long agentId, int agentType);
	
	/**
	 * Removes the agent from the resource
	 * @param dataResourceId To join to
	 * @param agentId The id
	 * @param agentType The type
	 */
	public void disassociateAgentWithResource(long dataResourceId, long agentId, int agentType);
	
	/**
	 * Removes the agent from the provider
	 * @param dataProviderId To join to
	 * @param agentId The id
	 * @param agentType The type
	 */
	public void disassociateAgentWithProvider(long dataProviderId, long agentId, int agentType);
}
