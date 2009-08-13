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
package org.gbif.portal.model;

import java.util.Date;

/**
 * Model object for the index data table
 * @author trobertson
 */
public class IndexData extends ModelObject {
	/**
	 * Generated
	 */
	private static final long serialVersionUID = -6420465355727170854L;
	public static final int TYPE_SCIENTIFIC_NAME = 1;
	public static final int TYPE_CATALOGUE_NUMBER = 2;
	
	protected long resourceAccessPointId;
	protected int type = TYPE_SCIENTIFIC_NAME;
	protected String lowerLimit;
	protected String upperLimit;
	protected Date started;
	protected Date finished;
	
	/**
	 * Constructor
	 */
	public IndexData(long id, long resourceAccessPointId, int type, String lowerLimit, String upperLimit, Date started, Date finished) {
		this.id = id;
		this.resourceAccessPointId = resourceAccessPointId;
		this.type = type;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		this.started = started;
		this.finished = finished;
	}
	
	/**
	 * Constructor
	 */
	public IndexData(long resourceAccessPointId, int type, String lowerLimit, String upperLimit) {
		this.resourceAccessPointId = resourceAccessPointId;
		this.type = type;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
	}

	/**
	 * @return Returns the finished.
	 */
	public Date getFinished() {
		return finished;
	}
	/**
	 * @param finished The finished to set.
	 */
	public void setFinished(Date finished) {
		this.finished = finished;
	}
	/**
	 * @return Returns the lowerLimit.
	 */
	public String getLowerLimit() {
		return lowerLimit;
	}
	/**
	 * @param lowerLimit The lowerLimit to set.
	 */
	public void setLowerLimit(String lowerLimit) {
		this.lowerLimit = lowerLimit;
	}
	/**
	 * @return Returns the resourceAccessPointId.
	 */
	public long getResourceAccessPointId() {
		return resourceAccessPointId;
	}
	/**
	 * @param resourceAccessPointId The resourceAccessPointId to set.
	 */
	public void setResourceAccessPointId(long resourceAccessPointId) {
		this.resourceAccessPointId = resourceAccessPointId;
	}
	/**
	 * @return Returns the started.
	 */
	public Date getStarted() {
		return started;
	}
	/**
	 * @param started The started to set.
	 */
	public void setStarted(Date started) {
		this.started = started;
	}
	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return Returns the upperLimit.
	 */
	public String getUpperLimit() {
		return upperLimit;
	}
	/**
	 * @param upperLimit The upperLimit to set.
	 */
	public void setUpperLimit(String upperLimit) {
		this.upperLimit = upperLimit;
	}

	
}
