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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

public class SearchAction extends BaseAction {
  @Inject
  private ChecklistBankClient clb;
  // search
  private String q;
  private List<Map> usages;

  @Override
  public String execute() {
    /** TODO: re-enable dynamic content */
    // using static just to make sure all layout is working properly
    q = "Puma concolor";
    usages = new ArrayList<Map>();
    usages.add(new HashMap<String, String>());
    usages.add(new HashMap<String, String>());
//    if (q == null) {
//      log.debug("Got species search request for null q, ignoring");
//    }
//    else {
//      log.debug("Trying species search for q [{}]", q);
//      try {
//        usages=clb.searchSpecies(q);
//      }
//      catch (Exception e) {
//        log.warn("Got exception trying for species lookup with q ["+q+"]", e);
//      }
//      log.debug("Got [{}] species results for q [{}]", usages.size(), q);
//    }
    return SUCCESS;
  }

  public String getQ() {
    return q;
  }

  public void setQ(String q) {
    this.q = q;
  }

  public List<Map> getUsages() {
    return usages;
  }
}
