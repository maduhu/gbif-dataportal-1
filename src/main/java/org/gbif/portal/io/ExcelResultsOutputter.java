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
package org.gbif.portal.io;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * A results outputter that uses the POI API to write out an excel 
 * document to the wrapped stream.
 * 
 * @author dmartin
 */
public class ExcelResultsOutputter extends SelectableFieldsOutputter  {
	
	/** The workbook to work against */
	protected HSSFWorkbook workbook;
	/** The current work sheet */
	protected HSSFSheet sheet;
	
	protected HSSFCellStyle hyperlinkStyle;
	
	protected int currentRow = 1;
	
	/** default column width */
	protected short defaultColumnWidth = 15;
	/** char width in a unit HSSF does describe */
	protected short charWidth = 300;	
	
	public final static int urlMaxLength=242;
	
	/**
	 * 
	 * @param workbook
	 * @param sheet
	 * @param downloadFieldMapping
	 * @param selectedFieldNames
	 * @param propertyFormatter
	 */
	public ExcelResultsOutputter(HSSFWorkbook workbook, HSSFSheet sheet, Map<String, OutputProperty> downloadFieldMapping, List<String> selectedFieldNames, PropertyFormatter propertyFormatter) {
		super(downloadFieldMapping, selectedFieldNames, propertyFormatter);
		this.sheet = sheet;
		this.workbook = workbook;
		//initialise styles
		//create a hyperlink style
		this.hyperlinkStyle = workbook.createCellStyle();
		HSSFFont hyperLinkFont = workbook.createFont();
		hyperLinkFont.setColor(HSSFColor.BLUE.index);
		hyperlinkStyle.setFont(hyperLinkFont);		
	}	
			
	@Override
	public void write(Map beanMap) throws IOException {		
		
		for(int j=0; j<selectedFieldNames.size();j++){
			
			String propertyName = selectedFieldNames.get(j);
			String propertyValue = getSelectedFieldValue(beanMap, propertyName);
			HSSFCell cell = getCell(currentRow, j);
			
			try {
				short textWidth = defaultColumnWidth;
				if(propertyValue!=null)
					setText(cell, propertyValue);
				if(propertyValue!=null && propertyValue.startsWith("http")){
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
				short columnWidth = sheet.getColumnWidth((short) j);
				
				if(textWidth>columnWidth){
					sheet.setColumnWidth((short) j, (short) textWidth);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} 
		}
		//move to next row in the sheet
		currentRow++;
	}
	
	protected HSSFCell getCell(int row, int col) {

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
		cell.setCellValue(text);
	}	
	
	@Override
	public void writeDelimiter() throws IOException {
		//not in use
	}
	
	@Override
	public void writeEndOfRecord() throws IOException {
		//not in use
	}

	@Override
	public void writeValue(String value) throws IOException {
		//not in use
	}
}