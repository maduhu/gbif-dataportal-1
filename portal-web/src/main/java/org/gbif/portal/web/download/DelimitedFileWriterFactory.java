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
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Spring wired factory for creating DelimitedFileWriter objects.
 * 
 * @author dmartin
 */
public class DelimitedFileWriterFactory extends BaseFileWriterFactory {

	/** The end of field marker */
	protected String delimiter;
	/** The end of record marker */
	protected String endOfRecord;
	
	/**
	 * @see org.gbif.portal.web.download.FileWriterFactory#createFileWriter(java.io.File, java.util.List, java.util.Map, org.gbif.portal.web.download.OutputProcess, java.io.OutputStream, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletRequest)
	 */
	public Runnable createFileWriter(HttpServletRequest request, 
			HttpServletResponse response, 
				List<Field> downloadFields, 
				Map<String, OutputProperty> downloadFieldMappings, 
				OutputProcess outputProcess, 
				List<SecondaryOutput> secondaryOutputs,
				File file,				
				OutputStream outputStream, 
				String description) {
		
		//create the thread
		DelimitedFileWriter dfw = new DelimitedFileWriter();
		initaliseWriter(dfw);
		//set all the properties
		dfw.setTemporaryFile(file);
		dfw.setOutputStream(outputStream);
		dfw.setHostUrl(DownloadUtils.getHostUrl(request));
		dfw.setLocale(RequestContextUtils.getLocale(request));
		dfw.setDownloadFields(downloadFields);
		dfw.setDownloadFieldMappings(downloadFieldMappings);
		dfw.setOutputProcess(outputProcess);
		dfw.setDelimiter(delimiter);
		dfw.setEndOfRecord(endOfRecord);
		dfw.setSecondaryDownloadOutputs(secondaryOutputs);
		return dfw;
	}

	/**
	 * @param delimiter the delimiter to set
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	/**
	 * @param endOfRecord the endOfRecord to set
	 */
	public void setEndOfRecord(String endOfRecord) {
		this.endOfRecord = endOfRecord;
	}
}