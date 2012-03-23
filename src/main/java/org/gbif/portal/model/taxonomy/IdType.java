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
package org.gbif.portal.model.taxonomy;

import java.io.Serializable;

import org.gbif.portal.model.IntegerEnumType;

/**
 * Enumerated type for the type of a remote concept id
 *
 * @author dhobern
 */
public class IdType extends IntegerEnumType implements Serializable{

	/**
	 * Generated
	 */
	private static final long serialVersionUID = 5768452506070357983L;
	
	public static final IdType UNKNOWN = new IdType("unknown", 0);
	public static final IdType LOCALID = new IdType("localid", 1);
	public static final IdType URL = new IdType("url", 2);
	public static final IdType OTHERID = new IdType("otherid", 3);
	public static final IdType SPECIALIST = new IdType("specialist", 4);
	public static final IdType SCRUTINYDATE = new IdType("scrutinydate", 5);
	public static final IdType GUID = new IdType("guid", 6);
	
	// 0 unknown
	// 1 remote "guid" - e.g. name_code in COL
	// 2 link to provider record
	// 3 other remote id - used for tracing - COL it is the taxa table key
	// 4 Specialist name
	// 5 Scrutiny date
	// 6 guid
	
	public IdType() {
		//default constructor, required by hibernate
	}
	
	private IdType(String name, int value) {
		super(name, Integer.valueOf(value));
	}
	
	/**
	 * Utility method to return the enumerated instance for the specified name
	 * @param name The enumerated name value
	 * @return The enumerated instance if found or null
	 */
	public static final IdType getIdType(String name) {
		if (name != null) {
			if (name.equalsIgnoreCase(LOCALID.getName())) {
				return LOCALID;
			} else if (name.equalsIgnoreCase(URL.getName())) {
				return URL;
			} else if (name.equalsIgnoreCase(OTHERID.getName())) {
				return OTHERID;
			} else if (name.equalsIgnoreCase(SPECIALIST.getName())) {
				return SPECIALIST;
			} else if (name.equalsIgnoreCase(SCRUTINYDATE.getName())) {
				return SCRUTINYDATE;
			} else if (name.equalsIgnoreCase(GUID.getName())) {
				return GUID;
			}
		}
		
		return UNKNOWN;
	}
	
	/**
	 * Utility method to return the enumerated instance for the specified value
	 * @param value The enumerated name integer value
	 * @return The enumerated instance if found or null
	 */
	public static final IdType getIdType(int value) {
		if (value == LOCALID.getValue()) {
			return LOCALID;
		} else if (value == URL.getValue()) {
			return URL;
		} else if (value == OTHERID.getValue()) {
			return OTHERID;
		} else if (value == SPECIALIST.getValue()) {
			return SPECIALIST;
		} else if (value == SCRUTINYDATE.getValue()) {
			return SCRUTINYDATE;
		} else if (value == GUID.getValue()) {
			return GUID;
		}

		return UNKNOWN;
	}
}