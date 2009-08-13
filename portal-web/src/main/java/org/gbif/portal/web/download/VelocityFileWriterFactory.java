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
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * A File Writer factory for VelocityFileWriter threads.
 * 
 * @author dmartin
 */
public class VelocityFileWriterFactory extends BaseFileWriterFactory {

	/** The path of the template to use */
	protected String headerTemplatePath;
	/** The path of the template to use */
	protected String footerTemplatePath;	
	/** the path of the template to use */
	protected String templatePath;
	/** The download fields to use */
	protected List<Field> downloadFields;
	/** The default max download if not specified */
	protected int defaultMaxDownload = 10000;
	/** The request key parameter for the max download override */
	protected String maxDownloadOverrideRequestKey = "mdlo";
	/** The absolute max download limit */
	protected int absoluteMaxDownloadLimit = 500000;
	
	/**
	 * @see org.gbif.portal.web.download.FileWriterFactory#createFileWriter(java.io.File, java.util.List, java.util.Map, org.gbif.portal.web.download.OutputProcess, java.io.OutputStream, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public Runnable createFileWriter(HttpServletRequest request, 
			HttpServletResponse response, 
			List<Field> downloadFields,
			Map<String, OutputProperty> downloadFieldMappings,
			OutputProcess outputProcess, 
			List<SecondaryOutput> secondaryOutputs,
			File file,			
			OutputStream outputStream,
			String description
			) {
		VelocityFileWriter vfw = new VelocityFileWriter();
		initialiseVelocityFileWriter(request, downloadFields, downloadFieldMappings, outputProcess, secondaryOutputs, file, outputStream, description, vfw);
		return vfw;
	}

	protected void initialiseVelocityFileWriter(HttpServletRequest request, List<Field> downloadFields, Map<String, OutputProperty> downloadFieldMappings, OutputProcess outputProcess, List<SecondaryOutput> secondaryOutputs, File file, OutputStream outputStream, String description, VelocityFileWriter vfw) {
		initaliseWriter(vfw);
		vfw.setTemplatePath(templatePath);
		vfw.setHeaderTemplatePath(headerTemplatePath);
		vfw.setFooterTemplatePath(footerTemplatePath);
		vfw.setDownloadFieldMappings(downloadFieldMappings);
		
		if(downloadFields!=null && !downloadFields.isEmpty()){
			vfw.setDownloadFields(downloadFields);
		} else {
			vfw.setDownloadFields(this.downloadFields);
		}
		vfw.setOutputProcess(outputProcess);

		//set the process limit
		int recordCount = ServletRequestUtils.getIntParameter(request, maxDownloadOverrideRequestKey, defaultMaxDownload);
		outputProcess.setProcessLimit(recordCount);
		
		vfw.setTemporaryFile(file);
		vfw.setOutputStream(outputStream);
		vfw.setHostUrl(DownloadUtils.getHostUrl(request));
		vfw.setLocale(RequestContextUtils.getLocale(request));
		vfw.setDescription(description);
		vfw.setSecondaryDownloadOutputs(secondaryOutputs);
	}

	/**
	 * @param templatePath the templatePath to set
	 */
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	/**
	 * @param downloadFields the downloadFields to set
	 */
	public void setDownloadFields(List<Field> downloadFields) {
		this.downloadFields = downloadFields;
	}

	/**
	 * @param footerTemplatePath the footerTemplatePath to set
	 */
	public void setFooterTemplatePath(String footerTemplatePath) {
		this.footerTemplatePath = footerTemplatePath;
	}

	/**
	 * @param headerTemplatePath the headerTemplatePath to set
	 */
	public void setHeaderTemplatePath(String headerTemplatePath) {
		this.headerTemplatePath = headerTemplatePath;
	}

	/**
	 * @param absoluteMaxDownloadLimit the absoluteMaxDownloadLimit to set
	 */
	public void setAbsoluteMaxDownloadLimit(int absoluteMaxDownloadLimit) {
		this.absoluteMaxDownloadLimit = absoluteMaxDownloadLimit;
	}

	/**
	 * @param defaultMaxDownload the defaultMaxDownload to set
	 */
	public void setDefaultMaxDownload(int defaultMaxDownload) {
		this.defaultMaxDownload = defaultMaxDownload;
	}

	/**
	 * @param maxDownloadOverrideRequestKey the maxDownloadOverrideRequestKey to set
	 */
	public void setMaxDownloadOverrideRequestKey(
			String maxDownloadOverrideRequestKey) {
		this.maxDownloadOverrideRequestKey = maxDownloadOverrideRequestKey;
	}
}