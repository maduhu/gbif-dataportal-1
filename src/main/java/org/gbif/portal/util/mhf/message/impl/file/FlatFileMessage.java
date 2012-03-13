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

import java.util.List;

import org.gbif.portal.util.file.DelimitedFileReader;
import org.gbif.portal.util.mhf.message.Message;
import org.gbif.portal.util.mhf.message.MessageAccessException;
import org.gbif.portal.util.mhf.message.MessageParseException;



/**
 * A wrapper around a flat file type containing tabular data (E.g. Tab Delmitted, CSV etc).
 * 
 * One should note that you may only go through the message in order and cannot reset to the beginning,
 * similar to a SAX parser
 * 
 * @author trobertson
 */
public class FlatFileMessage implements Message {
	/**
	 * The data
	 */
	DelimitedFileReader dataset;
	
	/**
	 * @param dataset Creates the message
	 */
	public FlatFileMessage(DelimitedFileReader dataset) {
		this.dataset = dataset;
	}

	/**
	 * The location must be of type Integer and will represent the row to return from the file
	 * If the row is before the current row, and exception is thrown (e.g. one pass only)
	 * @see org.gbif.portal.util.mhf.message.Message#getPart(java.lang.Object)
	 */
	public Message getPart(Object location) throws MessageAccessException, MessageParseException {
		if (!(location instanceof Integer)) {
			throw new MessageAccessException("Only Integer locations are supported - recieved: " + location.getClass());
		}
		//Integer locator = (Integer)location;
		
		return null; //data.get(locator.intValue());
	}

	/**
	 * NOT SUPPORTED
	 * @see org.gbif.portal.util.mhf.message.Message#getParts(java.lang.Object)
	 */
	public List<Message> getParts(Object location) throws MessageAccessException, MessageParseException {
		throw new MessageAccessException("Not supported in FlatFileMessage");
	}
	
	/**
	 * The location must be of type Integer[][]
	 * @see org.gbif.portal.util.mhf.message.Message#getPartAsString(java.lang.Object)
	 */
	public String getPartAsString(Object location) throws MessageAccessException {
		if (!(location instanceof Integer[][])) {
			throw new MessageAccessException("Only Integer[][] locations are supported - recieved: " + location.getClass());
		}
		//Integer[][] locator = (Integer[][])location;
		return null;
	}

	/**
	 * NOT SUPPORTED
	 * @see org.gbif.portal.util.mhf.message.Message#getPartsAsString(java.lang.Object)
	 */
	public List<String> getPartsAsString(Object location) throws MessageAccessException {
		throw new MessageAccessException("Not supported in FlatFileMessage");
	}

	/**
	 * Throws an exception
	 */
	public String getRawData() throws MessageAccessException {
		throw new MessageAccessException("Not supported in FlatFileMessage");
	}
	
	/**
	 * Throws an exception
	 */
	public String getLoggableData() throws MessageAccessException {
		throw new MessageAccessException("Not supported in FlatFileMessage");
	}
}
