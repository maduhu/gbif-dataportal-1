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

import org.gbif.portal.model.BaseObject;

/**
 * Encapsulates a name for a country in a specific locale. Hence there
 * will be multiple country names for a single country with different locales.
 * 
 * @hibernate.class
 * 	table="country_name"
 * 
 * @author dmartin
 */
public class CountryName  extends BaseObject{

	/** The Country this name relates to */
	protected Country country;
	/** The name */
	protected String name;
	/** A more searchable name with vowels stripped out */
	protected String searchableName;
	/** The ISO Country Code */
	protected String isoCountryCode;
	/** The locale for this name, 2 letter string */
	protected String locale;
	
	/**
	 * @hibernate.many-to-one
	 * 	column="iso_country_code"
	 * 	not-null="true"
	 * 
	 * @return the country
	 */
	public Country getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(Country country) {
		this.country = country;
	}
	
	/**
     * @hibernate.property
     * 	column="iso_country_code"
     * 	insert="false"
	 * update="false"
     *  
	 * @return the isoCountryCode
	 */
	public String getIsoCountryCode() {
		return isoCountryCode;
	}
	/**
	 * @param isoCountryCode the isoCountryCode to set
	 */
	public void setIsoCountryCode(String isoCountryCode) {
		this.isoCountryCode = isoCountryCode;
	}
	
	/**
     * @hibernate.property
     * 	column="locale"
	 * 
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}
	/**
	 * @param locale the locale to set
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	/**
     * @hibernate.property
     * 	column="name"
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
     * @hibernate.property
     * 	column="searchable_name"
	 * 
	 * @return the searchableName
	 */
	public String getSearchableName() {
		return searchableName;
	}
	
	/**
	 * @param searchableName the searchableName to set
	 */
	public void setSearchableName(String searchableName) {
		this.searchableName = searchableName;
	}
}