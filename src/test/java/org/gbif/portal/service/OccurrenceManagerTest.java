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
package org.gbif.portal.service;

import java.util.List;

import org.gbif.portal.dto.occurrence.IdentifierRecordDTO;
import org.gbif.portal.dto.occurrence.ImageRecordDTO;
import org.gbif.portal.dto.occurrence.LinkRecordDTO;
import org.gbif.portal.dto.occurrence.OccurrenceRecordDTO;
import org.gbif.portal.dto.occurrence.RawOccurrenceRecordDTO;

/**
 * JUnit tests for the OccurrenceManager service layer interface.
 * 
 * @author dmartin
 */
public class OccurrenceManagerTest extends AbstractServiceTest {

	public void testGetOccurrenceRecordFor() throws Exception{
		OccurrenceManager occurrenceManager = (OccurrenceManager) getBean("occurrenceManager");	
		OccurrenceRecordDTO occurrenceRecordDTO = occurrenceManager.getOccurrenceRecordFor("1");
		logger.info("Retrieved record: "+occurrenceRecordDTO);
	}
	
	public void testGetRawOccurrenceRecordFor() throws Exception{
		OccurrenceManager occurrenceManager = (OccurrenceManager) getBean("occurrenceManager");	
		RawOccurrenceRecordDTO rawOccurrenceRecordDTO = occurrenceManager.getRawOccurrenceRecordFor("1");
		logger.info("Retrieved record: "+rawOccurrenceRecordDTO);
	}
	
	public void testGetTotalOccurrenceRecordCount() throws Exception{
		OccurrenceManager occurrenceManager = (OccurrenceManager) getBean("occurrenceManager");	
		int count = occurrenceManager.getTotalOccurrenceRecordCount();
		logger.info("Retrieved record count: "+count);
	}	
	
	public void testGetImageRecordsForOccurrenceRecord() throws Exception{
		OccurrenceManager occurrenceManager = (OccurrenceManager) getBean("occurrenceManager");	
		List<ImageRecordDTO> list = occurrenceManager.getImageRecordsForOccurrenceRecord("4609");
		logger.info("Retrieved record count: "+list.size());
	}	
	
	public void testGetLinkRecordsForOccurrenceRecord() throws Exception{
		OccurrenceManager occurrenceManager = (OccurrenceManager) getBean("occurrenceManager");	
		List<LinkRecordDTO> list = occurrenceManager.getLinkRecordsForOccurrenceRecord("4609");
		logger.info("Retrieved record count: "+list.size());
	}	
	
	public void testGetIdentifierRecordsForOccurrenceRecord() throws Exception{
		OccurrenceManager occurrenceManager = (OccurrenceManager) getBean("occurrenceManager");	
		List<IdentifierRecordDTO> list = occurrenceManager.getIdentifierRecordsForOccurrenceRecord("4454");
		logger.info("Retrieved record count: "+list.size());
	}	
	
//	public void testHibernatePagination() throws Exception {
//		
//		TripletQueryManager tqm = (TripletQueryManager) getBean("tripletDetailedOccurrenceManager");
//		List<PropertyStoreTripletDTO> criteria = new ArrayList<PropertyStoreTripletDTO>();  
//		criteria.add(new PropertyStoreTripletDTO("http://gbif.org/portal-service/2006/1.0","SERVICE.OCCURRENCE.QUERY.SUBJECT.TR.FAMILYID", "SERVICE.QUERY.PREDICATE.EQUAL", 13144039L));
//		SearchResultsDTO sr = tqm.doTripletQuery(criteria, true, new SearchConstraints(0, 50000));
//		
//	}
	
//	public void testSimpleQueryDAO() throws Exception {
//		String simpleQuery = "select oc.id, dr.name, dp.name, tn.canonical, oc, ror from OccurrenceRecord as oc " +
//				"inner join oc.rawOccurrenceRecord ror " +
//				"inner join oc.dataResource dr " +
//				"inner join oc.dataProvider dp " +
//				"inner join oc.taxonName tn " +
//				"where  (oc.nubTaxonConcept.familyConceptId = ? )";
//		SimpleQueryDAO sqd = (SimpleQueryDAO) getBean("simpleOccurrenceQueryDAO");
//		ArrayList<Object> parameters = new ArrayList<Object>();
//		parameters.add(new Long(13144039));
//		sqd.outputResultsForQuery(simpleQuery, parameters, 0, 10, new VelocityResultsOutputter(System.out, "org/gbif/portal/io/darwin-core.vm", new DummyPropertyFormatter()));
//	}

//	public void  initMe(){
//		this.applicationContext = new ClassPathXmlApplicationContext(getConfigLocations());
//	}
//
//	public OccurrenceManagerTest(){
//		initMe();
//	}
//	
//	public static void main(String[] args) throws Exception{
//		OccurrenceManagerTest omt = new OccurrenceManagerTest();
//		omt.testSimpleQueryDAO();
//	}
	
//	
//	public void testSimplePSTest() throws Exception {
//		
//		DataSource ds = (DataSource) getBean("dataSource");
//		PreparedStatement ps = ds.getConnection().prepareStatement("select oc.*, ror.* from occurrence_record oc inner join raw_occurrence_record ror on oc.raw_occurrence_record_id=ror.id limit 43000");
//		ResultSet rs = ps.executeQuery();
//		rs.first();
//	}	
	
}
