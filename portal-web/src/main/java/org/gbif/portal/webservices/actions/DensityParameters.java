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
import org.gbif.portal.dto.util.EntityType;
import org.gbif.portal.util.path.PathMapping;
import org.gbif.portal.webservices.util.GbifWebServiceException;


/**
 * @author Donald Hobern
 *
 */
public class DensityParameters extends Parameters {
	
	public static final String DENSITY_SERVICE_NAME ="density";
	public static final String DENSITY_NAMESPACE = "http://portal.gbif.org/ws/response/gbif";
	
	public static final String KEY_TAXONCONCEPTKEY = "taxonconceptkey";
	public static final String KEY_DATAPROVIDERKEY = "dataproviderkey";
	public static final String KEY_DATARESOURCEKEY = "dataresourcekey";
	public static final String KEY_RESOURCENETWORKKEY = "resourcenetworkkey";
	public static final String KEY_ORIGINISOCOUNTRYCODE = "originisocountrycode";

	protected String key = null;
	protected EntityType entityType = null;

	public static Log log = LogFactory.getLog(DensityParameters.class);
	
	protected DensityParameters() {
		// Null constructor
	}
	
	public String getServiceName() {
		return DENSITY_SERVICE_NAME;
	}
	
	public DensityParameters(Map<String, Object> params, PathMapping pathMapping)
		throws GbifWebServiceException
	{
		super(params, pathMapping);
		
		try {
			this.pathMapping = pathMapping;
			
			Set<String> keys = params.keySet();

			for (String k : keys) {
				Object value = params.get(k);
				
				if (k.equals(KEY_TAXONCONCEPTKEY)) {
					key = (String) value;
					entityType = EntityType.TYPE_TAXON;
				}
				else if (k.equals(KEY_DATAPROVIDERKEY)) {
					key = (String) value;
					entityType = EntityType.TYPE_DATA_PROVIDER;
				}
				else if (k.equals(KEY_DATARESOURCEKEY)) {
					key = (String) value;
					entityType = EntityType.TYPE_DATA_RESOURCE;
				}
				else if (k.equals(KEY_RESOURCENETWORKKEY)) {
					key = (String) value;
					entityType = EntityType.TYPE_RESOURCE_NETWORK;
				}
				else if (k.equals(KEY_ORIGINISOCOUNTRYCODE)) {
					key = (String) value;
					entityType = EntityType.TYPE_COUNTRY;
				}
			}
			
			if (requestType == Action.LIST && key == null) {
				throw new GbifWebServiceException("Must provide key for list request");
			}
		}
		catch (GbifWebServiceException gwse) {
			throw(gwse);
		}
		catch (Exception e) {
			throw new GbifWebServiceException("DensityParameters exception: " + e);
		}
	}

	/**
	 * Returns the request type name for a given KVP set.
	 */
	public String getRequestTypeName() {
		return Action.getRequestTypeName(requestType);
	}

	public Map<String, Object> getParameterMap(Integer overrideRequestType) {
		Map<String,Object> map = super.getParameterMap(overrideRequestType);
		int rt = (overrideRequestType == null) ? requestType : overrideRequestType.intValue();
		if (rt == Action.LIST) {
			if (entityType == EntityType.TYPE_COUNTRY) {
				map.put(KEY_ORIGINISOCOUNTRYCODE, key);
			} else if (entityType == EntityType.TYPE_DATA_PROVIDER) {
				map.put(KEY_DATAPROVIDERKEY, key);
			} else if (entityType == EntityType.TYPE_DATA_RESOURCE) {
				map.put(KEY_TAXONCONCEPTKEY, key);
			} else if (entityType == EntityType.TYPE_RESOURCE_NETWORK) {
				map.put(KEY_RESOURCENETWORKKEY, key);
			} else if (entityType == EntityType.TYPE_TAXON) {
				map.put(KEY_TAXONCONCEPTKEY, key);
			}
			map.put(KEY_FORMAT, getFormatName());
		}
		return map;
	}

	/**
	 * @return the entityType
	 */
	public EntityType getEntityType() {
		return entityType;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
}