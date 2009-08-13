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
package org.gbif.portal.model.occurrence;

import java.io.Serializable;

import org.gbif.portal.model.IntegerEnumType;
import org.gbif.portal.util.db.OccurrenceRecordUtils;

/**
 * Enumerated type for identifiers
 *
 * @author dhobern
 */
public class IdentifierType extends IntegerEnumType implements Serializable{
	/**
	 * Generated
	 */
	private static final long serialVersionUID = -6414459810689631419L;
	
	public static final IdentifierType GUID = new IdentifierType("guid", OccurrenceRecordUtils.IDENTIFIERTYPE_GUID);
	public static final IdentifierType FIELDNUMBER = new IdentifierType("field number", OccurrenceRecordUtils.IDENTIFIERTYPE_FIELDNUMBER);
	public static final IdentifierType COLLECTORNUMBER = new IdentifierType("collector number", OccurrenceRecordUtils.IDENTIFIERTYPE_COLLECTORNUMBER);
	public static final IdentifierType ACCESSIONNUMBER = new IdentifierType("accession number", OccurrenceRecordUtils.IDENTIFIERTYPE_ACCESSIONNUMBER);
	public static final IdentifierType SEQUENCENUMBER = new IdentifierType("sequence number", OccurrenceRecordUtils.IDENTIFIERTYPE_SEQUENCENUMBER);
	public static final IdentifierType OTHERCATALOGNUMBER = new IdentifierType("other catalog number", OccurrenceRecordUtils.IDENTIFIERTYPE_OTHERCATALOGNUMBER);
	
	public IdentifierType() {
		//default constructor, required by hibernate
	}
	
	private IdentifierType(String name, int value) {
		super(name, Integer.valueOf(value));
	}
	
	/**
	 * Utility method to return the enumerated instance for the specified name
	 * @param name The enumerated name value
	 * @return The enumerated instance if found or null
	 */
	public static final IdentifierType getIdentifierType(String name) {
		if (name != null) {
			if (name.equalsIgnoreCase(GUID.getName())) {
				return GUID;
			} else if (name.equalsIgnoreCase(FIELDNUMBER.getName())) {
				return FIELDNUMBER;
			} else if (name.equalsIgnoreCase(COLLECTORNUMBER.getName())) {
				return COLLECTORNUMBER;
			} else if (name.equalsIgnoreCase(ACCESSIONNUMBER.getName())) {
				return ACCESSIONNUMBER;
			} else if (name.equalsIgnoreCase(SEQUENCENUMBER.getName())) {
				return SEQUENCENUMBER;
			} else if (name.equalsIgnoreCase(OTHERCATALOGNUMBER.getName())) {
				return OTHERCATALOGNUMBER;
			} 
	
		}
		return null;
	}
	
	/**
	 * Utility method to return the enumerated instance for the specified value
	 * @param value The enumerated name integer value
	 * @return The enumerated instance if found or null
	 */
	public static final IdentifierType getBasisOfRecord(int value) {
		if (value == GUID.getValue()) {
			return GUID;
		} else if (value == FIELDNUMBER.getValue()) {
			return FIELDNUMBER;
		} else if (value == COLLECTORNUMBER.getValue()) {
			return COLLECTORNUMBER;
		} else if (value == ACCESSIONNUMBER.getValue()) {
			return ACCESSIONNUMBER;
		} else if (value == SEQUENCENUMBER.getValue()) {
			return SEQUENCENUMBER;
		} else if (value == OTHERCATALOGNUMBER.getValue()) {
			return OTHERCATALOGNUMBER;
		} 
		return null;
	}
}