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

import org.gbif.portal.dao.PropertyStoreNamespaceDAO;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * Pure JDBC implementation
 * @author trobertson
 */
public class PropertyStoreNamespaceDAOImpl extends JdbcDaoSupport implements
		PropertyStoreNamespaceDAO {
	/**
	 * Create the mappings 
	 */
	protected static final String CREATE_MAPPINGS = 
		"insert into namespace_mapping(resource_access_point_id, property_store_namespace_id, priority) " +
		"select ?, id, ? from property_store_namespace where namespace=?"; 
	
	/**
	 * Get the mappings 
	 */
	protected static final String GET_MAPPINGS_FOR_RESOURCE = 
		"select namespace from namespace_mapping inner join property_store_namespace on id = property_store_namespace_id " +
		"where resource_access_point_id=? order by priority"; 
	
	/**
	 * Insert a new mapping if there isn't one
	 */
	protected static final String INSERT_MAPPING_IF_NECESSARY = 
		"insert into namespace_mapping(resource_access_point_id, property_store_namespace_id, priority) " +
		"select ?, psn.id,	max(existing.priority)+1 " + 
		"from property_store_namespace psn left outer join namespace_mapping nm on nm.property_store_namespace_id = psn.id and nm.resource_access_point_id=?, namespace_mapping existing " +
		"where nm.property_store_namespace_id is null and existing.resource_access_point_id=? and psn.namespace=? " +
		"group by 1, 2";
	
	protected static final String DELETE_FOR_RESOURCE_ACCESS_POINT =
		"delete from namespace_mapping where resource_access_point_id=?";
	
	/**
	 * String row mapper
	 */
	protected SimpleStringRowMapper simpleStringRowMapper = new SimpleStringRowMapper(); 
	
	/**
	 * Utility to create a String for a row 
	 * @author trobertson
	 */
	protected class SimpleStringRowMapper implements RowMapper {
		/**
		 * The factory
		 */
		public String mapRow(ResultSet rs, int rowNumber) throws SQLException {
			return new String(rs.getString(1));
		}
	}	
	
	/**
	 * @see org.gbif.portal.dao.PropertyStoreNamespaceDAO#createNamespaceMappings(long, java.util.List)
	 */
	public void createNamespaceMappings(final long resourceAccessPointId, List<String> namespaces) {
		int i=0;
		for (final String namespace : namespaces) {
			final int temp = i++;
			getJdbcTemplate().update(
					new PreparedStatementCreator() {
						public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
							   PreparedStatement ps = conn.prepareStatement(PropertyStoreNamespaceDAOImpl.CREATE_MAPPINGS);
							   ps.setLong(1, resourceAccessPointId);
							   ps.setInt(2, temp);
							   ps.setString(3, namespace);
							   return ps;
						}					
					}
			);
		}
	}

	/**
	 * @see org.gbif.portal.dao.PropertyStoreNamespaceDAO#getNamespacesForResourceAccessPoint(long)
	 */
	@SuppressWarnings("unchecked")
	public List<String> getNamespacesForResourceAccessPoint(long resourceAccessPointId) {
		return (List<String>) getJdbcTemplate()
		.query(PropertyStoreNamespaceDAOImpl.GET_MAPPINGS_FOR_RESOURCE,
			new Object[] {new Long(resourceAccessPointId)},
			new RowMapperResultSetExtractor(simpleStringRowMapper));
	}

	/**
	 * @see org.gbif.portal.dao.PropertyStoreNamespaceDAO#appendNamespaceIfNotAttached(long, java.lang.String)
	 */
	public void appendNamespaceIfNotAttached(final long resourceAccessPointId, final String namespace) {
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						   PreparedStatement ps = conn.prepareStatement(PropertyStoreNamespaceDAOImpl.INSERT_MAPPING_IF_NECESSARY);
						   ps.setLong(1, resourceAccessPointId);
						   ps.setLong(2, resourceAccessPointId);
						   ps.setLong(3, resourceAccessPointId);
						   ps.setString(4, namespace);
						   return ps;
					}					
				}
		);
	}

	/**
	 * @see org.gbif.portal.dao.PropertyStoreNamespaceDAO#createNamespaceMappings(long, java.util.List, boolean)
	 */
	public void createNamespaceMappings(final long resourceAccessPointId, List<String> namespaces, boolean deleteOldMappings) {
		if (deleteOldMappings) {
			getJdbcTemplate().update(
					new PreparedStatementCreator() {
						public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
							   PreparedStatement ps = conn.prepareStatement(PropertyStoreNamespaceDAOImpl.DELETE_FOR_RESOURCE_ACCESS_POINT);
							   ps.setLong(1, resourceAccessPointId);
							   return ps;
						}					
					}
			);
		}
		createNamespaceMappings(resourceAccessPointId, namespaces);		
	}
}
