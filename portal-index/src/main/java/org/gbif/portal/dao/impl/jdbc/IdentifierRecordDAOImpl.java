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

import org.gbif.portal.dao.IdentifierRecordDAO;
import org.gbif.portal.model.IdentifierRecord;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * A pure jdbc implementation
 * @author Donald Hobern
 */
public class IdentifierRecordDAOImpl extends JdbcDaoSupport implements IdentifierRecordDAO {
	/**
	 * The create SQL 
	 */	
	protected static final String CREATE_SQL = "insert into identifier_record (" +
												"data_resource_id," +
												"occurrence_id," +
												"identifier_type," +
												"identifier) " +
												"values (?,?,?,?)";
	
	/**
	 * The update SQL 
	 */	
	protected static final String UPDATE_SQL = "update identifier_record set " +
												"data_resource_id=?," +
												"occurrence_id=?," +
												"identifier_type=?," +
												"identifier=? where id=?";
	
	/**
	 * The query by occurrence record id
	 */
	protected static final String QUERY_BY_OCCURRENCE_ID_SQL = 
		"select id,data_resource_id,occurrence_id,identifier_type,identifier " + 
		"from identifier_record ir " +
		"where occurrence_id=?";
	
	/**
	 * The delete
	 */
	protected static final String DELETE_SQL = 
		"delete from identifier_record where id=?";
	
	/**
	 * IdentifierRecord row mapper
	 */
	protected IdentifierRecordRowMapper identifierRecordRowMapper = new IdentifierRecordRowMapper(); 
	
	/**
	 * Utility to create a IdentifierRecord for a row 
	 * @author trobertson
	 */
	protected class IdentifierRecordRowMapper implements RowMapper {
		/**
		 * The factory
		 */
		public IdentifierRecord mapRow(ResultSet rs, int rowNumber) throws SQLException {
			return new IdentifierRecord(
					rs.getLong("id"),
					rs.getLong("data_resource_id"),
					rs.getLong("occurrence_id"),
					rs.getInt("identifier_type"),
					rs.getString("identifier"));
		}
	}	
	
	/**
	 * @see org.gbif.portal.dao.IdentifierRecordDAO#create(org.gbif.portal.model.IdentifierRecord)
	 */
	public long create(final IdentifierRecord identifierRecord) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(
			new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					PreparedStatement ps = conn.prepareStatement(IdentifierRecordDAOImpl.CREATE_SQL);
					ps.setLong(1, identifierRecord.getDataResourceId());
					ps.setLong(2, identifierRecord.getOccurrenceId());
					ps.setInt(3, identifierRecord.getIdentifierType());
					ps.setString(4, identifierRecord.getIdentifier());
					return ps;
				}					
			},
			keyHolder
		);
		identifierRecord.setId(keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}

	/**
	 * @see org.gbif.portal.dao.IdentifierRecordDAO#updateOrCreate(org.gbif.portal.model.IdentifierRecord)
	 */
	public long updateOrCreate(final IdentifierRecord identifierRecord) {
		if (identifierRecord.getId()<=0) {
			return create(identifierRecord);
		}  else {
			getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					   PreparedStatement ps = conn.prepareStatement(IdentifierRecordDAOImpl.UPDATE_SQL);
					   ps.setLong(1, identifierRecord.getDataResourceId());
					   ps.setLong(2, identifierRecord.getOccurrenceId());
					   ps.setInt(3, identifierRecord.getIdentifierType());
					   ps.setString(4, identifierRecord.getIdentifier());
					   ps.setLong(5, identifierRecord.getId());
					   return ps;
					}					
				}
			);
			return identifierRecord.getId();
		}
	}
	
	/**
	 * @see org.gbif.portal.dao.IdentifierRecordDAO#findByOccurrenceId(long)
	 */
	@SuppressWarnings("unchecked")
	public List<IdentifierRecord> findByOccurrenceId(final long rawOccurrenceRecordId) {
		List<IdentifierRecord> results = (List<IdentifierRecord>) getJdbcTemplate()
			.query(IdentifierRecordDAOImpl.QUERY_BY_OCCURRENCE_ID_SQL,
				new Object[] {rawOccurrenceRecordId},
				new RowMapperResultSetExtractor(identifierRecordRowMapper));
		return results;
	}

	/**
	 * @see org.gbif.portal.dao.IdentifierRecordDAO#getCreatedOrModifiedSince(java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	public void delete(final IdentifierRecord identifierRecord) {
		if (identifierRecord != null && identifierRecord.getId()>0) {
			getJdbcTemplate()
				.update(IdentifierRecordDAOImpl.DELETE_SQL,
					new Object[] {identifierRecord.getId()});
		}
	}

	/**
	 * @return Returns the identifierRecordRowMapper.
	 */
	public IdentifierRecordRowMapper getIdentifierRecordRowMapper() {
		return identifierRecordRowMapper;
	}

	/**
	 * @param identifierRecordRowMapper The identifierRecordRowMapper to set.
	 */
	public void setIdentifierRecordRowMapper(
			IdentifierRecordRowMapper identifierRecordRowMapper) {
		this.identifierRecordRowMapper = identifierRecordRowMapper;
	}
}
