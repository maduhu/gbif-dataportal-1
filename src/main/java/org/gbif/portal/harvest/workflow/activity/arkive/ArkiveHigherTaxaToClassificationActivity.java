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
package org.gbif.portal.harvest.workflow.activity.arkive;

import java.util.LinkedList;
import java.util.List;

import org.gbif.portal.harvest.taxonomy.ScientificNameParser;
import org.gbif.portal.model.TaxonName;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * An activity that will use a RegularExpressionToTaxonName to build a TaxonName and insert it into the 
 * classification list. 
 * 
 * @author trobertson
 */
public class ArkiveHigherTaxaToClassificationActivity extends BaseActivity implements Activity {	
	/**
	 * Context Keys
	 */
	protected String contextKeyUrl;
	protected String contextKeyClassificationList;

	/**
	 * Parsers
	 */
	protected ScientificNameParser kingdomParser;
	protected ScientificNameParser phylumParser;
	protected ScientificNameParser classParser;

	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		String url = (String) context.get(getContextKeyUrl(), String.class, true);
		if (url != null) {			
			List<TaxonName> classification = (List<TaxonName>) context.get(getContextKeyClassificationList(), List.class, false);
			if (classification == null) {
				classification = new LinkedList<TaxonName>();
				context.put(getContextKeyClassificationList(), classification);
			}
			
			String kingdom = null;
			String phylum = null;
			String bioClass = null;
			
			if (url.indexOf("/plants_and_algae/") > 0) {
				kingdom = "Plantae";
			} else if (url.indexOf("/fungi/") > 0) {
				kingdom = "Fungi";
			} else if (url.indexOf("/invertebrates_terrestrial_and_freshwater/") > 0) {
				kingdom = "Animalia";
			} else if (url.indexOf("/invertebrates_marine/") > 0) {
				kingdom = "Animalia";
			} else if (url.indexOf("/mammals/") > 0) {
				kingdom = "Animalia";
				phylum = "Chordata";
				bioClass = "Mammalia";
			} else if (url.indexOf("/birds/") > 0) {
				kingdom = "Animalia";
				phylum = "Chordata";
				bioClass = "Aves";
			} else if (url.indexOf("/reptiles/") > 0) {
				kingdom = "Animalia";
				phylum = "Chordata";
				bioClass = "Reptilia";
			} else if (url.indexOf("/amphibians/") > 0) {
				kingdom = "Animalia";
				phylum = "Chordata";
				bioClass = "Amphibia";
			} else if (url.indexOf("/fish/") > 0) {
				kingdom = "Animalia";
				phylum = "Chordata";
			} 

			if (kingdom != null) {
				int parserResult = kingdomParser.parse(context, kingdom, classification);
				logger.debug("Parsed kingdom[" + kingdom + "]: " + (parserResult==ScientificNameParser.PARSED));
			} 
			if (phylum != null) {
				int parserResult = phylumParser.parse(context, phylum, classification);
				logger.debug("Parsed phylum[" + phylum + "]: " + (parserResult==ScientificNameParser.PARSED));
			} 
			if (bioClass != null) {
				int parserResult = classParser.parse(context, bioClass, classification);
				logger.debug("Parsed class[" + bioClass + "]: " + (parserResult==ScientificNameParser.PARSED));
			} 
		}
		return context;
	}

	/**
	 * @return the contextKeyClassificationList
	 */
	public String getContextKeyClassificationList() {
		return contextKeyClassificationList;
	}

	/**
	 * @param contextKeyClassificationList the contextKeyClassificationList to set
	 */
	public void setContextKeyClassificationList(String contextKeyClassificationList) {
		this.contextKeyClassificationList = contextKeyClassificationList;
	}

	/**
	 * @return the contextKeyUrl
	 */
	public String getContextKeyUrl() {
		return contextKeyUrl;
	}

	/**
	 * @param contextKeyUrl the contextKeyUrl to set
	 */
	public void setContextKeyUrl(String contextKeyUrl) {
		this.contextKeyUrl = contextKeyUrl;
	}

	/**
	 * @return the kingdomParser
	 */
	public ScientificNameParser getKingdomParser() {
		return kingdomParser;
	}

	/**
	 * @param kingdomParser the kingdomParser to set
	 */
	public void setKingdomParser(ScientificNameParser kingdomParser) {
		this.kingdomParser = kingdomParser;
	}

	/**
	 * @return the classParser
	 */
	public ScientificNameParser getClassParser() {
		return classParser;
	}

	/**
	 * @param classParser the classParser to set
	 */
	public void setClassParser(ScientificNameParser classParser) {
		this.classParser = classParser;
	}

	/**
	 * @return the phylumParser
	 */
	public ScientificNameParser getPhylumParser() {
		return phylumParser;
	}

	/**
	 * @param phylumParser the phylumParser to set
	 */
	public void setPhylumParser(ScientificNameParser phylumParser) {
		this.phylumParser = phylumParser;
	}
}