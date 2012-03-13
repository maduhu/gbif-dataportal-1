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
package org.gbif.portal.util.mhf.message.impl.file;

import java.io.InputStream;

import org.gbif.portal.util.file.DelimitedFileReader;
import org.gbif.portal.util.mhf.message.Message;
import org.gbif.portal.util.mhf.message.MessageFactory;
import org.gbif.portal.util.mhf.message.MessageParseException;

/**
 * The concrete factory for Building Flat file based Messages
 * @author trobertson
 */
public class FlatFileMessageFactory implements MessageFactory {
	/** 
	 * @see org.gbif.portal.util.mhf.message.MessageFactory#build(java.io.InputStream)
	 */
	public Message build(InputStream rawDataStream) throws MessageParseException  {
		try {
			DelimitedFileReader dataset = new DelimitedFileReader(rawDataStream, "\t", null, true);
			return new FlatFileMessage(dataset);
		} catch (Exception e) {
			throw new MessageParseException(e);
		}
	}
	
}
