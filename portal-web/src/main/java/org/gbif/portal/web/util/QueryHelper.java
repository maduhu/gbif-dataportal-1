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
package org.gbif.portal.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.web.content.filter.FilterHelper;
import org.gbif.portal.web.filter.CriteriaDTO;
import org.gbif.portal.web.filter.CriteriaUtil;
import org.gbif.portal.web.filter.CriterionDTO;
import org.gbif.portal.web.filter.FilterDTO;
import org.gbif.portal.web.filter.FilterMapWrapper;
import org.gbif.portal.web.filter.PredicateDTO;

import com.ibm.icu.util.StringTokenizer;

/**
 * Provides Query Utilities and the mapping of criteria to triplet queries.
 * 
 * @author dmartin
 */
public class QueryHelper {

	public static Log logger = LogFactory.getLog(QueryHelper.class);	
	/**The array of supported wildcards **/
	protected char[] supportedWildcards;
	/**The wildcard supported by Service Layer **/
	protected char wildcard;
	 /**The key for the is like predicate **/
	protected String isLikePredicate;
	 /**The key for the is equal predicate **/
	protected String equalPredicate;
	/**The query namespace**/
	protected String queryNamespace;
	/** Filters to use */
	protected FilterMapWrapper filters;
	/** The filter pre query processors */
	protected Map<String, FilterHelper> preProcessors;
	
	/**
	 * Replaces supported wildcards with the wildcard
	 * supported by the service layer. namely %.
	 * @param object
	 * @return String with replaced wildcards.
	 */	
	public String processWildcards(String object){
		//FIXME need to remove wildcards from beginning of string!!!
		for (int i=0; i<supportedWildcards.length;i++){
			object=object.replace(supportedWildcards[i], wildcard);
		}
		//remove wildcards from beginning to stop crippling queries
		while(object.length()>0 &&object.charAt(0)=='%'){
			object = object.substring(1);
		}
		return object;
	}
	
	/**
	 * Replaces supported wildcards with the wildcard
	 * supported by the service layer. namely %.
	 * @param object
	 * @return String with replaced wildcards.
	 */	
	public String removeWildcards(String object){
		//FIXME need to remove wildcards from beginning of string!!!
		for (int i=0; i<supportedWildcards.length;i++){
			object=object.replaceAll("\\"+supportedWildcards[i], "");
		}
		return object;
	}	

	/**
	 * Retrieve a list of triplets for the supplied subject.
	 * 
	 * @param triplets
	 * @param subject
	 * @return
	 */
	public static List<PropertyStoreTripletDTO> getTripletsBySubject(List<PropertyStoreTripletDTO> triplets, String subject){
		List<PropertyStoreTripletDTO> selectedTriplets = new ArrayList<PropertyStoreTripletDTO>();
		for(PropertyStoreTripletDTO triplet: triplets){
			if(subject.equals(triplet.getSubject())){
				selectedTriplets.add(triplet);
			}
		}
		return selectedTriplets;
	}
	
	/**
	 * Generate a List of triplets using the request.
	 * 
	 * @param filters
	 * @param criteria
	 * @return PropertyStoreTripletDTO
	 */
	public List<PropertyStoreTripletDTO> getTriplets(HttpServletRequest request, HttpServletResponse response){
		CriteriaDTO criteria = CriteriaUtil.getCriteria(request, filters.getFilters());
		return getTriplets(filters.getFilters(), criteria, request, response);
	}
	
	/**
	 * Generate a List of triplets using the supplied filters and criteria.
	 * 
	 * @param filters
	 * @param criteria
	 * @return PropertyStoreTripletDTO
	 */
	public List<PropertyStoreTripletDTO> getTriplets(List<FilterDTO> filters, CriteriaDTO criteria, HttpServletRequest request, HttpServletResponse response){
		
    	List<PropertyStoreTripletDTO> triplets = new LinkedList<PropertyStoreTripletDTO>();
    	// map each criterion to a triplet
    	for (CriterionDTO criterion: criteria.getCriteria()){
    		//empty values are flatly ignored
    		if (StringUtils.isEmpty(criterion.getValue()))
    			continue;
    		//tidy the value
    		criterion.setValue(tidyValue(criterion.getValue()));
    		PropertyStoreTripletDTO triplet = new PropertyStoreTripletDTO();
    		//get the right filter
    		FilterDTO filter = null;
			for(FilterDTO theFilter: filters){
				if(theFilter.getId().equals(criterion.getSubject()))
					filter = theFilter;
			}
    		if(filter==null)
    			continue;
    		
    		//check for a preprocessor
    		List<PredicateDTO> predicates = filter.getPredicates();
    		PredicateDTO predicateDTO = predicates.get(Integer.parseInt(criterion.getPredicate())); 
    		String predicateKey = predicateDTO.getKey(); 
    		
    		//get spring key for this predicate
    		Object valueObject = criterion.getValue();
    		if( valueObject instanceof String){
    			String value = (String) valueObject;
    			value = value.trim();
    			valueObject = value;
	    		//process wildcards if wildcard friendly
	    		if(filter.isWildcardFriendly()){
	    			//check for wildcard
	    			if(hasWildcards((String) value)){
	    				//adjust the predicate if not is like
	    				predicateKey = switchPredicate(predicateKey);
	    			}
	    			valueObject = processWildcards((String) value);
	    		}
	    		
	    		String valueType = filter.getValueType();
	    		if (StringUtils.isNotEmpty(valueType)){
	    			valueObject = parseValueType(valueType, value);
	    		}
    		}
    		
    		if(valueObject!=null){
	    		triplet.setSubject(filter.getSubject());
	    		triplet.setPredicate(predicateKey);    		
	    		triplet.setObject(valueObject);
	    		triplet.setNamespace(getQueryNamespace());
	    		triplets.add(triplet);
    		}
    	}
    	
    	//pre process those triplets!!
    	if(!triplets.isEmpty() && preProcessors!=null){
			for(FilterHelper processor: preProcessors.values()){
	   			processor.preProcess(triplets, request, response);
			}
    	}
		return triplets;
	}
	
	/**
	 * @param valueType
	 * @param value
	 * @return
	 */
	public static Object parseValueType(String valueType, String value){
		if ("java.lang.Boolean".equals(valueType)){
			return Boolean.parseBoolean(value);
		}		
		if ("java.util.Date".equals(valueType)){
			return DateUtil.parseDate(value);
		}
		if ("java.lang.Float".equals(valueType)){
			try{
				return NumberUtils.createFloat(value);
			} catch(NumberFormatException e){
				return null;
			}
		}
		if("java.lang.Integer".equals(valueType)){
			try{
				return NumberUtils.createInteger(value);
			} catch(NumberFormatException e){
				return null;
			}
		}
		if("java.lang.Long".equals(valueType)){
			try{
				return NumberUtils.createLong(value);
			} catch(NumberFormatException e){
				return null;
			}
		}		
		throw new IllegalArgumentException("Bad Filter Configuration - Unrecognised valueType: "+valueType);
	}

	/**
	 * Replaces wildcard with supported service layer wildcard
	 * 
	 * @param request
	 * @return
	 */
	public static String getSearchString(String searchStringRequestKey, HttpServletRequest request){
		String searchString = (String) request.getAttribute(searchStringRequestKey);		
		if(StringUtils.isNotEmpty(searchString)){
			try {
				searchString = URLDecoder.decode(searchString, "UTF-8");
				request.setAttribute(searchStringRequestKey, searchString);
				searchString = QueryHelper.tidyValue(searchString);
			} catch (UnsupportedEncodingException e){
				logger.debug( e.getMessage(), e);
			}
			searchString = searchString.replace('*', '%');
			
		}
		if(logger.isDebugEnabled())
			logger.debug("search string: "+searchString);
		return searchString;
	}	
	
	/**
	 * Tidies the supplied query string removing multiple space chars.
	 * @return
	 */
	public static String tidyValue(String queryString){
		if(StringUtils.isEmpty(queryString))
			return null;
		queryString = queryString.trim();
		StringTokenizer tokenizer = new StringTokenizer(queryString, " ");
		StringBuffer tidiedBuffer = new StringBuffer();
		while(tokenizer.hasMoreTokens()){
			String token = tokenizer.nextToken();
			token = StringUtils.deleteWhitespace(token);
			tidiedBuffer.append(token);
			if(tokenizer.hasMoreTokens())
				tidiedBuffer.append(' ');
		}
		return tidiedBuffer.toString();
	}
	
	/**
	 * Indicates whether the supplied object value has wildcards.
	 * @param object
	 * @return true if has a wildcard
	 */
	public boolean hasWildcards(String object){
		if(object==null|| object.length()==0)
			return false;
		for (int i=0; i<supportedWildcards.length;i++){
			if(object.indexOf(supportedWildcards[i]) !=-1)
				return true;
		}
		return false;
	}	
	
	/**
	 * Switches to the isLike predicate if is equals.
	 * @return
	 */
	public String switchPredicate(String predicateKey) {
		if(predicateKey==null)
			return null;
		if(predicateKey.equals(equalPredicate))
			return isLikePredicate;
		return predicateKey;
	}

	/**
	 * @return the equalPredicate
	 */
	public String getEqualPredicate() {
		return equalPredicate;
	}

	/**
	 * @param equalPredicate the equalPredicate to set
	 */
	public void setEqualPredicate(String equalPredicate) {
		this.equalPredicate = equalPredicate;
	}

	/**
	 * @return the isLikePredicate
	 */
	public String getIsLikePredicate() {
		return isLikePredicate;
	}

	/**
	 * @param isLikePredicate the isLikePredicate to set
	 */
	public void setIsLikePredicate(String isLikePredicate) {
		this.isLikePredicate = isLikePredicate;
	}

	/**
	 * @return the queryNamespace
	 */
	public String getQueryNamespace() {
		return queryNamespace;
	}

	/**
	 * @param queryNamespace the queryNamespace to set
	 */
	public void setQueryNamespace(String queryNamespace) {
		this.queryNamespace = queryNamespace;
	}

	/**
	 * @return the supportedWildcards
	 */
	public char[] getSupportedWildcards() {
		return supportedWildcards;
	}

	/**
	 * @param supportedWildcards the supportedWildcards to set
	 */
	public void setSupportedWildcards(char[] supportedWildcards) {
		this.supportedWildcards = supportedWildcards;
	}

	/**
	 * @return the wildcard
	 */
	public char getWildcard() {
		return wildcard;
	}

	/**
	 * @param wildcard the wildcard to set
	 */
	public void setWildcard(char wildcard) {
		this.wildcard = wildcard;
	}

	/**
	 * @return the preProcessors
	 */
	public Map<String, FilterHelper> getPreProcessors() {
		return preProcessors;
	}

	/**
	 * @param preProcessors the preProcessors to set
	 */
	public void setPreProcessors(Map<String, FilterHelper> preProcessors) {
		this.preProcessors = preProcessors;
	}

	/**
	 * @param filters the filters to set
	 */
	public void setFilters(FilterMapWrapper filters) {
		this.filters = filters;
	}
}