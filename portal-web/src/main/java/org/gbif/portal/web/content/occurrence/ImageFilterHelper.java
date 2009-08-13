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
package org.gbif.portal.web.content.occurrence;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.web.content.filter.FilterContentProvider;
import org.gbif.portal.web.content.filter.FilterHelper;
import org.gbif.portal.web.filter.CriterionDTO;
import org.springframework.web.servlet.ModelAndView;

/**
 * Images filter helper.
 * 
 * @author jcuadra
 */
public class ImageFilterHelper implements FilterHelper {

	protected String subject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.IMAGE";
	
	protected Integer hasImage = 0;
	
	protected FilterContentProvider filterContentProvider;
	
	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#addCriterion2Request(org.gbif.portal.web.filter.CriterionDTO, org.springframework.web.servlet.ModelAndView, javax.servlet.http.HttpServletRequest)
	 */
	public void addCriterion2Request(CriterionDTO criterionDTO,
			ModelAndView mav, HttpServletRequest request) {}

	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#getDisplayValue(java.lang.String, java.util.Locale)
	 */
	public String getDisplayValue(String value, Locale locale) {
		return value;
	}

	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#preProcess(java.util.List, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void preProcess(List<PropertyStoreTripletDTO> triplets,
			HttpServletRequest request, HttpServletResponse response) {
		List<PropertyStoreTripletDTO> hasImages = new ArrayList<PropertyStoreTripletDTO>();
		
		for(PropertyStoreTripletDTO triplet: triplets){
			if(triplet.getSubject().equals(subject)){
				hasImages.add(triplet);
			}
		}
		triplets.removeAll(hasImages);
		for(PropertyStoreTripletDTO triplet: hasImages){
			if(triplet.getObject()!=null && triplet.getObject() instanceof Integer){
				if(triplet.getObject().equals(hasImage)){
					filterContentProvider.addHasImagesOccurrenceTriplets(triplets, triplet.getNamespace());
				} else {
					filterContentProvider.addNoImagesOccurrenceTriplets(triplets, triplet.getNamespace());
				}
			}
		}
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
	 * @param filterContentProvider the filterContentProvider to set
	 */
	public void setFilterContentProvider(FilterContentProvider filterContentProvider) {
		this.filterContentProvider = filterContentProvider;
	}

	/**
	 * @param hasImage the hasImage to set
	 */
	public void setHasImage(Integer hasImage) {
		this.hasImage = hasImage;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
}