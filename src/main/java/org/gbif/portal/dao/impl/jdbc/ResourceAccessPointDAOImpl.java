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
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.dao.PropertyStoreNamespaceDAO;
import org.gbif.portal.dao.ResourceAccessPointDAO;
import org.gbif.portal.dao.impl.jdbc.rowmapper.IdRowMapper;
import org.gbif.portal.model.ResourceAccessPoint;
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
public class ResourceAccessPointDAOImpl extends JdbcDaoSupport implements
		ResourceAccessPointDAO {
	
	/**
	 * Gets the job group to use when determining workers
	 * Defualts to "harvesting"
	 */
	protected String jobGroup = "harvesting";
	
	/**
	 * The length of the URL substring to determine if they appear the same
	 */
	protected int substringLength = 20;

	/**
	 * The create sql
	 */
	protected static final String CREATE_SQL = "insert into resource_access_point(" +
												"data_provider_id," +
												"data_resource_id," +
												"url," +
												"remote_id_at_url," +
												"uuid," +
												"created," +
												"modified) " +
												"values (?,?,?,?,?,?,?)";

	/**
	 * The update sql
	 */
	protected static final String UPDATE_SQL = "update resource_access_point set " +
												"data_provider_id=?, " +
												"data_resource_id=?, " +
												"url=?, " +
												"remote_id_at_url=?, " +
												"uuid=?, " +
												"modified=? " +
												"where id=?";
	
	/**
	 * Updates
	 */
	protected static final String UPDATE_START_HARVEST_SQL = "update resource_access_point set last_harvest_start=? where id=?";
	protected static final String UPDATE_START_EXTRACT_SQL = "update resource_access_point set last_extract_start=? where id=?";
	protected static final String UPDATE_SUPPORTS_DATE_LAST_MODIFIED_SQL = "update resource_access_point set supports_date_last_modified=? where id=?";
	
	
	/**
	 * The delete sql
	 */
	protected static final String DELETE_SQL = "update resource_access_point set deleted=? where id=?";
	
	/**
	 * The query by ID sql
	 */
	protected static final String RETRIEVE_ALL_SQL = "select id, data_provider_id, data_resource_id, url, remote_id_at_url, uuid, supports_date_last_modified, last_harvest_start, last_extract_start, interval_metadata_days, interval_harvest_days, created, modified, deleted " +
	"from resource_access_point";	
	/**
	 * The query by ID sql
	 */
	protected static final String QUERY_BY_ID_SQL = "select id, data_provider_id, data_resource_id, url, remote_id_at_url, uuid, supports_date_last_modified, last_harvest_start, last_extract_start, interval_metadata_days, interval_harvest_days, created, modified, deleted " +
	"from resource_access_point where id=?";

	/**
	 * The query by UUID sql
	 */
	protected static final String QUERY_BY_UUID_SQL = "select id, data_provider_id, data_resource_id, url, remote_id_at_url, uuid, supports_date_last_modified, last_harvest_start, last_extract_start, interval_metadata_days, interval_harvest_days, created, modified, deleted " +
	"from resource_access_point where uuid=?";
	
	/**
	 * The query by URL, remoteID and DR id
	 */
	protected static final String QUERY_BY_URL_REMOTEID_DR_SQL = "select id, data_provider_id, data_resource_id, url, remote_id_at_url, uuid, supports_date_last_modified, last_harvest_start, last_extract_start, interval_metadata_days, interval_harvest_days, created, modified, deleted " +
	"from resource_access_point where remote_id_at_url=? and url=? and data_resource_id=?";
	
	/**
	 * Gets the ones that are 
	 */
	protected static final String QUERY_WORKING_SQL = "select working.id from QRTZ_FIRED_TRIGGERS q inner join resource_access_point working on concat('rap:',working.id) = q.TRIGGER_NAME " +
	"where working.id != ? and q.TRIGGER_GROUP!='EXTRACT' and substring(working.url, 1,?) = ?";
	
	/**
	 * Reusable row mapper
	 */
	protected ResourceAccessPointRowMapper resourceAccessPointRowMapper = new ResourceAccessPointRowMapper();
	
	/**
	 * DAOs
	 */
	protected PropertyStoreNamespaceDAO propertyStoreNamespaceDAO;
	
	/**
	 * Utility to create a ResourceAccessPoint for a row 
	 * @author trobertson
	 */
	protected class ResourceAccessPointRowMapper implements RowMapper {
		/**
		 * The factory
		 */
		public ResourceAccessPoint mapRow(ResultSet rs, int rowNumber) throws SQLException {
			return new ResourceAccessPoint(rs.getLong("id"),
					rs.getLong("data_provider_id"),
					rs.getLong("data_resource_id"),
					rs.getString("url"),
					rs.getString("remote_id_at_url"),
					rs.getString("uuid"),
					rs.getBoolean("supports_date_last_modified"),
					rs.getTimestamp("last_harvest_start"),
					rs.getTimestamp("last_extract_start"),
					rs.getInt("interval_metadata_days"),
					rs.getInt("interval_harvest_days"),
					rs.getTimestamp("created"),
					rs.getTimestamp("modified"),
					rs.getTimestamp("deleted"));
		}
	}
	
	/**
	 * @see org.gbif.portal.dao.ResourceAccessPointDAO#create(org.gbif.portal.model.ResourceAccessPoint)
	 */
	public long create(final ResourceAccessPoint resourceAccessPoint) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(
			new PreparedStatementCreator() {
				Timestamp createDate = new Timestamp(System.currentTimeMillis());
				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					   PreparedStatement ps = conn.prepareStatement(ResourceAccessPointDAOImpl.CREATE_SQL);
					   ps.setLong(1, resourceAccessPoint.getDataProviderId());
					   ps.setLong(2, resourceAccessPoint.getDataResourceId());
					   ps.setString(3, resourceAccessPoint.getUrl());
					   ps.setString(4, resourceAccessPoint.getRemoteIdAtUrl());
					   ps.setString(5, resourceAccessPoint.getUuid());
					   ps.setTimestamp(6, createDate);
					   ps.setTimestamp(7, createDate);
					   return ps;
				}					
			},
			keyHolder
		);
		resourceAccessPoint.setId(keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}

	/**
	 * @see org.gbif.portal.dao.ResourceAccessPointDAO#updateOrCreate(org.gbif.portal.model.ResourceAccessPoint)
	 */
	public long updateOrCreate(final ResourceAccessPoint resourceAccessPoint) {
		if (resourceAccessPoint.getId() <= 0) {
			return create(resourceAccessPoint);
		} else {
			getJdbcTemplate().update(
					new PreparedStatementCreator() {
						public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
							   PreparedStatement ps = conn.prepareStatement(ResourceAccessPointDAOImpl.UPDATE_SQL);
							   ps.setLong(1, resourceAccessPoint.getDataProviderId());
							   ps.setLong(2, resourceAccessPoint.getDataResourceId());
							   ps.setString(3, resourceAccessPoint.getUrl());
							   ps.setString(4, resourceAccessPoint.getRemoteIdAtUrl());
							   ps.setString(5, resourceAccessPoint.getUuid());
							   ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
							   ps.setLong(7, resourceAccessPoint.getId());							   
							   return ps;
						}					
					}
			);
			
			return resourceAccessPoint.getId();
		}
	}

	/**
	 * @see org.gbif.portal.dao.ResourceAccessPointDAO#setStartExtract(java.util.Date)
	 */
	public void setStartExtract(final Date date, final long id) {
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						   PreparedStatement ps = conn.prepareStatement(ResourceAccessPointDAOImpl.UPDATE_START_EXTRACT_SQL);
						   ps.setTimestamp(1, new Timestamp(date.getTime()));
						   ps.setLong(2, id);
						   return ps;
					}					
				}
		);
	}

	/**
	 * @see org.gbif.portal.dao.ResourceAccessPointDAO#setStartHarvest(java.util.Date)
	 */
	public void setStartHarvest(final Date date, final long id) {
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						   PreparedStatement ps = conn.prepareStatement(ResourceAccessPointDAOImpl.UPDATE_START_HARVEST_SQL);
						   ps.setTimestamp(1, new Timestamp(date.getTime()));
						   ps.setLong(2, id);
						   return ps;
					}					
				}
		);
	}	
	
	/**
	 * @see org.gbif.portal.dao.ResourceAccessPointDAO#setSupportsDateLastModified(boolean, long)
	 */
	public void setSupportsDateLastModified(final boolean supports, final long id) {
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						   PreparedStatement ps = conn.prepareStatement(ResourceAccessPointDAOImpl.UPDATE_SUPPORTS_DATE_LAST_MODIFIED_SQL);
						   ps.setBoolean(1, supports);
						   ps.setLong(2, id);
						   return ps;
					}					
				}
		);
	}	
	

	/**
	 * @see org.gbif.portal.dao.ResourceAccessPointDAO#getByUuid(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<ResourceAccessPoint> getByUuid(final String uuid) {
		List<ResourceAccessPoint> results = (List<ResourceAccessPoint>) getJdbcTemplate()
			.query(ResourceAccessPointDAOImpl.QUERY_BY_UUID_SQL,
				new Object[] {uuid},
				new RowMapperResultSetExtractor(resourceAccessPointRowMapper));
		populateNamespaces(results);
		return results;
	}
	
	/**
	 * @see org.gbif.portal.dao.ResourceAccessPointDAO#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List<ResourceAccessPoint> getAll() {
		List<ResourceAccessPoint> results = (List<ResourceAccessPoint>) getJdbcTemplate()
		.query(ResourceAccessPointDAOImpl.RETRIEVE_ALL_SQL,
			new Object[] {},
			new RowMapperResultSetExtractor(resourceAccessPointRowMapper));
		populateNamespaces(results);			
		return results;
	}

	/**
	 * @see org.gbif.portal.dao.ResourceAccessPointDAO#getByRemoteIdAtUrlAndUrlForResource(java.lang.String, java.lang.String, long)
	 */
	@SuppressWarnings("unchecked")
	public ResourceAccessPoint getByRemoteIdAtUrlAndUrlForResource(final String remoteId, final String URL, final long dataResourceId) {
		List<ResourceAccessPoint> results = (List<ResourceAccessPoint>) getJdbcTemplate()
			.query(ResourceAccessPointDAOImpl.QUERY_BY_URL_REMOTEID_DR_SQL,
				new Object[] {remoteId, URL, dataResourceId},
				new RowMapperResultSetExtractor(resourceAccessPointRowMapper));
		populateNamespaces(results);
		if (results.size() == 0) {
			return null;
		} else if (results.size()>1) {
			logger.warn("Found multiple ResourceAccessPoints with RemoteIdAtURL[" + remoteId + "], URL[" + URL +"], DR_ID[" + dataResourceId + "]");
		}
		return results.get(0);
	}
	
	
	
	/**
	 * @see org.gbif.portal.dao.ResourceAccessPointDAO#getByUuid(long)
	 */
	@SuppressWarnings("unchecked")
	public ResourceAccessPoint getById(final long id) {
		List<ResourceAccessPoint> results = (List<ResourceAccessPoint>) getJdbcTemplate()
			.query(ResourceAccessPointDAOImpl.QUERY_BY_ID_SQL,
				new Object[] {id},
				new RowMapperResultSetExtractor(resourceAccessPointRowMapper));
		populateNamespaces(results);
		if (results.size() == 0) {
			return null;
		} else if (results.size()>1) {
			logger.warn("Found multiple ResourceAccessPoints with ID: " + id);
		}
		return results.get(0);
	}

	/**
	 * Populates the namespace list for the Resource access points
	 * @param resourceAccessPoints To get the namespaces for
	 */
	protected void populateNamespaces(List<ResourceAccessPoint> resourceAccessPoints) {
		for (ResourceAccessPoint resourceAccessPoint : resourceAccessPoints) {
			resourceAccessPoint.setNamespaces(
					propertyStoreNamespaceDAO.getNamespacesForResourceAccessPoint(resourceAccessPoint.getId()));
		}
	}
	
	/**
	 * @return Returns the propertyStoreNamespaceDAO.
	 */
	public PropertyStoreNamespaceDAO getPropertyStoreNamespaceDAO() {
		return propertyStoreNamespaceDAO;
	}

	/**
	 * @param propertyStoreNamespaceDAO The propertyStoreNamespaceDAO to set.
	 */
	public void setPropertyStoreNamespaceDAO(
			PropertyStoreNamespaceDAO propertyStoreNamespaceDAO) {
		this.propertyStoreNamespaceDAO = propertyStoreNamespaceDAO;
	}

	/**
	 * Using the QRTZ tables, sees if there appears to be a worker on the same URL
	 * This is capable of providing race conditions so isn't 100% conclusive
	 * @see org.gbif.portal.dao.ResourceAccessPointDAO#isURLBeingHarvested(org.gbif.portal.model.ResourceAccessPoint)
	 */
	@SuppressWarnings("unchecked")
	public boolean isURLBeingHarvested(final String url, final long id) {
		List<Long> results = (List<Long>) getJdbcTemplate()
		.query(ResourceAccessPointDAOImpl.QUERY_WORKING_SQL,
			new Object[] { 
				id,
				getSubstringLength(),
				url.substring(0,getSubstringLength())},
			new RowMapperResultSetExtractor(new IdRowMapper()));
		if (results.size() == 0) {
			return false;
		} else {		
			return true;
		}
	}

	/**
	 * @return Returns the jobGroup.
	 */
	public String getJobGroup() {
		return jobGroup;
	}

	/**
	 * @param jobGroup The jobGroup to set.
	 */
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	/**
	 * @return Returns the substringLength.
	 */
	public int getSubstringLength() {
		return substringLength;
	}

	/**
	 * @param substringLength The substringLength to set.
	 */
	public void setSubstringLength(int substringLength) {
		this.substringLength = substringLength;
	}
}
