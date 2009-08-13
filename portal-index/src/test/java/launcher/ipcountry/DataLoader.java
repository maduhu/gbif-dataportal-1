package launcher.ipcountry;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.gbif.portal.util.request.IPUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This class just updates the start_long and end_long values in the ip_country table.
 * 
 * @author dmartin
 */
public class DataLoader {	
	
	private ApplicationContext context;

	private void init() {
		String[] locations = {
				"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"
		};
		context = new ClassPathXmlApplicationContext(locations);
	}

	private void index() {

		DataSource dataSource = (DataSource) context.getBean("dataSource");
		try {
			java.sql.Connection connection  = dataSource.getConnection();
			connection.setAutoCommit(true);
			Statement stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = stmt.executeQuery("select id, start, end, start_long, end_long from ip_country");
			rs.absolute(1);
			do {
				//get the start
				String start = rs.getString("start");
				String end = rs.getString("end");
				if(start!=null && end!=null){
					long startLong =  IPUtils.convertIPtoLong(start);
					long endLong =  IPUtils.convertIPtoLong(end);
					rs.updateLong("start_long", startLong);
					rs.updateLong("end_long", endLong);
					rs.updateRow();
				}
			}
			while(rs.next());
			rs.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DataLoader launcher = new DataLoader();
		launcher.init();
		launcher.index();
		System.exit(1);
	}
}