/***************************************************************************
 * Copyright (C) 2006 Global Biodiversity Information Facility Secretariat.  
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

package org.gbif.portal.webservices.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.dto.taxonomy.CommonNameDTO;
import org.gbif.portal.dto.taxonomy.RelationshipAssertionDTO;
import org.gbif.portal.dto.taxonomy.TaxonConceptDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.webservices.util.GbifWebServiceException;

/**
 * @author
 *
 */
public class TaxonAction extends Action {

	/* (non-Javadoc)
	 * @see org.gbif.portal.service.TaxonomyManager
	 */
	protected TaxonomyManager taxonomyManager;

	/* (non-Javadoc)
	 * @see org.gbif.portal.service.DataResourceManager
	 */
	protected DataResourceManager dataResourceManager;


	public static Log log = LogFactory.getLog(TaxonAction.class);

	/**
	 * Gets the template of the Taxon Action.
	 * 
	 * @param parameterMap
	 * @return String with the template
	 */
	public String getTemplate(Map<String,Object> parameterMap)
	{
		TaxonParameters params = null;
		
		try {
			params = new TaxonParameters(parameterMap, pathMapping);
			
			switch (params.getRequestType()) {
			case LIST:
				return "org/gbif/portal/ws/taxon/taxon.vm";
			case GET:
				return "org/gbif/portal/ws/taxon/taxon-get.vm";
			case COUNT:
					return "org/gbif/portal/ws/taxon/taxon-count.vm";
			case HELP:
				return "org/gbif/portal/ws/taxon/taxon-count.vm";					
			default:
				return null;
			}
		} catch (Exception se) {
			log.error("Unregistered data service error: " + se.getMessage(), se);
			if (params == null)
				return null;//gbifMappingFactory.getGbifResponseDocument(parameterMap, se);
			else
				return null;//gbifMappingFactory.getGbifResponseDocument(params, se);
		}
		
	}
	
	/**
	 * Counts the number of taxon records
	 * 
	 * @param params
	 * @return number of taxon records
	 * @throws GbifWebServiceException
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> countTaxonRecords(TaxonParameters params) 
	throws GbifWebServiceException {
		
		Map<String,Object> results = new HashMap<String,Object>();
		
		Map<String,String> headerMap;
		Map<String,String> parameterMap;	
		Map<String, String> summaryMap=null;
				
		headerMap = returnHeader(params,true);
		parameterMap = returnParameters(params.getParameterMap(null));			
		
		try {
			String dataResourceKey = params.getDataResourceKey();
			
			if (dataResourceKey != null && dataResourceKey.equalsIgnoreCase("PORTAL")) {
				List<DataResourceDTO> dataResources = (List<DataResourceDTO>) 
					dataResourceManager.findDataResources("ECAT Taxonomy", true, null, null, null,
														  new SearchConstraints(0, 1)).getResults();
				if (dataResources == null || dataResources.size() == 0) {
					throw new GbifWebServiceException("Could not retrieve ECAT taxonomy");
				}
				dataResourceKey = dataResources.get(0).getKey();
			}
			String searchString = params.getScientificName();
			boolean fuzzy = false;
			if (searchString != null) {
				int index = searchString.indexOf("*");
				if (index < 0)
					index = searchString.indexOf("%");
				if (index >= 0) {
					fuzzy = true;
					searchString = searchString.substring(0, index);
				}
			}	
			Long recordCount = taxonomyManager.countTaxonConcepts(
					searchString,
					fuzzy,
					params.getRank(),
					params.getDataProviderKey(), 
					dataResourceKey, 
					params.getResourceNetworkKey(), 
					params.getHostIsoCountryCode(),
					params.getModifiedSince());	
			
			summaryMap = returnSummary(recordCount);
			
			results.put("headerMap", headerMap);
			results.put("parameterMap", parameterMap);	
			results.put("summaryMap", summaryMap);
			results.put("count", recordCount);
			return results;
			
		} catch (ServiceException se) {
			log.error("Unregistered data service error: " + se.getMessage(), se);
			throw new GbifWebServiceException("Data service problems - " + se.getMessage());
		}
		catch (Exception se) {
			log.error("Data service error: " + se.getMessage(), se);
			throw new GbifWebServiceException("Data service problems - " + se.toString());
		}			
	}

		
	/**
	 * Returns the taxon record, as a product of a search for a taxon key or a common name
	 * 
	 * @param params
	 * @return the taxon record
	 * @throws GbifWebServiceException
	 */
	public Map<String,Object> getTaxonRecord(TaxonParameters params) throws GbifWebServiceException {
		
		//overall results
		Map<String,Object> results = new HashMap<String,Object>();
		
		Map<String,String> headerMap;
		Map<String,String> parameterMap;
		Map<String, String> summaryMap=null;
		
		headerMap = returnHeader(params,true);
		parameterMap = returnParameters(params.getParameterMap(null));
		
		//creates the set of TaxonConcept maps
		List<Map<String, Object>> taxonConceptMapSet = new ArrayList<Map<String, Object>>();		
		
		TaxonConceptDTO dto = null;
		
		//set that stores all the relationships of the original taxon
		List<Map<String,String>> relationshipMapSet = new ArrayList<Map<String,String>>(); 
		//Map<String, Map<String, Set<TaxonConceptDTO>>> mapDTO = new HashMap<String, Map<String, Set<TaxonConceptDTO>>>();
		
		try {
			if (params.getKey().startsWith("c-")) {
				CommonNameDTO cnDTO = taxonomyManager.getCommonNameFor(params.getKey().substring(2));
				String taxonConceptKey = cnDTO.getTaxonConceptKey();
				dto = taxonomyManager.getTaxonConceptFor(taxonConceptKey);
			} else {
				dto = taxonomyManager.getTaxonConceptFor(params.getKey());
			}
			
			List<TaxonConceptDTO> set = new ArrayList<TaxonConceptDTO>();
			set.add(dto);
			summaryMap = returnSummary(params, set, true);
			
			//pass the taxon concept DTO to template
			results.put("taxonConceptDTO", dto);
			
			//1) Iterate up to the root, while adding all the intermediate taxons to the set of TaxonConceptMap
			TaxonConceptDTO parent;
			String parentKey=dto.getParentConceptKey();
			String currentKey=dto.getKey();
			
			TaxonConceptDTO parentConceptDTO = taxonomyManager.getParentConceptFor(currentKey);
			
			//add the relationship of the inmediate parent
			if(parentKey!=null)
			{				
				Map<String,String> relationshipMap = new HashMap<String,String>();		 //relationship map				
				//attributes for the parent's relationship are added
				relationshipMap.put("taxonResource", params.getGetUrl("taxon", parentKey));
				relationshipMap.put("relationshipResource", "http://rs.tdwg.org/ontology/voc/TaxonConcept#IsChildTaxonOf");
				relationshipMapSet.add(relationshipMap);	
			}
			
			//add all the taxon concepts of the parents
			while (parentConceptDTO!=null)
			{				
				Map<String,Object> taxonConceptMap = new HashMap<String,Object>();		 //add a new taxon concept map
				
				//the parent is being created as a new TaxonConceptDTO
				//parent = taxonomyManager.getParentConceptFor(currentKey);
				
				//adds the parent taxon to the list of taxons
				taxonConceptMap.put("taxonConceptDTO", parentConceptDTO);
				if(parentConceptDTO.getParentConceptKey()!=null)
				{
					Map<String,String> relationshipMapForParentTaxon = new HashMap<String,String>();		 //relationship map
					relationshipMapForParentTaxon.put("taxonResource", params.getGetUrl("taxon", parentConceptDTO.getParentConceptKey()));
					relationshipMapForParentTaxon.put("relationshipResource", "http://rs.tdwg.org/ontology/voc/TaxonConcept#IsChildTaxonOf");
					taxonConceptMap.put("relationshipMap", relationshipMapForParentTaxon);
				}
				
				//add the taxonConcept map to the list of TCs
				taxonConceptMapSet.add(taxonConceptMap);
				
				//set the current key to the parent's key
				currentKey = parentConceptDTO.getKey();
				
				//get the parent's parent (if any)
				parentConceptDTO = taxonomyManager.getParentConceptFor(parentConceptDTO.getKey());
			}
			
			//2) Iterate to include all the taxon's children
			List<BriefTaxonConceptDTO> relatedConcepts = taxonomyManager.getChildConceptsFor(dto.getKey(), true);
			
			for (BriefTaxonConceptDTO relatedConcept : relatedConcepts) {
				TaxonConceptDTO relatedConceptDTO = taxonomyManager.getTaxonConceptFor(relatedConcept.getKey());
								
				//add the related taxon to the taxon list
				Map<String,Object> taxonConceptMap = new HashMap<String,Object>();		 //add a new taxon concept map
				Map<String,String> relationshipMapForRelatedTaxon = new HashMap<String,String>();		 //relationship map	for the related taxon concept DTO
				relationshipMapForRelatedTaxon.put("taxonResource", params.getGetUrl("taxon", dto.getKey()));
				relationshipMapForRelatedTaxon.put("relationshipResource", "http://rs.tdwg.org/ontology/voc/TaxonConcept#IsChildTaxonOf");
				taxonConceptMap.put("taxonConceptDTO", relatedConceptDTO);
				taxonConceptMap.put("relationshipMap", relationshipMapForRelatedTaxon);
				
				taxonConceptMapSet.add(taxonConceptMap);
				
				//relationship map for the original taxon
				Map<String,String> relationshipMap = new HashMap<String,String>();	
				relationshipMap.put("taxonResource", params.getGetUrl("taxon", relatedConcept.getKey()));
				relationshipMap.put("relationshipResource", "http://rs.tdwg.org/ontology/voc/TaxonConcept#IsParentTaxonOf");
				relationshipMapSet.add(relationshipMap);
			}
			
			//3) Iterate to include all the FROM RelationshipAssertions
			List<RelationshipAssertionDTO> fromAssertions = taxonomyManager.findRelationshipAssertionsForFromTaxonConcept(dto.getKey());

			for (RelationshipAssertionDTO fromAssertion : fromAssertions) {
				if (fromAssertion.getRelationshipType() == RelationshipAssertionDTO.RELATIONSHIP_TYPE_SYNONYM
						 || fromAssertion.getRelationshipType() == RelationshipAssertionDTO.RELATIONSHIP_TYPE_AMBIGUOUS_SYNONYM
						 || fromAssertion.getRelationshipType() == RelationshipAssertionDTO.RELATIONSHIP_TYPE_MISAPPLIED_NAME) {
					TaxonConceptDTO relatedConceptDTO = taxonomyManager.getTaxonConceptFor(fromAssertion.getToTaxonConceptKey());
										
					//add the related taxon to the taxon list
					Map<String,Object> taxonConceptMap = new HashMap<String,Object>();		 //add a new taxon concept map
					taxonConceptMap.put("taxonConceptDTO", relatedConceptDTO);
					taxonConceptMapSet.add(taxonConceptMap);
					
					//relationship map for the original taxon
					Map<String,String> relationshipMap = new HashMap<String,String>();
					relationshipMap.put("taxonResource", params.getGetUrl("taxon", relatedConceptDTO.getKey()));
					relationshipMap.put("relationshipResource", "http://rs.tdwg.org/ontology/voc/TaxonConcept#IsIncludedIn");
					relationshipMapSet.add(relationshipMap);
				}
			}
			
			//4) Iterate to include all the TO RelationshipAssertions
			List<RelationshipAssertionDTO> toAssertions = taxonomyManager.findRelationshipAssertionsForToTaxonConcept(dto.getKey());

			for (RelationshipAssertionDTO toAssertion : toAssertions) {
				if (toAssertion.getRelationshipType() == RelationshipAssertionDTO.RELATIONSHIP_TYPE_SYNONYM
						 || toAssertion.getRelationshipType() == RelationshipAssertionDTO.RELATIONSHIP_TYPE_AMBIGUOUS_SYNONYM
						 || toAssertion.getRelationshipType() == RelationshipAssertionDTO.RELATIONSHIP_TYPE_MISAPPLIED_NAME) {
					TaxonConceptDTO relatedConceptDTO = taxonomyManager.getTaxonConceptFor(toAssertion.getFromTaxonConceptKey());
										
					//add the related taxon to the taxon list
					Map<String,Object> taxonConceptMap = new HashMap<String,Object>();		 //add a new taxon concept map
					taxonConceptMap.put("taxonConceptDTO", relatedConceptDTO);
					taxonConceptMapSet.add(taxonConceptMap);
					
					//relationship map for the original taxon
					Map<String,String> relationshipMap = new HashMap<String,String>();
					relationshipMap.put("taxonResource", params.getGetUrl("taxon", relatedConceptDTO.getKey()));
					relationshipMap.put("relationshipResource", "http://rs.tdwg.org/ontology/voc/TaxonConcept#HasSynonym");
					relationshipMapSet.add(relationshipMap);
				}
			}
			
			//5) Iterate to include common names
			List<CommonNameDTO> commonNames = taxonomyManager.findCommonNamesForTaxonConcept(dto.getKey(), new SearchConstraints(0, 1000));
			//TODO: Create all the common names, review the problema again
			
			
			//6) If the taxon is not a nub concept, get the nub taxon concept
			if (!dto.getIsNubConcept()) {
			  
			  // With the new rollover not all concepts are tied to the NUB
			  if (dto.getPartnerConceptKey() != null) {
	        //add the related taxon to the taxon list
	        Map<String,Object> taxonConceptMap = new HashMap<String,Object>();     //add a new taxon concept map
	        TaxonConceptDTO relatedConceptDTO = taxonomyManager.getTaxonConceptFor(dto.getPartnerConceptKey());
	        taxonConceptMap.put("taxonConceptDTO", relatedConceptDTO);
	        taxonConceptMapSet.add(taxonConceptMap);
	        
	        //relationship map for the original taxon
	        Map<String,String> relationshipMap = new HashMap<String,String>();
	        relationshipMap.put("taxonResource", params.getGetUrl("taxon", relatedConceptDTO.getKey()));
	        relationshipMap.put("relationshipResource", "http://rs.tdwg.org/ontology/voc/TaxonConcept#IsIncludedIn");
	        relationshipMapSet.add(relationshipMap);
			  }
			} 
			
			//7) If the taxon is a nub concept, then get all the concepts that are pointing to it
			else {
				List<TaxonConceptDTO> otherConcepts = taxonomyManager.getTaxonConceptsForNubTaxonConcept(dto.getKey());
				
				
				for (TaxonConceptDTO relatedConceptDTO : otherConcepts) {
					
					//add the related taxon to the taxon list
					Map<String,Object> taxonConceptMap = new HashMap<String,Object>();		 //add a new taxon concept map
					taxonConceptMap.put("taxonConceptDTO", relatedConceptDTO);
					taxonConceptMapSet.add(taxonConceptMap);
					
					//relationship map for the original taxon
					Map<String,String> relationshipMap = new HashMap<String,String>();
					relationshipMap.put("taxonResource", params.getGetUrl("taxon", relatedConceptDTO.getKey()));
					relationshipMap.put("relationshipResource", "http://rs.tdwg.org/ontology/voc/TaxonConcept#Includes");
					relationshipMapSet.add(relationshipMap);					
				} 
			}			

			results.put("relationshipMapSet", relationshipMapSet);
			results.put("taxonConceptMapSet", taxonConceptMapSet);
			
			results.put("headerMap", headerMap);
			results.put("parameterMap", parameterMap);
			results.put("summaryMap", summaryMap);
			results.put("count", 1);
			
			//results.put("results", results);
			
			return results;
		}		
		catch (ServiceException se) {
			log.error("Data service error: " + se.getMessage(), se);
			throw new GbifWebServiceException("Data service problems - " + se.getMessage());
		}
		catch (Exception se) {
			log.error("Data service error: " + se.getMessage(), se);
			throw new GbifWebServiceException("Data service problems - " + se.toString());
		}			
	}

	/**
	 * Find the taxon records that match a given criteria and the count of them
	 * 
	 * @param params
	 * @return
	 * @throws GbifWebServiceException
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> findTaxonRecords(TaxonParameters params) 
	throws GbifWebServiceException {
		
		Map<String,Object> results = new HashMap<String,Object>();
		
		Map<String,String> headerMap;
		Map<String,String> parameterMap;	
		Map<String, String> summaryMap=null;
		
		headerMap = returnHeader(params,true);
		parameterMap = returnParameters(params.getParameterMap(null));			
		
		SearchResultsDTO searchResultsDTO = null;
		try {
			String dataResourceKey = params.getDataResourceKey();
			if (dataResourceKey != null && dataResourceKey.equalsIgnoreCase("PORTAL")) {
				List<DataResourceDTO> dataResources = (List<DataResourceDTO>) 
					dataResourceManager.findDataResources("ECAT Taxonomy", true, null, null, null,
														  new SearchConstraints(0, 1)).getResults();
				if (dataResources == null || dataResources.size() == 0) {
					throw new GbifWebServiceException("Could not retrieve ECAT taxonomy");
				}
				dataResourceKey = dataResources.get(0).getKey();
			}
			
			String searchString = params.getScientificName();
			boolean fuzzy = false;
			if (searchString != null) {
				int index = searchString.indexOf("*");
				if (index < 0)
					index = searchString.indexOf("%");
				if (index >= 0) {
					fuzzy = true;
					searchString = searchString.substring(0, index);
				}
			}
						
			 searchResultsDTO = taxonomyManager.findTaxonConcepts(
					searchString,
					fuzzy,
					params.getRank(),
					params.getDataProviderKey(), 
					dataResourceKey, 
					params.getResourceNetworkKey(), 
					params.getHostIsoCountryCode(),
					null, //
					params.getModifiedSince(),
					true, //allow unconfirmed names
					false, //sortAlphabetically
					params.getSearchConstraints()); 
			 
			 if(searchResultsDTO==null)
			 {
				 DataProviderDTO  nubDataProvider = dataResourceManager.getNubDataProvider();
				 searchResultsDTO = taxonomyManager.findTaxonConcepts(
							searchString,
							fuzzy,
							params.getRank(),
							nubDataProvider.getKey(), 
							dataResourceKey, 
							params.getResourceNetworkKey(), 
							params.getHostIsoCountryCode(),
							null, //
							params.getModifiedSince(),
							true, //allow unconfirmed names
							false, //sortAlphabetically
							params.getSearchConstraints()); 
			 }
			 
			 
			 
			 

			summaryMap = returnSummary(params, searchResultsDTO, true); 
			 
			List<TaxonConceptDTO> resultsAsTC;
			
			if(searchResultsDTO!=null)
				resultsAsTC = (List<TaxonConceptDTO>) searchResultsDTO.getResults();
			else
				resultsAsTC = new ArrayList<TaxonConceptDTO>();
			
			
			Map<String, Map<String, List<TaxonConceptDTO>>> groupedResults = groupByProviderResource(resultsAsTC);
			
			results.put("results", groupedResults);
			if(searchResultsDTO!=null)
				results.put("count", searchResultsDTO.getResults().size());
			else
				results.put("count", 0);
			
			results.put("headerMap", headerMap);
			results.put("summaryMap", summaryMap);
			results.put("parameterMap", parameterMap);				
			
			return results;
			
		} catch (ServiceException se) {
			log.error("Unregistered data service error: " + se.getMessage(), se);
			throw new GbifWebServiceException("Data service problems - " + se.getMessage());
		}
		catch (Exception se) {
			log.error("Data service error: " + se.getMessage(), se);
			if(se!=null)
				throw new GbifWebServiceException("Data service problems - " + se.toString());
			else
				throw new GbifWebServiceException("Data service problems - NullPointerException");			
		}	
	}
	
	/**
	 * Groups the results into resources and then providers
	 * 
	 * @param results
	 *            The taxons to group
	 * @return The grouped taxons
	 */
	protected Map<String, Map<String, List<TaxonConceptDTO>>> groupByProviderResource(
			List<TaxonConceptDTO> results) {
		// looks nasty but is taxons grouped by resources grouped by
		// providers....
		Map<String, Map<String, List<TaxonConceptDTO>>> providers = new HashMap<String, Map<String, List<TaxonConceptDTO>>>();
		
		for (TaxonConceptDTO tc : results) {
			// get the resources map or create new one
			Map<String, List<TaxonConceptDTO>> resources = null;
			
			if (providers.containsKey(tc.getDataProviderKey())) {
				resources = providers.get(tc.getDataProviderKey());
			} else {
				resources = new HashMap<String, List<TaxonConceptDTO>>();
				providers.put(tc.getDataProviderKey(), resources);
			}

			// get the taxon set or create new one
			List<TaxonConceptDTO> taxa = null;
			if (resources.containsKey(tc.getDataResourceKey())) {
				taxa = resources.get(tc.getDataResourceKey());
			} else {
				taxa = new ArrayList<TaxonConceptDTO>();
				resources.put(tc.getDataResourceKey(), taxa);
			}

			// add the occurrence
			taxa.add(tc);
		}

		return providers;
	}	

	/**
	 * @return the dataResourceManager
	 */
	public DataResourceManager getDataResourceManager() {
		return dataResourceManager;
	}

	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}

	/**
	 * @return the taxonomyManager
	 */
	public TaxonomyManager getTaxonomyManager() {
		return taxonomyManager;
	}

	/**
	 * @param taxonomyManager the taxonomyManager to set
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
	}
}
