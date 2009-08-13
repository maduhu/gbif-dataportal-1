package org.gbif.portal.web.content.filter;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.io.ResultsOutputter;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.triplet.TripletQueryManager;
import org.gbif.portal.web.content.Query;
import org.gbif.portal.web.download.OutputProcess;
/**
 * TripletQuery wraps a triplet based query.
 * 
 * Note: this cant be a spring wired singleton.
 *  
 * @author dmartin
 */
public class TripletQuery implements Query, OutputProcess {
	
	protected static Log log = LogFactory.getLog(TripletQuery.class);
	/**The search constraints for this query**/
	protected SearchConstraints searchConstraints = new SearchConstraints();
	/**The set of triplets to use for this query**/
	protected List<PropertyStoreTripletDTO> triplets;
	/**The Service Manager that is providing the query**/
	protected TripletQueryManager tripletQueryManager;
	/**Indicates if all or any of the triplets should be matched.**/
	protected boolean matchAll = true;
	
	/**
	 * @see org.gbif.portal.web.download.OutputProcess#process(org.gbif.portal.io.ResultsOutputter)
	 */
	public void process(ResultsOutputter resultsOutputter) throws Exception {
		tripletQueryManager.formatTripletQueryRequest(triplets, matchAll, searchConstraints, resultsOutputter);
	}	
	
	/**
	 * Execute the query using the supplied criteria.
	 * @return a SearchResultsDTO containing the results.
	 */
	public SearchResultsDTO execute() throws ServiceException {
		return tripletQueryManager.doTripletQuery(triplets, matchAll, searchConstraints);
	}
	
	/**
	 * @see org.gbif.portal.web.download.OutputProcess#setProcessLimit(int)
	 */
	public void setProcessLimit(int maxNoToProcess) {
		if(searchConstraints==null){
			searchConstraints = new SearchConstraints(0, maxNoToProcess);
		} else {
			searchConstraints.setMaxResults(maxNoToProcess);
		}
	}

	/**
	 * @return the matchAll
	 */
	public boolean isMatchAll() {
		return matchAll;
	}

	/**
	 * @param matchAll the matchAll to set
	 */
	public void setMatchAll(boolean matchAll) {
		this.matchAll = matchAll;
	}

	/**
	 * @return the searchConstraints
	 */
	public SearchConstraints getSearchConstraints() {
		return searchConstraints;
	}

	/**
	 * @param searchConstraints the searchConstraints to set
	 */
	public void setSearchConstraints(SearchConstraints searchConstraints) {
		this.searchConstraints = searchConstraints;
	}

	/**
	 * @return the tripletQueryManager
	 */
	public TripletQueryManager getTripletQueryManager() {
		return tripletQueryManager;
	}

	/**
	 * @param tripletQueryManager the tripletQueryManager to set
	 */
	public void setTripletQueryManager(TripletQueryManager tripletQueryManager) {
		this.tripletQueryManager = tripletQueryManager;
	}

	/**
	 * @return the triplets
	 */
	public List<PropertyStoreTripletDTO> getTriplets() {
		return triplets;
	}

	/**
	 * @param triplets the triplets to set
	 */
	public void setTriplets(List<PropertyStoreTripletDTO> triplets) {
		this.triplets = triplets;
	}
}