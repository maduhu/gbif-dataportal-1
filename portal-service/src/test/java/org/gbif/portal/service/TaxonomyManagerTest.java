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
package org.gbif.portal.service;

import java.util.List;

import junit.framework.TestCase;

import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.dto.taxonomy.TaxonConceptDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.dto.util.TaxonRankType;

/**
 * Junit tests for TaxonomyManager implementations.
 * 
 * @see TaxonomyManager
 * 
 * @author dmartin
 */
public class TaxonomyManagerTest extends AbstractServiceTest {

	public void testGetTaxonConceptFor() throws Exception {
		TaxonomyManager taxonomyManager = (TaxonomyManager) getBean("taxonomyManager");
		TaxonConceptDTO taxonConceptDTO = taxonomyManager.getTaxonConceptFor("1");
		logger.info("Retrieved taxon concept :" + taxonConceptDTO);
		try {
			taxonConceptDTO = taxonomyManager.getTaxonConceptFor("xsd");
			TestCase.fail("Should throw illegal argument exception");
		} catch (Exception e){
			//success
		}
	}

	public void testGetTaxonConceptForRemoteId() throws Exception {
		TaxonomyManager taxonomyManager = (TaxonomyManager) getBean("taxonomyManager");
		List<TaxonConceptDTO> taxonConceptDTOs = taxonomyManager.getTaxonConceptForRemoteId("ITS-196475");
		logger.info("Retrieved taxon concepts :" + taxonConceptDTOs);
		if(taxonConceptDTOs!= null && taxonConceptDTOs.size()>0)
			logger.info("First Retrieved taxon concept :" + taxonConceptDTOs.get(0));			
	}	
	
	public void testGetParentConceptFor() throws Exception {
		TaxonomyManager taxonomyManager = (TaxonomyManager) getBean("taxonomyManager");
		TaxonConceptDTO parentConceptDTO = taxonomyManager.getParentConceptFor("2");
		logger.info("Retrieved parent concept with:" + parentConceptDTO);
	}
	
	public void testGetChildConceptsFor() throws Exception {
		TaxonomyManager taxonomyManager = (TaxonomyManager) getBean("taxonomyManager");
		List<BriefTaxonConceptDTO> dtoList = taxonomyManager.getChildConceptsFor("1", true);
		logger.info("Retrieved list size :" + dtoList.size());
	}	

	public void testGetNubConceptFor() throws Exception {
		TaxonomyManager taxonomyManager = (TaxonomyManager) getBean("taxonomyManager");
		TaxonConceptDTO nubConceptDTO = taxonomyManager.getNubTaxonConceptFor("2");
		logger.info("Retrieved nub concept with:" + nubConceptDTO);
	}	
	
	public void testRootConceptsForCountry() throws Exception {
		TaxonomyManager taxonomyManager = (TaxonomyManager) getBean("taxonomyManager");
		List<BriefTaxonConceptDTO> dtoList = taxonomyManager.getRootTaxonConceptsForCountry("AU");
		dtoList = taxonomyManager.getRootTaxonConceptsForTaxonomy("1", null);
		logger.info("Retrieved root concepts for:" + dtoList.size());
		if(dtoList.size()>0){
			logger.info("First root concept:" + dtoList.get(0));			
		}
	}
	
	public void testRootConceptsForTaxonomy() throws Exception {
		TaxonomyManager taxonomyManager = (TaxonomyManager) getBean("taxonomyManager");
		List<BriefTaxonConceptDTO> dtoList = taxonomyManager.getRootTaxonConceptsForTaxonomy(null,"1");
		dtoList = taxonomyManager.getRootTaxonConceptsForTaxonomy("1", null);
		logger.info("Retrieved root concepts for:" + dtoList.size());
	}		
	
	public void testFindTaxonConceptsWithSameScientificNameAndRankAs() throws Exception {
		TaxonomyManager taxonomyManager = (TaxonomyManager) getBean("taxonomyManager");
		SearchConstraints searchConstraints = new SearchConstraints(0, 8);
		SearchResultsDTO searchResultsDTO = taxonomyManager.findTaxonConceptsWithSameScientificNameAndRankAs("3", "1", null, searchConstraints);
		logger.info("Retrieved results size:" + searchResultsDTO.getResults().size());
		if(searchResultsDTO.getResults().size()>0){
			logger.info("First result:" + searchResultsDTO.getResults().get(0));
		}
		
		searchResultsDTO = taxonomyManager.findTaxonConceptsWithSameScientificNameAndRankAs("1", null, null, searchConstraints);
		logger.info("Retrieved results size:" + searchResultsDTO.getResults().size());
		if(searchResultsDTO.getResults().size()>0){
			logger.info("First result:" + searchResultsDTO.getResults().get(0));
		}
	}	
	
	public void testGetClassificationFor() throws Exception {
		TaxonomyManager taxonomyManager = (TaxonomyManager) getBean("taxonomyManager");
		List<BriefTaxonConceptDTO> concepts = taxonomyManager.getClassificationFor("19", true, true, null, false, true);
		if(concepts!=null){
			for (BriefTaxonConceptDTO concept: concepts)
				logger.info("Retrieved concept:" + concept);
		}

		concepts = taxonomyManager.getClassificationFor("16", true, true, null, false, true);
		if(concepts!=null){			
			for (BriefTaxonConceptDTO concept: concepts)
				logger.info("Retrieved concept:" + concept);
		}
		
		concepts = taxonomyManager.getClassificationFor("348799", true, false, null, false, true);
		
		
		concepts = taxonomyManager.getClassificationFor("9886973", true, true, null, false, true);
		if(concepts!=null)
			logger.info("Number of concept:"+concepts.size());
		concepts = taxonomyManager.getClassificationFor("9886973", true, true, "AU", false, true);
		if(concepts!=null)
			logger.info("Number of concept:"+concepts.size());
	}	
	
	public void testFindTaxonConcepts() throws Exception {
		TaxonomyManager taxonomyManager = (TaxonomyManager) getBean("taxonomyManager");
		SearchConstraints searchConstraints = new SearchConstraints(0, 10);
		SearchResultsDTO searchResults = taxonomyManager.findTaxonConcepts("Anim*", true, null, null, null, null, null, null, null, true, false, searchConstraints);
		logger.info("Retrieved results:" + searchResults.getResults().size());
		searchResults = taxonomyManager.findTaxonConcepts("Animalia", false, "kingdom", null, null, null, null, null, null, true, false, searchConstraints);
		logger.info("Retrieved results:" + searchResults.getResults().size());
		searchResults = taxonomyManager.findTaxonConcepts("Animalia", true, "1", null, null, null, null, null, null, true, false, searchConstraints);
		logger.info("Retrieved results:" + searchResults.getResults().size());
		searchResults = taxonomyManager.findTaxonConcepts("Animalia", true,  null, "1", null, null, null, null, null, true, false, searchConstraints);
		logger.info("Retrieved results:" + searchResults.getResults().size());		
		searchResults = taxonomyManager.findTaxonConcepts(null, false,  "1", null, null, null, null, null, null, true, false, searchConstraints);
		logger.info("Retrieved results:" + searchResults.getResults().size());		
	}		
	
	public void testFindSpeciesConcepts() throws Exception {
		TaxonomyManager taxonomyManager = (TaxonomyManager) getBean("taxonomyManager");
		SearchConstraints searchConstraints = new SearchConstraints(0, 10);
		SearchResultsDTO searchResults = taxonomyManager.findSpeciesConcepts(null, "aluco", true, null, null, null, searchConstraints);
		logger.info("Retrieved results:" + searchResults.getResults().size());
	}

	public void testFindTaxonConceptsForCommonName() throws Exception {
		TaxonomyManager taxonomyManager = (TaxonomyManager) getBean("taxonomyManager");
		SearchConstraints searchConstraints = new SearchConstraints(0, 10);
		SearchResultsDTO searchResults = taxonomyManager.findTaxonConceptsForCommonName("A*", true, searchConstraints);
		logger.info("Retrieved results:" + searchResults.getResults().size());
//		searchResults = taxonomyManager.findTaxonConcepts("Animalia", false, "kingdom", null, null, null, null, null, searchConstraints);
//		logger.info("Retrieved results:" + searchResults.getResults().size());
//		searchResults = taxonomyManager.findTaxonConcepts("Animalia", true, "1", null, null, null, null, null, searchConstraints);
//		logger.info("Retrieved results:" + searchResults.getResults().size());
//		searchResults = taxonomyManager.findTaxonConcepts("Animalia", true,  null, null, "1", null, null, null, searchConstraints);
//		logger.info("Retrieved results:" + searchResults.getResults().size());		
//		searchResults = taxonomyManager.findTaxonConcepts(null, false,  null, null, null, "1", null, null, searchConstraints);
//		logger.info("Retrieved results:" + searchResults.getResults().size());		
	}	
	
	public void testCountTaxonConcepts() throws Exception {
		TaxonomyManager taxonomyManager = (TaxonomyManager) getBean("taxonomyManager");
		Long count = taxonomyManager.countTaxonConcepts("Anima*", true, "kingdom", null, null, null, null, null);
		logger.info("Retrieved results: " + count);
		count = taxonomyManager.countTaxonConcepts(null, false, "phylum", "1", null, null, null, null);
		logger.info("Retrieved results: " + count);
	}		

	public void testGetTotalTaxonConceptCount() throws Exception {
		TaxonomyManager taxonomyManager = (TaxonomyManager) getBean("taxonomyManager");
		logger.info("Retrieved total count:" +taxonomyManager.getTotalTaxonConceptCount());
	}
	
	public void testGetTaxonConceptCount() throws Exception {
		TaxonomyManager taxonomyManager = (TaxonomyManager) getBean("taxonomyManager");		
		logger.info("Retrieved total count:" +taxonomyManager.getTaxonConceptCount("Animalia",true, "1",null));		
		logger.info("Retrieved total count:" +taxonomyManager.getTaxonConceptCount("Animalia",true, null,"1"));				
		logger.info("Retrieved total count:" +taxonomyManager.getTaxonConceptCount("Animalia",true, null,null));				
		logger.info("Retrieved total count:" +taxonomyManager.getTaxonConceptCount("Animalia",false, null, null));						
	}
	
	public void testGetTaxonConceptCountForRank() throws Exception {
		TaxonomyManager taxonomyManager = (TaxonomyManager) getBean("taxonomyManager");
		logger.info("Number of Kingdoms:" +taxonomyManager.getTaxonConceptCountForRank(TaxonRankType.KINGDOM_STR, null, "1", null));			
		logger.info("Number of Kingdoms:" +taxonomyManager.getTaxonConceptCountForRank(TaxonRankType.KINGDOM_STR, null, null, "1"));			
		logger.info("Number of Phyla:" +taxonomyManager.getTaxonConceptCountForRank(TaxonRankType.PHYLUM_STR, null, "1", null));			
		//logger.info("Number of Genera:" +taxonomyManager.getTaxonConceptCountForRank(TaxonRankType.GENUS, null, null, null));						
		//logger.info("Number of Species:" +taxonomyManager.getTaxonConceptCountForRank(TaxonRankType.SPECIES, null, null, null));						
		//logger.info("Number of SubSpecies:" +taxonomyManager.getTaxonConceptCountForRank("subspecies", null, null, null));						
	}
	
	public void testGetRootConceptRankForTaxonomy() throws Exception {
		TaxonomyManager taxonomyManager = (TaxonomyManager) getBean("taxonomyManager");
		logger.info("Root Concept Rank:" +taxonomyManager.getRootConceptRankForTaxonomy("1", null));			
		logger.info("Root Concept Rank:" +taxonomyManager.getRootConceptRankForTaxonomy("1", "1"));			
		logger.info("Root Concept Rank:" +taxonomyManager.getRootConceptRankForTaxonomy(null, "1"));			
	}	
	
	public void testFindMatchingScientificNames() throws Exception {
		TaxonomyManager taxonomyManager = (TaxonomyManager) getBean("taxonomyManager");
		SearchResultsDTO resultsDTO = taxonomyManager.findMatchingScientificNames("Animals", true, null, null, true, null, null, false, new SearchConstraints());
		logger.info("Search results:" +resultsDTO.getResults().size());
		if(resultsDTO.getResults().size()>0){
			logger.info("First Results:" +resultsDTO.getResults().get(0));
		}
	}
	
	public void testFindMatchingCommonNames() throws Exception {
		TaxonomyManager taxonomyManager = (TaxonomyManager) getBean("taxonomyManager");
		SearchResultsDTO resultsDTO = taxonomyManager.findMatchingCommonNames("A", true, new SearchConstraints(0, 10));
		logger.info("Search results:" +resultsDTO.getResults().size());
		if(resultsDTO.getResults().size()>0){
			logger.info("First Results:" +resultsDTO.getResults().get(0));
		}
	}
	
	public void testfindImagesFor() throws Exception {
		TaxonomyManager taxonomyManager = (TaxonomyManager) getBean("taxonomyManager");
		SearchResultsDTO resultsDTO = taxonomyManager.findImagesFor("993565", new SearchConstraints(0, 1));
		logger.info("Search results:" +resultsDTO.getResults().size());
		if(resultsDTO.getResults().size()>0){
			logger.info("First Results:" +resultsDTO.getResults().get(0));
		}
	}
	
	public void testCountChildConcept() throws Exception {
		TaxonomyManager taxonomyManager = (TaxonomyManager) getBean("taxonomyManager");
		int count = taxonomyManager.countChildConceptsFor("1", TaxonRankType.ORDER, true, false, false);
		logger.debug("Concept count: "+count);
	}
	
	public void findRemoteUrlFor() throws Exception {
		TaxonomyManager taxonomyManager = (TaxonomyManager) getBean("taxonomyManager");
		List<String> urls = taxonomyManager.findRemoteUrlFor("1");
		logger.debug("Url count: "+urls.size());
	}	
}