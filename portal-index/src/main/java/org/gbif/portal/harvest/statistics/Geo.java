package org.gbif.portal.harvest.statistics;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * A test class at the moment... 
 * 
 * @author trobertson
 */
public class Geo {
	
	/**
	 * @param server DB server (e.g. Aenetus)
	 * @param user DB user
	 * @param password DB password
	 */
	protected void run(String server, String db, String user, String password) throws Exception {
		Connection conn = null;
		try {
			conn = getConnection(server, db, user, password);
			// month
			loop(conn, "kingdom_concept_id", 2,false);
			loop(conn, "phylum_concept_id", 3,false);
			loop(conn, "class_concept_id", 4,false);
			loop(conn, "order_concept_id", 5,false);
			loop(conn, "family_concept_id", 6,false);
			loop(conn, "genus_concept_id", 7, false);
			loop(conn, "species_concept_id", 8, false);
			loop(conn, "nub_concept_id", 9, false);
			loop(conn, "data_resource_id", 1, false);
			
			// decade
			loop(conn, "kingdom_concept_id", 2,true);
			loop(conn, "phylum_concept_id", 3,true);
			loop(conn, "class_concept_id", 4,true);
			loop(conn, "order_concept_id", 5,true);
			loop(conn, "family_concept_id", 6,true);
			loop(conn, "genus_concept_id", 7, true);
			loop(conn, "species_concept_id", 8, true);
			loop(conn, "nub_concept_id", 9, true);
			loop(conn, "data_resource_id", 1, true);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// release database resources
			try {
				conn.close();
			} catch (Exception e) {};
		}
		System.out.println("Finished All");
	}
	
	/**
	 * Single parameter loop
	 * @param conn
	 * @param parameterName
	 */
	protected void loop(Connection conn, String parameterName, int cellType, boolean byDecade) {
		System.out.println("Starting parameter: " + parameterName);
		Statement stmt = null;
		ResultSet rs = null;
		Map<CentiCellId, Integer> data = new HashMap<CentiCellId, Integer>(10000000);
		try {
			String query = 
				"select " +
					"o." + parameterName + " as " + parameterName + "," +
					"o.cell_id as cell_id," +
					"o.centi_cell_id as centi_cell_id, " +
					"o.year as year," + 
					"o.month as month " +
				"from occurrence o";
			
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
				// it must have a cell and centi cell if it is to be considered georeferenced - right?
				if (rs.getObject("cell_id")!=null && 
						rs.getObject("centi_cell_id")!=null) {
					
					// these are reused so pull em out
					int cellId = rs.getInt("cell_id");
					int centiCellId = rs.getInt("centi_cell_id");
					 
					Integer month = null;
					if (rs.getObject("month") != null)
						month = rs.getInt("month");
					Integer year = null;
					Integer decade = null;
					if (rs.getObject("year") != null) {
						year = rs.getInt("year");
						decade = (year/10)*10;
					}
					
					if (!byDecade)
						incrementMapCount(data, new CentiCellId(cellId,centiCellId,month,rs.getInt(parameterName)));
					else 
						incrementMapCount(data, new CentiCellId(cellId,centiCellId,decade,rs.getInt(parameterName)));
				}
				
				int loopLog = 1000000;
				if (i % loopLog == 0) {
					System.out.println(loopLog + " records returned in: "
							+ (1 + (System.currentTimeMillis() - time) / 1000)
							+ " secs. Total records returned: " + i);
					time = System.currentTimeMillis();
				}
			}
			System.out.println("All records returned in: "
					+ (1 + (System.currentTimeMillis() - startTime) / 1000)
					+ " secs. Total records returned: " + i);
			
			
			if (!byDecade)
				writeToFile(parameterName + "Month.txt", cellType, data);
			else 
				writeToFile(parameterName + "Decade.txt", cellType, data);
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
		System.out.println("Finished parameter: " + parameterName);		
		// release some resource
		System.gc();
	}
	
	/**
	 * @param map To increment the count for
	 * @param key To get in the map
	 */
	@SuppressWarnings("unchecked")
	protected void incrementMapCount(Map map, Object key) {
		Integer count = (Integer) map.get(key);
		if (count == null) {
			map.put(key, 1);
		} else {
			map.put(key, (count + 1));
		}
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
	 * @param args Requires server, user, password
	 */
	public static void main(String args[]) {
		if (args.length != 4) {
			System.out.print("Usage: OccurrenceStatistics dbServerName dbName dbUser rbPassword");
			System.exit(1);
		}
		Geo app = new Geo();
		try {
			app.run(args[0], args[1], args[2], args[3]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Little class for using in collections
	 * @author trobertson
	 */
	class CentiCellId {
		protected int cellId;
		protected int centiCellId;
		protected Integer datePart;
		protected int id;
		
		public CentiCellId(int cellId, int centiCellId, Integer datePart, int id) {
			this.cellId = cellId;
			this.centiCellId = centiCellId;
			this.datePart = datePart;
			this.id=id;
		}
		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj!=null && obj instanceof CentiCellId) {
				CentiCellId target = (CentiCellId) obj;
				if (cellId == target.cellId
						&& centiCellId == target.centiCellId
						&& id == target.id						
						&& ((datePart == null) && (target.datePart == null)
								|| ((datePart != null) && datePart.equals(target.datePart)))) {
					return true;
				}
			}
			return false;
		}
		/**
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			if (datePart == null) 
				return (cellId*100) + centiCellId + id;
			else 
				return (cellId*103) + centiCellId + id + (datePart*717);
		}
	}
	
	/**
	 * Writes the file of data
	 * @param fileName
	 * @param data
	 */
	protected void writeToFile(String fileName, int type, Map<CentiCellId, Integer> data) {
		FileWriter fw = null;
		BufferedWriter out = null;
		System.out.println("Writing to file: " + fileName);
		try {
			fw = new FileWriter(fileName);
			out = new BufferedWriter(fw);
			
			for (CentiCellId k : data.keySet()) {
				// why it needs to be cast a String I am not so sure...
				fw.write("" + type);
				fw.write("\t");
				fw.write("" + k.id);
				fw.write("\t");
				fw.write("" + k.cellId);
				fw.write("\t");
				fw.write("" + k.centiCellId);
				fw.write("\t");
				if (k.datePart == null) 
					fw.write("\\N");
				else 
					fw.write("" + k.datePart);
				fw.write("\t");
				fw.write("" + data.get(k));
				fw.write("\n");			
			}
			System.out.println("Successfully written " + data.keySet().size() +  " records to file: " + fileName);
			
			
		} catch (Exception e) {// Catch exception if any
			System.out.println("Error writing to file: " + fileName);
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}		
	}
}
