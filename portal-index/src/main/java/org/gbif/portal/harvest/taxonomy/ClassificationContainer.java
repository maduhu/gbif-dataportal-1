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

import org.gbif.portal.model.TaxonName;

/**
 * A class that contains a reference to a ClassificationList.
 * 
 * Used within the ECAT workflow to allow the previous classification to be passed
 * into next record processing activity.
 * 
 * @author Donald Hobern
 */
public class ClassificationContainer {
	/**
	 * The compiled pattern for the regex
	 */
	private List<TaxonName> classificationList;

	/**
	 * @return the classificationList
	 */
	public List<TaxonName> getClassificationList() {
		return classificationList;
	}

	/**
	 * @param classificationList the classificationList to set
	 */
	public void setClassificationList(List<TaxonName> classificationList) {
		this.classificationList = classificationList;
	}
	
	/**
	 * Get the (int) rank for the lowest taxon in the classification
	 * @return rank (-1 if no classification)
	 */
	public int getLowestTaxonRank() {
		int rank = -1;
		if (classificationList != null) {
			for (TaxonName name : classificationList) {
				if (name.getRank() > rank) {
					rank = name.getRank();
				}
			}
		}
		return rank;
	}
	
	/**
	 * Get the (int) rank for the highest taxon in the classification
	 * @return rank (-1 if no classification)
	 */
	public int getHighestTaxonRank() {
		int rank = -1;
		if (classificationList != null) {
			for (TaxonName name : classificationList) {
				if (rank == -1 || name.getRank() < rank) {
					rank = name.getRank();
				}
			}
		}
		return rank;
	}
	
	/**
	 * Get the taxonName with the specified rank
	 * @return TaxonName
	 */
	public TaxonName getTaxonNameByRank(int rank) {
		TaxonName taxonName = null;
		if (classificationList != null) {
			for (TaxonName name : classificationList) {
				if (rank == name.getRank()) {
					taxonName = name;
					break;
				}
			}
		}
		return taxonName;
	}
}
