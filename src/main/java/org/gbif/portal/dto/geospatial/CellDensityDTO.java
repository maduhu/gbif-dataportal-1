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
package org.gbif.portal.dto.geospatial;

import java.io.Serializable;

/**
 * Represents a cell with a key, along with a density count.
 * A cell may be of any size (0.1 degree, 1 degree, 10 degree for example)
 * depending on the context of the usage.  Similarly the count represented
 * may be a count of a particular Taxon Concept or Country or provider 
 * for example.
 * 
 * @author trobertson
 */
public class CellDensityDTO implements Serializable {

	private static final long serialVersionUID = 1713443359359856383L;

	/**
	 * The identifier for the cell
	 */
	protected int cellId;
	
	/**
	 * The density count
	 */
	protected int count;
	
	/**
	 * @param cellId To set
	 * @param count To set
	 */
	public CellDensityDTO(int cellId, int count) {
		this.cellId = cellId;
		this.count = count;
	}

	/**
	 * default
	 */
	public CellDensityDTO() {
	}

	/**
	 * Suitable for logging
	 */
	public String toString() {
		return cellId + ": " + count;
	}
	
	/**
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

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}
}