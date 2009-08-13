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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.web.content.filter.FilterHelper;
import org.gbif.portal.web.filter.CriterionDTO;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * @author dmartin
 */
public class YearRangeFilterHelper implements FilterHelper {

	protected static Log logger = LogFactory.getLog(YearRangeFilterHelper.class);	
	
	protected MessageSource messageSource;
	
	protected String betweenI18nKey = "between";
	
	protected String andI18nKey = "and";
	
	protected String subject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.YEARRANGE";
	
	protected String gePredicate = "SERVICE.QUERY.PREDICATE.GE";
	protected String lePredicate = "SERVICE.QUERY.PREDICATE.LE";
	
	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#addCriterion2Request(org.gbif.portal.web.filter.CriterionDTO, org.springframework.web.servlet.ModelAndView, javax.servlet.http.HttpServletRequest)
	 */
	public void addCriterion2Request(CriterionDTO criterionDTO,
			ModelAndView mav, HttpServletRequest request) {
	}

	/**
	 * Takes a string of the format 1992-1996 and returns between 1992 and 1996 
	 * 
	 * @see org.gbif.portal.web.content.filter.FilterHelper#getDisplayValue(java.lang.String, java.util.Locale)
	 */
	public String getDisplayValue(String value, Locale locale) {
		//if date range, will be of the format yyyy-yyyy
		if(value!=null && value.indexOf('-')>0){
			String startYear = value.substring(0, value.indexOf('-'));
			String endYear = value.substring(value.indexOf('-')+1);
			StringBuffer sb = new StringBuffer();
			sb.append(messageSource.getMessage(betweenI18nKey, null, locale));
			sb.append(' ');
			sb.append(startYear);
			sb.append(' ');
			sb.append(messageSource.getMessage(andI18nKey, null, locale));			
			sb.append(' ');			
			sb.append(endYear);
			return sb.toString();
		} else {
			return value; 
		}
	}

	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#preProcess(java.util.List, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void preProcess(List<PropertyStoreTripletDTO> triplets,
			HttpServletRequest request, HttpServletResponse response) {

		List<PropertyStoreTripletDTO> additionalTriplets = new ArrayList<PropertyStoreTripletDTO>();
		PropertyStoreTripletDTO yearRangeTriplet = null;
		
		try{		
			for(PropertyStoreTripletDTO triplet: triplets){
				
				if(triplet.getSubject().equals(subject)){
					
					yearRangeTriplet = triplet;
					String yearRange = (String) triplet.getObject();
					
					String startYear = yearRange.substring(0, yearRange.indexOf('-'));
					String endYear = yearRange.substring(yearRange.indexOf('-')+1);
					
					triplet.setPredicate(gePredicate);
					triplet.setObject(new Integer(startYear));
					
					PropertyStoreTripletDTO endYearTriplet = new PropertyStoreTripletDTO(triplet.getNamespace(), triplet.getSubject(), lePredicate, new Integer(endYear));
					additionalTriplets.add(endYearTriplet);
				}
			}
			triplets.addAll(additionalTriplets);
			} catch(NumberFormatException e){
				logger.error(e.getMessage(), e);
				
				//tidy up triplets - remove year range triplet if unable to set.
				if(yearRangeTriplet!=null)
					triplets.remove(yearRangeTriplet);
			}			
	}

	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#getDefaultDisplayValue(javax.servlet.http.HttpServletRequest)
	 */
	public String getDefaultDisplayValue(HttpServletRequest request) {
		String defaultValue = getDefaultValue(request);
		Locale locale = RequestContextUtils.getLocale(request);
		return getDisplayValue(defaultValue, locale);
	}
	
	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#getDefaultValue(javax.servlet.http.HttpServletRequest)
	 */
	public String getDefaultValue(HttpServletRequest request) {
		Date today = new Date(System.currentTimeMillis());
		Calendar c = GregorianCalendar.getInstance();
		c.setTime(today);
		int startYear = c.get(Calendar.YEAR);
		int endYear = -1;
		
		if(startYear % 10 ==0)
			endYear = startYear -10;
		else
			endYear = startYear - (startYear % 10);
		
		return endYear+"-"+startYear;
	}	
	
	/**
	 * @param andI18nKey the andI18nKey to set
	 */
	public void setAndI18nKey(String andI18nKey) {
		this.andI18nKey = andI18nKey;
	}

	/**
	 * @param betweenI18nKey the betweenI18nKey to set
	 */
	public void setBetweenI18nKey(String betweenI18nKey) {
		this.betweenI18nKey = betweenI18nKey;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}