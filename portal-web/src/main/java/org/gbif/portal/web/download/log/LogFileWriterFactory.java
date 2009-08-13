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

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.io.OutputProperty;
import org.gbif.portal.web.download.Field;
import org.gbif.portal.web.download.OutputProcess;
import org.gbif.portal.web.download.SecondaryOutput;
import org.gbif.portal.web.download.VelocityFileWriterFactory;

/**
 * A File Writer factory for VelocityFileWriter threads.
 * 
 * @author dmartin
 */
public class LogFileWriterFactory extends VelocityFileWriterFactory {

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
		LogFileWriter lfw = new LogFileWriter();
		initialiseVelocityFileWriter(request, downloadFields, downloadFieldMappings, outputProcess, secondaryOutputs, file, outputStream, description, lfw);
		return lfw;
	}
}