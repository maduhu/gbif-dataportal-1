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
package org.gbif.portal.util.mhf.message.impl.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.gbif.portal.util.mhf.message.Message;
import org.gbif.portal.util.mhf.message.MessageFactory;
import org.gbif.portal.util.mhf.message.MessageParseException;



/**
 * The concrete factory for Building XML based Messages
 * @author trobertson
 */
public class XMLMessageFactory implements MessageFactory {
	/**
	 * Logger
	 */
	protected Log logger = LogFactory.getLog(XMLMessageFactory.class);
	
	/** 
	 * @see org.gbif.portal.util.mhf.message.MessageFactory#build(java.io.InputStream)
	 */
	public Message build(InputStream rawDataStream) throws MessageParseException  {
		try {
			SAXReader xmlReader = new SAXReader();
			return new XMLMessage(xmlReader.read(rawDataStream));			
		} catch (DocumentException e) {
			try {
				StringWriter sw = new StringWriter();
				int c;
				while ((c = rawDataStream.read()) != -1) {
					sw.write(c);
				}
				logger.warn("Unparsed data: " + sw.toString());
			} catch (IOException e1) {
				logger.warn("Unable to even read the stream... " + e1.getMessage());
			}
			
			throw new MessageParseException("RawXml is not parsable: " + e.getMessage(), e);
		}		
	}
	
	/**
	 * Creates a new Message based on the node that is passed in
	 * @param node To copy or detach
	 * @param detach If the node may be detached from the parent document
	 * @return A new message
	 */
	public XMLMessage build(Node node, boolean detach) {
		Document document = DocumentFactory.getInstance().createDocument();
		if (detach) {
			document.add(node.detach());
		} else {
			Node cloned = (Node)node.clone();
			document.add(cloned);
		}
		return new XMLMessage(document);
	}
	
	/**
	 * Creates a new Message based on the node that is passed in
	 * @param node That will be clones
	 * @return A new message
	 */
	public XMLMessage build(Node node) {
		return build(node, false);
	}
	
	/**
	 * Creates a new Message based on the node that is passed in
	 * @param node That will be clones
	 * @return A new message
	 * @throws MessageParseException If the String cannot be parsed
	 */
	public XMLMessage build(String rawData) throws MessageParseException {
		StringReader reader = new StringReader(rawData);
		try {
			SAXReader xmlReader = new SAXReader();
			return new XMLMessage(xmlReader.read(reader), rawData);
			
		} catch (DocumentException e) {
			throw new MessageParseException("RawXml is not parsable: " + e.getMessage(), e, rawData);
		}
	}
}
