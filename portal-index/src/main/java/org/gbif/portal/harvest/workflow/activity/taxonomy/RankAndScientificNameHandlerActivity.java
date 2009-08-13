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
package org.gbif.portal.harvest.workflow.activity.taxonomy;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.model.RawOccurrenceRecord;
import org.gbif.portal.util.log.GbifLogMessage;
import org.gbif.portal.util.log.GbifLogUtils;
import org.gbif.portal.util.log.LogEvent;
import org.gbif.portal.util.mapping.CodeMapping;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * Rank and scientific name handler handles the situation in which a taxon
 * is specified by a rank name and a scientific name, by selecting an appropriate
 * rank-specific context key and moving the scientific name there (and setting
 * an infraspecific marker if appropriate).
 * 
 * If a previousClassificationContainer is passed, this activity also initialises the
 * classification with any missing higher ranks from the previous classification
 * list. 
 * 
 * @author Donald Hobern
 */
public class RankAndScientificNameHandlerActivity extends BaseActivity implements Activity {	
	/**
	 * Context Keys
	 */
	protected String contextKeyRank;
	protected String contextKeyScientificName;
	protected String contextKeyAuthor;
	protected String contextKeyInfraspecificMarker;
	protected String contextKeyParsedRank;
	protected String contextKeyRawOccurrenceRecord;
	protected CodeMapping taxonRankMapping;
	protected Map ranksToNameContextKeys;
	protected Map ranksToAuthorContextKeys;
	protected Map ranksToInfraspecificMarkers;
	protected GbifLogUtils gbifLogUtils;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		String rankString = (String) context.get(getContextKeyRank(), String.class, false);
		if (rankString != null) {
			Integer rank = taxonRankMapping.mapToCode(StringUtils.chomp(rankString, "."));
			if (rank == null) {
				StringBuffer sb = new StringBuffer();
				sb.append(rankString);
				sb.append(": rank string not mapped");
				
				if (getContextKeyRawOccurrenceRecord() != null) {
					RawOccurrenceRecord ror = (RawOccurrenceRecord) context.get(getContextKeyRawOccurrenceRecord(), RawOccurrenceRecord.class, false);
					if(ror != null) {
						sb.append(" raw occurrence record: ");
						sb.append(ror.getId());
						if(ror.getCollectionCode()!=null){
							sb.append("; collection code: ");
							sb.append(ror.getCollectionCode());
						}
						if(ror.getInstitutionCode()!=null){
							sb.append("; catalogue number: ");
							sb.append(ror.getInstitutionCode());
						}
						if(ror.getCatalogueNumber()!=null){
							sb.append("; catalogue number: ");
							sb.append(ror.getCatalogueNumber());
						}
					}
				}
				GbifLogMessage message = gbifLogUtils.createGbifLogMessage(context, LogEvent.EXTRACT_TAXONRANKPARSEISSUE, sb.toString());
				message.setCountOnly(true);
				logger.error(message);
				throw new UnknownRankException("Could not find a mapping for rank string: " + rankString);
			}
			context.put(getContextKeyParsedRank(), rank);
			String contextKey = (String) ranksToNameContextKeys.get(rank.toString());
			if (contextKey != null && !contextKey.equals(getContextKeyScientificName())) {
				String scientificName = (String) context.get(getContextKeyScientificName(), String.class, false);
				// If the name is of species rank or lower and starts with an upper case
				// character, assume it is a full scientific name
				if (scientificName != null && (rank < 7000 || Character.isLowerCase(scientificName.charAt(0)))) {
					String rankValue = (String) context.get(contextKey, String.class, false);
					if (rankValue == null) {
						context.put(contextKey, scientificName);
						context.remove(getContextKeyScientificName());
					}
				}
			}
			contextKey = (String) ranksToAuthorContextKeys.get(rank.toString());
			if (contextKey != null && !contextKey.equals(getContextKeyAuthor())) {
				String author = (String) context.get(getContextKeyAuthor(), String.class, false);
				if (author != null) {
					String rankValue = (String) context.get(contextKey, String.class, false);
					if (rankValue == null) {
						context.put(contextKey, author);
						context.remove(getContextKeyAuthor());
					}
				}
			}
			String infraspecificMarker = (String) ranksToInfraspecificMarkers.get(rank);
			if (infraspecificMarker != null) {
				String currentMarker = (String) context.get(getContextKeyInfraspecificMarker(), String.class, false);
				if (currentMarker == null) {
					context.put(getContextKeyInfraspecificMarker(), infraspecificMarker);
				}
			}
		}
		return context;
	}

	/**
	 * @return the contextKeyInfraspecificMarker
	 */
	public String getContextKeyInfraspecificMarker() {
		return contextKeyInfraspecificMarker;
	}

	/**
	 * @param contextKeyInfraspecificMarker the contextKeyInfraspecificMarker to set
	 */
	public void setContextKeyInfraspecificMarker(
			String contextKeyInfraspecificMarker) {
		this.contextKeyInfraspecificMarker = contextKeyInfraspecificMarker;
	}

	/**
	 * @return the contextKeyRank
	 */
	public String getContextKeyRank() {
		return contextKeyRank;
	}

	/**
	 * @param contextKeyRank the contextKeyRank to set
	 */
	public void setContextKeyRank(String contextKeyRank) {
		this.contextKeyRank = contextKeyRank;
	}

	/**
	 * @return the contextKeyScientificName
	 */
	public String getContextKeyScientificName() {
		return contextKeyScientificName;
	}

	/**
	 * @param contextKeyScientificName the contextKeyScientificName to set
	 */
	public void setContextKeyScientificName(String contextKeyScientificName) {
		this.contextKeyScientificName = contextKeyScientificName;
	}

	/**
	 * @return the ranksToNameContextKeys
	 */
	public Map getRanksToNameContextKeys() {
		return ranksToNameContextKeys;
	}

	/**
	 * @param ranksToNameContextKeys the ranksToNameContextKeys to set
	 */
	public void setRanksToNameContextKeys(Map ranksToNameContextKeys) {
		this.ranksToNameContextKeys = ranksToNameContextKeys;
	}

	/**
	 * @return the ranksToInfraspecificMarkers
	 */
	public Map getRanksToInfraspecificMarkers() {
		return ranksToInfraspecificMarkers;
	}

	/**
	 * @param ranksToInfraspecificMarkers the ranksToInfraspecificMarkers to set
	 */
	public void setRanksToInfraspecificMarkers(Map ranksToInfraspecificMarkers) {
		this.ranksToInfraspecificMarkers = ranksToInfraspecificMarkers;
	}

	/**
	 * @return the taxonRankMapping
	 */
	public CodeMapping getTaxonRankMapping() {
		return taxonRankMapping;
	}

	/**
	 * @param taxonRankMapping the taxonRankMapping to set
	 */
	public void setTaxonRankMapping(CodeMapping taxonRankMapping) {
		this.taxonRankMapping = taxonRankMapping;
	}

	/**
	 * @return the contextKeyAuthor
	 */
	public String getContextKeyAuthor() {
		return contextKeyAuthor;
	}

	/**
	 * @param contextKeyAuthor the contextKeyAuthor to set
	 */
	public void setContextKeyAuthor(String contextKeyAuthor) {
		this.contextKeyAuthor = contextKeyAuthor;
	}

	/**
	 * @return the ranksToAuthorContextKeys
	 */
	public Map getRanksToAuthorContextKeys() {
		return ranksToAuthorContextKeys;
	}

	/**
	 * @param ranksToAuthorContextKeys the ranksToAuthorContextKeys to set
	 */
	public void setRanksToAuthorContextKeys(Map ranksToAuthorContextKeys) {
		this.ranksToAuthorContextKeys = ranksToAuthorContextKeys;
	}

	/**
	 * @return the contextKeyParsedRank
	 */
	public String getContextKeyParsedRank() {
		return contextKeyParsedRank;
	}

	/**
	 * @param contextKeyParsedRank the contextKeyParsedRank to set
	 */
	public void setContextKeyParsedRank(String contextKeyParsedRank) {
		this.contextKeyParsedRank = contextKeyParsedRank;
	}

	/**
	 * @return the gbifLogUtils
	 */
	public GbifLogUtils getGbifLogUtils() {
		return gbifLogUtils;
	}

	/**
	 * @param gbifLogUtils the gbifLogUtils to set
	 */
	public void setGbifLogUtils(GbifLogUtils gbifLogUtils) {
		this.gbifLogUtils = gbifLogUtils;
	}

	/**
	 * @return the contextKeyRawOccurrenceRecord
	 */
	public String getContextKeyRawOccurrenceRecord() {
		return contextKeyRawOccurrenceRecord;
	}

	/**
	 * @param contextKeyRawOccurrenceRecord the contextKeyRawOccurrenceRecord to set
	 */
	public void setContextKeyRawOccurrenceRecord(
			String contextKeyRawOccurrenceRecord) {
		this.contextKeyRawOccurrenceRecord = contextKeyRawOccurrenceRecord;
	}
}