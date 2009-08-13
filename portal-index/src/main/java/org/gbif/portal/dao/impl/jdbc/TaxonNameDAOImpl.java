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
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.dao.TaxonNameDAO;
import org.gbif.portal.dao.impl.jdbc.rowmapper.TaxonNameRowMapper;
import org.gbif.portal.model.TaxonName;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * Pure JDBC implementation
 * @author trobertson
 */
public class TaxonNameDAOImpl extends JdbcDaoSupport implements
		TaxonNameDAO {

	/**
	 * The create sql
	 */
	protected static final String CREATE_SQL = "insert into taxon_name(" +
												"canonical," +
												"supra_generic," +
												"generic," +
												"infrageneric," +
												"specific_epithet," +
												"infraspecific," +
												"infraspecific_marker," +
												"is_hybrid," +
												"rank," +
												"author," +
												"searchable_canonical) " +
												"values (?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * The update sql
	 */
	protected static final String UPDATE_SQL = "update taxon_name " +
												"set canonical=?," +
												"set supra_generic=?," +
												"set generic=?," +
												"set infrageneric=?," +
												"set specific_epithet=?," +
												"set infraspecific=?," +
												"set infraspecific_marker=?," +
												"set is_hybrid=?," +
												"set rank=?," +
												"set author=? where id=?";

	/**
	 * The delete sql
	 */
	protected static final String DELETE_SQL = "delete from taxon_name where id = ?";

	/**
	 * The get by id
	 */
	protected static final String QUERY_BY_ID_SQL = 
	"select tn.id,tn.canonical,tn.supra_generic,tn.generic,tn.infrageneric,tn.specific_epithet,tn.infraspecific,tn.infraspecific_marker,tn.is_hybrid,tn.rank,tn.author " +
	" from taxon_name tn where tn.id=?";
	
	/**
	 * The get by canonical author and rank sql (e.g. Business Unique)
	 */
	protected static final String QUERY_BY_CANONICAL_AUTHOR_RANK_SQL = 
	"select tn.id,tn.canonical,tn.supra_generic,tn.generic,tn.infrageneric,tn.specific_epithet,tn.infraspecific,tn.infraspecific_marker,tn.is_hybrid,tn.rank,tn.author " +
	" from taxon_name tn where tn.canonical=? and tn.author=? and tn.rank=?";
	
	/**
	 * The get by canonical and rank sql with no author (e.g. Business Unique)
	 */
	protected static final String QUERY_BY_CANONICAL_RANK_NOAUTHOR_SQL = 
	"select tn.id,tn.canonical,tn.supra_generic,tn.generic,tn.infrageneric,tn.specific_epithet,tn.infraspecific,tn.infraspecific_marker,tn.is_hybrid,tn.rank,tn.author " +
	" from taxon_name tn where tn.canonical=? and tn.author is null and tn.rank=?";
	
	/**
	 * The get by canonical and lowest rank sql
	 */
	protected static final String QUERY_BY_CANONICAL_LOWEST_RANK_SQL = 
	"select tn.id,tn.canonical,tn.supra_generic,tn.generic,tn.infrageneric,tn.specific_epithet,tn.infraspecific,tn.infraspecific_marker,tn.is_hybrid,tn.rank,tn.author " +
	" from taxon_name tn where tn.canonical=? and tn.rank<=?";
	
	/**
	 * Gets all
	 */
	protected static final String GET_ALL = 
	"select tn.id,tn.canonical,tn.supra_generic,tn.generic,tn.infrageneric,tn.specific_epithet,tn.infraspecific,tn.infraspecific_marker,tn.is_hybrid,tn.rank,tn.author " +
	" from taxon_name tn limit ?,?";
	
	/**
	 * Updates the searchable canonical
	 */
	protected static final String SET_SEARCHABLE_CANONICAL = 
	"update taxon_name set searchable_canonical=? where id=?";
	
	/**
	 * Reusable mapper
	 */
	protected TaxonNameRowMapper taxonNameRowMapper = new TaxonNameRowMapper();
	
	/**
	 * @see org.gbif.portal.dao.TaxonNameDAO#create(org.gbif.portal.harvest.taxonomy.TaxonName)
	 */
	public long create(final TaxonName taxonName) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(
			new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					PreparedStatement ps = conn.prepareStatement(TaxonNameDAOImpl.CREATE_SQL);
					ps.setString(1, taxonName.getCanonical());
					ps.setString(2, taxonName.getSupraGeneric());
					ps.setString(3, taxonName.getGeneric());
					ps.setString(4, taxonName.getInfraGeneric());
					ps.setString(5, taxonName.getSpecific());
					ps.setString(6, taxonName.getInfraSpecific());
					ps.setString(7, taxonName.getInfraSpecificMarker());
					ps.setInt(8, taxonName.getType());
					ps.setInt(9, taxonName.getRank());
					ps.setString(10, taxonName.getAuthor());
					ps.setString(11, taxonName.getSearchableCanonical());
					return ps;
				}					
			},
			keyHolder
		);
		taxonName.setId(keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}
	
	/**
	 * @see org.gbif.portal.dao.TaxonNameDAO#create(org.gbif.portal.harvest.taxonomy.TaxonName)
	 */
	public long createOrUpdate(final TaxonName taxonName) {
		if (taxonName.getId() == null) {
			return create(taxonName);
		} else {
			getJdbcTemplate().update(
					new PreparedStatementCreator() {
						public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
							PreparedStatement ps = conn.prepareStatement(TaxonNameDAOImpl.UPDATE_SQL);
							ps.setString(1, taxonName.getCanonical());
							ps.setString(2, taxonName.getSupraGeneric());
							ps.setString(3, taxonName.getGeneric());
							ps.setString(4, taxonName.getInfraGeneric());
							ps.setString(5, taxonName.getSpecific());
							ps.setString(6, taxonName.getInfraSpecific());
							ps.setString(7, taxonName.getInfraSpecificMarker());
							ps.setInt(8, taxonName.getType());
							ps.setInt(9, taxonName.getRank());
							ps.setString(10, taxonName.getAuthor());
							ps.setLong(11, taxonName.getId());
							return ps;
					}					
				}
			);
			return taxonName.getId();
		}
	}
	
	/**
	 * @see org.gbif.portal.dao.TaxonNameDAO#getUnique(java.lang.String, java.lang.String, int)
	 */
	@SuppressWarnings("unchecked")
	public TaxonName getUnique(String canonical, String author, int rank) {
		List<TaxonName> results = null;
		if (StringUtils.isEmpty(author)) {
			results = (List<TaxonName>) getJdbcTemplate()
			.query(TaxonNameDAOImpl.QUERY_BY_CANONICAL_RANK_NOAUTHOR_SQL,
				new Object[]{canonical, rank},
				new RowMapperResultSetExtractor(taxonNameRowMapper, 1));
		} else {
			results = (List<TaxonName>) getJdbcTemplate()
			.query(TaxonNameDAOImpl.QUERY_BY_CANONICAL_AUTHOR_RANK_SQL,
				new Object[]{canonical, author, rank},
				new RowMapperResultSetExtractor(taxonNameRowMapper, 1));
		}
		if (results.size() == 0) {
			return null;
		} else if (results.size()>1) {
			logger.warn("Found multiple TaxonNames with canonical[" + canonical + "], author[" + author + 
					"], rank[" + rank + "]");
		}
		return results.get(0);
	}
	
	/**
	 * @see org.gbif.portal.dao.TaxonNameDAO#getUnique(java.lang.String, java.lang.String, int)
	 */
	@SuppressWarnings("unchecked")
	public TaxonName getById(long id) {
		List<TaxonName> results = (List<TaxonName>) getJdbcTemplate()
			.query(TaxonNameDAOImpl.QUERY_BY_ID_SQL,
				new Object[]{id},
				new RowMapperResultSetExtractor(taxonNameRowMapper, 1));
		if (results.size() == 0) {
			return null;
		} else if (results.size()>1) {
			logger.warn("Found multiple TaxonNames with id[" + id + "]");
		}
		return results.get(0);
	}

	/**
	 * @see org.gbif.portal.dao.TaxonNameDAO#setSearchableCanonical(long, java.lang.String)
	 */
	public void setSearchableCanonical(final long id, final String searchableCanonical) {
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(TaxonNameDAOImpl.SET_SEARCHABLE_CANONICAL);
						ps.setString(1, searchableCanonical);
						ps.setLong(2, id);
						return ps;
					}
				}
			);
	}

	/**
	 * @see org.gbif.portal.dao.TaxonNameDAO#getTaxonName(long, int)
	 */
	@SuppressWarnings("unchecked")
	public List<TaxonName> getTaxonName(long start, int limit) {
		return (List<TaxonName>) getJdbcTemplate()
			.query(TaxonNameDAOImpl.GET_ALL,
				new Object[]{start, limit},
				new RowMapperResultSetExtractor(taxonNameRowMapper, limit));
	}

	/* (non-Javadoc)
	 * @see org.gbif.portal.dao.TaxonNameDAO#delete(long)
	 */
	public void delete(final long id) {
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						   PreparedStatement ps = conn.prepareStatement(TaxonNameDAOImpl.DELETE_SQL);
						   ps.setLong(1, id);
						   return ps;
					}					
				}
			);
	}

	/**
	 * @see org.gbif.portal.dao.TaxonNameDAO#getByCanonicalAndLowestRank(java.lang.String, int)
	 */
	@SuppressWarnings("unchecked")
	public List<TaxonName> getByCanonicalAndLowestRank(String canonical, int lowestRankInclusive) {
		return (List<TaxonName>) getJdbcTemplate()
		.query(TaxonNameDAOImpl.QUERY_BY_CANONICAL_LOWEST_RANK_SQL,
			new Object[]{canonical, lowestRankInclusive},
			new RowMapperResultSetExtractor(taxonNameRowMapper, 10));
	}
}
