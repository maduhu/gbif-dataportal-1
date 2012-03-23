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

import org.gbif.portal.harvest.taxonomy.ClassificationContainer;
import org.gbif.portal.model.TaxonName;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * If a taxonomy file is structured in a nested fashion (either by filling
 * only the columns special to each rank or by giving name and rank for each
 * line, and each line being treated as a child taxon of the previous line
 * if its rank is lower), then a previousClassificationContainer can be 
 * used to pass the classification from one invocation to the next.
 * 
 * This activity copies all appropriately ranked elements from the 
 * classification supplied through previousClassificationContainer (if
 * provided) to the current classification, and then setting the
 * current classification into previousClassificationContainer.
 * 
 * @author Donald Hobern
 */
public class PreviousClassificationHandlerActivity extends BaseActivity implements Activity {	
	/**
	 * Context Keys
	 */
	protected String contextKeyClassificationList;
	protected String contextKeyPreviousClassificationContainer;
	protected String contextKeyGenus;
	protected String contextKeySpecificEpithet;
	protected String contextKeyParsedRank;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		ClassificationContainer previousClassificationContainer = (ClassificationContainer) context.get(getContextKeyPreviousClassificationContainer(), ClassificationContainer.class, false);
		if (previousClassificationContainer != null) {
			List<TaxonName> classificationList = (List<TaxonName>) context.get(getContextKeyClassificationList(), List.class, false);
			if (classificationList == null) {
				classificationList = new LinkedList<TaxonName>();
				context.put(getContextKeyClassificationList(), classificationList);
			}
			if (previousClassificationContainer.getClassificationList() != null) {
				/* Note that the "highestRankValue" corresponds with the lowest rank */
				Integer highestRankValue = (Integer) context.get(getContextKeyParsedRank(), Integer.class, false);
				if (highestRankValue == null) {
					highestRankValue = 0;
					for (TaxonName name : classificationList) {
						if (name.getRank() > highestRankValue) {
							highestRankValue = name.getRank();
						}
					}
				}
				for (TaxonName name : previousClassificationContainer.getClassificationList()) {
					if (name.getRank() < highestRankValue) {
						for (TaxonName currentName : classificationList) {
							if (currentName.getRank() == name.getRank()) {
								name = null;
								break;
							}
						}
						if (name != null) {
							classificationList.add(name);
							/* If this is a genus, make sure its name is in the context */
							if (name.getRank() == 6000) {
								String currentGenus = (String) context.get(getContextKeyGenus(), String.class, false);
								if (currentGenus == null) {
									context.put(getContextKeyGenus(), name.getCanonical());
								}
							}
							/* If this is a species, make sure its epithet is in the context */
							if (name.getRank() == 7000) {
								String currentSpecies = (String) context.get(getContextKeySpecificEpithet(), String.class, false);
								if (currentSpecies == null) {
									context.put(getContextKeySpecificEpithet(), name.getSpecific());
								}
							}
						}
					}
				}
			}
			previousClassificationContainer.setClassificationList(classificationList);
		}
		return context;
	}

	/**
	 * @return the contextKeyPreviousClassificationContainer
	 */
	public String getContextKeyPreviousClassificationContainer() {
		return contextKeyPreviousClassificationContainer;
	}

	/**
	 * @param contextKeyPreviousClassificationContainer the contextKeyPreviousClassificationContainer to set
	 */
	public void setContextKeyPreviousClassificationContainer(
			String contextKeyPreviousClassificationContainer) {
		this.contextKeyPreviousClassificationContainer = contextKeyPreviousClassificationContainer;
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
	 * @return the contextKeyGenus
	 */
	public String getContextKeyGenus() {
		return contextKeyGenus;
	}

	/**
	 * @param contextKeyGenus the contextKeyGenus to set
	 */
	public void setContextKeyGenus(String contextKeyGenus) {
		this.contextKeyGenus = contextKeyGenus;
	}

	/**
	 * @return the contextKeySpecificEpithet
	 */
	public String getContextKeySpecificEpithet() {
		return contextKeySpecificEpithet;
	}

	/**
	 * @param contextKeySpecificEpithet the contextKeySpecificEpithet to set
	 */
	public void setContextKeySpecificEpithet(String contextKeySpecificEpithet) {
		this.contextKeySpecificEpithet = contextKeySpecificEpithet;
	}

	/**
	 * @return the contextKeyParsedRank
	 */
	public String getContextKeyParsedRank() {
		return contextKeyParsedRank;
	}

	/**
	 * @param contextKeyParsedRank the contextKeyParsedRank to set
	 */
	public void setContextKeyParsedRank(String contextKeyParsedRank) {
		this.contextKeyParsedRank = contextKeyParsedRank;
	}
}