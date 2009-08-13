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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.dto.resources.ResourceNetworkDTO;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.web.content.filter.FilterHelper;
import org.gbif.portal.web.filter.CriterionDTO;
import org.springframework.web.servlet.ModelAndView;

/**
 * A Data Provider Id Filter Helper.
 * 
 * @author dmartin
 */
public class ResourceNetworkIdFilterHelper implements FilterHelper {
	
	protected static Log logger = LogFactory.getLog(ResourceNetworkIdFilterHelper.class);

	protected DataResourceManager dataResourceManager;
	protected String resourceNetworkSubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.RESOURCENETWORKID";
	protected String dataResourceSubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.DATARESOURCEID";
	protected String inPredicate = "SERVICE.QUERY.PREDICATE.IN";
	
	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#addCriterion2Request(org.gbif.portal.web.filter.CriterionDTO, org.springframework.web.servlet.ModelAndView, javax.servlet.http.HttpServletRequest)
	 */
	public void addCriterion2Request(CriterionDTO criterionDTO,
			ModelAndView mav, HttpServletRequest request) {}

	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#getDisplayValue(java.lang.String)
	 */
	public String getDisplayValue(String value, Locale locale) {
		try{
			ResourceNetworkDTO resourceNetwork = dataResourceManager.getResourceNetworkFor(value);
			if(resourceNetwork==null)
				return null;
			return resourceNetwork.getName();
		} catch (ServiceException e){
			logger.error(e.getMessage(), e);
			return value;
		}
	}

	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#preProcess(java.util.List, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void preProcess(List<PropertyStoreTripletDTO> triplets,
			HttpServletRequest request, HttpServletResponse response) {
		
		for(PropertyStoreTripletDTO triplet: triplets){
			if(triplet.getSubject().equals(resourceNetworkSubject)){
				String resourceNetworkKey = (String) triplet.getObject();
				try {
					List<DataResourceDTO> dataResources = dataResourceManager.getDataResourcesForResourceNetwork(resourceNetworkKey);
					triplet.setSubject(dataResourceSubject);
					triplet.setPredicate(inPredicate);
					List<Long> ids = new ArrayList<Long>();
					for(DataResourceDTO dr: dataResources){
						if(dr.getKey()!=null)
							ids.add(new Long(dr.getKey()));
					}
					triplet.setObject(ids);
				} catch (ServiceException e) {
					logger.error(e.getMessage(), e);
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
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}
}