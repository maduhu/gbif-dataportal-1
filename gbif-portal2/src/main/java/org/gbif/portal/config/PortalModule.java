package org.gbif.portal.config;

import org.gbif.portal.client.ChecklistBankClient;
import org.gbif.portal.client.ChecklistBankClientImpl;
import org.gbif.portal.client.RegistryClient;
import org.gbif.portal.client.RegistryClientImpl;
import org.gbif.utils.file.FileUtils;

import java.io.IOException;
import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.apache.ApacheHttpClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PortalModule extends AbstractModule {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Override
  protected void configure() {
    bind(RegistryClient.class).to(RegistryClientImpl.class).in(Scopes.SINGLETON);
    bind(ChecklistBankClient.class).to(ChecklistBankClientImpl.class).in(Scopes.SINGLETON);

  }

  @Provides
  @Singleton
  public PortalConfig providePortalConfig() {
    Properties p = new Properties();
    try {
      log.debug("Loading application.properties");
      p.load(FileUtils.classpathStream("application.properties"));
      for (String k : p.stringPropertyNames()) {
        log.debug(k + " --> " + p.get(k));
      }
    } catch (IOException e) {
      log.error("Cannot load application.properties");
    }
    return new PortalConfig(p);
  }

  @Provides
  @Singleton
  /**
   * provide a reusable httpclient for multiple jersey web resources
   */
  public ApacheHttpClient provideHttpClient() {
    // jersey unfortunately still uses http client 3.1
    ClientConfig cc = new DefaultClientConfig();
    //cc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
    cc.getClasses().add(JacksonJsonProvider.class);
    ApacheHttpClient client = ApacheHttpClient.create(cc);
    return client;
  }

}
