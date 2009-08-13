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

import org.gbif.portal.model.BaseObject;
import org.gbif.portal.model.ModelEntityType;

/**
 * Model Object for Resource Rank
 * 
 * @author dmartin
 */
public class ResourceRank extends BaseObject {

	/** Indicates the type of resource e.g. Images */
	protected ResourceType resourceType;
	/** The Entity Type that is being ranked. e.g. DataResource */
	protected ModelEntityType entityType;
	/** The id for this entity. E.g. the data resource id */
	protected long entityId;
	/** The actual ranking counting down from 1, with 1 being the highest rank */
	protected int rank;

	/**
	 * @return the entityId
	 */
	public long getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}

	/**
	 * @return the entityType
	 */
	public ModelEntityType getEntityType() {
		return entityType;
	}

	/**
	 * @param entityType the entityType to set
	 */
	public void setEntityType(ModelEntityType entityType) {
		this.entityType = entityType;
	}

	/**
	 * @return the rank
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * @param rank the rank to set
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * @return the resourceType
	 */
	public ResourceType getResourceType() {
		return resourceType;
	}

	/**
	 * @param resourceType the resourceType to set
	 */
	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}
}