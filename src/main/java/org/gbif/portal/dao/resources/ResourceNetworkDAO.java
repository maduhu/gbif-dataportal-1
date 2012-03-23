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
package org.gbif.portal.dao.resources;

import java.util.Date;
import java.util.List;

import org.gbif.portal.model.resources.ResourceNetwork;

/**
 * DAO methods for the ResourceNetwork entity.
 * 
 * @author dmartin
 */
public interface ResourceNetworkDAO {
	
	/**
	 * Returns the ResourceNetwork with the specified id value.
	 * 
	 * @param ResourceNetworkId The id of the resource network
	 * @return The ResourceNetwork object for this id.
	 */
	public ResourceNetwork getResourceNetworkFor(long resourceNetworkId);

	/**
	 * Retrieve a list of resource networks including the supplied resource 
	 *
	 * @param dataResource the resource to check
	 * @return a list of ResourceNetwork model objects
	 */
	public List<ResourceNetwork> getResourceNetworksForDataResource(long dataResourceId);

	/**
	 * Retrieve a list of resource networks matching the parameters
	 *
	 * @param nameStub the name to search for
	 * @param fuzzy whether to do a wildcard search
	 * @param code short identifier for network
	 * @param startIndex the result to start at
	 * @param maxResults max number of results to return
	 * @return a list of ResourceNetwork model objects
	 */
	public List<ResourceNetwork> findResourceNetworks(String nameStub, boolean fuzzy, String code, Date modifiedSince, int startIndex, int maxResults);

	/**
	 * Count resource networks matching the supplied name 
	 *
	 * @param nameStub the name to search for
	 * @param fuzzy whether to do a wildcard search
	 * @param code short identifier for network
	 * @param startIndex the result to start at
	 * @param maxResults max number of results to return
	 * @return count of ResourceNetwork model objects
	 */
	public Long countResourceNetworks(String nameStub, boolean fuzzy, String code, Date modifiedSince);

	/**
	 * Retrieve a list of resource networks
	 * 
	 * @return list of resource networks
	 */
	public List getResourceNetworkList();
}