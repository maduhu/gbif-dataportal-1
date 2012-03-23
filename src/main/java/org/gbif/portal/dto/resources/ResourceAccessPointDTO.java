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

package org.gbif.portal.dto.resources;

import java.io.Serializable;

/**
 * This is for transferring data about a resource network
 * 
 * @author dmartin
 */
public class ResourceAccessPointDTO implements Serializable {
	
	/**
	 * Generated
	 */
	private static final long serialVersionUID = -5778016990265460671L;
	
	/**
	 * The key
	 */
	protected String key;
	
	/**
	 * The provider which hosts the resource this access point will reach
	 */
	protected String dataProviderKey;
	
	/**
	 * The resource represented at the end of this access point
	 */
	protected String dataResourceKey;
	
	/**
	 * The url for the resource
	 */
	protected String url;
	
	/**
	 * The remote identifier at the url (E.g. digir resource)
	 */
	protected String remoteIdAtUrl;

	/**
	 * @return the dataProviderKey
	 */
	public String getDataProviderKey() {
		return dataProviderKey;
	}

	/**
	 * @param dataProviderKey the dataProviderKey to set
	 */
	public void setDataProviderKey(String dataProviderKey) {
		this.dataProviderKey = dataProviderKey;
	}

	/**
	 * @return the dataResourceKey
	 */
	public String getDataResourceKey() {
		return dataResourceKey;
	}

	/**
	 * @param dataResourceKey the dataResourceKey to set
	 */
	public void setDataResourceKey(String dataResourceKey) {
		this.dataResourceKey = dataResourceKey;
	}

	/**
	 * @return the remoteIdAtUrl
	 */
	public String getRemoteIdAtUrl() {
		return remoteIdAtUrl;
	}

	/**
	 * @param remoteIdAtUrl the remoteIdAtUrl to set
	 */
	public void setRemoteIdAtUrl(String remoteIdAtUrl) {
		this.remoteIdAtUrl = remoteIdAtUrl;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}	

}