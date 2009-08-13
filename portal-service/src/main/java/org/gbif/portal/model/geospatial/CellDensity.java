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


/**
 * CellDensity Model Object represents an occurrence population density for a particular type of 
 * concept (Taxon, Country, Provider etc) within a keyed grid.
 * 
 * At the time of writing -180, -90 is grid keyed 0, -179, -90 being 1 etc etc 
 *
 * @author trobertson
 *
 * @hibernate.class
 * 	table="cell_density"
 * 
 */
public class CellDensity implements Serializable{

	private static final long serialVersionUID = 3538387461976291159L;

	/**
	 * The composite identifier for the CellDensity
	 */
	protected CellDensityId identifier;
		
	/**
	 * The count of the occurrences of the conceptId
	 */
	protected int count;

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

	/**
	 * @return the identifier
	 */
	public CellDensityId getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifer the identifier to set
	 */
	public void setIdentifier(CellDensityId identifer) {
		this.identifier = identifer;
	}
}