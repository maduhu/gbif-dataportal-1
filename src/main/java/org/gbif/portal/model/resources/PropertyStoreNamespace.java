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

/**
 * The namespace within the property store
 * 
 * @author tim robertson
 */
public class PropertyStoreNamespace extends BaseObject {
	/**
	 * The namespace itself
	 */
	protected String namespace;

	/**
	 * @return Returns the namespace.
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * @param namespace The namespace to set.
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * Returns just the namespace
	 * This must not change as it is used to map directly to the List<String> on the resource access point
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getNamespace();
	}
}