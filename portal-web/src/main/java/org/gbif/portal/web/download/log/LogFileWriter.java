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
package org.gbif.portal.web.download.log;

import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.io.DataResourceAuditor;
import org.gbif.portal.io.LogStatAuditor;
import org.gbif.portal.io.ResultsOutputter;
import org.gbif.portal.io.VelocityResultsOutputter;
import org.gbif.portal.web.controller.dataset.LogQuery;
import org.gbif.portal.web.download.FieldFormatter;
import org.gbif.portal.web.download.VelocityFileWriter;

/**
 * Writes a file out using velocity.
 * 
 * @author dmartin
 */
public class LogFileWriter extends VelocityFileWriter {

	protected static Log logger = LogFactory.getLog(LogFileWriter.class);	
	
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
		LogStatAuditor lsa = new LogStatAuditor();
		DataResourceAuditor dra = new DataResourceAuditor();
		lsa.setNextResultsOutputter(dra);
		dra.setNextResultsOutputter(resultsOutputter);
		resultsOutputter = lsa;
		
		//run the output process
		outputProcess.process(resultsOutputter);
		
		//write the footer
		writeTemplate(footerTemplatePath, outputStream);
		
		//close the file stream
		if(zipped){
			((ZipOutputStream) outputStream).closeEntry();
		}

		//log usage
		if(logEventId!=null && resultsOutputter instanceof DataResourceAuditor){
			downloadUtils.logDownloadUsage(dra, logEventId);
		}

		//output template aggregate stats
		if(outputProcess instanceof LogQuery){
			LogQuery lq = (LogQuery) outputProcess;
			lq.outputLogStats(outputStream, lsa.getLogStats());
		}
		
		//once returned rename the file to indicate the file has been written
		signalFileWriteComplete();
	}
}