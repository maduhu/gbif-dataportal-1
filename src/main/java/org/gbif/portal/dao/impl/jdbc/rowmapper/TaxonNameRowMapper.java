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

import org.gbif.portal.model.TaxonName;
import org.springframework.jdbc.core.RowMapper;

/**
 * Utility to create a TaxonName for a row
 * @author trobertson
 */
public class TaxonNameRowMapper implements RowMapper {
	/**
	 * The factory
	 */
	public TaxonName mapRow(ResultSet rs, int rowNumber) throws SQLException {
		TaxonName tn = new TaxonName();
		tn.setId(rs.getLong("tn.id"));
		tn.setAuthor(rs.getString("tn.author"));
		tn.setCanonical(rs.getString("tn.canonical"));
		tn.setGeneric(rs.getString("tn.generic"));
		tn.setInfraGeneric(rs.getString("tn.infrageneric"));
		tn.setInfraSpecific(rs.getString("tn.infraspecific"));
		tn.setInfraSpecificMarker(rs.getString("tn.infraspecific_marker"));
		tn.setRank(rs.getInt("tn.rank"));
		tn.setSpecific(rs.getString("tn.specific_epithet"));
		tn.setSupraGeneric(rs.getString("tn.supra_generic"));
		tn.setType(rs.getInt("tn.is_hybrid"));
		return tn;
	}
}
