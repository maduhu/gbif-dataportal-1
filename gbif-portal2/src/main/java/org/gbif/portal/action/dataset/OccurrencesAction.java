package org.gbif.portal.action.dataset;

import org.gbif.portal.action.BaseAction;

public class OccurrencesAction extends BaseAction {

  // detail
  private Integer id;

  @Override
  public String execute() {
    log.debug("Getting occurrences for dataset id [{}]", id);
    return SUCCESS;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }
}
