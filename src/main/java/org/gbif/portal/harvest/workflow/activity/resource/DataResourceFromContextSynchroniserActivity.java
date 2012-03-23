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
package org.gbif.portal.harvest.workflow.activity.resource;

import org.gbif.portal.dao.DataResourceDAO;
import org.gbif.portal.model.DataResource;
import org.gbif.portal.util.mapping.CodeMapping;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * Creates the data resource named in the context if it does not exist.
 * Sets the data resource ID in the context for the newly created resource or
 * sets the ID of the exising named resource. 
 * 
 * @author trobertson
 */
public class DataResourceFromContextSynchroniserActivity extends BaseActivity {
	/**
	 * Unknown basis of record
	 */
	public static int UNKNOWN_BASIS_OF_RECORD = 0;
	
	/**
	 * DAO
	 */
	protected DataResourceDAO dataResourceDAO;
	
	/**
	 * Context key for the data provider id
	 */
	protected String contextKeyDataProviderId;
	
	/**
	 * Context key for the data resource id
	 */
	protected String contextKeyDataResourceId;
	
	/**
	 * Context key for the resource name
	 */
	protected String contextKeyResourceName;
	
	/**
	 * Context key for the resource name
	 */
	protected String contextKeyDisplayName;
	
	/**
	 * Context key for the description
	 */
	protected String contextKeyDescription;
	
	/**
	 * Context key for the basis of record
	 */
	protected String contextKeyBasisOfRecord;
	
	/**
	 * Context key for the citation
	 */
	protected String contextKeyCitation;
	
	/**
	 * Context key for the rights
	 */
	protected String contextKeyRights;
	
	/**
	 * Context key for the logo URL
	 */
	protected String contextKeyLogoUrl;
	
	/**
	 * Context key for the website URL
	 */
	protected String contextKeyWebsiteUrl;
	
	/**
	 * Context key for the taxonomic priority
	 */
	protected String contextKeyTaxonomicPriorityText;
	
	/**
	 * Context key for the taxonomic priority
	 */
	protected String contextKeyTaxonomicPriority;
	
	/**
	 * Maps the basis of record strings to standard codes
	 */
	protected CodeMapping basisOfRecordMapping = null;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		long dataProviderId = (Long) context.get(getContextKeyDataProviderId(), Long.class, true);
		String resourceName = (String) context.get(getContextKeyResourceName(), String.class, true);
		String displayName = (String) context.get(getContextKeyDisplayName(), String.class, false);
		if (displayName == null) {
			displayName = resourceName;
		}
		DataResource resource = getDataResourceDAO().getByNameForProvider(resourceName, dataProviderId);
		String description = (String) context.get(getContextKeyDescription(), String.class, true);
		String basisOfRecord = (String) context.get(getContextKeyBasisOfRecord(), String.class, false);
		String citation = (String) context.get(getContextKeyCitation(), String.class, false);
		String rights = (String) context.get(getContextKeyRights(), String.class, false);
		String logoUrl = (String) context.get(getContextKeyLogoUrl(), String.class, false);
		String websiteUrl = (String) context.get(getContextKeyWebsiteUrl(), String.class, false);
		String taxonomicPriorityText = (String) context.get(getContextKeyTaxonomicPriorityText(), String.class, true);
		
		Integer taxonomicPriority = null;
		if (taxonomicPriorityText != null) {
			try {
				taxonomicPriority = new Integer(taxonomicPriorityText);
			} catch (Exception e) {
				// ignore
			}
		}

		int basisOfRecordCode = UNKNOWN_BASIS_OF_RECORD;
		
		if (basisOfRecord != null) {
			basisOfRecordCode = basisOfRecordMapping.mapToCode(basisOfRecord).intValue();
		}

		if (resource != null) {
			context.put(getContextKeyDataResourceId(), resource.getId());
			
			if (getContextKeyTaxonomicPriority() != null) {
				context.put(getContextKeyTaxonomicPriority(), resource.getTaxonomicPriority());
			}

			resource.setName(resourceName);
			resource.setDisplayName(displayName);
			resource.setBasisOfRecord(basisOfRecordCode);
			resource.setDescription(description);
			resource.setCitation(citation);
			resource.setRights(rights);
			resource.setWebsiteUrl(websiteUrl);
			resource.setLogoUrl(logoUrl);
			if (taxonomicPriority != null) {
				resource.setTaxonomicPriority(taxonomicPriority);
			}
				
			getDataResourceDAO().updateOrCreate(resource);
		} else {
			DataResource dataResource = new DataResource();
			dataResource.setName(resourceName);
			dataResource.setDisplayName(displayName);
			dataResource.setDataProviderId(dataProviderId);
			dataResource.setBasisOfRecord(basisOfRecordCode);
			dataResource.setDescription(description);
			dataResource.setCitation(citation);
			dataResource.setRights(rights);
			dataResource.setWebsiteUrl(websiteUrl);
			dataResource.setLogoUrl(logoUrl);
			if (taxonomicPriority != null) {
				dataResource.setTaxonomicPriority(taxonomicPriority);
			}
				
			long id = getDataResourceDAO().create(dataResource);
			context.put(getContextKeyDataResourceId(), id);
			
			if (getContextKeyTaxonomicPriority() != null) {
				context.put(getContextKeyTaxonomicPriority(), taxonomicPriority);
			}
		}
		return context;
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
	 * @return the contextKeyResourceName
	 */
	public String getContextKeyResourceName() {
		return contextKeyResourceName;
	}

	/**
	 * @param contextKeyResourceName the contextKeyResourceNime to set
	 */
	public void setContextKeyResourceName(String contextKeyResourceName) {
		this.contextKeyResourceName = contextKeyResourceName;
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
	 * @return the contextKeyDisplayName
	 */
	public String getContextKeyDisplayName() {
		return contextKeyDisplayName;
	}

	/**
	 * @param contextKeyDisplayName the contextKeyDisplayName to set
	 */
	public void setContextKeyDisplayName(String contextKeyDisplayName) {
		this.contextKeyDisplayName = contextKeyDisplayName;
	}

	/**
	 * @return the contextKeyTaxonomicPriority
	 */
	public String getContextKeyTaxonomicPriority() {
		return contextKeyTaxonomicPriority;
	}

	/**
	 * @param contextKeyTaxonomicPriority the contextKeyTaxonomicPriority to set
	 */
	public void setContextKeyTaxonomicPriority(String contextKeyTaxonomicPriority) {
		this.contextKeyTaxonomicPriority = contextKeyTaxonomicPriority;
	}

	/**
	 * @return the contextKeyTaxonomicPriorityText
	 */
	public String getContextKeyTaxonomicPriorityText() {
		return contextKeyTaxonomicPriorityText;
	}

	/**
	 * @param contextKeyTaxonomicPriorityText the contextKeyTaxonomicPriorityText to set
	 */
	public void setContextKeyTaxonomicPriorityText(
			String contextKeyTaxonomicPriorityText) {
		this.contextKeyTaxonomicPriorityText = contextKeyTaxonomicPriorityText;
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
