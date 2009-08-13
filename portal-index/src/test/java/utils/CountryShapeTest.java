package utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * A test class at the moment...
 * Loop over all OR records and test the lat long with a GIS loaded with country files 
 * 
 * @author trobertson
 */
public class CountryShapeTest {
	
	/**
	 * @param server DB server (e.g. Aenetus)
	 * @param user DB user
	 * @param password DB password
	 */
	protected void run(String server, String db, String user, String password,
			String gisServer, String gisDb, String gisUser, String gisPassword) throws Exception {
		Connection conn = null;
		Connection gisConn = null;
		try {
			conn = getConnection(server, db, user, password);
			gisConn = getGISConnection(gisServer, gisDb, gisUser, gisPassword);
			loop(conn, gisConn);			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// release database resources
			try {
				conn.close();
			} catch (Exception e) {};
			// release database resources
			try {
				gisConn.close();
			} catch (Exception e) {};
		}
		System.out.println("Finished All");
	}
	
	/**
	 * Does the work
	 */
	protected void loop(Connection conn, Connection gisConn) {
		Statement stmt = null;
		ResultSet rs = null;

		// records whose ISO code matches the code from the shape file 
		Count counts = new Count();
		
		try {
			String query = 
				"select " +
					"o.id as id," + 
					"o.latitude as latitude," +
					"o.longitude as longitude," +
					"o.iso_country_code as isoCode," +
					"o.geospatial_issue as issue " +
				//"from occurrence_record o where iso_country_code is null and latitude is not null and longitude is not null";
					"from occurrence_record o";
			
			// The following 2 lines make it open cursor record by record, zero memory result set style
			stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			stmt.setFetchSize(Integer.MIN_VALUE);
			rs = stmt.executeQuery(query);

			// extract data from the ResultSet scroll from top
			int i = 0;
			long time = System.currentTimeMillis();
			long startTime = System.currentTimeMillis();
			while (rs.next()) {
				i++;
				
				Float latitude = null;
				if (rs.getObject("latitude")!=null) {
					latitude = rs.getFloat("latitude");
				}
				Float longitude = null;
				if (rs.getObject("longitude")!=null) {
					longitude = rs.getFloat("longitude");
				}
				String isoCode = null;
				if (rs.getObject("isoCode")!=null) {
					isoCode = rs.getString("isoCode");
				}
				int issue = rs.getInt("issue");
				int id = rs.getInt("id");
				
				if (latitude!=null && longitude!=null) {
					testGIS(gisConn, id, latitude, longitude, isoCode, issue, counts);
				}
				
				int loopLog = 1000;
				if (i % loopLog == 0) {
					System.out.println(loopLog + " records returned in: "
							+ (1 + (System.currentTimeMillis() - time) / 1000)
							+ " secs. Total records returned: " + i);
					time = System.currentTimeMillis();
					counts.log();
				}
			}
			System.out.println("All records returned in: "
					+ (1 + (System.currentTimeMillis() - startTime) / 1000)
					+ " secs. Total records returned: " + i);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// release database resources
			try {
				rs.close();
			} catch (Exception e) {};
			try {
				stmt.close();
			} catch (Exception e) {};		}
	
		// release some resource
		System.gc();
	}
	
	class Count {
		public int countMatch;
		public int countNonMatch;
		public int additionalInterpretations;
		public int noIdea;
		public Map<String, String> nonMatches = new HashMap<String, String>();
		public void log() {
			System.out.println("  match[" + countMatch + "]   nonMatch[" + countNonMatch + "]    additional[" + 
					additionalInterpretations + "]    noIdea[" + noIdea + "]  conflictsSize[" + nonMatches.size() + "]    conflicts[" + nonMatches + "]");
		}
	}
	
	protected void testGIS(Connection conn, int id, float latitude, float longitude, String isoCode, Integer issue, Count counts) throws Exception {
		Statement st = conn.createStatement();		
		ResultSet rs = st.executeQuery("select iso_country_code from country_shape where Contains(ogc_geom, GeomFromText('POINT(" + longitude + " " + latitude + ")'))");
		if (rs.next()) {
			String fibs = rs.getString(1);
			//System.out.println(isoCode + " :" + fibs);
			
			// if it was not set...
			if (isoCode == null || isoCode.equals("null")) {
				if (fibs == null) { // and still no idea
					counts.noIdea++;
				} else { // but now we know
					counts.additionalInterpretations++;					
				}
			} else if (isoCode.equalsIgnoreCase(fibs)) {
				counts.countMatch++;
			} else {
				counts.countNonMatch++;
				counts.nonMatches.put(isoCode, fibs);
				if (isoCode.equals("BR")) {
					System.out.println("id: " + id + " : " + fibs);
					System.exit(2);
				}
			}
			
		}
		try {
			rs.close();
		} catch (Exception e){}
		try {
			st.close();
		} catch (Exception e){}
		
	}
	
	/**
	 * @throws Exception
	 */
	protected static Connection getConnection(String server, String db, String username, String password) throws Exception {
		// Test if the newer type is quicker...
		//String driver = "org.gjt.mm.mysql.Driver";
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://" + server + "/" + db;
		Class.forName(driver);
		return DriverManager.getConnection(url, username, password);
	}

	/**
	 * @throws Exception
	 */
	protected static Connection getGISConnection(String server, String db, String username, String password) throws Exception {
		// Test if the newer type is quicker...
		//String driver = "org.gjt.mm.mysql.Driver";
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://" + server + "/" + db;
		Class.forName(driver);
		return DriverManager.getConnection(url, username, password);
	}

	/**
	 * @param args Requires server, user, password
	 */
	public static void main(String args[]) {
		if (args.length != 8) {
			System.out.print("Usage: CountryShapeTest dbServerName dbName dbUser rbPassword dbGISServerName dbGISName dbGISUser rbGISPassword ");
			System.exit(1);
		}
		CountryShapeTest app = new CountryShapeTest();
		try {
			app.run(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
