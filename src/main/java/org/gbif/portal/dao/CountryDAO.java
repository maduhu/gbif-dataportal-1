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
package org.gbif.portal.dao;

import org.gbif.portal.model.Country;

/**
 * The Country mapping DAO
 * @author Donald Hobern
 */
public interface CountryDAO {
	/**
	 * Gets the Country for the given iso country code
	 * @param isoCountryCode To search for
	 * @return country 
	 */
	public Country getByIsoCountryCode(String isoCountryCode);
	/**
	 * Gets the Country for the given country name
	 * @param isoCountryCode To search for
	 * @return country 
	 */	
	public Country getByCountryName(String countryName);
}
