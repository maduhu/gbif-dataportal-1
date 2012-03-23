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
package org.gbif.portal.harvest.workflow.activity.tag;

/**
 * Placeholder interface for tag id enums.
 * 
 * @author dmartin
 */
public interface TagIds {

	/** Tag id for host country - country tag */
	public static final int HOSTCOUNTRY_COUNTRY_TAG_ID = 2001;
	
	/** Keywords associated with this dataset */
	public static final int DATA_RESOURCE_KEYWORDS = 4000;
	
	public static final int DATA_RESOURCE_OCCURRENCES_COUNTRY = 4100;
	
	public static final int DATA_RESOURCE_OCCURRENCES_BOUNDING_BOX = 4101;
	
	public static final int DATA_RESOURCE_OCCURRENCES_WKT_POLYGON = 4102;
	
	public static final int DATA_RESOURCE_OCCURRENCES_DATE_RANGE = 4120;
	
	public static final int DATA_RESOURCE_OCCURRENCES_MONTH = 4121;
	
	public static final int DATA_RESOURCE_OCCURRENCES_SPECIES = 4140;
	
	public static final int DATA_RESOURCE_OCCURRENCES_GENUS = 4141;
	
	public static final int DATA_RESOURCE_OCCURRENCES_FAMILY = 4142;
	
	public static final int DATA_RESOURCE_TAXONOMIC_SCOPE = 4150; 
	
	public static final int DATA_RESOURCE_ASSOCIATED_KINGDOM = 4151;

	/** Species common names associated with this resource */
	public static final int DATA_RESOURCE_COMMON_NAMES = 4152;

	/** Indicates whether a data resource contains type records */
	public static final int DATA_RESOURCE_CONTAINS_TYPE_SPECIMENS = 4160;

	/** Names of collectors who have collected for this dataset */
	public static final int DATA_RESOURCE_COLLECTOR = 4161;
}