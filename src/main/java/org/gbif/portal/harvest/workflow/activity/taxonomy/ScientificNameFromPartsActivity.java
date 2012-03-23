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

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.harvest.taxonomy.ClassificationContainer;
import org.gbif.portal.model.TaxonName;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * Scientific name from parts generates a scientific name in the context if
 * the parts are present but the full name is absent
 * @author Donald Hobern
 */
public class ScientificNameFromPartsActivity extends BaseActivity implements Activity {	
	/**
	 * Context Keys
	 */
	protected String contextKeyScientificName;
	protected String contextKeyGenus;
	protected String contextKeySpecificEpithet;
	protected String contextKeyInfraspecificEpithet;
	protected String contextKeyInfraspecificMarker;
	protected String contextKeyPreviousClassificationContainer;
	protected String contextKeyParsedRank;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		String scientificName = (String) context.get(getContextKeyScientificName(), String.class, false);
		Integer parsedRank = (Integer) context.get(getContextKeyParsedRank(), Integer.class, false);
		if (scientificName == null && (parsedRank == null || parsedRank >= 7000)) {
			String genus = StringUtils.trimToNull((String) context.get(getContextKeyGenus(), String.class, false));
			if (genus == null) {
				TaxonName genusTaxon = getNameFromPreviousClassification(context, 6000);
				if (genusTaxon != null) {
					genus = genusTaxon.getGeneric();
				}
			}
			if (genus != null) {
				String specificEpithet = StringUtils.trimToNull((String) context.get(getContextKeySpecificEpithet(), String.class, false));
				if (specificEpithet == null) {
					TaxonName speciesTaxon = getNameFromPreviousClassification(context, 7000);
					if (speciesTaxon != null) {
						specificEpithet = speciesTaxon.getSpecific();
					}
				}
				if (specificEpithet != null) {
					if (specificEpithet.startsWith(genus + " ")) {
						scientificName = specificEpithet;
					} else {
						scientificName = genus + " " + specificEpithet;
					}
					String infraspecificEpithet = StringUtils.trimToNull((String) context.get(getContextKeyInfraspecificEpithet(), String.class, false));
					if (infraspecificEpithet != null) {
						String infraspecificMarker = StringUtils.trimToNull((String) context.get(getContextKeyInfraspecificMarker(), String.class, false));
						if (infraspecificMarker != null) {
							scientificName += " " + infraspecificMarker;
						}
						scientificName += " " + infraspecificEpithet;
					}
					context.put(contextKeyScientificName, scientificName);
				}
			}
		}
		return context;
	}
	
	private TaxonName getNameFromPreviousClassification(ProcessContext context, int rank) {
		TaxonName name = null;
		if (getContextKeyPreviousClassificationContainer() != null) {
			try {
				ClassificationContainer classification = (ClassificationContainer) 
					context.get(getContextKeyPreviousClassificationContainer(), ClassificationContainer.class, false);
				if (classification != null) {
					name = classification.getTaxonNameByRank(rank);
				}
			} catch (Exception e) {
				// Ignore
			}
		}
		return name;
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
	 * @return the contextKeyInfraspecificEpithet
	 */
	public String getContextKeyInfraspecificEpithet() {
		return contextKeyInfraspecificEpithet;
	}

	/**
	 * @param contextKeyInfraspecificEpithet the contextKeyInfraspecificEpithet to set
	 */
	public void setContextKeyInfraspecificEpithet(
			String contextKeyInfraspecificEpithet) {
		this.contextKeyInfraspecificEpithet = contextKeyInfraspecificEpithet;
	}

	/**
	 * @return the contextKeyInfraspecificMarker
	 */
	public String getContextKeyInfraspecificMarker() {
		return contextKeyInfraspecificMarker;
	}

	/**
	 * @param contextKeyInfraspecificMarker the contextKeyInfraspecificMarker to set
	 */
	public void setContextKeyInfraspecificMarker(
			String contextKeyInfraspecificMarker) {
		this.contextKeyInfraspecificMarker = contextKeyInfraspecificMarker;
	}

	/**
	 * @return the contextKeyScientificName
	 */
	public String getContextKeyScientificName() {
		return contextKeyScientificName;
	}

	/**
	 * @param contextKeyScientificName the contextKeyScientificName to set
	 */
	public void setContextKeyScientificName(String contextKeyScientificName) {
		this.contextKeyScientificName = contextKeyScientificName;
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