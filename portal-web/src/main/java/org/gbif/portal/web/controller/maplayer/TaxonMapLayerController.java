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
package org.gbif.portal.web.controller.maplayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.TaxonomyManager;

/**
 * A map layer controller for taxon maps that handles mapping non nub concept
 * ids to their nub equivalent.
 * 
 * @author dmartin
 */
public class TaxonMapLayerController extends EntityMapLayerController{

	/** Taxonomy manager for concept lookup */
	protected TaxonomyManager taxonomyManager;
	
	/** Default entity id **/
	protected String defaultEntityId = "0";
	
	/**
	 * @see org.gbif.portal.web.controller.maplayer.EntityMapLayerController#getKeys(java.util.Map)
	 */
	@Override
	public List<String> getKeys(Map<String, String> properties) {
		
		List<String> keys = super.getKeys(properties);
		List<String> processedKeys = new ArrayList<String>();
		//iterate through, retrieving concept and replacing with nub concept
		//if necessary
		for(String key: keys){
			BriefTaxonConceptDTO tc = null;
			try {
				tc = taxonomyManager.getBriefTaxonConceptFor(key);
				if(tc!=null){
					if(!tc.getIsNubConcept()){
						processedKeys.add(tc.getPartnerConceptKey());
					} else {
						processedKeys.add(tc.getKey());
					}
				}
			} catch (ServiceException e) {
				logger.debug(e.getMessage(), e);
			}
		}
		//if no keys point to a non existent taxonConcept, replace with 0.
		if(processedKeys.isEmpty())
			processedKeys.add(defaultEntityId);
		return processedKeys; 
	}

	/**
	 * @param taxonomyManager the taxonomyManager to set
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
	}
}