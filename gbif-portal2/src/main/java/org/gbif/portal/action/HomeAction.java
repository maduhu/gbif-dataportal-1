package org.gbif.portal.action;

public class HomeAction extends BaseAction {

  public HomeAction() {
    this.currentMenu = "index";
  }

  @Override
  public String execute() throws Exception {
    super.execute();
    return SUCCESS;
  }

  public String registered() {
    return SUCCESS;
  }

}
