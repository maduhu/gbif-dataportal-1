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
package org.gbif.portal.harvest.workflow.activity;

import java.util.List;

import org.gbif.portal.dao.ResourceAccessPointDAO;
import org.gbif.portal.model.ResourceAccessPoint;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * An activity that will turn a single resource access point into multiple for each of the given 
 * identifier in the context
 * 
 * If the resource access point already has an identifier, then nothing is done
 * 
 * @author trobertson
 */
public class ResourceAccessPointToMultipleActivity extends BaseActivity implements
		Activity {	
	/**
	 * Context Keys
	 */
	protected String contextKeyResourceAccessPointId;
	protected String contextKeyIdentifierList;
	
	/**
	 * DAOs
	 */
	protected ResourceAccessPointDAO resourceAccessPointDAO;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		long resourceAccessPointId = ((Long) context.get(getContextKeyResourceAccessPointId(), Long.class, true)).longValue();
		List<String> remoteIdentifiers = (List<String>) context.get(getContextKeyIdentifierList(), List.class, true);
		ResourceAccessPoint resourceAccessPoint = resourceAccessPointDAO.getById(resourceAccessPointId);
		if (resourceAccessPoint != null) {
			if (resourceAccessPoint.getRemoteIdAtUrl() == null) {
				boolean first = true;
				for (String identifier : remoteIdentifiers) {
					resourceAccessPoint.setRemoteIdAtUrl(identifier);
					if (first) {
						resourceAccessPointDAO.updateOrCreate(resourceAccessPoint);
						first = false;
					} else {
						// for safety
						resourceAccessPoint.setId(-1);
						resourceAccessPointDAO.create(resourceAccessPoint);
					}					
				}
				
			} else {
				logger.debug("ResourceAccessPoint[" + resourceAccessPointId + "] already has a remoteIdentifier[" + resourceAccessPoint.getRemoteIdAtUrl() + "] so ignoring");
			}
		} else {
			logger.warn("ResourceAccessPoint does not exist for id: " + resourceAccessPointId);
		}		
		
		return context;
	}

	/**
	 * @return Returns the contextKeyIdentifierList.
	 */
	public String getContextKeyIdentifierList() {
		return contextKeyIdentifierList;
	}

	/**
	 * @param contextKeyIdentifierList The contextKeyIdentifierList to set.
	 */
	public void setContextKeyIdentifierList(String contextKeyIdentifierList) {
		this.contextKeyIdentifierList = contextKeyIdentifierList;
	}

	/**
	 * @return Returns the contextKeyResourceAccessPointId.
	 */
	public String getContextKeyResourceAccessPointId() {
		return contextKeyResourceAccessPointId;
	}

	/**
	 * @param contextKeyResourceAccessPointId The contextKeyResourceAccessPointId to set.
	 */
	public void setContextKeyResourceAccessPointId(
			String contextKeyResourceAccessPointId) {
		this.contextKeyResourceAccessPointId = contextKeyResourceAccessPointId;
	}

	/**
	 * @return Returns the resourceAccessPointDAO.
	 */
	public ResourceAccessPointDAO getResourceAccessPointDAO() {
		return resourceAccessPointDAO;
	}

	/**
	 * @param resourceAccessPointDAO The resourceAccessPointDAO to set.
	 */
	public void setResourceAccessPointDAO(
			ResourceAccessPointDAO resourceAccessPointDAO) {
		this.resourceAccessPointDAO = resourceAccessPointDAO;
	}
}
