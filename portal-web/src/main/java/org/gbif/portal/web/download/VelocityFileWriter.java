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
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.gbif.portal.io.DataResourceAuditor;
import org.gbif.portal.io.OutputProperty;
import org.gbif.portal.io.ResultsOutputter;
import org.gbif.portal.io.VelocityResultsOutputter;
import org.gbif.portal.util.request.TemplateUtils;
import org.springframework.context.MessageSource;

/**
 * Writes a file out using velocity.
 * 
 * @author dmartin
 */
public class VelocityFileWriter extends FileWriter {

	protected static Log logger = LogFactory.getLog(VelocityFileWriter.class);	
	
	/** The path of the template to use */
	protected String headerTemplatePath;
	/** The path of the template to use */
	protected String footerTemplatePath;
	/** The path of the template to use */
	protected String templatePath;
	/** The locale to use */
	protected Locale locale;
	/** The mappings from field names to Output properties */
	protected Map<String, OutputProperty> downloadFieldMappings;
	/** The host url */
	protected String hostUrl;
	/** A description of what is being written */
	protected String description;
	/** The secondary outputs to run */
	protected List<SecondaryOutput> secondaryDownloadOutputs;
	
	/**
	 * @see org.gbif.portal.web.download.FileWriter#writeFile()
	 */
	public void writeFile() throws Exception {
		
		//write the header		
		writeTemplate(headerTemplatePath, outputStream);

		//Create FieldFormatter - the object that is i18n aware
		FieldFormatter ff = new FieldFormatter(downloadFields, messageSource, locale, hostUrl);
		
		//Create Results outputter - the object that knows about the required format
		ResultsOutputter resultsOutputter = new VelocityResultsOutputter(outputStream, templatePath, ff);
		
		//check for citation
		if((addCitation || audit) && zipped){
			DataResourceAuditor cro = new DataResourceAuditor();	
			cro.setNextResultsOutputter(resultsOutputter);
			resultsOutputter = cro;
		}
		
		//run the output process
		outputProcess.process(resultsOutputter);
		
		//write the footer
		writeTemplate(footerTemplatePath, outputStream);
		
		//close the file stream
		if(zipped){
			((ZipOutputStream) outputStream).closeEntry();
		}

		//write out the citation
		if(addCitation && zipped){
			downloadUtils.outputCitation(outputStream, (DataResourceAuditor) resultsOutputter, citationFileName, locale, hostUrl);
		}
		
    //write out the rights
    if(addRights && zipped){
      downloadUtils.outputRights(outputStream, (DataResourceAuditor) resultsOutputter, rightsFileName, locale, hostUrl);
    }   		

		//log usage
		if(logEventId!=null && resultsOutputter instanceof DataResourceAuditor){
			downloadUtils.logDownloadUsage((DataResourceAuditor) resultsOutputter, logEventId);
		}
		
		//run secondary outputs
		if(zipped && secondaryDownloadOutputs!=null){
			downloadUtils.addSecondaryOutputs((ZipOutputStream) outputStream, secondaryDownloadOutputs);
		}
		
		//once returned rename the file to indicate the file has been written
		signalFileWriteComplete();
	}

	/**
	 * Write out the supplied template.
	 * 
	 * @param templatePath
	 * @param outputStream
	 * @throws Exception
	 */
	protected void writeTemplate(String templatePath, OutputStream outputStream) throws Exception {
		if(StringUtils.isNotEmpty(templatePath)){
			VelocityContext velocityContext = new VelocityContext();
			velocityContext.put("hostUrl", hostUrl);
			Template template = Velocity.getTemplate(templatePath);
			template.initDocument();
			TemplateUtils tu = new TemplateUtils();
			OutputStreamWriter writer = new OutputStreamWriter(outputStream); 
			tu.merge(template, velocityContext, writer);
			writer.flush();
		}
	}

	/**
	 * @param addCitation the addCitation to set
	 */
	public void setAddCitation(boolean addCitation) {
		this.addCitation = addCitation;
	}

	/**
	 * @param downloadFieldMappings the downloadFieldMappings to set
	 */
	public void setDownloadFieldMappings(
			Map<String, OutputProperty> downloadFieldMappings) {
		this.downloadFieldMappings = downloadFieldMappings;
	}

	/**
	 * @param downloadFields the downloadFields to set
	 */
	public void setDownloadFields(List<Field> downloadFields) {
		this.downloadFields = downloadFields;
	}

	/**
	 * @param hostUrl the hostUrl to set
	 */
	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @param outputProcess the outputProcess to set
	 */
	public void setOutputProcess(OutputProcess outputProcess) {
		this.outputProcess = outputProcess;
	}

	/**
	 * @param outputStream the outputStream to set
	 */
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	/**
	 * @param templatePath the templatePath to set
	 */
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	/**
	 * @param temporaryFile the temporaryFile to set
	 */
	public void setTemporaryFile(File temporaryFile) {
		this.temporaryFile = temporaryFile;
	}

	/**
	 * @param zipped the zipped to set
	 */
	public void setZipped(boolean zipped) {
		this.zipped = zipped;
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
	 * @param citationFileName the citationFileName to set
	 */
	public void setCitationFileName(String citationFileName) {
		this.citationFileName = citationFileName;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the secondaryDownloadOutputs
	 */
	public List<SecondaryOutput> getSecondaryDownloadOutputs() {
		return secondaryDownloadOutputs;
	}

	/**
	 * @param secondaryDownloadOutputs the secondaryDownloadOutputs to set
	 */
	public void setSecondaryDownloadOutputs(
			List<SecondaryOutput> secondaryDownloadOutputs) {
		this.secondaryDownloadOutputs = secondaryDownloadOutputs;
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
}