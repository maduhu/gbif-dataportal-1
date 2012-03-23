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
package org.gbif.portal.service.triplet;

import java.util.Arrays;
import java.util.List;

import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.occurrence.BriefOccurrenceRecordDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.service.AbstractServiceTest;

/**
 * Junit tests for Occurrence TripletQueryManager implementations.
 * This is not exactly a true JUnit test but is enough to determine if the basic wiring and setup
 * is functioning
 * 
 * @see TripletManager
 * 
 * @author trobertson
 */
public class OccurrenceTripletManagerTest extends AbstractServiceTest {

	@SuppressWarnings("unchecked")
	public void testDoTripletQuery() throws Exception {
		TripletQueryManager manager = (TripletQueryManager) getBean("tripletOccurrenceManager");
		
		SearchResultsDTO results = manager.doTripletQuery(
				Arrays.asList(new PropertyStoreTripletDTO[]{
						new PropertyStoreTripletDTO("http://gbif.org/portal-service/2006/1.0", 
								"SERVICE.OCCURRENCE.QUERY.SUBJECT.SCIENTIFICNAME",
								"SERVICE.QUERY.PREDICATE.EQUAL",
								"Puma concolor")
				}),
				true, 
				new SearchConstraints(0, 100));
		
		assertNotNull(results);
		assertNotNull(results.getResults());
//		assertTrue(results.getResults().size()>0);
		List<BriefOccurrenceRecordDTO> data = results.getResults();
		for (BriefOccurrenceRecordDTO item : data) {
			logger.debug("Key[" + item.getKey() + "] InstitutionCode[" + item.getInstitutionCode() + "] CollectionCode[" + item.getCollectionCode() 
					+ "] CatalogueNumber[" + item.getCatalogueNumber() + "]");
		}
	}
}
