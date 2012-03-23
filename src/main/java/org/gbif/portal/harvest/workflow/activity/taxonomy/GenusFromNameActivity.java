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

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.harvest.taxonomy.ScientificNameParser;
import org.gbif.portal.model.TaxonName;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * An activity that will extract a genus from a name, and put it into the context 
 * 
 * @author trobertson
 */
public class GenusFromNameActivity extends BaseActivity implements Activity {	
	/**
	 * Context Keys
	 */
	protected String contextKeyName;
	protected String contextKeyGenus;
	
	// delete this
	protected ScientificNameParser plantaeNameParser;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		String name = (String) context.get(getContextKeyName(), String.class, true);
		if (StringUtils.isEmpty(name)) {
			throw new InvalidNameFormatException("The name appears to be empty: " + name);
		} 
		
		if (!name.contains(" ")) {
			context.put(getContextKeyGenus(), name);
			//logger.info("Genus: " + name);
		} else {
			context.put(getContextKeyGenus(), name.substring(0, name.indexOf(" ")));
			//logger.info("Genus: " + name.substring(0, name.indexOf(" ")));
		}
		
		plantaeNameParser.parse(context, name, new LinkedList<TaxonName>());
		
		return context;
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
	 * @return Returns the contextKeyName.
	 */
	public String getContextKeyName() {
		return contextKeyName;
	}

	/**
	 * @param contextKeyName The contextKeyName to set.
	 */
	public void setContextKeyName(String contextKeyName) {
		this.contextKeyName = contextKeyName;
	}

	/**
	 * @return Returns the plantaeNameParser.
	 */
	public ScientificNameParser getPlantaeNameParser() {
		return plantaeNameParser;
	}

	/**
	 * @param plantaeNameParser The plantaeNameParser to set.
	 */
	public void setPlantaeNameParser(ScientificNameParser plantaeNameParser) {
		this.plantaeNameParser = plantaeNameParser;
	}
}
