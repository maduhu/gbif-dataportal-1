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

package org.gbif.portal.web.content.map;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.util.geospatial.LatLongBoundingBox;
import org.gbif.portal.web.content.geospatial.ZoomLevel;

/**
 * Map Content Provider tests
 * 
 * @author Dave Martin
 */
public class MapContentProviderTest extends TestCase {

	protected static Log logger = LogFactory.getLog(MapContentProviderTest.class);
	
	public void testGetBoundingBoxForZoomLevel(){

		LatLongBoundingBox llbb = MapContentProvider.getBoundingBoxForZoomLevel(ZoomLevel.ZOOM_LEVEL_6, (106f+105f)/2, (-11f-10f)/2);
		logger.debug(llbb);
		MapContentProvider.roundLatLongValues(llbb);
		logger.debug(llbb);
	}
}