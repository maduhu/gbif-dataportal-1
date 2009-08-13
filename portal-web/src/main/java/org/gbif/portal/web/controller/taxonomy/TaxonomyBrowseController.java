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

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.dto.taxonomy.TaxonConceptDTO;
import org.gbif.portal.dto.util.TaxonRankType;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.web.content.filter.FilterContentProvider;
import org.gbif.portal.web.controller.RestKeyValueController;
import org.gbif.portal.web.filter.CriteriaDTO;
import org.gbif.portal.web.util.TaxonConceptUtils;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.ModelAndView;

/**
 * Simple Small Tree Browser Controller intended to support the pygmy style 
 * browser. This style of browser only displays the direct ascendents of the
 * selected concept and its direct descendents. When there is not a concept selected
 * the root nodes are returned - i.e. concepts with no parent.
 * 
 * @author dmartin
 */
public class TaxonomyBrowseController extends RestKeyValueController {

	/** Taxonomy Manager providing tree and taxonConcept lookup */
	protected TaxonomyManager taxonomyManager;
	/** Data Resource manager providing data resource details */
	protected DataResourceManager dataResourceManager;

	/** Utilities for organising concepts */
	protected TaxonConceptUtils taxonConceptUtils;
	
	/** Filter Content Provider for criteria construction utils **/
	protected FilterContentProvider filterContentProvider;
	
	/** Message source */
	protected MessageSource messageSource;
	
	/** The request properties data provider key */
	protected String dataProviderPropertyKey = "provider";	
	/** The request properties data resource key */
	protected String dataResourcePropertyKey = "resource";
	/** The request properties taxon concept key */	
	protected String taxonConceptPropertyKey = "taxon";

	/** The key for the list of concepts */
	protected String conceptsModelKey = "concepts";
	/** The key for the list of selected concept */
	protected String taxonConceptModelKey = "taxonConcept";

	/** The key for the selected data provider */
	protected String nubProviderModelKey = "nubProvider";		
	/** The key for the selected data provider */
	protected String dataProviderModelKey = "dataProvider";	
	/** The key for the selected data resource */
	protected String dataResourceModelKey = "dataResource";

	/** The model key for the list of  data providers */
	protected String dataProvidersModelKey = "dataProviders";	
	/** The model key for the list of data resources */
	protected String dataResourcesModelKey = "dataResources";
	
	/** The highest rank model key  */
	protected String highestRankModelKey = "highestRank";
	
	/** The occurrence criteria for quick searching */
	protected String occurrenceCriteriaModelKey = "occurrenceCriteria";

	/** The taxonomy criteria for quick searching */
	protected String taxonomyCriteriaModelKey = "taxonomyCriteria";

	/** Threshold used to determining rendering */
	protected String taxonPriorityThresholdModelKey = " taxonPriorityThreshold";	

	/** Threshold used to determining rendering */
	protected int taxonPriorityThreshold = 20;
	
	protected String messageSourceModelKey = "messageSource";
	/** Whether or not to display unconfirmed names */
	protected boolean allowUnconfirmed = true;
	
	/**
	 * @see org.gbif.portal.web.controller.RestController#handleRequest(java.util.List, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ModelAndView handleRequest(Map<String, String>properties,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String dataProviderKey = properties.get(dataProviderPropertyKey);
		String dataResourceKey = properties.get(dataResourcePropertyKey);
		String taxonConceptKey = properties.get(taxonConceptPropertyKey);
		if(logger.isDebugEnabled())
			logger.debug("provider:"+dataProviderKey+", resource:"+dataResourceKey+", concept:"+taxonConceptKey);
		
		//Create the view		
		ModelAndView mav = resolveAndCreateView(properties, request, false);	
		
		DataProviderDTO nubProvider = dataResourceManager.getNubDataProvider();
		if(nubProvider!=null)
			mav.addObject(nubProviderModelKey, nubProvider);
		
		//retrieve the list of taxonomies - for the drop down
//		List<DataProviderDTO> dataProviders = dataResourceManager.getDataProvidersOfferingTaxonomies();
//		List<DataResourceDTO> dataResources = dataResourceManager.getDataResourcesWithInferredTaxonomies();
//		mav.addObject(dataProvidersModelKey, dataProviders);
//		mav.addObject(dataResourcesModelKey, dataResources);
		
		//get the concepts
		TaxonConceptDTO selectedConcept = null;
		List<BriefTaxonConceptDTO> concepts = null; 
		DataProviderDTO dataProviderDTO = null;
		DataResourceDTO dataResourceDTO = null;		
		
		try {
			//if concept is null, check for provider or resource id
			if(StringUtils.isEmpty(taxonConceptKey)){
				//use provider key if available, resource key if not and default taxonomy (treated as a provider key)	
				if(StringUtils.isNotEmpty(dataProviderKey)){
					//retrieve the data provider
					dataProviderDTO = dataResourceManager.getDataProviderFor(dataProviderKey);
					if(dataProviderDTO==null)
						return redirectToDefaultView();				
					//get the root taxon concepts
					concepts= taxonomyManager.getRootTaxonConceptsForTaxonomy(dataProviderKey, null);
				} else if (StringUtils.isNotEmpty(dataResourceKey)){
					//retrieve the data resource
					dataResourceDTO = dataResourceManager.getDataResourceFor(dataResourceKey);
					if(dataResourceDTO==null)
						return redirectToDefaultView();
					//retrieve the data provider
					dataProviderDTO = dataResourceManager.getDataProviderFor(dataResourceDTO.getDataProviderKey());
					//get the root taxon concepts
					concepts= taxonomyManager.getRootTaxonConceptsForTaxonomy(null, dataResourceKey);
				} else {
					//use the default taxonomy - treat this as a DataProvider for now
					//retrieve the data provider
					dataProviderDTO = dataResourceManager.getNubDataProvider();
					//get the root taxon concepts
					if(dataProviderDTO!=null)
						concepts= taxonomyManager.getRootTaxonConceptsForTaxonomy(dataProviderDTO.getKey(), null);
				}
				//if only one child descend tree until there is a choice
				if(concepts!=null && concepts.size()==1){
					String currentConceptKey = concepts.get(0).getKey();
					List<BriefTaxonConceptDTO> childConcepts= taxonomyManager.getChildConceptsFor(currentConceptKey, allowUnconfirmed);
					concepts.addAll(childConcepts);
					int noOfChildConcepts = childConcepts.size();
					while(noOfChildConcepts==1){
						BriefTaxonConceptDTO currentConcept = childConcepts.get(0);
						currentConceptKey = childConcepts.get(0).getKey();
						childConcepts= taxonomyManager.getChildConceptsFor(currentConceptKey, allowUnconfirmed);
						noOfChildConcepts = childConcepts.size();
						if(noOfChildConcepts>1)
							taxonConceptUtils.organiseUnconfirmedNames(request, currentConcept, concepts, childConcepts, taxonPriorityThreshold);
						else
							concepts.addAll(childConcepts);					
					}
				}
			} else {
				//get the selected concept
				selectedConcept = taxonomyManager.getTaxonConceptFor(taxonConceptKey);
				if(selectedConcept==null)
					return redirectToDefaultView();
				//retrieve the data provider
				dataProviderDTO = dataResourceManager.getDataProviderFor(selectedConcept.getDataProviderKey());
				//retrieve the data resource
				if(selectedConcept.getDataResourceKey()!=null)
					dataResourceDTO = dataResourceManager.getDataResourceFor(selectedConcept.getDataResourceKey());
				
				
				//get high level concepts first
				concepts = taxonomyManager.getClassificationFor(taxonConceptKey, false, null, allowUnconfirmed);
				
				//get child concepts
				List<BriefTaxonConceptDTO> childConcepts = taxonomyManager.getChildConceptsFor(taxonConceptKey, allowUnconfirmed);
				
				//organised the child concepts
				taxonConceptUtils.organiseUnconfirmedNames(request, selectedConcept, concepts, childConcepts, taxonPriorityThreshold);
				
				mav.addObject(taxonConceptModelKey, selectedConcept);
				
				//add occurrence search criteria - Search for Occurrences of
				CriteriaDTO occurrenceCriteria = filterContentProvider.getOccurrenceSearchCriteria(selectedConcept);
				if(occurrenceCriteria!=null)
					mav.addObject(occurrenceCriteriaModelKey, occurrenceCriteria);

				//add taxonomy search criteria- Compare classifications of
				CriteriaDTO taxonomyCriteria = filterContentProvider.getTaxonomySearchCriteria(selectedConcept);
				mav.addObject(taxonomyCriteriaModelKey, taxonomyCriteria);
			}
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			redirectToDefaultView();
		}

		TaxonRankType topRank = null;
		//add highest rank - for the reset link
		if(dataResourceDTO!=null && !dataResourceDTO.isSharedTaxonomy())
			topRank = taxonomyManager.getRootConceptRankForTaxonomy(null, dataResourceDTO.getKey());
		else if(dataProviderDTO!=null)
			topRank = taxonomyManager.getRootConceptRankForTaxonomy(dataProviderDTO.getKey(), null);
		
		if(topRank!=null)
			mav.addObject(highestRankModelKey, topRank.getName());
		
		if(dataProviderDTO!=null)		
			mav.addObject(dataProviderModelKey, dataProviderDTO);
		
		if(dataResourceDTO!=null)
			mav.addObject(dataResourceModelKey, dataResourceDTO);
		
		if(concepts!=null)
			mav.addObject(conceptsModelKey, concepts);

		//taxon priority threshold
		mav.addObject(taxonPriorityThresholdModelKey, taxonPriorityThreshold);
		mav.addObject(messageSourceModelKey, messageSource);
		return mav;
	}

	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}

	/**
	 * @param taxonomyManager the taxonomyManager to set
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
	}

	/**
	 * @param conceptsModelKey the conceptsModelKey to set
	 */
	public void setConceptsModelKey(String conceptsModelKey) {
		this.conceptsModelKey = conceptsModelKey;
	}

	/**
	 * @param dataResourceKey the dataResourceKey to set
	 */
	public void setDataResourcePropertyKey(String dataResourcePropertyKey) {
		this.dataResourcePropertyKey = dataResourcePropertyKey;
	}

	/**
	 * @param dataResourceModelKey the dataResourceModelKey to set
	 */
	public void setDataResourceModelKey(String dataResourceModelKey) {
		this.dataResourceModelKey = dataResourceModelKey;
	}

	/**
	 * @param selectedConceptModelKey the selectedConceptModelKey to set
	 */
	public void setTaxonConceptModelKey(String taxonConceptModelKey) {
		this.taxonConceptModelKey = taxonConceptModelKey;
	}

	/**
	 * @param taxonConceptKey the taxonConceptKey to set
	 */
	public void setTaxonConceptPropertyKey(String taxonConceptPropertyKey) {
		this.taxonConceptPropertyKey = taxonConceptPropertyKey;
	}

	/**
	 * @param dataProviderModelKey the dataProviderModelKey to set
	 */
	public void setDataProviderModelKey(String dataProviderModelKey) {
		this.dataProviderModelKey = dataProviderModelKey;
	}

	/**
	 * @param dataProviderPropertyKey the dataProviderPropertyKey to set
	 */
	public void setDataProviderPropertyKey(String dataProviderPropertyKey) {
		this.dataProviderPropertyKey = dataProviderPropertyKey;
	}

	/**
	 * @param dataProvidersModelKey the dataProvidersModelKey to set
	 */
	public void setDataProvidersModelKey(String dataProvidersModelKey) {
		this.dataProvidersModelKey = dataProvidersModelKey;
	}

	/**
	 * @param dataResourcesModelKey the dataResourcesModelKey to set
	 */
	public void setDataResourcesModelKey(String dataResourcesModelKey) {
		this.dataResourcesModelKey = dataResourcesModelKey;
	}

	/**
	 * @param filterContentProvider the filterContentProvider to set
	 */
	public void setFilterContentProvider(FilterContentProvider filterContentProvider) {
		this.filterContentProvider = filterContentProvider;
	}

	/**
	 * @param highestRankModelKey the highestRankModelKey to set
	 */
	public void setHighestRankModelKey(String highestRankModelKey) {
		this.highestRankModelKey = highestRankModelKey;
	}

	/**
	 * @param occurrenceCriteriaModelKey the occurrenceCriteriaModelKey to set
	 */
	public void setOccurrenceCriteriaModelKey(String occurrenceCriteriaModelKey) {
		this.occurrenceCriteriaModelKey = occurrenceCriteriaModelKey;
	}

	/**
	 * @param nubProviderModelKey the nubProviderModelKey to set
	 */
	public void setNubProviderModelKey(String nubProviderModelKey) {
		this.nubProviderModelKey = nubProviderModelKey;
	}

	/**
	 * @param taxonomyCriteriaModelKey the taxonomyCriteriaModelKey to set
	 */
	public void setTaxonomyCriteriaModelKey(String taxonomyCriteriaModelKey) {
		this.taxonomyCriteriaModelKey = taxonomyCriteriaModelKey;
	}

	/**
	 * @param taxonPriorityThreshold the taxonPriorityThreshold to set
	 */
	public void setTaxonPriorityThreshold(int taxonPriorityThreshold) {
		this.taxonPriorityThreshold = taxonPriorityThreshold;
	}

	/**
	 * @param taxonPriorityThresholdModelKey the taxonPriorityThresholdModelKey to set
	 */
	public void setTaxonPriorityThresholdModelKey(
			String taxonPriorityThresholdModelKey) {
		this.taxonPriorityThresholdModelKey = taxonPriorityThresholdModelKey;
	}

	/**
	 * @param taxonConceptUtils the taxonConceptUtils to set
	 */
	public void setTaxonConceptUtils(TaxonConceptUtils taxonConceptUtils) {
		this.taxonConceptUtils = taxonConceptUtils;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @param messageSourceModelKey the messageSourceModelKey to set
	 */
	public void setMessageSourceModelKey(String messageSourceModelKey) {
		this.messageSourceModelKey = messageSourceModelKey;
	}

	/**
   * @param allowUnconfirmed the allowUnconfirmed to set
   */
  public void setAllowUnconfirmed(boolean allowUnconfirmed) {
  	this.allowUnconfirmed = allowUnconfirmed;
  }
}