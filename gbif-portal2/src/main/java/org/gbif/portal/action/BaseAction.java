package org.gbif.portal.action;

import org.gbif.checklistbank.Constants;
import org.gbif.checklistbank.model.Agent;
import org.gbif.ecat.cfg.DataDirConfig;

import com.google.inject.Inject;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class BaseAction extends ActionSupport implements Action, SessionAware {
  protected final Logger log = LoggerFactory.getLogger(getClass());
  public static final String NOT_FOUND = "404";
  public static final String NOT_ALLOWED = "401";
  public static final String LOGIN_REQUIRED = "loginRequired";
  public static final String NOT_IMPLEMENTED = "notImplemented";
  @Inject
  protected DataDirConfig cfg;

  protected Map<String, Object> session;
  protected String currentMenu = "index";

  @Override
  public String execute() throws Exception {
    return SUCCESS;
  }

  public String getDomain() {
    return cfg.domain();
  }

  public String getCurrentMenu() {
    // is set via action constructor
    return currentMenu;
  }

  public Agent getUser() {
    Agent u = null;
    try {
      u = (Agent) session.get(DataDirConfig.SESSION_USER);
    } catch (final Exception e) {
      // swallow. if session is not yet opened we get an exception here...
    }
    return u;
  }

  public boolean isLoggedIn() {
    return getUser() == null ? false : true;
  }

  protected List<String> splitMultiValueParameter(final String value) {
    if (value == null) {
      return new ArrayList<String>();
    }
    final String[] paras = StringUtils.split(value, ", ");
    return Arrays.asList(paras);
  }

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

  public DataDirConfig getCfg() {
    return cfg;
  }

}
