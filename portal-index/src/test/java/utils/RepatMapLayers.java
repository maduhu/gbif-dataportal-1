package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.gbif.portal.util.geospatial.CellIdUtils;
import org.gbif.portal.util.geospatial.LatLongBoundingBox;

/**
 * Simple runnable that generates map layers for host country and country
 * intersects.
 * 
 * @author dmartin
 */
public class RepatMapLayers {

	protected static byte[] EOL = "\n".getBytes();
	protected static byte[] TAB = "\t".getBytes();

	protected final static String countryUnknown = "XX";

	public static void main(String[] args) throws Exception {

		if (args.length != 0 && args.length != 4) {
			System.out.println("Usage: <db-url> <username> <password> <basedir>");
			System.out.println("example: jdbc:mysql://localhost/portal root password /usr/share/tomcat/temp/");
			return;
		}

		String dbUrl = args[0];
		String username = args[1];
		String password = args[2];
		String basedir = args[3];

		String driver = "org.gjt.mm.mysql.Driver";
		Class.forName(driver);

		String filePath = basedir + "/maplayers/homeCountry/";
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(dbUrl, username, password);
			//select distinct host countries
			String query = "select distinct(iso_country_code) from data_provider";
			stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			stmt.setFetchSize(Integer.MIN_VALUE);
			rs = stmt.executeQuery(query);

			List<String> isoCountryCodes = new ArrayList<String>();

			while (rs.next()) {
				String isoCountryCode = rs.getString("iso_country_code");
				isoCountryCodes.add(isoCountryCode);
			}
			rs.close();
			stmt.close();

			isoCountryCodes.add(countryUnknown);

			// for each host country
			for (String hostCountryCode: isoCountryCodes) {
				// generate all the host layers for this country

				StringBuffer cellDensityQuery = new StringBuffer(
						"select oc.iso_country_code, cell_id, count(oc.id) density "
				    + "from occurrence_record oc " 
				    + "inner join data_provider dp on oc.data_provider_id=dp.id where");
				
				if (countryUnknown.equals(hostCountryCode)) {
					cellDensityQuery.append(" dp.iso_country_code is null ");
				} else {
					cellDensityQuery.append(" dp.iso_country_code=? ");
				}
				
				cellDensityQuery.append("and cell_id is not null " 
						+ "and geospatial_issue=0 "
				    + "group by oc.iso_country_code, cell_id " 
				    + "order by oc.iso_country_code, cell_id ");

				PreparedStatement pstmt = conn.prepareStatement(cellDensityQuery.toString());
				if (!countryUnknown.equals(hostCountryCode)) {
					pstmt.setString(1, hostCountryCode);
				}
				pstmt.execute();
				ResultSet rs2 = pstmt.getResultSet();

				String currentFilePath = null;
				FileOutputStream fOut = null;

				if (hostCountryCode == null || "null".equalsIgnoreCase(hostCountryCode)) {
					hostCountryCode = countryUnknown;
				}

				// the directory to create the files
				// maplayers/repatriation/<host>/<country>
				currentFilePath = filePath + hostCountryCode;

				File hostDir = new File(currentFilePath);
				if (!hostDir.exists()) {
					FileUtils.forceMkdir(hostDir);
				}

				String currentIso = null;

				while (rs2.next()) {
					String isoCountryCode = rs2.getString("iso_country_code");
					int cellId = rs2.getInt("cell_id");
					int density = rs2.getInt("density");

					if (isoCountryCode == null || "null".equalsIgnoreCase(isoCountryCode)) {
						isoCountryCode = countryUnknown;
					}

					if (currentIso == null || !currentIso.equals(isoCountryCode)) {
						// create directory if not there
						currentIso = isoCountryCode;

						File file = new File(currentFilePath + File.separatorChar + isoCountryCode + ".txt");
						if (file.exists()) {
							System.out.println("deleting - " + file.getPath());
							file.delete();
						}
						file.createNewFile();
						if (fOut != null) {
							fOut.close();
						}
						fOut = new FileOutputStream(file);
						fOut.write("MINX\tMINY\tMAXX\tMAXY\tDENSITY".getBytes());
					}

					LatLongBoundingBox llbb = CellIdUtils.toBoundingBox(cellId);
					fOut.write(EOL);
					fOut.write(Float.toString(llbb.getMinLong()).getBytes());
					fOut.write(TAB);
					fOut.write(Float.toString(llbb.getMinLat()).getBytes());
					fOut.write(TAB);
					fOut.write(Float.toString(llbb.getMaxLong()).getBytes());
					fOut.write(TAB);
					fOut.write(Float.toString(llbb.getMaxLat()).getBytes());
					fOut.write(TAB);
					fOut.write(Integer.toString(density).getBytes());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// release database resources
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}