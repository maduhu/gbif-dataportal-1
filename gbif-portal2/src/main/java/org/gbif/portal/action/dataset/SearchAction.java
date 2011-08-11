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
package org.gbif.portal.action.dataset;

import org.gbif.portal.action.BaseAction;
import org.gbif.portal.client.RegistryClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

public class SearchAction extends BaseAction {
  @Inject
  private RegistryClient registry;
  // search
  private String q;
  private List<?> datasets;

  @Override
  public String execute() {
    log.debug("Searching for datasets matching [{}]", q);
    /** TODO: re-enable real lookup */
    // using static just to make sure all layout is working properly
    q = "Pontaurus";
    datasets = new ArrayList<Map>();

//    datasets=registry.searchDatasets(q);
//    log.debug("Got [{}] datasets matching [{}]", datasets.size(), q);
    return SUCCESS;
  }

  public List<?> getDatasets() {
    return datasets;
  }

  public String getQ() {
    return q;
  }

  public void setQ(String q) {
    this.q = q;
  }
}
