package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Creates taxonomic scope tags
 * 
 * @author davemartin
 */
public class TaxonomicCoverageTagLoader extends DataResourceTagLoader  {
	
	protected static Log logger = LogFactory.getLog(TaxonomicCoverageTagLoader.class);

	public static final int DEFAULT_CONCEPTS_TO_ADD = 5;	
	/**
	 * @see launcher.DataResourceTagLoader#process(javax.sql.DataSource, org.springframework.jdbc.core.JdbcTemplate, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	protected void process(DataSource dataSource, JdbcTemplate template,
			Integer dataResourceId) throws Exception {
		if(dataResourceId==1)
			return;
		
		//determine root taxa
		List<Map> rootTaxaResults = template.queryForList(
				"SELECT tc.id id,tc.partner_concept_id as partner_concept_id,tc.rank, " +
				"nc.kingdom_concept_id as kingdom_concept_id " +
				"FROM taxon_concept tc " +
				"LEFT OUTER JOIN taxon_concept nc ON tc.partner_concept_id = nc.id "  +
				"WHERE tc.parent_concept_id IS NULL AND tc.data_resource_id=? ORDER BY tc.rank", 
				new Object[]{dataResourceId} );
		
		boolean tagsInserted = false;
		
		int taxonomicScopeTagsAdded = 0;
		int MAX_TAGS = 10;
		
		Connection conn = dataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("INSERT INTO bi_relation_tag " +
				"(tag_id,from_entity_id,to_entity_id,is_system_generated) " +
				"VALUES (4150, ?, ?,true)");		
		
		//for each kingdom, go down each root until there is more than one child
		for(Map rootTaxa: rootTaxaResults){
			
			Long taxonConceptId = (Long) rootTaxa.get("id");
			Long nubConceptId = (Long) rootTaxa.get("partner_concept_id");

			//determine root taxa
			List<Map> childResults = template.queryForList("SELECT id,partner_concept_id, rank FROM taxon_concept tc " +
					"WHERE tc.parent_concept_id is null AND data_resource_id=?", 
					new Object[]{dataResourceId});
			
			//while there is only one child, iterate down
			while(childResults.size()==1){

				taxonConceptId = (Long) childResults.get(0).get("id");
				nubConceptId = (Long) childResults.get(0).get("partner_concept_id");
				childResults = template.queryForList("SELECT tc.id, tc.partner_concept_id FROM taxon_concept tc " +
						"WHERE tc.parent_concept_id = ?", 
						new Object[]{taxonConceptId});
			}
			
			if(nubConceptId!=null){
				//we have found the root taxon for this branch - create root taxon tag
				ps.setLong(1, dataResourceId);
				ps.setLong(2, nubConceptId);
				ps.execute();
				tagsInserted = true;
				taxonomicScopeTagsAdded++;
				if(taxonomicScopeTagsAdded>MAX_TAGS)
					break;
			}
		}
		ps.close();
		
		//if nothing inserted, insert tags for the first few concepts in root list
		if(!tagsInserted){
			
			ps = conn.prepareStatement("INSERT INTO bi_relation_tag " +
					"(tag_id,from_entity_id,to_entity_id,is_system_generated) " +
					"VALUES (4150, ?, ?,true)");

			int count = 0;
			for(Map rootTaxa: rootTaxaResults){
				if(count>=DEFAULT_CONCEPTS_TO_ADD)
					break;
				Long nubConceptId = (Long) rootTaxa.get("partner_concept_id");
				if(nubConceptId!=null){
					ps.setLong(1, dataResourceId);
					ps.setLong(2, nubConceptId);
					ps.execute();	
					count++;
				}
			}
			ps.close();
		}
		
		//set kingdom coverage
		ps = conn.prepareStatement("INSERT INTO bi_relation_tag " +
				"(tag_id,from_entity_id,to_entity_id,is_system_generated) " +
				"VALUES (4151, ?, ?,true)");

		List<Long> addedKingdoms = new ArrayList<Long>();
		for(Map rootTaxa: rootTaxaResults){
			Long nubConceptId = (Long) rootTaxa.get("kingdom_concept_id");
			if(nubConceptId!=null && !addedKingdoms.contains(nubConceptId)){
				ps.setLong(1, dataResourceId);
				ps.setLong(2, nubConceptId);
				ps.execute();
				addedKingdoms.add(nubConceptId);
			}
		}				
		ps.close();
		conn.close();
	}
	
	/**
   * @see utils.DataResourceTagLoader#clearOldTags(javax.sql.DataSource)
   */
  @Override
  protected void clearOldTags(DataSource dataSource) throws Exception {
		Connection conn = dataSource.getConnection();
		Statement stmt = conn.createStatement();
		stmt.execute("DELETE from bi_relation_tag where tag_id=4150");
		stmt.execute("DELETE from bi_relation_tag where tag_id=4151");
		stmt.close();
  }			
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TaxonomicCoverageTagLoader launcher = new TaxonomicCoverageTagLoader();
			launcher.run();
		} catch (Exception e){
			e.printStackTrace();
		}
		System.exit(1);
	}	
}