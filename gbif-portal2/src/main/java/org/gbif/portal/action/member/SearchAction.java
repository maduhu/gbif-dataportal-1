package org.gbif.portal.action.member;

import org.gbif.portal.action.BaseAction;

public class SearchAction extends BaseAction {

  /** TODO: implement member lookup */
  // search
  private String q;

  @Override
  public String execute() {
    log.debug("Trying member search for q [{}]", q);
    return SUCCESS;
  }

  public String getQ() {
    return q;
  }

  public void setQ(String q) {
    this.q = q;
  }
}
