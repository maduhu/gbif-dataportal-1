package org.gbif.portal.action.species;

import org.gbif.portal.action.BaseAction;

public class NameUsageAction extends BaseAction {

  private Integer id;

  @Override
  public String execute() {
    return nameUsage();
  }

  public String nameUsage() {
    log.debug("Loading name usage for species id [{}]", id);
    return SUCCESS;
  }

  public String nameUsageRaw() {
    log.debug("Loading raw name usage for species id [{}]", id);
    return SUCCESS;
  }

  public String nameUsageList() {
    log.debug("Loading name usage list for species id [{}]", id);
    return SUCCESS;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }
}
