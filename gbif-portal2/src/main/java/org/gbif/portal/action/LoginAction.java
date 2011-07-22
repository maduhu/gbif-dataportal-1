package org.gbif.portal.action;

import org.gbif.checklistbank.model.Agent;
import org.gbif.checklistbank.service.AgentService;
import org.gbif.ecat.cfg.DataDirConfig;

import com.google.inject.Inject;

import org.apache.struts2.interceptor.ServletRequestAware;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

public class LoginAction extends BaseAction implements ServletRequestAware {
  @Inject
  private AgentService agentService;

  private String redirectUrl;
  private String email;
  private String password;

  private HttpServletRequest request;

  @Override
  public String execute() {
    currentMenu = "login";
    final Agent user = agentService.authorise(email, password);
    if (user != null) {
      log.info("User " + email + " logged in successfully");
      user.setLastLogin(new Date());
      agentService.update(user);
      session.put(DataDirConfig.SESSION_USER, user);
      // remember previous URL to redirect back to
      setRedirectUrl();
      return SUCCESS;
    } else {
      addFieldError("email", "The email - password combination does not exists");
      log.info("User " + email + " failed to log in with password " + password);
    }
    return INPUT;
  }

  private void setRedirectUrl() {
    redirectUrl = cfg.domain() + "/index/";
    if (request != null) {
      final String referer = request.getHeader("Referer");
      if (referer != null && referer.startsWith(cfg.domain()) && !(referer.endsWith("login") || referer.endsWith("register/save"))) {
        redirectUrl = referer;
      }
    }
    log.info("Redirecting to " + redirectUrl);
  }

  public String logout() {
    setRedirectUrl();
    session.clear();
    return SUCCESS;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(final String email) {
    if (email != null && !email.equalsIgnoreCase("email")) {
      this.email = email;
    }
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  @Override
  public void setServletRequest(final HttpServletRequest request) {
    this.request = request;
  }

  public String getRedirectUrl() {
    return redirectUrl;
  }

}
