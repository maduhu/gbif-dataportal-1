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
package org.gbif.portal.harvest.taxonomy;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dao.TaxonNameDAO;
import org.gbif.portal.model.TaxonName;
import org.gbif.portal.util.log.GbifLogMessage;
import org.gbif.portal.util.log.GbifLogUtils;
import org.gbif.portal.util.log.LogEvent;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * Will parse a scientific name into it's components based on the 
 * configured regular expressions
 * 
 * For example:
 * 	Aus bus subsp. cus 
 * 
 * May be parsed to 
 * - Aus
 *   - Aus bus
 *     - Aus bus subsp. cus
 *     
 * This does not deal with authors in any way
 * 
 * @author trobertson
 */
public class ScientificNameParser {
	/**
	 * Logger
	 */
	protected Log logger = LogFactory.getLog(ScientificNameParser.class);
	
	/**
	 * Parser outcome states
	 */
	public static final int PARSED = 0;
	public static final int NOT_PARSED_BAD_FORMAT = 1;
	public static final int NOT_PARSED_AMBIGUOUS = 2;
	
	/**
	 * The mapping
	 */
	protected List<RegularExpressionToTaxonName> orderedRegularExpresionsMapping;
	
	/**
	 * The taxon name DAO
	 */
	protected TaxonNameDAO taxonNameDAO;
	
	/**
	 * Utilities
	 */
	protected GbifLogUtils gbifLogUtils;
	protected Map<String,String> contextKeyHigherTaxonDisambiguationMap;
	
	/**
	 * Using the configured regular expressions in order, will attempt to parse the name into 
	 * it's components, appending each to the classification.  The author is ignored and overwritten
	 * with that supplied.
	 * @param name To parse
	 * @param classification To append to
	 * @param author To use, regardless of the parsed author
	 * @param suppliedRank To use, regardless of the parser rank
	 * @return one of the parser outcome states that are defined as static INTs 
	 */
	public int parse(ProcessContext context, String name, List<TaxonName> classification, String author, Integer suppliedRank) {
		TaxonName parsedName = new TaxonName(); 
		for (RegularExpressionToTaxonName matcher : orderedRegularExpresionsMapping) {
			boolean parsed = matcher.parse(name, parsedName, suppliedRank);
			
			if (parsed) {
				// Protect ourselves against mishandling higher taxon names as genus level names when we have clues otherwise
				if (suppliedRank == null && parsedName.getRank() >= 6000 && parsedName.getRank() < 7000) {
					
					//First see if the name appears in the classification already under a different rank
					for (TaxonName existing : classification) {
						if (existing.getCanonical().equals(parsedName.getCanonical())) {
							if (existing.getRank() == 6000 && parsedName.getRank() > 6000) {
								// Allow for genus with matching lower ranked genus group name
							} else {
								// Consider this already handled as a higher rank
								// Tim: I am leaving this in case I need to back track
								// This break does not break out of the full loop, and thus it continues with further parsers - so "Aus sp."
								// will be found and then parsed again - the net result is lots of logs saying that the name is not parsable, when in 
								// fact it has been correctly handled.
								// thus we know that the important part of the name has been parsed already - e.g. Aus has been found previously so we can 
								// with a bit of confidence say it is parsed - although it was done previously
								//parsed = false;
								//break;
								return PARSED; // see above comment - it wasn't parsed but we know it is a match...
							}
						}
					}
					
					// Now check whether there are other higher taxa of the same name already in the system
					if (parsed) {
						List<TaxonName> matchingHigherTaxonNames = taxonNameDAO.getByCanonicalAndLowestRank(parsedName.getCanonical(), 6000);
						boolean haveGenus = false;
						boolean haveHigherRank = false;
						boolean haveMultipleHigherRanks = false;
						Integer higherRank = null;
						
						for(TaxonName matchingName : matchingHigherTaxonNames) {
							if (matchingName.getRank() == 6000) {
								haveGenus = true;
							} else {
								haveHigherRank = true;
								if (higherRank == null) {
									higherRank = matchingName.getRank();
								} else if (matchingName.getRank() != higherRank) {
									haveMultipleHigherRanks = true;
								}
							}
						}
						
						if (haveGenus) {
							if (haveHigherRank) {
								// Difficult case - could be the genus or the higher rank
								// TODO consider if we can do better
								// Basically we use a map to try to disambiguate tough cases where the
								// decision is actually well known (e.g. Polychaeta)
								parsed = false;
								if (contextKeyHigherTaxonDisambiguationMap != null) {
									String rankString = contextKeyHigherTaxonDisambiguationMap.get(parsedName.getCanonical());
									if (rankString != null) {
										parsedName.setRank(new Integer(rankString));
										parsed = true;
										// Check this is compatible with the classification
										for (TaxonName existing : classification) {
											if (existing.getRank() >= parsedName.getRank()) {
												parsed = false;
												break;
											}
										}
									}
								}
							} else {
								// else we are reasonably happy
							}
						} else if (haveHigherRank) {
							// We only know this name as a rank higher than genus
							if (haveMultipleHigherRanks) {
								// Too much choice - ignore the name
								parsed = false;
							} else {
								// Check that this fits below the other supplied ranks
								for (TaxonName existing : classification) {
									if (existing.getRank() >= higherRank) {
										parsed = false;
										break;
									}
								}
								if (parsed) {
									parsedName.setRank(higherRank);
								}
							}
						}

						if (!parsed) {
							if (context != null) {
								// removed - why would we do context based logging here, in something workflow agnostic?
								//GbifLogMessage message = gbifLogUtils.createGbifLogMessage(context, LogEvent.EXTRACT_AMBIGUOUSSCIENTIFICNAME, name + ": ambiguous scientific name could not be processed");
								//message.setCountOnly(true);
								//logger.error(message);
							} else {
								logger.error("Scientific name [" + name + "] ambiguous and could not be processed");
							}
							return NOT_PARSED_AMBIGUOUS;
						}
					}
				}
				
				if (parsed) {
					logger.debug("Parsed: " + name);
					classification.add(parsedName);
	
					if (author != null) {
						logger.debug("Overriding Author with: " + author);
						classification.get(classification.size()-1).setAuthor(author);
					}
					return PARSED;
				}
			}
		}
		if (StringUtils.isNotEmpty(name)) {
			logger.info("Not parsed: " + name);
		} else {
			// there are always lots of empty names so keep logging down
			logger.debug("Not parsed as name is empty");
		}
		return NOT_PARSED_BAD_FORMAT;
	}

	/**
	 * Using the configured regular expressions in order, will attempt to parse the name into 
	 * it's components, appending each to the classification
	 * @param name To parse
	 * @param classification To append to
	 * @return one of the parser outcome states that are defined as static INTs 
	 */
	public int parse(ProcessContext context, String name, List<TaxonName> classification) {
		return parse(context, name, classification, null, null);
	}

	/**
	 * Using the configured regular expressions in order, will attempt to parse the name into 
	 * it's components, appending each to the classification.  The author is ignored and overwritten
	 * with that supplied.
	 * @param name To parse
	 * @param classification To append to
	 * @param author To use, regardless of the parsed author
	 * @return one of the parser outcome states that are defined as static INTs 
	 */
	public int parse(ProcessContext context, String name, List<TaxonName> classification, String author) {
		return parse(context, name, classification, author, null);
	}

	/**
	 * Using the configured regular expressions in order, will attempt to parse the name into 
	 * it's components, appending each to the classification.  The author is ignored and overwritten
	 * with that supplied.
	 * @param name To parse
	 * @param classification To append to
	 * @param author To use, regardless of the parsed author
	 * @return one of the parser outcome states that are defined as static INTs 
	 */
	public int parse(ProcessContext context, String name, List<TaxonName> classification, Integer suppliedRank) {
		return parse(context, name, classification, null, suppliedRank);
	}

	/**
	 * @return Returns the orderedRegularExpresionsMapping.
	 */
	public List<RegularExpressionToTaxonName> getOrderedRegularExpresionsMapping() {
		return orderedRegularExpresionsMapping;
	}

	/**
	 * @param orderedRegularExpresionsMapping The orderedRegularExpresionsMapping to set.
	 */
	public void setOrderedRegularExpresionsMapping(
			List<RegularExpressionToTaxonName> orderedRegularExpresionsMapping) {
		this.orderedRegularExpresionsMapping = orderedRegularExpresionsMapping;
	}

	/**
	 * @return the taxonNameDAO
	 */
	public TaxonNameDAO getTaxonNameDAO() {
		return taxonNameDAO;
	}

	/**
	 * @param taxonNameDAO the taxonNameDAO to set
	 */
	public void setTaxonNameDAO(TaxonNameDAO taxonNameDAO) {
		this.taxonNameDAO = taxonNameDAO;
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
	 * @return the contextKeyHigherTaxonDisambiguationMap
	 */
	public Map<String, String> getContextKeyHigherTaxonDisambiguationMap() {
		return contextKeyHigherTaxonDisambiguationMap;
	}

	/**
	 * @param contextKeyHigherTaxonDisambiguationMap the contextKeyHigherTaxonDisambiguationMap to set
	 */
	public void setContextKeyHigherTaxonDisambiguationMap(
			Map<String, String> contextKeyHigherTaxonDisambiguationMap) {
		this.contextKeyHigherTaxonDisambiguationMap = contextKeyHigherTaxonDisambiguationMap;
	}
}
