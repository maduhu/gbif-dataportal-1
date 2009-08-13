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
package org.gbif.portal.model.geospatial;

import java.io.Serializable;

import org.gbif.portal.model.ModelEntityType;

/**
 * CellDensityId represents the composite key for the Cell Density Model Object 
 */
public class CellDensityId implements Serializable{
	/**
	 * Generated
	 */
	private static final long serialVersionUID = 3871380942079292584L;

	/**
	 * The first part of the key identifying the cell in question
	 */
	protected long entityId;

	/**
	 * The second part of the key identifying the type of count contained 
	 */
	protected ModelEntityType type;
	
	/**
	 * The third part of the identifier for the cell
	 */
	protected int cellId;
	
	/**
     * @hibernate.key-property
     *	column="type"
	 * @return the type
	 */
	public ModelEntityType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ModelEntityType type) {
		this.type = type;
	}

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
	 * @hibernate.key-property
	 * 	column="cell_id"
	 * @return the cellId
	 */
	public int getCellId() {
		return cellId;
	}

	/**
	 * @param cellId the cellId to set
	 */
	public void setCellId(int cellId) {
		this.cellId = cellId;
	}
}