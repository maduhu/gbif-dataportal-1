package org.gbif.portal.action.species;

import org.gbif.portal.action.BaseAction;

public class ExtendedTaxonomyAction extends BaseAction {

  private Integer id;

  @Override
  public String execute() {
    log.debug("Loading extended taxonomy for species id [{}]", id);
    return SUCCESS;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }
}
