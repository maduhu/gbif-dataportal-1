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
 * Enumerated type for links
 *
 * @author dhobern
 */
public class LinkType extends IntegerEnumType implements Serializable{
	/**
	 * Generated
	 */
	private static final long serialVersionUID = -6414459810689631419L;
	
	public static final LinkType UNKNOWN = new LinkType("unknown", OccurrenceRecordUtils.LINKTYPE_UNKNOWN);
	public static final LinkType OCCURRENCEPAGE = new LinkType("occurrence page", OccurrenceRecordUtils.LINKTYPE_OCCURRENCEPAGE);
	
	public LinkType() {
		//default constructor, required by hibernate
	}
	
	private LinkType(String name, int value) {
		super(name, Integer.valueOf(value));
	}
	
	/**
	 * Utility method to return the enumerated instance for the specified name
	 * @param name The enumerated name value
	 * @return The enumerated instance if found or null
	 */
	public static final LinkType getIdentifierType(String name) {
		if (name != null) {
			if (name.equalsIgnoreCase(UNKNOWN.getName())) {
				return UNKNOWN;
			} else if (name.equalsIgnoreCase(OCCURRENCEPAGE.getName())) {
				return OCCURRENCEPAGE;
			} 
		}
		return null;
	}
	
	/**
	 * Utility method to return the enumerated instance for the specified value
	 * @param value The enumerated name integer value
	 * @return The enumerated instance if found or null
	 */
	public static final LinkType getBasisOfRecord(int value) {
		if (value == UNKNOWN.getValue()) {
			return UNKNOWN;
		} else if (value == OCCURRENCEPAGE.getValue()) {
			return OCCURRENCEPAGE;
		} 
		return null;
	}
}