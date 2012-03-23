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

import org.gbif.portal.dao.RawIdentifierDAO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * Pure JDBC implementation
 * @author trobertson
 */
public class RawIdentifierDAOImpl extends JdbcDaoSupport implements
		RawIdentifierDAO {
	/**
	 * The create sql (note that it is MYSQL specific at the moment)
	 */
	protected static final String CREATE_SQL = "insert ignore into raw_identifier(" +
												"identifier) " +
												"values (?)";

	/**
	 * The query by identifier sql
	 */
	protected static final String QUERY_BY_IDENTIFIER_SQL = "select id " +
	"from raw_identifier where identifier=?";
	
	/**
	 * Reusable row mapper
	 */
	protected LongRowMapper longRowMapper = new LongRowMapper();
	
	/**
	 * Utility to create a long for a row 
	 * @author trobertson
	 */
	protected class LongRowMapper implements RowMapper {
		/**
		 * The factory
		 */
		public Long mapRow(ResultSet rs, int rowNumber) throws SQLException {
			return new Long(rs.getLong("id"));
		}
	}
	
	/**
	 * Gets the id of the RawIdentifier representing the string supplied
	 * @param identifier To get
	 * @return The id or null
	 */
	@SuppressWarnings("unchecked")
	public Long getIdForIdentifier(final String identifier) {
		List<Long> results = (List<Long>) getJdbcTemplate()
		.query(RawIdentifierDAOImpl.QUERY_BY_IDENTIFIER_SQL,
			new Object[] {identifier},
			new RowMapperResultSetExtractor(longRowMapper));
		if (results.size() == 0) {
			return null;
		} else if (results.size()>1) {
			logger.warn("Found multiple RawIdentifiers with identifier: " + identifier);
		}
		return results.get(0);
	}
	
	/**
	 * @see org.gbif.portal.dao.RawIdentifierDAO#createIfNotExists(java.lang.String)
	 */
	public Long createIfNotExists(final String identifier) {
		Long existing = getIdForIdentifier(identifier);
		if (existing != null) {
			return existing.longValue();
		} else {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						   PreparedStatement ps = conn.prepareStatement(RawIdentifierDAOImpl.CREATE_SQL);
						   ps.setString(1, identifier);
						   return ps;
					}					
				},
				keyHolder
			);
			return keyHolder.getKey().longValue();
		}
	}
	
	/**
	 * @see org.gbif.portal.dao.RawIdentifierDAO#createAllIfNotExists(java.util.List)
	 */
	public void createAllIfNotExists(final List<String> identifiers) {
		getJdbcTemplate().batchUpdate(CREATE_SQL,
				new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement ps, int index) throws SQLException {
						ps.setString(1, identifiers.get(index));						
					}
					public int getBatchSize() {
						return identifiers.size();
					}
				});
	}
}
