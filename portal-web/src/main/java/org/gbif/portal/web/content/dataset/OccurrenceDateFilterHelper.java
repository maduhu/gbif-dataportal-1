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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.web.content.filter.FilterHelper;
import org.gbif.portal.web.filter.CriterionDTO;
import org.gbif.portal.web.util.DateUtil;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Filter Helper for Occurrence Dates. This allows correct selection of a particular occurrence date.
 * 
 * @author dmartin
 */
public class OccurrenceDateFilterHelper implements FilterHelper {

	protected MessageSource messageSource;
	protected static Log logger = LogFactory.getLog(OccurrenceDateFilterHelper.class);	
	
	protected String betweenI18nKey = "between";
	protected String andI18nKey = "and";
	
	protected String occurrenceDateSubject ="SERVICE.OCCURRENCE.QUERY.SUBJECT.OCCURRENCEDATE";
	protected String isPredicate ="SERVICE.QUERY.PREDICATE.EQUAL";	
	protected String lessThanPredicate ="SERVICE.QUERY.PREDICATE.LE";	
	protected String greaterThanPredicate ="SERVICE.QUERY.PREDICATE.GE";	
	
	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#preProcess(java.util.List, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void preProcess(List<PropertyStoreTripletDTO> triplets, HttpServletRequest request, HttpServletResponse response) {

		//pick out date triplets
		List<PropertyStoreTripletDTO> dateTriplets = new ArrayList<PropertyStoreTripletDTO>();
		for(PropertyStoreTripletDTO triplet: triplets){
			if(triplet.getSubject().equals(occurrenceDateSubject)){
				dateTriplets.add(triplet);
			}
		}
		triplets.removeAll(dateTriplets);
		
		//process date triplets
		for(PropertyStoreTripletDTO triplet: dateTriplets){
			if(triplet.getSubject().equals(occurrenceDateSubject)){
				String tripletObject = (String) triplet.getObject();
				String startDate = null;
				String endDate = null;

				//is it a range or specific date?
				if(tripletObject!=null && tripletObject.indexOf('-')>0){
					int indexOfSeparator = tripletObject.indexOf('-');
					startDate = tripletObject.substring(0, indexOfSeparator);
					endDate = tripletObject.substring(indexOfSeparator+1);
				} else {
					startDate = tripletObject;
					endDate = tripletObject;
				}
					
				try {
					Date start = DateUtil.setTime(DateUtils.parseDate(startDate, new String[]{"ddMMyyyy"}), 0, 0, 0);
					Date end = DateUtil.setTime(DateUtils.parseDate(endDate, new String[]{"ddMMyyyy"}), 23, 59, 59);

					triplets.add(new PropertyStoreTripletDTO(triplet.getNamespace(),triplet.getSubject(), greaterThanPredicate, start));
					triplets.add(new PropertyStoreTripletDTO(triplet.getNamespace(),triplet.getSubject(), lessThanPredicate, end));
				} catch (ParseException e) {
					logger.warn(e.getMessage(), e);
					//do nothing for this dodgy triplet
				}
			}
		}
	}


	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#getDisplayValue(java.lang.String)
	 */
	public String getDisplayValue(String value, Locale locale) {
		//if date range, will be of the format ddmmyyyy-ddmmyyyy
		if(value!=null && value.indexOf('-')>0){
			String startDate = value.substring(0, value.indexOf('-'));
			String endDate = value.substring(value.indexOf('-')+1);
			StringBuffer sb = new StringBuffer();
			sb.append(messageSource.getMessage(betweenI18nKey, null, locale));
			sb.append(' ');
			sb.append(getPrettyPrintDateString(startDate));
			sb.append(' ');
			sb.append(messageSource.getMessage(andI18nKey, null, locale));			
			sb.append(' ');			
			sb.append(getPrettyPrintDateString(endDate));
			return sb.toString();
		} else {
			return getPrettyPrintDateString(value); 
		}
	}

	/**
	 * Creates a string of the form 7 June 1987 with internationalization of month.
	 * 
	 * @param theDate
	 * @return
	 */
	public String getPrettyPrintDateString(String dateString){
		Date theDate;
		try {
			theDate = DateUtils.parseDate(dateString, new String[]{"ddMMyyyy"});
		} catch (ParseException e) {
			logger.error(e);
			return "";
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(theDate);
		StringBuffer sb = new StringBuffer();
		sb.append(calendar.get(Calendar.DAY_OF_MONTH));
		sb.append(' ');
		int month = calendar.get(Calendar.MONTH) + 1; //GregorianCalendar indexes from 0 for month
		sb.append(messageSource.getMessage("month." + month, null, null));
		sb.append(' ');
		sb.append(calendar.get(Calendar.YEAR));
		return sb.toString();
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
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		String startDate = sdf.format(today);
		int startYear = c.get(Calendar.YEAR);

		
		if(startYear % 10 ==0){
			c.add(Calendar.YEAR, -10);
		} else {
			c.add(Calendar.YEAR, -(startYear % 10));			
		}
		String enddate = sdf.format(c.getTime());
		return enddate+"-"+startDate;
	}	
	
	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#addCriterion2Request(org.gbif.portal.web.filter.CriterionDTO, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void addCriterion2Request(CriterionDTO criterionDTO, ModelAndView mav, HttpServletRequest request) {}
	
	/**
	 * @param isPredicate the isPredicate to set
	 */
	public void setIsPredicate(String isPredicate) {
		this.isPredicate = isPredicate;
	}

	/**
	 * @param occurrenceDateSubject the occurrenceDateSubject to set
	 */
	public void setOccurrenceDateSubject(String occurrenceDateSubject) {
		this.occurrenceDateSubject = occurrenceDateSubject;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
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
	 * @param greaterThanPredicate the greaterThanPredicate to set
	 */
	public void setGreaterThanPredicate(String greaterThanPredicate) {
		this.greaterThanPredicate = greaterThanPredicate;
	}

	/**
	 * @param lessThanPredicate the lessThanPredicate to set
	 */
	public void setLessThanPredicate(String lessThanPredicate) {
		this.lessThanPredicate = lessThanPredicate;
	}
}