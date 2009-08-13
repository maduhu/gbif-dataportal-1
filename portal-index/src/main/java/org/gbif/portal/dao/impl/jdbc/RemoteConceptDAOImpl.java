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
import java.sql.Timestamp;
import java.util.List;

import org.gbif.portal.dao.RemoteConceptDAO;
import org.gbif.portal.model.RemoteConcept;
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
public class RemoteConceptDAOImpl extends JdbcDaoSupport implements RemoteConceptDAO {
	/**
	 * The create SQL 
	 */	
	protected static final String CREATE_SQL = "insert into remote_concept (" +
												"taxon_concept_id," +
												"id_type," +
												"remote_id, " +
												"modified) " +
												"values (?,?,?,?)";
	
	/**
	 * The update SQL 
	 */	
	protected static final String UPDATE_SQL = "update remote_concept set " +
												"taxon_concept_id=?," +
												"id_type=?," +
												"remote_id=?," +
												"modified=? " +
												"where id=?";
	
	/**
	 * The query by taxon concept id
	 */
	protected static final String QUERY_BY_TAXON_CONCEPT_ID_SQL = 
		"select id,taxon_concept_id,id_type,remote_id,modified " + 
		"from remote_concept rc " +
		"where taxon_concept_id=?";

	/**
	 * The query by remote id and data resource id
	 */
	protected static final String QUERY_BY_REMOTE_ID_AND_ID_TYPE_AND_DATA_RESOURCE_ID_SQL = 
		"select rc.id,rc.taxon_concept_id,rc.id_type,rc.remote_id,rc.modified " + 
		"from remote_concept rc inner join taxon_concept tc on tc.id=rc.taxon_concept_id " +
		"where rc.remote_id=? and rc.id_type=? and tc.data_resource_id=?";
	
	/**
	 * The delete
	 */
	protected static final String DELETE_SQL = 
		"delete from remote_concept where id=?";
	
	/**
	 * The delete old entries
	 */
	protected static final String DELETE_OLDER_REMOTE_CONCEPTS_SQL = 
		"delete from remote_concept rc using taxon_concept tc, remote_concept rc " +
		"where tc.id=rc.taxon_concept_id " +
		"and tc.data_resource_id=? " +
		"and rc.modified<?";
	
	/**
	 * RemoteConcept row mapper
	 */
	protected RemoteConceptRowMapper RemoteConceptRowMapper = new RemoteConceptRowMapper(); 
	
	/**
	 * Utility to create a RemoteConcept for a row 
	 * @author trobertson
	 */
	protected class RemoteConceptRowMapper implements RowMapper {
		/**
		 * The factory
		 */
		public RemoteConcept mapRow(ResultSet rs, int rowNumber) throws SQLException {
			return new RemoteConcept(
					rs.getLong("id"),
					rs.getLong("taxon_concept_id"),
					rs.getLong("id_type"),
					rs.getString("remote_id"),
					rs.getDate("modified"));
		}
	}	
	
	/**
	 * @see org.gbif.portal.dao.RemoteConceptDAO#create(org.gbif.portal.model.RemoteConcept)
	 */
	public long create(final RemoteConcept remoteConcept) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(
			new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					PreparedStatement ps = conn.prepareStatement(RemoteConceptDAOImpl.CREATE_SQL);
					ps.setLong(1, remoteConcept.getTaxonConceptId());
					ps.setLong(2, remoteConcept.getIdType());
					ps.setString(3, remoteConcept.getRemoteId());
					if (remoteConcept.getModified() != null) {
						ps.setTimestamp(4, new Timestamp(remoteConcept.getModified().getTime()));
					} else {
						ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
					}
					return ps;
				}					
			},
			keyHolder
		);
		remoteConcept.setId(keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}

	/**
	 * @see org.gbif.portal.dao.RemoteConceptDAO#updateOrCreate(org.gbif.portal.model.RemoteConcept)
	 */
	public long updateOrCreate(final RemoteConcept remoteConcept) {
		if (remoteConcept.getId()<=0) {
			return create(remoteConcept);
		}  else {
			getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					   PreparedStatement ps = conn.prepareStatement(RemoteConceptDAOImpl.UPDATE_SQL);
						ps.setLong(1, remoteConcept.getTaxonConceptId());
						ps.setLong(2, remoteConcept.getIdType());
						ps.setString(3, remoteConcept.getRemoteId());
						ps.setTimestamp(4, new Timestamp(remoteConcept.getModified().getTime()));
					    ps.setLong(5, remoteConcept.getId());
					   return ps;
					}					
				}
			);
			return remoteConcept.getId();	
		}
	}

	/* (non-Javadoc)
	 * @see org.gbif.portal.dao.RemoteConceptDAO#findByTaxonConceptId(long)
	 */
	@SuppressWarnings("unchecked")
	public List<RemoteConcept> findByTaxonConceptId(long taxonConceptId) {
		List<RemoteConcept> results = (List<RemoteConcept>) getJdbcTemplate()
			.query(RemoteConceptDAOImpl.QUERY_BY_TAXON_CONCEPT_ID_SQL,
				new Object[] {taxonConceptId},
				new RowMapperResultSetExtractor(RemoteConceptRowMapper));
		return results;
	}

	/* (non-Javadoc)
	 * @see org.gbif.portal.dao.RemoteConceptDAO#findByRemoteIdAndDataResourceId(java.lang.String, long)
	 */
	@SuppressWarnings("unchecked")
	public List<RemoteConcept> findByRemoteIdAndIdTypeAndDataResourceId(String remoteId, long idType, long dataResourceId) {
		List<RemoteConcept> results = (List<RemoteConcept>) getJdbcTemplate()
		.query(RemoteConceptDAOImpl.QUERY_BY_REMOTE_ID_AND_ID_TYPE_AND_DATA_RESOURCE_ID_SQL,
			new Object[] {remoteId, idType, dataResourceId},
			new RowMapperResultSetExtractor(RemoteConceptRowMapper));
		return results;
	}

	/**
	 * @see org.gbif.portal.dao.RemoteConceptDAO#delete(RemoteConcept)
	 */
	@SuppressWarnings("unchecked")
	public void delete(final RemoteConcept RemoteConcept) {
		if (RemoteConcept != null && RemoteConcept.getId()>0) {
			getJdbcTemplate()
				.update(RemoteConceptDAOImpl.DELETE_SQL,
					new Object[] {RemoteConcept.getId()});
		}
	}

	/**
	 * @return Returns the RemoteConceptRowMapper.
	 */
	public RemoteConceptRowMapper getRemoteConceptRowMapper() {
		return RemoteConceptRowMapper;
	}

	/**
	 * @param RemoteConceptRowMapper The RemoteConceptRowMapper to set.
	 */
	public void setRemoteConceptRowMapper(
			RemoteConceptRowMapper RemoteConceptRowMapper) {
		this.RemoteConceptRowMapper = RemoteConceptRowMapper;
	}

	public void deleteRemoteConceptsOlderThan(final Long dataResourceId, final Long timer) {
		getJdbcTemplate()
			.update(RemoteConceptDAOImpl.DELETE_OLDER_REMOTE_CONCEPTS_SQL,
				new Object[] {dataResourceId, new Timestamp(timer)});
	}
}
