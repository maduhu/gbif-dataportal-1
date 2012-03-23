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
 * @author dmartin
 */
public class CollectorTagLoader extends DataResourceTagLoader {

	@SuppressWarnings("unchecked")
	protected void process(DataSource dataSource, JdbcTemplate template, 
			Integer dataResourceId) throws Exception {

		Connection conn = dataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("INSERT INTO string_tag " +
				"(tag_id,entity_id,value,is_system_generated) " +
				"VALUES (4161, ?, ?, true)");
		
		List<Map> collectorNames = template.queryForList(
				"SELECT DISTINCT collector_name FROM raw_occurrence_record " +
				"WHERE data_resource_id=? and collector_name is not null limit 10", 
				new Object[]{dataResourceId} );		
		
		int count=0;
		
		for(Map collectorNameMap: collectorNames){
			try {			
				String collectorName = (String) collectorNameMap.get("collector_name");
				//escape single quotes
				StringBuffer oldSe = new StringBuffer("'");
				StringBuffer newSe = new StringBuffer("\\'");
				collectorName = collectorName.replace(oldSe, newSe);
				ps.setLong(1, dataResourceId);
				ps.setString(2, collectorName);
				ps.execute();
				count++;
			}catch (Exception e){
				logger.error(e.getMessage(), e);
			}
		}
		ps.close();
		conn.close();
		
		logger.info("Collector tags inserted: "+count);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CollectorTagLoader launcher = new CollectorTagLoader();
			launcher.run();
		} catch (Exception e){
			e.printStackTrace();
		}
		System.exit(1);
	}

	/**
   * @see utils.DataResourceTagLoader#clearOldTags(javax.sql.DataSource)
   */
  @Override
  protected void clearOldTags(DataSource dataSource) throws Exception {
		Connection conn = dataSource.getConnection();
		Statement stmt = conn.createStatement();
		stmt.execute("DELETE from string_tag where tag_id=4161");
		stmt.close();
  }		
}