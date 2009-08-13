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
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Concrete class that outputs results in a delimited format to an output stream.
 * 
 * @author dmartin
 */
public class DelimitedResultsOutputter extends SelectableFieldsOutputter {

	protected static Log logger = LogFactory.getLog(DelimitedResultsOutputter.class);	
	
	/** The output stream to write to */
	protected OutputStream outputStream;
	/** The delimiter to use */
	protected byte[] delimiterBytes;
	/** The delimiter to use */
	protected byte[] endOfRecordBytes;		
	
	/**
	 * Constructs a results outputter.
	 * 
	 * @param outputStream the outputstream to write to
	 * @param downloadFieldMapping mapping from field names to bean properties
	 * @param requestedFields the requested fields
	 * @param propertyFormatter property formatting, nullable
	 * @param delimiter the field delimiter to use
	 * @param endOfRecord the end of record marker to use
	 */
	public DelimitedResultsOutputter(OutputStream outputStream, Map<String, OutputProperty> downloadFieldMapping, List<String> requestedFields, PropertyFormatter propertyFormatter, String delimiter, String endOfRecord) {
		super(downloadFieldMapping, requestedFields, propertyFormatter);
		this.outputStream = outputStream;
		this.delimiterBytes = delimiter.getBytes();
		this.endOfRecordBytes = endOfRecord.getBytes();
	}	
	
	/**
	 * @see org.gbif.portal.io.SelectableFieldsOutputter#writeEndOfRecord()
	 */
	@Override
	public void writeEndOfRecord() throws IOException{
		outputStream.write(endOfRecordBytes);
	}

	/**
	 * @see org.gbif.portal.io.SelectableFieldsOutputter#writeValue(java.lang.String)
	 */
	@Override
	public void writeValue(String value) throws IOException{
		if(value!=null){
			outputStream.write(value.getBytes());
		}
	}

	@Override
	public void writeDelimiter() throws IOException {
		outputStream.write(delimiterBytes);		
	}
	
	/**
	 * @param outputStream the outputStream to set
	 */
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
}