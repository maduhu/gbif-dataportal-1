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
package org.gbif.portal.web.controller.taxonomy.bean;

public class NamesList {

	private String nameList;
	private boolean isScientific = true;
	private boolean mapSynonymsToAccepted = true;
	
	/**
	 * @return the isScientific
	 */
	public boolean isScientific() {
		return isScientific;
	}
	
	/**
	 * @return the isScientific
	 */
	public boolean getIsScientific() {
		return isScientific;
	}	
	/**
	 * @param isScientific the isScientific to set
	 */
	public void setScientific(boolean isScientific) {
		this.isScientific = isScientific;
	}
	/**
	 * @param isScientific the isScientific to set
	 */
	public void setIsScientific(boolean isScientific) {
		this.isScientific = isScientific;
	}	
	
	/**
	 * @return the nameList
	 */
	public String getNameList() {
		return nameList;
	}
	/**
	 * @param nameList the nameList to set
	 */
	public void setNameList(String nameList) {
		this.nameList = nameList;
	}
	/**
	 * @return the mapSynonymsToAccepted
	 */
	public boolean isMapSynonymsToAccepted() {
		return mapSynonymsToAccepted;
	}
	/**
	 * @param mapSynonymsToAccepted the mapSynonymsToAccepted to set
	 */
	public void setMapSynonymsToAccepted(boolean mapSynonymsToAccepted) {
		this.mapSynonymsToAccepted = mapSynonymsToAccepted;
	}
}