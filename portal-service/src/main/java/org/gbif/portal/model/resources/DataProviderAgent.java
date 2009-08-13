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
 * DataProviderAgent
 * 
 * Represents the relationship between a data provider and an agent 
 * 
 * Object representation of the DataProviderAgent data model concept.
 * 
 * @author Donald Hobern
 */
public class DataProviderAgent extends BaseObject {
	
	/**The data provider**/
	protected DataProvider dataProvider;
	/**The agent**/
	protected Agent agent;
	/**The agent type**/
	protected AgentType agentType;

	/**
	 * Default Constructor.
	 */
	public DataProviderAgent(){}
	
	/**
	 * Initialises the ids.
	 */
	public DataProviderAgent(long id, DataProvider dataProvider, Agent agent, AgentType agentType){
		this.id = id;
		this.dataProvider = dataProvider;
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
	 * @return the dataProvider
	 */
	public DataProvider getDataProvider() {
		return dataProvider;
	}

	/**
	 * @param dataProvider the dataProvider to set
	 */
	public void setDataProvider(DataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

}
