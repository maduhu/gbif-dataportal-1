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

import org.gbif.portal.dao.TypificationRecordDAO;
import org.gbif.portal.dao.impl.jdbc.ImageRecordDAOImpl.LongRowMapper;
import org.gbif.portal.model.ImageRecord;
import org.gbif.portal.model.TypificationRecord;
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
public class TypificationRecordDAOImpl extends JdbcDaoSupport implements TypificationRecordDAO {
	/**
	 * The create SQL 
	 */	
	protected static final String CREATE_SQL = "insert into typification_record (" +
												"data_resource_id," +
												"occurrence_id," +
												"taxon_name_id," +
												"scientific_name," +
												"publication," +
												"type_status," +
												"notes) " +
												"values (?,?,?,?,?,?,?)";
	
	/**
	 * The update SQL 
	 */	
	protected static final String UPDATE_SQL = "update typification_record set " +
												"data_resource_id=?," +
												"occurrence_id=?," +
												"taxon_name_id=?," +
												"scientific_name=?," +
												"publication=?," +
												"type_status=?," +
												"notes=? where id=?";
	
	/**
	 * The query by occurrence record id
	 */
	protected static final String QUERY_BY_OCCURRENCE_ID_SQL = 
		"select id,data_resource_id,occurrence_id,taxon_name_id,scientific_name,publication,type_status,notes " + 
		"from typification_record ir " +
		"where occurrence_id=?";
	
	/**
	 * The delete
	 */
	protected static final String DELETE_SQL = 
		"delete from typification_record where id=?";
	
	/**
	 * The "has some" for resource access point 
	 */
	protected static final String RAP_HAS_RECORDS_SQL = 
		"select r.id as id from typification_record i inner join raw_occurrence_record r on i.occurrence_id=r.id where r.resource_access_point_id=? limit 1";
	
	
	/**
	 * TypificationRecord row mapper
	 */
	protected TypificationRecordRowMapper typificationRecordRowMapper = new TypificationRecordRowMapper();
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
	 * Utility to create a TypificationRecord for a row 
	 * @author trobertson
	 */
	protected class TypificationRecordRowMapper implements RowMapper {
		/**
		 * The factory
		 */
		public TypificationRecord mapRow(ResultSet rs, int rowNumber) throws SQLException {
			return new TypificationRecord(
					rs.getLong("id"),
					rs.getLong("data_resource_id"),
					rs.getLong("occurrence_id"),
					rs.getLong("taxon_name_id"),
					rs.getString("scientific_name"),
					rs.getString("publication"),
					rs.getString("type_status"),
					rs.getString("notes"));
		}
	}	
	
	/**
	 * @see org.gbif.portal.dao.TypificationRecordDAO#create(org.gbif.portal.model.TypificationRecord)
	 */
	public long create(final TypificationRecord typificationRecord) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(
			new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					PreparedStatement ps = conn.prepareStatement(TypificationRecordDAOImpl.CREATE_SQL);
					ps.setLong(1, typificationRecord.getDataResourceId());
					ps.setLong(2, typificationRecord.getOccurrenceId());
					ps.setLong(3, typificationRecord.getTaxonNameId());
					ps.setString(4, typificationRecord.getScientificName());
					ps.setString(5, typificationRecord.getPublication());
					ps.setString(6, typificationRecord.getTypeStatus());
					ps.setString(7, typificationRecord.getNotes());
					return ps;
				}					
			},
			keyHolder
		);
		typificationRecord.setId(keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}

	/**
	 * @see org.gbif.portal.dao.TypificationRecordDAO#updateOrCreate(org.gbif.portal.model.TypificationRecord)
	 */
	public long updateOrCreate(final TypificationRecord typificationRecord) {
		if (typificationRecord.getId()<=0) {
			return create(typificationRecord);
		}  else {
			getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					   PreparedStatement ps = conn.prepareStatement(TypificationRecordDAOImpl.UPDATE_SQL);
					   ps.setLong(1, typificationRecord.getDataResourceId());
					   ps.setLong(2, typificationRecord.getOccurrenceId());
					   ps.setLong(3, typificationRecord.getTaxonNameId());
					   ps.setString(4, typificationRecord.getScientificName());
					   ps.setString(5, typificationRecord.getPublication());
					   ps.setString(6, typificationRecord.getTypeStatus());
					   ps.setString(7, typificationRecord.getNotes());
					   ps.setLong(8, typificationRecord.getId());
					   return ps;
					}					
				}
			);
			return typificationRecord.getId();	
		}
	}

	/**
	 * @see org.gbif.portal.dao.TypificationRecordDAO#findByOccurrenceId(long)
	 */	
	@SuppressWarnings("unchecked")
	public List<TypificationRecord> findByOccurrenceId(final long occurrenceId) {
		List<TypificationRecord> results = (List<TypificationRecord>) getJdbcTemplate()
			.query(TypificationRecordDAOImpl.QUERY_BY_OCCURRENCE_ID_SQL,
				new Object[] {occurrenceId},
				new RowMapperResultSetExtractor(typificationRecordRowMapper));
		return results;
	}

	/**
	 * @see org.gbif.portal.dao.TypificationRecordDAO#getCreatedOrModifiedSince(java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	public void delete(final TypificationRecord typificationRecord) {
		if (typificationRecord != null && typificationRecord.getId()>0) {
			getJdbcTemplate()
				.update(TypificationRecordDAOImpl.DELETE_SQL,
					new Object[] {typificationRecord.getId()});
		}
	}

	/**
	 * @return Returns the typificationRecordRowMapper.
	 */
	public TypificationRecordRowMapper getTypificationRecordRowMapper() {
		return typificationRecordRowMapper;
	}

	/**
	 * @param typificationRecordRowMapper The typificationRecordRowMapper to set.
	 */
	public void setTypificationRecordRowMapper(
			TypificationRecordRowMapper typificationRecordRowMapper) {
		this.typificationRecordRowMapper = typificationRecordRowMapper;
	}


	@SuppressWarnings("unchecked")
	public boolean hasTypificationRecords(long resourceAccessPointId) {
		List<Long> results = (List<Long>) getJdbcTemplate()
		.query(TypificationRecordDAOImpl.RAP_HAS_RECORDS_SQL,
			new Object[] {resourceAccessPointId},
			new RowMapperResultSetExtractor(longRowMapper));
		if (results.size() == 0) {
			return false;
		} else  {
			return true;
		}
	}
}
