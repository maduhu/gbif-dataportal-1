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
package org.gbif.portal.util.log.impl;

import org.gbif.portal.util.log.GbifLogMessage;
import org.gbif.portal.util.log.GbifLogMessageDAO;
import org.gbif.portal.util.log.LogEvent;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;
import java.util.List;


/**
 * Implementation of a DAO for log messages.
 *
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

  /**
   * Reusable row mapper
   */
  protected GbifLogMessageRowMapper gbifLogMessageRowMapper = new GbifLogMessageRowMapper();

  /**
   * Utility to create a GbifLogMessage for a row
   *
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
   *
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

  @SuppressWarnings("unchecked")
  public long create(final GbifLogMessage message) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    getJdbcTemplate().update(
      new PreparedStatementCreator() {
        public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
          // timestamp included?
          PreparedStatement ps = null;
          if (message.getTimestamp() != null) {
            ps = conn.prepareStatement(GbifLogMessageDAOImpl.CREATE_SQL_WITH_TIMESTAMP);
            Timestamp ts = new Timestamp(message.getTimestamp().getTime());
            ps.setTimestamp(13, ts);
          } else {
            ps = conn.prepareStatement(GbifLogMessageDAOImpl.CREATE_SQL);
          }
          ps.setLong(1, message.getPortalInstanceId());
          ps.setLong(2, (message.getLogGroup() == null) ? 0 : message.getLogGroup().getId());
          ps.setInt(3, message.getEvent().getValue());
          ps.setInt(4, message.getLevel().toInt());
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
        new Object[]{portalInstanceId},
        new RowMapperResultSetExtractor(longRowMapper));
    if (!results.isEmpty()) {
      maxId = results.get(0);
    }
    return maxId;
  }

  public int deleteExtractEventsByOccurrenceId(long occurrenceId) {
    return getJdbcTemplate().update(
      GbifLogMessageDAOImpl.DELETE_BY_OCCURRENCE_ID,
      new Object[]{LogEvent.EXTRACT_SCIENTIFICNAMEPARSEISSUE, LogEvent.EXTRACT_RANGE_END, occurrenceId});
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
}
