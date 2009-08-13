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
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.io.OutputProperty;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * A FileWriterFactory for excel writers.
 * 
 * @author dmartin
 */
public class ExcelFileWriterFactory extends BaseFileWriterFactory {

	/** The sheet name for the results */
	protected String sheetName;
	/** max sheet size that excel can process -1 for the column headings */
	protected int maxNoToProcess = 65535;
	/** The secondary outputs to run */
	protected List<SecondaryOutput> secondaryDownloadOutputs;	
	
	/**
	 * @see org.gbif.portal.web.download.FileWriterFactory#createFileWriter(java.io.File, java.util.List, java.util.Map, org.gbif.portal.web.download.OutputProcess, java.io.OutputStream, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public Runnable createFileWriter(HttpServletRequest request,
			HttpServletResponse response, List<Field> downloadFields,
			Map<String, OutputProperty> downloadFieldMappings,
			OutputProcess outputProcess, List<SecondaryOutput> secondaryOutputs, File file, 
			OutputStream outputStream, String description) {
		
		ExcelFileWriter efw = new ExcelFileWriter();
		initaliseWriter(efw);
		efw.setOutputStream(outputStream);
		efw.setDownloadFieldMappings(downloadFieldMappings);
		efw.setDownloadFields(downloadFields);
		efw.setHostUrl(DownloadUtils.getHostUrl(request));
		efw.setOutputProcess(outputProcess);
		efw.setTemporaryFile(file);
		outputProcess.setProcessLimit(maxNoToProcess);
		efw.setSheetName(sheetName);
		efw.setLocale(RequestContextUtils.getLocale(request));
		return efw;
	}
	
	/**
	 * @see org.gbif.portal.web.download.FileWriterFactory#getFileExtension()
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

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @param sheetName the sheetName to set
	 */
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	/**
	 * @param addCitation the addCitation to set
	 */
	public void setAddCitation(boolean addCitation) {
		this.addCitation = addCitation;
	}

	/**
	 * @param maxNoToProcess the maxNoToProcess to set
	 */
	public void setMaxNoToProcess(int maxNoToProcess) {
		this.maxNoToProcess = maxNoToProcess;
	}
}