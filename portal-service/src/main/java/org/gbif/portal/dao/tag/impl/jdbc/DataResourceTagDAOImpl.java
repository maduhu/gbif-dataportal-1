package org.gbif.portal.dao.tag.impl.jdbc;

import java.util.List;

import org.gbif.portal.dao.tag.SimpleTagDAO;
import org.gbif.portal.dao.tag.TagDAO;
import org.gbif.portal.dao.tag.impl.jdbc.rowmapper.BiRelationTagRowMapper;
import org.gbif.portal.dao.tag.impl.jdbc.rowmapper.BooleanTagRowMapper;
import org.gbif.portal.dao.tag.impl.jdbc.rowmapper.GeographicalCoverageTagRowMapper;
import org.gbif.portal.dao.tag.impl.jdbc.rowmapper.NumberTagRowMapper;
import org.gbif.portal.dao.tag.impl.jdbc.rowmapper.StringTagRowMapper;
import org.gbif.portal.dao.tag.impl.jdbc.rowmapper.TemporalCoverageTagRowMapper;
import org.gbif.portal.dto.tag.BiRelationTagDTO;
import org.gbif.portal.dto.tag.BooleanTag;
import org.gbif.portal.dto.tag.GeographicalCoverageTag;
import org.gbif.portal.dto.tag.NumberTag;
import org.gbif.portal.dto.tag.StringTag;
import org.gbif.portal.dto.tag.TemporalCoverageTag;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * JDBC impl for SimpleTagDAO for data resources.
 * 
 * @author dmartin
 */
public class DataResourceTagDAOImpl extends JdbcDaoSupport implements SimpleTagDAO  {
	
	//conventions in sql - tag_id to be the first arg, entity ids to be second
	protected static final String SELECT_BOOLEAN_TAG_BY_DATASET="select "+ 
		"tag_id,entity_id,dr.name as entity_name,is_true "+
		"from boolean_tag bt "+
		"inner join data_resource dr on entity_id=dr.id "+  
		"where tag_id=? and entity_id=?"; 	
	
	protected static final String SELECT_STRING_TAG_BY_DATASET="select "+ 
		"tag_id,entity_id,dr.name as entity_name,value "+
		"from string_tag st "+
		"inner join data_resource dr on entity_id=dr.id "+  
		"where tag_id=? and entity_id=? order by value";
	
	protected static final String SELECT_NUMBER_TAG_BY_DATASET="select "+ 
		"tag_id,entity_id,dr.name as entity_name,value "+
		"from number_tag nt "+
		"inner join data_resource dr on entity_id=dr.id "+  
		"where tag_id=? and entity_id=? order by value asc"; 		
	
	protected static final String SELECT_DATASET_GEOGRAPHICAL_COVERAGE_TAG_BY_DATASET="select "+ 
		"tag_id,entity_id,dr.name as entity_name,min_longitude,max_longitude,min_latitude,max_latitude "+
		"from geographical_coverage_tag gct "+
		"inner join data_resource dr on entity_id=dr.id "+  
		"where tag_id=? and entity_id=?"; 
	
	//from = data_resource_id, to=country
	protected static final String SELECT_DATASET_COUNTRY_TAG_BY_DATASET="select " + 
		"tag_id,from_entity_id,dr.name as from_entity_name,to_entity_id,cn.name as to_entity_name,rt.count as count " + 
		"from bi_relation_tag rt " +
		"inner join data_resource dr on dr.id=from_entity_id " +
		"inner join country_name cn on cn.country_id=rt.to_entity_id " + 
		"where cn.locale='en' and tag_id=? and dr.id=? order by from_entity_name"; 	
	
	//from = data_resource_id, to=taxon concept
	protected static final String SELECT_TAXONOMIC_SCOPE_TAG_BY_DATASET="select " + 
		"tag_id,from_entity_id,dr.name as from_entity_name,to_entity_id," +
		"tn.canonical as to_entity_name,rt.count as count,rk.name as rank " + 
		"from bi_relation_tag rt " +
		"inner join data_resource dr on dr.id=from_entity_id " + 
		"inner join taxon_concept tc on tc.id=rt.to_entity_id " + 
		"inner join taxon_name tn on tc.taxon_name_id=tn.id " + 
		"inner join rank rk on rk.id=tn.rank "+		
		"where tag_id=? and from_entity_id=? " +
		"group by 1,2,3,4,5,6,7 " +
		"order by to_entity_name";
	
	protected static final String SELECT_COMMON_TAG_BY_DATASET="select " + 
		"tag_id,from_entity_id,dr.name as from_entity_name,to_entity_id,cn.name as to_entity_name,rt.count as count " + 
		"from bi_relation_tag rt " +
		"inner join data_resource dr on dr.id=from_entity_id " +
		"inner join common_name cn on cn.id=rt.to_entity_id " + 
		"where tag_id=? and from_entity_id=? order by to_entity_name"; 			
	
	protected static final String SELECT_DATASET_TEMPORAL_COVERAGE_TAG_BY_DATASET="select " + 
		"tag_id,entity_id,dr.name as entity_name,start_date,end_date " + 
		"from temporal_coverage_tag " + 
		"inner join data_resource dr on dr.id=entity_id " + 
		"where tag_id=? and dr.id=?"; 				

	/**
	 * Reusable row mapper
	 */
	protected GeographicalCoverageTagRowMapper gcRowMapper = new GeographicalCoverageTagRowMapper();
	protected TemporalCoverageTagRowMapper tcRowMapper = new TemporalCoverageTagRowMapper();	
	protected BiRelationTagRowMapper brRowMapper = new BiRelationTagRowMapper();
	protected BooleanTagRowMapper boolRowMapper = new BooleanTagRowMapper();	
	protected StringTagRowMapper sRowMapper = new StringTagRowMapper();
	protected NumberTagRowMapper nRowMapper = new NumberTagRowMapper();
	
	@SuppressWarnings("unchecked")
	public List<BooleanTag> retrieveBooleanTagsForEntity(final int tagId, final long entityId) {
		logger.debug("BooleanTag: Querying with tagId: "+tagId+", entityId: "+entityId);
		List<BooleanTag> results = (List<BooleanTag>) getJdbcTemplate()
		.query(SELECT_BOOLEAN_TAG_BY_DATASET,
				new Object[] {tagId, entityId},
				new RowMapperResultSetExtractor(boolRowMapper));
		logger.debug("Results:"+results.size());
		return results;
	}
	
	@SuppressWarnings("unchecked")
	public List<GeographicalCoverageTag> retrieveGeographicalCoverageTagsForEntity(final int tagId, final long entityId) {
		logger.debug("GeographicalCoverageTag: Querying with tagId: "+tagId+", entityId: "+entityId);
		//TODO the tag id must be used to select the correct sql - hardcoded for now
		List<GeographicalCoverageTag> results = (List<GeographicalCoverageTag>) getJdbcTemplate()
		.query(SELECT_DATASET_GEOGRAPHICAL_COVERAGE_TAG_BY_DATASET,
				new Object[] {tagId, entityId},
				new RowMapperResultSetExtractor(gcRowMapper));
		logger.debug("Results:"+results.size());
		return results;
	}
	
	@SuppressWarnings("unchecked")
	public List<TemporalCoverageTag> retrieveTemporalCoverageTagsForEntity(final int tagId, final long entityId) {
		logger.debug("TemporalCoverageTag: Querying with tagId: "+tagId+", entityId: "+entityId);
		//TODO the tag id must be used to select the correct sql - hardcoded for now
		List<TemporalCoverageTag> results = (List<TemporalCoverageTag>) getJdbcTemplate()
		.query(SELECT_DATASET_TEMPORAL_COVERAGE_TAG_BY_DATASET,
				new Object[] {tagId, entityId},
				new RowMapperResultSetExtractor(tcRowMapper));
		logger.debug("Results:"+results.size());
		return results;
	}	
	
	@SuppressWarnings("unchecked")
	public List<StringTag> retrieveStringTagsForEntity(final int tagId, final long entityId) {
		logger.debug("StringTag: Querying with tagId: "+tagId+", entityId: "+entityId);
		//TODO the tag id must be used to select the correct sql - hardcoded for now
		List<StringTag> results = (List<StringTag>) getJdbcTemplate()
		.query(SELECT_STRING_TAG_BY_DATASET,
				new Object[] {tagId, entityId},
				new RowMapperResultSetExtractor(sRowMapper));
		logger.debug("Results:"+results.size());
		return results;
	}	
	
	@SuppressWarnings("unchecked")
	public List<NumberTag> retrieveNumberTagsForEntity(final int tagId, final long entityId) {
		logger.debug("NumberTag: Querying with tagId: "+tagId+", entityId: "+entityId);
		//TODO the tag id must be used to select the correct sql - hardcoded for now
		List<NumberTag> results = (List<NumberTag>) getJdbcTemplate()
		.query(SELECT_NUMBER_TAG_BY_DATASET,
				new Object[] {tagId, entityId},
				new RowMapperResultSetExtractor(nRowMapper));
		logger.debug("Results:"+results.size());
		return results;
	}		
	
	@SuppressWarnings("unchecked")
	public List<BiRelationTagDTO> retrieveBiRelationTagsForEntity(final int tagId, final long fromEntityId) {
		logger.debug("BiRelationTag: Querying with tagId: "+tagId+", fromEntityId: "+fromEntityId);
		
		String query = SELECT_DATASET_COUNTRY_TAG_BY_DATASET;
		
		if(tagId==TagDAO.DATA_RESOURCE_TAXONOMIC_SCOPE 
				|| tagId==TagDAO.DATA_RESOURCE_ASSOCIATED_KINGDOM 
				|| tagId==TagDAO.DATA_RESOURCE_OCCURRENCES_SPECIES 
				|| tagId==TagDAO.DATA_RESOURCE_OCCURRENCES_GENUS 
				|| tagId==TagDAO.DATA_RESOURCE_OCCURRENCES_FAMILY)
			query=SELECT_TAXONOMIC_SCOPE_TAG_BY_DATASET;
		
		if(tagId==1152)
			query=SELECT_COMMON_TAG_BY_DATASET;
		
		logger.debug("Querying with tagId: "+tagId+", entityId: "+fromEntityId);
		logger.debug(query);
		//TODO the tag id must be used to select the correct sql - hardcoded for now
		List<BiRelationTagDTO> results = (List<BiRelationTagDTO>) getJdbcTemplate()
		.query(query,
				new Object[] {tagId, fromEntityId},
				new RowMapperResultSetExtractor(brRowMapper));			
		logger.debug("Results:"+results.size());
		return results;
	}
}