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

import java.util.List;
import java.util.Map;

import org.gbif.portal.harvest.taxonomy.TaxonomyUtils;
import org.gbif.portal.harvest.workflow.activity.taxonomy.ClassificationSynchroniserActivity.DeferredClassification;
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
public class RemoteConceptCleanupActivity extends BaseActivity implements Activity {	
	/**
	 * Context Keys
	 */
	protected String contextKeyTimer;
	protected String contextKeyDataResourceId;
	protected String contextKeyRemoteConceptMap;
	protected String contextKeyAcceptedConceptMap;
	
	/**
	 * The utils
	 */
	protected TaxonomyUtils taxonomyUtils;
	protected GbifLogUtils gbifLogUtils;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		
		Long dataResourceId = (Long) context.get(getContextKeyDataResourceId(), Long.class, false);
		Long timer = (Long) context.get(getContextKeyTimer(), Long.class, false);
		if (timer != null) {
			taxonomyUtils.deleteRemoteConceptsOlderThan(dataResourceId, timer);
		}
		
		Map<String, List<DeferredClassification>> map = (Map<String, List<DeferredClassification>>) context.get(getContextKeyRemoteConceptMap(), Map.class, false);
		if (map != null) {
			for (String key : map.keySet()) {
				List<DeferredClassification> deferred = map.get(key);
				StringBuffer sb = new StringBuffer();
				sb.append(key);
				sb.append(": omitted ");
				if (deferred != null) {
					sb.append(deferred.size());
					sb.append(" ");
				}
				sb.append("taxa because parent taxon not found with this id");
				logger.error(gbifLogUtils.createGbifLogMessage(context, LogEvent.EXTRACT_MISSINGPARENTTAXON, sb.toString()));

			}
		}
		
		/* The following assigns fake ranks to nodes without a rank to help to
		 * fit them properly into sorted classifications
		 */
		taxonomyUtils.updateUnknownRanks(dataResourceId);
		
		return context;
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
	 * @return the contextKeyDataResourceId
	 */
	public String getContextKeyDataResourceId() {
		return contextKeyDataResourceId;
	}

	/**
	 * @param contextKeyDataResourceId the contextKeyDataResourceId to set
	 */
	public void setContextKeyDataResourceId(String contextKeyDataResourceId) {
		this.contextKeyDataResourceId = contextKeyDataResourceId;
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
}