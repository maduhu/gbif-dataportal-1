package org.gbif.portal.action;

import org.gbif.checklistbank.model.Agent;
import org.gbif.checklistbank.service.AgentService;
import org.gbif.ecat.cfg.DataDirConfig;
import org.gbif.ecat.jdbc.DataAccessException;

import com.google.inject.Inject;
import com.opensymphony.xwork2.validator.validators.EmailValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountAction extends CrudAction {
  private static Pattern emailPattern = Pattern.compile(EmailValidator.emailAddressPattern, Pattern.CASE_INSENSITIVE);
  private Agent user = new Agent();
  private String password2;
  private boolean alreadyRegistered = false;

  @Inject
  private AgentService agentService;

  public AccountAction() {
    super("User");
    this.currentMenu = "account";
  }

  public String confirm() {
    return NOT_IMPLEMENTED;
  }

  public String getPassword2() {
    return password2;
  }

  @Override
  public Agent getUser() {
    return user;
  }

  public boolean isAlreadyRegistered() {
    return alreadyRegistered;
  }

  @Override
  public void prepare() throws Exception {
    user = getUser();
    if (user != null) {
      id = user.getId();
    }
    super.prepare();
  }

  @Override
  protected void prepareExisting(final Integer id) {
    user = getUser();
  }

  @Override
  protected boolean read() {
    return true;
  }

  public String register() {
    if (user.getEmail() != null) {
      // some strings must not be empty
      if (user.getEmail() == null || user.getEmail().length() == 0) {
        addFieldError("email", "Please provide an email address");
      } else {
        // validate email
        final Matcher matcher = emailPattern.matcher(user.getEmail());
        if (!matcher.matches()) {
          addFieldError("email", "Please provide a real email");
        }
      }
      if (user.getName() == null) {
        addFieldError("name", "Please provide your real name");
      }
      if (user.getPassword() == null || user.getPassword().length() < 4) {
        addFieldError("password", "Please provide a password with at least 4 characters");
      } else {
        if (!user.getPassword().equals(password2)) {
          addFieldError("password2", "Please provide exactly the same password twice");
        }
      }

      if (getFieldErrors().size() == 0) {
        // make sure its unique
        try {
          agentService.insert(user);
        } catch (final DataAccessException e) {
          addFieldError("email", "The email address is already registered");
          alreadyRegistered = true;
          log.debug("DataAccessException when registering new account", e);
          return INPUT;
        }
        return SUCCESS;
      }
    }
    return INPUT;
  }

  public String resendPassword() {
    return NOT_IMPLEMENTED;
  }

  @Override
  protected boolean save() {
    if (user.getId() != null) {
      agentService.update(user);
      // update user in sessin too
      session.put(DataDirConfig.SESSION_USER, user);
    }
    return true;
  }

  public void setPassword2(final String password2) {
    this.password2 = password2;
  }

  public void setUser(final Agent user) {
    this.user = user;
  }

}
