package org.gbif.portal.action;

import org.gbif.checklistbank.model.Checklist;
import org.gbif.checklistbank.model.NameUsageSimple;
import org.gbif.checklistbank.model.voc.SortOrder;
import org.gbif.checklistbank.service.ChecklistService;
import org.gbif.checklistbank.service.HitsService;
import org.gbif.checklistbank.service.NameUsageService;

import com.google.inject.Inject;

import java.util.List;

public class ChecklistAction extends PageableAction {
  @Inject
  private NameUsageService<NameUsageSimple> usageSimpleService;
  @Inject
  protected ChecklistService checklistService;

  protected Checklist checklist;
  private Integer id;
  private List<NameUsageSimple> roots;

  public ChecklistAction() {
    this.currentMenu = "index";
  }

  @Override
  public String execute() {
    checklist = checklistService.get(id);
    if (checklist == null) {
      return NOT_FOUND;
    }
    roots = usageSimpleService.getRoot(checklist, p, ps, SortOrder.rank);

    return SUCCESS;
  }

  public Integer getId() {
    return id;
  }

  public List<NameUsageSimple> getRoots() {
    return roots;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public Checklist getChecklist() {
    return checklist;
  }

}
