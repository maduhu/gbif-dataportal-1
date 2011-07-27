package org.gbif.portal.config;

import org.gbif.portal.client.RegistryClient;
import org.gbif.utils.file.FileUtils;

import java.io.IOException;
import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PortalModule extends AbstractModule {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Override
  protected void configure() {
    bind(RegistryClient.class).in(Scopes.SINGLETON);
  }

  @Provides
  @Singleton
  public Properties provideProperties() {
    Properties p = new Properties();
    try {
      log.debug("Loading application.properties");
      p.load(FileUtils.classpathStream("application.properties"));
      for (String k : p.stringPropertyNames()){
        log.debug(k + " --> " + p.get(k));
      }
    } catch (IOException e) {
      log.error("Cannot load application.properties");
    }
    return p;
  }

  @Provides
  @Inject
  @Singleton
  public PortalConfig providePortalConfig(Properties properties) {
    return new PortalConfig(properties);
  }
}
