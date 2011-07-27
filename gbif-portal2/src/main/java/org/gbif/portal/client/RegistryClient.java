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

import javax.inject.Singleton;
import javax.ws.rs.core.MultivaluedMap;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import com.sun.jersey.api.json.JSONConfiguration;

@Singleton
public class RegistryClient extends BaseClient{

  private String WEB_SERVICE_URL;
  private WebResource DATASET_RESOURCE;
  private WebResource MEMBER_RESOURCE;

  @Inject
  public RegistryClient(@Named("ws.dataset") String RegistryWsBaseUrl) {
    this.WEB_SERVICE_URL = RegistryWsBaseUrl;
    ClientConfig cc = new DefaultClientConfig();
    //cc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
    cc.getClasses().add(JacksonJsonProvider.class);
    Client client = ApacheHttpClient.create(cc);
    DATASET_RESOURCE= client.resource(WEB_SERVICE_URL+"resource.json");
    MEMBER_RESOURCE = client.resource(WEB_SERVICE_URL + "resource.json");
  }

  public List<Object> searchDatasets(String q){
    //TODO: manipulate query string?
    MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
    queryParams.add("q", StringUtils.trimToNull(q));
    List<Object> resources = DATASET_RESOURCE.queryParams(queryParams).get(List.class);
    return resources;
  }
}
