package org.gbif.portal.web.content;

import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.service.ServiceException;

/**
 * Encapsulates a query and a set of constraints. Implementations of this interface
 * would typically wrap a call to a service layer method passing the set
 * of SearchConstraints to use.
 * 
 * @author dmartin
 */
public interface Query {
	
	/**
	 * Execute the query based on the query's current
	 * SearchConstraints
	 * 
	 * @return SearchResultsDTO contains the results for this query
	 */
	public SearchResultsDTO execute() throws ServiceException;	
	
	/**
	 * Get the SearchConstraints. Users of a query would retrieve the
	 * existing constraints for a query and manipulate them how they please.
	 * 
	 * @return SearchConstraintsDTO contains the constraints for this query
	 */
	public SearchConstraints getSearchConstraints();
}
