package org.gbif.portal.action;

import org.gbif.ecat.cfg.DataDirConfig;
import org.gbif.portal.config.PortalConfig;

import com.google.inject.Inject;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 *
 */
public abstract class BaseAction extends ActionSupport implements Action, SessionAware {
  protected final Logger log = LoggerFactory.getLogger(getClass());
  public static final String NOT_FOUND = "404";
  public static final String NOT_ALLOWED = "401";
  public static final String LOGIN_REQUIRED = "loginRequired";
  public static final String NOT_IMPLEMENTED = "notImplemented";

  @Inject
  protected PortalConfig cfg;
  protected Map<String, Object> session;

  @Override
  public String execute(){
    log.debug("executing action class: "+this.getClass().getName());
    return SUCCESS;
  }
  public Object getUser() {
    Object u = null;
    try {
      u = session.get(DataDirConfig.SESSION_USER);
    } catch (final Exception e) {
      // swallow. if session is not yet opened we get an exception here...
    }
    return u;
  }

  public boolean isLoggedIn() {
    return getUser() == null ? false : true;
  }

  /*
  Translates a map with value entries being resource keys into the current language values using the actions text provider.
  Useful for translating maps that serve to populate select boxes with keys being the form value.
   */
  protected Map<String, String> translateI18nMap(final Map<String, String> map) {
    for (final String key : map.keySet()) {
      final String i18Key = map.get(key);
      map.put(key, getText(i18Key));
    }
    return map;
  }

  @Override
  public void setSession(final Map<String, Object> session) {
    this.session = session;
  }

  public PortalConfig getCfg() {
    return cfg;
  }

  public String getBase() {
    return cfg.getBaseUrl();
  }

}
