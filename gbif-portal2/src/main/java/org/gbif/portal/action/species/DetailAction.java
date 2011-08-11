/*
 * Copyright 2011 Global Biodiversity Information Facility (GBIF)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gbif.portal.action.species;

import org.gbif.portal.action.BaseAction;
import org.gbif.portal.client.ChecklistBankClient;

import java.util.Map;

import com.google.inject.Inject;

public class DetailAction extends BaseAction {
  @Inject
  private ChecklistBankClient clb;
  // detail
  private Integer id;
  private Map usage;

  @Override
  public String execute() {
    // static until layout is solid
    return SUCCESS;
    /** TODO: re-enable dynamic lookup */
//    if (id!=null) {
//      usage = clb.getUsage(id);
//      if (usage != null) {
//        return SUCCESS;
//      }
//    }
//    return NOT_FOUND;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public Map getUsage() {
    return usage;
  }
}
