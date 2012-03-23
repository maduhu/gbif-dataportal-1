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
package org.gbif.portal.harvest.workflow.activity.col;

import org.gbif.portal.dao.DataResourceDAO;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * This syncs the resource with the named parameters and then adds it the mapped ids in the context 
 * 
 * @author trobertson
 */
public class CreateAndAddResourceToMapActivity extends BaseActivity {
	/**
	 * Names are self explainatory
	 */
	protected String fileUrl;
	/* defaults to "0" */
	protected String keyForCreatedResource = "0";
	protected DataResourceDAO dataResourceDAO;
	
	/**
     * @see org.gbif.portal.util.workflow.BaseMapContextActivity#doExecute(org.gbif.portal.util.workflow.MapContext)
     */
    @SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
    	return context;
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
	 * @return Returns the fileUrl.
	 */
	public String getFileUrl() {
		return fileUrl;
	}

	/**
	 * @param fileUrl The fileUrl to set.
	 */
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	/**
	 * @return Returns the keyForCreatedResource.
	 */
	public String getKeyForCreatedResource() {
		return keyForCreatedResource;
	}

	/**
	 * @param keyForCreatedResource The keyForCreatedResource to set.
	 */
	public void setKeyForCreatedResource(String keyForCreatedResource) {
		this.keyForCreatedResource = keyForCreatedResource;
	}
}