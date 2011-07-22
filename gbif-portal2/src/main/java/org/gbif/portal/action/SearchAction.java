package org.gbif.portal.action;

import org.gbif.checklistbank.model.NameUsageSimple;
import org.gbif.checklistbank.model.voc.SearchType;
import org.gbif.checklistbank.model.voc.SortOrder;
import org.gbif.checklistbank.service.NameUsageService;

import com.google.inject.Inject;

import java.util.List;

public class SearchAction extends PageableAction {
  @Inject
  private NameUsageService<NameUsageSimple> usageSimpleService;
  private String q; // search string
  private Integer rkey;
  private List<NameUsageSimple> usages;

  public SearchAction() {
    this.currentMenu = "search";
  }

  @Override
  public String execute() {
    usages = usageSimpleService.search(null, null, q, SearchType.fullname, rkey, null, null, null, p, ps, SortOrder.rank);
    return SUCCESS;
  }

  public String getQ() {
    return q;
  }

  public Integer getRkey() {
    return rkey;
  }

  public List<NameUsageSimple> getUsages() {
    return usages;
  }

  public void setQ(final String q) {
    this.q = q;
  }

  public void setRkey(final Integer rkey) {
    this.rkey = rkey;
  }

}
