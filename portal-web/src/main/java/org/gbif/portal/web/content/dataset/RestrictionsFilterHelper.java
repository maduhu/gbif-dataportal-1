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
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.web.content.filter.FilterContentProvider;
import org.gbif.portal.web.content.filter.FilterHelper;
import org.gbif.portal.web.filter.CriterionDTO;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.ModelAndView;

import com.ibm.icu.util.StringTokenizer;

/**
 * A filter helper for the Restrictions filter.
 * 
 * Note: Restrictions filter currently not in use.
 * 
 * @author dmartin
 */
public class RestrictionsFilterHelper implements FilterHelper {
	
	protected List<String> restrictionI18nKeys;
	protected List<String> tripletSubjectKeys;
	protected String restrictionsSubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.RESTRICTIONS";
	protected String geoOnlySubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.GEOREFERENCEDONLY";
	protected String equalsPredicate = "SERVICE.QUERY.PREDICATE.EQUAL";
	protected MessageSource messageSource;
	protected FilterContentProvider filterContentProvider;

	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#addCriterion2Request(org.gbif.portal.web.filter.CriterionDTO, org.springframework.web.servlet.ModelAndView, javax.servlet.http.HttpServletRequest)
	 */
	public void addCriterion2Request(CriterionDTO criterionDTO,
			ModelAndView mav, HttpServletRequest request) {}

	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#getDisplayValue(java.lang.String)
	 */
	public String getDisplayValue(String value, Locale locale) {
		if(StringUtils.isNotEmpty(value)){
			List<Boolean> restrictions = getRestrictionsFromString(value);
			StringBuffer sb = new StringBuffer();
			for(int i=0; i<restrictionI18nKeys.size() && i<restrictions.size(); i++){
				if(restrictions.get(i)){
					if(sb.length()>0)
						sb.append(", ");
					sb.append(messageSource.getMessage(restrictionI18nKeys.get(i), null, locale));
				}
			}
			return sb.toString();
		}
		return value;
	}

	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#preProcess(java.util.List, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void preProcess(List<PropertyStoreTripletDTO> triplets,
			HttpServletRequest request, HttpServletResponse response) {
		
		List<PropertyStoreTripletDTO> restrictionFilters = new ArrayList<PropertyStoreTripletDTO>();
		//look for restrictions filters
		for (PropertyStoreTripletDTO triplet: triplets){
			if(restrictionsSubject.equals(triplet.getSubject())){
				restrictionFilters.add(triplet);
			}
		}
		triplets.removeAll(restrictionFilters);
		
		//retrieve restrictions triplet
		for(PropertyStoreTripletDTO triplet: restrictionFilters){
			if(triplet.getSubject().equals(restrictionsSubject)){
				List<Boolean> restrictions = getRestrictionsFromString((String)triplet.getObject());
				for(int i=0; i<tripletSubjectKeys.size() && i<restrictions.size(); i++){
					if(tripletSubjectKeys.get(i).equals(geoOnlySubject)){
						filterContentProvider.addGeoreferencedOnlyOccurrenceTriplets(triplets, triplet.getNamespace());
					} else {
						PropertyStoreTripletDTO restrictionTriplet = new PropertyStoreTripletDTO(triplet.getNamespace(), tripletSubjectKeys.get(i), equalsPredicate, 0);
						triplets.add(restrictionTriplet);
					}
				}
			}
		}
	}
	
	/**
	 * Splits comma separated booleans into a list. wow. 
	 * @param value
	 * @return
	 */
	private List<Boolean> getRestrictionsFromString(String value){
		StringTokenizer st = new StringTokenizer(value, ",");
		List<Boolean> restrictions = new ArrayList<Boolean>();
		while(st.hasMoreTokens())
			restrictions.add(Boolean.parseBoolean(st.nextToken()));
		return restrictions;
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
	 * @param restrictionI18nKeys the restrictionI18nKeys to set
	 */
	public void setRestrictionI18nKeys(List<String> restrictionI18nKeys) {
		this.restrictionI18nKeys = restrictionI18nKeys;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @param restrictionsSubject the restrictionsSubject to set
	 */
	public void setRestrictionsSubject(String restrictionsSubject) {
		this.restrictionsSubject = restrictionsSubject;
	}

	/**
	 * @param tripletSubjectKeys the tripletSubjectKeys to set
	 */
	public void setTripletSubjectKeys(List<String> tripletSubjectKeys) {
		this.tripletSubjectKeys = tripletSubjectKeys;
	}

	/**
	 * @param equalsPredicate the equalsPredicate to set
	 */
	public void setEqualsPredicate(String equalsPredicate) {
		this.equalsPredicate = equalsPredicate;
	}

	/**
	 * @param filterContentProvider the filterContentProvider to set
	 */
	public void setFilterContentProvider(FilterContentProvider filterContentProvider) {
		this.filterContentProvider = filterContentProvider;
	}

	/**
	 * @param geoOnlySubject the geoOnlySubject to set
	 */
	public void setGeoOnlySubject(String geoOnlySubject) {
		this.geoOnlySubject = geoOnlySubject;
	}
}