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

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.model.TaxonName;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * An activity that will ensure that the Genus is in the classification
 * If there is a classification that includes lower order ranks (than Genus)
 * but does not have the genus in the classification, then the Genus is taken from the 
 * first rank below Genus, and is inserted into the classification. 
 * 
 * @author trobertson
 */
public class GenusInClassificationCheckActivity extends BaseActivity implements Activity {	
	/**
	 * Context Keys
	 */
	protected String contextKeyClassificationList;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		List<TaxonName> classification = (List<TaxonName>) context.get(getContextKeyClassificationList(), List.class, true);
		boolean found = false;
		boolean required = false;
		for (TaxonName taxon : classification) {
			if (taxon.getRank() == 6000) {
				found = true;
			}
			if (taxon.getRank() >= 6000) {
				required = true;
			}
		}
		
		if (!found && required) {
			logger.debug("No Genus found in classification, but it is required - attempting to create one");
				int indexToPlaceGenus = 0;
				TaxonName firstLowerThanGenus = null;
				for (int i=0; i<classification.size(); i++) {
					if (classification.get(i).getRank() < 6000) {
						indexToPlaceGenus = i;
					} else if (classification.get(i).getRank() > 6000
							&& firstLowerThanGenus == null) {
						firstLowerThanGenus = classification.get(i);
						break;
					}
				}
				
				if (firstLowerThanGenus != null
						&& StringUtils.isNotEmpty(firstLowerThanGenus.getGeneric())) {
					TaxonName genus = new TaxonName();
					genus.setRank(6000);
					genus.setCanonical(firstLowerThanGenus.getGeneric());
					genus.setGeneric(firstLowerThanGenus.getGeneric());
					logger.debug("Adding a genus to index: " + indexToPlaceGenus);
					if (classification.size()>1) {
						classification.add(indexToPlaceGenus+1, genus);	
					} else {
						classification.add(indexToPlaceGenus, genus);
					}
					
				} else {
					logger.info("Cannot insert the genus as there is no generic atom set in the lower rank, or it is null: " + firstLowerThanGenus);
				}
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
}
