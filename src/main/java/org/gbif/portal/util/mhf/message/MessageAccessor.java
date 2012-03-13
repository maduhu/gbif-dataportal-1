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
 * A MessageAccessor is an object that accesses a message based on the 
 * location passed in.  It is effectively a Mediator that follows the Command 
 * pattern such that an accessor can be configured in the property store.
 * 
 * An example of a MessageAccessor could be an XPathAccessor that is configured
 * to take an XPath expression and returns the result of accessing the XMLMessage.  
 * 
 * @author Tim Robertson
 */
public interface MessageAccessor {
	/**
	 * Accesses the supplied message and returns the response in accordance
	 * with the configuration of the Accessor
	 * 
	 * @param message To access
	 * @return The result of accessing the message 
	 * @throws MessageAccessException should there be an error accessing the message
	 */
	public Object invoke(Message message) throws MessageAccessException;
}
