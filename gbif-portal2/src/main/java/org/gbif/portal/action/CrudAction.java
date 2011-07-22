package org.gbif.portal.action;

import com.opensymphony.xwork2.Preparable;

import org.apache.struts2.interceptor.ServletRequestAware;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class CrudAction extends PageableAction implements Preparable, ServletRequestAware {
  public static final String LIST = "list";
  public static final String READ = SUCCESS;
  public static final String SAVED = "saved";
  public static final String DELETED = "deleted";
  protected String objectType;
  protected Integer id;
  protected boolean add = false;
  protected boolean del = false;
  protected boolean save = false;
  private Map<String, String> requestParams;

  protected CrudAction(final String objectType) {
    super();
    this.objectType = objectType;
  }

  protected boolean delete(final Integer id) {
    return false;
  }

  @Override
  public String execute() {
    if (save) {
      if (save()) {
        if (objectType != null) {
          if (id != null) {
            this.addActionMessage(objectType + " saved");
          } else {
            this.addActionMessage(objectType + " created");
          }
        }
        return SAVED;
      } else {
        return NOT_FOUND;
      }
    } else if (del) {
      if (delete(id)) {
        if (objectType != null) {
          this.addActionMessage(objectType + " deleted");
        }
        return DELETED;
      } else {
        return NOT_FOUND;
      }
    } else if (add) {
      if (read()) {
        return SUCCESS;
      }
    } else {
      if (id == null) {
        if (list()) {
          return LIST;
        }
      } else {
        if (read()) {
          return SUCCESS;
        } else {
          return NOT_FOUND;
        }
      }
    }
    return ERROR;
  }

  protected boolean list() {
    return false;
  }

  @Override
  public void prepare() throws Exception {
    // extract and set id, add, del & save params
    if (requestParams.containsKey("add")) {
      add = true;
      prepareNew();
    } else if (requestParams.containsKey("id")) {
      try {
        id = Integer.parseInt(requestParams.get("id"));
        prepareExisting(id);
      } catch (final NumberFormatException e) {
        // most forms submit an empty id value, so parseInt will throw an NumberFormatException
        prepareNew();
      }
    } else if (requestParams.containsKey("save")) {
      prepareNew();
    }

    if (requestParams.containsKey("del")) {
      del = true;
    }
    if (requestParams.containsKey("save")) {
      save = true;
    }
  }

  protected void prepareExisting(final Integer id) {
  }

  protected void prepareNew() {
  }

  protected boolean read() {
    return false;
  }

  protected boolean save() {
    return false;
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
