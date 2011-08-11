package org.gbif.portal.action.species;

import org.gbif.portal.action.BaseAction;

public class SynonymsAction extends BaseAction {

  private Integer id;

  @Override
  public String execute() {
    return SUCCESS;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }
}
