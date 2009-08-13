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
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.web.util.DateUtil;

/**
 * Simple timer task that checks the last modified date
 * on a file and deletes if the file is older than a set 
 * period.
 * 
 * @author dmartin
 */
public class ExpiredFileTask extends TimerTask {

	protected static Log logger = LogFactory.getLog(ExpiredFileTask.class);	
	
	protected List<FileType> fileTypes;
	
	protected String tmpDirSystemProperty = "java.io.tmpdir";	
	
	protected String directoryPath = null;
	
	/**
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		
		try {
			//get the directory to search
			File directory = new File(getFilePath());
			File[] files = directory.listFiles();
			
			Date now = new Date(System.currentTimeMillis());
			
			if(files!=null){
				for(File file: files){
					
					if(logger.isDebugEnabled())
						logger.debug("File: "+file.getName());
		
					FileType fileType = getFileType(file.getName());
					
					if(fileType!=null){
						long lastModified = file.lastModified();
						Date lastModifiedDate = new Date(lastModified);
						
						Date expirationTime = DateUtils.addSeconds(now, - fileType.getExpiryTimeInSecs());
						if(logger.isDebugEnabled())
							logger.debug("Current expiry date for file type: "+DateUtil.getDateTimeString(expirationTime));
						
						if(logger.isDebugEnabled())
							logger.debug("Last modified date: "+DateUtil.getDateTimeString(lastModifiedDate));
						
						//if the last modified date is before the expiration date
						//delete it
						if(lastModifiedDate.before(expirationTime)){
							logger.debug("Delete file");
							file.delete();
						}
					}
				}
			}
		} catch (Exception e){
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Returns the temp dir path id no directory path is set.
	 * 
	 * @return a file path
	 */
	private String getFilePath(){
		if(directoryPath==null){
			return System.getProperty(tmpDirSystemProperty);
		}
		return directoryPath;
	}
	
	/**
	 * Retrieve a file type for this file name.
	 * 
	 * @param name
	 * @return
	 */
	private FileType getFileType(String name) {
		if(name==null)
			return null;
		for(FileType fileType: fileTypes){
			if(name.startsWith(fileType.getFileNamePrefix())){
				return fileType;
			}
		}
		return null;
	}

	/**
	 * @param tmpDirSystemProperty the tmpDirSystemProperty to set
	 */
	public void setTmpDirSystemProperty(String tmpDirSystemProperty) {
		this.tmpDirSystemProperty = tmpDirSystemProperty;
	}

	/**
	 * @return the fileTypes
	 */
	public List<FileType> getFileTypes() {
		return fileTypes;
	}

	/**
	 * @param fileTypes the fileTypes to set
	 */
	public void setFileTypes(List<FileType> fileTypes) {
		this.fileTypes = fileTypes;
	}

	/**
	 * @return the tmpDirSystemProperty
	 */
	public String getTmpDirSystemProperty() {
		return tmpDirSystemProperty;
	}
}