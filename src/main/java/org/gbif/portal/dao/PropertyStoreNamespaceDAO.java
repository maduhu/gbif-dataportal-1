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

import java.util.List;

/**
 * The Property Store Namespace DAO
 * @author trobertson
 */
public interface PropertyStoreNamespaceDAO {
	/**
	 * Creates a new mapping for the given resourceAccessPoint and for the namespaces provided
	 * The namespaces are created, if they don't exist 
	 * @param resourceAccessPointId To associate the mappings with
	 * @param namespaces To associate
	 */
	public void createNamespaceMappings(long resourceAccessPointId, List<String> namespaces);
	
	/**
	 * Creates a new mappings for the given resourceAccessPoint and for the namespaces provided
	 * The namespaces are created, if they don't exist, and old mappings may optionally be removed 
	 * @param resourceAccessPointId To associate the mappings with
	 * @param namespaces To associate
	 * @param deleteOldMappings If a delete of any extra mappings should occur
	 */
	public void createNamespaceMappings(long resourceAccessPointId, List<String> namespaces, boolean deleteOldMappings);
	
	/**
	 * Returns the list of namespaces that the ResourceAccessPoint has 
	 * @param resourceAccessPointId To get the namespaces of
	 * @return The namespaces
	 */
	public List<String> getNamespacesForResourceAccessPoint(long resourceAccessPointId);
	
	/**
	 * Will ensure that the namespace is attached to the resource access point provided
	 * if it is not attached (with the lowest priority)
	 * @param resourceAccessPointId To attach to  
	 * @param namespace To attach
	 */
	public void appendNamespaceIfNotAttached(long resourceAccessPointId, String namespace);
}
