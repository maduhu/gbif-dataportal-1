package org.gbif.portal.action;

import org.gbif.checklistbank.Constants;
import org.gbif.checklistbank.model.Checklist;
import org.gbif.checklistbank.model.Citation;
import org.gbif.checklistbank.model.ClbClassification;
import org.gbif.checklistbank.model.ClbClassificationVerbatim;
import org.gbif.checklistbank.model.ClbStatistics;
import org.gbif.checklistbank.model.Description;
import org.gbif.checklistbank.model.Distribution;
import org.gbif.checklistbank.model.Identifier;
import org.gbif.checklistbank.model.Image;
import org.gbif.checklistbank.model.NameUsage;
import org.gbif.checklistbank.model.NameUsageFull;
import org.gbif.checklistbank.model.NameUsageSimple;
import org.gbif.checklistbank.model.SpeciesProfile;
import org.gbif.checklistbank.model.VernacularNameFull;
import org.gbif.checklistbank.model.comp.DistributionOrder;
import org.gbif.checklistbank.model.voc.ChecklistType;
import org.gbif.checklistbank.model.voc.SortOrder;
import org.gbif.checklistbank.nub.SpeciesProfileService;
import org.gbif.checklistbank.service.ChecklistService;
import org.gbif.checklistbank.service.CitationService;
import org.gbif.checklistbank.service.ClassificationService;
import org.gbif.checklistbank.service.ClbStatisticsService;
import org.gbif.checklistbank.service.DescriptionService;
import org.gbif.checklistbank.service.DistributionService;
import org.gbif.checklistbank.service.IdentifierService;
import org.gbif.checklistbank.service.ImageService;
import org.gbif.checklistbank.service.NameUsageService;
import org.gbif.checklistbank.service.ParsedNameService;
import org.gbif.checklistbank.service.VernacularService;
import org.gbif.checklistbank.utils.SqlStatement;
import org.gbif.ecat.model.ParsedName;
import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.gbif.utils.collection.MapUtils;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UsageAction extends PageableAction {
  @Inject
  private NameUsageService<NameUsageFull> usageFullService;
  @Inject
  private NameUsageService<NameUsage> usageService;
  @Inject
  private NameUsageService<NameUsageSimple> usageSimpleService;
  @Inject
  private ClassificationService classificationService;
  @Inject
  private ParsedNameService pnameService;
  @Inject
  private VernacularService<VernacularNameFull> vernacularService;
  @Inject
  private ImageService imageService;
  @Inject
  private IdentifierService identifierService;
  @Inject
  private DistributionService distributionService;
  @Inject
  private DescriptionService descriptionService;
  @Inject
  private SpeciesProfileService speciesProfileService;
  @Inject
  private CitationService citationService;
  @Inject
  private ClbStatisticsService statsService;
  @Inject
  protected ChecklistService checklistService;

  // parameter
  private Integer id;
  // result
  protected Checklist checklist;
  private NameUsageFull usage;
  private Collection<SpeciesProfile> speciesProfiles;
  private ClbClassificationVerbatim classification;
  private ParsedName<Integer> pname;
  private List<NameUsageSimple> children;
  private List<NameUsageSimple> synonyms;
  private List<NameUsageSimple> irregularSynonyms;
  private List<VernacularNameFull> vernaculars;
  private Collection<Image> images;
  private Collection<Identifier> identifier;
  private List<Description> descriptions;
  private List<Distribution> distribution;
  private List<Citation> bibliography;
  private Integer nubUsageId;
  private final Map<ChecklistType, Map<Integer, String>> lexicals = new TreeMap<ChecklistType, Map<Integer, String>>();
  private final int initialSynonyms = 25;
  private Rank rank;

  public UsageAction() {
    this.currentMenu = "index";
  }

  public List<Description> getDescriptions() {
    return descriptions;
  }

  @Override
  public String execute() throws Exception {
    SqlStatement.DEBUG = true;
    if (id != null) {
      usage = usageFullService.get(id);
    }
    if (usage == null) {
      return NOT_FOUND;
    }
    super.execute();
    checklist = checklistService.get(usage.getChecklistId());
    pname = pnameService.get(usage.getNameStringId());

    classification = classificationService.getCanonicalClbVerbatimClassification(id);
    if (rank == null) {
      // get children
      children = usageSimpleService.getChildren(id, p, ps, SortOrder.rank);
    } else {
      // get descendants for a given rank
      children = usageSimpleService.getDescendantsByRank(id, rank, p, ps, SortOrder.alpha);
    }
    synonyms = usageSimpleService.getSynonyms(id, null, null, SortOrder.alpha, null);
    identifier = identifierService.getByUsage(id);
    if (Constants.NUB_CHECKLIST_ID.equals(usage.getChecklistId())) {
      images = imageService.getByNubId(usage.getId());
      // use new set instance to make names & distribution unique
      vernaculars = new ArrayList(new HashSet<VernacularNameFull>(vernacularService.getByNubId(usage.getId())));
      distribution = new ArrayList<Distribution>(new HashSet<Distribution>(distributionService.getByNubId(usage.getNubId())));
      descriptions = new ArrayList<Description>(descriptionService.getByNubId(usage.getNubId()));
      speciesProfiles = new ArrayList<SpeciesProfile>(speciesProfileService.getByNubId(usage.getNubId()));
      irregularSynonyms = usageSimpleService.getSynonyms(id, null, initialSynonyms, SortOrder.rank, null, TaxonomicStatus.IntermediateRankSynonym,
          TaxonomicStatus.DeterminationSynonym);
      bibliography = new ArrayList<Citation>(citationService.getByNubId(usage.getId()));
    } else {
      images = imageService.getByUsage(id);
      vernaculars = new ArrayList(vernacularService.getByUsage(id));
      distribution = new ArrayList<Distribution>(distributionService.getByUsage(id));
      descriptions = new ArrayList<Description>(descriptionService.getByUsage(id));
      speciesProfiles = new ArrayList<SpeciesProfile>(speciesProfileService.getByUsage(id));
      irregularSynonyms = new ArrayList();
      bibliography = new ArrayList<Citation>(citationService.getByUsage(usage.getId()));
    }
    Collections.sort(distribution, new DistributionOrder());
    Collections.sort(vernaculars);

    final ClbStatistics stats = statsService.getStatistics();
    final List<NameUsage> similarUsages = usageService.listByNubId(usage.getNubId());
    for (final NameUsage u : similarUsages) {
      // ignore self
      if (u.getId().equals(usage.getId())) {
        continue;
      }
      if (Constants.NUB_CHECKLIST_ID.equals(u.getChecklistId())) {
        nubUsageId = u.getId();
      } else {
        // keep seperate maps for each checklist type
        final ChecklistType ct = stats.getChecklistType(u.getChecklistId());
        if (!lexicals.containsKey(ct)) {
          lexicals.put(ct, new HashMap<Integer, String>());
        }
        // only remember resource name and usage id
        lexicals.get(ct).put(u.getId(), stats.getChecklistTitle(u.getChecklistId()));
      }
    }
    // sort alphabetically within each type map
    for (final ChecklistType ct : lexicals.keySet()) {
      lexicals.put(ct, MapUtils.sortByValue(lexicals.get(ct)));
    }

    return SUCCESS;
  }

  public List<Citation> getBibliography() {
    return bibliography;
  }

  public List<NameUsageSimple> getChildren() {
    return children;
  }

  public ClbClassificationVerbatim getClassification() {
    return classification;
  }

  public Collection<Rank> getClbRanks() {
    return ClbClassification.CLB_RANKS;
  }

  public List<Distribution> getDistribution() {
    return distribution;
  }

  public Integer getId() {
    return this.id;
  }

  public Collection<Identifier> getIdentifier() {
    return identifier;
  }

  public Collection<Image> getImages() {
    return images;
  }

  public int getInitialSynonyms() {
    return initialSynonyms;
  }

  public List<NameUsageSimple> getIrregularSynonyms() {
    return irregularSynonyms;
  }

  public Map<ChecklistType, Map<Integer, String>> getLexicals() {
    return lexicals;
  }

  public Integer getNubUsageId() {
    return nubUsageId;
  }

  public ParsedName<Integer> getPname() {
    return pname;
  }

  public Rank getRank() {
    return rank;
  }

  public Collection<SpeciesProfile> getSpeciesProfiles() {
    return speciesProfiles;
  }

  public List<NameUsageSimple> getSynonyms() {
    return synonyms;
  }

  public NameUsageFull getUsage() {
    return usage;
  }

  public Collection<VernacularNameFull> getVernaculars() {
    return vernaculars;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public void setRank(final Integer rankTermID) {
    this.rank = Rank.valueOfTermID(rankTermID);
  }

}
