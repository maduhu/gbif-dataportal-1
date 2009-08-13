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
package org.gbif.portal.web.controller.feedback;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.dto.occurrence.OccurrenceRecordDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.dto.taxonomy.TaxonConceptDTO;
import org.gbif.portal.dto.util.EntityType;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.OccurrenceManager;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.web.controller.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * A Controller for dealing with the input to the user feedback
 * Determines the feedback type and from that can get the information the form requires 
 *
 * @author Tim Robertson
 */
public class FeedbackInputController extends RestController {
	
	/** The Managers */
	protected TaxonomyManager taxonomyManager;
	protected DataResourceManager dataResourceManager;
	protected OccurrenceManager occurrenceManager;
	
	/** The request keys **/
	protected String conceptTypeRequestKey = "conceptType";
	protected String conceptKeyRequestKey = "conceptKey";
	
	/** The view names */
	protected String feedbackInputViewName = "feedbackInputView";
	
	/** The model keys */
	protected String feedbackOnURLModelKey = "feedbackOnURL";
	protected String feedbackTypeModelKey = "type";
	protected String feedbackConceptKeyModelKey = "conceptKey";
	protected String taxonConceptModelKey = "taxonConcept";
	protected String occurrenceModelKey = "occurrenceRecord";
	protected String dataProviderModelKey = "dataProvider";
	protected String dataProviderAgentsModelKey="agents";
	protected String dataResourceModelKey = "dataResource";
	protected String dataResourceAgentsModelKey = "dataResourceAgents";	
	
	/**
	 * @see org.gbif.portal.web.controller.RestController#handleRequest(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ModelAndView handleRequest(Map<String, String> properties, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String conceptTypeAsString = properties.get(conceptTypeRequestKey);
		String conceptKey = properties.get(conceptKeyRequestKey);
		
		EntityType conceptType = EntityType.entityTypesByName.get(conceptTypeAsString);
		
		String dataProviderKey = null;
		String dataResourceKey = null;
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(feedbackOnURLModelKey, request.getParameter(feedbackOnURLModelKey));
		model.put(feedbackConceptKeyModelKey, conceptKey);
		if (conceptType.equals(EntityType.TYPE_TAXON)) {
			logger.info("User feeding back information for taxon");
			model.put(feedbackTypeModelKey, EntityType.TYPE_TAXON.getName());
			TaxonConceptDTO dto = taxonomyManager.getTaxonConceptFor(conceptKey);
			if (dto != null) {
				dataProviderKey = dto.getDataProviderKey();
				dataResourceKey = dto.getDataResourceKey();
				model.put(taxonConceptModelKey, dto);
				
			} else {
				logger.warn("Unable to get taxon concept for key: " + conceptKey);
			}
			
		} else if(conceptType.equals(EntityType.TYPE_DATA_RESOURCE)) {
			logger.info("User feeding back information for resource");
			model.put(feedbackTypeModelKey, EntityType.TYPE_DATA_RESOURCE.getName());
			dataResourceKey = conceptKey;
			
		} else if(conceptType.equals(EntityType.TYPE_OCCURRENCE)) {
			logger.info("User feeding back information for occurrence");
			model.put(feedbackTypeModelKey, EntityType.TYPE_OCCURRENCE.getName());
			OccurrenceRecordDTO dto = occurrenceManager.getOccurrenceRecordFor(conceptKey);
			if (dto != null) {
				dataProviderKey = dto.getDataProviderKey();
				dataResourceKey = dto.getDataResourceKey();
				model.put(occurrenceModelKey, dto);
				model.put(taxonConceptModelKey, taxonomyManager.getTaxonConceptFor(dto.getTaxonConceptKey()));
			} else {
				logger.warn("Unable to get occurrence for key: " + conceptKey);
			}			
		}
		
		if (dataResourceKey != null) {
			DataResourceDTO dto = dataResourceManager.getDataResourceFor(dataResourceKey);
			if(dto!=null){
				dataProviderKey = dto.getDataProviderKey();
				model.put(dataResourceModelKey, dto);
				model.put(dataResourceAgentsModelKey, dataResourceManager.getAgentsForDataResource(dataResourceKey));
			}
		} else {
			logger.warn("Unable to get data resource for key: " + dataResourceKey);
		}
		logger.info("Feedback relates to the data provider key: " + dataProviderKey);
		if (dataProviderKey != null) {
			model.put(dataProviderModelKey, dataResourceManager.getDataProviderFor(dataProviderKey));
		}
		
		return new ModelAndView(feedbackInputViewName, model);
	}

	/**
	 * @return Returns the dataResourceManager.
	 */
	public DataResourceManager getDataResourceManager() {
		return dataResourceManager;
	}

	/**
	 * @param dataResourceManager The dataResourceManager to set.
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}

	/**
	 * @return Returns the occurrenceManager.
	 */
	public OccurrenceManager getOccurrenceManager() {
		return occurrenceManager;
	}

	/**
	 * @param occurrenceManager The occurrenceManager to set.
	 */
	public void setOccurrenceManager(OccurrenceManager occurrenceManager) {
		this.occurrenceManager = occurrenceManager;
	}

	/**
	 * @return Returns the taxonomyManager.
	 */
	public TaxonomyManager getTaxonomyManager() {
		return taxonomyManager;
	}

	/**
	 * @param taxonomyManager The taxonomyManager to set.
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
	}
}