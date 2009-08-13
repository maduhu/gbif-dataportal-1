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

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.harvest.taxonomy.ScientificNameParser;
import org.gbif.portal.model.TaxonName;
import org.gbif.portal.util.log.GbifLogMessage;
import org.gbif.portal.util.log.GbifLogUtils;
import org.gbif.portal.util.log.LogEvent;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * An activity that will use a RegularExpressionToTaxonName to build a TaxonName and insert it into the 
 * classification list. 
 * 
 * @author trobertson
 */
public class TaxonNameToClassificationActivity extends BaseActivity implements Activity {	
	/**
	 * Context Keys
	 */
	protected String contextKeyName;
	protected String contextKeyClassificationList;
	protected String contextKeyAuthor;
	protected String contextKeyRawOccurrenceRecord;
	protected String contextKeyParsedRank;
	
	/**
	 * The parser to work on the name
	 */
	protected ScientificNameParser parser;
	
	/**
	 * The parser to work on the name
	 */
	protected GbifLogUtils gbifLogUtils;
	
	/**
	 * Default value for parsedRank - this is to allow parsers for data using defined ranks to enforce interpretation as genus level names
	 */
	protected Integer defaultParsedRank = null;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		String name = (String) context.get(getContextKeyName(), String.class, false);
		if (StringUtils.trimToNull(name) != null) {			
			List<TaxonName> classification = (List<TaxonName>) context.get(getContextKeyClassificationList(), List.class, false);
			if (classification == null) {
				classification = new LinkedList<TaxonName>();
				context.put(getContextKeyClassificationList(), classification);
			}

			Integer parsedRank = defaultParsedRank;
			if (getContextKeyParsedRank() != null) {
				parsedRank = (Integer) context.get(getContextKeyParsedRank(), Integer.class, false);
			}
			
			String author = null;
			if (getContextKeyAuthor() != null) {
				author = (String) context.get(getContextKeyAuthor(), String.class, false);
			}
			
			int parserResult = parser.parse(context, name, classification, author, parsedRank); 
			if (parserResult == ScientificNameParser.NOT_PARSED_BAD_FORMAT) {
				GbifLogMessage message = gbifLogUtils.createGbifLogMessage(context, LogEvent.EXTRACT_SCIENTIFICNAMEPARSEISSUE, "Scientific name not parsed [" + name + "]");
				message.setCountOnly(true);
				logger.error(message);
			} else if (parserResult == ScientificNameParser.PARSED) {
				GbifLogMessage message 
					= gbifLogUtils.createGbifLogMessage(context, LogEvent.EXTRACT_SCIENTIFICNAMEPARSECOUNT);
				message.setCountOnly(true);
				logger.debug(message);
			} else if (parserResult == ScientificNameParser.NOT_PARSED_AMBIGUOUS) {
				GbifLogMessage message = gbifLogUtils.createGbifLogMessage(context, LogEvent.EXTRACT_AMBIGUOUSSCIENTIFICNAME, "Scientific name ambiguous without specifying rank [" + name + "]");
				message.setCountOnly(true);
				logger.error(message);
			}
		}
		return context;
	}

	/**
	 * @return Returns the contextKeyClassificationList.
	 */
	public String getContextKeyClassificationList() {
		return contextKeyClassificationList;
	}

	/**
	 * @param contextKeyClassificationList The contextKeyClassificationList to set.
	 */
	public void setContextKeyClassificationList(String contextKeyClassificationList) {
		this.contextKeyClassificationList = contextKeyClassificationList;
	}

	/**
	 * @return Returns the contextKeyName.
	 */
	public String getContextKeyName() {
		return contextKeyName;
	}

	/**
	 * @param contextKeyName The contextKeyName to set.
	 */
	public void setContextKeyName(String contextKeyName) {
		this.contextKeyName = contextKeyName;
	}

	/**
	 * @return Returns the parser.
	 */
	public ScientificNameParser getParser() {
		return parser;
	}

	/**
	 * @param parser The parser to set.
	 */
	public void setParser(ScientificNameParser parser) {
		this.parser = parser;
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
	 * @return the defaultParsedRank
	 */
	public Integer getDefaultParsedRank() {
		return defaultParsedRank;
	}

	/**
	 * @param defaultParsedRank the defaultParsedRank to set
	 */
	public void setDefaultParsedRank(Integer defaultParsedRank) {
		this.defaultParsedRank = defaultParsedRank;
	}
}