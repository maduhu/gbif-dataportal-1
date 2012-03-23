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

import org.gbif.portal.harvest.taxonomy.RegularExpressionToTaxonName;
import org.gbif.portal.model.TaxonName;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * An activity that will add a species to anything identified as subspecies in the classification if it does not exist 
 * 
 * @author trobertson
 */
public class SpeciesInClassificationForSubspeciesActivity extends BaseActivity implements Activity {	
	/**
	 * Context Keys
	 */
	protected String contextKeyClassificationList;
	
	/**
	 * This can be used to get the Canonical
	 */
	protected RegularExpressionToTaxonName speciesParser;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		List<TaxonName> classification = (List<TaxonName>) context.get(getContextKeyClassificationList(), List.class, false);
		if (classification != null 
				&& classification.size()>0) {
			// if the last one has a rank >= 8000 then lets make sure there is a species in there ya!
			TaxonName last = classification.get(classification.size()-1);
			logger.info("Last name:" + last);
			if (last.getRank() >= 8000) {
				TaxonName lastButOne = null;
				boolean createSpecies = false;
				if (classification.size()>1) {
					lastButOne = classification.get(classification.size()-2);
					if (lastButOne.getRank() != 7000) {
						createSpecies = true;
					}
				}
				logger.info("Species needs added:" + createSpecies);
				if (createSpecies) {
					TaxonName species = new TaxonName();
					boolean parsed = speciesParser.parse(last.getCanonical(), species);
					if (parsed) {
						species.setAuthor(null);
						classification.add(classification.size()-1, species);
					}					
				}
			}
		}
		return context;
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
	 * @return Returns the speciesParser.
	 */
	public RegularExpressionToTaxonName getSpeciesParser() {
		return speciesParser;
	}

	/**
	 * @param speciesParser The speciesParser to set.
	 */
	public void setSpeciesParser(RegularExpressionToTaxonName speciesParser) {
		this.speciesParser = speciesParser;
	}
}