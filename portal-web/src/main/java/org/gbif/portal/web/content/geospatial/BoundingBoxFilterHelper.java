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
package org.gbif.portal.web.content.geospatial;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.dto.util.BoundingBoxDTO;
import org.gbif.portal.service.util.BoundingBoxUtils;
import org.gbif.portal.util.geospatial.LatLongBoundingBox;
import org.gbif.portal.web.content.filter.FilterHelper;
import org.gbif.portal.web.filter.CriterionDTO;
import org.springframework.web.servlet.ModelAndView;

/**
 * Takes a Bounding Box triplets and transforms into lat/long triplets
 *
 * @author Dave Martin
 */
public class BoundingBoxFilterHelper implements FilterHelper {

	protected static Log logger = LogFactory.getLog(BoundingBoxFilterHelper.class);	
	
	protected String subject="SERVICE.OCCURRENCE.QUERY.SUBJECT.BOUNDINGBOX";
	
	protected String latitudeSubject="SERVICE.OCCURRENCE.QUERY.SUBJECT.LATITUDE";
	protected String longitudeSubject="SERVICE.OCCURRENCE.QUERY.SUBJECT.LONGITUDE";

	protected String centiCellIdSubject="SERVICE.OCCURRENCE.QUERY.SUBJECT.CENTICELLID";
	protected String cellIdSubject="SERVICE.OCCURRENCE.QUERY.SUBJECT.CELLID";
	protected String cellIdMod360Subject="SERVICE.OCCURRENCE.QUERY.SUBJECT.CELLID.MOD360";
	
	protected String equalsPredicate = "SERVICE.QUERY.PREDICATE.EQUAL";
	protected String lessThanPredicate = "SERVICE.QUERY.PREDICATE.L";
	protected String lessThanOrEqualPredicate = "SERVICE.QUERY.PREDICATE.LE";	
	protected String greaterThanOrEqualPredicate = "SERVICE.QUERY.PREDICATE.GE";
	
	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#preProcess(org.gbif.portal.dto.PropertyStoreTripletDTO, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void preProcess(List<PropertyStoreTripletDTO> triplets,
			HttpServletRequest request, HttpServletResponse response) {
		
		if(triplets.size()==0)
			return;
		
		String namespace = triplets.get(0).getNamespace();
		
		List<PropertyStoreTripletDTO> bbFilters = new ArrayList<PropertyStoreTripletDTO>();
		
		//look for BB filters
		for (PropertyStoreTripletDTO triplet: triplets){
			if(subject.equals(triplet.getSubject())){
					bbFilters.add(triplet);
			}
		}
		
		for(PropertyStoreTripletDTO bbFilter: bbFilters){
			triplets.remove(bbFilter);
			String value = (String) bbFilter.getObject();
			LatLongBoundingBox llbb = getLatLongBoundingBox(value);
			if(llbb!=null){
				triplets.addAll(BoundingBoxUtils.getTripletsFromLatLongBoundingBox(namespace, llbb));
			}
		}
	}

	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#getDisplayValue(java.lang.String)
	 */
	public String getDisplayValue(String value, Locale locale) {
		LatLongBoundingBox llbb = getLatLongBoundingBox(value);
		if(llbb==null)
			return null;
		StringBuffer sb = new StringBuffer();
		sb.append(Math.abs(llbb.getMinLong()));
		sb.append("&deg;");
		sb.append(llbb.getMinLong()>0 ? 'E':'W');
		sb.append(", ");
		sb.append(Math.abs(llbb.getMinLat()));
		sb.append("&deg;");
		sb.append(llbb.getMinLat()>0 ? 'N':'S');
		sb.append(", ");
		sb.append(Math.abs(llbb.getMaxLong()));
		sb.append("&deg;");
		sb.append(llbb.getMaxLong()>0 ? 'E':'W');
		sb.append(", ");
		sb.append(Math.abs(llbb.getMaxLat()));
		sb.append("&deg;");
		sb.append(llbb.getMaxLat()>0 ? 'N':'S');
		return sb.toString();
	}	

	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#addCriterion2Request(org.gbif.portal.web.filter.CriterionDTO, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void addCriterion2Request(CriterionDTO criterionDTO, ModelAndView mav, HttpServletRequest request) {
		LatLongBoundingBox llbb = getLatLongBoundingBox(criterionDTO.getValue());
		mav.addObject("boundingBox",llbb);
		request.setAttribute("boundingBox", llbb);
	}

	/**
	 * Creates a Lat Long bounding box from a string of the format 12E,12S,12W,12N.
	 * @param value
	 * @return
	 */
	public static LatLongBoundingBox getLatLongBoundingBox(String value) {
		StringTokenizer tokenizer = new StringTokenizer(value, ",");
		List<String> elements = new ArrayList<String>();
		while(tokenizer.hasMoreTokens())
			elements.add(tokenizer.nextToken().trim());
		if(elements.size()!=4){
			logger.debug("bounding box format not reckonized:'"+value+"', expected format <min-longitude>E,<min-latitude>S,<max-longitude>W,<max-latitude>N");
			return null;
		}
		return new LatLongBoundingBox(getLatLong(elements.get(0)), getLatLong(elements.get(1)), getLatLong(elements.get(2)), getLatLong(elements.get(3)));
	}

	/**
	 * Creates a Lat Long bounding box from a string of the format 12E,12S,12W,12N.
	 * @param value
	 * @return
	 */
	public static BoundingBoxDTO getBoundingBoxDTO(String value) {
		StringTokenizer tokenizer = new StringTokenizer(value, ",");
		List<String> elements = new ArrayList<String>();
		while(tokenizer.hasMoreTokens())
			elements.add(tokenizer.nextToken().trim());
		if(elements.size()!=4){
			logger.debug("bounding box format not reckonized:'"+value+"', expected format <min-longitude>E,<min-latitude>S,<max-longitude>W,<max-latitude>N");
			return null;
		}
		return new BoundingBoxDTO(getLatLong(elements.get(0)), getLatLong(elements.get(1)), getLatLong(elements.get(2)), getLatLong(elements.get(3)));
	}	
	
	/**
	 * Takes the min/max lat/long and returns something of the form 12E,12S,12W,12N.
	 * @param minLongitude
	 * @param minLatitude
	 * @param maxLongitude
	 * @param maxLatitude
	 * @return
	 */
	public  static String getBoundingBoxQueryString(float minLongitude, float minLatitude, float maxLongitude, float maxLatitude) {
		StringBuffer sb = new StringBuffer();
		sb.append(Math.abs(minLongitude));
		sb.append(minLongitude>0?'E':'W');
		sb.append(',');
		sb.append(Math.abs(minLatitude));
		sb.append(minLatitude>0?'N':'S');
		sb.append(',');
		sb.append(Math.abs(maxLongitude));
		sb.append(maxLongitude>0?'E':'W');
		sb.append(',');
		sb.append(Math.abs(maxLatitude));
		sb.append(maxLatitude>0?'N':'S');
		logger.debug(sb);
		return sb.toString();
	}
	
	/**
	 * Takes 12E, returns -12
	 * @param element
	 * @return
	 */
	public static Float getLatLong(String element){
		char pole = element.charAt(element.length()-1);
		if(Character.isDigit(pole))
			return new Float(element);
		String number = element.substring(0, element.length()-1);
		if(pole=='W' || pole=='S')
			number="-"+number;
		return new Float(number);
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

	/**
	 * @param greaterThanOrEqualPredicate the greaterThanOrEqualPredicate to set
	 */
	public void setGreaterThanOrEqualPredicate(String greaterThanOrEqualPredicate) {
		this.greaterThanOrEqualPredicate = greaterThanOrEqualPredicate;
	}

	/**
	 * @param latitudeSubject the latitudeSubject to set
	 */
	public void setLatitudeSubject(String latitudeSubject) {
		this.latitudeSubject = latitudeSubject;
	}

	/**
	 * @param lessThanPredicate the lessThanPredicate to set
	 */
	public void setLessThanPredicate(String lessThanPredicate) {
		this.lessThanPredicate = lessThanPredicate;
	}

	/**
	 * @param longitudeSubject the longitudeSubject to set
	 */
	public void setLongitudeSubject(String longitudeSubject) {
		this.longitudeSubject = longitudeSubject;
	}

	/**
	 * @param cellIdMod360Subject the cellIdMod360Subject to set
	 */
	public void setCellIdMod360Subject(String cellIdMod360Subject) {
		this.cellIdMod360Subject = cellIdMod360Subject;
	}

	/**
	 * @param cellIdSubject the cellIdSubject to set
	 */
	public void setCellIdSubject(String cellIdSubject) {
		this.cellIdSubject = cellIdSubject;
	}

	/**
	 * @param centiCellIdSubject the centiCellIdSubject to set
	 */
	public void setCentiCellIdSubject(String centiCellIdSubject) {
		this.centiCellIdSubject = centiCellIdSubject;
	}

	/**
	 * @param equalsPredicate the equalsPredicate to set
	 */
	public void setEqualsPredicate(String equalsPredicate) {
		this.equalsPredicate = equalsPredicate;
	}

	/**
	 * @param lessThanOrEqualPredicate the lessThanOrEqualPredicate to set
	 */
	public void setLessThanOrEqualPredicate(String lessThanOrEqualPredicate) {
		this.lessThanOrEqualPredicate = lessThanOrEqualPredicate;
	}
}