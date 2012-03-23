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
package org.gbif.portal.harvest.workflow.activity.provider;

import org.gbif.portal.dao.DataProviderDAO;
import org.gbif.portal.model.DataProvider;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * Creates the data provider named if it does not exist.
 * Sets the data provider ID in the context for the newly created provider or
 * sets the ID of the exising named provider. 
 * 
 * @author trobertson
 */
public class DataProviderFromValuesSynchroniserActivity extends BaseActivity {
	/**
	 * DAO
	 */
	protected DataProviderDAO dataProviderDAO;
	
	/**
	 * Context key for the data provider created or loaded
	 */
	protected String contextKeyDataProviderId;
	
	/**
	 * Provider details
	 */
	protected String name;
	protected String description;
	protected String address;
	protected String websiteUrl;
	protected String logoUrl;
	protected String email;
	protected String telephone;
	
	
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		DataProvider provider = getDataProviderDAO().getByName(name);
		if (provider == null) {
			provider= new DataProvider();
			logger.info("New provider will be created for: " + name);
		} else {
			logger.info("Updating provider id[" + provider.getId() + "] name[" + name + "]");
		}
		provider.setDescription(description);
		provider.setAddress(address);
		provider.setWebsiteUrl(websiteUrl);
		provider.setLogoUrl(logoUrl);
		provider.setEmail(email);
		provider.setTelephone(telephone);
		provider.setName(name);
		long id = getDataProviderDAO().updateOrCreate(provider);
		provider.setId(id);
		context.put(getContextKeyDataProviderId(), provider.getId());
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
	 * @return the dataProviderDAO
	 */
	public DataProviderDAO getDataProviderDAO() {
		return dataProviderDAO;
	}

	/**
	 * @param dataProviderDAO the dataProviderDAO to set
	 */
	public void setDataProviderDAO(DataProviderDAO dataProviderDAO) {
		this.dataProviderDAO = dataProviderDAO;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the address.
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address The address to set.
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return Returns the logoUrl.
	 */
	public String getLogoUrl() {
		return logoUrl;
	}

	/**
	 * @param logoUrl The logoUrl to set.
	 */
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	/**
	 * @return Returns the telephone.
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephone The telephone to set.
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * @return Returns the websiteUrl.
	 */
	public String getWebsiteUrl() {
		return websiteUrl;
	}

	/**
	 * @param websiteUrl The websiteUrl to set.
	 */
	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

}
