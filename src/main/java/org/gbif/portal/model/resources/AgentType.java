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

import java.io.Serializable;

import org.gbif.portal.model.IntegerEnumType;

/**
 * Enumerated type for agent roles.
 *
 * @author dhobern
 */
public class AgentType extends IntegerEnumType implements Serializable{

	/**
	 * Generated
	 */
	private static final long serialVersionUID = 5996545372350396239L;
	
	public static final AgentType UNKNOWN = new AgentType("unknown", 0);
	public static final AgentType DATAADMINISTRATOR = new AgentType("data administrator", 1);
	public static final AgentType SYSTEMADMINISTRATOR = new AgentType("system administrator", 2);
	
	public AgentType() {
		//default constructor, required by hibernate
	}
	
	private AgentType(String name, int value) {
		super(name, Integer.valueOf(value));
	}
	
	/**
	 * Utility method to return the enumerated instance for the specified name
	 * @param name The enumerated name value
	 * @return The enumerated instance if found or null
	 */
	public static final AgentType getBasisOfRecord(String name) {
		if (name != null) {
			if (name.equalsIgnoreCase(UNKNOWN.getName())) {
				return UNKNOWN;
			} else if (name.equalsIgnoreCase(DATAADMINISTRATOR.getName())) {
				return DATAADMINISTRATOR;
			} else if (name.equalsIgnoreCase(SYSTEMADMINISTRATOR.getName())) {
				return SYSTEMADMINISTRATOR;
			} 
	
		}
		return null;
	}
	
	/**
	 * Utility method to return the enumerated instance for the specified value
	 * @param value The enumerated name integer value
	 * @return The enumerated instance if found or null
	 */
	public static final AgentType getBasisOfRecord(int value) {
		if (value == UNKNOWN.getValue()) {
			return UNKNOWN;
		} else if (value == DATAADMINISTRATOR.getValue()) {
			return DATAADMINISTRATOR;
		} else if (value == SYSTEMADMINISTRATOR.getValue()) {
			return SYSTEMADMINISTRATOR;
		} 
		return null;
	}
}