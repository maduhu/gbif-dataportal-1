package org.gbif.portal.model.taxonomy;

import java.io.Serializable;

/**
 * Identifier class used for compound key.
 * 
 * @author dmartin
 */
public class TaxonCountryId implements Serializable{
	
	private static final long serialVersionUID = 8174862734224994208L;
	
	public Long taxonConceptId;
	public String isoCountryCode;
	
	/**
	 * @return the isoCountryCode
	 */
	public String getIsoCountryCode() {
		return isoCountryCode;
	}
	/**
	 * @param isoCountryCode the isoCountryCode to set
	 */
	public void setIsoCountryCode(String isoCountryCode) {
		this.isoCountryCode = isoCountryCode;
	}
	/**
	 * @return the taxonConceptId
	 */
	public Long getTaxonConceptId() {
		return taxonConceptId;
	}
	/**
	 * @param taxonConceptId the taxonConceptId to set
	 */
	public void setTaxonConceptId(Long taxonConceptId) {
		this.taxonConceptId = taxonConceptId;
	}
}