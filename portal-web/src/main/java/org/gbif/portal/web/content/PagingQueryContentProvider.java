package org.gbif.portal.web.content;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.SearchResultsDTO;

/**
 * PagingQueryContentProvider
 * 
 * To use this content provider, implement the addContent() method.
 * 
 * @author dmartin
 */
public class PagingQueryContentProvider implements ContentProvider {

	protected static Log logger = LogFactory.getLog(PagingQueryContentProvider.class);
	/** The default page size */
	public static final int DEFAULT_PAGE_SIZE = 20;
	/** Page no request key */
	protected String pageRequestKey="pageno";
	/** Lower key request key */
	protected String resultSizeRequestKey="resultSize";
	/** Upper key request key */
	protected String resultSizeIsExactRequestKey="resultSizeIsExact";	
	
	/** 
	 * The attribute used to store the resultset into the session. This should be overidden in spring config
	 * where there are potentially 2 different queries stored in a session.  
	 */
	protected String resultsRequestKey="results";	
	/**
	 * These parameters will be configured in spring at the minute but will probably
	 * be part of user preferences
	 */
	protected int pageSize=DEFAULT_PAGE_SIZE;

	/**
	 * @see org.gbif.portal.web.content.ContentProvider#addContent(org.gbif.portal.web.content.ContentView, java.lang.Object)
	 */
	public void addContent(ContentView cc, HttpServletRequest request, HttpServletResponse response) throws Exception {};

	/**
	 * Takes a set of triplets and performs a query adding the results to the session and to the request.
	 * This is useful if the criteria in the request needs to be altered before the query is constructed.
	 * 
	 * @param request
	 * @return Search Results DTO containing results.
	 */
	@SuppressWarnings("unchecked")
	public final SearchResultsDTO doQuery (Query query, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// get the page number
		Integer page = new Integer(1);
		try {
			if (request.getParameter(pageRequestKey) !=  null) {
				page = Integer.parseInt(request.getParameter(pageRequestKey));
			}
		} catch (NumberFormatException e) {
			logger.warn("Ignoring invalid page: " + page);
		}
		if (page<1) {
			page=1;
		} 
		
		if(logger.isDebugEnabled())
			logger.debug("Viewing page: " + page);
		
		// issue the query
		query.getSearchConstraints().setStartIndex((page*pageSize) - pageSize);
		query.getSearchConstraints().setMaxResults((5*pageSize) + 1);
		if(logger.isDebugEnabled())
			logger.debug("Using search constraints: " + query.getSearchConstraints());
		
		SearchResultsDTO results = query.execute();		
		if(logger.isDebugEnabled())
			logger.debug("Returning search results: " + results);
		
		//set up the paginated results set
		PaginatedResultSet paginatedResultSet = new PaginatedResultSet();
		paginatedResultSet.setSearchResults(results);
		paginatedResultSet.setPageNumber(page);
		paginatedResultSet.setObjectsPerPage(pageSize);
    	
		if(logger.isDebugEnabled()){
			logger.debug("Setting results full size = " +  paginatedResultSet.getFullListSize());			
		}
		
		//add these to model to help construct links
		request.setAttribute(resultsRequestKey, paginatedResultSet);
		request.setAttribute(resultSizeRequestKey, paginatedResultSet.getFullListSize());
		request.setAttribute(resultSizeIsExactRequestKey, paginatedResultSet.isFullListSizeIsExact());
		return paginatedResultSet.getSearchResults();
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @param resultsRequestKey the resultsRequestKey to set
	 */
	public void setResultsRequestKey(String resultsRequestKey) {
		this.resultsRequestKey = resultsRequestKey;
	}

	/**
	 * @param resultSizeIsExactRequestKey the resultSizeIsExactRequestKey to set
	 */
	public void setResultSizeIsExactRequestKey(String resultSizeIsExactRequestKey) {
		this.resultSizeIsExactRequestKey = resultSizeIsExactRequestKey;
	}

	/**
	 * @param resultSizeRequestKey the resultSizeRequestKey to set
	 */
	public void setResultSizeRequestKey(String resultSizeRequestKey) {
		this.resultSizeRequestKey = resultSizeRequestKey;
	}
}