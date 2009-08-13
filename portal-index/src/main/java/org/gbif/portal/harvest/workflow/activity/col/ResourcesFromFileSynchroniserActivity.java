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

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.gbif.portal.dao.DataResourceDAO;
import org.gbif.portal.model.DataResource;
import org.gbif.portal.util.file.DelimitedFileReader;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ContextCorruptException;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * This synces the resources from the file and puts the ids in a hashmap, mapping the COL databases id to 
 * a GBIF resource 
 * 
 * @author trobertson
 */
public class ResourcesFromFileSynchroniserActivity extends BaseActivity {
	/**
	 * Names are self explainatory
	 */
	protected String fileUrl;
	protected String contextKeyResourceIds;
	protected String contextKeyProviderId;
	protected DataResourceDAO dataResourceDAO;
	
	// The basis of record defaults to 102 (Taxonomy)
	protected int basisOfRecordType = 102;
	
	// the priority defaults to 1
	protected int taxonomicPriority = 1;
	
	
	// the file columns
	private static final int COLUMN_DATABASE_ID = 0;
	private static final int COLUMN_DATABASE_NAME = 1;
	private static final int COLUMN_WEBSITE = 2;
	private static final int COLUMN_ORGANIZATION = 3;
	private static final int COLUMN_CONTACT_PERSON = 4;
	private static final int COLUMN_DESCRITPION = 5;
	private static final int COLUMN_AUTHORS_EDITORS = 6;
	private static final int COLUMN_LOGO_URL = 7;
	
	
	/**
     * @see org.gbif.portal.util.workflow.BaseMapContextActivity#doExecute(org.gbif.portal.util.workflow.MapContext)
     */
    @SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
    	// Initialise the maps
    	Map<String, Long> databaseIds = initialiseResourceIdsMap(context);
    	
    	logger.info("Starting: " + fileUrl);
    	long time = System.currentTimeMillis();
		InputStream is = new FileInputStream(fileUrl);
		DelimitedFileReader reader = new DelimitedFileReader(is, "\t", "\"", true);
	
		if (reader!=null) {
			do {
				String dbId = reader.get(COLUMN_DATABASE_ID);
				String dbName = reader.get(COLUMN_DATABASE_NAME);
				String dbWebsite = reader.get(COLUMN_WEBSITE);
				String dbOrganisation = reader.get(COLUMN_ORGANIZATION);
				String dbContactPerson = reader.get(COLUMN_CONTACT_PERSON);
				String dbDescription = reader.get(COLUMN_DESCRITPION);
				String dbAuthorsEditors = reader.get(COLUMN_AUTHORS_EDITORS);
				String logoUrl = reader.get(COLUMN_LOGO_URL);
				
				DataResource dr = dataResourceDAO.getByNameForProvider(dbName, (Long)context.get(contextKeyProviderId, Long.class, true));
				if (dr==null) {
					dr = new DataResource();
					dr.setBasisOfRecord(basisOfRecordType);
					dr.setCitableAgent(dbAuthorsEditors);
					dr.setDataProviderId((Long)context.get(contextKeyProviderId, Long.class, true));
					dr.setDescription(dbDescription);
					dr.setDisplayName(dbName);
					dr.setName(dbName);
					dr.setTaxonomicPriority(taxonomicPriority);
					dr.setLogoUrl(logoUrl);
					long id = dataResourceDAO.create(dr);
					databaseIds.put(dbId, id);
					
				} else {
					logger.warn("TODO: DataResourceDAO does not have an Update right now...");
					databaseIds.put(dbId, dr.getId());
				}
				
			} while (reader.next());
		} else {
			logger.error("File reader not created for " + fileUrl);
		}
		
		logger.info("Finished [" + fileUrl + "] in " + ((1 + System.currentTimeMillis() - time)/1000) + " secs");
		return context;		
	}

	/**
	 * @param context
	 * @return
	 * @throws ContextCorruptException
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Long> initialiseResourceIdsMap(ProcessContext context) throws ContextCorruptException {
		Map<String,Long> resources = (Map<String,Long>) context.get(contextKeyResourceIds, Map.class, false);
    	if (resources == null) {
    		resources = new HashMap<String,Long>();
    		logger.info("Creating the resources map");
    		context.put(contextKeyResourceIds, resources);
    	}
		return resources;
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
	 * @return Returns the contextKeyResourceIds.
	 */
	public String getContextKeyResourceIds() {
		return contextKeyResourceIds;
	}

	/**
	 * @param contextKeyResourceIds The contextKeyResourceIds to set.
	 */
	public void setContextKeyResourceIds(String contextKeyResourceIds) {
		this.contextKeyResourceIds = contextKeyResourceIds;
	}

	/**
	 * @return Returns the contextKeyProviderId.
	 */
	public String getContextKeyProviderId() {
		return contextKeyProviderId;
	}

	/**
	 * @param contextKeyProviderId The contextKeyProviderId to set.
	 */
	public void setContextKeyProviderId(String contextKeyProviderId) {
		this.contextKeyProviderId = contextKeyProviderId;
	}

	/**
	 * @return Returns the basisOfRecordType.
	 */
	public int getBasisOfRecordType() {
		return basisOfRecordType;
	}

	/**
	 * @param basisOfRecordType The basisOfRecordType to set.
	 */
	public void setBasisOfRecordType(int basisOfRecordType) {
		this.basisOfRecordType = basisOfRecordType;
	}

	/**
	 * @return Returns the taxonomicPriority.
	 */
	public int getTaxonomicPriority() {
		return taxonomicPriority;
	}

	/**
	 * @param taxonomicPriority The taxonomicPriority to set.
	 */
	public void setTaxonomicPriority(int taxonomicPriority) {
		this.taxonomicPriority = taxonomicPriority;
	}
}