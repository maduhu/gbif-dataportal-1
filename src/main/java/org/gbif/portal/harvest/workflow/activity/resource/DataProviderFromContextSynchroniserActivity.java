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

import org.gbif.portal.dao.DataProviderDAO;
import org.gbif.portal.model.DataProvider;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * Creates the data provider named in the context if it does not exist.
 * Sets the data provider ID in the context for the newly created provider or
 * sets the ID of the exising named provider. 
 * 
 * @author trobertson
 */
public class DataProviderFromContextSynchroniserActivity extends BaseActivity {
	/**
	 * DAO
	 */
	protected DataProviderDAO dataProviderDAO;
	
	/**
	 * Context key for the data provider created or loaded
	 */
	protected String contextKeyDataProviderId;
	
	/**
	 * Context key for the provider name
	 */
	protected String contextKeyProviderName;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		String providerName = (String) context.get(getContextKeyProviderName(), String.class, true);
		DataProvider provider = getDataProviderDAO().getByName(providerName);
		if (provider != null) {
			context.put(getContextKeyDataProviderId(), provider.getId());
		} else {
			DataProvider dataProvider = new DataProvider();
			dataProvider.setName(providerName);
			long id = getDataProviderDAO().create(dataProvider);
			context.put(getContextKeyDataProviderId(), id);
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
	 * @return the contextKeyProviderName
	 */
	public String getContextKeyProviderName() {
		return contextKeyProviderName;
	}

	/**
	 * @param contextKeyProviderName the contextKeyProviderName to set
	 */
	public void setContextKeyProviderName(String contextKeyProviderName) {
		this.contextKeyProviderName = contextKeyProviderName;
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

}
