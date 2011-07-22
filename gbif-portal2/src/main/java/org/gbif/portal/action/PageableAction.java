package org.gbif.portal.action;

public class PageableAction extends BaseAction {
  protected Integer p = 1;
  protected Integer ps = 50;

  public Integer getP() {
    return p;
  }

  public void setP(final int p) {
    this.p = p;
  }

  public Integer getPs() {
    return ps;
  }

  public void setPs(final int ps) {
    this.ps = ps;
  }
}
