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
package org.gbif.portal.client;

import org.gbif.portal.config.PortalConfig;

import javax.inject.Singleton;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.apache.commons.lang.StringUtils;

@Singleton
/**
 * A simple client to the checklist bank json webservices.
 * API documentation: http://ecat-dev.gbif.org/api/clb
 * TODO: this is broken because the server is broken - see http://code.google.com/p/gbif-ecat/issues/detail?id=61
 */
public class ChecklistBankClientImpl extends BaseClient implements ChecklistBankClient{
  private WebResource USAGE_RESOURCE;
  private WebResource NAV_RESOURCE;

  @Inject
  public ChecklistBankClientImpl(PortalConfig cfg, ApacheHttpClient client) {
    log.info("Creating checklist bank client with base url "+ cfg.getSpeciesWs());
    USAGE_RESOURCE = client.resource(cfg.getSpeciesWs() + "usage/");
    NAV_RESOURCE = client.resource(cfg.getSpeciesWs() + "resource");
  }

  @Override
  public List<Map> searchSpecies(String q) {
    MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
    queryParams.add("q", StringUtils.trimToNull(q));
    queryParams.add("rkey", "1");
    queryParams.add("showRanks", "kpcofg");
    queryParams.add("showVernaculars", "all");
    queryParams.add("showIds", "true");
    queryParams.add("page", "1");
    queryParams.add("pagesize", "100");

    List<Map> results = removeMessageWrapperAsList(USAGE_RESOURCE.queryParams(queryParams).get(Map.class));
    return results;
  }

  @Override
  public List<Map> searchUsages(String q) {
    // TODO: Write implementation
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public Map getUsage(Integer id) {
    return removeMessageWrapperAsMap(USAGE_RESOURCE.path(id.toString()).get(Map.class));
  }

  private List<Map> removeMessageWrapperAsList(Map obj){
    return (List<Map>) obj.get("data");
  }
  private Map removeMessageWrapperAsMap(Map obj) {
    return (Map) obj.get("data");
  }
}
