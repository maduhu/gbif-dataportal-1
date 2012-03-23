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

package org.gbif.portal.util.mhf.message.impl.xml.accessor;

import org.dom4j.XPath;
import org.gbif.portal.util.mhf.message.Message;
import org.gbif.portal.util.mhf.message.MessageAccessException;
import org.gbif.portal.util.mhf.message.MessageAccessor;
import org.gbif.portal.util.mhf.message.MessageParseException;


/**
 * Takes an XPath Expression and invokes the "getPart()" on the Message to return a
 * Message as the response.
 *
 * @author Tim Robertson
 */
public class MessageFromXPathAccessor implements MessageAccessor {
  /**
   * That is used to access the message
   */
  protected XPath xPath;

  /**
   * @throws MessageAccessException
   * @see org.gbif.portal.util.mhf.message.MessageAccessor#invoke(org.gbif.portal.util.mhf.message.Message)
   */
  public Message invoke(Message message) throws MessageAccessException {
    try {
      return message.getPart(getXPath());
    } catch (MessageParseException e) {
      throw new MessageAccessException("Error creating a Message from the sub message part", e);
    }
  }

  /**
   * @return Returns the xPath.
   */
  public XPath getXPath() {
    return xPath;
  }

  /**
   * @param path The xPath to set.
   */
  public void setXPath(XPath path) {
    xPath = path;
  }
}
