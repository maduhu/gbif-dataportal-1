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

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.dao.CommonNameDAO;
import org.gbif.portal.model.CommonName;
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
public class CommonNameDAOImpl extends JdbcDaoSupport implements
		CommonNameDAO {

	/**
	 * The create sql
	 */
	protected static final String CREATE_SQL = "insert into common_name(" +
												"taxon_concept_id," +
												"name," +
												"iso_language_code," +
												"language) " +
												"values (?,?,?,?)";
	
	/**
	 * The update sql
	 */
	protected static final String UPDATE_SQL = "update common_name " +
												"set taxon_concept_id=?," +
												"set name=?," +
												"set iso_language_code=?," +
												"set language=? where id=?";

	/**
	 * The delete sql
	 */
	protected static final String DELETE_SQL = "delete from common_name where id = ?";

	/**
	 * The get by id
	 */
	protected static final String QUERY_BY_ID_SQL = 
	"select cn.id,cn.taxon_concept_id,cn.name,cn.iso_language_code,cn.language " +
	" from common_name cn where cn.id=?";
	
	/**
	 * The get by taxon_concept_id, name and language sql (e.g. Business Unique)
	 */
	protected static final String QUERY_BY_CONCEPT_NAME_LANGUAGE_SQL = 
		"select cn.id,cn.taxon_concept_id,cn.name,cn.iso_language_code,cn.language " +
		" from common_name cn where cn.taxon_concept_id=? and cn.name=? and cn.language=?";

	/**
	 * The get by taxon_concept_id, name and language sql (e.g. Business Unique)
	 */
	protected static final String QUERY_BY_CONCEPT_NAME_NOLANGUAGE_SQL = 
		"select cn.id,cn.taxon_concept_id,cn.name,cn.iso_language_code,cn.language " +
		" from common_name cn where cn.taxon_concept_id=? and cn.name=? and cn.language is null";
	
	/**
	 * Reusable mapper
	 */
	protected CommonNameRowMapper commonNameRowMapper = new CommonNameRowMapper();

	/**
	 * Utility to create a CommonName for a row 
	 * @author trobertson
	 */
	protected class CommonNameRowMapper implements RowMapper {
		/**
		 * The factory
		 */
		public CommonName mapRow(ResultSet rs, int rowNumber) throws SQLException {
			return new CommonName(rs.getLong("id"),
					rs.getLong("taxon_concept_id"),
					rs.getString("name"),
					rs.getString("iso_language_code"),
					rs.getString("language"));			
		}
	}

	/**
	 * @see org.gbif.portal.dao.CommonNameDAO#create(org.gbif.portal.harvest.taxonomy.CommonName)
	 */
	public long create(final CommonName commonName) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(
			new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					PreparedStatement ps = conn.prepareStatement(CommonNameDAOImpl.CREATE_SQL);
					ps.setLong(1, commonName.getTaxonConceptId());
					ps.setString(2, commonName.getName());
					ps.setString(3, commonName.getIsoLanguageCode());
					ps.setString(4, commonName.getLanguage());
					return ps;
				}					
			},
			keyHolder
		);
		commonName.setId(keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}
	
	/**
	 * @see org.gbif.portal.dao.CommonNameDAO#create(org.gbif.portal.harvest.taxonomy.CommonName)
	 */
	public long createOrUpdate(final CommonName commonName) {
		if (commonName.getId() == null) {
			return create(commonName);
		} else {
			getJdbcTemplate().update(
					new PreparedStatementCreator() {
						public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
							PreparedStatement ps = conn.prepareStatement(CommonNameDAOImpl.UPDATE_SQL);
							ps.setLong(1, commonName.getTaxonConceptId());
							ps.setString(2, commonName.getName());
							ps.setString(3, commonName.getIsoLanguageCode());
							ps.setString(4, commonName.getLanguage());
							ps.setLong(5, commonName.getId());
							return ps;
					}					
				}
			);
			return commonName.getId();
		}
	}
	
	/**
	 * @see org.gbif.portal.dao.CommonNameDAO#getUnique(java.lang.Long, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public CommonName getUnique(Long taxonConceptId, String name, String language) {
		List<CommonName> results = null;
		if (StringUtils.isEmpty(language)) {
			results = (List<CommonName>) getJdbcTemplate()
			.query(CommonNameDAOImpl.QUERY_BY_CONCEPT_NAME_NOLANGUAGE_SQL,
				new Object[]{taxonConceptId, name},
				new RowMapperResultSetExtractor(commonNameRowMapper, 1));
		} else {
			results = (List<CommonName>) getJdbcTemplate()
			.query(CommonNameDAOImpl.QUERY_BY_CONCEPT_NAME_LANGUAGE_SQL,
				new Object[]{taxonConceptId, name, language},
				new RowMapperResultSetExtractor(commonNameRowMapper, 1));
		}
		if (results.size() == 0) {
			return null;
		} else if (results.size()>1) {
			logger.warn("Found multiple CommonNames with concept[" + taxonConceptId + "], name[" + name + 
					"], language[" + language + "]");
		}
		return results.get(0);
	}
	
	/**
	 * @see org.gbif.portal.dao.CommonNameDAO#getUnique(java.lang.String, java.lang.String, int)
	 */
	@SuppressWarnings("unchecked")
	public CommonName getById(long id) {
		List<CommonName> results = (List<CommonName>) getJdbcTemplate()
			.query(CommonNameDAOImpl.QUERY_BY_ID_SQL,
				new Object[]{id},
				new RowMapperResultSetExtractor(commonNameRowMapper, 1));
		if (results.size() == 0) {
			return null;
		} else if (results.size()>1) {
			logger.warn("Found multiple CommonNames with id[" + id + "]");
		}
		return results.get(0);
	}

	/* (non-Javadoc)
	 * @see org.gbif.portal.dao.CommonNameDAO#delete(long)
	 */
	public void delete(final long id) {
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						   PreparedStatement ps = conn.prepareStatement(CommonNameDAOImpl.DELETE_SQL);
						   ps.setLong(1, id);
						   return ps;
					}					
				}
			);
	}
}
