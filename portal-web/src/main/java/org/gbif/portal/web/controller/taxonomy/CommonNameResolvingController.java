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
package org.gbif.portal.web.controller.taxonomy;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.taxonomy.TaxonConceptDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.web.controller.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
/**
 * A Controller that resolves a name to a concept.
 * 
 * @author dmartin
 */
public class CommonNameResolvingController extends RestController {

	/**Taxonomy Manager for name searches **/
	protected TaxonomyManager taxonomyManager;
	
	protected String nameResolvingView = "commonNameResolveView";
	protected String nameRequestKey = "name";	
	protected String nameModelKey = "name";
	protected String nameMatchesModelKey = "nameMatches";
	/**
	 * @see org.gbif.portal.web.controller.RestController#handleRequest(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView handleRequest(Map<String, String>properties,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if(properties.size()>0){
			String name = properties.get(nameRequestKey);
			name = decodeParameter(name);
			// get vernacular names for this name
			SearchResultsDTO searchResultsDTO = taxonomyManager.findTaxonConceptsForCommonName(name, false, new SearchConstraints());
			//if more than one results
			List<TaxonConceptDTO> taxonConcepts = (List<TaxonConceptDTO>) searchResultsDTO.getResults();
			int resultSetSize = taxonConcepts.size();
			if(resultSetSize==1){
				TaxonConceptDTO tcDTO = (TaxonConceptDTO) taxonConcepts.get(0);
				if(!tcDTO.getIsNubConcept()){
					tcDTO = taxonomyManager.getTaxonConceptFor(tcDTO.getPartnerConceptKey());
				}
				
				try {
					name = URLEncoder.encode(name, "UTF-8");
				} catch (Exception e){
					logger.error(e.getMessage(), e);
				}
				//if one match, redirect to the concept
				return new ModelAndView(new RedirectView("/species/"+tcDTO.getKey()+"/commonName/"+name, true));				
			} else {
				ModelAndView mav = new ModelAndView(nameResolvingView);
				mav.addObject(nameModelKey, name);
				mav.addObject(nameMatchesModelKey, searchResultsDTO.getResults());
				return mav;
			}
		}
		return redirectToDefaultView();
	}

	/**
	 * @param taxonomyManager the taxonomyManager to set
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
	}

	/**
	 * @param nameMatchesModelKey the nameMatchesModelKey to set
	 */
	public void setNameMatchesModelKey(String nameMatchesModelKey) {
		this.nameMatchesModelKey = nameMatchesModelKey;
	}

	/**
	 * @param nameModelKey the nameModelKey to set
	 */
	public void setNameModelKey(String nameModelKey) {
		this.nameModelKey = nameModelKey;
	}

	/**
	 * @param nameRequestKey the nameRequestKey to set
	 */
	public void setNameRequestKey(String nameRequestKey) {
		this.nameRequestKey = nameRequestKey;
	}

	/**
	 * @param nameResolvingView the nameResolvingView to set
	 */
	public void setNameResolvingView(String nameResolvingView) {
		this.nameResolvingView = nameResolvingView;
	}
}