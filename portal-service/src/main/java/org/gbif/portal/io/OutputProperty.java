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

public class OutputProperty {

	/** The bean for this property - nullable */
	private String beanName;
	/** The property name */
	private String propertyName;
	
	/**
	 * @return the beanName
	 */
	public String getBeanName() {
		return beanName;
	}
	/**
	 * @param beanName the beanName to set
	 */
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	/**
	 * @return the beanProperty
	 */
	public String getPropertyName() {
		return propertyName;
	}
	/**
	 * @param beanProperty the beanProperty to set
	 */
	public void setPropertyName(String beanProperty) {
		this.propertyName = beanProperty;
	}
}