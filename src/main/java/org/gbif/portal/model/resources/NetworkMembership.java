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
 * NetworkMembership
 * 
 * Represents a resource network which may include data resources from 
 * various data providers
 * 
 * Object representation of the NetworkMembership data model concept.
 * 
 * @author Donald Hobern
 */
public class NetworkMembership extends BaseObject {
	
	/** The data resource */
	protected DataResource dataResource;
	/** The resource network */
	protected ResourceNetwork resourceNetwork;

	/** The data resource id */
	protected Long dataResourceId;
	/** The resource network id */
	protected Long resourceNetworkId;	
	
	/**
	 * Default Constructor.
	 */
	public NetworkMembership(){}
	
	/**
	 * Initialises the ids.
	 */
	public NetworkMembership(long id, DataResource dataResource, ResourceNetwork resourceNetwork){
		this.id = id;
		this.dataResource = dataResource;
		this.resourceNetwork = resourceNetwork;
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

	/**
	 * @return the resourceNetwork
	 */
	public ResourceNetwork getResourceNetwork() {
		return resourceNetwork;
	}

	/**
	 * @param resourceNetwork the resourceNetwork to set
	 */
	public void setResourceNetwork(ResourceNetwork resourceNetwork) {
		this.resourceNetwork = resourceNetwork;
	}

	/**
	 * @return the dataResourceId
	 */
	public Long getDataResourceId() {
		return dataResourceId;
	}

	/**
	 * @param dataResourceId the dataResourceId to set
	 */
	public void setDataResourceId(Long dataResourceId) {
		this.dataResourceId = dataResourceId;
	}

	/**
	 * @return the resourceNetworkId
	 */
	public Long getResourceNetworkId() {
		return resourceNetworkId;
	}

	/**
	 * @param resourceNetworkId the resourceNetworkId to set
	 */
	public void setResourceNetworkId(Long resourceNetworkId) {
		this.resourceNetworkId = resourceNetworkId;
	}
}