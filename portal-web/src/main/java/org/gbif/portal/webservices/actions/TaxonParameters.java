/***************************************************************************
 * Copyright (C) 2006 Global Biodiversity Information Facility Secretariat.  
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
package org.gbif.portal.webservices.actions;

import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.util.path.PathMapping;
import org.gbif.portal.webservices.util.GbifWebServiceException;


/**
 * @author Donald Hobern
 *
 */
public class TaxonParameters extends Parameters {
	
	public static final String TAXON_SERVICE_NAME ="taxon";
	
	public static final String TAXON_NAMESPACE = "http://portal.gbif.org/ws/response/gbif";
	public static final String TCS_NAMESPACE = "http://www.tdwg.org/schemas/tcs/1.01";
	public static final String TCS_LOCATION = "http://tdwg.napier.ac.uk/TCS_1.01/v101.xsd";
	
	public static final String KEY_FORMAT = "format";
	public static final String KEY_SCIENTIFICNAME = "scientificname";
	public static final String KEY_RANK = "rank";
	public static final String KEY_DATAPROVIDERKEY = "dataproviderkey";
	public static final String KEY_DATARESOURCEKEY = "dataresourcekey";
	public static final String KEY_RESOURCENETWORKKEY = "resourcenetworkkey";
	public static final String KEY_HOSTISOCOUNTRYCODE = "hostisocountrycode";
	public static final String KEY_KEY = "key";

	public static final int FORMAT_BRIEF = 0;
	public static final int FORMAT_TCS_1_01 = 1;
	
	public static final String FORMATNAME_BRIEF = "brief";
	public static final String FORMATNAME_TCS_1_01 = "tcs-1.01";

	protected int format = FORMAT_BRIEF;
	protected String scientificName = null;
	protected String rank = null;
	protected String hostIsoCountryCode = null;
	protected String dataProviderKey = null;
	protected String dataResourceKey = null;
	protected String resourceNetworkKey = null;
	protected String key = null;

	public static Log log = LogFactory.getLog(TaxonParameters.class);
	
	protected TaxonParameters() {
		// Null constructor
	}
	
	public String getServiceName() {
		return TAXON_SERVICE_NAME;
	}
	
	public TaxonParameters(Map<String, Object> params, PathMapping pathMapping)
		throws GbifWebServiceException
	{
		super(params, pathMapping);
		
		try {
			this.pathMapping = pathMapping;
			
			Set<String> keys = params.keySet();
			for (String k : keys) {
				Object value = params.get(k);
				
				if (k.equals(KEY_FORMAT)) {
					format = getFormat((String) value); 
				}
				else if (k.equals(KEY_SCIENTIFICNAME)) {
					scientificName = (String) value;
					int wildcardIndex = scientificName.indexOf("*");
					if (wildcardIndex < 0) {
						wildcardIndex = scientificName.indexOf("%");
					}
					if (wildcardIndex > 0 && wildcardIndex < MINIMUM_WILDCARD_INDEX) {
						throw new GbifWebServiceException("Scientific name string must include at least " + MINIMUM_WILDCARD_INDEX + " characters before the first wildcard character");
					}
				}
				else if (k.equals(KEY_RANK)) {
					rank = (String) value; 
				}
				else if (k.equals(KEY_DATAPROVIDERKEY)) {
					dataProviderKey = (String) value; 
				}
				else if (k.equals(KEY_DATARESOURCEKEY)) {
					dataResourceKey = (String) value; 
				}
				else if (k.equals(KEY_RESOURCENETWORKKEY)) {
					resourceNetworkKey = (String) value; 
				}
				else if (k.equals(KEY_HOSTISOCOUNTRYCODE)) {
					hostIsoCountryCode = ((String) value).toUpperCase(); 
				}
				else if (k.equals(KEY_KEY)) {
					key = (String) value; 
				}
			}
			
			if (requestType == Action.GET && key == null) {
				throw new GbifWebServiceException("Must provide key for get request");
			}
		}
		catch (GbifWebServiceException gwse) {
			throw(gwse);
		}
		catch (Exception e) {
			throw new GbifWebServiceException("TaxonParameters exception: " + e);
		}
	}

	/**
	 * Returns the request type name for a given KVP set.
	 */
	public String getRequestTypeName() {
		return Action.getRequestTypeName(requestType);
	}

	protected int getFormat(String formatString) {
		int format = FORMAT_BRIEF;
		
		if (formatString != null) {
			if (formatString.equals(FORMATNAME_TCS_1_01)) {
				format = FORMAT_TCS_1_01;
			}
		}
		
		return format;
	}
	
	public String getFormatName() {
		String name = FORMATNAME_BRIEF;
		
		switch(format) {
		case FORMAT_TCS_1_01: name = FORMATNAME_TCS_1_01;
		}
		
		return name;
	}
	
	public Map<String, Object> getParameterMap(Integer overrideRequestType) {
		Map<String,Object> map = super.getParameterMap(overrideRequestType);
		int rt = (overrideRequestType == null) ? requestType : overrideRequestType.intValue();
		if (rt != Action.SCHEMA && rt != Action.STYLESHEET) {
			if (requestType == Action.COUNT || requestType == Action.LIST) {
				if (scientificName != null) map.put(KEY_SCIENTIFICNAME, scientificName);
				if (rank != null) map.put(KEY_RANK, rank);
				if (dataProviderKey != null) map.put(KEY_DATAPROVIDERKEY, dataProviderKey);
				if (dataResourceKey != null) map.put(KEY_DATARESOURCEKEY, dataResourceKey);
				if (resourceNetworkKey != null) map.put(KEY_RESOURCENETWORKKEY, resourceNetworkKey);
				if (hostIsoCountryCode != null) map.put(KEY_HOSTISOCOUNTRYCODE, hostIsoCountryCode);
			}
			if (requestType == Action.LIST) {
				map.put(KEY_FORMAT, getFormatName());
			}
			if (requestType == Action.GET) {
				if (key != null) map.put(KEY_KEY, key);
			}
		}
		return map;
	}

	/**
	 * @return the dataProviderKey
	 */
	public String getDataProviderKey() {
		return dataProviderKey;
	}

	/**
	 * @return the dataResourceKey
	 */
	public String getDataResourceKey() {
		return dataResourceKey;
	}

	/**
	 * @return the resourceNetworkKey
	 */
	public String getResourceNetworkKey() {
		return resourceNetworkKey;
	}

	/**
	 * @return the format
	 */
	public int getFormat() {
		return format;
	}

	/**
	 * @return the hostIsoCountryCode
	 */
	public String getHostIsoCountryCode() {
		return hostIsoCountryCode;
	}

	/**
	 * @return the scientificName
	 */
	public String getScientificName() {
		return scientificName;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the rank
	 */
	public String getRank() {
		return rank;
	}
}