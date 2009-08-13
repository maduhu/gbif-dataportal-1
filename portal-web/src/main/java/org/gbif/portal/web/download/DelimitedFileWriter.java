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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.io.DataResourceAuditor;
import org.gbif.portal.io.DelimitedResultsOutputter;
import org.gbif.portal.io.OutputProperty;
import org.gbif.portal.io.ResultsOutputter;

/**
 * Runnable that outputs a delimited file.
 * 
 * @author dmartin
 */
public class DelimitedFileWriter extends FileWriter {
	
	protected static Log logger = LogFactory.getLog(DelimitedFileWriter.class);

	/** The locale to use */
	private Locale locale;
	/** The mappings from field names to Output properties */
	protected Map<String, OutputProperty> downloadFieldMappings;
	/** The host url */
	protected String hostUrl;
	/** Field delimiter */
	protected String delimiter = "\t";
	/** End of record marker */
	protected String endOfRecord = "\n";
	/** The secondary outputs to run */
	protected List<SecondaryOutput> secondaryDownloadOutputs;	
	/**
	 * Write out the delimited file.
	 * 
	 * @throws IOException 
	 */
	public void writeFile() throws Exception {

		logger.debug("#######Delimited File Writer starting writeFile...");
		//Create FieldFormatter - the object that is i18n aware
		DelimitedFieldFormatter dff = new DelimitedFieldFormatter(downloadFields, messageSource, locale, hostUrl);
		dff.setDelimiter(delimiter);
		//handy for debug
//		Field idField = new Field();
//		idField.setFieldI18nNameKey("occurrenceRecordId");
//		idField.setFieldName("occurrenceRecordId");
//		downloadFields.add(0, idField);		
		
		//stick requested fields into list of strings
		List<String> requestedFieldNames = new ArrayList<String>();
		for(Field field: downloadFields){
			requestedFieldNames.add(field.getFieldName());
			if(logger.isDebugEnabled()){
				logger.debug("Requested field = "+field.getFieldName());
			}
		}
		
		//Create Results outputter - the object that knows about the required format
		ResultsOutputter resultsOutputter = new DelimitedResultsOutputter(outputStream, downloadFieldMappings, requestedFieldNames, dff, delimiter, endOfRecord);
		
		//check for citation
		if(addCitation && zipped){
			DataResourceAuditor cro = new DataResourceAuditor();	
			cro.setNextResultsOutputter(resultsOutputter);
			resultsOutputter = cro;
		}
		
		//write out the field headings
		byte[] delimiterInBytes = delimiter.getBytes();
		for(Field field: downloadFields){
			outputStream.write(messageSource.getMessage(field.getFieldI18nNameKey(), null, field.getFieldI18nNameKey(), locale).getBytes());
			outputStream.write(delimiterInBytes);
		}
		outputStream.write(endOfRecord.getBytes());
	
		//pass both to the query manager
		outputProcess.process(resultsOutputter);
		
		//close the file stream
		if(zipped)
			((ZipOutputStream)outputStream).closeEntry();

		//write out the citation
		if(addCitation && zipped){
			downloadUtils.outputCitation(outputStream, (DataResourceAuditor) resultsOutputter, citationFileName, locale, hostUrl);
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
	 * @return the downloadFieldMappings
	 */
	public Map<String, OutputProperty> getDownloadFieldMappings() {
		return downloadFieldMappings;
	}

	/**
	 * @param downloadFieldMappings the downloadFieldMappings to set
	 */
	public void setDownloadFieldMappings(
			Map<String, OutputProperty> downloadFieldMappings) {
		this.downloadFieldMappings = downloadFieldMappings;
	}

	/**
	 * @return the hostUrl
	 */
	public String getHostUrl() {
		return hostUrl;
	}

	/**
	 * @param hostUrl the hostUrl to set
	 */
	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @return the delimiter
	 */
	public String getDelimiter() {
		return delimiter;
	}

	/**
	 * @param delimiter the delimiter to set
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	/**
	 * @return the endOfRecord
	 */
	public String getEndOfRecord() {
		return endOfRecord;
	}

	/**
	 * @param endOfRecord the endOfRecord to set
	 */
	public void setEndOfRecord(String endOfRecord) {
		this.endOfRecord = endOfRecord;
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
}