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
package org.gbif.portal.web.controller.taxonomy.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.dto.taxonomy.CommonNameDTO;
import org.gbif.portal.dto.taxonomy.RelationshipAssertionDTO;
import org.gbif.portal.dto.taxonomy.RemoteConceptDTO;
import org.gbif.portal.dto.taxonomy.TaxonConceptDTO;


/**
 * DistributionDTO
 * 
 * @author trobertson
 */
public class PartnerConceptBean implements Serializable {
	
	/**
	 * Generated
	 */
	private static final long serialVersionUID = -3460950073378364966L;
	
	protected TaxonConceptDTO taxonConcept;
	protected List<BriefTaxonConceptDTO> higherConcepts;
	protected DataResourceDTO dataResource;
	protected List<RemoteConceptDTO> remoteConcepts;
	protected List<RelationshipAssertionDTO> synonyms;
	protected List<RelationshipAssertionDTO> relationshipAssertions;
	protected List<Map<String, List<CommonNameDTO>>> commonNames;

	/**
	 * @return the dataResource
	 */
	public DataResourceDTO getDataResource() {
		return dataResource;
	}
	/**
	 * @param dataResource the dataResource to set
	 */
	public void setDataResource(DataResourceDTO dataResource) {
		this.dataResource = dataResource;
	}
	/**
	 * @return the remoteConcepts
	 */
	public List<RemoteConceptDTO> getRemoteConcepts() {
		return remoteConcepts;
	}
	/**
	 * @param remoteConcepts the remoteConcepts to set
	 */
	public void setRemoteConcepts(List<RemoteConceptDTO> remoteConcepts) {
		this.remoteConcepts = remoteConcepts;
	}
	/**
	 * @return the taxonConcept
	 */
	public TaxonConceptDTO getTaxonConcept() {
		return taxonConcept;
	}
	/**
	 * @param taxonConcept the taxonConcept to set
	 */
	public void setTaxonConcept(TaxonConceptDTO taxonConcept) {
		this.taxonConcept = taxonConcept;
	}
	/**
	 * @return the synonyms
	 */
	public List<RelationshipAssertionDTO> getSynonyms() {
		return synonyms;
	}
	/**
	 * @param synonyms the synonyms to set
	 */
	public void setSynonyms(List<RelationshipAssertionDTO> synonyms) {
		this.synonyms = synonyms;
	}
	/**
	 * @return the relationshipAssertions
	 */
	public List<RelationshipAssertionDTO> getRelationshipAssertions() {
		return relationshipAssertions;
	}
	/**
	 * @param relationshipAssertions the relationshipAssertions to set
	 */
	public void setRelationshipAssertions(
			List<RelationshipAssertionDTO> relationshipAssertions) {
		this.relationshipAssertions = relationshipAssertions;
	}
	/**
	 * @return the higherConcepts
	 */
	public List<BriefTaxonConceptDTO> getHigherConcepts() {
		return higherConcepts;
	}
	/**
	 * @param higherConcepts the higherConcepts to set
	 */
	public void setHigherConcepts(List<BriefTaxonConceptDTO> higherConcepts) {
		this.higherConcepts = higherConcepts;
	}
	/**
	 * @return Returns the commonNames.
	 */
	public List<Map<String, List<CommonNameDTO>>> getCommonNames() {
		return commonNames;
	}
	/**
	 * @param commonNames The commonNames to set.
	 */
	public void setCommonNames(List<Map<String, List<CommonNameDTO>>> commonNames) {
		this.commonNames = commonNames;
	}
}