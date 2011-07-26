package org.gbif.portal.action;

/**
 * simple base action extension that provides consistent support for paging of results.
 * TODO: discuss how we implement result paging in general, maybe even clientside?
 */
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
