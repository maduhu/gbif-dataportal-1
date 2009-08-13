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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.GeospatialManager;
import org.gbif.portal.web.content.filter.FilterHelper;
import org.gbif.portal.web.content.filter.PicklistHelper;
import org.gbif.portal.web.filter.CriterionDTO;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * A picklist helper for the host country filter.
 * 
 * @author trobertson
 */
public class HostCountryPicklistHelper implements PicklistHelper, FilterHelper {
	/**
	 * Logger
	 */
	protected static Log logger = LogFactory.getLog(HostCountryPicklistHelper.class);

	/**
	 * Manager
	 */
	protected GeospatialManager geospatialManager;
	protected DataResourceManager dataResourceManager;
	
	/**
	 * For getting the i18n key based on the iso country code
	 */
	protected Map<String, String> isoCountryCodeToI18NKey;
	
	/**
	 * The message source
	 */
	protected MessageSource messageSource;
	
	public void addCriterion2Request(CriterionDTO criterionDTO,
			ModelAndView mav, HttpServletRequest request) {}

	public String getDefaultDisplayValue(HttpServletRequest request) {
		return null;
	}

	public String getDefaultValue(HttpServletRequest request) {
		return null;
	}

	public String getDisplayValue(String value, Locale locale) {
		return value;
	}

	/**
	 * Replace Host Country triplets with data provider id triplets,
	 * hence removing join. Host country is currently determined by the 
	 * iso_country_code on the data provider table.
	 * 
	 * @see org.gbif.portal.web.content.filter.FilterHelper#preProcess(java.util.List, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void preProcess(List<PropertyStoreTripletDTO> triplets,
			HttpServletRequest request, HttpServletResponse response) {
		
		try {
		
			if(triplets.size()==0)
				return;
			
			String namespace = triplets.get(0).getNamespace();
			
			List<PropertyStoreTripletDTO> hcTriplets = new ArrayList<PropertyStoreTripletDTO>();
			
			//look for BB filters
			for (PropertyStoreTripletDTO triplet: triplets){
				if("SERVICE.OCCURRENCE.QUERY.SUBJECT.HOSTCOUNTRYCODE".equals(triplet.getSubject())){
					hcTriplets.add(triplet);
				}
			}		
			
			List<Long> dataProviderIds = new ArrayList<Long>();
			
			for(PropertyStoreTripletDTO hcTriplet: hcTriplets){
				
				SearchResultsDTO searchResults = dataResourceManager.findDataProviders(null, 
						false, (String) hcTriplet.getObject(), null, new SearchConstraints(0,10000));
				List<DataProviderDTO> dps = searchResults.getResults();
				for(DataProviderDTO dp: dps){
					dataProviderIds.add(new Long(dp.getKey()));
				}
			}
			
			if(!dataProviderIds.isEmpty()){
				triplets.removeAll(hcTriplets);
				triplets.add(new PropertyStoreTripletDTO(namespace, 
						"SERVICE.OCCURRENCE.QUERY.SUBJECT.DATAPROVIDERID",
						"SERVICE.QUERY.PREDICATE.IN",
						dataProviderIds
					));
			}
			
		} catch(Exception e){
			logger.error(e.getMessage(), e);
		}
	}	
	
	/**
	 * Gets the ordered map based on the i18n values
	 * @see org.gbif.portal.web.content.filter.PicklistHelper#getPicklist(javax.servlet.http.HttpServletRequest)
	 */
	public Map<String, String> getPicklist(HttpServletRequest request, Locale locale) {
		Locale theLocale = null;
		if(request!=null)
			theLocale = RequestContextUtils.getLocale(request);
		else
			theLocale = locale;
		
		List<String> isoCodes = geospatialManager.getHostCountryISOCountryCodes();
		Map<String, String> i18nValue2IsoCode = new TreeMap<String,String>();
		for (String code : isoCodes) {
			if (code != null && !isoCountryCodeToI18NKey.containsKey(code)) {
				logger.warn("Received an unknown country code[" + code + "] - ignoring from map...");
			} else if (code != null) {
				// get the i18n value of it, and use that to key it
				String i18nValue;
				try {
					i18nValue = messageSource.getMessage("country."+code, null, theLocale);
					i18nValue2IsoCode.put(i18nValue, code);					
				} catch (NoSuchMessageException e) {
					logger.warn("Received a country code[" + code + "] that has no translation to language[" + theLocale + "]- ignoring from map...");
				}
			}			
		}
		Map<String, String> orderedIsoCodeToI18NValue = new LinkedHashMap<String,String>();
		for (String i18nValue : i18nValue2IsoCode.keySet()) {
			orderedIsoCodeToI18NValue.put(i18nValue2IsoCode.get(i18nValue), i18nValue);
		}
		return orderedIsoCodeToI18NValue;
	}

	/**
	 * @param geospatialManager The geospatialManager to set.
	 */
	public void setGeospatialManager(GeospatialManager geospatialManager) {
		this.geospatialManager = geospatialManager;
	}

	/**
	 * @param isoCountryCodeToI18NKey The isoCountryCodeToI18NKey to set.
	 */
	public void setIsoCountryCodeToI18NKey(
			Map<String, String> isoCountryCodeToI18NKey) {
		this.isoCountryCodeToI18NKey = isoCountryCodeToI18NKey;
	}

	/**
	 * @param messageSource The messageSource to set.
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @param logger the logger to set
	 */
	public static void setLogger(Log logger) {
		HostCountryPicklistHelper.logger = logger;
	}

	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}
}