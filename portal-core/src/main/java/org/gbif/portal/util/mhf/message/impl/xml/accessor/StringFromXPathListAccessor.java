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

import java.util.List;


/**
 * Takes a list of XPath Expressions and invokes them in turn until it finds
 * an element with actual content.
 *
 * @author Donald Hobern
 */
public class StringFromXPathListAccessor implements MessageAccessor {
  /**
   * That is used to access the message
   */
  protected List xPathList;

  /**
   * @throws MessageAccessException
   * @see org.gbif.portal.util.mhf.message.MessageAccessor#invoke(org.gbif.portal.util.mhf.message.Message)
   */
  public String invoke(Message message) throws MessageAccessException {
    for (Object xPath : xPathList) {
      String s = message.getPartAsString((XPath) xPath);
      if (s != null && !s.isEmpty()) {
        return s;
      }
    }
    return null;
  }

  /**
   * @return the xPathList
   */
  public List getXPathList() {
    return xPathList;
  }

  /**
   * @param pathList the xPathList to set
   */
  public void setXPathList(List pathList) {
    xPathList = pathList;
  }
}
