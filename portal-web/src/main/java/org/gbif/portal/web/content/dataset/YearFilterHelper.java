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
package org.gbif.portal.web.content.dataset;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.web.content.filter.FilterHelper;
import org.gbif.portal.web.filter.CriterionDTO;
import org.springframework.web.servlet.ModelAndView;

/**
 * A Filter Helper for a year filter.
 * 
 * @author dmartin
 */
public class YearFilterHelper implements FilterHelper {

	protected String subject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.YEAR";
	
	protected String occurrenceDateSubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.OCCURRENCEDATE";
	
	protected String isNotNullPredicate = "SERVICE.QUERY.PREDICATE.ISNOTNULL";
	
	protected String notEqualPredicate = "SERVICE.QUERY.PREDICATE.NEQUAL";
	
	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#addCriterion2Request(org.gbif.portal.web.filter.CriterionDTO, org.springframework.web.servlet.ModelAndView, javax.servlet.http.HttpServletRequest)
	 */
	public void addCriterion2Request(CriterionDTO criterionDTO,
			ModelAndView mav, HttpServletRequest request) {}

	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#getDisplayValue(java.lang.String)
	 */
	public String getDisplayValue(String value, Locale locale) {
		return value;
	}

	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#preProcess(java.util.List, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void preProcess(List<PropertyStoreTripletDTO> triplets, HttpServletRequest request, HttpServletResponse response) {

		
		//look for a single year filter
//		for (PropertyStoreTripletDTO triplet: triplets){
//			if(subject.equals(triplet.getSubject())){
//				//just need to add year is not null triplet
//				PropertyStoreTripletDTO yearNotNull = new PropertyStoreTripletDTO(triplet.getNamespace(), triplet.getSubject(), isNotNullPredicate, null);
//				PropertyStoreTripletDTO yearNotZero = new PropertyStoreTripletDTO(triplet.getNamespace(), triplet.getSubject(), notEqualPredicate, new Integer(0));
//				triplets.add(yearNotNull);
//				triplets.add(yearNotZero);
//				return;
//			}
//		}
	}
	
	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#getDefaultDisplayValue(javax.servlet.http.HttpServletRequest)
	 */
	public String getDefaultDisplayValue(HttpServletRequest request) {
		return null;
	}
	
	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#getDefaultValue(javax.servlet.http.HttpServletRequest)
	 */
	public String getDefaultValue(HttpServletRequest request) {
		return null;
	}	

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
}