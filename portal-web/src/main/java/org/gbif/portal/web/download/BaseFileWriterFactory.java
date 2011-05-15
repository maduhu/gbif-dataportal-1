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
package org.gbif.portal.web.download;

import org.springframework.context.MessageSource;

/**
 * A Base File Writer factory for convienence.
 * 
 * @author dmartin
 */
public abstract class BaseFileWriterFactory implements FileWriterFactory {

	/** Download utilities */
	protected DownloadUtils downloadUtils;
	/** The log event id to use */
	protected Integer logEventId;	
	/** The i18n message source */
	protected MessageSource messageSource;
	/** Whether or not to add a citation */
	protected boolean addCitation = false;
	/** Whether or not to add a rights file */
	protected boolean addRights = false;
	/** Whether or not to add a citation */
	protected boolean zipped = true;
	/** The default file extension for the primary file that is created */
	protected String fileExtension = ".txt";

	/**
	 * Initialise the file writer.
	 * @param fw
	 */
	protected void initaliseWriter(FileWriter fw){
		fw.setMessageSource(messageSource);
		fw.setAddCitation(addCitation);
		fw.setZipped(zipped);		
		fw.setDownloadUtils(downloadUtils);
		fw.setLogEventId(logEventId);
	}
	
	/**
	 * @return the addCitation
	 */
	public boolean isAddCitation() {
		return addCitation;
	}
	/**
	 * @param addCitation the addCitation to set
	 */
	public void setAddCitation(boolean addCitation) {
		this.addCitation = addCitation;
	}
	
	/**
	 *  the addRights
	 * @return
	 */
	public boolean isAddRights() {
		return addRights;
	}

	/**
	 * @param addRights the rights to set
	 */
	public void setAddRights(boolean addRights) {
		this.addRights = addRights;
	}

	/**
	 * @return the downloadUtils
	 */
	public DownloadUtils getDownloadUtils() {
		return downloadUtils;
	}
	/**
	 * @param downloadUtils the downloadUtils to set
	 */
	public void setDownloadUtils(DownloadUtils downloadUtils) {
		this.downloadUtils = downloadUtils;
	}
	/**
	 * @return the logEventId
	 */
	public Integer getLogEventId() {
		return logEventId;
	}
	/**
	 * @param logEventId the logEventId to set
	 */
	public void setLogEventId(Integer logEventId) {
		this.logEventId = logEventId;
	}
	/**
	 * @return the messageSource
	 */
	public MessageSource getMessageSource() {
		return messageSource;
	}
	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	/**
	 * @return the zipped
	 */
	public boolean isZipped() {
		return zipped;
	}
	/**
	 * @param zipped the zipped to set
	 */
	public void setZipped(boolean zipped) {
		this.zipped = zipped;
	}

	/**
	 * @return the fileExtension
	 */
	public String getFileExtension() {
		return fileExtension;
	}
	/**
	 * @param fileExtension the fileExtension to set
	 */
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
}