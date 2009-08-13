/**
 * 
 */
package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author davemartin
 */
public class CommonNameTagLoader extends DataResourceTagLoader{
	
	protected int MAX_COMMON_NAMES_TO_ADD = 10;
	
	@SuppressWarnings("unchecked")
	protected void process(DataSource dataSource, JdbcTemplate template,
			Integer dataResourceId) throws Exception {
		
		Connection conn = dataSource.getConnection();		
		List<Map> commonNames = template.queryForList(
				"SELECT DISTINCT cn.id as id FROM taxon_concept tc " +
				"INNER JOIN taxon_concept nc ON tc.partner_concept_id=nc.id " +
				"INNER JOIN common_name cn ON cn.taxon_concept_id=nc.id " +
				"WHERE tc.data_resource_id=?", 
				new Object[]{dataResourceId} );		

		int count=0;
		PreparedStatement ps = conn.prepareStatement("INSERT INTO bi_relation_tag " +
				"(tag_id,from_entity_id,to_entity_id,is_system_generated) " +
				"VALUES (4152, ?, ?,true)");
		
		for(Map commonName: commonNames){
			if(count>=MAX_COMMON_NAMES_TO_ADD)
				break;
			Integer commonNameId = (Integer) commonName.get("id");
			ps.setLong(1, dataResourceId);
			ps.setLong(2, commonNameId);
			ps.execute();	
			count++;
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
		stmt.execute("DELETE from bi_relation_tag where tag_id=4152");
		stmt.close();
  }		
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CommonNameTagLoader launcher = new CommonNameTagLoader();
			launcher.run();
		} catch (Exception e){
			e.printStackTrace();
		}
		System.exit(1);
	}	
}
