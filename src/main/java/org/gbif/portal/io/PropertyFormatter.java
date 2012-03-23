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
package org.gbif.portal.io;

/**
 * Interface implemented by classes wishing to provide property formatting.
 * Implementations will take a propertyName, decide if they can format it in some way
 * and then format the supplied property value where applicable.
 * 
 * An example an application is replacing a value with an internationalized value.
 * 
 * @author dmartin
 */
public interface PropertyFormatter {
	
	/**
	 * Format the supplied property if the property name supplied is recognised.
	 * 
	 * @param propertyName
	 * @param propertyValue
	 * @return the formatted value
	 */
	public String format(String propertyName, String propertyValue);
}