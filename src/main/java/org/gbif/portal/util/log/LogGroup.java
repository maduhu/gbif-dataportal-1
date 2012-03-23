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
package org.gbif.portal.util.log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to manage log message groups.
 *
 * @author Donald Hobern
 */
public class LogGroup implements Serializable {

  /**
   * Generated
   */
  private static final long serialVersionUID = 985927704928612695L;

  public static long UNINITIALISED = -1;

  protected long id;
  protected Map<String, GbifLogMessage> map = null;
  protected boolean isEnded = false;

  public LogGroup() {
    id = UNINITIALISED;
  }

  public LogGroup(long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * If the log group isnt "ended" this will add the message
   * to the map which is keyed on message.toString()
   *
   * @param message
   * @return
   */
  public boolean handleMessage(GbifLogMessage message) {
    boolean handled = false;
    if (!isEnded()) {
      if (map == null) {
        map = new HashMap<String, GbifLogMessage>();
        map.put(message.toString(), message);
        message.setCount(0);
      } else {
        GbifLogMessage oldMessage = map.get(message.toString());
        if (oldMessage == null) {
          map.put(message.toString(), message);
          message.setCount(0);
        } else {
          oldMessage.setCount(oldMessage.getCount() + 1);
        }
      }
      handled = true;
    }
    return handled;
  }

  public Map<String, GbifLogMessage> getStoredMessages() {
    return map;
  }

  protected void end() {
    isEnded = true;
  }

  public boolean isEnded() {
    return isEnded;
  }
}
