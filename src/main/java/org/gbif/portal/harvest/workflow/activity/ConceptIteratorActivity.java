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
package org.gbif.portal.harvest.workflow.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * Repeatedly calls the child process for a list of concepts in the context
 * @author trobertson
 */
public class ConceptIteratorActivity extends BaseActivity {
	/**
	 * The concept that must exist and be set to false to continue
	 */
	protected String contextKeyConceptToIterate;
	
	/**
	 * The key to put the concept into the child process
	 */
	protected String contextKeyKeyForChildContext;
	
	/**
     * @see org.gbif.portal.util.workflow.BaseMapContextActivity#doExecute(org.gbif.portal.util.workflow.MapContext)
     */
    @SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
    	List<Object> concepts = (List<Object>)context.get(getContextKeyConceptToIterate(), List.class, true);
    	logger.debug("Iterating over " + concepts.size() + " concepts...");
    	for (Object concept : concepts) {
    		Map<String, Object> seed  = new HashMap<String, Object>();
    		seed.put(getContextKeyKeyForChildContext(), concept);
    		launchWorkflow(context, seed);
    	}
		return context;		
	}

	/**
	 * @return Returns the contextKeyConceptToIterate.
	 */
	public String getContextKeyConceptToIterate() {
		return contextKeyConceptToIterate;
	}

	/**
	 * @param contextKeyConceptToIterate The contextKeyConceptToIterate to set.
	 */
	public void setContextKeyConceptToIterate(String contextKeyConceptToIterate) {
		this.contextKeyConceptToIterate = contextKeyConceptToIterate;
	}

	/**
	 * @return Returns the contextKeyKeyForChildContext.
	 */
	public String getContextKeyKeyForChildContext() {
		return contextKeyKeyForChildContext;
	}

	/**
	 * @param contextKeyKeyForChildContext The contextKeyKeyForChildContext to set.
	 */
	public void setContextKeyKeyForChildContext(String contextKeyKeyForChildContext) {
		this.contextKeyKeyForChildContext = contextKeyKeyForChildContext;
	}
}