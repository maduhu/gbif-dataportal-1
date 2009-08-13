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

import java.util.List;

import org.gbif.portal.model.IndexData;

/**
 * The Index Data DAO
 * @author Tim Robertson
 */
public interface IndexDataDAO {
	/**
	 * Creates the indexData record
	 * @param indexData To create
	 * @return The ID of the newly created record
	 */
	public long create(IndexData indexData);
	
	/**
	 * Sets the start date on the identified record as now
	 * @param id To set the start on
	 */
	public void setStartAsNow(long id);
	
	/**
	 * Sets the finished date on the identified record as now
	 * @param id To set the finished on
	 */
	public void setFinishedAsNow(long id);
	
	/**
	 * Deletes the record
	 * @param id Identifying that to delete
	 */
	public void delete(long id);

	
	/**
	 * Deletes the record that are not finished
	 * @param rapId Identifying that to delete
	 */
	public void deleteAllUnfinished(long rapId);	
	
	
	/**
	 * Sets the started and finished to now() for the record that are not finished
	 * @param rapId Identifying that to delete
	 */
	public void deactivateAllUnfinished(long rapId);	
	
	/**
	 * Gets the indexData by the resourceAccessPointId that are not complete 
	 * @param rapId To find by
	 * @return The list of IndexData or an empty list
	 */
	public List<IndexData> findByResourceAccessPointId(long rapId);
}
