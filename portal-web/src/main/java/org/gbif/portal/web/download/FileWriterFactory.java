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

/**
 * A spring wired factory for creating runnable file writers.
 * 
 * @author dmartin
 */
public interface FileWriterFactory {

	/**
	 * Create a file writer ready for execution.
	 * 
	 * @param file file to write to
	 * @param downloadFields the fields to include
	 * @param downloadFieldMappings how to get the fields
	 * @param outputProcess the output process to run
	 * @param outputStream the output stream to write to
	 * @param request
	 * @param response
	 * 
	 * @return runnable ready for execution
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
	);
	
	/**
	 * The file extension to use.
	 * 
	 * @return the file extension string
	 */
	public String getFileExtension();
}