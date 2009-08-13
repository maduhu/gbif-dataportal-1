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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.KeyValueDTO;
import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.web.content.filter.FilterHelper;
import org.gbif.portal.web.content.filter.PicklistHelper;
import org.gbif.portal.web.filter.CriterionDTO;
import org.springframework.web.servlet.ModelAndView;

/**
 * A Data Provider Id Filter Helper.
 * 
 * @author dmartin
 */
public class DataProviderIdFilterHelper implements FilterHelper, PicklistHelper {
	
	protected static Log logger = LogFactory.getLog(DataProviderIdFilterHelper.class);

	protected DataResourceManager dataResourceManager;
	
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
			DataProviderDTO dataProvider = dataResourceManager.getDataProviderFor(value);
			if(dataProvider==null)
				return null;
			return dataProvider.getName();
		} catch (ServiceException e){
			logger.error(e.getMessage(), e);
			return value;
		}
	}

	/**
	 * @see org.gbif.portal.web.content.filter.PicklistHelper#getPicklist()
	 */
	public Map<String, String> getPicklist(HttpServletRequest request, Locale locale) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		List<KeyValueDTO> dpkv = dataResourceManager.getDataProviderList();
		for(KeyValueDTO kv: dpkv)
			map.put(kv.getKey(), kv.getValue());
		return map;
	}	
	
	/**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#preProcess(java.util.List, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void preProcess(List<PropertyStoreTripletDTO> triplets,
			HttpServletRequest request, HttpServletResponse response) {}

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