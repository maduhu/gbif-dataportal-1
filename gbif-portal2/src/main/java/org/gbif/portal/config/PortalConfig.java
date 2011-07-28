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
package org.gbif.portal.config;

import org.gbif.ecat.cfg.DataDirConfig;
import org.gbif.ecat.cfg.DataDirConfigFactory;
import org.gbif.utils.file.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.gbif.utils.file.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;


/**
 * Portal configuration, mainly via properties, but allowing for custom methods in this class
 */
public class PortalConfig {

  protected static final Logger log = LoggerFactory.getLogger(DataDirConfig.class);
  public static final String SESSION_USER = "user";
  private static final String KEY_BASE_URL = "baseurl";
  private static final String KEY_VERSION = "version";
  private static final String KEY_WS_REG = "ws.registry";
  private static final String KEY_WS_CLB = "ws.species";
  private static final String KEY_WS_OCC = "ws.occurrence";
  protected Properties properties;

  private PortalConfig() {
  }

  public PortalConfig(InputStream configStream) throws IOException {
    // Read properties file.
    this.properties = new Properties();
    this.properties.load(configStream);
  }

  public PortalConfig(Properties properties) {
    this.properties = properties;
  }

  public Properties getProperties() {
    return properties;
  }

  public String getProperty(String property) {
    return properties.getProperty(property);
  }

  public String getBaseUrl() {
    return properties.getProperty(KEY_BASE_URL);
  }

  public String getVersion() {
    return properties.getProperty(KEY_VERSION);
  }

  public String getRegistryWs() {
    return properties.getProperty(KEY_WS_REG);
  }

  public String getSpeciesWs() {
    return properties.getProperty(KEY_WS_CLB);
  }

  public String getOccurrenceWs() {
    return properties.getProperty(KEY_WS_OCC);
  }

  /**
   * Gets integer property as Integer object and catches exceptions for wrongly formated numbers, returning null in
   * those cases.
   *
   * @return the property value as an Integer or NULL if not found or other value than integer
   */
  public Integer getPropertyAsInteger(String property) {
    try {
      return Integer.valueOf(properties.getProperty(property));
    } catch (NumberFormatException e) {
    }
    return null;
  }

  public void setProperty(String property, String value) {
    properties.setProperty(property, value);
  }
}
