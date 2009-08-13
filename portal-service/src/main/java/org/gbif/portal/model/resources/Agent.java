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

import java.util.Date;
import java.util.Set;

import org.gbif.portal.model.BaseObject;

/**
 * DataProvider
 * 
 * Represents a GBIF Data Provider which may provide DataResources 
 * 
 * Object representation of the DataProvider data model concept.
 * http://wiki.gbif.org/dadiwiki/wikka.php?wakka=DataProvider
 * 
 * @author dbarnier
 * @author dmartin
 * 
 * @hibernate.class
 * 	table="data_provider"
 */
public class Agent extends BaseObject {
	
	/**The agent name**/
	protected String name;
	/**The agent type**/	
	protected Integer agentTypeCode;
	/**The agent address**/	
	protected String address;
	/**The agent email**/	
	protected String email;
	/**The data provider logo url**/	
	protected String telephone;
	/**Links to associated data resources**/
	protected Set<DataProviderAgent> dataProviderAgents;
	/**Links to associated data resources**/
	protected Set<DataResourceAgent> dataResourceAgents;
	/** The creation date for the provider object */
	protected Date created;	
	/** The last modification date for the provider object */
	protected Date modified;	
	/** The deletion date for the provider object */
	protected Date deleted;	

	/**
	 * Default Constructor.
	 */
	public Agent(){}
	
	/**
	 * Initialises.
	 */
	public Agent(long id, String name, String address, String email, String telephone){
		this.id = id;
		this.name = name;
		this.address = address;
		this.email = email;
		this.telephone = telephone;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the agentTypeCode
	 */
	public Integer getAgentTypeCode() {
		return agentTypeCode;
	}

	/**
	 * @param agentTypeCode the agentTypeCode to set
	 */
	public void setAgentTypeCode(Integer agentTypeCode) {
		this.agentTypeCode = agentTypeCode;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the deleted
	 */
	public Date getDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the modified
	 */
	public Date getModified() {
		return modified;
	}

	/**
	 * @param modified the modified to set
	 */
	public void setModified(Date modified) {
		this.modified = modified;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephone the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * @return the dataProviderAgents
	 */
	public Set<DataProviderAgent> getDataProviderAgents() {
		return dataProviderAgents;
	}

	/**
	 * @param dataProviderAgents the dataProviderAgents to set
	 */
	public void setDataProviderAgents(Set<DataProviderAgent> dataProviderAgents) {
		this.dataProviderAgents = dataProviderAgents;
	}

	/**
	 * @return the dataResourceAgents
	 */
	public Set<DataResourceAgent> getDataResourceAgents() {
		return dataResourceAgents;
	}

	/**
	 * @param dataResourceAgents the dataResourceAgents to set
	 */
	public void setDataResourceAgents(Set<DataResourceAgent> dataResourceAgents) {
		this.dataResourceAgents = dataResourceAgents;
	}	
}
