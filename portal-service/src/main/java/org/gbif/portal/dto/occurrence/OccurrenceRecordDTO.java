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
package org.gbif.portal.dto.occurrence;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * OccurrenceRecordDTO. More detailed occurrence record information
 * 
 * @author dmartin
 */
public class OccurrenceRecordDTO extends BriefOccurrenceRecordDTO {

	private static final long serialVersionUID = 7911075108039213039L;
	/** Concept within the taxonomy */
	protected String taxonConceptKey;
	/** Nub taxonomy concept if it exists */
	protected String nubTaxonConceptKey;
	/** Altitude in metres */
	protected Integer altitudeInMetres;
	/** Depth in metres */
	protected Float depthInMetres;
	/** One degree cell id */
	protected Integer cellId;
	/** One-tenth degree cell id */
	protected Integer centiCellId;
	/**  Locality as recorded */
	protected String region;

	/**
	 * @return the cellId
	 */
	public Integer getCellId() {
		return cellId;
	}

	/**
	 * @param cellId the cellId to set
	 */
	public void setCellId(Integer cellId) {
		this.cellId = cellId;
	}

	/**
	 * @return the centiCellId
	 */
	public Integer getCentiCellId() {
		return centiCellId;
	}

	/**
	 * @param centiCellId the centiCellId to set
	 */
	public void setCentiCellId(Integer centiCellId) {
		this.centiCellId = centiCellId;
	}

	/**
	 * @return the nubTaxonConceptKey
	 */
	public String getNubTaxonConceptKey() {
		return nubTaxonConceptKey;
	}

	/**
	 * @param nubTaxonConceptKey the nubTaxonConceptKey to set
	 */
	public void setNubTaxonConceptKey(String nubTaxonConceptKey) {
		this.nubTaxonConceptKey = nubTaxonConceptKey;
	}

	/**
	 * @return the taxonConceptKey
	 */
	public String getTaxonConceptKey() {
		return taxonConceptKey;
	}

	/**
	 * @param taxonConceptKey the taxonConceptKey to set
	 */
	public void setTaxonConceptKey(String taxonConceptKey) {
		this.taxonConceptKey = taxonConceptKey;
	}

	/**
	 * @return the modified
	 */
	public Date getModified() {
		return modified;
	}

	/**
	 * @param modified the modified to set
	 */
	public void setModified(Date modified) {
		this.modified = modified;
	}

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}
	
	/**
   * @return the altitudeInMetres
   */
  public Integer getAltitudeInMetres() {
  	return altitudeInMetres;
  }

	/**
   * @param altitudeInMetres the altitudeInMetres to set
   */
  public void setAltitudeInMetres(Integer altitudeInMetres) {
  	this.altitudeInMetres = altitudeInMetres;
  }

	/**
   * @return the depthInMetres
   */
  public Float getDepthInMetres() {
  	return depthInMetres;
  }

	/**
   * @param depthInMetres the depthInMetres to set
   */
  public void setDepthInMetres(Float depthInMetres) {
  	this.depthInMetres = depthInMetres;
  }
  
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}  
}