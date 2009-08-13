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
 * An activity that will ensure that the Species is in the classification
 * If there is a classification that includes lower order ranks (than Species)
 * but does not have the species in the classification, then the Genus + Species is taken from the 
 * first rank below Species, and is inserted into the classification. 
 * 
 * @author trobertson
 */
public class SpeciesInClassificationCheckActivity extends BaseActivity implements Activity {	
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
			if (taxon.getRank() == 7000) {
				found = true;
			}
			if (taxon.getRank() >= 7000) {
				required = true;
			}
		}
		
		if (!found && required) {
			logger.debug("No Species found in classification, but it is required - attempting to create one");
				int indexToPlaceSpecies = 0;
				TaxonName firstLowerThanSpecies = null;
				for (int i=0; i<classification.size(); i++) {
					if (classification.get(i).getRank() < 7000) {
						indexToPlaceSpecies = i;
					} else if (classification.get(i).getRank() > 7000
							&& firstLowerThanSpecies == null) {
						firstLowerThanSpecies = classification.get(i);
						break;
					}
				}
				
				if (firstLowerThanSpecies != null
						&& StringUtils.isNotEmpty(firstLowerThanSpecies.getGeneric())
						&& StringUtils.isNotEmpty(firstLowerThanSpecies.getSpecific())) {
					TaxonName species = new TaxonName();
					species.setRank(7000);
					species.setCanonical(firstLowerThanSpecies.getGeneric() + " " + firstLowerThanSpecies.getSpecific());
					species.setGeneric(firstLowerThanSpecies.getGeneric());
					species.setSpecific(firstLowerThanSpecies.getSpecific());
					logger.debug("Adding a species to index: " + indexToPlaceSpecies);
					if (classification.size()>1) {
						classification.add(indexToPlaceSpecies+1, species);	
					} else {
						classification.add(indexToPlaceSpecies, species);
					}
					
				} else {
					logger.info("Cannot insert the species as there is no generic or specific atom set in the lower rank, or it is null: " + firstLowerThanSpecies);
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
