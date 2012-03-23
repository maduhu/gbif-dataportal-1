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

import org.gbif.portal.dao.ResourceAccessPointDAO;
import org.gbif.portal.model.ResourceAccessPoint;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * An activity that will typically be used as the first in harvesting.
 * For seed data would include the ResourceAccessPoint ID, and this activity
 * will populate the data provider, and namespaces required for harvesting.
 * 
 * @author trobertson
 */
public class HarvestLaunchActivity extends BaseActivity implements
		Activity {	
	/**
	 * Context Keys
	 */
	protected String contextKeyDataProviderId;
	protected String contextKeyDataResourceId;
	protected String contextKeyResourceAccessPointId;
	protected String contextKeyPsNamespaces;
	protected String contextKeyUrl;
	protected String contextKeyRemoteIdentifier;
	protected String contextKeyCount;
	protected String contextKeyAddedCount;
	protected String contextKeyUpdatedCount;
	protected String contextKeySupportsDateLastModified;
	protected String contextKeyDateLastHarvestStarted;
	protected String contextKeyDateLastExtractStarted;
	
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
		ResourceAccessPoint resourceAccessPoint = resourceAccessPointDAO.getById(resourceAccessPointId);
		context.put(getContextKeyDataProviderId(), resourceAccessPoint.getDataProviderId());
		context.put(getContextKeyDataResourceId(), resourceAccessPoint.getDataResourceId());
		context.put(getContextKeyPsNamespaces(), resourceAccessPoint.getNamespaces());
		context.put(getContextKeyUrl(), resourceAccessPoint.getUrl());
		context.put(getContextKeyRemoteIdentifier(), resourceAccessPoint.getRemoteIdAtUrl());
		context.put(getContextKeySupportsDateLastModified(), resourceAccessPoint.isSupportsDateLastModified());
		context.put(getContextKeyDateLastHarvestStarted(), resourceAccessPoint.getDateLastHarvestStarted());
		context.put(getContextKeyDateLastExtractStarted(), resourceAccessPoint.getDateLastExtractStarted());
		context.put(getContextKeyAddedCount(), new Integer(0));
		context.put(getContextKeyUpdatedCount(), new Integer(0));

		logger.info("DataProviderID: " + resourceAccessPoint.getDataProviderId()); 
		logger.info("ResourceAccessPointID: " + resourceAccessPointId);
		logger.info("DataResourceID: " + resourceAccessPoint.getDataResourceId());
		logger.info("URL: " + resourceAccessPoint.getUrl());
		logger.info("RemoteID: " + resourceAccessPoint.getRemoteIdAtUrl());
		logger.info("PSNamespaces: " + resourceAccessPoint.getNamespaces());
		Integer startAt = (Integer) context.get(getContextKeyCount(), Integer.class, false);
		if (startAt == null) {
			startAt = new Integer(0);
			context.put(getContextKeyCount(), new Integer(0));
		}
		logger.info("StartingAt: " + startAt);
		
		return context;
	}	

	/**
	 * @return Returns the contextKeyPsNamespaces.
	 */
	public String getContextKeyPsNamespaces() {
		return contextKeyPsNamespaces;
	}


	/**
	 * @param contextKeyPsNamespaces The contextKeyPsNamespaces to set.
	 */
	public void setContextKeyPsNamespaces(String contextKeyPsNamespaces) {
		this.contextKeyPsNamespaces = contextKeyPsNamespaces;
	}

	/**
	 * @return Returns the contextKeyDataProviderId.
	 */
	public String getContextKeyDataProviderId() {
		return contextKeyDataProviderId;
	}

	/**
	 * @param contextKeyDataProviderId The contextKeyDataProviderId to set.
	 */
	public void setContextKeyDataProviderId(String contextKeyDataProviderId) {
		this.contextKeyDataProviderId = contextKeyDataProviderId;
	}

	/**
	 * @return Returns the contextKeyDataResourceId.
	 */
	public String getContextKeyDataResourceId() {
		return contextKeyDataResourceId;
	}

	/**
	 * @param contextKeyDataResourceId The contextKeyDataResourceId to set.
	 */
	public void setContextKeyDataResourceId(String contextKeyDataResourceId) {
		this.contextKeyDataResourceId = contextKeyDataResourceId;
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

	/**
	 * @return Returns the contextKeyUrl.
	 */
	public String getContextKeyUrl() {
		return contextKeyUrl;
	}

	/**
	 * @param contextKeyUrl The contextKeyUrl to set.
	 */
	public void setContextKeyUrl(String contextKeyUrl) {
		this.contextKeyUrl = contextKeyUrl;
	}

	/**
	 * @return Returns the contextKeyRemoteIdentifier.
	 */
	public String getContextKeyRemoteIdentifier() {
		return contextKeyRemoteIdentifier;
	}

	/**
	 * @param contextKeyRemoteIdentifier The contextKeyRemoteIdentifier to set.
	 */
	public void setContextKeyRemoteIdentifier(String contextKeyRemoteIdentifier) {
		this.contextKeyRemoteIdentifier = contextKeyRemoteIdentifier;
	}

	/**
	 * @return Returns the contextKeyCount.
	 */
	public String getContextKeyCount() {
		return contextKeyCount;
	}

	/**
	 * @param contextKeyCount The contextKeyCount to set.
	 */
	public void setContextKeyCount(String contextKeyCount) {
		this.contextKeyCount = contextKeyCount;
	}

	/**
	 * @return Returns the contextKeySupportsDateLastModified.
	 */
	public String getContextKeySupportsDateLastModified() {
		return contextKeySupportsDateLastModified;
	}

	/**
	 * @param contextKeySupportsDateLastModified The contextKeySupportsDateLastModified to set.
	 */
	public void setContextKeySupportsDateLastModified(
			String contextKeySupportsDateLastModified) {
		this.contextKeySupportsDateLastModified = contextKeySupportsDateLastModified;
	}

	/**
	 * @return Returns the contextKeyDateLastExtractStarted.
	 */
	public String getContextKeyDateLastExtractStarted() {
		return contextKeyDateLastExtractStarted;
	}

	/**
	 * @param contextKeyDateLastExtractStarted The contextKeyDateLastExtractStarted to set.
	 */
	public void setContextKeyDateLastExtractStarted(
			String contextKeyDateLastExtractStarted) {
		this.contextKeyDateLastExtractStarted = contextKeyDateLastExtractStarted;
	}

	/**
	 * @return Returns the contextKeyDateLastHarvestStarted.
	 */
	public String getContextKeyDateLastHarvestStarted() {
		return contextKeyDateLastHarvestStarted;
	}

	/**
	 * @param contextKeyDateLastHarvestStarted The contextKeyDateLastHarvestStarted to set.
	 */
	public void setContextKeyDateLastHarvestStarted(
			String contextKeyDateLastHarvestStarted) {
		this.contextKeyDateLastHarvestStarted = contextKeyDateLastHarvestStarted;
	}

	/**
	 * @return the contextKeyAddedCount
	 */
	public String getContextKeyAddedCount() {
		return contextKeyAddedCount;
	}

	/**
	 * @param contextKeyAddedCount the contextKeyAddedCount to set
	 */
	public void setContextKeyAddedCount(String contextKeyAddedCount) {
		this.contextKeyAddedCount = contextKeyAddedCount;
	}

	/**
	 * @return the contextKeyUpdatedCount
	 */
	public String getContextKeyUpdatedCount() {
		return contextKeyUpdatedCount;
	}

	/**
	 * @param contextKeyUpdatedCount the contextKeyUpdatedCount to set
	 */
	public void setContextKeyUpdatedCount(String contextKeyUpdatedCount) {
		this.contextKeyUpdatedCount = contextKeyUpdatedCount;
	}
}
