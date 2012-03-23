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

import java.util.List;

import org.gbif.portal.model.resources.DataProviderAgent;
import org.gbif.portal.model.resources.DataResourceAgent;

/**
 * DAO Implementation for accessing agent model objects.
 * 
 * @author Donald Hobern
 */
public interface AgentDAO {
	
	/**
	 * Returns the Agents for an data provider.
	 * 
	 * @param dataProviderId identifier for an data provider
	 * @return List of Agent objects.
	 */
	public List<DataProviderAgent> getAgentsForDataProvider(long dataProviderId);
	
	/**
	 * Returns the Agents for an data resource.
	 * 
	 * @param dataResourceId identifier for an data resource
	 * @return List of Agent objects.
	 */
	public List<DataResourceAgent> getAgentsForDataResource(long dataResourceId);

	/**
	 * Retrieve agent(s) for the supplied email address
	 * @param email
	 * @return
	 */
	public List getAgentsForEmailAddress(String email);
}