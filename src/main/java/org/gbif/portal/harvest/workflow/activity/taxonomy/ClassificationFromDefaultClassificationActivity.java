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

import java.util.LinkedList;
import java.util.List;

import org.gbif.portal.model.TaxonName;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * Classification Synchroniser is a lightweight wrapper for a 
 * @see org.gbif.portal.harvest.taxonomy.TaxonomyUtils
 * @author trobertson
 */
public class ClassificationFromDefaultClassificationActivity extends BaseActivity implements Activity {	
	/**
	 * Context Keys
	 */
	protected String contextKeyDefaultClassificationList;
	protected String contextKeyClassificationList;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		List<TaxonName> defaultClassification = (List<TaxonName>) context.get(getContextKeyDefaultClassificationList(), List.class, false);
		if (defaultClassification != null) {
			List<TaxonName> classification = (List<TaxonName>) context.get(getContextKeyClassificationList(), List.class, false);
			if (classification == null) {
				classification = new LinkedList<TaxonName>();
				context.put(getContextKeyClassificationList(), classification);
			}
			classification.addAll(defaultClassification);
		}
		return context;
	}

	/**
	 * @return the contextKeyClassificationList
	 */
	public String getContextKeyClassificationList() {
		return contextKeyClassificationList;
	}

	/**
	 * @param contextKeyClassificationList the contextKeyClassificationList to set
	 */
	public void setContextKeyClassificationList(String contextKeyClassificationList) {
		this.contextKeyClassificationList = contextKeyClassificationList;
	}

	/**
	 * @return the contextKeyDefaultClassificationList
	 */
	public String getContextKeyDefaultClassificationList() {
		return contextKeyDefaultClassificationList;
	}

	/**
	 * @param contextKeyDefaultClassificationList the contextKeyDefaultClassificationList to set
	 */
	public void setContextKeyDefaultClassificationList(
			String contextKeyDefaultClassificationList) {
		this.contextKeyDefaultClassificationList = contextKeyDefaultClassificationList;
	}
}
