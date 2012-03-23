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
package org.gbif.portal.dao;

import java.util.Date;
import java.util.List;

import org.gbif.portal.model.ResourceAccessPoint;

/**
 * The Resource Access Point DAO
 * @author trobertson
 */
public interface ResourceAccessPointDAO {
	/**
	 * Creates the record
	 * @param resourceAccessPoint To create
	 * @return The id of the created record
	 */
	public long create(ResourceAccessPoint resourceAccessPoint);
	
	/**
	 * Updates the record or creates one if it doesn't exist
	 * @param resourceAccessPoint To update.  Should the ID be null, then create is called 
	 * @return The id of the created record or updated
	 */
	public long updateOrCreate(ResourceAccessPoint resourceAccessPoint);

	/**
	 * Gets all the resource access points.
	 * @return The list of ResourceAccessPoints or an empty list
	 */
	public List<ResourceAccessPoint> getAll();
	
	/**
	 * Gets the list of ResourceAccessPoints that are associated with the UUID given
	 * @param uuid To search for
	 * @return The list of ResourceAccessPoints or an empty list
	 */
	public List<ResourceAccessPoint> getByUuid(String uuid);
	
	/**
	 * Gets the ResourceAccessPoint by the ID
	 * @param id Th ID
	 * @return The ResourceAccessPoint or null
	 */
	public ResourceAccessPoint getById(long id);
	
	/**
	 * @param date The date to set the start harvest 
	 */
	public void setStartHarvest(Date date, long id);
	
	/**
	 * @param date The date to set the start extract
	 */
	public void setStartExtract(Date date, long id);
	
	/**
	 * @param supports true if it supports the value
	 */
	public void setSupportsDateLastModified(boolean supports, long id);
	
	/**
	 * Used to determine if it *appears* that a harvest worker is currently
	 * accessing the URL using another RAP
	 * This is implementation specific but will most likely check to see if the 
	 * first 20 chars of the URL are being accessed by a worker using a RAP different
	 * to that provided 
	 * @param url To see if being touched
	 * @param id Except using this RAP id
	 * @return true if another RAP is being used to hit the URL
	 */
	public boolean isURLBeingHarvested(String url, long id); 
	
	/**
	 * Gets a RAP for the a data resource based on it's URL and remote id
	 * @param remoteId
	 * @param URL
	 * @param dataResourceId
	 * @return
	 */
	public ResourceAccessPoint getByRemoteIdAtUrlAndUrlForResource(String remoteId, String URL, long dataResourceId);
}
