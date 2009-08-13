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
package org.gbif.portal.dto.resources;

import java.io.Serializable;

/**
 * AgentDTO
 * 
 * @author Donald Hobern
 */
public class DataProviderAgentDTO  implements Serializable {
	
	/**
	 * Generated 
	 */
	private static final long serialVersionUID = -7922106864289155372L;

	protected String key;
	protected String dataProviderKey;
	protected String agentKey;
	protected String agentName;
	protected String agentAddress;
	protected String agentEmail;
	protected String agentTelephone;
	protected Integer agentType;
	/**
	 * @return the agentAddress
	 */
	public String getAgentAddress() {
		return agentAddress;
	}
	/**
	 * @param agentAddress the agentAddress to set
	 */
	public void setAgentAddress(String agentAddress) {
		this.agentAddress = agentAddress;
	}
	/**
	 * @return the agentEmail
	 */
	public String getAgentEmail() {
		return agentEmail;
	}
	/**
	 * @param agentEmail the agentEmail to set
	 */
	public void setAgentEmail(String agentEmail) {
		this.agentEmail = agentEmail;
	}
	/**
	 * @return the agentKey
	 */
	public String getAgentKey() {
		return agentKey;
	}
	/**
	 * @param agentKey the agentKey to set
	 */
	public void setAgentKey(String agentKey) {
		this.agentKey = agentKey;
	}
	/**
	 * @return the agentName
	 */
	public String getAgentName() {
		return agentName;
	}
	/**
	 * @param agentName the agentName to set
	 */
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	/**
	 * @return the agentTelephone
	 */
	public String getAgentTelephone() {
		return agentTelephone;
	}
	/**
	 * @param agentTelephone the agentTelephone to set
	 */
	public void setAgentTelephone(String agentTelephone) {
		this.agentTelephone = agentTelephone;
	}
	/**
	 * @return the agentType
	 */
	public Integer getAgentType() {
		return agentType;
	}
	/**
	 * @param agentType the agentType to set
	 */
	public void setAgentType(Integer agentType) {
		this.agentType = agentType;
	}
	/**
	 * @return the dataProviderKey
	 */
	public String getDataProviderKey() {
		return dataProviderKey;
	}
	/**
	 * @param dataProviderKey the dataProviderKey to set
	 */
	public void setDataProviderKey(String dataProviderKey) {
		this.dataProviderKey = dataProviderKey;
	}
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
}