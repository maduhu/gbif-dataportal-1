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

import org.gbif.portal.dao.LinkRecordDAO;
import org.gbif.portal.dao.impl.jdbc.TypificationRecordDAOImpl.LongRowMapper;
import org.gbif.portal.model.ImageRecord;
import org.gbif.portal.model.LinkRecord;
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
public class LinkRecordDAOImpl extends JdbcDaoSupport implements LinkRecordDAO {
	/**
	 * The create SQL 
	 */	
	protected static final String CREATE_SQL = "insert into link_record (" +
												"data_resource_id," +
												"occurrence_id," +
												"taxon_concept_id," +
												"raw_link_type," +
												"link_type," +
												"url," +
												"description) " +
												"values (?,?,?,?,?,?,?)";
	
	/**
	 * The update SQL 
	 */	
	protected static final String UPDATE_SQL = "update link_record set " +
												"data_resource_id=?," +
												"occurrence_id=?," +
												"taxon_concept_id=?," +
												"raw_link_type=?," +
												"link_type=?," +
												"url=?," +
												"description=? where id=?";
	
	/**
	 * The query by occurrence record id
	 */
	protected static final String QUERY_BY_OCCURRENCE_ID_SQL = 
		"select id,data_resource_id,occurrence_id,taxon_concept_id,raw_link_type,link_type,url,description " + 
		"from link_record lr " +
		"where occurrence_id=?";
	
	/**
	 * The delete
	 */
	protected static final String DELETE_SQL = 
		"delete from link_record where id=?";
	
	/**
	 * The "has some" for resource access point 
	 */
	protected static final String RAP_HAS_RECORDS_SQL = 
		"select r.id as id from link_record i inner join raw_occurrence_record r on i.occurrence_id=r.id where r.resource_access_point_id=? limit 1";
	
	
	/**
	 * LinkRecord row mapper
	 */
	protected LinkRecordRowMapper linkRecordRowMapper = new LinkRecordRowMapper(); 
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
	 * Utility to create a LinkRecord for a row 
	 * @author trobertson
	 */
	protected class LinkRecordRowMapper implements RowMapper {
		/**
		 * The factory
		 */
		public LinkRecord mapRow(ResultSet rs, int rowNumber) throws SQLException {
			return new LinkRecord(
					rs.getLong("id"),
					rs.getLong("data_resource_id"),
					rs.getLong("occurrence_id"),
					rs.getLong("taxon_concept_id"),
					rs.getString("raw_link_type"),
					rs.getInt("link_type"),
					rs.getString("url"),
					rs.getString("description"));
		}
	}	
	
	/**
	 * @see org.gbif.portal.dao.LinkRecordDAO#create(org.gbif.portal.model.LinkRecord)
	 */
	public long create(final LinkRecord linkRecord) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(
			new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					PreparedStatement ps = conn.prepareStatement(LinkRecordDAOImpl.CREATE_SQL);
					ps.setLong(1, linkRecord.getDataResourceId());
					ps.setLong(2, linkRecord.getOccurrenceId());
					ps.setLong(3, linkRecord.getTaxonConceptId());
					ps.setString(4, linkRecord.getRawLinkType());
					ps.setInt(5, linkRecord.getLinkType());
					ps.setString(6, linkRecord.getUrl());
					ps.setString(7, linkRecord.getDescription());
					return ps;
				}					
			},
			keyHolder
		);
		linkRecord.setId(keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}

	/**
	 * @see org.gbif.portal.dao.LinkRecordDAO#updateOrCreate(org.gbif.portal.model.LinkRecord)
	 */
	public long updateOrCreate(final LinkRecord linkRecord) {
		if (linkRecord.getId()<=0) {
			return create(linkRecord);
		}  else {
			getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					    PreparedStatement ps = conn.prepareStatement(LinkRecordDAOImpl.UPDATE_SQL);
						ps.setLong(1, linkRecord.getDataResourceId());
						ps.setLong(2, linkRecord.getOccurrenceId());
						ps.setLong(3, linkRecord.getTaxonConceptId());
						ps.setString(4, linkRecord.getRawLinkType());
						ps.setInt(5, linkRecord.getLinkType());
						ps.setString(6, linkRecord.getUrl());
						ps.setString(7, linkRecord.getDescription());
					    ps.setLong(8, linkRecord.getId());
					    return ps;
					}					
				}
			);
			return linkRecord.getId();	
		}
	}
	
	/**
	 * @see org.gbif.portal.dao.LinkRecordDAO#findByOccurrenceId(long)
	 */
	@SuppressWarnings("unchecked")
	public List<LinkRecord> findByOccurrenceId(final long occurrenceId) {
		List<LinkRecord> results = (List<LinkRecord>) getJdbcTemplate()
			.query(LinkRecordDAOImpl.QUERY_BY_OCCURRENCE_ID_SQL,
				new Object[] {occurrenceId},
				new RowMapperResultSetExtractor(linkRecordRowMapper));
		return results;
	}

	/**
	 * @see org.gbif.portal.dao.LinkRecordDAO#getCreatedOrModifiedSince(java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	public void delete(final LinkRecord linkRecord) {
		if (linkRecord != null && linkRecord.getId()>0) {
			getJdbcTemplate()
				.update(LinkRecordDAOImpl.DELETE_SQL,
					new Object[] {linkRecord.getId()});
		}
	}

	/**
	 * @return Returns the linkRecordRowMapper.
	 */
	public LinkRecordRowMapper getLinkRecordRowMapper() {
		return linkRecordRowMapper;
	}

	/**
	 * @param linkRecordRowMapper The linkRecordRowMapper to set.
	 */
	public void setLinkRecordRowMapper(
			LinkRecordRowMapper linkRecordRowMapper) {
		this.linkRecordRowMapper = linkRecordRowMapper;
	}

	@SuppressWarnings("unchecked")
	public boolean hasLinkRecords(long resourceAccessPointId) {
		List<Long> results = (List<Long>) getJdbcTemplate()
		.query(LinkRecordDAOImpl.RAP_HAS_RECORDS_SQL,
			new Object[] {resourceAccessPointId},
			new RowMapperResultSetExtractor(longRowMapper));
		if (results.size() == 0) {
			return false;
		} else  {
			return true;
		}
	}
}
