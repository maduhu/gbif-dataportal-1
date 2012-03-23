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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gbif.portal.dao.RawOccurrenceRecordDAO;
import org.gbif.portal.model.LinnaeanRankClassification;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * An activity that will page through the raw taxonomy for a resource access point
 * and stick the K,P,C,O,F,G,SN to the context and start a child workflow
 * @author trobertson
 */
public class RawTaxonomyPagingActivity extends BaseActivity implements
		Activity {
	/**
	 * The dao 
	 */
	protected RawOccurrenceRecordDAO rawOccurrenceRecordDAO;
	
	/**
	 * Context Keys
	 */
	protected String contextKeyDataResourceId = "dataResourceId";
	protected String contextKeyKingdom = "kingdom";
	protected String contextKeyPhylum = "phylum";
	protected String contextKeyClass = "class";
	protected String contextKeyOrder = "order";
	protected String contextKeyFamily = "family";
	protected String contextKeyGenus = "genus";
	protected String contextKeyScientificName = "scientificName";
	protected String contextKeyStoredClassifications = "storedClassifications";
	protected boolean storedClassifications = false;
	protected String contextKeyTaxonConceptId = "taxonConceptId";
	protected String contextKeyTaxonNameId = "taxonNameId";
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		Long dataResourceId = (Long) context.get(getContextKeyDataResourceId(), Long.class, true);
		long time = System.currentTimeMillis();
		List<LinnaeanRankClassification> classifications = rawOccurrenceRecordDAO.getFullRawDistinctClassification(dataResourceId);
		logger.info("Resource access point has " + classifications.size() + " distinct classifications. Determined in " + ((1+ System.currentTimeMillis()-time)/1000)  + " secs.");
		
		for (LinnaeanRankClassification classification : classifications) {
			Map<String, Object> seed = new HashMap<String, Object>();
			seed.put(getContextKeyKingdom(), classification.getKingdom());
			seed.put(getContextKeyPhylum(), classification.getPhylum());
			seed.put(getContextKeyClass(), classification.getKlass());
			seed.put(getContextKeyOrder(), classification.getOrder());
			seed.put(getContextKeyFamily(), classification.getFamily());
			seed.put(getContextKeyGenus(), classification.getGenus());
			seed.put(getContextKeyScientificName(), classification.getScientificName());
			
			// launch the workflow to handle the classification
			launchWorkflow(context, seed);
			
			// we should upper case the first values for the key since OR does that at least
			classification.setKingdom(upperCaseFirst(classification.getKingdom()));
			classification.setPhylum(upperCaseFirst(classification.getPhylum()));
			classification.setKlass(upperCaseFirst(classification.getKlass()));
			classification.setOrder(upperCaseFirst(classification.getOrder()));
			classification.setFamily(upperCaseFirst(classification.getFamily()));
			classification.setGenus(upperCaseFirst(classification.getGenus()));
			classification.setScientificName(upperCaseFirst(classification.getScientificName()));			
			
			if (storedClassifications) {
				Map<LinnaeanRankClassification, Long[]> classificationMap = (Map<LinnaeanRankClassification, Long[]>) context.get(contextKeyStoredClassifications, Map.class, false);
				if (classificationMap == null) {
					classificationMap = new HashMap<LinnaeanRankClassification, Long[]>();
					context.put(getContextKeyStoredClassifications(), classificationMap);
				}
				logger.debug("Total classificationSize: " + classificationMap.size());
				
				Long taxonConceptId = (Long)context.get(contextKeyTaxonConceptId, Long.class, false);
				
				if (taxonConceptId!=null) {
					classificationMap.put(classification, 
							new Long[]{(Long)context.get(contextKeyTaxonConceptId, Long.class, true), 
							(Long)context.get(contextKeyTaxonNameId, Long.class, true)});
					
					logger.debug("CACHING: " + classification);
				} else {
					logger.warn("No concept cached for: " + classification);
				}

			}
		}
		return context;
	}

	/**
	 * Fix cases in which polynomials and higher taxa are all upper or lower case
	 * @param name
	 * @return
	 */
	private String upperCaseFirst(String name) {
		if (name!=null && (name.equals(name.toUpperCase()) || name.equals(name.toLowerCase()))) {
			
			if (name.length()>1) {
				name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
			} else {
				name = name.toUpperCase();
			}
		}
		return name;
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
	 * @return Returns the storedClassifications.
	 */
	public boolean isStoredClassifications() {
		return storedClassifications;
	}

	/**
	 * @param storedClassifications The storedClassifications to set.
	 */
	public void setStoredClassifications(boolean storedClassifications) {
		this.storedClassifications = storedClassifications;
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

	public String getContextKeyDataResourceId() {
		return contextKeyDataResourceId;
	}

	public void setContextKeyDataResourceId(String contextKeyDataResourceId) {
		this.contextKeyDataResourceId = contextKeyDataResourceId;
	}
	
	
	
}
