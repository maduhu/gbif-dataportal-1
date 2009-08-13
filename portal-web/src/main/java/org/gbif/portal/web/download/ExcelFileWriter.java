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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.gbif.portal.io.DataResourceAuditor;
import org.gbif.portal.io.ExcelResultsOutputter;
import org.gbif.portal.io.OutputProperty;
import org.gbif.portal.io.ResultsOutputter;
import org.springframework.context.MessageSource;

/**
 * A Runnable File Writer for creating excel files.
 * 
 * @author dmartin
 */
public class ExcelFileWriter extends FileWriter {
	
	protected static Log logger = LogFactory.getLog(ExcelFileWriter.class);

	/** The locale to use */
	private Locale locale;
	/** The mappings from field names to Output properties */
	protected Map<String, OutputProperty> downloadFieldMappings;
	/** The host url */
	protected String hostUrl;
	/** The sheet to create */
	protected String sheetName;
	/** The secondary outputs to run */
	protected List<SecondaryOutput> secondaryDownloadOutputs;	

	/**
	 * Write out the delimited file.
	 * 
	 * @throws IOException 
	 */
	public void writeFile() throws Exception {

		//Create FieldFormatter - the object that is i18n aware
		FieldFormatter ff = new FieldFormatter(downloadFields, messageSource, locale, hostUrl);
		
		//stick requested fields into list of strings
		List<String> requestedFieldNames = new ArrayList<String>();
		for(Field field: downloadFields)
			requestedFieldNames.add(field.getFieldName());
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(messageSource.getMessage(sheetName, null, sheetName, locale));

		//create a titles style
		HSSFCellStyle titlesStyle = workbook.createCellStyle();
		titlesStyle.setFillPattern((short) HSSFCellStyle.SOLID_FOREGROUND);
		titlesStyle.setFillBackgroundColor(HSSFColor.DARK_GREEN.index);
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.WHITE.index);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		titlesStyle.setFont(font);
		
		//write out the field headings
		for(int i=0; i<downloadFields.size();i++){
			Field field = downloadFields.get(i);
			HSSFCell cell = getCell(sheet, 0, i);
			cell.setCellStyle(titlesStyle);
			setText(cell, messageSource.getMessage(field.getFieldI18nNameKey(), null, field.getFieldI18nNameKey(), locale));
		}
		
		//Create Results outputter - the object that knows about the required format
		ResultsOutputter resultsOutputter = new ExcelResultsOutputter(workbook, sheet, downloadFieldMappings, requestedFieldNames, ff);
		
		//check for citation
		if(addCitation && zipped){
			DataResourceAuditor cro = new DataResourceAuditor();	
			cro.setNextResultsOutputter(resultsOutputter);
			resultsOutputter = cro;
		}
		
		//pass both to the query manager
		outputProcess.process(resultsOutputter);

		//write out the workbook
		workbook.write(outputStream);
		outputStream.flush();
		
		//close the file stream
		if(zipped)
			((ZipOutputStream) outputStream).closeEntry();

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
	 * Gets a cell at the specified row and column.
	 * 
	 * @param sheet
	 * @param row
	 * @param col
	 * @return
	 */
	protected HSSFCell getCell(HSSFSheet sheet, int row, int col) {
		HSSFRow sheetRow = sheet.getRow(row);
		if (sheetRow == null) {
			sheetRow = sheet.createRow(row);
		}
		HSSFCell cell = sheetRow.getCell((short) col);
		if (cell == null) {
			cell = sheetRow.createCell((short) col);
		}
		return cell;
	}		
	
	/**
	 * Convenient method to set a String as text content in a cell.
	 * @param cell the cell in which the text must be put
	 * @param text the text to put in the cell
	 */
	protected void setText(HSSFCell cell, String text) {
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		HSSFRichTextString rts = new HSSFRichTextString(text);
		cell.setCellValue(rts);
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
	 * @param addCitation the addCitation to set
	 */
	public void setAddCitation(boolean addCitation) {
		this.addCitation = addCitation;
	}

	/**
	 * @param sheetName the sheetName to set
	 */
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
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