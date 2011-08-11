package org.gbif.portal.action.country;

import org.gbif.portal.action.BaseAction;

public class SearchAction extends BaseAction {
  /** TODO: implement country lookup */
  // search
  private String q;

  @Override
  public String execute() {
    log.debug("Trying country search for q [{}]", q);
    return SUCCESS;
  }

  public String getQ() {
    return q;
  }

  public void setQ(String q) {
    this.q = q;
  }
}
