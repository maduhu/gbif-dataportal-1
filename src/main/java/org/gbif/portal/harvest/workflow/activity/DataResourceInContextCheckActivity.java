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

import java.util.HashMap;
import java.util.Map;

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
 * An activity that will check that there is a DataResource in the context and if not
 * it will be retrieved or created.
 * 
 * This activity comes from the the following requirements:
 * - With many Biocase providers, the DataSet names are not searchable
 * - Thus there is no way to create DataResources
 * - When a Biocase provider is harvested, it may be configured so that the 
 *   Datasets are all done simultaneously
 * - Thus, a Biocase provider may be indexed by launching only knowing the
 *   ResourceAccessPoint.
 *   
 * This activity therefore would be used in a situation like above, to ensure that
 * the resource exists, if not create it and ensure that the details are in the context
 * for future activities that rely on the DataResource (pretty much everything...)
 * 
 * When providers indeed map the dataset names, this should become redundant, or be an activity
 * that may be used with some modifications in the UDDI sync'ing.
 * 
 * @author trobertson
 */
public class DataResourceInContextCheckActivity extends BaseActivity implements
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
	protected String contextKeyBasisOfRecord;
	protected String contextKeyDescription;
	protected String contextKeyCitation;
	protected String contextKeyRights;
	protected String contextKeyLogoUrl;
	protected String contextKeyWebsiteUrl;
	protected String contextKeyResourceAccessPointId;
	protected String contextKeyCachedDataResourceMap;

	/**
	 * Maps the basis of record strings to standard codes
	 */
	protected CodeMapping basisOfRecordMapping = null;
	
	/**
	 * DAOs
	 */
	protected DataResourceDAO dataResourceDAO;
	protected ResourceAccessPointDAO resourceAccessPointDAO;
	protected PropertyStoreNamespaceDAO propertyStoreNamespaceDAO;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		String dataResourceName = (String) context.get(getContextKeyDataResourceName(), String.class, true);
		long dataProviderId = ((Long) context.get(getContextKeyDataProviderId(), Long.class, true)).longValue();
		long resourceAccessPointId = ((Long) context.get(getContextKeyResourceAccessPointId(), Long.class, true)).longValue();
		String basisOfRecord = (String) context.get(getContextKeyBasisOfRecord(), String.class, false);
		String description = (String) context.get(getContextKeyDescription(), String.class, false);
		String citation = (String) context.get(getContextKeyCitation(), String.class, false);
		String rights = (String) context.get(getContextKeyRights(), String.class, false);
		String logoUrl = (String) context.get(getContextKeyLogoUrl(), String.class, false);
		String websiteUrl = (String) context.get(getContextKeyWebsiteUrl(), String.class, false);
		
		int basisOfRecordCode = UNKNOWN_BASIS_OF_RECORD;
		
		if (basisOfRecord != null) {
			basisOfRecordCode = basisOfRecordMapping.mapToCode(basisOfRecord).intValue();
		}
		
		// get the cached resources from context or create and save them
		Map<String, Long> cachedResources = (Map<String, Long>) context.get(getContextKeyCachedDataResourceMap(), Map.class, false);
		if (cachedResources == null) {
			cachedResources = new HashMap<String, Long>();
			context.put(getContextKeyCachedDataResourceMap(), cachedResources);
		}
		
		// if the cache contains the resource name, just continue
		if (cachedResources.containsKey(dataResourceName)) {
			logger.info("Cache contains DataResource: " + dataResourceName);
			context.put(getContextKeyDataResourceId(), cachedResources.get(dataResourceName));
			
		} else {
			// See if it is in the database
			DataResource dataResource = dataResourceDAO.getByNameForProvider(dataResourceName, dataProviderId);
			if (dataResource != null) {
				logger.info("DB contains DataResource: " + dataResourceName);
				cachedResources.put(dataResourceName, new Long(dataResource.getId()));
				context.put(getContextKeyDataResourceId(), new Long(dataResource.getId()));

				dataResource.setName(dataResourceName);
				dataResource.setBasisOfRecord(basisOfRecordCode);
				dataResource.setDescription(description);
				dataResource.setCitation(citation);
				dataResource.setRights(rights);
				dataResource.setLogoUrl(logoUrl);
				dataResource.setWebsiteUrl(websiteUrl);
				dataResourceDAO.updateOrCreate(dataResource);
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
				dataResource.setLogoUrl(logoUrl);
				dataResource.setWebsiteUrl(websiteUrl);
				long dataResourceId = dataResourceDAO.create(dataResource);
				context.put(getContextKeyDataResourceId(), new Long(dataResourceId));
				
				ResourceAccessPoint resourceAccessPointToCopy = resourceAccessPointDAO.getById(resourceAccessPointId);
				ResourceAccessPoint resourceAccessPoint = new ResourceAccessPoint();
				resourceAccessPoint.setDataProviderId(dataProviderId);
				resourceAccessPoint.setDataResourceId(dataResourceId);
				resourceAccessPoint.setRemoteIdAtUrl(dataResourceName);
				resourceAccessPoint.setUrl(resourceAccessPointToCopy.getUrl());
				resourceAccessPoint.setUuid(resourceAccessPointToCopy.getUuid());
				long id = resourceAccessPointDAO.create(resourceAccessPoint);
				propertyStoreNamespaceDAO.createNamespaceMappings(id, resourceAccessPointToCopy.getNamespaces());
				
				cachedResources.put(dataResourceName, new Long(dataResourceId));
				
				// NOTE: we do not put the new resource_access_point id in even though we have created one.
				// this is because it is NOT the resource access point that we are using - but we THINK there should
				// be one for future indexing, IF the "dataset" can be used in the future....
			}
		}
		
		return context;
	}

	/**
	 * @return the contextKeyDataResourceName
	 */
	public String getContextKeyDataResourceName() {
		return contextKeyDataResourceName;
	}

	/**
	 * @param contextKeyDataResourceName the contextKeyDataResourceName to set
	 */
	public void setContextKeyDataResourceName(String contextKeyDataResourceName) {
		this.contextKeyDataResourceName = contextKeyDataResourceName;
	}

	/**
	 * @return the dataResourceDAO
	 */
	public DataResourceDAO getDataResourceDAO() {
		return dataResourceDAO;
	}

	/**
	 * @param dataResourceDAO the dataResourceDAO to set
	 */
	public void setDataResourceDAO(DataResourceDAO dataResourceDAO) {
		this.dataResourceDAO = dataResourceDAO;
	}

	/**
	 * @return the contextKeyDataProviderId
	 */
	public String getContextKeyDataProviderId() {
		return contextKeyDataProviderId;
	}

	/**
	 * @param contextKeyDataProviderId the contextKeyDataProviderId to set
	 */
	public void setContextKeyDataProviderId(String contextKeyDataProviderId) {
		this.contextKeyDataProviderId = contextKeyDataProviderId;
	}


	/**
	 * @return the contextKeyCachedDataResourceMap
	 */
	public String getContextKeyCachedDataResourceMap() {
		return contextKeyCachedDataResourceMap;
	}

	/**
	 * @param contextKeyCachedDataResourceMap the contextKeyCachedDataResourceMap to set
	 */
	public void setContextKeyCachedDataResourceMap(
			String contextKeyCachedDataResourceMap) {
		this.contextKeyCachedDataResourceMap = contextKeyCachedDataResourceMap;
	}

	/**
	 * @return the contextKeyDataResourceId
	 */
	public String getContextKeyDataResourceId() {
		return contextKeyDataResourceId;
	}

	/**
	 * @param contextKeyDataResourceId the contextKeyDataResourceId to set
	 */
	public void setContextKeyDataResourceId(String contextKeyDataResourceId) {
		this.contextKeyDataResourceId = contextKeyDataResourceId;
	}

	/**
	 * @return the resourceAccessPointDAO
	 */
	public ResourceAccessPointDAO getResourceAccessPointDAO() {
		return resourceAccessPointDAO;
	}

	/**
	 * @param resourceAccessPointDAO the resourceAccessPointDAO to set
	 */
	public void setResourceAccessPointDAO(
			ResourceAccessPointDAO resourceAccessPointDAO) {
		this.resourceAccessPointDAO = resourceAccessPointDAO;
	}

	/**
	 * @return the contextKeyResourceAccessPointId
	 */
	public String getContextKeyResourceAccessPointId() {
		return contextKeyResourceAccessPointId;
	}

	/**
	 * @param contextKeyResourceAccessPointId the contextKeyResourceAccessPointId to set
	 */
	public void setContextKeyResourceAccessPointId(
			String contextKeyResourceAccessPointId) {
		this.contextKeyResourceAccessPointId = contextKeyResourceAccessPointId;
	}

	/**
	 * @return the propertyStoreNamespaceDAO
	 */
	public PropertyStoreNamespaceDAO getPropertyStoreNamespaceDAO() {
		return propertyStoreNamespaceDAO;
	}

	/**
	 * @param propertyStoreNamespaceDAO the propertyStoreNamespaceDAO to set
	 */
	public void setPropertyStoreNamespaceDAO(
			PropertyStoreNamespaceDAO propertyStoreNamespaceDAO) {
		this.propertyStoreNamespaceDAO = propertyStoreNamespaceDAO;
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
}
