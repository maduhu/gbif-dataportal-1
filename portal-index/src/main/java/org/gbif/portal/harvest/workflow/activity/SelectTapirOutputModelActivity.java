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

import java.util.List;
import java.util.Map;

import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * An activity that will add all namespaces in a supplied list to the list of 
 * property store namespaces.
 * 
 * Used to add conceptual schemas from a TAPIR capabilities document to the 
 * context
 * 
 * @author Donald Hobern
 */
public class SelectTapirOutputModelActivity extends BaseActivity implements
		Activity {	
	/**
	 * Context Keys
	 */
	protected String contextKeyPsNamespaces;
	protected String contextKeyOutputModel;
	protected Map outputModelsForNamespaces;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		List<String> psNamespaces = (List<String>)context.get(getContextKeyPsNamespaces(), List.class, true);

		for (String psNamespace : psNamespaces) {
			String outputModel = (String) outputModelsForNamespaces.get(psNamespace);
			if (outputModel != null) {
				context.put(contextKeyOutputModel, outputModel);
				break;
			}
		}
		
		return context;
	}

	/**
	 * @return Returns the contextKeyPsNamespaces.
	 */
	public String getContextKeyPsNamespaces() {
		return contextKeyPsNamespaces;
	}

	/**
	 * @param contextKeyPsNamespaces The contextKeyPsNamespaces to set.
	 */
	public void setContextKeyPsNamespaces(String contextKeyPsNamespaces) {
		this.contextKeyPsNamespaces = contextKeyPsNamespaces;
	}

	/**
	 * @return the contextKeyOutputModel
	 */
	public String getContextKeyOutputModel() {
		return contextKeyOutputModel;
	}

	/**
	 * @param contextKeyOutputModel the contextKeyOutputModel to set
	 */
	public void setContextKeyOutputModel(String contextKeyOutputModel) {
		this.contextKeyOutputModel = contextKeyOutputModel;
	}

	/**
	 * @return the outputModelsForNamespaces
	 */
	public Map getOutputModelsForNamespaces() {
		return outputModelsForNamespaces;
	}

	/**
	 * @param outputModelsForNamespaces the outputModelsForNamespaces to set
	 */
	public void setOutputModelsForNamespaces(Map outputModelsForNamespaces) {
		this.outputModelsForNamespaces = outputModelsForNamespaces;
	}
}
