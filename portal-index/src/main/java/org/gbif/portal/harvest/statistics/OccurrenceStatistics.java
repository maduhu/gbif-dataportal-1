package org.gbif.portal.harvest.statistics;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A crawler that will loop over all the OR records and build up the statistics we track.
 * This is an implementation that meets our current needs, but does not have a mechanism for registering 
 * future statistics so could be refactored in the future perhaps?  Or perhaps it is simple enough to hack in 
 * a few more parameters...
 * 
 * Note that this only works on the resource level.  Data providers, networks etc can simply sum up the DR stats.  
 * Remember that the full loop is stored in memory and thus it is not ideal to bloat it out any more than is necessary
 * So, that said, we only need to track
 * TAXON, COUNTRY and RESOURCE
 * Everything else can be deduced from that
 * 
 * @author trobertson
 */
public class OccurrenceStatistics {
	/** 
	 * Cell types
	 */
	public static int CELL_TYPE_TAXON = 1;
	public static int CELL_TYPE_COUNTRY = 2;
	public static int CELL_TYPE_RESOURCE = 4;
	
	/**
	 * Cells
	 */ 
	protected Map<CentiCellId, Integer> taxonCellCount = new HashMap<CentiCellId, Integer>(500000);
	protected Map<CentiCellId, Integer> countryCellCount = new HashMap<CentiCellId, Integer>(500000);
	protected Map<CentiCellId, Integer> resourceCellCount = new HashMap<CentiCellId, Integer>(500000);
	
	/**
	 * Stats
	 */
	protected Map<String, Integer> countryOccurrence = new HashMap<String, Integer>(50000);
	protected Map<String, Integer> countryOccurrenceGeospatial = new HashMap<String, Integer>(50000);
	protected Map<String, Integer> countryOccurrenceCleanGeospatial = new HashMap<String, Integer>(50000);
	protected Map<Integer, Integer> resourceOccurrence = new HashMap<Integer, Integer>(50000);
	protected Map<Integer, Integer> resourceOccurrenceGeospatial = new HashMap<Integer, Integer>(50000);
	protected Map<Integer, Integer> resourceOccurrenceCleanGeospatial = new HashMap<Integer, Integer>(50000);
	
	/**
	 * Map of iso codes to the IDs
	 */
	Map<String, Integer> countryCodeToId = new HashMap<String, Integer>(1000);
	
	/**
	 * @param server DB server (e.g. Aenetus)
	 * @param user DB user
	 * @param password DB password
	 */
	protected void run(String server, String user, String password) throws Exception {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection(server, user, password);
			generateCountryCodes(conn);
			String query = 
				"select " +
					"o.cell_id as cell_id," +
					"o.centi_cell_id as centi_cell_id," +
					"o.geospatial_issue as geospatial_issue," +
					"o.data_resource_id as data_resource_id," +
					"o.iso_country_code as country," +
					"o.nub_concept_id as identified," +
					"nc.kingdom_concept_id as kingdom," +
					"nc.phylum_concept_id as phylum," +
					"nc.class_concept_id as class," +
					"nc.order_concept_id as 'order'," +
					"nc.family_concept_id as family," +
					"nc.genus_concept_id as genus," +
					"nc.species_concept_id as species " +
				"from occurrence_record o " +
					" inner join taxon_concept nc on o.nub_concept_id=nc.id ";
			
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
				
				
				String country =  rs.getString("country");
				Integer countryCode = null;
				if (country != null)
					countryCodeToId.get(country);
				int resourceId = rs.getInt("data_resource_id");
				
				// track the country and resource counts
				if (countryCode != null)
					incrementMapCount(countryOccurrence, countryCode);
				incrementMapCount(resourceOccurrence, resourceId);
				
				// it must have a cell and centi cell if it is to be considered georeferenced - right?
				if (rs.getObject("cell_id")!=null && 
						rs.getObject("centi_cell_id")!=null) {
					
					// these are reused so pull em out
					int cellId = rs.getInt("cell_id");
					int centiCellId = rs.getInt("centi_cell_id");
					
					// track the country and resource geo counts counts
					if (countryCode != null)
						incrementMapCount(countryOccurrenceGeospatial, countryCode);
					incrementMapCount(resourceOccurrenceGeospatial, resourceId);
					if (rs.getInt("geospatial_issue") == 0) {
						if (countryCode != null)
							incrementMapCount(countryOccurrenceCleanGeospatial, countryCode);
						incrementMapCount(resourceOccurrenceCleanGeospatial, resourceId);
					}
					
					// for taxon layer, we need the unique set of identifications (e.g. don't count things
					// identified as a higher taxon twice in the same centi cell)				
					Set<Integer> identifications = new HashSet<Integer>();
					if (rs.getObject("identified")!=null)
						identifications.add(rs.getInt("identified"));
					if (rs.getObject("kingdom")!=null)
						identifications.add(rs.getInt("kingdom"));
					if (rs.getObject("phylum")!=null)
						identifications.add(rs.getInt("phylum"));
					if (rs.getObject("class")!=null)
						identifications.add(rs.getInt("class"));
					if (rs.getObject("order")!=null)
						identifications.add(rs.getInt("order"));
					if (rs.getObject("family")!=null)
						identifications.add(rs.getInt("family"));
					if (rs.getObject("genus")!=null)
						identifications.add(rs.getInt("genus"));
					if (rs.getObject("species")!=null)
						identifications.add(rs.getInt("species"));
					// for each indentification - accumulate the count
					for (int id : identifications) {
						incrementMapCount(taxonCellCount, new CentiCellId(cellId,centiCellId,id));						
					}
					
					// and the layers for the country and resource
					if (countryCode != null)
						incrementMapCount(countryCellCount, new CentiCellId(cellId,centiCellId,countryCode));
					incrementMapCount(resourceCellCount, new CentiCellId(cellId,centiCellId,resourceId));
					
				}
				
				int loopLog = 1000000;
				if (i % loopLog == 0) {
					System.out.println(loopLog + " records returned in: "
							+ (1 + (System.currentTimeMillis() - time) / 1000)
							+ " secs. Total records returned: " + i);
					time = System.currentTimeMillis();
					logMemory();
				}
			}
			logMemory();
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
			} catch (Exception e) {};
			try {
				conn.close();
			} catch (Exception e) {};
		}
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
	 * Builds the countryCodeToId
	 * @param conn Which will not be closed
	 * @throws SQLException On error
	 */
	protected void generateCountryCodes(Connection conn) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select iso_country_code, id from country");
			while (rs.next()) {
				countryCodeToId.put(rs.getString("iso_country_code"), rs.getInt("id"));
			}
		} finally {
			// release database resources
			try {
				rs.close();
			} catch (Exception e) {};
			try {
				stmt.close();
			} catch (Exception e) {};
		}
	}
	
	/**
	 * @throws Exception
	 */
	protected static Connection getConnection(String server, String username, String password) throws Exception {
		String driver = "org.gjt.mm.mysql.Driver";
		String url = "jdbc:mysql://" + server + "/portal";
		Class.forName(driver);
		return DriverManager.getConnection(url, username, password);
	}

	/**
	 * @param args Requires server, user, password
	 */
	public static void main(String args[]) {
		if (args.length != 3) {
			System.out.print("Usage: OccurrenceStatistics dbServerName dbUser rbPassword");
			System.exit(1);
		}
		OccurrenceStatistics app = new OccurrenceStatistics();
		try {
			app.run(args[0], args[1], args[2]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected static void logMemory() {
		System.out.println("MaxMemory: " + Runtime.getRuntime().maxMemory());
		System.out.println("FreeMemory: " + Runtime.getRuntime().freeMemory());
		System.out.println("UsedMemory: " + Runtime.getRuntime().totalMemory());
	}
	
	/**
	 * Little class for using in collections
	 * @author trobertson
	 */
	class CentiCellId {
		protected int cellId;
		protected int centiCellId;
		protected int id;
		
		public CentiCellId(int cellId, int centiCellId, int id) {
			this.cellId = cellId;
			this.centiCellId = centiCellId;
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
						&& id == target.id) {
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
			return (cellId*100) + centiCellId + id;
		}
		
	}
}
