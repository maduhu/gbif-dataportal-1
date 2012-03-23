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
package org.gbif.portal.dao;

import java.util.List;

import org.gbif.portal.dao.geospatial.GeoRegionDAO;
import org.gbif.portal.model.geospatial.GeoRegion;
import org.gbif.portal.model.occurrence.OccurrenceRecord;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * @author davejmartin
 */
public class GeoRegionDAOTest extends AbstractDependencyInjectionSpringContextTests {

	/**
	 * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#getConfigLocations()
	 */
	protected String[] getConfigLocations() {
		return new String [] {
				"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml",
				"classpath*:/org/gbif/portal/**/impl/applicationContext-*-test.xml",
				"classpath*:org/gbif/portal/dao/applicationContext-dao-ro.xml",
				"classpath*:org/gbif/portal/dao/applicationContext-factories.xml",
				"classpath*:/org/gbif/portal/service/impl/applicationContext-service-test.xml"				
		};
	}
	
	public void testGetOccurrencesForRegion(){
		GeoRegionDAO grDAO = (GeoRegionDAO) this.applicationContext.getBean("geoRegionDAORO");
		List<OccurrenceRecord> ors = grDAO.getOccurrencesForGeoRegion(1l, 0, 1);
		if(ors!=null && !ors.isEmpty())
			logger.debug(ors.get(0));
	}
	
	public void testGetGeoRegionsForOccurrenceRecord(){
		GeoRegionDAO grDAO = (GeoRegionDAO) this.applicationContext.getBean("geoRegionDAORO");
		List<GeoRegion> ors = grDAO.getGeoRegionsForOccurrenceRecord(1l);
		if(ors!=null && !ors.isEmpty())		
			logger.debug(ors.get(0));
	}	
}
