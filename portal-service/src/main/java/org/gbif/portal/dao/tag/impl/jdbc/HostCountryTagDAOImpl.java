package org.gbif.portal.dao.tag.impl.jdbc;

import java.util.List;

import org.gbif.portal.dao.tag.BiRelationTagDAO;
import org.gbif.portal.dao.tag.QuadRelationTagDAO;
import org.gbif.portal.dao.tag.impl.jdbc.rowmapper.BiRelationTagRowMapper;
import org.gbif.portal.dao.tag.impl.jdbc.rowmapper.QuadRelationTagRowMapper;
import org.gbif.portal.dto.tag.BiRelationTagDTO;
import org.gbif.portal.dto.tag.QuadRelationTagDTO;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * Tag DAO for host country tags.
 * 
 * @author dmartin
 */
public class HostCountryTagDAOImpl extends JdbcDaoSupport implements BiRelationTagDAO, QuadRelationTagDAO  {
	
	//conventions in sql - tag_id to be the first arg, entity ids to be second
	
	//from = data_resource_id, to=country
	protected static final String SELECT_ALL_HOSTCOUNTRY_COUNTRY_TAG="select " + 
		"tag_id,entity1_id as from_entity_id,hc.iso_country_code as from_entity_name," +
		"entity2_id as to_entity_id,c.iso_country_code as to_entity_name,sum(qt.count) as count " + 
		"from quad_relation_tag qt " +
		"left outer join country hc on hc.id=entity1_id " +
		"left outer join country c on c.id=entity2_id " + 
		"where tag_id=? and rollover_id=? group by 2,4 order by 3,5"; 	
	
	protected static final String SELECT_HOSTCOUNTRY_TAG="select " + 
		"tag_id,entity1_id as from_entity_id,hc.iso_country_code as from_entity_name," +
		"entity2_id as to_entity_id,c.iso_country_code as to_entity_name,sum(qt.count) as count " + 
		"from quad_relation_tag qt " +
		"left outer join country hc on hc.id=entity1_id " +
		"left outer join country c on c.id=entity2_id " + 
		"where tag_id=? and entity1_id=? and rollover_id=? group by 2,4 order by 3,5"; 	

	protected static final String SELECT_COUNTRY_TAG="select " + 
		"tag_id,entity1_id as from_entity_id,hc.iso_country_code as from_entity_name," +
		"entity2_id as to_entity_id,c.iso_country_code as to_entity_name,sum(qt.count) as count " +	 
		"from quad_relation_tag qt " +
		"left outer join country hc on hc.id=entity1_id " +
		"left outer join country c on c.id=entity2_id " + 
		"where tag_id=? and entity2_id=? and rollover_id=? group by 2,4 order by 3,5"; 	
	
	protected static final String SELECT_HOSTCOUNTRY_COUNTRY_TAG="select " + 
		"tag_id,entity1_id as from_entity_id,hc.iso_country_code as from_entity_name," +
		"entity2_id as to_entity_id,c.iso_country_code as to_entity_name,sum(qt.count) as count " + 
		"from quad_relation_tag qt " +
		"left outer join country hc on hc.id=entity1_id " +
		"left outer join country c on c.id=entity2_id " + 
		"where tag_id=? and entity1_id=? and entity2_id=? and rollover_id=? group by 2,4 order by 3,5"; 	
	
	protected static final String SELECT_HOSTCOUNTRY_BREAKDOWN_TAG="select  " + 
		"tag_id," + 
		"entity1_id,hc.iso_country_code as entity1_name," + 
		"entity2_id,c.iso_country_code as entity2_name," + 
		"entity3_id,tn.canonical as entity3_name," + 
		"entity4_id,entity4_id as entity4_name," + 
		"sum(qt.count) as count " + 
		"from quad_relation_tag qt " + 
		"left outer join country hc on hc.id=entity1_id " + 
		"left outer join country c on c.id=entity2_id " + 
		"left outer join taxon_concept tc on tc.id=entity3_id " + 
		"left outer join taxon_name tn on tc.taxon_name_id=tn.id " + 
		"where tag_id=? and entity1_id=? and rollover_id=? group by 6,8 order by 7,8"; 		
	
	protected static final String SELECT_COUNTRY_BREAKDOWN_TAG="select " + 
		"tag_id," + 
		"entity1_id,hc.iso_country_code as entity1_name," + 
		"entity2_id,c.iso_country_code as entity2_name," + 
		"entity3_id,tn.canonical as entity3_name," + 
		"entity4_id,entity4_id as entity4_name," + 
		"sum(qt.count) as count " + 
		"from quad_relation_tag qt " + 
		"left outer join country hc on hc.id=entity1_id " + 
		"left outer join country c on c.id=entity2_id " + 
		"left outer join taxon_concept tc on tc.id=entity3_id " + 
		"left outer join taxon_name tn on tc.taxon_name_id=tn.id " + 
		"where tag_id=? and entity2_id=? and rollover_id=? group by 6,8 order by 7,8"; 		
	
	protected static final String SELECT_HOSTCOUNTRY_COUNTRY_BREAKDOWN_TAG="select " + 
		"tag_id," + 
		"entity1_id,hc.iso_country_code as entity1_name," + 
		"entity2_id,c.iso_country_code as entity2_name," + 
		"entity3_id,tn.canonical as entity3_name," + 
		"entity4_id,entity4_id as entity4_name," + 
		"sum(qt.count) as count " + 
		"from quad_relation_tag qt " + 
		"left outer join country hc on hc.id=entity1_id " + 
		"left outer join country c on c.id=entity2_id " + 
		"left outer join taxon_concept tc on tc.id=entity3_id " + 
		"left outer join taxon_name tn on tc.taxon_name_id=tn.id " + 
		"where tag_id=? and entity1_id=? and entity2_id=? and rollover_id=? group by 6,8 order by 7,8"; 	
	
	protected static final String SELECT_LATEST_ROLLOVER_ID = "select max(id) from rollover";
	
	/** the id of the latest rollover */
	protected int currentRolloverId = -1;
	
	/**
	 * Reusable row mapper
	 */
	protected BiRelationTagRowMapper rsRowMapper = new BiRelationTagRowMapper();
	protected QuadRelationTagRowMapper quadRowMapper = new QuadRelationTagRowMapper();
	
	@SuppressWarnings("unchecked")
	public List<BiRelationTagDTO> retrieveBiRelationTagsFor(final int tagId) {
		//TODO the tag id must be used to select the correct sql - hardcoded for now
		List<BiRelationTagDTO> results = (List<BiRelationTagDTO>) getJdbcTemplate()
		.query(SELECT_ALL_HOSTCOUNTRY_COUNTRY_TAG,
				new Object[] {tagId, getCurrentRolloverId()},
				new RowMapperResultSetExtractor(rsRowMapper));			
		logger.debug("Results:"+results.size());
		return results;
	}
	
	@SuppressWarnings("unchecked")
	public List<BiRelationTagDTO> retrieveFromBiRelationTagsFor(final int tagId, final Long fromEntityId) {
		//TODO the tag id must be used to select the correct sql - hardcoded for now
		List<BiRelationTagDTO> results = (List<BiRelationTagDTO>) getJdbcTemplate()
		.query(SELECT_HOSTCOUNTRY_TAG,
				new Object[] {tagId, fromEntityId, getCurrentRolloverId()},
				new RowMapperResultSetExtractor(rsRowMapper));			
		logger.debug("Results:"+results.size());
		return results;
	}	
	
	@SuppressWarnings("unchecked")
	public List<BiRelationTagDTO> retrieveToBiRelationTagsFor(final int tagId, final Long toEntityId) {
		//TODO the tag id must be used to select the correct sql - hardcoded for now
		List<BiRelationTagDTO> results = (List<BiRelationTagDTO>) getJdbcTemplate()
		.query(SELECT_COUNTRY_TAG,
				new Object[] {tagId, toEntityId, getCurrentRolloverId()},
				new RowMapperResultSetExtractor(rsRowMapper));			
		logger.debug("Results:"+results.size());
		return results;
	}	
	
	@SuppressWarnings("unchecked")
	public List<BiRelationTagDTO> retrieveBiRelationTagFor(final int tagId, final Long fromEntityId, final Long toEntityId) {
		//TODO the tag id must be used to select the correct sql - hardcoded for now
		List<BiRelationTagDTO> results = (List<BiRelationTagDTO>) getJdbcTemplate()
		.query(SELECT_HOSTCOUNTRY_COUNTRY_TAG,
				new Object[] {tagId, fromEntityId, toEntityId, getCurrentRolloverId()},
				new RowMapperResultSetExtractor(rsRowMapper));			
		logger.debug("Results:"+results.size());
		return results;
	}		

	/**
	 * retrieve the host, country, kingdom, basis breakdown 
	 * 
	 * @param tagId
	 * @param entity1Id
	 * @param entity2Id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<QuadRelationTagDTO> retrieveQuadRelationTagsFor(final int tagId, final Long entity1Id, final Long entity2Id) {
		List<QuadRelationTagDTO> results = (List<QuadRelationTagDTO>) getJdbcTemplate()
		.query(SELECT_HOSTCOUNTRY_COUNTRY_BREAKDOWN_TAG,
				new Object[] {tagId, entity1Id, entity2Id, getCurrentRolloverId()},
				new RowMapperResultSetExtractor(quadRowMapper));			
		logger.debug("Results:"+results.size());
		return results;		
	}	
	
	/**
	 * retrieve the host, kingdom, basis breakdown 
	 * 
	 * @param tagId
	 * @param entity1Id
	 * @param entity2Id
	 * @return
	 */	
	@SuppressWarnings("unchecked")
	public List<QuadRelationTagDTO> retrieveQuadRelationTagsForEntity1(final int tagId, final Long entity1Id) {
		List<QuadRelationTagDTO> results = (List<QuadRelationTagDTO>) getJdbcTemplate()
		.query(SELECT_HOSTCOUNTRY_BREAKDOWN_TAG,
				new Object[] {tagId, entity1Id, getCurrentRolloverId()},
				new RowMapperResultSetExtractor(quadRowMapper));			
		logger.debug("Results:"+results.size());
		return results;		
	}	
	
	/**
	 * retrieve the country, kingdom, basis breakdown 
	 * 
	 * @param tagId
	 * @param entity1Id
	 * @param entity2Id
	 * @return
	 */	
	@SuppressWarnings("unchecked")
  public List<QuadRelationTagDTO> retrieveQuadRelationTagsForEntity2(final int tagId, final Long entity2Id) {
		List<QuadRelationTagDTO> results = (List<QuadRelationTagDTO>) getJdbcTemplate()
		.query(SELECT_COUNTRY_BREAKDOWN_TAG,
				new Object[] {tagId, entity2Id, getCurrentRolloverId()},
				new RowMapperResultSetExtractor(quadRowMapper));			
		logger.debug("Results:"+results.size());
		return results;		
	}
	
	/**
	 * Retrieve the latest rollover id.
	 * @return
	 */
	private int getCurrentRolloverId(){
		if(currentRolloverId<0){
			currentRolloverId = getJdbcTemplate().queryForInt(SELECT_LATEST_ROLLOVER_ID);
		}
		return currentRolloverId;
	}
}