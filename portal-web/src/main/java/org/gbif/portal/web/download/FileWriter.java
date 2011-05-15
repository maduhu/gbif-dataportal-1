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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;

/**
 * Abstract file writer that file writer implementations should subclass for 
 * convienence.
 * 
 * @author dmartin
 */
public abstract class FileWriter implements Runnable {

	protected static Log logger = LogFactory.getLog(FileWriter.class);	
	
	/** The temporary file reference - required to rename the file after completion */
	protected File temporaryFile;
	/** The output stream to write to */
	protected OutputStream outputStream;	
	/** The fields to download */
	protected List<Field> downloadFields;
	/** The process to run */
	protected OutputProcess outputProcess;
	/** The log event id to use */
	protected DownloadUtils downloadUtils;	
	/** Message source */
	protected MessageSource messageSource;
	/** The log event id to use */
	protected Integer logEventId;	
	/** Whether to add a citation */
	protected boolean addCitation = true;
	/** Whether to add a citation */
	protected boolean addRights = true;	
	/** Whether the file to produce is zipped */
	protected boolean zipped = true;
	/** The file name for the citation */
	protected String citationFileName = "citation.txt";
	/** The file name for the citation */
	protected String rightsFileName = "rights.txt";		
	/** Audit the download */
	protected boolean audit = true;	

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			writeFile();
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			signalFileWriteFailure();
		}
	}	

	/**
	 * Once returned rename the file to indicate the file has been written.
	 * 
	 * @param temporaryFile
	 */
	public void signalFileWriteFailure(){
		try {
			//close the stream
			outputStream.close();
			String fileName = temporaryFile.getName();
			temporaryFile.renameTo(new File(temporaryFile.getParent()+File.separator+fileName.substring(0, fileName.length()-DownloadUtils.temporaryExtensionIndicator.length())+DownloadUtils.failedExtensionIndicator));
		} catch(Exception e){
			logger.error("There was a problem indicating file writing failure");
			logger.error(e.getMessage(),e);
		}
	}	

	/**
	 * Once returned rename the file to indicate the file has been written.
	 * 
	 * @param temporaryFile
	 */
	public void signalFileWriteComplete() throws Exception{
		//close the stream
		outputStream.close();
		String fileName = temporaryFile.getName();
		temporaryFile.renameTo(new File(temporaryFile.getParent()+File.separator+fileName.substring(0, fileName.length()-DownloadUtils.temporaryExtensionIndicator.length() )));
	}	
	
	/**
	 * Write out the file in the required format.
	 * 
	 * @throws IOException 
	 */	
	public abstract void writeFile() throws Exception;

	/**
	 * @return the outputStream
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}

	/**
	 * @param outputStream the outputStream to set
	 */
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	/**
	 * @return the temporaryFile
	 */
	public File getTemporaryFile() {
		return temporaryFile;
	}

	/**
	 * @param temporaryFile the temporaryFile to set
	 */
	public void setTemporaryFile(File temporaryFile) {
		this.temporaryFile = temporaryFile;
	}

	/**
	 * @param downloadFields the downloadFields to set
	 */
	public void setDownloadFields(List<Field> downloadFields) {
		this.downloadFields = downloadFields;
	}

	/**
	 * @return the outputProcess
	 */
	public OutputProcess getOutputProcess() {
		return outputProcess;
	}

	/**
	 * @param outputProcess the outputProcess to set
	 */
	public void setOutputProcess(OutputProcess outputProcess) {
		this.outputProcess = outputProcess;
	}

	/**
	 * @return the downloadFields
	 */
	public List<Field> getDownloadFields() {
		return downloadFields;
	}

	/**
	 * @param downloadUtils the downloadUtils to set
	 */
	public void setDownloadUtils(DownloadUtils downloadUtils) {
		this.downloadUtils = downloadUtils;
	}
	
	/**
	 * @param logEventId the logEventId to set
	 */
	public void setLogEventId(Integer logEventId) {
		this.logEventId = logEventId;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @param addCitation the addCitation to set
	 */
	public void setAddCitation(boolean addCitation) {
		this.addCitation = addCitation;
	}
	
	/**
	 * 
	 * @param addRights the addRights to set
	 */
	public void setAddRights(boolean addRights) {
		this.addRights = addRights;
	}

	/**
	 * @param zipped the zipped to set
	 */
	public void setZipped(boolean zipped) {
		this.zipped = zipped;
	}

	/**
	 * @param citationFileName the citationFileName to set
	 */
	public void setCitationFileName(String citationFileName) {
		this.citationFileName = citationFileName;
	}
	
	/**
	 * @param rightsFileName
	 */
	public void setRightsFileName(String rightsFileName) {
		this.rightsFileName = rightsFileName;
	}	
	
	/**
	 * @param audit the audit to set
	 */
	public void setAudit(boolean audit) {
		this.audit = audit;
	}	
}