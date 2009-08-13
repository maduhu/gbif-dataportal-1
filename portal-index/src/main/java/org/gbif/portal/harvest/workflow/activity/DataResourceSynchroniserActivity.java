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

import org.gbif.portal.dao.DataResourceDAO;
import org.gbif.portal.dao.PropertyStoreNamespaceDAO;
import org.gbif.portal.dao.ResourceAccessPointDAO;
import org.gbif.portal.model.DataResource;
import org.gbif.portal.model.ResourceAccessPoint;
import org.gbif.portal.util.mapping.CodeMapping;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * An activity that will check that there is a DataResource for the given resource access point and 
 * the name.  If not, it is created along with an AccessPoint
 * 
 * @author trobertson
 */
public class DataResourceSynchroniserActivity extends BaseActivity implements
		Activity {	
	/**
	 * Unknown basis of record
	 */
	public static int UNKNOWN_BASIS_OF_RECORD = 0;
	
	/**
	 * Context Keys
	 */
	protected String contextKeyDataResourceName;
	protected String contextKeyDataProviderId;
	protected String contextKeyDataResourceId;
	protected String contextKeyResourceAccessPointId;
	protected String contextKeyUrl;
	protected String contextKeyRemoteIdentifier;
	protected String contextKeyBasisOfRecord;
	protected String contextKeyDescription;
	protected String contextKeyCitation;
	protected String contextKeyRights;
	protected String contextKeyLogoUrl;
	protected String contextKeyWebsiteUrl;
	protected String contextKeyProviderRecordCount;
	
	/**
	 * DAOs
	 */
	protected DataResourceDAO dataResourceDAO;
	protected ResourceAccessPointDAO resourceAccessPointDAO;
	protected PropertyStoreNamespaceDAO propertyStoreNamespaceDAO;
	
	/**
	 * Maps the basis of record strings to standard codes
	 */
	protected CodeMapping basisOfRecordMapping = null;
	
	/**
	 * Used to modify the existing RAP instead of creating a new one
	 */
	protected boolean updateExistingRAP = false;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		String dataResourceName = (String) context.get(getContextKeyDataResourceName(), String.class, true);
		long dataProviderId = ((Long) context.get(getContextKeyDataProviderId(), Long.class, true)).longValue();
		long resourceAccessPointId = ((Long) context.get(getContextKeyResourceAccessPointId(), Long.class, true)).longValue();
		String url = (String) context.get(getContextKeyUrl(), String.class, true);
		String remoteId = (String) context.get(getContextKeyRemoteIdentifier(), String.class, true);
		String basisOfRecord = (String) context.get(getContextKeyBasisOfRecord(), String.class, false);
		String description = (String) context.get(getContextKeyDescription(), String.class, false);
		String citation = (String) context.get(getContextKeyCitation(), String.class, false);
		String rights = (String) context.get(getContextKeyRights(), String.class, false);
		String logoUrl = (String) context.get(getContextKeyLogoUrl(), String.class, false);
		String websiteUrl = (String) context.get(getContextKeyWebsiteUrl(), String.class, false);
		String providerRecordCountText = (String) context.get(getContextKeyProviderRecordCount(), String.class, false);
		
		int basisOfRecordCode = UNKNOWN_BASIS_OF_RECORD;
		
		if (basisOfRecord != null) {
			basisOfRecordCode = basisOfRecordMapping.mapToCode(basisOfRecord).intValue();
		}

		Integer providerRecordCount = null;
		
		if (providerRecordCountText != null) {
			try {
				providerRecordCount = new Integer(providerRecordCountText);
			} catch (Exception e) {
				// ignore
			}
		}
		
		// See if it is in the database
		// This uses the "digir CODE" to find the resource hence making this a DiGIR only activity 
		//DataResource dataResource = dataResourceDAO.getByNameAndUrlForProvider(dataResourceName, url, dataProviderId);
		DataResource dataResource = dataResourceDAO.getByRemoteIdAtUrlAndUrlForProvider(remoteId, url, dataProviderId);				
		
		if (dataResource != null) {
			logger.info("DB contains DataResource: " + dataResourceName);
			context.put(getContextKeyDataResourceId(), new Long(dataResource.getId()));

			dataResource.setName(dataResourceName);
			dataResource.setBasisOfRecord(basisOfRecordCode);
			dataResource.setDescription(description);
			dataResource.setCitation(citation);
			dataResource.setRights(rights);
			dataResource.setWebsiteUrl(websiteUrl);
			dataResource.setLogoUrl(logoUrl);
			dataResource.setProviderRecordCount(providerRecordCount);
			long id = dataResourceDAO.updateOrCreate(dataResource);
			context.put(getContextKeyDataResourceId(), new Long(id));
			// get the RAP for this DR
			ResourceAccessPoint rap = resourceAccessPointDAO.getByRemoteIdAtUrlAndUrlForResource(remoteId, url, id);
			if (rap==null) {
				throw new Exception("Data corrupt - there is no RAP for the DR[" + id + "] with URL[" + url + "] and RemoteID[" + remoteId + "]");
			} else {
				context.put(getContextKeyResourceAccessPointId(), new Long(rap.getId()));
			}
			
		} else {
			logger.info("Creating new DataResource: " + dataResourceName);
			// need to create it, and make sure there exists a ResourceAccessPoint attempting to locate it
			dataResource = new DataResource();
			dataResource.setDataProviderId(dataProviderId);
			dataResource.setName(dataResourceName);
			dataResource.setBasisOfRecord(basisOfRecordCode);
			dataResource.setDescription(description);
			dataResource.setCitation(citation);
			dataResource.setRights(rights);
			dataResource.setWebsiteUrl(websiteUrl);
			dataResource.setLogoUrl(logoUrl);
			dataResource.setProviderRecordCount(providerRecordCount);
			long dataResourceId = dataResourceDAO.create(dataResource);
			context.put(getContextKeyDataResourceId(), new Long(dataResourceId));
			
			// DiGIR for example does this
			if (!updateExistingRAP) {
				ResourceAccessPoint resourceAccessPointToCopy = resourceAccessPointDAO.getById(resourceAccessPointId);
				ResourceAccessPoint resourceAccessPoint = new ResourceAccessPoint();
				resourceAccessPoint.setDataProviderId(dataProviderId);
				resourceAccessPoint.setDataResourceId(dataResourceId);
				resourceAccessPoint.setRemoteIdAtUrl(dataResourceName);
				resourceAccessPoint.setUrl(resourceAccessPointToCopy.getUrl());
				resourceAccessPoint.setUuid(resourceAccessPointToCopy.getUuid());
				resourceAccessPoint.setRemoteIdAtUrl(remoteId);
				long id = resourceAccessPointDAO.create(resourceAccessPoint);
				List<String> namespaces = (List<String>)context.get(getContextKeyPsNamespaces(), List.class, true);
				propertyStoreNamespaceDAO.createNamespaceMappings(id, namespaces , true);
				
				// override the resource access point which was incorrect (no remote id)
				context.put(getContextKeyResourceAccessPointId(), new Long(id));
			
			} else { // TAPIR style
				ResourceAccessPoint resourceAccessPoint = resourceAccessPointDAO.getById(resourceAccessPointId);
				resourceAccessPoint.setDataResourceId(dataResourceId);
				resourceAccessPoint.setRemoteIdAtUrl(remoteId);
				resourceAccessPointDAO.updateOrCreate(resourceAccessPoint);
			}
		}
		
		return context;
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
	 * @return Returns the contextKeyDataResourceName.
	 */
	public String getContextKeyDataResourceName() {
		return contextKeyDataResourceName;
	}

	/**
	 * @param contextKeyDataResourceName The contextKeyDataResourceName to set.
	 */
	public void setContextKeyDataResourceName(String contextKeyDataResourceName) {
		this.contextKeyDataResourceName = contextKeyDataResourceName;
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
	 * @return Returns the dataResourceDAO.
	 */
	public DataResourceDAO getDataResourceDAO() {
		return dataResourceDAO;
	}

	/**
	 * @param dataResourceDAO The dataResourceDAO to set.
	 */
	public void setDataResourceDAO(DataResourceDAO dataResourceDAO) {
		this.dataResourceDAO = dataResourceDAO;
	}

	/**
	 * @return Returns the propertyStoreNamespaceDAO.
	 */
	public PropertyStoreNamespaceDAO getPropertyStoreNamespaceDAO() {
		return propertyStoreNamespaceDAO;
	}

	/**
	 * @param propertyStoreNamespaceDAO The propertyStoreNamespaceDAO to set.
	 */
	public void setPropertyStoreNamespaceDAO(
			PropertyStoreNamespaceDAO propertyStoreNamespaceDAO) {
		this.propertyStoreNamespaceDAO = propertyStoreNamespaceDAO;
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
	 * @return the contextKeyBasisOfRecord
	 */
	public String getContextKeyBasisOfRecord() {
		return contextKeyBasisOfRecord;
	}

	/**
	 * @param contextKeyBasisOfRecord the contextKeyBasisOfRecord to set
	 */
	public void setContextKeyBasisOfRecord(String contextKeyBasisOfRecord) {
		this.contextKeyBasisOfRecord = contextKeyBasisOfRecord;
	}

	/**
	 * @return the contextKeyCitation
	 */
	public String getContextKeyCitation() {
		return contextKeyCitation;
	}

	/**
	 * @param contextKeyCitation the contextKeyCitation to set
	 */
	public void setContextKeyCitation(String contextKeyCitation) {
		this.contextKeyCitation = contextKeyCitation;
	}

	/**
	 * @return the contextKeyDescription
	 */
	public String getContextKeyDescription() {
		return contextKeyDescription;
	}

	/**
	 * @param contextKeyDescription the contextKeyDescription to set
	 */
	public void setContextKeyDescription(String contextKeyDescription) {
		this.contextKeyDescription = contextKeyDescription;
	}

	/**
	 * @return the contextKeyRights
	 */
	public String getContextKeyRights() {
		return contextKeyRights;
	}

	/**
	 * @param contextKeyRights the contextKeyRights to set
	 */
	public void setContextKeyRights(String contextKeyRights) {
		this.contextKeyRights = contextKeyRights;
	}

	/**
	 * @return the basisOfRecordMapping
	 */
	public CodeMapping getBasisOfRecordMapping() {
		return basisOfRecordMapping;
	}

	/**
	 * @param basisOfRecordMapping the basisOfRecordMapping to set
	 */
	public void setBasisOfRecordMapping(CodeMapping basisOfRecordMapping) {
		this.basisOfRecordMapping = basisOfRecordMapping;
	}

	/**
	 * @return the contextKeyLogoUrl
	 */
	public String getContextKeyLogoUrl() {
		return contextKeyLogoUrl;
	}

	/**
	 * @param contextKeyLogoUrl the contextKeyLogoUrl to set
	 */
	public void setContextKeyLogoUrl(String contextKeyLogoUrl) {
		this.contextKeyLogoUrl = contextKeyLogoUrl;
	}

	/**
	 * @return the contextKeyProviderRecordCount
	 */
	public String getContextKeyProviderRecordCount() {
		return contextKeyProviderRecordCount;
	}

	/**
	 * @param contextKeyProviderRecordCount the contextKeyProviderRecordCount to set
	 */
	public void setContextKeyProviderRecordCount(
			String contextKeyProviderRecordCount) {
		this.contextKeyProviderRecordCount = contextKeyProviderRecordCount;
	}

	/**
	 * @return the contextKeyWebsiteUrl
	 */
	public String getContextKeyWebsiteUrl() {
		return contextKeyWebsiteUrl;
	}

	/**
	 * @param contextKeyWebsiteUrl the contextKeyWebsiteUrl to set
	 */
	public void setContextKeyWebsiteUrl(String contextKeyWebsiteUrl) {
		this.contextKeyWebsiteUrl = contextKeyWebsiteUrl;
	}

	/**
	 * @return Returns the updateExistingRAP.
	 */
	public boolean isUpdateExistingRAP() {
		return updateExistingRAP;
	}

	/**
	 * @param updateExistingRAP The updateExistingRAP to set.
	 */
	public void setUpdateExistingRAP(boolean updateExistingRAP) {
		this.updateExistingRAP = updateExistingRAP;
	}
}
