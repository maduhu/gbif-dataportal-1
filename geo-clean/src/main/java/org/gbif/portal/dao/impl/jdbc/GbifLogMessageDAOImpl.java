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

import org.gbif.portal.dao.GbifLogMessageDAO;
import org.gbif.portal.model.GbifLogMessage;
import org.gbif.portal.util.log.LogEvent;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;


/**
 * Implementation of a DAO for log messages.
 * @author Donald Hobern
 */
public class GbifLogMessageDAOImpl extends JdbcDaoSupport implements GbifLogMessageDAO {
	
	protected static GbifLogMessageDAOImpl instance;
	
	/**
	 * The query by instance id sql
	 */
	protected static final String QUERY_MAX_LOGGROUPID_SQL = 
		"select max(log_group_id) as id from gbif_log_message where portal_instance_id=?";

	/**
	 * The delete by occurrence id SQL
	 */
	protected static final String DELETE_BY_OCCURRENCE_ID = 
		"delete from gbif_log_message where event_id>=? and event_id<=? and occurrence_id=?";
	
	
	/**
	 * The insert SQL
	 */
	protected static final String CREATE_SQL = "insert into gbif_log_message (" +
        "portal_instance_id," +
        "log_group_id," +
        "event_id," + 
        "level," +
        "data_provider_id," +
        "data_resource_id," +
        "occurrence_id," +
        "taxon_concept_id," +
        "user_id," +
        "message," +
        "restricted," +
        "count) " +
		"values (?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * The insert SQL (with timestamp)
	 */
	protected static final String CREATE_SQL_WITH_TIMESTAMP = "insert into gbif_log_message (" +
        "portal_instance_id," +
        "log_group_id," +
        "event_id," + 
        "level," +
        "data_provider_id," +
        "data_resource_id," +
        "occurrence_id," +
        "taxon_concept_id," +
        "user_id," +
        "message," +
        "restricted," +
        "count," +
        "timestamp) " +
		"values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

	private static final String QUERY_BY_OCCURRENCE_ID_AND_EVENT_ID = "select glm.id, " +
			"glm.portal_instance_id, " +
			"glm.log_group_id, " +
			"glm.event_id, " +
			"glm.level, " +
			"glm.data_provider_id, " +
			"glm.data_resource_id, " +
			"glm.occurrence_id, " +
			"glm.taxon_concept_id, " +
			"glm.user_id, " +
			"glm.message, " +
			"glm.restricted, " +
			"glm.count, " +
			"glm.timestamp from gbif_log_message glm " +
			"where glm.occurrence_id=? and event_id=?";

	private static final String UPDATE_SQL = "update gbif_log_message set " +
												"id=:id,"+
												"portal_instance_id=:portalInstanceId," +
												"event_id=:eventId," +
												"log_group_id=:logGroupId," +
												"level=:level," +
												"data_provider_id=:dataProviderId," +
												"data_resource_id=:dataResourceId," +
												"occurrence_id=:occurrenceId," +
												"taxon_concept_id=:taxonConceptId," +
												"user_id=:userId," +
												"message=:message," +
												"restricted=:restricted," +
												"count=:count," +
												"timestamp=:timestamp where id=:id";

	/**
	 * Reusable row mapper
	 */
	protected GbifLogMessageRowMapper gbifLogMessageRowMapper = new GbifLogMessageRowMapper();
	
	/**
	 * To make use of bean naming
	 */
	protected NamedParameterJdbcTemplate namedParameterTemplate;
	
	/**
	 * Utility to create a GbifLogMessage for a row 
	 * @author Donald Hobern
	 */
	protected class GbifLogMessageRowMapper implements RowMapper {
		/**
		 * The factory
		 */
		public GbifLogMessage mapRow(ResultSet rs, int rowNumber) throws SQLException {
			return new GbifLogMessage(
					rs.getLong("id"),
					rs.getLong("portal_instance_id"),
					rs.getLong("log_group_id"),
					rs.getInt("event_id"),
					rs.getInt("level"),
					rs.getLong("data_provider_id"),
					rs.getLong("data_resource_id"),
					rs.getLong("occurrence_id"),
					rs.getLong("taxon_concept_id"),
					rs.getLong("user_id"),
					rs.getString("message"),
					rs.getBoolean("restricted"),
					rs.getInt("count"),
					rs.getDate("timestamp"));
		}
	}

	public GbifLogMessageDAOImpl() {
		instance = this;
	}
	
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
	 * @see org.gbif.portal.util.log.GbifLogMessageDAO#create(gbifLogMessage)
	 */
	@SuppressWarnings("unchecked")
	public long create(final GbifLogMessage message) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(
			new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					// timestamp included?
					PreparedStatement ps = null;
					if (message.getTimestamp() !=null) {
						ps = conn.prepareStatement(GbifLogMessageDAOImpl.CREATE_SQL_WITH_TIMESTAMP);
						Timestamp ts = new Timestamp(message.getTimestamp().getTime());
						ps.setTimestamp(13, ts);
					} else {
						ps = conn.prepareStatement(GbifLogMessageDAOImpl.CREATE_SQL);
					}
					ps.setLong(1, message.getPortalInstanceId());
					ps.setLong(2, message.getLogGroupId());
					ps.setInt(3, message.getEventId());
					ps.setInt(4, message.getLevel());
					ps.setObject(5, message.getDataProviderId());
					ps.setObject(6, message.getDataResourceId());
					ps.setObject(7, message.getOccurrenceId());
					ps.setObject(8, message.getTaxonConceptId());
					ps.setObject(9, message.getUserId());
					ps.setString(10, message.getMessage());
					ps.setBoolean(11, message.isRestricted());
					ps.setInt(12, message.getCount());
					return ps;
				}					
			},
			keyHolder
		);
		message.setId(keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}

	@SuppressWarnings("unchecked")
	public long getMaxLogGroupId(Long portalInstanceId) {
		long maxId = 0;

		List<Long> results = (List<Long>) getJdbcTemplate()
		.query(GbifLogMessageDAOImpl.QUERY_MAX_LOGGROUPID_SQL,
			new Object[] {portalInstanceId},
			new RowMapperResultSetExtractor(longRowMapper));
		if (results.size() > 0) {
			maxId = results.get(0);
		} 
		return maxId;
	}
	
	public int deleteExtractEventsByOccurrenceId(long occurrenceId) {
		return getJdbcTemplate().update(
					GbifLogMessageDAOImpl.DELETE_BY_OCCURRENCE_ID, 
					new Object[]{LogEvent.EXTRACT_SCIENTIFICNAMEPARSEISSUE.getValue(), LogEvent.EXTRACT_RANGE_END, occurrenceId});
	}
	
	public int deleteGeospatialIssueEventsByOccurrenceIdAndEventId(long occurrenceId) {
		return getJdbcTemplate().update(
				GbifLogMessageDAOImpl.DELETE_BY_OCCURRENCE_ID, 
				new Object[]{LogEvent.EXTRACT_GEOSPATIALISSUE.getValue(), LogEvent.EXTRACT_GEOSPATIALISSUE.getValue(), occurrenceId});
	}
	
	public GbifLogMessage getByOccurrenceIdAndGeospatialIssue(long occurrenceId,
			int extractGeospatialissueValue) {
		List<GbifLogMessage> results = (List<GbifLogMessage>) getJdbcTemplate()
		.query(GbifLogMessageDAOImpl.QUERY_BY_OCCURRENCE_ID_AND_EVENT_ID,
			new Object[]{occurrenceId, extractGeospatialissueValue},
			new RowMapperResultSetExtractor(gbifLogMessageRowMapper, 1));
	if (results.size()==0) {
		return null;
	} else if (results.size()>1) {
		logger.warn("Found multiple GbifLogMessages with id[" + occurrenceId + "]");
	}
	return results.get(0);
	}

	public long update(GbifLogMessage gbifLogMessage) {
			SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(gbifLogMessage);
			getNamedParameterTemplate().update(
					GbifLogMessageDAOImpl.UPDATE_SQL,
					namedParameters);
			return gbifLogMessage.getId();	
	}

	/**
	 * @return the instance
	 */
	public static GbifLogMessageDAOImpl getInstance() {
		return instance;
	}

	/**
	 * @param instance the instance to set
	 */
	public static void setInstance(GbifLogMessageDAOImpl instance) {
		GbifLogMessageDAOImpl.instance = instance;
	}
	
	/**
	 * @return the namedParameterTemplate
	 */
	public NamedParameterJdbcTemplate getNamedParameterTemplate() {
		return namedParameterTemplate;
	}

	/**
	 * @param namedParameterTemplate the namedParameterTemplate to set
	 */
	public void setNamedParameterTemplate(
			NamedParameterJdbcTemplate namedParameterTemplate) {
		this.namedParameterTemplate = namedParameterTemplate;
	}
	
}