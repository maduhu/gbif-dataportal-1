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
package org.gbif.portal.web.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.gbif.portal.web.download.Field;
import org.gbif.portal.web.download.UrlField;
import org.gbif.portal.web.filter.CriteriaDTO;
import org.gbif.portal.web.filter.CriteriaUtil;
import org.gbif.portal.web.filter.FilterDTO;
import org.gbif.portal.web.filter.FilterUtils;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.document.AbstractExcelView;

/**
 * Excel view for producing spreadsheets from sets of objects.
 * 
 * @author dmartin
 */
public class ExcelView extends AbstractExcelView {
	
	/** Message source for i18n lookups */
	protected MessageSource messageSource;
	/** the sheet title i18n key */
	protected String resultsSheetTitleI18nKey="occurrence.search.results";
	/** the sheet title i18n key */
	protected String detailsSheetTitleI18nKey="occurrence.search.details";
	
	/** default column width */
	protected short defaultColumnWidth = 15;
	/** char width in a unit HSSF does describe */
	protected short charWidth = 300;
	
	public final static int urlMaxLength=242;
	
	/**
	 * @see org.springframework.web.servlet.view.document.AbstractExcelView#buildExcelDocument(java.util.Map, org.apache.poi.hssf.usermodel.HSSFWorkbook, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	protected void buildExcelDocument(
		     Map model, HSSFWorkbook workbook,
		     HttpServletRequest request, HttpServletResponse response) {
		 
		 Locale locale = RequestContextUtils.getLocale(request);
		 
		 //create results sheet
		 String sheetTitle = messageSource.getMessage(resultsSheetTitleI18nKey, null, locale);
		 HSSFSheet resultsSheet = workbook.createSheet(sheetTitle);
		 resultsSheet.setDefaultColumnWidth((short)(defaultColumnWidth));
		 
		 //create a titles style
		 HSSFCellStyle titlesStyle = workbook.createCellStyle();
		 titlesStyle.setFillPattern((short) HSSFCellStyle.SOLID_FOREGROUND);
		 titlesStyle.setFillBackgroundColor(HSSFColor.DARK_GREEN.index);
		 HSSFFont font = workbook.createFont();
		 font.setColor(HSSFColor.WHITE.index);
		 font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 titlesStyle.setFont(font);

		 //create a hyperlink style
		 HSSFCellStyle hyperlinkStyle = workbook.createCellStyle();
		 HSSFFont hyperLinkFont = workbook.createFont();
		 hyperLinkFont.setColor(HSSFColor.BLUE.index);
		 hyperlinkStyle.setFont(hyperLinkFont);

		 //reused cell reference
		 HSSFCell cell = null;

		 //write results sheet
		 List<Field> fields = (List<Field>) request.getAttribute("requestedFields");
		 List results = (List) request.getAttribute("results");
		 
		 int currentRow = 0;
		 
		 //column headings
		 for (int i=0; i<fields.size(); i++){
			cell = getCell(resultsSheet,currentRow, i);
			cell.setCellStyle(titlesStyle);
			String title = messageSource.getMessage(fields.get(i).getFieldI18nNameKey(), null, locale);
		    setText(cell, title);
		    short titleLength = (short) (title.length() * charWidth);
		    short columnWidth = resultsSheet.getColumnWidth((short) i);
			//update column width for long columns
		    if(columnWidth<titleLength){
				resultsSheet.setColumnWidth((short) i, (short) (titleLength));
			}
		 }

		 currentRow++;
		 //results
		 for (int i=0; i<results.size(); i++){
			 
			Object result = results.get(i);
			
			for(int j=0; j<fields.size();j++){
				
				Field field = fields.get(j);
				cell = getCell(resultsSheet, currentRow, j);
				
				try {
					short textWidth = defaultColumnWidth;
					String propertyValue = field.getRenderValue(request, messageSource, locale, result);
					if(propertyValue!=null)
						setText(cell, propertyValue);
					if(field instanceof UrlField){
						if(propertyValue!=null && propertyValue.length()<urlMaxLength){
							String linkFormula = "HYPERLINK(\"" + propertyValue + "\")"; 
							cell.setCellFormula(linkFormula);
							cell.setCellStyle(hyperlinkStyle);
						}
					}
					if(propertyValue!=null){
						
						int textWidthInt = propertyValue.length() *  charWidth;
						if(textWidthInt>32768){
							textWidth = Short.MAX_VALUE;
						} else {
							textWidth = (short) textWidthInt;
						}
					}
					
					//update column width for long columns
					short columnWidth = resultsSheet.getColumnWidth((short) j);
					
					if(textWidth>columnWidth){
						resultsSheet.setColumnWidth((short) j, (short) textWidth);
					}
				} catch (Exception e) {
					logger.warn(e.getMessage(), e);
				} 
			}			 
			currentRow++;
		 }
		 
		 //set up details sheet
		 HSSFSheet detailsSheet = workbook.createSheet(messageSource.getMessage(detailsSheetTitleI18nKey, null, locale));
		 detailsSheet.setColumnWidth((short)0, (short) 6000);
		 detailsSheet.setColumnWidth((short)1, (short)Short.MAX_VALUE);
;		 List<FilterDTO> filters = (List) request.getAttribute("filters");
		 CriteriaDTO criteria = (CriteriaDTO) request.getAttribute("criteria");
		 String query =  FilterUtils.getQueryDescription(filters, criteria, messageSource, locale);
		 cell = getCell(detailsSheet, 0, 0);
		 cell.setCellStyle(titlesStyle);
		 setText(cell, messageSource.getMessage("occurrence.search.description", null, locale));
		 cell = getCell(detailsSheet, 0, 1);
		 setText(cell, query);
		 //add url for search
		 cell = getCell(detailsSheet, 1, 0);
		 cell.setCellStyle(titlesStyle);
		 setText(cell, messageSource.getMessage("occurrence.search.url", null, locale));
		 cell = getCell(detailsSheet, 1, 1);
		 cell.setCellStyle(hyperlinkStyle);
		 String url = "http://"+request.getHeader("host")+request.getContextPath()+"/occurrences/search.htm?"+CriteriaUtil.getUrl(criteria);
		 setText(cell, url);
		 //there is a formula limit in Excel of 255 characters
		 if(url!=null && url.length()<urlMaxLength){
			 String link = "HYPERLINK(\"" + url + "\")"; 
			 cell.setCellFormula(link);
		 }
		 //add url for download page
		 cell = getCell(detailsSheet, 2, 0);
		 cell.setCellStyle(titlesStyle);
		 setText(cell, messageSource.getMessage("occurrence.search.download.url", null, locale));		 
		 cell = getCell(detailsSheet, 2, 1);
		 cell.setCellStyle(hyperlinkStyle);
		 String downloadurl = "http://"+request.getHeader("host")+request.getContextPath()+"/occurrences/download.htm?"+CriteriaUtil.getUrl(criteria);
		 setText(cell, downloadurl);
		 if(downloadurl!=null && downloadurl.length()<urlMaxLength){
			 String link = "HYPERLINK(\"" + downloadurl + "\")"; 
			 cell.setCellFormula(link);
		  }
		 //add date for this download
		 cell = getCell(detailsSheet, 3, 0);
		 cell.setCellStyle(titlesStyle);
		 setText(cell, messageSource.getMessage("occurrence.search.download.date", null, locale));
		 cell = getCell(detailsSheet, 3, 1);
		 SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		 setText(cell, sdf.format(new Date(System.currentTimeMillis())));
	}
	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @param defaultColumnWidth the defaultColumnWidth to set
	 */
	public void setDefaultColumnWidth(short defaultColumnWidth) {
		this.defaultColumnWidth = defaultColumnWidth;
	}

	/**
	 * @param sheetTitleI18nKey the sheetTitleI18nKey to set
	 */
	public void setResultsSheetTitleI18nKey(String sheetTitleI18nKey) {
		this.resultsSheetTitleI18nKey = sheetTitleI18nKey;
	}
}