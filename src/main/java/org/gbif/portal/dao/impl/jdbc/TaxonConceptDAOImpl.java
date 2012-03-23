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
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.gbif.portal.dao.TaxonConceptDAO;
import org.gbif.portal.dao.TaxonNameDAO;
import org.gbif.portal.dao.impl.jdbc.rowmapper.IdRowMapper;
import org.gbif.portal.dao.impl.jdbc.rowmapper.IntRowMapper;
import org.gbif.portal.dao.impl.jdbc.rowmapper.TaxonConceptLiteRowMapper;
import org.gbif.portal.dao.impl.jdbc.rowmapper.TaxonConceptRowMapper;
import org.gbif.portal.model.TaxonConcept;
import org.gbif.portal.model.TaxonConceptLite;
import org.gbif.portal.model.TaxonName;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * Pure JDBC implementation
 * @author trobertson
 */
public class TaxonConceptDAOImpl extends JdbcDaoSupport implements
		TaxonConceptDAO {

	/**
	 * Create SQL
	 */
	protected static final String CREATE_SQL =
		"insert into taxon_concept(rank,taxon_name_id,data_provider_id,data_resource_id,parent_concept_id,is_accepted,partner_concept_id,is_nub_concept,is_secondary," +
		"kingdom_concept_id,phylum_concept_id,class_concept_id,order_concept_id,family_concept_id,genus_concept_id,species_concept_id,priority," +
		"created,modified) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Update the parent
	 */
	protected static final String UPDATE_PARENT_SQL =
		"update taxon_concept set parent_concept_id=? where id=?";
	
	/**
	 * Update the parent
	 */
	protected static final String UPDATE_PARENT_WHERE_PARENT_SQL =
		"update taxon_concept set parent_concept_id=? where parent_concept_id=?";
	
	/**
	 * Update the parent
	 */
	protected static final String DELETE_SQL =
		"delete from taxon_concept where id=?";	
	
	/**
	 * Update the partner concept id
	 */
	protected static final String UPDATE_PARTNER_SQL =
		"update taxon_concept set partner_concept_id=? where id=?";
	
	/**
	 * Update the parent
	 */
	protected static final String UPDATE_ACCEPTED_SQL =
		"update taxon_concept set is_accepted=? where id=?";
	
	/**
	 * Update the parent and denormalised
	 */
	protected static final String UPDATE_PARENT_AND_DENORMALISED_SQL =
		"update taxon_concept set parent_concept_id=?," +
		"kingdom_concept_id=?,phylum_concept_id=?,class_concept_id=?,order_concept_id=?,family_concept_id=?,genus_concept_id=?,species_concept_id=?,priority=? " +
		"where id=?";
	
	/**
	 * Update ranks for unranked concepts with ranked parents
	 */
	protected static final String UPDATE_UNRANKED_CONCEPTS_SQL =
		"update taxon_concept tc left join taxon_concept p on p.id=tc.parent_concept_id " +
		"set tc.rank=p.rank+1 where tc.data_resource_id=? and tc.rank=0 and p.rank!=0";
	
	/**
	 * Update the parent via remote concepts NOTE - needs a generated list of ids at end
	 */
	protected static final String UPDATE_PARENT_BATCH_SQL =
		"update taxon_concept set parent_concept_id=? where id in ";
	/**
	 * The get by canonical and rank sql within a provider
	 */
	protected static final String QUERY_BY_CANONICAL_RANK_SQL = 
		"select tc.id as id " +
		"from taxon_concept tc inner join taxon_name tn on tn.id=tc.taxon_name_id " +
		"where tn.canonical=? and tc.rank=? and tc.data_resource_id=? " +
		"and tc.priority<" + DISAMBIGUATION_PRIORITY;
		
	/**
	 * The get by canonical author and rank sql (e.g. Business Unique) within a resource
	 */
	protected static final String QUERY_BY_CANONICAL_AUTHOR_RANK_SQL = 
		"select tc.id as id " +
		"from taxon_concept tc inner join taxon_name tn on tn.id=tc.taxon_name_id " +
		"where tn.canonical=? and tn.author=? and tc.rank=? and tc.data_resource_id=? " +
		"and tc.priority<" + DISAMBIGUATION_PRIORITY;
	
	/**
	 * The get by canonical and rank sql with no author (e.g. Business Unique) within a resource
	 */
	protected static final String QUERY_BY_CANONICAL_RANK_NOAUTHOR_SQL = 
		"select tc.id as id " +
		"from taxon_concept tc inner join taxon_name tn on tn.id=tc.taxon_name_id " +
		"where tn.canonical=? and tn.author is null and tc.rank=? and tc.data_resource_id=? " +
		"and tc.priority<" + DISAMBIGUATION_PRIORITY;
	
	/**
	 * The get by canonical author and rank sql (e.g. Business Unique) within a provider
	 */
	protected static final String QUERY_BY_CANONICAL_AUTHOR_RANK_PROVIDER_SQL = 
		"select tc.id as id " +
		"from taxon_concept tc inner join taxon_name tn on tn.id=tc.taxon_name_id " +
		"where tn.canonical=? and tn.author=? and tc.rank=? and tc.data_provider_id=? " +
		"and tc.priority<" + DISAMBIGUATION_PRIORITY;
	
	
	/**
	 * The get by canonical author and rank sql (e.g. Business Unique) within a provider
	 */
	protected static final String QUERY_BY_CANONICAL_RESOURCE_SQL = 
		"select tc.id,tc.parent_concept_id,tc.rank,tc.is_accepted,tc.data_provider_id,tc.data_resource_id,tc.partner_concept_id,tc.is_nub_concept,tc.is_secondary," +
		"tc.kingdom_concept_id,tc.phylum_concept_id,tc.class_concept_id,tc.order_concept_id,tc.family_concept_id,tc.genus_concept_id,tc.species_concept_id," +
		"tn.id,tn.canonical,tn.supra_generic,tn.generic,tn.infrageneric,tn.specific_epithet,tc.priority," +
		"tn.infraspecific,tn.infraspecific_marker,tn.is_hybrid,tn.rank,tn.author " +
		"from taxon_concept tc inner join taxon_name tn on tn.id=tc.taxon_name_id " +
		"where tn.canonical=? and tc.data_resource_id=? " +
		"and tc.priority<" + DISAMBIGUATION_PRIORITY;
	
	
	
	
	/**
	 * The get by canonical and rank sql with no author (e.g. Business Unique) within a provider
	 */
	protected static final String QUERY_BY_CANONICAL_RANK_NOAUTHOR_PROVIDER_SQL = 
		"select tc.id as id " +
		"from taxon_concept tc inner join taxon_name tn on tn.id=tc.taxon_name_id " +
		"where tn.canonical=? and tn.author is null and tc.rank=? and tc.data_provider_id=? " +
		"and tc.priority<" + DISAMBIGUATION_PRIORITY;
	
	/**
	 * The get by canonical and rank sql within a provider
	 */
	protected static final String QUERY_BY_CANONICAL_RANK_PROVIDER_SQL = 
		"select tc.id as id " +
		"from taxon_concept tc inner join taxon_name tn on tn.id=tc.taxon_name_id " +
		"where tn.canonical=? and tc.rank=? and tc.data_provider_id=? " +
		"and tc.priority<" + DISAMBIGUATION_PRIORITY;
	
	/**
	 * Gets the distinct ranks within a resource
	 */
	protected static final String QUERY_FOR_RANKS_WITHIN_RESOURCE = 
		"select distinct rank from taxon_concept tc where tc.data_resource_id=? order by rank";
	
	/**
	 * Gets the distinct ranks within a resource that are unpartnered
	 */
	protected static final String QUERY_FOR_UNPARTNERED_RANKS_WITHIN_RESOURCE = 
		"select distinct rank from taxon_concept tc where tc.data_resource_id=? and partner_concept_id is null order by rank";
	
	/**
	 * The get by ID for a concept lite
	 */
	protected static final String QUERY_FOR_CONCEPT_LITE = 
		"select tc.id,tc.parent_concept_id,tc.rank,tc.is_accepted,tc.partner_concept_id,tc.data_provider_id,tc.data_resource_id,tc.is_nub_concept,tc.is_secondary,tc.priority," +
		"tn.id,tn.canonical,tn.supra_generic,tn.generic,tn.infrageneric,tn.specific_epithet," +		
		"tn.infraspecific,tn.infraspecific_marker,tn.is_hybrid,tn.rank,tn.author " +
		"from taxon_concept tc inner join taxon_name tn on tn.id=tc.taxon_name_id " +
		"where tc.id=?";
	
	/**
	 * The get by provider id  and remote id for a concept lite
	 */
	protected static final String QUERY_BY_PROVIDER_ID_AND_REMOTE_ID_SQL = 
		"select tc.id,tc.parent_concept_id,tc.rank,tc.is_accepted,tc.partner_concept_id,tc.data_provider_id,tc.data_resource_id,tc.is_nub_concept,tc.is_secondary,tc.priority," +
		"tn.id,tn.canonical,tn.supra_generic,tn.generic,tn.infrageneric,tn.specific_epithet," +		
		"tn.infraspecific,tn.infraspecific_marker,tn.is_hybrid,tn.rank,tn.author " +
		"from taxon_concept tc inner join taxon_name tn on tn.id=tc.taxon_name_id " +
		"left join remote_concept rc on rc.taxon_concept_id=tc.id " +
		"where tc.data_provider_id=? and rc.id_type=1 and rc.remote_id=?";
	
	/**
	 * The get by name and resource for a disambiguation concept
	 */
	protected static final String QUERY_FOR_DISAMBIGUATION_CONCEPT = 
		"select tc.id,tc.parent_concept_id,tc.rank,tc.is_accepted,tc.data_provider_id,tc.data_resource_id,tc.partner_concept_id,tc.is_nub_concept,tc.is_secondary," +
		"tc.kingdom_concept_id,tc.phylum_concept_id,tc.class_concept_id,tc.order_concept_id,tc.family_concept_id,tc.genus_concept_id,tc.species_concept_id," +
		"tn.id,tn.canonical,tn.supra_generic,tn.generic,tn.infrageneric,tn.specific_epithet,tc.priority," +
		"tn.infraspecific,tn.infraspecific_marker,tn.is_hybrid,tn.rank,tn.author " +
		"from taxon_concept tc inner join taxon_name tn on tn.id=tc.taxon_name_id " +
		"where tn.canonical=? and tc.data_resource_id=? and tc.priority=" + DISAMBIGUATION_PRIORITY;
	
	/**
	 * The get by rank for the resource
	 */
	protected static final String QUERY_BY_RANK_RESOURCE_SQL = 
		"select tc.id as id " +
		"from taxon_concept tc " +
		"where tc.rank=? and tc.data_resource_id=? and tc.id>? and tc.priority<" + DISAMBIGUATION_PRIORITY + " " +
		"order by tc.id limit ?";
	
	/**
	 * The get by rank for the resource
	 */
	protected static final String QUERY_BY_RANK_GREATER_THAN_RESOURCE_SQL = 
		"select tc.id as id " +
		"from taxon_concept tc " +
		"where tc.rank>=? and tc.data_resource_id=? and tc.id>? and tc.priority<" + DISAMBIGUATION_PRIORITY + " " +
		" order by tc.id limit ?";

	/**
	 * The get by rank for the resource
	 */
	protected static final String QUERY_BY_RANK_RESOURCE_ACCEPTED_SQL_NO_PARTNER = 
		"select tc.id as id " +
		"from taxon_concept tc " +
		"where tc.rank=? and tc.data_resource_id=? and tc.id>? and tc.is_accepted=? and tc.priority<" + DISAMBIGUATION_PRIORITY + " and tc.partner_concept_id is null " +
		"order by tc.id limit ?";
	
	/**
	 * The get by rank for the resource
	 */
	protected static final String QUERY_BY_RANK_RESOURCE_ACCEPTED_SQL = 
		"select tc.id as id " +
		"from taxon_concept tc " +
		"where tc.rank=? and tc.data_resource_id=? and tc.id>? and tc.is_accepted=? and tc.priority<" + DISAMBIGUATION_PRIORITY + " " +
		"order by tc.id limit ?";
	
	/**
	 * The get by rank for the resource
	 */
	protected static final String QUERY_BY_RANK_GREATER_THAN_RESOURCE_ACCEPTED_SQL = 
		"select tc.id as id " +
		"from taxon_concept tc " +
		"where tc.rank>=? and tc.data_resource_id=? and tc.id>? and tc.is_accepted=? and tc.priority<" + DISAMBIGUATION_PRIORITY + " " +
		"order by tc.id limit ?";

	/**
	 * The get by rank for the resource with no partner
	 */
	protected static final String QUERY_BY_RANK_GREATER_THAN_RESOURCE_ACCEPTED_SQL_NO_PARTNER = 
		"select tc.id as id " +
		"from taxon_concept tc " +
		"where tc.rank>=? and tc.data_resource_id=? and tc.id>? and tc.is_accepted=? and tc.priority<" + DISAMBIGUATION_PRIORITY + " and tc.partner_concept_id is null " +
		"order by tc.id limit ?";

	/**
	 * Get by ID for the full concept
	 */
	protected static final String QUERY_FOR_CONCEPT = 
		"select tc.id,tc.parent_concept_id,tc.rank,tc.is_accepted,tc.data_provider_id,tc.data_resource_id,tc.partner_concept_id,tc.is_nub_concept,tc.is_secondary," +
		"tc.kingdom_concept_id,tc.phylum_concept_id,tc.class_concept_id,tc.order_concept_id,tc.family_concept_id,tc.genus_concept_id,tc.species_concept_id," +
		"tn.id,tn.canonical,tn.supra_generic,tn.generic,tn.infrageneric,tn.specific_epithet,tc.priority," +
		"tn.infraspecific,tn.infraspecific_marker,tn.is_hybrid,tn.rank,tn.author " +
		"from taxon_concept tc inner join taxon_name tn on tn.id=tc.taxon_name_id " +
		"where tc.id=?";
	
	/**
	 * Get a TC with parent
	 */
	protected static final String QUERY_FOR_CONCEPT_WITH_PARENT = 
		"select tc.id,tc.parent_concept_id,tc.rank,tc.is_accepted,tc.data_provider_id,tc.data_resource_id,tc.partner_concept_id,tc.is_nub_concept,tc.is_secondary," +
		"tc.kingdom_concept_id,tc.phylum_concept_id,tc.class_concept_id,tc.order_concept_id,tc.family_concept_id,tc.genus_concept_id,tc.species_concept_id,priority," +
		"tn.id,tn.canonical,tn.supra_generic,tn.generic,tn.infrageneric,tn.specific_epithet," +
		"tn.infraspecific,tn.infraspecific_marker,tn.is_hybrid,tn.rank,tn.author " +
		"from taxon_concept tc inner join taxon_name tn on tn.id=tc.taxon_name_id " +
		"where tn.canonical=:canonical and (tn.author is null or (tn.author is not null and tn.author=:author)) and tc.rank=:rank and tc.data_resource_id=:data_resource_id and tc.parent_concept_id in(:parent_concept_ids)";
	
	/**
	 * Get a TC with NO parent
	 */
	protected static final String QUERY_FOR_CONCEPT_WITHOUT_PARENT = 
		"select tc.id,tc.parent_concept_id,tc.rank,tc.is_accepted,tc.data_provider_id,tc.data_resource_id,tc.partner_concept_id,tc.is_nub_concept,tc.is_secondary," +
		"tc.kingdom_concept_id,tc.phylum_concept_id,tc.class_concept_id,tc.order_concept_id,tc.family_concept_id,tc.genus_concept_id,tc.species_concept_id,tc.priority," +
		"tn.id,tn.canonical,tn.supra_generic,tn.generic,tn.infrageneric,tn.specific_epithet," +
		"tn.infraspecific,tn.infraspecific_marker,tn.is_hybrid,tn.rank,tn.author " +
		"from taxon_concept tc inner join taxon_name tn on tn.id=tc.taxon_name_id " +
		"where tn.canonical=? and (tn.author is null or (tn.author is not null and tn.author=?)) and tc.rank=? and tc.data_resource_id=?";
	
	/**
	 * Get a TC with parent
	 */
	protected static final String QUERY_FOR_CONCEPT_WITH_PARENT_NO_AUTHOR = 
		"select tc.id,tc.parent_concept_id,tc.rank,tc.is_accepted,tc.data_provider_id,tc.data_resource_id,tc.partner_concept_id,tc.is_nub_concept,tc.is_secondary," +
		"tc.kingdom_concept_id,tc.phylum_concept_id,tc.class_concept_id,tc.order_concept_id,tc.family_concept_id,tc.genus_concept_id,tc.species_concept_id,tc.priority," +
		"tn.id,tn.canonical,tn.supra_generic,tn.generic,tn.infrageneric,tn.specific_epithet," +
		"tn.infraspecific,tn.infraspecific_marker,tn.is_hybrid,tn.rank,tn.author " +
		"from taxon_concept tc inner join taxon_name tn on tn.id=tc.taxon_name_id " +
		"where tn.canonical=:canonical and tc.rank=:rank and tc.data_resource_id=:data_resource_id and tc.parent_concept_id in(:parent_concept_ids)";
	
	/**
	 * Get a TC with NO parent
	 */
	protected static final String QUERY_FOR_CONCEPT_WITHOUT_PARENT_NO_AUTHOR = 
		"select tc.id,tc.parent_concept_id,tc.rank,tc.is_accepted,tc.data_provider_id,tc.data_resource_id,tc.partner_concept_id,tc.is_nub_concept,tc.is_secondary," +
		"tc.kingdom_concept_id,tc.phylum_concept_id,tc.class_concept_id,tc.order_concept_id,tc.family_concept_id,tc.genus_concept_id,tc.species_concept_id,tc.priority," +
		"tn.id,tn.canonical,tn.supra_generic,tn.generic,tn.infrageneric,tn.specific_epithet," +
		"tn.infraspecific,tn.infraspecific_marker,tn.is_hybrid,tn.rank,tn.author " +
		"from taxon_concept tc inner join taxon_name tn on tn.id=tc.taxon_name_id " +
		"where tn.canonical=? and tc.rank=? and tc.data_resource_id=?";
	
	/**
	 * Update the kingdom
	 */
	protected static final String UPDATE_KINGDOM_SQL =
		"update taxon_concept set kingdom_concept_id=? where id=?";
	
	/**
	 * Update the phylum
	 */
	protected static final String UPDATE_PHYLUM_SQL =
		"update taxon_concept set phylum_concept_id=? where id=?";
	
	/**
	 * Update the class
	 */
	protected static final String UPDATE_CLASS_SQL =
		"update taxon_concept set class_concept_id=? where id=?";
	
	/**
	 * Update the order
	 */
	protected static final String UPDATE_ORDER_SQL =
		"update taxon_concept set order_concept_id=? where id=?";
	
	/**
	 * Update the family
	 */
	protected static final String UPDATE_FAMILY_SQL =
		"update taxon_concept set family_concept_id=? where id=?";
	
	/**
	 * Update the genus
	 */
	protected static final String UPDATE_GENUS_SQL =
		"update taxon_concept set genus_concept_id=? where id=?";
	
	/**
	 * Update the species
	 */
	protected static final String UPDATE_SPECIES_SQL =
		"update taxon_concept set species_concept_id=? where id=?";
	
	/**
	 * Update the rank
	 */
	protected static final String UPDATE_RANK_SQL =
		"update taxon_concept set rank=? where id=?";
	
	/**
	 * Mark all concepts primary
	 */
	protected static final String MARK_CONCEPTS_SECONDARY_SQL =
		"update taxon_concept tc set tc.is_secondary=1 where tc.data_resource_id=?";
	
	/**
	 * Mark secondary concepts
	 */
	protected static final String MARK_CONCEPTS_WITH_OCCURRENCES_PRIMARY_SQL =
		"update taxon_concept tc inner join occurrence_record oc on oc.taxon_concept_id=tc.id set tc.is_secondary=0 where tc.data_resource_id=?";
		 
	
	/**
	 * Reusable row mappers
	 */
	protected IdRowMapper idRowMapper = new IdRowMapper();
	protected TaxonConceptLiteRowMapper tcLiteRowMapper = new TaxonConceptLiteRowMapper();
	protected TaxonConceptRowMapper tcRowMapper = new TaxonConceptRowMapper();
	protected IntRowMapper intRowMapper = new IntRowMapper();
	
	/**
	 * DAOs
	 */
	protected TaxonNameDAO taxonNameDAO;
	
	/**
	 * Could be slightly more efficient, but let's see...
	 * @see org.gbif.portal.dao.TaxonConceptDAO#getClassificationsOf(java.lang.String, java.lang.String, int, long)
	 */
	@SuppressWarnings("unchecked")
	public List<List<TaxonName>> getClassificationsOf(String canonical, String author, int rank, long dataResourceId) {
		List<Long> ids = null;
		if (author == null) {
			ids = (List<Long>) getJdbcTemplate()
			.query(TaxonConceptDAOImpl.QUERY_BY_CANONICAL_RANK_NOAUTHOR_SQL,
				new Object[]{canonical, rank, dataResourceId},
				new RowMapperResultSetExtractor(idRowMapper, 3));
		} else {
			ids = (List<Long>) getJdbcTemplate()
			.query(TaxonConceptDAOImpl.QUERY_BY_CANONICAL_AUTHOR_RANK_SQL,
				new Object[]{canonical, author, rank, dataResourceId},
				new RowMapperResultSetExtractor(idRowMapper, 3));
		}
		logger.debug("Found " + ids.size() + " concepts for canonical[" + canonical + "], author[" + author + 
					"], rank[" + rank + "], dataResourceId[" + dataResourceId + "]");
		
		List<List<TaxonName>> results = new LinkedList<List<TaxonName>>();
		// build the classifications
		for (Long id : ids) {
			results.add(getClassification(id));
		}
		return results;
	}
	
	/**
	 * Could be slightly more efficient, but let's see...
	 * @see org.gbif.portal.dao.TaxonConceptDAO#getClassificationsWithinProviderOf(java.lang.String, java.lang.String, int, long)
	 */
	@SuppressWarnings("unchecked")
	public List<List<TaxonConceptLite>> getClassificationsWithinProviderOf(String canonical, String author, int rank, long dataProviderId) {
		List<Long> ids = null;
		if (author == null) {
			ids = (List<Long>) getJdbcTemplate()
			.query(TaxonConceptDAOImpl.QUERY_BY_CANONICAL_RANK_NOAUTHOR_PROVIDER_SQL,
				new Object[]{canonical, rank, dataProviderId},
				new RowMapperResultSetExtractor(idRowMapper, 3));
		} else {
			ids = (List<Long>) getJdbcTemplate()
			.query(TaxonConceptDAOImpl.QUERY_BY_CANONICAL_AUTHOR_RANK_PROVIDER_SQL,
				new Object[]{canonical, author, rank, dataProviderId},
				new RowMapperResultSetExtractor(idRowMapper, 3));
		}
		logger.debug("Found " + ids.size() + " concepts for canonical[" + canonical + "], author[" + author + 
					"], rank[" + rank + "], dataProviderId[" + dataProviderId + "]");
		
		List<List<TaxonConceptLite>> results = new LinkedList<List<TaxonConceptLite>>();
		// build the classifications
		for (Long id : ids) {
			results.add(getClassificationConcepts(id));
		}
		return results;
	}
	
	
	/**
	 * Could be slightly more efficient, but let's see...
	 * @see org.gbif.portal.dao.TaxonConceptDAO#getClassificationsWithinProviderOf(java.lang.String, java.lang.String, int, long)
	 */
	@SuppressWarnings("unchecked")
	public List<List<TaxonConceptLite>> getClassificationsWithinProviderOf(String canonical, int rank, long dataProviderId) {
		List<Long> ids = (List<Long>) getJdbcTemplate()
			.query(TaxonConceptDAOImpl.QUERY_BY_CANONICAL_RANK_PROVIDER_SQL,
				new Object[]{canonical, rank, dataProviderId},
				new RowMapperResultSetExtractor(idRowMapper, 3));
		
		logger.debug("Found " + ids.size() + " concepts for canonical[" + canonical + "], " +
				"rank[" + rank + "], dataProviderId[" + dataProviderId + "]");
		
		List<List<TaxonConceptLite>> results = new LinkedList<List<TaxonConceptLite>>();
		// build the classifications
		for (Long id : ids) {
			results.add(getClassificationConcepts(id));
		}
		return results;
	}
	
	/**
	 * Could be slightly more efficient, but let's see...
	 * @see org.gbif.portal.dao.TaxonConceptDAO#getClassificationsOf(java.lang.String, java.lang.String, int, long)
	 */
	@SuppressWarnings("unchecked")
	public List<List<TaxonConceptLite>> getClassificationsOf(String canonical, int rank, long dataResourceId) {
		List<Long> ids = (List<Long>) getJdbcTemplate()
			.query(TaxonConceptDAOImpl.QUERY_BY_CANONICAL_RANK_SQL,
				new Object[]{canonical, rank, dataResourceId},
				new RowMapperResultSetExtractor(idRowMapper, 3));
		
		logger.debug("Found " + ids.size() + " concepts for canonical[" + canonical + "], " +
				"rank[" + rank + "], dataResourceId[" + dataResourceId + "]");
		
		List<List<TaxonConceptLite>> results = new LinkedList<List<TaxonConceptLite>>();
		// build the classifications
		for (Long id : ids) {
			results.add(getClassificationConcepts(id));
		}
		return results;
	}
	
	/**
	 * @see org.gbif.portal.dao.TaxonConceptDAO#getClassification(long)
	 * @deprecated Use the TaxonConceptLite version
	 */
	public List<TaxonName> getClassification(final long conceptId) {
		List<TaxonName> classification = new LinkedList<TaxonName>();
		
		TaxonConceptLite tcl = getTaxonConceptLite(conceptId);
		while (tcl != null) {
			classification.add(0, tcl.getTaxonName());
			tcl = getTaxonConceptLite(tcl.getParentId());
		}		
		/*
		if (logger.isDebugEnabled()) {
			StringBuffer sb = new StringBuffer("Classification:");
			for (TaxonName tn : classification) {
				sb.append("\n - " + tn.toString());
			}
			logger.debug(sb.toString());
		}
		*/
		return classification;
	}
	
	/**
	 * @see org.gbif.portal.dao.TaxonConceptDAO#getClassificationConcepts(long)
	 */
	public List<TaxonConceptLite> getClassificationConcepts(final long conceptId) {
		List<TaxonConceptLite> classification = new LinkedList<TaxonConceptLite>();
		
		TaxonConceptLite tcl = getTaxonConceptLite(conceptId);
		while (tcl != null) {
			classification.add(0, tcl);
			tcl = getTaxonConceptLite(tcl.getParentId());
		}		
		/*
		if (logger.isDebugEnabled()) {
			StringBuffer sb = new StringBuffer("Classification:");
			for (TaxonConceptLite tc : classification) {
				sb.append("\n - " + tc.getTaxonName().toString());
			}
			logger.debug(sb.toString());
		}
		*/
		
		return classification;
	}
	
	/**
	 * @see org.gbif.portal.dao.TaxonConceptDAO#getTaxonConceptLite(long)
	 */
	@SuppressWarnings("unchecked")
	public TaxonConceptLite getTaxonConceptLite(final long conceptId) {
		List<TaxonConceptLite> results = (List<TaxonConceptLite>)
		 getJdbcTemplate()
		 .query(TaxonConceptDAOImpl.QUERY_FOR_CONCEPT_LITE,
			new Object[]{conceptId},
			new RowMapperResultSetExtractor(tcLiteRowMapper, 1));
		if (results.size()>0) {
			return results.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * @see org.gbif.portal.dao.TaxonConceptDAO#getTaxonConcept(long)
	 */
	@SuppressWarnings("unchecked")
	public TaxonConcept getTaxonConcept(final long conceptId) {
		List<TaxonConcept> results = (List<TaxonConcept>)
		 getJdbcTemplate()
		 .query(TaxonConceptDAOImpl.QUERY_FOR_CONCEPT,
			new Object[]{conceptId},
			new RowMapperResultSetExtractor(tcRowMapper, 1));
		if (results.size()>0) {
			return results.get(0);
		} else {
			return null;
		}
	}
	
	
	/**
	 * @see org.gbif.portal.dao.TaxonConceptDAO#remove(long)
	 */
	public void remove(final long conceptId) {
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(TaxonConceptDAOImpl.DELETE_SQL);
						ps.setObject(1,conceptId);
						return ps;
					}
				});		
	}
	
	/**
	 * @see org.gbif.portal.dao.TaxonConceptDAO#create(org.gbif.portal.model.TaxonConcept)
	 */
	public long create(final TaxonConcept concept) {
		Long taxonNameId = concept.getTaxonName().getId(); 
		if (taxonNameId == null) {
			// see if there is a name
			TaxonName persisted = taxonNameDAO.getUnique(concept.getTaxonName().getCanonical(), concept.getTaxonName().getAuthor(), concept.getRank());
			if (persisted == null) {
				taxonNameId = taxonNameDAO.create(concept.getTaxonName());
			} else {
				concept.setTaxonName(persisted);
				taxonNameId = persisted.getId();
			}			
		}
		final long taxonNameIdFinal = taxonNameId;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(
			new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					PreparedStatement ps = conn.prepareStatement(TaxonConceptDAOImpl.CREATE_SQL);
					ps.setInt(1,concept.getRank());
					ps.setLong(2,taxonNameIdFinal);
					ps.setLong(3,concept.getDataProviderId());
					ps.setLong(4,concept.getDataResourceId());
					ps.setObject(5,concept.getParentId());
					ps.setBoolean(6,concept.isAccepted());
					ps.setObject(7,concept.getPartnerConceptId());
					ps.setBoolean(8,concept.isNubConcept());
					ps.setBoolean(9,concept.isSecondary());
					ps.setObject(10,concept.getKingdomConceptId());
					ps.setObject(11,concept.getPhylumConceptId());
					ps.setObject(12,concept.getClassConceptId());
					ps.setObject(13,concept.getOrderConceptId());
					ps.setObject(14,concept.getFamilyConceptId());
					ps.setObject(15,concept.getGenusConceptId());
					ps.setObject(16,concept.getSpeciesConceptId());
					ps.setInt(17,concept.getPriority());
					long time = System.currentTimeMillis();
					ps.setTimestamp(18,new Timestamp(time));
					ps.setTimestamp(19,new Timestamp(time));
					return ps;
				}					
			},
			keyHolder
		);
		concept.setId(keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}

	/**
	 * @return Returns the taxonNameDAO.
	 */
	public TaxonNameDAO getTaxonNameDAO() {
		return taxonNameDAO;
	}

	/**
	 * @param taxonNameDAO The taxonNameDAO to set.
	 */
	public void setTaxonNameDAO(TaxonNameDAO taxonNameDAO) {
		this.taxonNameDAO = taxonNameDAO;
	}

	/**
	 * @see org.gbif.portal.dao.TaxonConceptDAO#getTaxonConcept(java.lang.String, java.lang.String, int, long java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public TaxonConcept getTaxonConcept(final String canonical, final String author, final int rank, final long dataResourceId, final List<Long> parentIds) {
		
		List<TaxonConcept> results = null;
		if (parentIds!=null && parentIds.size()>0) {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("canonical", canonical);
			parameters.put("author", author);
			parameters.put("rank", rank);
			parameters.put("data_resource_id", dataResourceId);
			parameters.put("parent_concept_ids", parentIds);
			// todo, this should be spring-alised as it i thread safe to do so...
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(this.getDataSource());
			results = (List<TaxonConcept>) template.query(
					TaxonConceptDAOImpl.QUERY_FOR_CONCEPT_WITH_PARENT,
					parameters, tcRowMapper);
			 
		} else {
			results = (List<TaxonConcept>)
			 getJdbcTemplate()
			 .query(TaxonConceptDAOImpl.QUERY_FOR_CONCEPT_WITHOUT_PARENT,
				new Object[]{canonical, author, rank, dataResourceId},
				new RowMapperResultSetExtractor(tcRowMapper, 1));
		}
		if (results!=null && results.size()>0) {
			return results.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * @see org.gbif.portal.dao.TaxonConceptDAO#getTaxonConcept(java.lang.String, int, long java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public TaxonConcept getTaxonConcept(final String canonical, final int rank, final long dataResourceId, final List<Long> parentIds) {
		
		List<TaxonConcept> results = null;
		if (parentIds!=null && parentIds.size()>0) {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("canonical", canonical);
			parameters.put("rank", rank);
			parameters.put("data_resource_id", dataResourceId);
			parameters.put("parent_concept_ids", parentIds);
			// todo, this should be spring-alised as it i thread safe to do so...
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(this.getDataSource());
			results = (List<TaxonConcept>) template.query(
					TaxonConceptDAOImpl.QUERY_FOR_CONCEPT_WITH_PARENT_NO_AUTHOR,
					parameters, tcRowMapper);
			 
		} else {
			results = (List<TaxonConcept>)
			 getJdbcTemplate()
			 .query(TaxonConceptDAOImpl.QUERY_FOR_CONCEPT_WITHOUT_PARENT_NO_AUTHOR,
				new Object[]{canonical, rank, dataResourceId},
				new RowMapperResultSetExtractor(tcRowMapper, 1));
		}
		if (results!=null && results.size()>0) {
			return results.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * @see org.gbif.portal.dao.TaxonConceptDAO#updateParent(long, long)
	 */
	public void updateParent(final long targetConcept, final long newParentId) {
		logger.debug("Changing parent of " + targetConcept + " to " + newParentId);
		if (targetConcept == newParentId) {
			logger.warn("Ignoring setting parent as a concept cannot reference itself as a parent");
		} else {
			getJdbcTemplate().update(
					new PreparedStatementCreator() {
						public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
							PreparedStatement ps = conn.prepareStatement(TaxonConceptDAOImpl.UPDATE_PARENT_SQL);
							ps.setObject(1,newParentId);
							ps.setLong(2,targetConcept);
							return ps;
						}
					});
		}
	}
	
	/**
	 * @see org.gbif.portal.dao.TaxonConceptDAO#updateParent(long, long)
	 */
	public void updateParentWhereParentId(final long oldParentId, final long newParentId) {
		logger.debug("Changing parent of " + oldParentId + " to " + newParentId);
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(TaxonConceptDAOImpl.UPDATE_PARENT_WHERE_PARENT_SQL);
						ps.setLong(1,newParentId);
						ps.setLong(2,oldParentId);
						return ps;
					}
				});
	}
	
	/**
	 * @see org.gbif.portal.dao.TaxonConceptDAO#updatePartnerConcept(long, long)
	 */
	public void updatePartnerConcept(final long targetConcept, final long newNubConceptId) {
		logger.debug("Changing nub of " + targetConcept + " to " + newNubConceptId);
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(TaxonConceptDAOImpl.UPDATE_PARTNER_SQL);
						ps.setObject(1,newNubConceptId);
						ps.setLong(2,targetConcept);
						return ps;
					}
				});
	}

	/**
	 * @see org.gbif.portal.dao.TaxonConceptDAO#updateKingdomConcept(java.lang.Long, long)
	 */
	public void updateKingdomConcept(final Long targetConcept, final long id) {
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(TaxonConceptDAOImpl.UPDATE_KINGDOM_SQL);
						ps.setObject(1,id);
						ps.setLong(2,targetConcept);
						return ps;
					}
				});
	}

	/**
	 * @see org.gbif.portal.dao.TaxonConceptDAO#updatePhylumConcept(java.lang.Long, long)
	 */
	public void updatePhylumConcept(final Long targetConcept, final long id) {
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(TaxonConceptDAOImpl.UPDATE_PHYLUM_SQL);
						ps.setObject(1,id);
						ps.setLong(2,targetConcept);
						return ps;
					}
				});
	}

	/**
	 * @see org.gbif.portal.dao.TaxonConceptDAO#updateClassConcept(java.lang.Long, long)
	 */
	public void updateClassConcept(final Long targetConcept, final long id) {
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(TaxonConceptDAOImpl.UPDATE_CLASS_SQL);
						ps.setObject(1,id);
						ps.setLong(2,targetConcept);
						return ps;
					}
				});
	}

	/**
	 * @see org.gbif.portal.dao.TaxonConceptDAO#updateOrderConcept(java.lang.Long, long)
	 */
	public void updateOrderConcept(final Long targetConcept, final long id) {
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(TaxonConceptDAOImpl.UPDATE_ORDER_SQL);
						ps.setObject(1,id);
						ps.setLong(2,targetConcept);
						return ps;
					}
				});
	}

	/**
	 * @see org.gbif.portal.dao.TaxonConceptDAO#updateFamilyConcept(java.lang.Long, long)
	 */
	public void updateFamilyConcept(final Long targetConcept, final long id) {
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(TaxonConceptDAOImpl.UPDATE_FAMILY_SQL);
						ps.setObject(1,id);
						ps.setLong(2,targetConcept);
						return ps;
					}
				});
	}

	/**
	 * @see org.gbif.portal.dao.TaxonConceptDAO#updateGenusConcept(java.lang.Long, long)
	 */
	public void updateGenusConcept(final Long targetConcept, final long id) {
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(TaxonConceptDAOImpl.UPDATE_GENUS_SQL);
						ps.setObject(1,id);
						ps.setLong(2,targetConcept);
						return ps;
					}
				});
	}
	
	/**
	 * @see org.gbif.portal.dao.TaxonConceptDAO#updateSpeciesConcept(java.lang.Long, long)
	 */
	public void updateSpeciesConcept(final Long targetConcept, final long id) {
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(TaxonConceptDAOImpl.UPDATE_SPECIES_SQL);
						ps.setObject(1,id);
						ps.setLong(2,targetConcept);
						return ps;
					}
				});
	}
	
	/* (non-Javadoc)
	 * @see org.gbif.portal.dao.TaxonConceptDAO#updateRank(java.lang.Long, long)
	 */
	public void updateRank(final Long targetConcept, final long rank) {
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(TaxonConceptDAOImpl.UPDATE_RANK_SQL);
						ps.setObject(1,rank);
						ps.setLong(2,targetConcept);
						return ps;
					}
				});
	}

	/**
	 * @see org.gbif.portal.dao.TaxonConceptDAO#updateParentAndDenormalised(long, long, java.lang.Long, java.lang.Long, java.lang.Long, java.lang.Long, java.lang.Long, java.lang.Long, java.lang.Long)
	 */
	public void updateParentAndDenormalised(final long targetConcept, final long newParentId, final Long kingdomConceptId, 
			final Long phylumConceptId, final Long classConceptId, final Long orderConceptId, 
			final Long familyConceptId, final Long genusConceptId, final Long speciesConceptId) {
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(TaxonConceptDAOImpl.UPDATE_PARENT_AND_DENORMALISED_SQL);
						ps.setObject(1,newParentId);
						ps.setObject(2,kingdomConceptId);
						ps.setObject(3,phylumConceptId);
						ps.setObject(4,classConceptId);
						ps.setObject(5,orderConceptId);
						ps.setObject(6,familyConceptId);
						ps.setObject(7,genusConceptId);
						ps.setObject(8,speciesConceptId);
						ps.setLong(9,targetConcept);
						return ps;
					}
				});
	}
	
	/**
	 * @see org.gbif.portal.dao.TaxonConceptDAO#getClassificationsOf(int, long, boolean, int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<List<TaxonConceptLite>> getClassificationsOf(int rank, long dataResourceId, boolean includeLowerTaxa, long minimumId, int maxResults) {
		List<Long> ids = null;
		if (includeLowerTaxa) {
			ids = (List<Long>) getJdbcTemplate()
			.query(TaxonConceptDAOImpl.QUERY_BY_RANK_GREATER_THAN_RESOURCE_SQL,
				new Object[]{rank, dataResourceId, minimumId, maxResults},
				new RowMapperResultSetExtractor(idRowMapper, 3));
		} else {
			ids = (List<Long>) getJdbcTemplate()
			.query(TaxonConceptDAOImpl.QUERY_BY_RANK_RESOURCE_SQL,
				new Object[]{rank, dataResourceId, minimumId, maxResults},
				new RowMapperResultSetExtractor(idRowMapper, 3));
		}
		logger.debug("Found " + ids.size() + " concepts for rank[" + rank + "], dataResourceId[" + dataResourceId + "], minimumId [" + 
				minimumId + "], maxResults [" + maxResults+ "]");
		
		List<List<TaxonConceptLite>> results = new LinkedList<List<TaxonConceptLite>>();
		// build the classifications
		for (Long id : ids) {
			results.add(getClassificationConcepts(id));
		}
		return results;
	}
	
	/**
	 * @see org.gbif.portal.dao.TaxonConceptDAO#getTaxonConcepts(java.lang.String, long)
	 */
	@SuppressWarnings("unchecked")
	public List<TaxonConcept> getTaxonConcepts(String canonical, long dataResourceId) {
		return (List<TaxonConcept>) getJdbcTemplate()
			// delme
			.query(TaxonConceptDAOImpl.QUERY_BY_CANONICAL_RESOURCE_SQL,
					new Object[]{canonical, dataResourceId},
					new RowMapperResultSetExtractor(tcRowMapper, 1));
	}
	
	/**
	 * @see org.gbif.portal.dao.TaxonConceptDAO#getRanksWithinResource(long)
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> getRanksWithinResource(final long dataResourceId) {
		return (List<Integer>)getJdbcTemplate()
		 .query(TaxonConceptDAOImpl.QUERY_FOR_RANKS_WITHIN_RESOURCE,
			new Object[]{dataResourceId},
			new RowMapperResultSetExtractor(intRowMapper, 10));
	}

	/**
	 * @see org.gbif.portal.dao.TaxonConceptDAO#getUnpartneredRanksWithinResource(long)
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> getUnpartneredRanksWithinResource(final long dataResourceId) {
		return (List<Integer>)getJdbcTemplate()
		 .query(TaxonConceptDAOImpl.QUERY_FOR_UNPARTNERED_RANKS_WITHIN_RESOURCE,
			new Object[]{dataResourceId},
			new RowMapperResultSetExtractor(intRowMapper, 10));
	}

	public void linkTaxonConceptsToParent(final Long parentConceptId, List<Long> childConceptIds) {
		StringBuffer sb = new StringBuffer();
		sb.append(TaxonConceptDAOImpl.UPDATE_PARENT_BATCH_SQL);
		String separator = "(";
		for (Long id : childConceptIds) {
			sb.append(separator);
			sb.append(id);
			separator=",";
		}
		sb.append(")");
		final String requestString = sb.toString();
		
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(requestString);
						ps.setLong(1,parentConceptId);
						return ps;
					}
				});
	}

	/* (non-Javadoc)
	 * @see org.gbif.portal.dao.TaxonConceptDAO#updateUnknownRanks(java.lang.Long)
	 */
	public boolean updateUnknownRanks(final Long dataResourceId) {
		int count = getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(TaxonConceptDAOImpl.UPDATE_UNRANKED_CONCEPTS_SQL);
						ps.setLong(1,dataResourceId);
						return ps;
					}
				});
		return (count > 0);
	}

	/* (non-Javadoc)
	 * @see org.gbif.portal.dao.TaxonConceptDAO#getDisambiguationConcept(java.lang.String, long)
	 */
	@SuppressWarnings("unchecked")
	public TaxonConcept getDisambiguationConcept(String canonical, long dataResourceId) {
		List<TaxonConcept> results = (List<TaxonConcept>)
		 getJdbcTemplate().query(TaxonConceptDAOImpl.QUERY_FOR_DISAMBIGUATION_CONCEPT,
			new Object[]{canonical, dataResourceId},
			new RowMapperResultSetExtractor(tcRowMapper, 1));
		if (results!=null && results.size()>0) {
			return results.get(0);
		} else {
			return null;
		}
	}

	/**
	 * This implementation works by marking all the resource's concepts as secondary and then setting
	 * those with records as primary again 
	 * @see org.gbif.portal.dao.TaxonConceptDAO#markConceptsWithoutOccurrenceRecordsAsSecondary(long)
	 */
	public void markConceptsWithoutOccurrenceRecordsAsSecondary(final long dataResourceId) {
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(TaxonConceptDAOImpl.MARK_CONCEPTS_SECONDARY_SQL);
						ps.setLong(1,dataResourceId);
						return ps;
					}
				});
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(TaxonConceptDAOImpl.MARK_CONCEPTS_WITH_OCCURRENCES_PRIMARY_SQL);
						ps.setLong(1,dataResourceId);
						return ps;
					}
				});
	}

	/* (non-Javadoc)
	 * @see org.gbif.portal.dao.TaxonConceptDAO#updateAccepted(java.lang.Long, boolean)
	 */
	public void updateAccepted(final Long taxonConceptId, final boolean accepted) {
		logger.debug("Setting accepted of " + taxonConceptId + " to " + accepted);
		getJdbcTemplate().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(TaxonConceptDAOImpl.UPDATE_ACCEPTED_SQL);
						ps.setBoolean(1,accepted);
						ps.setLong(2,taxonConceptId);
						return ps;
					}
				});
	}

	/* (non-Javadoc)
	 * @see org.gbif.portal.dao.TaxonConceptDAO#getTaxonConceptByDataProviderIdAndRemoteId(java.lang.Long, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public TaxonConceptLite getTaxonConceptByDataProviderIdAndRemoteId(Long dataProviderId, String nameCode) {
		List<TaxonConceptLite> results = (List<TaxonConceptLite>)
		 getJdbcTemplate()
		 .query(TaxonConceptDAOImpl.QUERY_BY_PROVIDER_ID_AND_REMOTE_ID_SQL,
			new Object[]{dataProviderId, nameCode},
			new RowMapperResultSetExtractor(tcLiteRowMapper, 1));
		if (results.size()>0) {
			return results.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<List<TaxonConceptLite>> getClassificationsOf(final int rank, final long dataResourceId, final boolean includeLowerTaxa, final boolean accepted, final long minimumId, final int maxResults) {
		return getClassificationsOf(rank, dataResourceId, includeLowerTaxa, accepted, minimumId, maxResults, false);
	}

	@SuppressWarnings("unchecked")
	public List<List<TaxonConceptLite>> getClassificationsOf(final int rank, final long dataResourceId, final boolean includeLowerTaxa, final boolean accepted, final long minimumId, final int maxResults, final boolean unpartneredOnly) {
		List<Long> ids = null;
		if (includeLowerTaxa) {
			ids = (List<Long>) getJdbcTemplate().query(
					new PreparedStatementCreator() {
						public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
							PreparedStatement ps = null;
							if (unpartneredOnly) {
								ps = conn.prepareStatement(TaxonConceptDAOImpl.QUERY_BY_RANK_GREATER_THAN_RESOURCE_ACCEPTED_SQL_NO_PARTNER);								
							} else {
								ps = conn.prepareStatement(TaxonConceptDAOImpl.QUERY_BY_RANK_GREATER_THAN_RESOURCE_ACCEPTED_SQL);
							}
							ps.setInt(1,rank);
							ps.setLong(2,dataResourceId);
							ps.setLong(3,minimumId);
							ps.setBoolean(4,accepted);							
							ps.setInt(5,maxResults);
							return ps;
						}						
					}, new RowMapperResultSetExtractor(idRowMapper, 10));
			
		} else {
			ids = (List<Long>) getJdbcTemplate().query(
					new PreparedStatementCreator() {
						public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
							PreparedStatement ps = null;
							if (unpartneredOnly) {
								ps = conn.prepareStatement(TaxonConceptDAOImpl.QUERY_BY_RANK_RESOURCE_ACCEPTED_SQL_NO_PARTNER);								
							} else {
								ps = conn.prepareStatement(TaxonConceptDAOImpl.QUERY_BY_RANK_RESOURCE_ACCEPTED_SQL);
							}
							ps.setInt(1,rank);
							ps.setLong(2,dataResourceId);
							ps.setLong(3,minimumId);
							ps.setBoolean(4,accepted);							
							ps.setInt(5,maxResults);
							return ps;
						}						
					}, new RowMapperResultSetExtractor(idRowMapper, 10));
			
			/*
			ids = (List<Long>) getJdbcTemplate()
			.query(TaxonConceptDAOImpl.QUERY_BY_RANK_RESOURCE_ACCEPTED_SQL,
				new Object[]{rank, dataResourceId, accepted, minimumId, maxResults},
				new RowMapperResultSetExtractor(idRowMapper, 3));
				*/
		}
		logger.debug("Found " + ids.size() + " concepts for rank[" + rank + "], dataResourceId[" + dataResourceId + "], accepted[" + accepted + "], minimumId[" + 
				minimumId + "], maxResults [" + maxResults+ "], unpartneredOnly[" + unpartneredOnly +"]");
		
		List<List<TaxonConceptLite>> results = new LinkedList<List<TaxonConceptLite>>();
		// build the classifications
		for (Long id : ids) {
			results.add(getClassificationConcepts(id));
		}
		return results;	}
}
