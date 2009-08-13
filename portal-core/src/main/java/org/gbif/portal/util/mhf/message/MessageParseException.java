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

/**
 * Used to indicate that some unexpected parsing of a message occured
 * It is possible to set the Raw Data that caused the error
 * @author trobertson
 */
public class MessageParseException extends Exception {
	/**
	 * Generated
	 */
	private static final long serialVersionUID = -7970013432067581159L;
	protected String rawData;
	
	/**
	 * Takes the message 
	 * @param message Message
	 */
	public MessageParseException(String message) {
		super(message);
	}

	/**
	 * Takes the cause
	 * @param cause of the exception
	 */
	public MessageParseException(Throwable cause) {
		super(cause);
	}

	/**
	 * Takes the message and the cause
	 * @param message Message
	 * @param cause Cause
	 */
	public MessageParseException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Takes the message and the cause and the raw data
	 * @param message Message
	 * @param cause Cause
	 * @param data Data
	 */
	public MessageParseException(String message, Throwable cause, String data) {
		super(message, cause);
		setRawData(data);
	}

	/**
	 * @return Returns the rawData.
	 */
	public String getRawData() {
		return rawData;
	}

	/**
	 * @param rawData The rawData to set.
	 */
	public void setRawData(String rawData) {
		this.rawData = rawData;
	}
}
