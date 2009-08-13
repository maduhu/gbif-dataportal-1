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
package org.gbif.portal.web.content.taxonomy;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.web.content.filter.FilterContentProvider;
import org.gbif.portal.web.content.filter.FilterHelper;
import org.gbif.portal.web.filter.CriterionDTO;
import org.springframework.web.servlet.ModelAndView;

/**
 * Typification filter helper.
 * 
 * @author Tim
 */
public class TypificationFilterHelper implements FilterHelper {

	protected Log logger = LogFactory.getLog(getClass());
	
	protected String subject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.TYPESTATUSCOUNT";
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
		List<PropertyStoreTripletDTO> typeStatusFilters = new ArrayList<PropertyStoreTripletDTO>();
		
		for(PropertyStoreTripletDTO triplet: triplets){
			if(triplet.getSubject().equals(subject)){
				typeStatusFilters.add(triplet);
			}
		}
		if(!typeStatusFilters.isEmpty()){
			logTriplets(triplets, "Before stripping");
			triplets.removeAll(typeStatusFilters);
			
			logTriplets(triplets, "After stripping");
			String namespace = typeStatusFilters.get(0).getNamespace();
			if (namespace != null) {
				filterContentProvider.addIsTypeStatusOccurrenceTriplets(triplets, namespace);
			}
			
			logTriplets(triplets, "After modification");
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
	 * Logs at debug, the filters
	 */
	protected void logTriplets(List<PropertyStoreTripletDTO> triplets, String prefix) {
		if(!logger.isDebugEnabled())
			return;
		if (prefix != null) {
			logger.debug(prefix);
		}
		if (triplets.size()==0) {
			logger.debug("Triplets are empty");
		}
		for (PropertyStoreTripletDTO t : triplets) {
			logger.debug(t.getSubject() + " : " + t.getPredicate() + " : " + t.getObject());
		}
	}
	
	/**
	 * @param filterContentProvider the filterContentProvider to set
	 */
	public void setFilterContentProvider(FilterContentProvider filterContentProvider) {
		this.filterContentProvider = filterContentProvider;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
}