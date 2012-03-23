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
 * Resource Type enumerated type.
 * 
 * @author dmartin
 */
public class ResourceType extends IntegerEnumType implements Serializable {

	private static final long serialVersionUID = 4595856724472502195L;

	public static final ResourceType IMAGE_DATA_RESOURCE = new ResourceType("image_data_resource", 1);

	public ResourceType() {
		//default constructor, required by hibernate
	}
	
	private ResourceType(String name, int value) {
		super(name, Integer.valueOf(value));
	}	
}