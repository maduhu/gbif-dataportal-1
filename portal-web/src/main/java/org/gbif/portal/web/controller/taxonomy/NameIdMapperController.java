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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.dto.taxonomy.TaxonConceptDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.web.controller.taxonomy.bean.NamesList;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.AbstractView;

/**
 * Simple tool for mapping taxonomic names to system ids for these concepts. 
 * 
 * @author dmartin
 */
public class NameIdMapperController extends SimpleFormController {

	protected TaxonomyManager taxonomyManager;
	protected DataResourceManager dataResourceManager;
	protected Map<String, AbstractView> exportViews = null;
	/** Whether to match unconfirmed names */
	protected boolean allowUnconfirmedNames = false;
	
	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object commandObject, BindException exception) throws Exception {
		
		List<List<TaxonConceptDTO>> matchedConcepts = new ArrayList<List<TaxonConceptDTO>>();
		List<String> suppliedNames = new ArrayList<String>();
		NamesList namesList = (NamesList) commandObject;
		
		if(StringUtils.isEmpty(namesList.getNameList()))
			return new ModelAndView(getFormView());
		
		StringTokenizer st = new StringTokenizer(namesList.getNameList(), "\n");
		String nubProviderKey = dataResourceManager.getNubDataProvider().getKey();
		SearchConstraints searchConstraints  = new SearchConstraints(0,5);
		
		/** Whether to allow unconfirmed names */
		boolean allowUnconfirmed = ServletRequestUtils.getBooleanParameter(request, "allowUnconfirmed", allowUnconfirmedNames);	
		
		while(st.hasMoreTokens()){

			String token = st.nextToken();
			token = token.trim();
			if(token!=null && StringUtils.isNotEmpty(token)){
				TaxonConceptDTO tcDTO = null;
				List<TaxonConceptDTO> tcs = null; 
				if(namesList.isScientific()){
					tcs = taxonomyManager.findTaxonConcepts(token, false, null, nubProviderKey, null, null, null, null, null, allowUnconfirmed, false, searchConstraints);
				} else {
					tcs = taxonomyManager.findTaxonConceptsForCommonName(token, false, searchConstraints);
				}
				
				if(tcs.size()==1){
					//check for synonyms
					tcDTO = tcs.get(0);
					if(namesList.isMapSynonymsToAccepted() && !tcDTO.isAccepted()){
						//find the accepted name
						tcDTO = taxonomyManager.getTaxonConceptFor(tcDTO.getAcceptedConceptKey());
					}
				}
				if(tcDTO!=null){
					List<TaxonConceptDTO> tcsList = new ArrayList<TaxonConceptDTO>();
					tcsList.add(tcDTO);
					matchedConcepts.add(tcsList);
				} else if(!tcs.isEmpty()) {
					matchedConcepts.add(tcs);
				} else
					matchedConcepts.add(new ArrayList<TaxonConceptDTO>());
				suppliedNames.add(token);
			}
		}
		
		ModelAndView mav = new ModelAndView(getSuccessView());
		mav.addObject("suppliedNames", suppliedNames);
		mav.addObject("names", matchedConcepts);
		return mav;
	}

	/**
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(isFormSubmission(request) && request.getParameter("export")!=null){

			ServletOutputStream sOut = response.getOutputStream();
			response.setHeader("Content-Disposition", "attachment; name-id-pairs.txt");
			
			//get all the supplied names from the form
			int index = 1;
			
			String suppliedName = request.getParameter("supplied-name-"+index);
			while (suppliedName!=null){
				String systemId = request.getParameter("systemId-"+index);
				sOut.write(suppliedName.getBytes());
				sOut.write("\t".getBytes());
				if(systemId!=null)
					sOut.write(systemId.getBytes());
				sOut.write("\n".getBytes());
				index++;
				suppliedName = request.getParameter("supplied-name-"+index);
			}
			return null;
		}
		return super.handleRequestInternal(request, response);
	}

	/**
	 * @param taxonomyManager the taxonomyManager to set
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
	}

	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}

	/**
	 * @param exportViews the exportViews to set
	 */
	public void setExportViews(Map<String, AbstractView> exportViews) {
		this.exportViews = exportViews;
	}
}