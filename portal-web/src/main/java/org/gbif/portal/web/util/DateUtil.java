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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * A class for Date Utility methods.
 *
 * @author Dave Martin
 */
public class DateUtil {

	/**Logger */
	public static Log log = LogFactory.getLog(DateUtil.class);
	/**The default date time format **/
	public static String dtf = "dd MMM yyyy HH:mm";
	/**The default date format **/
	public static String df = "dd/MM/yyyy";	
	
	/**
	 * Utility for setting the time on a date.
	 * @param date
	 * @param h
	 * @param m
	 * @param s
	 * @return the date with the time set
	 */
	public static Date setTime(Date date, int h, int m, int s) {
		if(date==null)
			return null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, h);
		calendar.set(Calendar.MINUTE, m);
		calendar.set(Calendar.SECOND, s);
		return calendar.getTime();
	}
	
	/**
	 * Construct a date
	 * @param request
	 * @param dateFieldName
	 * @return
	 */
	public static Date getDateFromRequest(HttpServletRequest request, String dateFieldName) {

		String day = request.getParameter(dateFieldName+"_day");
		String month = request.getParameter(dateFieldName+"_month");
		String year = request.getParameter(dateFieldName+"_year");
		request.setAttribute(dateFieldName+"_day", day);
		request.setAttribute(dateFieldName+"_month", month);
		request.setAttribute(dateFieldName+"_year", year);
		
		if(day==null || month==null && year==null)
			return null;
		
		try {
			return DateUtils.parseDate(day+month+year, new String[]{"ddMMyyyy"});
		} catch (ParseException e) {
			log.trace(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * Takes a date string and attempts to parse
	 * @param dateString
	 * @return parse Date object, null if unparsable
	 */
	public static Date parseDate(String dateString){
		if (dateString==null)
			return null;
		Date parsedDate = null;
		try {
			dateString = dateString.trim();
			parsedDate = DateUtils.parseDate(dateString, new String[]{
					"dd/MM/yyyy","yyyyMMdd", "yyyy MM dd",  "yyyy/MM/dd", "yyyy-MM-dd", "yyyy dd MM", "yyyy"});
			if(parsedDate!=null)
				return parsedDate;
		} catch (Exception e) {
			log.debug(e);
		}
		try {
			parsedDate = DateUtils.parseDate(dateString, new String[]{
					"ddMMyyyy",  "dd MM yyyy", "ddMMMyyyy", "dd MMM yyyy",
					"dd/MM/yyyy", "dd-MM-yyyy", "dd-MMM-yyyy", 
					 "dd MM yyyy"});
			return parsedDate;
		} catch (Exception e) {
			log.debug(e);
		}
		return null;
	}

	/**
	 * Gets a date string using the default format.
	 * @param theDate
	 * @return date string
	 */
	public static String getDateString (Date theDate){
		if (theDate==null)
			return null;
		return new SimpleDateFormat(df).format(theDate);
	}

	/**
	 * Gets a date time string using the default format.
	 * @param theDate
	 * @return date time string
	 */	
	public static String getDateTimeString (Date theDate){
		if (theDate==null)
			return null;
		return new SimpleDateFormat(dtf).format(theDate);
	}
}