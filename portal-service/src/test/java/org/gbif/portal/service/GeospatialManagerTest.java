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
import java.util.Locale;

import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.geospatial.CellDensityDTO;
import org.gbif.portal.dto.geospatial.CountryDTO;
import org.gbif.portal.dto.util.EntityType;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.util.request.IPUtils;

/**
 * Junit tests for TaxonomyManager implementations.
 * 
 * @see TaxonomyManager
 * 
 * @author dmartin
 */
public class GeospatialManagerTest extends AbstractServiceTest {

	public void testGetCountryFor() throws Exception {
		GeospatialManager geospatialManager = (GeospatialManager) getBean("geospatialManager");
		CountryDTO countryDTO = geospatialManager.getCountryFor("1", new Locale("en"));
		logger.info("Retrieved Country: " + countryDTO);
	}	

	public void testGetCountryForIsoCountryCode() throws Exception {
		GeospatialManager geospatialManager = (GeospatialManager) getBean("geospatialManager");
		CountryDTO countryDTO = geospatialManager.getCountryForIsoCountryCode("CA", new Locale("en"));
		logger.info("Retrieved Country: " + countryDTO);
	}	
	
	@SuppressWarnings("unchecked")
	public void testFindCountries() throws Exception {
		GeospatialManager geospatialManager = (GeospatialManager) getBean("geospatialManager");
		SearchResultsDTO searchResultsDTO = geospatialManager.findCountries("C%", true, false, true, new Locale("en"), new SearchConstraints(0, 100));	
		List<CountryDTO> countries = searchResultsDTO.getResults();
		logger.info("Retrieved result cell density size :" + countries.size());
		if (countries.size()>0) {
			logger.info("First result: " + countries.get(0));
		}
	}	

	public void testGetCountriesFor() throws Exception {
		GeospatialManager geospatialManager = (GeospatialManager) getBean("geospatialManager");
		List<CountryDTO> countries = geospatialManager.getCountriesFor(new Character('T'), true, new Locale("en"));
		logger.info("Retrieved Country: " + countries);
	}		
	
	@SuppressWarnings("unchecked")
	public void testFindAllCountries() throws Exception {
		GeospatialManager geospatialManager = (GeospatialManager) getBean("geospatialManager");
		SearchResultsDTO searchResultsDTO = geospatialManager.findAllCountries(new Locale("en"));	
		List<CountryDTO> countries = searchResultsDTO.getResults();
		logger.info("Retrieved result cell density size :" + countries.size());
		if (countries.size()>0) {
			logger.info("First result: " + countries.get(0));
		}
	}	
	
	/**
	 * Does not do any assertions due to the Data not being loaded before test
	 */
	public void testCellDensityForTaxon() throws Exception {
		GeospatialManager geospatialManager = (GeospatialManager) getBean("geospatialManager");
		List<CellDensityDTO> results = geospatialManager.get1DegCellDensities(EntityType.TYPE_TAXON, "1562");
		logger.info("Retrieved result cell density size :" + results.size());
		if (results.size()>0) {
			logger.info("Concept has a count of " + results.get(0).getCount() + " in cell id " + results.get(0).getCellId());
		}
	}
	
	/**
	 * Does not do any assertions due to the Data not being loaded before test
	 */
	public void testCellDensityForProvider() throws Exception {
		GeospatialManager geospatialManager = (GeospatialManager) getBean("geospatialManager");
		List<CellDensityDTO> results = geospatialManager.get1DegCellDensities(EntityType.TYPE_DATA_PROVIDER, "3");
		logger.info("Retrieved result cell density size :" + results.size());
		if (results.size()>0) {
			logger.info("Concept has a count of " + results.get(0).getCount() + " in cell id " + results.get(0).getCellId());
		}
	}
	
	/**
	 * Does not do any assertions due to the Data not being loaded before test
	 */
	public void testCellDensityForResource() throws Exception {
		GeospatialManager geospatialManager = (GeospatialManager) getBean("geospatialManager");
		List<CellDensityDTO> results = geospatialManager.get1DegCellDensities(EntityType.TYPE_DATA_RESOURCE, "1");
		logger.info("Retrieved result cell density size :" + results.size());
		if (results.size()>0) {
			logger.info("Concept has a count of " + results.get(0).getCount() + " in cell id " + results.get(0).getCellId());
		}
	}
	
	/**
	 * Does not do any assertions due to the Data not being loaded before test
	 */
	public void testGetCentiCellDensityForTaxon() throws Exception {
		GeospatialManager geospatialManager = (GeospatialManager) getBean("geospatialManager");
		List<CellDensityDTO> results = geospatialManager.get0Point1DegCellDensities(EntityType.TYPE_TAXON, "1562", 47612);
		logger.info("Retrieved result cell density size :" + results.size());
		if (results.size()>0) {
			logger.info("Concept has a count of " + results.get(0).getCount() + " in cell id " + 47612 + ", centiCellId: " + results.get(0).getCellId());
		}
	}
	
	/**
	 * Does not do any assertions due to the Data not being loaded before test
	 */
	public void testGetCentiCellDensityForProvider() throws Exception {
		GeospatialManager geospatialManager = (GeospatialManager) getBean("geospatialManager");
		List<CellDensityDTO> results = geospatialManager.get0Point1DegCellDensities(EntityType.TYPE_DATA_PROVIDER, "3", 7453);
		logger.info("Retrieved result cell density size :" + results.size());
		if (results.size()>0) {
			logger.info("Concept has a count of " + results.get(0).getCount() + " in cell id " + 7453 + ", centiCellId: " + results.get(0).getCellId());
		}
	}
	
	/**
	 * Does not do any assertions due to the Data not being loaded before test
	 */
	public void testGetCentiCellDensityForResource() throws Exception {
		GeospatialManager geospatialManager = (GeospatialManager) getBean("geospatialManager");
		List<CellDensityDTO> results = geospatialManager.get0Point1DegCellDensities(EntityType.TYPE_DATA_RESOURCE, "1", 0);
		logger.info("Retrieved result cell density size :" + results.size());
		if (results.size()>0) {
			logger.info("Concept has a count of " + results.get(0).getCount() + " in cell id " + 0 + ", centiCellId: " + results.get(0).getCellId());
		}
	}
	
	public void testGetTotalCountryCount() throws Exception {
		GeospatialManager geospatialManager = (GeospatialManager) getBean("geospatialManager");
		int count = geospatialManager.getTotalCountryCount();
		logger.info("County count: "+count);
	}
	
	public void testGetCountryForIP() throws Exception {	
		GeospatialManager geospatialManager = (GeospatialManager) getBean("geospatialManager");
		CountryDTO country = geospatialManager.getCountryForIPAddress("192.38.28.101", null);
		logger.info(IPUtils.convertIPtoLong("192.38.28.101"));
		logger.info("County: "+country);
	}
}