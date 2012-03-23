/***************************************************************************
 * Copyright (C) 2005 Global Biodiversity Information Facility Secretariat.
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ***************************************************************************/
package org.gbif.portal.harvest.workflow.activity.taxonomy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.harvest.taxonomy.TaxonomyUtils;
import org.gbif.portal.model.RemoteConcept;
import org.gbif.portal.model.TaxonConceptLite;
import org.gbif.portal.model.TaxonName;
import org.gbif.portal.util.log.GbifLogMessage;
import org.gbif.portal.util.log.GbifLogUtils;
import org.gbif.portal.util.log.LogEvent;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * Classification Synchroniser is a lightweight wrapper for a 
 * @see org.gbif.portal.harvest.taxonomy.TaxonomyUtils
 * @author trobertson
 */
public class ClassificationSynchroniserActivity extends BaseActivity implements Activity {	
	/**
	 * Context Keys
	 */
	protected String contextKeyClassificationList;
	protected String contextKeyDataResourceId;
	protected String contextKeyDataProviderId;
	protected String contextKeyTaxonConceptId;
	protected String contextKeyTaxonNameId;
	protected String contextKeyCommonName;
	protected String contextKeyLanguage;
	protected String contextKeyRemoteId;
	protected String contextKeyRemoteParentId;
	protected String contextKeyRemoteAcceptedId;
	protected String contextKeyRemoteConceptMap;
	protected String contextKeyAcceptedConceptMap;
	protected String contextKeyTimer;
	protected String contextKeyTaxonomicPriority;
	
	
	class DeferredClassification {
		protected List<TaxonName> classification;
		protected String remoteId;
		protected String acceptedId;
		/**
		 * @return the classification
		 */
		public List<TaxonName> getClassification() {
			return classification;
		}
		/**
		 * @param classification the classification to set
		 */
		public void setClassification(List<TaxonName> classification) {
			this.classification = classification;
		}
		/**
		 * @return the remoteId
		 */
		public String getRemoteId() {
			return remoteId;
		}
		/**
		 * @param remoteId the remoteId to set
		 */
		public void setRemoteId(String remoteId) {
			this.remoteId = remoteId;
		}
		/**
		 * @return the acceptedId
		 */
		public String getAcceptedId() {
			return acceptedId;
		}
		/**
		 * @param acceptedId the acceptedId to set
		 */
		public void setAcceptedId(String acceptedId) {
			this.acceptedId = acceptedId;
		}
	}
	
    /**
	 * The utils
	 */
	protected TaxonomyUtils taxonomyUtils;
	protected GbifLogUtils gbifLogUtils;
	
	/**
	 * Control flags defaults to false
	 */
	protected boolean removeUnwantedNames = false;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {

		Map<String, List<DeferredClassification>> taxonMap = (Map<String, List<DeferredClassification>>) context.get(getContextKeyRemoteConceptMap(), Map.class, false);
		Map<String, List<Object>> acceptedMap = (Map<String, List<Object>>) context.get(getContextKeyAcceptedConceptMap(), Map.class, false);
		
		long dataProviderId = (Long) context.get(getContextKeyDataProviderId(), Long.class, true);
		long dataResourceId = (Long) context.get(getContextKeyDataResourceId(), Long.class, true);
		List<TaxonName> classification = (List<TaxonName>) context.get(getContextKeyClassificationList(), List.class, false);
		
		if (classification == null || classification.size()==0) {
			logger.warn("No classification to synchronise");
			return context;
		}
		
		int taxonomicPriority = 100;
		if (getContextKeyTaxonomicPriority() != null) {
			Integer priority = (Integer) context.get(getContextKeyTaxonomicPriority(), Integer.class, false);
			if (priority != null) {
				taxonomicPriority = priority.intValue();
			}
		}
		
		boolean deferStorage = false;
		String remoteId = StringUtils.trimToNull((String) context.get(getContextKeyRemoteId(), String.class, false));
		String remoteParentId = StringUtils.trimToNull((String) context.get(getContextKeyRemoteParentId(), String.class, false));
		String remoteAcceptedId = StringUtils.trimToNull((String) context.get(getContextKeyRemoteParentId(), String.class, false));
		
		if (remoteParentId != null && !StringUtils.equals(remoteParentId, remoteId)) {
			List<RemoteConcept> conceptsForParentId = taxonomyUtils.findRemoteConceptsByRemoteIdAndIdTypeAndDataResourceId(remoteParentId, 0, dataResourceId);
			
			if (conceptsForParentId.size() == 0) {
				deferStorage = true;
			} else {
				RemoteConcept parentRemoteConcept = conceptsForParentId.get(0);
				// Check that we have seen and touched this concept in this workflow
				Long timer = (Long) context.get(getContextKeyTimer(), Long.class, false);
				if (timer != null && timer > parentRemoteConcept.getModified().getTime()) {
					deferStorage = true;
				} else {
					addAncestorsToClassification(classification, parentRemoteConcept.getTaxonConceptId());
				}
			}
			
			if (deferStorage) {
				// The values in this taxonMap are lists of Classifications still to be stored 
				// but dependent on the RemoteConcept identified by the key
				if (taxonMap == null) {
					taxonMap = new HashMap<String,List<DeferredClassification>>();
					context.put(getContextKeyRemoteConceptMap(), taxonMap);
				}
				List<DeferredClassification> children = taxonMap.get(remoteParentId);
				if (children == null) {
					children = new ArrayList<DeferredClassification>();
					taxonMap.put(remoteParentId, children);
				}
				DeferredClassification dc = new DeferredClassification();
				dc.setClassification(classification);
				dc.setRemoteId(remoteId);
				dc.setAcceptedId(remoteAcceptedId);
				children.add(dc);
				
				logger.info("Awaiting " + taxonMap.size() + " nodes");
			} 
		}
		
		TaxonConceptLite tc = null;
		
		if (!deferStorage) {
			tc = synchronise(context, classification, dataProviderId, dataResourceId, taxonMap, acceptedMap, remoteId, remoteParentId, remoteAcceptedId, taxonomicPriority);
			
			if (tc == null) {
				logger.warn("Stopping process as there is no classification found");
				context.setStopProcess(true);
			}
		}
		
		String common = (String) context.get(getContextKeyCommonName(), String.class, false);
		if (common != null) {
//			CommonName commonName = commonNameDAO.getUnique(); 
			
		}
		
		return context;
	}

	/**
	 * Add the names from parent taxon
	 * @param classification
	 * @param taxonConceptId
	 */
	private void addAncestorsToClassification(List<TaxonName> classification, long taxonConceptId) {
		List<TaxonConceptLite> ancestors = taxonomyUtils.getClassificationConcepts(taxonConceptId);
		if (ancestors != null) {
			for (TaxonConceptLite ancestor : ancestors) {
				TaxonName name = ancestor.getTaxonName();
				if (name.getRank() > 0) {
					classification.add(name);
				}
			}
		}
	}

	/**
	 * @param context
	 * @param classification
	 * @param dataProviderId
	 * @param dataResourceId
	 * @param taxonMap
	 * @param remoteId
	 * @param remoteParentId
	 * @param taxonomicPriority
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private TaxonConceptLite synchronise(ProcessContext context, 
										 List<TaxonName> classification, 
										 long dataProviderId, 
										 long dataResourceId, 
										 Map<String,List<DeferredClassification>> taxonMap, 
										 Map<String,List<Object>> acceptedMap, 
										 String remoteId, 
										 String remoteParentId, 
										 String acceptedId, 
										 int taxonomicPriority) {
		 
		// This is ugly.  To handle unranked taxa, we rely on the fact that this method always assigns them some kind of pseudo-rank.
		// If the classification starts with a name with rank 0, we determine this pseudo-rank and either switch the name for one
		// with the same name and the correct pseudo-rank or else we change the rank on this one from 0 to the pseudo-rank.
		
		if (classification.size() > 0) {
			TaxonName first = classification.get(0);
			if (first.getRank() == 0) {
				Integer pseudorank = 1;
				for (int i = 1; i < classification.size(); i++) {
					Integer rank = classification.get(i).getRank();
					if (rank >= pseudorank) {
						pseudorank = rank + 1;
					}
				}
				
				if (pseudorank == 6001 || pseudorank == 7001 || pseudorank == 8001) {
					//step over nothoranks
					pseudorank += 1;
				}
				
				first.setRank(pseudorank);
			}
		}
		
		if (removeUnwantedNames) {
			classification = taxonomyUtils.removeUnwantedNames(classification);
		}
		
		TaxonConceptLite tc = taxonomyUtils.synchroniseNames(classification, dataProviderId, dataResourceId, taxonomicPriority);

		if (tc != null) {
			context.put(getContextKeyTaxonConceptId(), tc.getId());
			context.put(getContextKeyTaxonNameId(), tc.getTaxonName().getId());

			if (remoteId != null) {
				List<RemoteConcept> conceptsForId = taxonomyUtils.findRemoteConceptsByRemoteIdAndIdTypeAndDataResourceId(remoteId, 1, dataResourceId);
				for (RemoteConcept existingConcept : conceptsForId) {
					if (    existingConcept.getIdType() == 1
						 && existingConcept.getTaxonConceptId() != tc.getId()) {
						GbifLogMessage message 
							= gbifLogUtils.createGbifLogMessage(context, LogEvent.EXTRACT_DUPLICATEREMOTECONCEPTID,
									remoteId + ": multiple records with this id");
						message.setTaxonConceptId(tc.getId());
						logger.error(message);
					}
				}
	
				taxonomyUtils.synchroniseRemoteConcepts(tc, remoteId);
	
				if (taxonMap != null) {
					// See if there are entries awaiting this remoteId for their parent
					List<DeferredClassification> children = taxonMap.remove(remoteId);
					if (children != null) {
						for (DeferredClassification childClassification : children) {
							addAncestorsToClassification(childClassification.getClassification(), tc.getId());
							synchronise(context, childClassification.getClassification(), dataProviderId, dataResourceId, taxonMap, acceptedMap, childClassification.getRemoteId(), remoteId, childClassification.getAcceptedId(), taxonomicPriority);
						}
					}
				}
			}

			// TODO - Just a little test here
			// This would find the nub classification for the taxonConcept
			// there is a test launcher that updates them, and until we have confidence, there is no
			// need to do this yet....
			// classificationSynchroniser.getNubTaxonConceptForTaxonConcept(204, classification);
		}
		
		return tc;
	}

	/**
	 * @return Returns the contextKeyClassificationList.
	 */
	public String getContextKeyClassificationList() {
		return contextKeyClassificationList;
	}

	/**
	 * @param contextKeyClassificationList The contextKeyClassificationList to set.
	 */
	public void setContextKeyClassificationList(String contextKeyClassificationList) {
		this.contextKeyClassificationList = contextKeyClassificationList;
	}

	/**
	 * @return Returns the contextKeyDataResourceId.
	 */
	public String getContextKeyDataResourceId() {
		return contextKeyDataResourceId;
	}

	/**
	 * @param contextKeyDataResourceId The contextKeyDataResourceId to set.
	 */
	public void setContextKeyDataResourceId(String contextKeyDataResourceId) {
		this.contextKeyDataResourceId = contextKeyDataResourceId;
	}

	/**
	 * @return Returns the contextKeyDataProviderId.
	 */
	public String getContextKeyDataProviderId() {
		return contextKeyDataProviderId;
	}

	/**
	 * @param contextKeyDataProviderId The contextKeyDataProviderId to set.
	 */
	public void setContextKeyDataProviderId(String contextKeyDataProviderId) {
		this.contextKeyDataProviderId = contextKeyDataProviderId;
	}

	/**
	 * @return the contextKeyTaxonConceptId
	 */
	public String getContextKeyTaxonConceptId() {
		return contextKeyTaxonConceptId;
	}

	/**
	 * @param contextKeyTaxonConceptId the contextKeyTaxonConceptId to set
	 */
	public void setContextKeyTaxonConceptId(String contextKeyTaxonConceptId) {
		this.contextKeyTaxonConceptId = contextKeyTaxonConceptId;
	}

	/**
	 * @return the contextKeyTaxonNameId
	 */
	public String getContextKeyTaxonNameId() {
		return contextKeyTaxonNameId;
	}

	/**
	 * @param contextKeyTaxonNameId the contextKeyTaxonNameId to set
	 */
	public void setContextKeyTaxonNameId(String contextKeyTaxonNameId) {
		this.contextKeyTaxonNameId = contextKeyTaxonNameId;
	}

	/**
	 * @return the contextKeyRemoteId
	 */
	public String getContextKeyRemoteId() {
		return contextKeyRemoteId;
	}

	/**
	 * @param contextKeyRemoteId the contextKeyRemoteId to set
	 */
	public void setContextKeyRemoteId(String contextKeyRemoteId) {
		this.contextKeyRemoteId = contextKeyRemoteId;
	}

	/**
	 * @return the contextKeyRemoteParentId
	 */
	public String getContextKeyRemoteParentId() {
		return contextKeyRemoteParentId;
	}

	/**
	 * @param contextKeyRemoteParentId the contextKeyRemoteParentId to set
	 */
	public void setContextKeyRemoteParentId(
			String contextKeyRemoteParentId) {
		this.contextKeyRemoteParentId = contextKeyRemoteParentId;
	}

	/**
	 * @return the contextKeyRemoteAcceptedId
	 */
	public String getContextKeyRemoteAcceptedId() {
		return contextKeyRemoteAcceptedId;
	}

	/**
	 * @param contextKeyRemoteAcceptedId the contextKeyRemoteAcceptedId to set
	 */
	public void setContextKeyRemoteAcceptedId(String contextKeyRemoteAcceptedId) {
		this.contextKeyRemoteAcceptedId = contextKeyRemoteAcceptedId;
	}

	/**
	 * @return the contextKeyRemoteConceptMap
	 */
	public String getContextKeyRemoteConceptMap() {
		return contextKeyRemoteConceptMap;
	}

	/**
	 * @param contextKeyRemoteConceptMap the contextKeyRemoteConceptMap to set
	 */
	public void setContextKeyRemoteConceptMap(String contextKeyRemoteConceptMap) {
		this.contextKeyRemoteConceptMap = contextKeyRemoteConceptMap;
	}

	/**
	 * @return the contextKeyAcceptedConceptMap
	 */
	public String getContextKeyAcceptedConceptMap() {
		return contextKeyAcceptedConceptMap;
	}

	/**
	 * @param contextKeyAcceptedConceptMap the contextKeyAcceptedConceptMap to set
	 */
	public void setContextKeyAcceptedConceptMap(String contextKeyAcceptedConceptMap) {
		this.contextKeyAcceptedConceptMap = contextKeyAcceptedConceptMap;
	}

	/**
	 * @return the contextKeyCommonName
	 */
	public String getContextKeyCommonName() {
		return contextKeyCommonName;
	}

	/**
	 * @param contextKeyCommonName the contextKeyCommonName to set
	 */
	public void setContextKeyCommonName(String contextKeyCommonName) {
		this.contextKeyCommonName = contextKeyCommonName;
	}

	/**
	 * @return the contextKeyLanguage
	 */
	public String getContextKeyLanguage() {
		return contextKeyLanguage;
	}

	/**
	 * @param contextKeyLanguage the contextKeyLanguage to set
	 */
	public void setContextKeyLanguage(String contextKeyLanguage) {
		this.contextKeyLanguage = contextKeyLanguage;
	}

	/**
	 * @return Returns the taxonomyUtils.
	 */
	public TaxonomyUtils getTaxonomyUtils() {
		return taxonomyUtils;
	}

	/**
	 * @param taxonomyUtils The taxonomyUtils to set.
	 */
	public void setTaxonomyUtils(TaxonomyUtils taxonomyUtils) {
		this.taxonomyUtils = taxonomyUtils;
	}

	/**
	 * @return the gbifLogUtils
	 */
	public GbifLogUtils getGbifLogUtils() {
		return gbifLogUtils;
	}

	/**
	 * @param gbifLogUtils the gbifLogUtils to set
	 */
	public void setGbifLogUtils(GbifLogUtils gbifLogUtils) {
		this.gbifLogUtils = gbifLogUtils;
	}

	/**
	 * @return the contextKeyTimer
	 */
	public String getContextKeyTimer() {
		return contextKeyTimer;
	}

	/**
	 * @param contextKeyTimer the contextKeyTimer to set
	 */
	public void setContextKeyTimer(String contextKeyTimer) {
		this.contextKeyTimer = contextKeyTimer;
	}

	/**
	 * @return the contextKeyTaxonomicPriority
	 */
	public String getContextKeyTaxonomicPriority() {
		return contextKeyTaxonomicPriority;
	}

	/**
	 * @param contextKeyTaxonomicPriority the contextKeyTaxonomicPriority to set
	 */
	public void setContextKeyTaxonomicPriority(String contextKeyTaxonomicPriority) {
		this.contextKeyTaxonomicPriority = contextKeyTaxonomicPriority;
	}

	/**
	 * @return Returns the removeUnwantedNames.
	 */
	public boolean isRemoveUnwantedNames() {
		return removeUnwantedNames;
	}

	/**
	 * @param removeUnwantedNames The removeUnwantedNames to set.
	 */
	public void setRemoveUnwantedNames(boolean removeUnwantedNames) {
		this.removeUnwantedNames = removeUnwantedNames;
	}
}