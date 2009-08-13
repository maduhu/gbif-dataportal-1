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
package org.gbif.portal.harvest.workflow.activity.occurrence;

import java.util.Map;

import org.gbif.portal.dao.RawOccurrenceRecordDAO;
import org.gbif.portal.harvest.taxonomy.TaxonomyUtils;
import org.gbif.portal.model.LinnaeanRankClassification;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * An activity that will look up the TC and TN ids for a classification and put them in the context
 * @author trobertson
 */
public class StoredClassificationToTaxonConceptIdActivity extends BaseActivity implements
		Activity {
	/**
	 * The dao 
	 */
	protected RawOccurrenceRecordDAO rawOccurrenceRecordDAO;
	
	/**
	 * Context Keys
	 */
	protected String contextKeyKingdom = "kingdom";
	protected String contextKeyPhylum = "phylum";
	protected String contextKeyClass = "class";
	protected String contextKeyOrder = "order";
	protected String contextKeyFamily = "family";
	protected String contextKeyGenus = "genus";
	protected String contextKeyScientificName = "scientificName";
	protected String contextKeyStoredClassifications = "storedClassifications";
	protected String contextKeyTaxonConceptId = "taxonConceptId";
	protected String contextKeyTaxonNameId = "taxonNameId";
	
	protected TaxonomyUtils taxonomyUtils;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		
		LinnaeanRankClassification classification = new LinnaeanRankClassification(
				(String) context.get(contextKeyKingdom, String.class, false),
				(String) context.get(contextKeyPhylum, String.class, false),
				(String) context.get(contextKeyClass, String.class, false),
				(String) context.get(contextKeyOrder, String.class, false),
				(String) context.get(contextKeyFamily, String.class, false),
				(String) context.get(contextKeyGenus, String.class, false),
				(String) context.get(contextKeyScientificName, String.class, false));
		
		// need to pull out any of the values that are ignored - e.g. "Unknown"
		// This cannot be done - it was keyed on the RAW values!!!
		//taxonomyUtils.removeUnwantedNames(classification);
		
		Map<LinnaeanRankClassification, Long[]> classificationMap = (Map<LinnaeanRankClassification, Long[]>) context.get(contextKeyStoredClassifications, Map.class, false);
		if (classificationMap != null) {
			Long[] tcTnId = classificationMap.get(classification);
			if (tcTnId != null && tcTnId[0]>0) {
				logger.debug("TaxonConceptId [" + tcTnId[0] + "] TaxonNameId[" + tcTnId[1] +"] found for classification: " + classification);
				context.put(contextKeyTaxonConceptId, tcTnId[0]);
				context.put(contextKeyTaxonNameId, tcTnId[1]);
			} else {
				logger.warn("No TaxonConceptId found for classification: " + classification);
			}
		} else {
			logger.warn("No stored classification map found in the context");
		}
		return context;
	}

	/**
	 * @return Returns the contextKeyClass.
	 */
	public String getContextKeyClass() {
		return contextKeyClass;
	}

	/**
	 * @param contextKeyClass The contextKeyClass to set.
	 */
	public void setContextKeyClass(String contextKeyClass) {
		this.contextKeyClass = contextKeyClass;
	}

	/**
	 * @return Returns the contextKeyFamily.
	 */
	public String getContextKeyFamily() {
		return contextKeyFamily;
	}

	/**
	 * @param contextKeyFamily The contextKeyFamily to set.
	 */
	public void setContextKeyFamily(String contextKeyFamily) {
		this.contextKeyFamily = contextKeyFamily;
	}

	/**
	 * @return Returns the contextKeyGenus.
	 */
	public String getContextKeyGenus() {
		return contextKeyGenus;
	}

	/**
	 * @param contextKeyGenus The contextKeyGenus to set.
	 */
	public void setContextKeyGenus(String contextKeyGenus) {
		this.contextKeyGenus = contextKeyGenus;
	}

	/**
	 * @return Returns the contextKeyKingdom.
	 */
	public String getContextKeyKingdom() {
		return contextKeyKingdom;
	}

	/**
	 * @param contextKeyKingdom The contextKeyKingdom to set.
	 */
	public void setContextKeyKingdom(String contextKeyKingdom) {
		this.contextKeyKingdom = contextKeyKingdom;
	}

	/**
	 * @return Returns the contextKeyOrder.
	 */
	public String getContextKeyOrder() {
		return contextKeyOrder;
	}

	/**
	 * @param contextKeyOrder The contextKeyOrder to set.
	 */
	public void setContextKeyOrder(String contextKeyOrder) {
		this.contextKeyOrder = contextKeyOrder;
	}

	/**
	 * @return Returns the contextKeyPhylum.
	 */
	public String getContextKeyPhylum() {
		return contextKeyPhylum;
	}

	/**
	 * @param contextKeyPhylum The contextKeyPhylum to set.
	 */
	public void setContextKeyPhylum(String contextKeyPhylum) {
		this.contextKeyPhylum = contextKeyPhylum;
	}

	/**
	 * @return Returns the contextKeyScientificName.
	 */
	public String getContextKeyScientificName() {
		return contextKeyScientificName;
	}

	/**
	 * @param contextKeyScientificName The contextKeyScientificName to set.
	 */
	public void setContextKeyScientificName(String contextKeyScientificName) {
		this.contextKeyScientificName = contextKeyScientificName;
	}

	/**
	 * @return Returns the contextKeyStoredClassifications.
	 */
	public String getContextKeyStoredClassifications() {
		return contextKeyStoredClassifications;
	}

	/**
	 * @param contextKeyStoredClassifications The contextKeyStoredClassifications to set.
	 */
	public void setContextKeyStoredClassifications(
			String contextKeyStoredClassifications) {
		this.contextKeyStoredClassifications = contextKeyStoredClassifications;
	}

	/**
	 * @return Returns the contextKeyTaxonConceptId.
	 */
	public String getContextKeyTaxonConceptId() {
		return contextKeyTaxonConceptId;
	}

	/**
	 * @param contextKeyTaxonConceptId The contextKeyTaxonConceptId to set.
	 */
	public void setContextKeyTaxonConceptId(String contextKeyTaxonConceptId) {
		this.contextKeyTaxonConceptId = contextKeyTaxonConceptId;
	}

	/**
	 * @return Returns the rawOccurrenceRecordDAO.
	 */
	public RawOccurrenceRecordDAO getRawOccurrenceRecordDAO() {
		return rawOccurrenceRecordDAO;
	}

	/**
	 * @param rawOccurrenceRecordDAO The rawOccurrenceRecordDAO to set.
	 */
	public void setRawOccurrenceRecordDAO(
			RawOccurrenceRecordDAO rawOccurrenceRecordDAO) {
		this.rawOccurrenceRecordDAO = rawOccurrenceRecordDAO;
	}

	/**
	 * @return Returns the contextKeyTaxonNameId.
	 */
	public String getContextKeyTaxonNameId() {
		return contextKeyTaxonNameId;
	}

	/**
	 * @param contextKeyTaxonNameId The contextKeyTaxonNameId to set.
	 */
	public void setContextKeyTaxonNameId(String contextKeyTaxonNameId) {
		this.contextKeyTaxonNameId = contextKeyTaxonNameId;
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
	
	
}
