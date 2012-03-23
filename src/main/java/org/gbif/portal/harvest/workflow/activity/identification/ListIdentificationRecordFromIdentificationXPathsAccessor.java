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

package org.gbif.portal.harvest.workflow.activity.identification;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.XPath;
import org.gbif.portal.util.mapping.impl.MapCodeMapping;
import org.gbif.portal.util.mhf.message.Message;
import org.gbif.portal.util.mhf.message.MessageAccessException;
import org.gbif.portal.util.mhf.message.MessageAccessor;
import org.gbif.portal.util.mhf.message.MessageParseException;


/**
 * Takes an XPath Expression and invokes the "getParts()" on the Message to return a 
 * List<Message> as the response.
 * 
 * @author Donald Hobern
 */
public class ListIdentificationRecordFromIdentificationXPathsAccessor implements MessageAccessor {
	/**
	 * Used to access the message
	 */
	protected XPath rootXPath;
	protected XPath scientificNameXPath;
	protected XPath alternateScientificNameXPath;
	protected XPath identifierXPath;
	protected XPath alternateIdentifierXPath;
	protected XPath dateXPath;
	protected XPath alternateDateXPath;
	protected XPath higherTaxaXPath;
	protected XPath higherTaxonNameXPath;
	protected XPath higherTaxonRankXPath;
	protected XPath genusXPath;
	protected XPath rankXPath;
	protected XPath preferredXPath;
	protected MapCodeMapping taxonRankMapping;
	
	/**
	 * @throws MessageAccessException 
	 * @see org.gbif.portal.util.mhf.message.MessageAccessor#invoke(org.gbif.portal.util.mhf.message.Message)
	 */
	public List<Identification> invoke(Message message) throws MessageAccessException {
		List<Identification> identifications = null;

		try {
			List<Message> messages = message.getParts(getRootXPath());
			
			if (messages != null) {
				for (Message m : messages) {
					String scientificName = StringUtils.trimToNull(m.getPartAsString(scientificNameXPath));
					if (scientificName == null && alternateScientificNameXPath != null) {
						scientificName = StringUtils.trimToNull(m.getPartAsString(alternateScientificNameXPath));
					}
					if (scientificName != null) {
						if (identifications == null) {
							identifications = new ArrayList<Identification>();
						}
						Identification identification = new Identification();
						identification.setScientificName(scientificName);
						String preferredString = StringUtils.trimToNull(m.getPartAsString(preferredXPath));
						if (preferredString != null) {
							if(preferredString.equals("1")) {
								preferredString = "true";
							}
							identification.setPreferred(new Boolean(preferredString));
						}
						identification.setIdentifier(StringUtils.trimToNull(m.getPartAsString(identifierXPath)));
						if (identification.getIdentifier() == null && alternateIdentifierXPath != null) {
							identification.setIdentifier(StringUtils.trimToNull(m.getPartAsString(alternateIdentifierXPath)));
						}
						identification.setDate(StringUtils.trimToNull(m.getPartAsString(dateXPath)));
						if (identification.getDate() == null && alternateDateXPath != null) {
							identification.setDate(StringUtils.trimToNull(m.getPartAsString(alternateDateXPath)));
						}
						if (rankXPath != null) {
							identification.setRank(StringUtils.trimToNull(m.getPartAsString(rankXPath)));
						}
						if (higherTaxaXPath != null) {
							List<Message> higherTaxa = m.getParts(higherTaxaXPath);
							if (higherTaxa != null) {
								for (Message higherTaxon : higherTaxa) {
									String taxonName = higherTaxon.getPartAsString(higherTaxonNameXPath);
									if (taxonName != null) {
										String taxonRank = higherTaxon.getPartAsString(higherTaxonRankXPath);
										try {
											int rank = (int) taxonRankMapping.mapToCode(taxonRank);
											switch (rank) {
											case 1000: identification.setKingdom(taxonName); break;
											case 2000: identification.setPhylum(taxonName); break;
											case 3000: identification.setBioClass(taxonName); break;
											case 4000: identification.setOrder(taxonName); break;
											case 5000: identification.setFamily(taxonName); break;
											}
										} catch (Exception e) {
											// ignore
										}
									}
								}
							}
						}
						if (genusXPath != null) {
							identification.setGenus(StringUtils.trimToNull(m.getPartAsString(genusXPath)));
						}
						
						identifications.add(identification);
					}
				}
			}
		} catch (MessageParseException e) {
			throw new MessageAccessException("Error handling identification messages", e);
		}
		
		return identifications;
	}

	/**
	 * @return the alternateDateXPath
	 */
	public XPath getAlternateDateXPath() {
		return alternateDateXPath;
	}

	/**
	 * @param alternateDateXPath the alternateDateXPath to set
	 */
	public void setAlternateDateXPath(XPath alternateDateXPath) {
		this.alternateDateXPath = alternateDateXPath;
	}

	/**
	 * @return the alternateIdentifierXPath
	 */
	public XPath getAlternateIdentifierXPath() {
		return alternateIdentifierXPath;
	}

	/**
	 * @param alternateIdentifierXPath the alternateIdentifierXPath to set
	 */
	public void setAlternateIdentifierXPath(XPath alternateIdentifierXPath) {
		this.alternateIdentifierXPath = alternateIdentifierXPath;
	}

	/**
	 * @return the dateXPath
	 */
	public XPath getDateXPath() {
		return dateXPath;
	}

	/**
	 * @param dateXPath the dateXPath to set
	 */
	public void setDateXPath(XPath dateXPath) {
		this.dateXPath = dateXPath;
	}

	/**
	 * @return the genusXPath
	 */
	public XPath getGenusXPath() {
		return genusXPath;
	}

	/**
	 * @param genusXPath the genusXPath to set
	 */
	public void setGenusXPath(XPath genusXPath) {
		this.genusXPath = genusXPath;
	}

	/**
	 * @return the higherTaxaXPath
	 */
	public XPath getHigherTaxaXPath() {
		return higherTaxaXPath;
	}

	/**
	 * @param higherTaxaXPath the higherTaxaXPath to set
	 */
	public void setHigherTaxaXPath(XPath higherTaxaXPath) {
		this.higherTaxaXPath = higherTaxaXPath;
	}

	/**
	 * @return the higherTaxonNameXPath
	 */
	public XPath getHigherTaxonNameXPath() {
		return higherTaxonNameXPath;
	}

	/**
	 * @param higherTaxonNameXPath the higherTaxonNameXPath to set
	 */
	public void setHigherTaxonNameXPath(XPath higherTaxonNameXPath) {
		this.higherTaxonNameXPath = higherTaxonNameXPath;
	}

	/**
	 * @return the higherTaxonRankXPath
	 */
	public XPath getHigherTaxonRankXPath() {
		return higherTaxonRankXPath;
	}

	/**
	 * @param higherTaxonRankXPath the higherTaxonRankXPath to set
	 */
	public void setHigherTaxonRankXPath(XPath higherTaxonRankXPath) {
		this.higherTaxonRankXPath = higherTaxonRankXPath;
	}

	/**
	 * @return the identifierXPath
	 */
	public XPath getIdentifierXPath() {
		return identifierXPath;
	}

	/**
	 * @param identifierXPath the identifierXPath to set
	 */
	public void setIdentifierXPath(XPath identifierXPath) {
		this.identifierXPath = identifierXPath;
	}

	/**
	 * @return the rootXPath
	 */
	public XPath getRootXPath() {
		return rootXPath;
	}

	/**
	 * @param rootXPath the rootXPath to set
	 */
	public void setRootXPath(XPath rootXPath) {
		this.rootXPath = rootXPath;
	}

	/**
	 * @return the scientificNameXPath
	 */
	public XPath getScientificNameXPath() {
		return scientificNameXPath;
	}

	/**
	 * @param scientificNameXPath the scientificNameXPath to set
	 */
	public void setScientificNameXPath(XPath scientificNameXPath) {
		this.scientificNameXPath = scientificNameXPath;
	}

	/**
	 * @return the taxonRankMapping
	 */
	public MapCodeMapping getTaxonRankMapping() {
		return taxonRankMapping;
	}

	/**
	 * @param taxonRankMapping the taxonRankMapping to set
	 */
	public void setTaxonRankMapping(MapCodeMapping taxonRankMapping) {
		this.taxonRankMapping = taxonRankMapping;
	}

	/**
	 * @return the alternateScientificNameXPath
	 */
	public XPath getAlternateScientificNameXPath() {
		return alternateScientificNameXPath;
	}

	/**
	 * @param alternateScientificNameXPath the alternateScientificNameXPath to set
	 */
	public void setAlternateScientificNameXPath(XPath alternateScientificNameXPath) {
		this.alternateScientificNameXPath = alternateScientificNameXPath;
	}

	/**
	 * @return the rankXPath
	 */
	public XPath getRankXPath() {
		return rankXPath;
	}

	/**
	 * @param rankXPath the rankXPath to set
	 */
	public void setRankXPath(XPath rankXPath) {
		this.rankXPath = rankXPath;
	}

	/**
	 * @return the preferredXPath
	 */
	public XPath getPreferredXPath() {
		return preferredXPath;
	}

	/**
	 * @param preferredXPath the preferredXPath to set
	 */
	public void setPreferredXPath(XPath preferredXPath) {
		this.preferredXPath = preferredXPath;
	}

}