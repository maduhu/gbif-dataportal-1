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
package org.gbif.portal.model.resources;

import java.util.Date;
import java.util.List;

import org.gbif.portal.model.BaseObject;

/**
 * A Resource Access Point encapsulates the connection parameters required to contact 
 * a resource on the network
 * 
 * @author tim robertson
 * 
 * @hibernate.class
 * 	table="resource_access_point"
 */
public class ResourceAccessPoint extends BaseObject {
	/**
	 * The provider which hosts the resource this access point will reach
	 */
	protected DataProvider dataProvider;
	
	/**
	 * The resource represented at the end of this access point
	 */
	protected DataResource dataResource;
	
	/** 
	 * The deletion date
	 */
	protected Date deleted;	
	
	/**
	 * The url for the resource
	 */
	protected String url;
	
	/**
	 * The remote identifier at the url (E.g. digir resource)
	 */
	protected String remoteIdAtUrl;	
	
	/**
	 * The Namespaces in the property store that the resource access point has associated 
	 * with it
	 */
	protected List<PropertyStoreNamespace> propertyStoreNamespaces;

	/**
	 * @return Returns the dataProvider.
	 */
	public DataProvider getDataProvider() {
		return dataProvider;
	}

	/**
	 * @param dataProvider The dataProvider to set.
	 */
	public void setDataProvider(DataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	/**
	 * @return Returns the dataResource.
	 */
	public DataResource getDataResource() {
		return dataResource;
	}

	/**
	 * @param dataResource The dataResource to set.
	 */
	public void setDataResource(DataResource dataResource) {
		this.dataResource = dataResource;
	}

	/**
	 * @return Returns the propertyStoreNamespaces.
	 */
	public List<PropertyStoreNamespace> getPropertyStoreNamespaces() {
		return propertyStoreNamespaces;
	}

	/**
	 * @param propertyStoreNamespaces The propertyStoreNamespaces to set.
	 */
	public void setPropertyStoreNamespaces(List<PropertyStoreNamespace> propertyStoreNamespaces) {
		this.propertyStoreNamespaces = propertyStoreNamespaces;
	}

	/**
	 * @return Returns the remoteIdAtUrl.
	 */
	public String getRemoteIdAtUrl() {
		return remoteIdAtUrl;
	}

	/**
	 * @param remoteIdAtUrl The remoteIdAtUrl to set.
	 */
	public void setRemoteIdAtUrl(String remoteIdAtUrl) {
		this.remoteIdAtUrl = remoteIdAtUrl;
	}

	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url The url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the deleted
	 */
	public Date getDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}
	
	
}
