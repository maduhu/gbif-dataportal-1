/***************************************************************************
 * Copyright (C) 2006 Global Biodiversity Information Facility Secretariat.
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
package org.gbif.portal.model.taxonomy;

import org.gbif.portal.model.BaseObject;

/**
 * The details of a taxonomic concept as held by the data provider. This includes its
 * unique identifer for their domain.
 * 
 * @author dmartin
 *
 * @hibernate.class
 * 	table="remote_concept"
 * 
 * @author dmartin
 */
public class RemoteConcept extends BaseObject {

	/** The remote Id for the concept */
	protected String remoteId;
	/** The concept this remote concept identifies */
	protected TaxonConcept taxonConcept;
	/** The concept this remote concept identifies */
	protected long taxonConceptId;
	/** The type of identifier */
	protected IdType idType;

	/**
     * @hibernate.property
     * 	column="remote_id"
	 * 
	 * @return the remoteId
	 */
	public String getRemoteId() {
		return remoteId;
	}

	/**
	 * @param remoteId the remoteId to set
	 */
	public void setRemoteId(String remoteId) {
		this.remoteId = remoteId;
	}

	/**
	 * @hibernate.many-to-one
	 * 	column="taxon_concept_id"
	 * 	not-null="true"
	 * 
	 * @return the taxonConcept
	 */
	public TaxonConcept getTaxonConcept() {
		return taxonConcept;
	}

	/**
	 * @param taxonConcept the taxonConcept to set
	 */
	public void setTaxonConcept(TaxonConcept taxonConcept) {
		this.taxonConcept = taxonConcept;
	}

	/**
	 * @return the idType
	 */
	public IdType getIdType() {
		return idType;
	}

	/**
	 * @param idType the idType to set
	 */
	public void setIdType(IdType idType) {
		this.idType = idType;
	}

	/**
	 * @return the taxonConceptId
	 */
	public long getTaxonConceptId() {
		return taxonConceptId;
	}

	/**
	 * @param taxonConceptId the taxonConceptId to set
	 */
	public void setTaxonConceptId(long taxonConceptId) {
		this.taxonConceptId = taxonConceptId;
	}
}