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
package org.gbif.portal.util.mhf.message;

import java.util.List;

/**
 * This defines the accessors to underlying raw data
 * for it to be usable in the MHF.
 * 
 * A message can be considered anything that can be divided into
 * parts, which can be located and extracted.  
 * 
 *  Implementations could include:
 *  <ul>
 *  	<li>XMLMessage with parts located by xpath</li>
 *  	<li>CSVMessage with parts located by row and column ids</li>
 *  	<li>BeanMessage with parts located by name</li>
 *  </ul>
 *  
 *  Implementations can decide whether to return null or exception when 
 *  using accessors and this should be documented on the concrete instance   
 *  
 * @author trobertson
 */
public interface Message {
	/**
	 * Accessor to the underlying data
	 * Implementations of this may choice to use this to return 
	 * a loggable version (e.g. for a Bean implementation) or the full
	 * data (e.g. for an XML implementation)
	 * 
	 * @return The raw data which may be complete or formatted for logging purposes
	 * @throws MessageAccessException If the data cannot be accessed 
	 */
	public String getRawData() throws MessageAccessException;
	
	/**
	 * Accessor to the underlying data to be returned as a loggable format
	 * 
	 * @return The raw data which is formatted for logging purposes
	 * @throws MessageAccessException If the data cannot be accessed 
	 */
	public String getLoggableData() throws MessageAccessException;
	
	/**
	 * Using the supplied location, returns a sub message representing the 
	 * section of the original message.
	 * 
	 * @param location Of the part that needs returned
	 * @return The part identified by the location, as a new Message, or possibly null
	 * @throws MessageAccessException If exception occured locating the part 
	 * @throws MessageParseException If exception occured building a new Message from the part
	 */
	public Message getPart(Object location) throws MessageAccessException, MessageParseException;
	
	/**
	 * Using the supplied location, return a List of sub message representing the 
	 * sections of the original message.
	 * 
	 * @param location That identifies the sub parts
	 * @return A List of Message representing the parts located
	 * @throws MessageAccessException If exception occured locating the parts
	 * @throws MessageParseException If exception occured building a new Message from the part
	 */
	public List<Message> getParts(Object location) throws MessageAccessException, MessageParseException;
	
	/**
	 * Returns the String value of a Message part.
	 * 
	 * @param location To identify the part 
	 * @return The part as a String
	 * @throws MessageAccessException If exception occured locating the part
	 */
	public String getPartAsString(Object location) throws MessageAccessException;
	
	/**
	 * Returns the List of String values of the Message that can be located by the 
	 * location supplied
	 * 
	 * @param location To identify the parts
	 * @return The String values of the parts found at the supplied location
	 * @throws MessageAccessException If exception occured locating the parts
	 */
	public List<String> getPartsAsString(Object location) throws MessageAccessException;
}
