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

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.model.TaxonName;

/**
 * A class that is loaded with a RegEx and will create a hybrid taxon name based on the configured group id's
 * to extract from the regex.
 * 
 * @author Donald Hobern
 */
public class RegularExpressionToHybridTaxonName extends RegularExpressionToTaxonName {
	/**
	 * Logger
	 */
	protected Log logger = LogFactory.getLog(RegularExpressionToHybridTaxonName.class);
	
	/**
	 * Parses the name based on the configuration
	 * @param name To parse
	 * @param parsedName to set the atomised parts on
	 * @return boolean true if parsed or false if the reg ex doesn't match it
	 */
	public boolean parse(String name, TaxonName parsedName) {
		return parse(name, parsedName, null);
	}
	
	/**
	 * Parses the name based on the configuration
	 * @param name To parse
	 * @param parsedName to set the atomised parts on
	 * @param suppliedRank rank supplied by caller - ignored by this class
	 * @return boolean true if parsed or false if the reg ex doesn't match it
	 */
	public boolean parse(String name, TaxonName parsedName, Integer suppliedRank) {

		name = prepareName(name);
		
		// todo tidy...
		if (name == null) {
			return false;
		}
		
		int rank = 0;
		boolean genusLevel = true;
		
		StringTokenizer st = new StringTokenizer(name);
		for (int i = 0; st.hasMoreTokens(); i++) {
			String token = st.nextToken();
			
			if (token.equals("x") || token.equals("X") || token.equals("*") || token.equals("\u00D7")) {
				switch (i) {
				case 0:
					// x Aus
					rank = 6001; 
					break;
				case 1:
					// Aus x bus
					if (st.hasMoreTokens() && Character.isLowerCase(st.nextToken().charAt(0))) {
						rank = 7001;
					}
					// Aus x Bus
					else {
						rank = 6001; 
					}
					break;
				default:
					if (genusLevel) {
						rank = 6001;
					} else	if (name.indexOf(" f. ") > 0) {
						rank = 8021; 
					} else if (name.indexOf(" var. ") > 0) {
						rank = 8011; 
					} else if (name.indexOf(" subsp. ") > 0 || name.indexOf(" ssp. ") > 0 || name.indexOf(" sub. ") > 0) {
						rank = 8001; 
					} else {
						// Aus bus x ...
						rank = 7001;
					}
					break;
				}
				break;
			} else if (Character.isLowerCase(token.charAt(0))) {
				genusLevel = false;
			}
		}

		if (rank > 0) {
			parsedName.setRank(rank);
			parsedName.setCanonical(name);
			parsedName.setType(1);
		}
		
		return (rank > 0);
	}
}
