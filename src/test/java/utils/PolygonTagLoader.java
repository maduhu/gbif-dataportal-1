package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.vividsolutions.jts.algorithm.ConvexHull;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * @author dave
 */
public class PolygonTagLoader extends DataResourceTagLoader {

	@SuppressWarnings("unchecked")
	@Override
	protected void process(DataSource dataSource, JdbcTemplate template,
			Integer dataResourceId) throws Exception {
		
		//do not generate for nub
		if(dataResourceId==1)
			return;
		
		List<Map> results = template.queryForList("select distinct(latitude), longitude from occurrence_record" +
				" where data_resource_id=? and latitude is not null", new Object[]{dataResourceId});

		Coordinate[] coordinates = new Coordinate[results.size()];
		int i=0;
		for(Map result: results){
			coordinates[i] = new Coordinate((Float)result.get("longitude"),(Float)result.get("latitude"));
			i++;
		}
		
		GeometryFactory gf = new GeometryFactory();
		ConvexHull convexHull = new ConvexHull(coordinates, gf);
		Geometry g = convexHull.getConvexHull();
		Coordinate[] coords = g.getCoordinates();
		for(Coordinate coord: coords){
			coord.x = (Math.floor(coord.x * 100))/100;
			coord.y = (Math.floor(coord.y * 100))/100;
		}

		String polygon = g.toString();
		System.out.println(polygon);
		if(!polygon.startsWith("POLYGON"))
			return;
		
		
		Connection conn = dataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("INSERT INTO string_tag " +
				"(tag_id,entity_id,value,is_system_generated) " +
				"VALUES (4102, ?, ?, true)");
		
		//escape single quotes
		ps.setLong(1, dataResourceId);
		ps.setString(2, polygon);
		ps.execute();
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
		stmt.execute("DELETE from string_tag where tag_id=4102");
		stmt.close();
  }		
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			PolygonTagLoader launcher = new PolygonTagLoader();
			launcher.run();
		} catch (Exception e){
			e.printStackTrace();
		}
		System.exit(1);
	}				
}