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
package org.gbif.portal.web.controller.dataset;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.OccurrenceManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.web.content.PagingQueryContentProvider;
import org.gbif.portal.web.content.Query;
import org.gbif.portal.web.controller.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author dmartin
 */
public class RawOccurrenceDataController extends RestController{

	protected OccurrenceManager occurrenceManager;

	protected DataResourceManager dataResourceManager;
	
	protected PagingQueryContentProvider pagingQueryContentProvider;
	
	protected String searchResultsModelKey = "searchResults";
	
	/**
	 * @see org.gbif.portal.web.controller.RestController#handleRequest(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(Map<String, String> propertiesMap,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String dataResourceKey = propertiesMap.get("dataResourceKey");
		String catalogueNumber = request.getParameter("catalogueNumber");
		
		DataResourceDTO dataResource = dataResourceManager.getDataResourceFor(dataResourceKey);
		if(dataResource==null){
			return redirectToDefaultView();
		}
		
		RawOccurrenceRecordQuery roq = new RawOccurrenceRecordQuery(); 
		roq.setDataResourceKey(dataResourceKey);
		roq.setCatalogueNumber(catalogueNumber);
		roq.setOccurrenceManager(occurrenceManager);
		
		SearchResultsDTO searchResults = pagingQueryContentProvider.doQuery(roq, request, response);
		ModelAndView mav = new ModelAndView("rawOccurrenceTableView");
		mav.addObject(searchResultsModelKey, searchResults);
		mav.addObject("dataResource", dataResource);
		return mav;
	}

	/**
	 * @author dmartin
	 */
	public class RawOccurrenceRecordQuery implements Query {

		protected OccurrenceManager occurrenceManager;
		
		protected String dataResourceKey;
		protected String catalogueNumber;
		protected SearchConstraints searchConstraints = new SearchConstraints();
		
		public SearchResultsDTO execute() throws ServiceException {
			return occurrenceManager.findRawOccurrenceRecord(dataResourceKey, catalogueNumber, searchConstraints);
		}

		public SearchConstraints getSearchConstraints() {
			return searchConstraints;
		}

		public void setOccurrenceManager(OccurrenceManager occurrenceManager){
			this.occurrenceManager = occurrenceManager;
		}

		/**
		 * @return the dataResourceKey
		 */
		public String getDataResourceKey() {
			return dataResourceKey;
		}

		/**
		 * @param dataResourceKey the dataResourceKey to set
		 */
		public void setDataResourceKey(String dataResourceKey) {
			this.dataResourceKey = dataResourceKey;
		}

		/**
		 * @return the catalogueNumber
		 */
		public String getCatalogueNumber() {
			return catalogueNumber;
		}

		/**
		 * @param catalogueNumber the catalogueNumber to set
		 */
		public void setCatalogueNumber(String catalogueNumber) {
			this.catalogueNumber = catalogueNumber;
		}

		/**
		 * @return the occurrenceManager
		 */
		public OccurrenceManager getOccurrenceManager() {
			return occurrenceManager;
		}

		/**
		 * @param searchConstraints the searchConstraints to set
		 */
		public void setSearchConstraints(SearchConstraints searchConstraints) {
			this.searchConstraints = searchConstraints;
		}
	}
	
	/**
	 * @param occurrenceManager the occurrenceManager to set
	 */
	public void setOccurrenceManager(OccurrenceManager occurrenceManager) {
		this.occurrenceManager = occurrenceManager;
	}

	/**
	 * @param searchResultsModelKey the searchResultsModelKey to set
	 */
	public void setSearchResultsModelKey(String searchResultsModelKey) {
		this.searchResultsModelKey = searchResultsModelKey;
	}

	/**
	 * @param pagingQueryContentProvider the pagingQueryContentProvider to set
	 */
	public void setPagingQueryContentProvider(
			PagingQueryContentProvider pagingQueryContentProvider) {
		this.pagingQueryContentProvider = pagingQueryContentProvider;
	}

	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}
}