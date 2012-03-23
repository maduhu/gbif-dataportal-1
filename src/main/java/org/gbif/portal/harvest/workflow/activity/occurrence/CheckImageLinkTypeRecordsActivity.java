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
package org.gbif.portal.harvest.workflow.activity.occurrence;

import org.gbif.portal.dao.ImageRecordDAO;
import org.gbif.portal.dao.LinkRecordDAO;
import org.gbif.portal.dao.TypificationRecordDAO;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * An activity that will get find out if we need to do image link and type processing on a record 
 * by record basis as many have none.
 * @author trobertson
 */
public class CheckImageLinkTypeRecordsActivity extends BaseActivity implements
		Activity {
	/**
	 * The daos 
	 */
	protected ImageRecordDAO imageRecordDAO;
	protected LinkRecordDAO linkRecordDAO;
	protected TypificationRecordDAO typificationRecordDAO;
	
	/**
	 * Context Keys
	 */
	protected String contextKeySkipLinkRecord = "skipLinkRecord";
	protected String contextKeySkipTypificationRecord = "skipTypificationRecord";
	protected String contextKeySkipImageRecord = "skipImageRecord";
	protected String contextKeyResourceAccessPointId = "resourceAccessPointId";
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		
		context.put(contextKeySkipImageRecord, !imageRecordDAO.hasImageRecords(
				(Long)context.get(contextKeyResourceAccessPointId, Long.class, true)));
		
		context.put(contextKeySkipLinkRecord, !linkRecordDAO.hasLinkRecords(
				(Long)context.get(contextKeyResourceAccessPointId, Long.class, true)));

		context.put(contextKeySkipTypificationRecord, !typificationRecordDAO.hasTypificationRecords(
				(Long)context.get(contextKeyResourceAccessPointId, Long.class, true)));

		return context;
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
	 * @return Returns the contextKeySkipImageRecord.
	 */
	public String getContextKeySkipImageRecord() {
		return contextKeySkipImageRecord;
	}

	/**
	 * @param contextKeySkipImageRecord The contextKeySkipImageRecord to set.
	 */
	public void setContextKeySkipImageRecord(String contextKeySkipImageRecord) {
		this.contextKeySkipImageRecord = contextKeySkipImageRecord;
	}

	/**
	 * @return Returns the contextKeySkipLinkRecord.
	 */
	public String getContextKeySkipLinkRecord() {
		return contextKeySkipLinkRecord;
	}

	/**
	 * @param contextKeySkipLinkRecord The contextKeySkipLinkRecord to set.
	 */
	public void setContextKeySkipLinkRecord(String contextKeySkipLinkRecord) {
		this.contextKeySkipLinkRecord = contextKeySkipLinkRecord;
	}

	/**
	 * @return Returns the contextKeySkipTypificationRecord.
	 */
	public String getContextKeySkipTypificationRecord() {
		return contextKeySkipTypificationRecord;
	}

	/**
	 * @param contextKeySkipTypificationRecord The contextKeySkipTypificationRecord to set.
	 */
	public void setContextKeySkipTypificationRecord(
			String contextKeySkipTypificationRecord) {
		this.contextKeySkipTypificationRecord = contextKeySkipTypificationRecord;
	}

	/**
	 * @return Returns the imageRecordDAO.
	 */
	public ImageRecordDAO getImageRecordDAO() {
		return imageRecordDAO;
	}

	/**
	 * @param imageRecordDAO The imageRecordDAO to set.
	 */
	public void setImageRecordDAO(ImageRecordDAO imageRecordDAO) {
		this.imageRecordDAO = imageRecordDAO;
	}

	/**
	 * @return Returns the linkRecordDAO.
	 */
	public LinkRecordDAO getLinkRecordDAO() {
		return linkRecordDAO;
	}

	/**
	 * @param linkRecordDAO The linkRecordDAO to set.
	 */
	public void setLinkRecordDAO(LinkRecordDAO linkRecordDAO) {
		this.linkRecordDAO = linkRecordDAO;
	}

	/**
	 * @return Returns the typificationRecordDAO.
	 */
	public TypificationRecordDAO getTypificationRecordDAO() {
		return typificationRecordDAO;
	}

	/**
	 * @param typificationRecordDAO The typificationRecordDAO to set.
	 */
	public void setTypificationRecordDAO(TypificationRecordDAO typificationRecordDAO) {
		this.typificationRecordDAO = typificationRecordDAO;
	}
}
