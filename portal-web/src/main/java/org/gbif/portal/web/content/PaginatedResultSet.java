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
package org.gbif.portal.web.content;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.pagination.PaginatedList;
import org.displaytag.properties.SortOrderEnum;
import org.gbif.portal.dto.SearchResultsDTO;
/**
 * A Results set used for paging.
 * 
 * The simplest of simple paging algorithms - pass in a page number and that is it 
 *  
 * @author trobertson (simplified version of the caching version dmartin wrote, but uses "limit ?,?" type sql to remove need for "order by")
 */
public class PaginatedResultSet implements PaginatedList {

	protected static Log logger = LogFactory.getLog(PaginatedResultSet.class);
	
	/**
	 * The result set this is currently wrapping
	 */
	protected SearchResultsDTO searchResults;
	
	/**
	 * The page number
	 */
	protected int pageNumber = 0;
	
	/**
	 * The results per page, defaults to 20
	 */
	protected int objectsPerPage = 20; 
	
	/**
	 * Used to indicate if it is an exact count 
	 */
	protected boolean fullListSizeIsExact = false;
	
	/**
	 * Gets the results for the page only
	 * @see org.displaytag.pagination.PaginatedList#getList()
	 */
	public List getList() {
		if (searchResults.getResults().size()>objectsPerPage) {
			return searchResults.getResults().subList(0,objectsPerPage);
		} else {
			return searchResults.getResults();
		}		
	}
	
	/**
	 * Gets the count of results which may be an estimation...
	 */
	public int getFullListSize() {
		int knownResults = searchResults.getResults().size();
		if (pageNumber > 1) {
			// then there count up previous pages
			knownResults += ((pageNumber-1)*objectsPerPage);
		}
		
		// if we are at the last page
		if (!searchResults.hasMoreResults()) {
			fullListSizeIsExact = true;
		} else {
			// ensure that it shows a next page
			knownResults+=1;
			fullListSizeIsExact = false;
		}
		return knownResults;
	}

	/**
	 * Not implemented
	 * @see org.displaytag.pagination.PaginatedList#getSortCriterion()
	 */
	public String getSortCriterion() {
		return null;
	}

	/**
	 * Not implemented
	 * @see org.displaytag.pagination.PaginatedList#getSortDirection()
	 */
	public SortOrderEnum getSortDirection() {
		return null;
	}

	/**
	 * Not implemented
	 * @see org.displaytag.pagination.PaginatedList#getSearchId()
	 */
	public String getSearchId() {
		return null;
	}


	/**
	 * @return Returns the objectsPerPage.
	 */
	public int getObjectsPerPage() {
		return objectsPerPage;
	}


	/**
	 * @param objectsPerPage The objectsPerPage to set.
	 */
	public void setObjectsPerPage(int objectsPerPage) {
		this.objectsPerPage = objectsPerPage;
	}


	/**
	 * @return Returns the pageNumber.
	 */
	public int getPageNumber() {
		return pageNumber;
	}


	/**
	 * @param pageNumber The pageNumber to set.
	 */
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}


	/**
	 * @return Returns the searchResults.
	 */
	public SearchResultsDTO getSearchResults() {
		return searchResults;
	}


	/**
	 * @param searchResults The searchResults to set.
	 */
	public void setSearchResults(SearchResultsDTO searchResults) {
		this.searchResults = searchResults;
	}

	/**
	 * @return Returns the fullListSizeIsExact.
	 */
	public boolean isFullListSizeIsExact() {
		return fullListSizeIsExact;
	}

	/**
	 * @param fullListSizeIsExact The fullListSizeIsExact to set.
	 */
	public void setFullListSizeIsExact(boolean fullListSizeIsExact) {
		this.fullListSizeIsExact = fullListSizeIsExact;
	}

}