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

import org.gbif.portal.dao.DataResourceDAO;
import org.gbif.portal.dao.TaxonConceptDAO;
import org.gbif.portal.model.DataResource;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * Set the secondary flag on all taxon concepts within a resource which do not have any
 * associated occurrence records.
 * 
 * NOTE - if the resource has a taxonomic priority less than or equal to 10, it is exempt 
 * from having its concepts marked as secondary
 * 
 * @author Donald Hobern
 */
public class MarkSecondaryTaxaActivity extends BaseActivity implements Activity {	
	/**
	 * DAOs
	 */
	protected TaxonConceptDAO taxonConceptDAO;
	protected DataResourceDAO dataResourceDAO;
	
	/**
	 * Context keys
	 */
	protected String contextKeyDataResourceId;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		Long dataResourceId = ((Long) context.get(getContextKeyDataResourceId(), Long.class, false));
		
		if (dataResourceId != null) {
			DataResource resource = dataResourceDAO.getById(dataResourceId.longValue());
			
			if (resource!=null && resource.getTaxonomicPriority() > 10) {
				taxonConceptDAO.markConceptsWithoutOccurrenceRecordsAsSecondary(dataResourceId);
			}
		}
		
		return context;
	}

	/**
	 * @return the contextKeyDataResourceId
	 */
	public String getContextKeyDataResourceId() {
		return contextKeyDataResourceId;
	}

	/**
	 * @param contextKeyDataResourceId the contextKeyDataResourceId to set
	 */
	public void setContextKeyDataResourceId(String contextKeyDataResourceId) {
		this.contextKeyDataResourceId = contextKeyDataResourceId;
	}

	/**
	 * @return the taxonConceptDAO
	 */
	public TaxonConceptDAO getTaxonConceptDAO() {
		return taxonConceptDAO;
	}

	/**
	 * @param taxonConceptDAO the taxonConceptDAO to set
	 */
	public void setTaxonConceptDAO(TaxonConceptDAO taxonConceptDAO) {
		this.taxonConceptDAO = taxonConceptDAO;
	}

	/**
	 * @return the dataResourceDAO
	 */
	public DataResourceDAO getDataResourceDAO() {
		return dataResourceDAO;
	}

	/**
	 * @param dataResourceDAO the dataResourceDAO to set
	 */
	public void setDataResourceDAO(DataResourceDAO dataResourceDAO) {
		this.dataResourceDAO = dataResourceDAO;
	}

}