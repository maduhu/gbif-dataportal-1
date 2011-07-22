package org.gbif.portal.guice;

import org.gbif.checklistbank.guice.BasicModule;
import org.gbif.checklistbank.guice.ConfigModule;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.util.Modules;

public class CombinedModule implements Module {

  @Override
  public void configure(final Binder binder) {
    final Module portalMod = Modules.combine(new ConfigModule(), new BasicModule(), new PortalModule());
    binder.install(portalMod);
  }

}
