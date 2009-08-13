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
package org.gbif.portal.web.controller.geography;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.dto.CountDTO;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.dto.util.EntityType;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.GeospatialManager;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.web.controller.search.NameSearchController;
import org.gbif.portal.web.controller.search.bean.NameSearch;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * A name search controller for datasets.
 * 
 * @author dmartin
 */
public class CountrySpeciesSearchController extends NameSearchController {

	protected DataResourceManager dataResourceManager;
	protected GeospatialManager geospatialManager;
	protected TaxonomyManager taxonomyManager;
	
	/** non publicly disclosed keys */
	protected String providerRequestKey = "provider";
	protected String resourceRequestKey = "resource";
	protected String georeferencedRequestKey = "geoRefOnly";
	protected String allowUnconfirmedRequestKey = "allowUnconfirmed";
	/**
	 * @see org.gbif.portal.web.controller.search.NameSearchController#handleSearch(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.ModelAndView, org.gbif.portal.web.controller.search.bean.NameSearch)
	 */
	@Override
	public void handleSearch(Map<String, String> propertiesMap,
			HttpServletRequest request, HttpServletResponse response,
			ModelAndView mav, NameSearch nameSearch) throws Exception {
		
		boolean georeferenced = ServletRequestUtils.getBooleanParameter(request, georeferencedRequestKey, false);
		boolean allowUnconfirmed = ServletRequestUtils.getBooleanParameter(request, allowUnconfirmedRequestKey, false);
		String locale = ServletRequestUtils.getStringParameter(request, "locale", "en");
		DataProviderDTO dataProvider = dataResourceManager.getNubDataProvider();
		String conceptId = null;
		
		try {
		 Long conceptIdAsLong = new Long(nameSearch.getQuery());
		 conceptId = nameSearch.getQuery();
		} catch(Exception e) {
			//expected
		}
		
		if(conceptId==null){
			SearchResultsDTO searchResults = taxonomyManager.findTaxonConcepts(nameSearch.getQuery(), false, 
					dataProvider.getKey(), null, null, null, null, null, (Date)null, allowUnconfirmed, false, new SearchConstraints(0,1));
			if(!searchResults.isEmpty()){
				conceptId = ((BriefTaxonConceptDTO) searchResults.get(0)).getKey();
			}
		}
		
		if(StringUtils.isNotEmpty(conceptId)){
			List<CountDTO> countryCounts = null;
			if(georeferenced){
				countryCounts = geospatialManager.getTotalsPerCountry(EntityType.TYPE_TAXON, conceptId);
			} else {
				countryCounts = taxonomyManager.getCountryCountsForTaxonConcept(conceptId, new Locale(locale));
			}
			mav.addObject(searchResultsModelKey, countryCounts);
		}
	}

	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}

	/**
	 * @param providerRequestKey the providerRequestKey to set
	 */
	public void setProviderRequestKey(String providerRequestKey) {
		this.providerRequestKey = providerRequestKey;
	}

	/**
	 * @param resourceRequestKey the resourceRequestKey to set
	 */
	public void setResourceRequestKey(String resourceRequestKey) {
		this.resourceRequestKey = resourceRequestKey;
	}

	/**
   * @param taxonomyManager the taxonomyManager to set
   */
  public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
  	this.taxonomyManager = taxonomyManager;
  }

	/**
   * @param georeferencedRequestKey the georeferencedRequestKey to set
   */
  public void setGeoreferencedRequestKey(String georeferencedRequestKey) {
  	this.georeferencedRequestKey = georeferencedRequestKey;
  }

	/**
   * @param allowUnconfirmedRequestKey the allowUnconfirmedRequestKey to set
   */
  public void setAllowUnconfirmedRequestKey(String allowUnconfirmedRequestKey) {
  	this.allowUnconfirmedRequestKey = allowUnconfirmedRequestKey;
  }

	/**
   * @return the geospatialManager
   */
  public GeospatialManager getGeospatialManager() {
  	return geospatialManager;
  }

	/**
   * @param geospatialManager the geospatialManager to set
   */
  public void setGeospatialManager(GeospatialManager geospatialManager) {
  	this.geospatialManager = geospatialManager;
  }

	/**
   * @return the dataResourceManager
   */
  public DataResourceManager getDataResourceManager() {
  	return dataResourceManager;
  }

	/**
   * @return the taxonomyManager
   */
  public TaxonomyManager getTaxonomyManager() {
  	return taxonomyManager;
  }

	/**
   * @return the providerRequestKey
   */
  public String getProviderRequestKey() {
  	return providerRequestKey;
  }

	/**
   * @return the resourceRequestKey
   */
  public String getResourceRequestKey() {
  	return resourceRequestKey;
  }

	/**
   * @return the georeferencedRequestKey
   */
  public String getGeoreferencedRequestKey() {
  	return georeferencedRequestKey;
  }

	/**
   * @return the allowUnconfirmedRequestKey
   */
  public String getAllowUnconfirmedRequestKey() {
  	return allowUnconfirmedRequestKey;
  }
}