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
package org.gbif.portal.service.log;

import org.gbif.portal.util.geospatial.CellIdUtils;
import org.gbif.portal.util.geospatial.LatLongBoundingBox;

/**
 * A logging helper for Cell id queries.
 * 
 * @author dmartin
 */
public class DescriptionFromCellId extends
		AbstractLoggableFromPredicateAndObject {

	/**
	 * @see org.gbif.portal.service.log.LoggableFromPredicateAndObject#getLoggable(java.lang.Object, java.lang.Object)
	 */
	public String getLoggable(Object predicate, Object object) {
		if(object==null)
			return null;
		Integer value = (Integer) object;
		LatLongBoundingBox llbb = CellIdUtils.toBoundingBox(value);
			
		//geo[<=115.0W,<=23.0N,>=21.0N,>=113.0W]
		
		if(predicate.equals("SERVICE.QUERY.PREDICATE.GE")){
			//greater than bottom left corner
			StringBuffer sb = new StringBuffer();
			sb.append(">=");
			sb.append(Math.abs(llbb.getMinLong()));
			sb.append(getLongitudeSuffix(llbb.getMinLong()));
			sb.append(',');
			sb.append(">=");
			sb.append(Math.abs(llbb.getMinLat()));
			sb.append(getLatitudeSuffix(llbb.getMinLat()));
			return sb.toString();
		} else if(predicate.equals("SERVICE.QUERY.PREDICATE.LE")) {
			//less than top right corner				
			StringBuffer sb = new StringBuffer();
			sb.append("<=");
			sb.append(Math.abs(llbb.getMinLong()));
			sb.append(getLongitudeSuffix(llbb.getMaxLong()));
			sb.append(',');
			sb.append("<=");
			sb.append(Math.abs(llbb.getMinLat()));
			sb.append(getLatitudeSuffix(llbb.getMaxLat()));
			return sb.toString();
		} else {
			//else must be equals
			StringBuffer sb = new StringBuffer();
			sb.append(llbb.getMinLat());
			sb.append(getLatitudeSuffix(llbb.getMinLat()));
			sb.append(">=");
			sb.append(Math.abs(llbb.getMinLong()));
			sb.append(getLongitudeSuffix(llbb.getMaxLong()));
			sb.append(',');
			sb.append(">=");
			sb.append(Math.abs(llbb.getMinLat()));
			sb.append(getLatitudeSuffix(llbb.getMaxLat()));	
			sb.append("<=");
			sb.append(Math.abs(llbb.getMaxLong()));
			sb.append(getLongitudeSuffix(llbb.getMaxLong()));
			sb.append(',');
			sb.append("<=");
			sb.append(Math.abs(llbb.getMaxLat()));		
			sb.append(getLatitudeSuffix(llbb.getMaxLat()));
			return sb.toString();
		}
	}

	public static char getLatitudeSuffix(float latitude){
		return latitude>=0 ? 'E' : 'W';
	}
	
	public static char getLongitudeSuffix(float longitude){
		return longitude>=0 ? 'N' : 'S';
	}
}