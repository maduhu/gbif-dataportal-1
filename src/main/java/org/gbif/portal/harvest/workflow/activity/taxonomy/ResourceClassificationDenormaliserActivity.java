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

import org.gbif.portal.harvest.taxonomy.TaxonomyUtils;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * Resource classification denormaliser generates the denormalised taxonomy for
 * a data resource
 * @author Donald Hobern
 */
public class ResourceClassificationDenormaliserActivity extends BaseActivity implements Activity {	
	/**
	 * Context Keys
	 */
	protected String contextKeyDataResourceId;
	
	/**
	 * The utils
	 */
	protected TaxonomyUtils taxonomyUtils;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		long dataResourceId = (Long) context.get(getContextKeyDataResourceId(), Long.class, true);
		taxonomyUtils.denormalisedTaxonomyForResource(dataResourceId);
		return context;
	}

	/**
	 * @return Returns the contextKeyDataResourceId.
	 */
	public String getContextKeyDataResourceId() {
		return contextKeyDataResourceId;
	}

	/**
	 * @param contextKeyDataResourceId The contextKeyDataResourceId to set.
	 */
	public void setContextKeyDataResourceId(String contextKeyDataResourceId) {
		this.contextKeyDataResourceId = contextKeyDataResourceId;
	}

	/**
	 * @return Returns the taxonomyUtils.
	 */
	public TaxonomyUtils getTaxonomyUtils() {
		return taxonomyUtils;
	}

	/**
	 * @param taxonomyUtils The taxonomyUtils to set.
	 */
	public void setTaxonomyUtils(TaxonomyUtils taxonomyUtils) {
		this.taxonomyUtils = taxonomyUtils;
	}
}
