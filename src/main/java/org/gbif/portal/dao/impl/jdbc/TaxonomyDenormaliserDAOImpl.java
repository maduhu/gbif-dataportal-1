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

import org.gbif.portal.dao.TaxonomyDenormaliserDAO;
import org.gbif.portal.dao.impl.jdbc.rowmapper.TaxonConceptRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * Pure JDBC implementation
 * @author trobertson
 */
public class TaxonomyDenormaliserDAOImpl extends JdbcDaoSupport implements
		TaxonomyDenormaliserDAO {

	/**
	 * Criteria to append when working within a provider
	 */
	protected static final String PROVIDER_ID_CRITERIA_SQL = " tc.data_provider_id=? ";
	
	/**
	 * Criteria to append when working within a provider
	 */
	protected static final String RESOURCE_ID_CRITERIA_SQL = " tc.data_resource_id=? ";
	
	/**
	 * The sql updates for denormalisation updating
	 */
	protected static final String CLEAR_DENORMALISED_DATA = "update taxon_concept tc set tc.kingdom_concept_id=null, tc.phylum_concept_id=null, tc.class_concept_id=null, tc.order_concept_id=null, tc.family_concept_id=null, tc.genus_concept_id=null, tc.species_concept_id=null where ";
	
	/**
	 * The sql updates for denormalisation updating
	 */
	protected static final String GET_DISTINCT_RANKS_PREFIX = "select distinct rank from taxon_concept tc where"; 
	protected static final String GET_DISTINCT_RANKS_POSTFIX = " order by tc.rank";
	
	/**
	 * These are used for building some sql to copy from the parent
	 */
	protected static final String UPDATE_COPY_FROM_PARENT_PREFIX = "update taxon_concept tc left join taxon_concept pc on pc.id = tc.parent_concept_id set ";
	protected static final String UPDATE_SET_KINGDOM = "tc.kingdom_concept_id = tc.id ";
	protected static final String UPDATE_SET_PHYLUM = "tc.phylum_concept_id = tc.id ";
	protected static final String UPDATE_SET_CLASS = "tc.class_concept_id = tc.id ";
	protected static final String UPDATE_SET_ORDER = "tc.order_concept_id = tc.id ";
	protected static final String UPDATE_SET_FAMILY = "tc.family_concept_id = tc.id ";
	protected static final String UPDATE_SET_GENUS = "tc.genus_concept_id = tc.id ";
	protected static final String UPDATE_SET_SPECIES = "tc.species_concept_id = tc.id ";
	protected static final String UPDATE_COPY_FROM_PARENT_KINGDOM = "tc.kingdom_concept_id = pc.kingdom_concept_id ";
	protected static final String UPDATE_COPY_FROM_PARENT_PHYLUM = ",tc.phylum_concept_id = pc.phylum_concept_id ";
	protected static final String UPDATE_COPY_FROM_PARENT_CLASS = ",tc.class_concept_id = pc.class_concept_id ";
	protected static final String UPDATE_COPY_FROM_PARENT_ORDER = ",tc.order_concept_id = pc.order_concept_id ";
	protected static final String UPDATE_COPY_FROM_PARENT_FAMILY = ",tc.family_concept_id = pc.family_concept_id ";
	protected static final String UPDATE_COPY_FROM_PARENT_GENUS = ",tc.genus_concept_id = pc.genus_concept_id ";
	protected static final String UPDATE_COPY_FROM_PARENT_SPECIES = ",tc.species_concept_id = pc.species_concept_id ";
	protected static final String UPDATE_COPY_FROM_PARENT_POSTFIX = "where tc.rank=? and ";	
	
	/**
	 * These are used for building some sql to copy from an accepted concept
	 */
	protected static final String UPDATE_COPY_FROM_ACCEPTED_PREFIX = "update taxon_concept tc left join relationship_assertion ra on ra.from_concept_id = tc.id and ra.relationship_type = 4 left join taxon_concept ac on ac.id = ra.to_concept_id set ";
	protected static final String UPDATE_COPY_FROM_ACCEPTED_KINGDOM = "tc.kingdom_concept_id = ac.kingdom_concept_id ";
	protected static final String UPDATE_COPY_FROM_ACCEPTED_PHYLUM = ",tc.phylum_concept_id = ac.phylum_concept_id ";
	protected static final String UPDATE_COPY_FROM_ACCEPTED_CLASS = ",tc.class_concept_id = ac.class_concept_id ";
	protected static final String UPDATE_COPY_FROM_ACCEPTED_ORDER = ",tc.order_concept_id = ac.order_concept_id ";
	protected static final String UPDATE_COPY_FROM_ACCEPTED_FAMILY = ",tc.family_concept_id = ac.family_concept_id ";
	protected static final String UPDATE_COPY_FROM_ACCEPTED_GENUS = ",tc.genus_concept_id = ac.genus_concept_id ";
	protected static final String UPDATE_COPY_FROM_ACCEPTED_SPECIES = ",tc.species_concept_id = ac.species_concept_id ";
	protected static final String UPDATE_COPY_FROM_ACCEPTED_POSTFIX = "where ac.rank>=? and ";	

	/**
	 * Reusable row mappers
	 */
	protected TaxonConceptRowMapper tcRowMapper = new TaxonConceptRowMapper();
	protected RankRowMapper rankRowMapper = new RankRowMapper();
	
	class RankRowMapper implements RowMapper {
		/**
		 * The factory
		 */
		public Integer mapRow(ResultSet rs, int rowNumber) throws SQLException {
			return new Integer(rs.getInt("rank"));
		}
	}	
	
	/**
	 * @see org.gbif.portal.dao.TaxonomyDenormaliserDAO#clearDenormalisedDataForProvider(long)
	 */
	public void clearDenormalisedDataForProvider(final long providerId) {
		updateSingleIdSql(TaxonomyDenormaliserDAOImpl.CLEAR_DENORMALISED_DATA, TaxonomyDenormaliserDAOImpl.PROVIDER_ID_CRITERIA_SQL, providerId);
	}

	/**
	 * @see org.gbif.portal.dao.TaxonomyDenormaliserDAO#clearDenormalisedDataForResource(long)
	 */
	public void clearDenormalisedDataForResource(final long resourceId) {
		updateSingleIdSql(TaxonomyDenormaliserDAOImpl.CLEAR_DENORMALISED_DATA, TaxonomyDenormaliserDAOImpl.RESOURCE_ID_CRITERIA_SQL, resourceId);
	}

	/**
	 * A utility to update by concatinating the 2 sql parts and setting the long id
	 * @param sqlPrefix The first section of SQL
	 * @param sqlPostfix The second section of SQL
	 * @param id To set
	 */
	protected void updateSingleIdSql(final String sqlPrefix, final String sqlPostfix, final long id) {
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(sqlPrefix + sqlPostfix);
						ps.setLong(1, id);
						return ps;
				}					
			}
		);
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getDistinctRanksForResource(long resourceId) {
		return (List<Integer>)
		 getJdbcTemplate()
		 .query(TaxonomyDenormaliserDAOImpl.GET_DISTINCT_RANKS_PREFIX
				 + TaxonomyDenormaliserDAOImpl.RESOURCE_ID_CRITERIA_SQL
				 + TaxonomyDenormaliserDAOImpl.GET_DISTINCT_RANKS_POSTFIX,
			new Object[]{resourceId},
			new RowMapperResultSetExtractor(rankRowMapper, 10));
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getDistinctRanksForProvider(long providerId) {
		return (List<Integer>)
		 getJdbcTemplate()
		 .query(TaxonomyDenormaliserDAOImpl.GET_DISTINCT_RANKS_PREFIX
				 + TaxonomyDenormaliserDAOImpl.PROVIDER_ID_CRITERIA_SQL
				 + TaxonomyDenormaliserDAOImpl.GET_DISTINCT_RANKS_POSTFIX,
			new Object[]{providerId},
			new RowMapperResultSetExtractor(rankRowMapper, 10));
	}

	public void copyParentDenormalisationForRankAndForProvider(final long providerId, final int rank) {
		StringBuffer sb = new StringBuffer(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_PARENT_PREFIX);
		if (rank==1000) sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_SET_KINGDOM);
		if (rank==2000) sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_SET_PHYLUM);
		if (rank==3000) sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_SET_CLASS);
		if (rank==4000) sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_SET_ORDER);
		if (rank==5000) sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_SET_FAMILY);
		if (rank==6000) sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_SET_GENUS);
		if (rank==7000) sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_SET_SPECIES);
		if (1000 < rank){
			if (rank%1000 == 0 && rank<=7000) sb.append(",");
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_PARENT_KINGDOM);
		}
		if (2000 < rank) {
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_PARENT_PHYLUM);
		}
		if (3000 < rank) {
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_PARENT_CLASS);
		}
		if (4000 < rank) {
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_PARENT_ORDER);
		}
		if (5000 < rank) {
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_PARENT_FAMILY);
		}
		if (6000 < rank) {
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_PARENT_GENUS);
		}
		if (7000 < rank) {
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_PARENT_SPECIES);
		}
		sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_PARENT_POSTFIX);
		sb.append(TaxonomyDenormaliserDAOImpl.PROVIDER_ID_CRITERIA_SQL);
		//logger.debug(sb.toString());
		final String sql = sb.toString();
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(sql);
						ps.setInt(1, rank);
						ps.setLong(2, providerId);
						
						return ps;
				}					
			}
		);
		
		sb = new StringBuffer(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_ACCEPTED_PREFIX);
		if (1000 <= rank){
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_ACCEPTED_KINGDOM);
		}
		if (2000 <= rank) {
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_ACCEPTED_PHYLUM);
		}
		if (3000 <= rank) {
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_ACCEPTED_CLASS);
		}
		if (4000 <= rank) {
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_ACCEPTED_ORDER);
		}
		if (5000 <= rank) {
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_ACCEPTED_FAMILY);
		}
		if (6000 <= rank) {
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_ACCEPTED_GENUS);
		}
		if (7000 <= rank) {
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_ACCEPTED_SPECIES);
		}
		sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_ACCEPTED_POSTFIX);
		sb.append(TaxonomyDenormaliserDAOImpl.PROVIDER_ID_CRITERIA_SQL);
		//logger.debug(sb.toString());
		final String sql2 = sb.toString();
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(sql2);
						ps.setInt(1, rank);
						ps.setLong(2, providerId);
						
						return ps;
				}					
			}
		);
}

	public void copyParentDenormalisationForRankAndForResource(final long resourceId, final int rank) {
		StringBuffer sb = new StringBuffer(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_PARENT_PREFIX);
		if (rank==1000) sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_SET_KINGDOM);
		if (rank==2000) sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_SET_PHYLUM);
		if (rank==3000) sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_SET_CLASS);
		if (rank==4000) sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_SET_ORDER);
		if (rank==5000) sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_SET_FAMILY);
		if (rank==6000) sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_SET_GENUS);
		if (rank==7000) sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_SET_SPECIES);
		if (1000 < rank){
			if (rank%1000 == 0 && rank<=7000) sb.append(",");
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_PARENT_KINGDOM);
		}
		if (2000 < rank) {
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_PARENT_PHYLUM);
		}
		if (3000 < rank) {
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_PARENT_CLASS);
		}
		if (4000 < rank) {
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_PARENT_ORDER);
		}
		if (5000 < rank) {
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_PARENT_FAMILY);
		}
		if (6000 < rank) {
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_PARENT_GENUS);
		}
		if (7000 < rank) {
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_PARENT_SPECIES);
		}
		sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_PARENT_POSTFIX);
		sb.append(TaxonomyDenormaliserDAOImpl.RESOURCE_ID_CRITERIA_SQL);
		//logger.debug(sb.toString());
		final String sql = sb.toString();
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(sql);
						ps.setInt(1, rank);
						ps.setLong(2, resourceId);
						
						return ps;
				}					
			}
		);
		
		sb = new StringBuffer(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_ACCEPTED_PREFIX);
		if (1000 <= rank){
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_ACCEPTED_KINGDOM);
		}
		if (2000 <= rank) {
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_ACCEPTED_PHYLUM);
		}
		if (3000 <= rank) {
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_ACCEPTED_CLASS);
		}
		if (4000 <= rank) {
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_ACCEPTED_ORDER);
		}
		if (5000 <= rank) {
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_ACCEPTED_FAMILY);
		}
		if (6000 <= rank) {
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_ACCEPTED_GENUS);
		}
		if (7000 <= rank) {
			sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_ACCEPTED_SPECIES);
		}
		sb.append(TaxonomyDenormaliserDAOImpl.UPDATE_COPY_FROM_ACCEPTED_POSTFIX);
		sb.append(TaxonomyDenormaliserDAOImpl.RESOURCE_ID_CRITERIA_SQL);
		//logger.debug(sb.toString());
		final String sql2 = sb.toString();
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(sql2);
						// We need to make sure that we handle species -> infraspecies and infraspecies -> species synonyms
						ps.setInt(1, (rank > 7000) ? 7000 : rank);
						ps.setLong(2, resourceId);
						
						return ps;
				}					
			}
		);
	}	
}
