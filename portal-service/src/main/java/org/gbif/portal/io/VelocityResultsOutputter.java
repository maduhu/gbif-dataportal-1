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
import java.io.OutputStreamWriter;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.tools.generic.DateTool;
import org.gbif.portal.util.request.TemplateUtils;

/**
 * A results outputter that writes out to an output stream using velocity.
 * 
 * @author dmartin
 */
public class VelocityResultsOutputter implements ChainableResultsOutputter {

	protected static Log logger = LogFactory.getLog(VelocityResultsOutputter.class);
	
	/** Velocity context key for the date tool */
	public static String dateFormatterKey = "dateFormatter";
	/** Velocity context key for the property formatter tool */
	public static String propertyFormatterKey = "propertyFormatter";

	/** The output stream to write to */
	protected OutputStream outputStream;
	/** The path to the velocity template to use */
	protected String templatePath;
	/** The property formatter to use */
	protected PropertyFormatter propertyFormatter;
	/** The next results outputter to pass on to */
	protected ResultsOutputter nextResultsOutputter;
	
	private TemplateUtils tu = null;
	
	/** The output stream wrapper to write to */
	private OutputStreamWriter writer = null;
	
	/** The velocityContext to use */
	private VelocityContext velocityContext = null; 

	/** The template to use */
	private Template template = null;
	
	/**
	 * Create a new velocity results outputter that will write to the supplied
	 * output stream.
	 * 
	 * @param outputStream
	 * @param templatePath
	 */
	public VelocityResultsOutputter(OutputStream outputStream, String templatePath, PropertyFormatter propertyFormatter) throws Exception {
		this.outputStream = outputStream;
		this.templatePath = templatePath;
		this.propertyFormatter = propertyFormatter;
		this.velocityContext = new VelocityContext();
		this.velocityContext.put(dateFormatterKey, new DateTool());
		this.velocityContext.put(propertyFormatterKey, propertyFormatter);
		this.template = Velocity.getTemplate(this.templatePath);
		this.template.initDocument();
		this.tu = new TemplateUtils();
		this.writer = new OutputStreamWriter(outputStream);
	}

	/**
	 * @see org.gbif.portal.io.ResultsOutputter#write(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public void write(Map beans) throws IOException {
		try {
			
			//clone the context
			VelocityContext context = new VelocityContext(velocityContext);
			
			//add all elements to the context
			for(Object key: beans.keySet())
				context.put((String) key, beans.get(key));
			
			//write it out
			tu.merge(template, context, writer);
			writer.flush();
			
			//remove context
			context=null;
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * @see org.gbif.portal.io.ChainableResultsOutputter#getNextResultsOutputter()
	 */
	public ResultsOutputter getNextResultsOutputter() {
		return nextResultsOutputter;
	}
	
	/**
	 * @param nextResultsOutputter the nextResultsOutputter to set
	 */
	public void setNextResultsOutputter(ResultsOutputter nextResultsOutputter) {
		this.nextResultsOutputter = nextResultsOutputter;
	}
	
	/**
	 * @see org.gbif.portal.io.ChainableResultsOutputter#isChainInOnePass()
	 */
	public boolean isChainInOnePass() {
		return false;
	}
}