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
import java.util.UUID;

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
public class RegistryClientImpl extends BaseClient implements RegistryClient{
  private WebResource REGISTRY_RESOURCE;

  @Inject
  public RegistryClientImpl(PortalConfig cfg, ApacheHttpClient client) {
    log.info("Creating registry client with base url "+ cfg.getRegistryWs());
    REGISTRY_RESOURCE= client.resource(cfg.getRegistryWs());
  }

  @Override
  public List<Object> searchDatasets(String q){
    //TODO: manipulate query string?
    MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
    queryParams.add("q", StringUtils.trimToNull(q));
    List<Object> resources = REGISTRY_RESOURCE.path("resource.json").queryParams(queryParams).get(List.class);
    return resources;
  }

  @Override
  public Object getDataset(UUID uuid) {
    return REGISTRY_RESOURCE.path(uuid.toString()+".json").get(Object.class);
  }
}
