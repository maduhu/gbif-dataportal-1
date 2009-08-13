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
package reports.cites;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dao.OccurrenceRecordDAO;
import org.gbif.portal.dao.RelationshipAssertionDAO;
import org.gbif.portal.dao.TaxonConceptDAO;
import org.gbif.portal.dao.TaxonNameDAO;
import org.gbif.portal.model.RelationshipAssertion;
import org.gbif.portal.model.TaxonConcept;
import org.gbif.portal.util.mhf.message.Message;
import org.gbif.portal.util.mhf.message.MessageAccessException;
import org.gbif.portal.util.mhf.message.MessageParseException;
import org.gbif.portal.util.mhf.message.impl.xml.XMLMessageFactory;
import org.gbif.portal.util.mhf.message.impl.xml.accessor.ListMessageFromXPathAccessor;
import org.gbif.portal.util.mhf.message.impl.xml.accessor.StringFromXPathAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Generates some stats based on the CITES plants and animals lists of names
 * @author trobertson
 */
public class CitesReportXML {
	protected static Log log = LogFactory.getLog(CitesReportXML.class);
	
	// XPath accessors
	protected ListMessageFromXPathAccessor citesRecordAccessor;
	protected StringFromXPathAccessor citesLegListingAccessor;
	protected StringFromXPathAccessor citesGenusAccessor;
	protected StringFromXPathAccessor citesSpeciesAccessor;
	protected StringFromXPathAccessor citesSubspeciesAccessor;
	
	// DAOs
	protected TaxonNameDAO taxonNameDAO;
	protected TaxonConceptDAO taxonConceptDAO;
	protected OccurrenceRecordDAO occurrenceRecordDAO;
	protected RelationshipAssertionDAO relationshipAssertionDAO;
	
	
	// the entry point
	public void run() throws MessageParseException, MessageAccessException {
		//run("animals.xml");
		run("plants.xml");
	}

	// runs on the input file
	protected void run(String filename) throws MessageParseException, MessageAccessException {
		log.info("Starting CITES report on: " + filename);
		Message document = buildDocumentToMessage(filename);
		List<Message> records = citesRecordAccessor.invoke(document);		
		log.info("There are " + records.size() + " records");
		
		
		List<String> names = getNamesOfCitedType(records, "I");
		List<NameReport> report = reportOnNames(names);
		summarise("I", report);
		
		names = getNamesOfCitedType(records, "II");
		report = reportOnNames(names);
		summarise("II", report);
		
		names = getNamesOfCitedType(records, "III");
		report = reportOnNames(names);
		summarise("III", report);
		
	}
	
	// for the list of names, build the report
	protected List<NameReport> reportOnNames(List<String> names) {
		List<NameReport> report = new LinkedList<NameReport>();
		String topName = "";
		int topCount = 0;
		for (String name : names) {
			
			// 1 = use the nub
			List<TaxonConcept> concepts = taxonConceptDAO.getTaxonConcepts(name, 1);
			for (TaxonConcept concept : concepts) {
				NameReport nameReport = new NameReport(name);
				if (!concept.isAccepted()) {
					nameReport.setSynonym(true);
					List<RelationshipAssertion> assertions = relationshipAssertionDAO.getRelationshipAssertionsForFromConcept(concept.getId());
					
					int synonymOccurrenceCount = 0;
					// should be 1, but let's just sum them
					for (RelationshipAssertion assertion : assertions) {
						// only synonyms
						if (assertion.getRelationshipType() == 4) {
							long acceptedConcept = assertion.getToConceptId();
							synonymOccurrenceCount += occurrenceRecordDAO.countByNubId(acceptedConcept);
						}
					}
					
					nameReport.setExpandedSynonomyOccurrenceCount(synonymOccurrenceCount);
				}
				
				int occurrenceCount = occurrenceRecordDAO.countByNubId(concept.getId());
				nameReport.setOccurrenceCount(occurrenceCount);
				
				int geoOccurrenceCount = occurrenceRecordDAO.countByNubIdGeospatial(concept.getId());
				nameReport.setGeospatialoccurrenceCount(geoOccurrenceCount);
				
				if (topCount<occurrenceCount) {
					topCount = occurrenceCount;
					topName = name;
				}
				report.add(nameReport);
			}
						
		}
		log.info(topName + " has the most records: " + topCount);
		return report;
	}

	
	// summarises the name report list
	protected void summarise(String type, List<NameReport> report) {
		log.info("Type: " + type);
		int occurrenceCount = 0;
		int geoOccurrenceCount = 0;
		int synonomyOccurrenceCount = 0;
		int synonomyNameCount = 0;
		for (NameReport nameReport : report) {
			occurrenceCount += nameReport.getOccurrenceCount();
			if (nameReport.isSynonym) {
				synonomyNameCount++;
				synonomyOccurrenceCount += nameReport.getExpandedSynonomyOccurrenceCount();
			} 
			occurrenceCount += nameReport.getOccurrenceCount();
			geoOccurrenceCount += nameReport.getGeospatialoccurrenceCount();
		}
		log.info("  name count: " + report.size());
		log.info("  occurrence count: " + occurrenceCount);
		log.info("  geospatial occurrence count: " + geoOccurrenceCount);
		log.info("  synonymy name count: " + synonomyNameCount);
		log.info("  occurrence count of the accepted names: " + synonomyOccurrenceCount);
	}
	
	// gets the names of from the list of in the type
	protected List<String> getNamesOfCitedType(List<Message> masterList, String type) throws MessageAccessException {
		List<String> names = new LinkedList<String>();
		for (Message message : masterList) {
			String recordType = citesLegListingAccessor.invoke(message);
			if (StringUtils.equalsIgnoreCase(type, recordType)) {
				String genus = citesGenusAccessor.invoke(message);
				String species = citesSpeciesAccessor.invoke(message);
				String subspecies = citesSubspeciesAccessor.invoke(message);
				names.add(buildName(genus, species, subspecies));
			}			
		}
		return names;
		
	}
	
	// build a name
	protected String buildName(String genus, String species, String subspecies) {
		if (StringUtils.isNotBlank(subspecies)) {
			return genus + " " + species + " " + subspecies;
		} else {
			return genus + " " + species;
		}
	}
	
	// build the doc
	protected Message buildDocumentToMessage(String fileName) throws MessageParseException {
		XMLMessageFactory factory = new XMLMessageFactory();
		InputStream is = getClass().getResourceAsStream(fileName);
		return factory.build(is);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] cites = {"classpath*:org/gbif/portal/**/applicationContext-*.xml",
				"classpath*:reports/cites/applicationContext-cites.xml"};
		ApplicationContext context = new ClassPathXmlApplicationContext(cites);
		CitesReportXML app = (CitesReportXML) context.getBean("citesReportXML");
		try {
			app.run();
		} catch (Exception e) {
			log.error(e.getMessage(), e);			
		}
		System.exit(0);
	}

	/**
	 * @return Returns the citesRecordAccessor.
	 */
	public ListMessageFromXPathAccessor getCitesRecordAccessor() {
		return citesRecordAccessor;
	}

	/**
	 * @param citesRecordAccessor The citesRecordAccessor to set.
	 */
	public void setCitesRecordAccessor(ListMessageFromXPathAccessor citesRecordAccessor) {
		this.citesRecordAccessor = citesRecordAccessor;
	}

	/**
	 * @return Returns the citesGenusAccessor.
	 */
	public StringFromXPathAccessor getCitesGenusAccessor() {
		return citesGenusAccessor;
	}

	/**
	 * @param citesGenusAccessor The citesGenusAccessor to set.
	 */
	public void setCitesGenusAccessor(StringFromXPathAccessor citesGenusAccessor) {
		this.citesGenusAccessor = citesGenusAccessor;
	}

	/**
	 * @return Returns the citesSpeciesAccessor.
	 */
	public StringFromXPathAccessor getCitesSpeciesAccessor() {
		return citesSpeciesAccessor;
	}

	/**
	 * @param citesSpeciesAccessor The citesSpeciesAccessor to set.
	 */
	public void setCitesSpeciesAccessor(StringFromXPathAccessor citesSpeciesAccessor) {
		this.citesSpeciesAccessor = citesSpeciesAccessor;
	}

	/**
	 * @return Returns the citesSubspeciesAccessor.
	 */
	public StringFromXPathAccessor getCitesSubspeciesAccessor() {
		return citesSubspeciesAccessor;
	}

	/**
	 * @param citesSubspeciesAccessor The citesSubspeciesAccessor to set.
	 */
	public void setCitesSubspeciesAccessor(
			StringFromXPathAccessor citesSubspeciesAccessor) {
		this.citesSubspeciesAccessor = citesSubspeciesAccessor;
	}


	/**
	 * @return Returns the citesLegListingAccessor.
	 */
	public StringFromXPathAccessor getCitesLegListingAccessor() {
		return citesLegListingAccessor;
	}

	/**
	 * @param citesLegListingAccessor The citesLegListingAccessor to set.
	 */
	public void setCitesLegListingAccessor(
			StringFromXPathAccessor citesLegListingAccessor) {
		this.citesLegListingAccessor = citesLegListingAccessor;
	}

	public TaxonNameDAO getTaxonNameDAO() {
		return taxonNameDAO;
	}

	public void setTaxonNameDAO(TaxonNameDAO taxonNameDAO) {
		this.taxonNameDAO = taxonNameDAO;
	}

	public TaxonConceptDAO getTaxonConceptDAO() {
		return taxonConceptDAO;
	}

	public void setTaxonConceptDAO(TaxonConceptDAO taxonConceptDAO) {
		this.taxonConceptDAO = taxonConceptDAO;
	}

	public OccurrenceRecordDAO getOccurrenceRecordDAO() {
		return occurrenceRecordDAO;
	}

	public void setOccurrenceRecordDAO(OccurrenceRecordDAO occurrenceRecordDAO) {
		this.occurrenceRecordDAO = occurrenceRecordDAO;
	}

	public RelationshipAssertionDAO getRelationshipAssertionDAO() {
		return relationshipAssertionDAO;
	}

	public void setRelationshipAssertionDAO(
			RelationshipAssertionDAO relationshipAssertionDAO) {
		this.relationshipAssertionDAO = relationshipAssertionDAO;
	}
}
