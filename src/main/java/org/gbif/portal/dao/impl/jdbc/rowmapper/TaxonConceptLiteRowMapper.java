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
package org.gbif.portal.dao.impl.jdbc.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.gbif.portal.model.TaxonConceptLite;
import org.springframework.jdbc.core.RowMapper;

/**
 * Utility to create a TaxonConceptLite for a row
 * @author trobertson
 */
public class TaxonConceptLiteRowMapper implements RowMapper {
	/**
	 * Reuse
	 */
	protected TaxonNameRowMapper tnrm = new TaxonNameRowMapper();
	
	/**
	 * The factory
	 */
	public TaxonConceptLite mapRow(ResultSet rs, int rowNumber) throws SQLException {
		TaxonConceptLite tcl = new TaxonConceptLite();
		tcl.setId(rs.getLong("tc.id"));
		tcl.setParentId(rs.getLong("tc.parent_concept_id"));
		tcl.setRank(rs.getInt("tc.rank"));
		tcl.setAccepted(rs.getBoolean("tc.is_accepted"));
		tcl.setDataProviderId(rs.getLong("tc.data_provider_id"));
		tcl.setDataResourceId(rs.getLong("tc.data_provider_id"));
		tcl.setPartnerConceptId(rs.getLong("tc.partner_concept_id"));
		tcl.setNubConcept(rs.getBoolean("tc.is_nub_concept"));
		tcl.setSecondary(rs.getBoolean("tc.is_secondary"));
		tcl.setPriority(rs.getInt("tc.priority"));
		tcl.setTaxonName(tnrm.mapRow(rs,rowNumber));		
		return tcl;
	}
}
