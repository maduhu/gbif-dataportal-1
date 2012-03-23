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
package org.gbif.portal.dao.impl.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.gbif.portal.dao.RelationshipAssertionDAO;
import org.gbif.portal.model.RelationshipAssertion;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * Pure JDBC implementation
 * @author trobertson
 */
public class RelationshipAssertionDAOImpl extends JdbcDaoSupport implements
		RelationshipAssertionDAO {
	
	/**
	 * The create sql
	 */
	protected static final String CREATE_SQL = "insert into relationship_assertion(" +
												"from_concept_id," +
												"to_concept_id," +
												"relationship_type) " +
												"values (?,?,?)";
	/**
	 * The create sql
	 */
	protected static final String QUERY_BY_FROM_CONCEPT_SQL = "select from_concept_id," +
												"to_concept_id," +
												"relationship_type " +
												"from relationship_assertion where from_concept_id=?";

	/**
	 * Reusable row mapper
	 */
	protected RelationshipAssertionRowMapper relationshipAssertionRowMapper = new RelationshipAssertionRowMapper();
	
	/**
	 * Utility to create a RelationshipAssertion for a row 
	 * @author trobertson
	 */
	protected class RelationshipAssertionRowMapper implements RowMapper {
		/**
		 * The factory
		 */
		public RelationshipAssertion mapRow(ResultSet rs, int rowNumber) throws SQLException {
			return new RelationshipAssertion(rs.getLong("from_concept_id"),
					rs.getLong("to_concept_id"),
					rs.getLong("relationship_type"));
		}
	}

	/**
	 * @see org.gbif.portal.dao.ResourceAccessPointDAO#create(org.gbif.portal.model.ResourceAccessPoint)
	 */
	public void create(final long from, final long to, final int type) {
		getJdbcTemplate().update(
			new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					   PreparedStatement ps = conn.prepareStatement(RelationshipAssertionDAOImpl.CREATE_SQL);
					   ps.setLong(1, from);
					   ps.setLong(2, to);
					   ps.setInt(3, type);
					   return ps;
				}					
			}
		);
	}
	/* (non-Javadoc)
	 * @see org.gbif.portal.dao.RelationshipAssertionDAO#getRelationshipAssertionsForFromConcept(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<RelationshipAssertion> getRelationshipAssertionsForFromConcept(Long fromId) {
		return (List<RelationshipAssertion>) getJdbcTemplate()
		.query(RelationshipAssertionDAOImpl.QUERY_BY_FROM_CONCEPT_SQL,
			new Object[]{fromId},
			new RowMapperResultSetExtractor(relationshipAssertionRowMapper, 3));
	}
}
