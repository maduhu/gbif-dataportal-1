package org.gbif.portal.action;

import org.gbif.checklistbank.model.Agent;
import org.gbif.ecat.cfg.DataDirConfig;
import org.gbif.portal.config.PortalConfig;
import org.gbif.portal.filter.RestfulFilter;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import com.opensymphony.xwork2.Preparable;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 */
public class BaseAction extends ActionSupport implements Action, Preparable, SessionAware, ServletRequestAware {
  protected final Logger log = LoggerFactory.getLogger(getClass());
  public static final String NOT_FOUND = "404";
  public static final String NOT_ALLOWED = "401";
  public static final String LOGIN_REQUIRED = "loginRequired";
  public static final String NOT_IMPLEMENTED = "notImplemented";
  protected Integer id;
  protected UUID uuid;
  protected RestfulFilter.CRUD actionType = RestfulFilter.CRUD.READ;
  private Map<String, String> requestParams;

  @Inject
  protected PortalConfig cfg;

  protected Map<String, Object> session;

  @Override
  public void prepare() throws Exception {
    // extract and set id and actionType params from request without relying on interceptors
    if (requestParams.containsKey("action")) {
      // parse action type
      try {
        actionType = RestfulFilter.CRUD.valueOf(requestParams.get("action"));
      } catch (IllegalArgumentException e) {
        actionType = RestfulFilter.CRUD.READ;
      }
      if (actionType==null){
        actionType = RestfulFilter.CRUD.READ;
      }
      // set ids
      try {
        id = Integer.parseInt(requestParams.get("id"));
      } catch (final NumberFormatException e) {
        try {
          uuid = UUID.fromString(requestParams.get("id"));
        } catch (final IllegalArgumentException e2) {
        }
      }

      // call special prepares in case we need to load existing objects before the execute takes place
      if (actionType == RestfulFilter.CRUD.UPDATE) {
        prepareExisting();
      } else if (actionType != RestfulFilter.CRUD.DELETE){
        // all but deletes require an empty model object to exist for params being set by interceptor
        prepareNew();
      }
    }
  }

  /*
  Override this method to prepare an existing object that the params interceptor will populate.
  Use the id or uuid properties to load object.
   */
  protected void prepareExisting() {
  }

  /*
  Override this method to prepare a new, empty object that the params interceptor will populate.
  */
  protected void prepareNew() {
  }

  @Override
  public String execute() throws Exception {
    return SUCCESS;
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

  @Override
  public void setServletRequest(final HttpServletRequest request) {
    requestParams = new HashMap<String, String>();
    final Enumeration<String> params = request.getParameterNames();
    while (params.hasMoreElements()) {
      final String k = params.nextElement();
      requestParams.put(k, request.getParameter(k));
    }
  }
}
