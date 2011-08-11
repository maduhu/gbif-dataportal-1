/***************************************************************************
 * Copyright (C) 2005 Global Biodiversity Information Facility Secretariat.
 * All Rights Reserved.
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ***************************************************************************/
package org.gbif.portal.web.controller.search;

import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.TaxonomyManager;

import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Paging taxon name search.
 * 
 * @author dmartin
 */
public class TaxonSearchController extends SimpleSearchPagingController {

  protected DataResourceManager dataResourceManager;
  protected TaxonomyManager taxonomyManager;
  /** Whether to allow unconfirmed names */
  protected boolean allowUnconfirmedNames = true;
  protected int minLengthForSort = 6;

  /**
   * @see org.gbif.portal.web.controller.search.SimpleSearchPagingController#doQuery(java.lang.String, java.util.Map,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  @Override
  public boolean doQuery(String searchString, SearchConstraints searchConstraints, Map<String, String> propertiesMap,
      HttpServletRequest request, HttpServletResponse response) throws ServiceException {
    // DataProviderDTO nubProviderDTO = dataResourceManager.getNubDataProvider();
    DataResourceDTO nubResourceDTO = dataResourceManager.getNubDataResource();
    boolean sortAlphabetically = false;
    if (searchString != null && searchString.length() > minLengthForSort) {
      sortAlphabetically = true;
    }

    // SearchResultsDTO searchResults = taxonomyManager.findTaxonConcepts(searchString, true, null,
// nubProviderDTO.getKey(), null, null, null, RequestContextUtils.getLocale(request).getLanguage(), null,
// allowUnconfirmedNames, sortAlphabetically, searchConstraints);
    SearchResultsDTO searchResults = taxonomyManager.findTaxonConcepts(searchString, true, null, null,
        nubResourceDTO.getKey(), null, null, RequestContextUtils.getLocale(request).getLanguage(), null,
        allowUnconfirmedNames, sortAlphabetically, searchConstraints);
    request.setAttribute(searchResultsRequestKey, searchResults);
    return searchResults.hasMoreResults();
  }

  /**
   * @param allowUnconfirmedNames the allowUnconfirmedNames to set
   */
  public void setAllowUnconfirmedNames(boolean allowUnconfirmedNames) {
    this.allowUnconfirmedNames = allowUnconfirmedNames;
  }

  /**
   * @param dataResourceManager the dataResourceManager to set
   */
  public void setDataResourceManager(DataResourceManager dataResourceManager) {
    this.dataResourceManager = dataResourceManager;
  }

  /**
   * @param minLengthForSort the minLengthForSort to set
   */
  public void setMinLengthForSort(int minLengthForSort) {
    this.minLengthForSort = minLengthForSort;
  }

  /**
   * @param taxonomyManager the taxonomyManager to set
   */
  public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
    this.taxonomyManager = taxonomyManager;
  }

}