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
import java.util.Date;
import java.util.List;

import org.gbif.portal.dao.AgentDAO;
import org.gbif.portal.dao.impl.jdbc.rowmapper.IntRowMapper;
import org.gbif.portal.model.Agent;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * A pure jdbc implementation
 * @author trobertson
 */
public class AgentDAOImpl extends JdbcDaoSupport implements AgentDAO {
	/**
	 * The create SQL 
	 */	
	protected static final String CREATE_SQL = "insert into agent(" +
												"name,"+
												"address," +
												"email," +
												"telephone," +
												"created," +
												"modified) " +
												"values (" +
												":name," +
												":address," +
												":email," +
												":telephone," +
											    ":created," +
											    ":modified)";
	
	/**
	 * The update SQL 
	 */	
	protected static final String UPDATE_SQL = "update agent set " +
												"name=:name,"+
												"address=:address," +
												"email=:email," +
												"telephone=:telephone," +
												"modified=:modified where id=:id";
	
	/**
	 * The query by name and email
	 */
	protected static final String QUERY_BY_NAME_AND_EMAIL_SQL = 
		"select a.id,a.name,a.email,a.address,a.telephone,a.created,a.modified,a.deleted " + 
		"from agent a " +
		"where a.name=? and a.email=?";
	
	/**
	 * Associations to DP and DR
	 */
	protected static final String SET_TO_DATA_PROVIDER_SQL = "insert into data_provider_agent(data_provider_id, agent_id, agent_type) values(?,?,?)";
	protected static final String SET_TO_DATA_RESOURCE_SQL = "insert into data_resource_agent(data_resource_id, agent_id, agent_type) values(?,?,?)";
	
	/**
	 * Finders for the associations
	 */
	protected static final String QUERY_FOR_PROVIDER_AGENT = "select id from data_provider_agent where data_provider_id=? and agent_id=? and agent_type=?";
	protected static final String QUERY_FOR_RESOURCE_AGENT = "select id from data_resource_agent where data_resource_id=? and agent_id=? and agent_type=?";
	
	/**
	 * Removers for the associations
	 */
	protected static final String DELETE_PROVIDER_AGENT = "delete from data_provider_agent where data_provider_id=? and agent_id=? and agent_type=?";
	protected static final String DELETE_RESOURCE_AGENT = "delete from data_resource_agent where data_resource_id=? and agent_id=? and agent_type=?";
		
	/**
	 * Row mappers
	 */
	protected AgentRowMapper agentRowMapper = new AgentRowMapper(); 
	protected IntRowMapper intRowMapper = new IntRowMapper();
	
	/**
	 * To make use of bean naming
	 */
	protected NamedParameterJdbcTemplate namedParameterTemplate;
	
	/**
	 * Utility to create an Agentfor a row 
	 * @author trobertson
	 */
	protected class AgentRowMapper implements RowMapper {
		/**
		 * The factory
		 */
		public Agent mapRow(ResultSet rs, int rowNumber) throws SQLException {
			Agent a =  new Agent();
			a.setId(rs.getLong("a.id"));
			a.setAddress(rs.getString("a.address"));
			a.setName(rs.getString("a.name"));
			a.setEmail(rs.getString("a.email"));
			a.setCreated(rs.getDate("created"));
			a.setModified(rs.getDate("modified"));
			a.setDeleted(rs.getDate("deleted"));
			return a;
		}
	}	
	
	/**
	 * @see org.gbif.portal.dao.AgentDAO#create(org.gbif.portal.model.Agent)
	 */
	public long create(final Agent agent) {
		Date now = new Date();
		agent.setModified(now);
		agent.setCreated(now);
		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(agent);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getNamedParameterTemplate().update(
			AgentDAOImpl.CREATE_SQL,
			namedParameters,
			keyHolder);
		agent.setId(keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}

	/**
	 * @see org.gbif.portal.dao.AgentDAO#updateOrCreate(org.gbif.portal.model.Agent)
	 */
	public long updateOrCreate(final Agent agent) {
		if (agent.getId()<=0) {
			return create(agent);
		}  else {
			agent.setModified(new Date());
			SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(agent);
			getNamedParameterTemplate().update(
					AgentDAOImpl.UPDATE_SQL,
					namedParameters);
			return agent.getId();	
		}
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

	/**
	 * @see org.gbif.portal.dao.AgentDAO#getByNameAndEmail(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Agent getByNameAndEmail(final String name, final String email) {
		List<Agent> results = (List<Agent>) getJdbcTemplate()
			.query(AgentDAOImpl.QUERY_BY_NAME_AND_EMAIL_SQL,
				new Object[]{name,email},
				new RowMapperResultSetExtractor(agentRowMapper, 1));
		if (results.size() == 0) {
			return null;
		} else if (results.size()>1) {
			logger.warn("Found multiple Agents with name[" + name + "] and email[" + email + "]");
		}
		return results.get(0);
	}

	/**
	 * @see org.gbif.portal.dao.AgentDAO#associateAgentWithResource(long, long, int)
	 */
	public void associateAgentWithResource(final long dataResourceId, final long agentId, final int agentType) {
		if (!isAgentAssociatedWithResource(dataResourceId, agentId, agentType)) {
			getJdbcTemplate().update(
					new PreparedStatementCreator() {
						public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
							PreparedStatement ps = conn.prepareStatement(AgentDAOImpl.SET_TO_DATA_RESOURCE_SQL);
							ps.setLong(1,dataResourceId);
							ps.setLong(2,agentId);
							ps.setInt(3,agentType);
							return ps;
						}
					});
		}
	}
	
	/**
	 * @see org.gbif.portal.dao.AgentDAO#associateAgentWithProvider(long, long, int)
	 */
	public void associateAgentWithProvider(final long dataProviderId, final long agentId, final int agentType) {
		if (!isAgentAssociatedWithProvider(dataProviderId, agentId, agentType)) {
			getJdbcTemplate().update(
					new PreparedStatementCreator() {
						public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
							PreparedStatement ps = conn.prepareStatement(AgentDAOImpl.SET_TO_DATA_PROVIDER_SQL);
							ps.setLong(1,dataProviderId);
							ps.setLong(2,agentId);
							ps.setInt(3,agentType);
							return ps;
						}
					});
		}		
	}

	/**
	 * @see org.gbif.portal.dao.AgentDAO#disassociateAgentWithResource(long, long, int)
	 */
	public void disassociateAgentWithResource(final long dataResourceId, final long agentId, final int agentType) {
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(AgentDAOImpl.DELETE_RESOURCE_AGENT);
						ps.setLong(1,dataResourceId);
						ps.setLong(2,agentId);
						ps.setInt(3,agentType);
						return ps;
					}
				});
	}

	/**
	 * @see org.gbif.portal.dao.AgentDAO#disassociateAgentWithProvider(long, long, int)
	 */
	public void disassociateAgentWithProvider(final long dataProviderId, final long agentId, final int agentType) {
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(AgentDAOImpl.DELETE_PROVIDER_AGENT);
						ps.setLong(1,dataProviderId);
						ps.setLong(2,agentId);
						ps.setInt(3,agentType);
						return ps;
					}
				});
	}
	
	/**
	 * @see org.gbif.portal.dao.AgentDAO#isAgentAssociatedWithResource(long, long, int)
	 */
	@SuppressWarnings("unchecked")
	public boolean isAgentAssociatedWithResource(long dataResourceId, long agentId, int agentType) {
		List<Integer> results = (List<Integer>)getJdbcTemplate()
		 .query(AgentDAOImpl.QUERY_FOR_RESOURCE_AGENT,
			new Object[]{dataResourceId, agentId, agentType},
			new RowMapperResultSetExtractor(intRowMapper, 1));
		if (results.size()>0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @see org.gbif.portal.dao.AgentDAO#isAgentAssociatedWithProvider(long, long, int)
	 */
	@SuppressWarnings("unchecked")
	public boolean isAgentAssociatedWithProvider(long dataProviderId, long agentId, int agentType) {
		List<Integer> results = (List<Integer>)getJdbcTemplate()
		 .query(AgentDAOImpl.QUERY_FOR_PROVIDER_AGENT,
			new Object[]{dataProviderId, agentId, agentType},
			new RowMapperResultSetExtractor(intRowMapper, 1));
		if (results.size()>0) {
			return true;
		} else {
			return false;
		}
	}
	
}
