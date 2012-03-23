/***************************************************************************
 * Copyright (C) 2006 Global Biodiversity Information Facility Secretariat.  
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
package org.gbif.portal.model.resources;

import org.gbif.portal.model.BaseObject;

/**
 * DataResourceAgent
 * 
 * Represents the relationship between a data resource and an agent 
 * 
 * Object representation of the DataResourceAgent data model concept.
 * 
 * @author Donald Hobern
 */
public class DataResourceAgent extends BaseObject {
	
	/**The data resource**/
	protected DataResource dataResource;
	/**The agent**/
	protected Agent agent;
	/**The agent type**/
	protected AgentType agentType;

	/**
	 * Default Constructor.
	 */
	public DataResourceAgent(){}
	
	/**
	 * Initialises the ids.
	 */
	public DataResourceAgent(long id, DataResource dataResource, Agent agent, AgentType agentType){
		this.id = id;
		this.dataResource = dataResource;
		this.agent = agent;
		this.agentType = agentType;
	}

	/**
	 * @return the agent
	 */
	public Agent getAgent() {
		return agent;
	}

	/**
	 * @param agent the agent to set
	 */
	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	/**
	 * @return the agentType
	 */
	public AgentType getAgentType() {
		return agentType;
	}

	/**
	 * @param agentType the agentType to set
	 */
	public void setAgentType(AgentType agentType) {
		this.agentType = agentType;
	}

	/**
	 * @return the dataResource
	 */
	public DataResource getDataResource() {
		return dataResource;
	}

	/**
	 * @param dataResource the dataResource to set
	 */
	public void setDataResource(DataResource dataResource) {
		this.dataResource = dataResource;
	}

}
