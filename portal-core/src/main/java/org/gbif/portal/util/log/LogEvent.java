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
package org.gbif.portal.util.log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.gbif.portal.util.enumeration.Enumeration;

/**
 * Enumerated type for Log Event
 * 
 * @author Donald Hobern
 */
public class LogEvent extends Enumeration implements Serializable {

	/**
	 * Generated
	 */
	private static final long serialVersionUID = -7326893175621088447L;
	
	/**
	 * Maps to support get methods
	 */
	private static Map<String, Enumeration> nameMap = new HashMap<String, Enumeration>();
	private static Map<Integer, Enumeration> valueMap = new HashMap<Integer, Enumeration>();

	/**
	 * Log Event values
	 */
	
	public static final LogEvent UNKNOWN = new LogEvent("unknown", 0);
	public static final LogEvent LOGGROUP_CLOSE = new LogEvent("logGroupClose", -1);

	// 1-999 - Harvester messages
	public static final int HARVEST_RANGE_START = 1;
	public static final int HARVEST_RANGE_END = 999;
	public static final LogEvent HARVEST_BEGIN = new LogEvent("harvestBegin", HARVEST_RANGE_START);
	public static final LogEvent HARVEST_END = new LogEvent("harvestEnd", HARVEST_RANGE_START + 1);
	public static final LogEvent HARVEST_MULTIPLEPREFERREDIDENTIFICATIONS = new LogEvent("harvestMultiplePreferredIdentifications", HARVEST_RANGE_START + 2);
	public static final LogEvent HARVEST_MULTIPLEUNPREFERREDIDENTIFICATIONS = new LogEvent("harvestMultipleUnpreferredIdentifications", HARVEST_RANGE_START + 3);
	
	// 1001-1999 - Extracter messages
	public static final int EXTRACT_RANGE_START = 1001;
	public static final int EXTRACT_RANGE_END = 1999;
	public static final LogEvent EXTRACT_BEGIN = new LogEvent("extractBegin", EXTRACT_RANGE_START);
	public static final LogEvent EXTRACT_END = new LogEvent("extractEnd", EXTRACT_RANGE_START + 1);
	public static final LogEvent EXTRACT_SCIENTIFICNAMEPARSEISSUE = new LogEvent("extractScientificNameParseIssue", EXTRACT_RANGE_START + 2);
	public static final LogEvent EXTRACT_SCIENTIFICNAMEPARSECOUNT = new LogEvent("extractScientificNameParseCount", EXTRACT_RANGE_START + 3);
	public static final LogEvent EXTRACT_TAXONRANKPARSEISSUE = new LogEvent("extractTaxonRankParseIssue", EXTRACT_RANGE_START + 4);
	public static final LogEvent EXTRACT_BASISOFRECORDPARSEISSUE = new LogEvent("extractBasisOfRecordParseIssue", EXTRACT_RANGE_START + 5);
	public static final LogEvent EXTRACT_COUNTRYNAMEPARSEISSUE = new LogEvent("extractCountryNameParseIssue", EXTRACT_RANGE_START + 6);
	public static final LogEvent EXTRACT_GEOSPATIALISSUE = new LogEvent("extractGeospatialParseIssue", EXTRACT_RANGE_START + 7);
	public static final LogEvent EXTRACT_DUPLICATEREMOTECONCEPTID = new LogEvent("extractDuplicateRemoteConceptId", EXTRACT_RANGE_START + 8);
	public static final LogEvent EXTRACT_MISSINGPARENTTAXON = new LogEvent("extractMissingParentTaxon", EXTRACT_RANGE_START + 9);
	public static final LogEvent EXTRACT_MISSINGACCEPTEDTAXON = new LogEvent("extractMissingAcceptedTaxon", EXTRACT_RANGE_START + 10);
	public static final LogEvent EXTRACT_AMBIGUOUSSCIENTIFICNAME = new LogEvent("extractAmbiguousScientificName", EXTRACT_RANGE_START + 11);
	public static final LogEvent EXTRACT_TEMPORALISSUE = new LogEvent("extractTemporalParseIssue", EXTRACT_RANGE_START + 12);
	
	// 2001-2999 - User messages
	public static final int USER_RANGE_START = 2001;
	public static final int USER_RANGE_END = 2999;
	public static final LogEvent USER_SESSION_BEGIN = new LogEvent("userSessionBegin", USER_RANGE_START);
	public static final LogEvent USER_SESSION_END = new LogEvent("userSessionEnd", USER_RANGE_START + 1);
	public static final LogEvent USER_FEEDBACK_OCCURRENCE = new LogEvent("userFeedbackOccurrence", USER_RANGE_START + 2);
	public static final LogEvent USER_FEEDBACK_TAXON = new LogEvent("userFeedbackTaxon", USER_RANGE_START + 3);
	
	// 3001 - 3999 - Usage stats
	public static final int USAGE_RANGE_START = 3001;
	public static final int USAGE_RANGE_END = 3999;
	public static final LogEvent USAGE_OCCURRENCE_SEARCH = new LogEvent("occurrenceSearch", USAGE_RANGE_START);
	public static final LogEvent USAGE_DATASET_METADATA_VIEW = new LogEvent("datasetMetadataView", USAGE_RANGE_START+1);
	public static final LogEvent USAGE_TAXON_VIEW = new LogEvent("taxonView", USAGE_RANGE_START+2);
	public static final LogEvent USAGE_TAXON_BROWSE = new LogEvent("taxonBrowse", USAGE_RANGE_START+4);
	public static final LogEvent USAGE_OCCURRENCE_VIEW = new LogEvent("occurrenceView", USAGE_RANGE_START+5);	
	public static final LogEvent USAGE_DATAPROVIDER_METADATA_VIEW = new LogEvent("dataproviderMetadataView", USAGE_RANGE_START+6);
	public static final LogEvent USAGE_DATASET_OCCURRENCE_DOWNLOAD = new LogEvent("datasetOccurrenceDownload", USAGE_RANGE_START+7);
	public static final LogEvent USAGE_DATASET_TAXONOMY_DOWNLOAD = new LogEvent("datasetTaxonomyDownload", USAGE_RANGE_START+8);
	public static final LogEvent USAGE_DATASET_LOG_DOWNLOAD = new LogEvent("datasetLogDownload", USAGE_RANGE_START+9);
	
	protected LogEvent(String name, Integer value) { 
		super(nameMap, valueMap, name, value); 
	}
	
	public static LogEvent get(String name) {
		return (LogEvent) nameMap.get(name);
	}

	public static LogEvent get(Integer value) {
		return (LogEvent) valueMap.get(value);
	}
	
	public static Map<String, Enumeration> getNameMap(){
		return nameMap;
	}
	
	public static Map<Integer, Enumeration> getValueMap(){
		return valueMap;
	}
}