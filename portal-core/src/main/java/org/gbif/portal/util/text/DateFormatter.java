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
package org.gbif.portal.util.text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Utilities for dealing with dates.
 * @author trobertson
 */
public class DateFormatter {
	/**
	 * The xml Date format
	 */
	public static final String XML_DATE_FORMAT = "yyyy'-'MM'-'dd'T'HH':'mm':'ss";
	
	/**
	 * A typical 2 digit date format
	 */
	public static final String DD_MM_YY_FORMAT = "ddMMyy";
	
	/**
	 * A typical 4 digit date format
	 */
	public static final String DD_MM_YYYY_FORMAT = "ddMMyyyy";
	
	/**
	 * Static method to create an XML W3C compliant Date string from the
	 * specified Date object.
	 *
	 * @param date Date The Date object to format
	 * @return String The formatted Date string
	 */
	public static String toXMLDateString(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormatter.XML_DATE_FORMAT);
		StringBuffer dateBuffer = new StringBuffer();
		dateBuffer.append(dateFormat.format(date));

		/* append timezone in XML W3C format: +/-HH:ZZ */
		int zoneOffset = TimeZone.getDefault().getOffset(date.getTime());

		if (zoneOffset < 0) {
			dateBuffer.append('-');
			zoneOffset = -zoneOffset;
		} else {
			dateBuffer.append('+');
		}

		int hourOffset = zoneOffset / (60 * 60 * 1000);
		if (hourOffset < 10) {
			dateBuffer.append('0');
		}
		dateBuffer.append(hourOffset);
		dateBuffer.append(':');
		int minuteOffset = (zoneOffset % (60 * 60 * 1000)) / (60 * 1000);
		if (minuteOffset < 10) {
			dateBuffer.append('0');
		}
		dateBuffer.append(minuteOffset);

		return dateBuffer.toString();
	}
	
	/**
	 * Static method to create an XML W3C compliant Date string representing "NOW".
	 *
	 * @return String The formatted Date string
	 */
	public static String currentDateTimeAsXMLString() {		
		return DateFormatter.toXMLDateString(new Date());
	}	
	
	/**
	 * Attempts tp create a date with the given params
	 * Currently only accepts integer definitions of dates
	 * but should be extended to handle Jan, January etc
	 * - however, what about internationalisation?
	 * @param day E.g. 07.  
	 * @param month E.g. 12 
	 * @param year E.g. 1978
	 * @return A date
	 */
	public static Date toDate(String day, String month, String year) throws InvalidDateException {
		try {
			// they must be integers
			int dayAsInt = Integer.parseInt(day);
			int monthAsInt = Integer.parseInt(month);
			int yearAsInt = Integer.parseInt(year);
			
			Date yearAsDate;
			// use a simple date formatter to get the year into 4 chars
			if (yearAsInt<100) {
				SimpleDateFormat sdf = new SimpleDateFormat("yy");
				sdf.setLenient(false);
				yearAsDate = sdf.parse(year);
				
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				sdf.setLenient(false);
				yearAsDate = sdf.parse(year);
			}
			
			// use this for validation
			// offset the month for java
			monthAsInt--;
			Calendar calendar = new GregorianCalendar();
			calendar.setLenient(false);
			calendar.setTime(yearAsDate);
			calendar.set(Calendar.MONTH, monthAsInt);
			calendar.set(Calendar.DAY_OF_MONTH, dayAsInt);			
			
			return calendar.getTime();
			
		} catch (Exception e) {
			throw new InvalidDateException("Only valid integer date values accepted [[day:" + day 
					+ "][month:" + month + "][year:" + year + "]]");
		}
	}
	
	/**
	 * Attempts tp create a date with the given params
	 * Currently only accepts integer definitions of dates
	 * but should be extended to handle Jan, January etc
	 * - however, what about internationalisation?
	 * @param day E.g. 07.  If it is not specified then the 1st is assumed
	 * @param month E.g. 12 If it is not specified then January is assumed
	 * @param year E.g. 1978
	 * @return A date
	 */
	public static Date toDateLenientDayMonth(String day, String month, String year) throws InvalidDateException {
		if (day == null || day.length()==0) {
			day = "1";
		}
		if (month == null || month.length()==0) {
			month = "1";
		}
		return toDate(day, month, year);			
	}
}
