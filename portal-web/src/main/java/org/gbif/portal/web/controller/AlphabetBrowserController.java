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
package org.gbif.portal.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * Simple Controller for Browsing Entities by Alphabet.
 * 
 * @author dmartin
 */
public abstract class AlphabetBrowserController extends RestController {

	/** The browser view name */
	protected String modelViewName;
	/** The default search character if none specified */
	protected char defaultSearchChar = 'A';
	/** The search string request key */
	protected String searchStringRequestKey="searchString";
	/** The search char model key */
	protected String selectedCharModelKey="selectedChar";
	/** The search results model key */
	protected String searchResultsModelKey="searchResults";	
	/** The Model Key for the alphabet */
	protected String alphabetModelKey="alphabet";		

	/**
	 * The method to override. Implementations will perform the search by calling a Service Layer method 
	 * and then add the content to the model.
	 * 
	 * @param searchChar the search character
	 * @param mav the model and view to populate
	 * @param request the http request
	 * @param response the http response
	 */
	public abstract ModelAndView alphabetSearch(char searchChar, ModelAndView mav, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * Retrieve an alphabet for this set of entities.
	 * 
	 * @param mav
	 * @param request
	 * @param response
	 */
	public abstract List<Character> retrieveAlphabet(HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * @see org.gbif.portal.web.controller.RestController#handleRequest(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ModelAndView handleRequest(Map<String, String> properties, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView(modelViewName);
		List<Character> alphabet = retrieveAlphabet(request, response);
		alphabet = sortAlphabetForDisplay(alphabet);
		mav.addObject(alphabetModelKey, alphabet);
		String searchString = properties.get(searchStringRequestKey);
		char searchChar = defaultSearchChar;
		if(StringUtils.isNotEmpty(searchString)){
			if(logger.isDebugEnabled())
				logger.debug("Search string before trim: "+searchString);
			searchString = searchString.trim();
			
			if(searchString.length()>1){
				//try parsing the number
				try {
					searchChar = (char) Integer.parseInt(searchString);
				} catch(NumberFormatException e){
					//expected behaviour
				}
			} else {
				if(logger.isDebugEnabled())
					logger.debug("Search string after decoding: "+searchString);
				searchChar = searchString.charAt(0);
			}
			if(logger.isDebugEnabled())
				logger.debug("Char to search with: "+searchChar+", unicode value:"+searchChar);
		} else if(alphabet!=null && !alphabet.isEmpty()){
			searchChar = alphabet.get(0);
		}
		mav.addObject(selectedCharModelKey, searchChar);
		return alphabetSearch(searchChar, mav, request,  response);
	}
	
	/**
	 * Sorts the alphabet so that it consists of alpha characters first, numeric chars
	 * and then other characters such as parentheses.
	 * 
	 * @param alphabet
	 */
	public static List<Character> sortAlphabetForDisplay(List<Character> alphabet){
		List<Character> listOfAlpha = new ArrayList<Character>();
		List<Character> listOfNumeric = new ArrayList<Character>();
		List<Character> listOfNonAlphaNumeric = new ArrayList<Character>();
		
		for(Character theCharacter: alphabet){
			
			if(Character.isDigit(theCharacter)){
				listOfNumeric.add(theCharacter);
			} else if( Character.isLetter(theCharacter)){
				listOfAlpha.add(theCharacter);
			} else {
				listOfNonAlphaNumeric.add(theCharacter);
			}
		}
		sortChars(listOfAlpha);
		sortChars(listOfNumeric);
		sortChars(listOfNonAlphaNumeric);
		listOfAlpha.addAll(listOfNumeric);
		listOfAlpha.addAll(listOfNonAlphaNumeric);
		return listOfAlpha;
	}
	
	public static void sortChars(List<Character> listOfChars){
		Collections.sort(listOfChars, new Comparator<Character>(){
			public int compare(Character o1, Character o2) {
				return o1.compareTo(o2);
			}});
	}
	
	/**
	 * @param modelViewName the modelViewName to set
	 */
	public void setModelViewName(String modelViewName) {
		this.modelViewName = modelViewName;
	}

	/**
	 * @param defaultSearchChar the defaultSearchChar to set
	 */
	public void setDefaultSearchChar(char defaultSearchChar) {
		this.defaultSearchChar = defaultSearchChar;
	}

	/**
	 * @param searchResultsModelKey the searchResultsModelKey to set
	 */
	public void setSearchResultsModelKey(String searchResultsModelKey) {
		this.searchResultsModelKey = searchResultsModelKey;
	}

	/**
	 * @param searchStringRequestKey the searchStringRequestKey to set
	 */
	public void setSearchStringRequestKey(String searchStringRequestKey) {
		this.searchStringRequestKey = searchStringRequestKey;
	}

	/**
	 * @param selectedCharModelKey the selectedCharModelKey to set
	 */
	public void setSelectedCharModelKey(String selectedCharModelKey) {
		this.selectedCharModelKey = selectedCharModelKey;
	}
}