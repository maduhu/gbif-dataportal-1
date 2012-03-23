/***************************************************************************
 * Copyright (C) 2005 Global Biodiversity Information Facility Secretariat.
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ***************************************************************************/
package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * We are not yet capabale of deleting taxonomic records, so this will delete the 
 * occurrence records only and the associated, types, logs etc
 * 
 * This will generate a SQL file that can be run to delete the records
 * 
 * Tables:
 *   data_resource
 *   data_resource_agent
 *   gbif_log_message
 *   identifier_record
 *   image_record
 *   link_record
 *   network_membership
 *   occurrence_record
 *   raw_occurrence_record
 *   resource_country
 *   typification_record
 *   resource_access_point
 * 
 * @author trobertson
 */
public class DeleteResourceSQL {
	// self explainatory
	protected String host;
	protected String user;
	protected String password;
	protected long id;
	protected Date date; 
	protected String dateAsString;
	
	
	// the SQL statements that get executed
	protected List<String> sql = new LinkedList<String>();
	protected List<String> counts = new LinkedList<String>();
	
	
	/**
	 * Constructor
	 */
	protected DeleteResourceSQL(String host, String user, String password, String id, String date) {
		this.host = host;
		this.user = user;
		this.password = password;
		try {
			this.id = Long.parseLong(id);
		} catch (NumberFormatException e) {
			System.out.println("ERROR: Data Resource ID is not a valid id: " + id);
			System.exit(1);
		}
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				this.date = sdf.parse(date);
				this.dateAsString = date.replaceAll("-", "");
			} catch (ParseException e) {
				System.out.println("ERROR: Date is not in valid format (yyyy-mm-dd): " + date);
				System.exit(1);
			}
		}
	}
	
	/**
	 * Generates the SQL for deleting the OR style data and all it's associated tables:
	 *   gbif_log_message
	 *   identifier_record
	 *   image_record
	 *   link_record
	 *   typification_record	 
	 *   occurrence_record
	 *   raw_occurrence_record
	 */
	protected void deleteORData() throws Exception {
		if (dateAsString!=null) {
			selectCountAddDelete("delete gbl.*" , "from gbif_log_message gbl inner join raw_occurrence_record ror on gbl.occurrence_id=ror.id where ror.data_resource_id=" + id + " and ror.modified<'" + dateAsString + "'");
			selectCountAddDelete("delete ir.*" , "from identifier_record ir inner join raw_occurrence_record ror on ir.occurrence_id=ror.id where ror.data_resource_id=" + id + " and ror.modified<'" + dateAsString + "'");
			selectCountAddDelete("delete ir.*" , "from image_record ir inner join raw_occurrence_record ror on ir.occurrence_id=ror.id where ror.data_resource_id=" + id + " and ror.modified<'" + dateAsString + "'");
			selectCountAddDelete("delete lr.*" , "from link_record lr inner join raw_occurrence_record ror on lr.occurrence_id=ror.id where ror.data_resource_id=" + id + " and ror.modified<'" + dateAsString + "'");
			selectCountAddDelete("delete tr.*" , "from typification_record tr inner join raw_occurrence_record ror on tr.occurrence_id=ror.id where ror.data_resource_id=" + id + " and ror.modified<'" + dateAsString + "'");
			selectCountAddDelete("delete o.*" , "from occurrence_record o inner join raw_occurrence_record ror on o.id=ror.id where ror.data_resource_id=" + id + " and ror.modified<'" + dateAsString + "'");
			selectCountAddDelete("delete" , "from raw_occurrence_record where data_resource_id=" + id + " and modified<'" + dateAsString + "'");
		} else {
			selectCountAddDelete("delete gbl.*" , "from gbif_log_message gbl inner join raw_occurrence_record ror on gbl.occurrence_id=ror.id where ror.data_resource_id=" + id);
			selectCountAddDelete("delete ir.*" , "from identifier_record ir inner join raw_occurrence_record ror on ir.occurrence_id=ror.id where ror.data_resource_id=" + id);
			selectCountAddDelete("delete ir.*" , "from image_record ir inner join raw_occurrence_record ror on ir.occurrence_id=ror.id where ror.data_resource_id=" + id);
			selectCountAddDelete("delete lr.*" , "from link_record lr inner join raw_occurrence_record ror on lr.occurrence_id=ror.id where ror.data_resource_id=" + id);
			selectCountAddDelete("delete tr.*" , "from typification_record tr inner join raw_occurrence_record ror on tr.occurrence_id=ror.id where ror.data_resource_id=" + id);
			selectCountAddDelete("delete o.*" , "from occurrence_record o inner join raw_occurrence_record ror on o.id=ror.id where ror.data_resource_id=" + id);
			selectCountAddDelete("delete" , "from raw_occurrence_record where data_resource_id=" + id);
		}
	}

	/**
	 * Deletes or marks as deleted all the tables associated with the DR:
	 *   data_resource_agent   
	 *   network_membership
	 *   resource_country
	 *   resource_access_point
	 *   data_resource
	 * @throws Exception
	 */
	protected void deleteDRData() throws Exception {
		selectCountAddDelete("delete" , "from data_resource_agent where data_resource_id=" + id);
		selectCountAddDelete("delete" , "from network_membership where data_resource_id=" + id);
		selectCountAddUpdate("update resource_access_point set deleted=now()" , "from resource_access_point", "where data_resource_id=" + id);
		selectCountAddUpdate("update data_resource set deleted=now()" , "from data_resource", "where id=" + id);
	}
	
	
	/**
	 * Gets a count of the records affected and creates the SQL to issue the delete
	 * The SQL is split into 2 parts to allow a "select count(*)" be prepended to get an estimation of the effect of running the SQL
	 * @param sql1 The first part of the delete e.g. "delete a.*"
	 * @param sql2 The second part of the delete e.g. "from table_a inner join..."
	 */
	protected void selectCountAddDelete(String sql1, String sql2) throws Exception {
		sql.add(sql1 + " " + sql2);
		Connection conn = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    try {
	      conn = getConnection(host, user, password);
	      stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
	      rs = stmt.executeQuery("select count(*) " + sql2);
	      // extract data from the ResultSet scroll from top
	      while (rs.next()) {
	        long count = rs.getLong(1);
	        counts.add("" + count);
	      }	      
	    } catch (Exception e) {
	    	System.err.println("ERROR: " + e.getMessage());
	    } finally {
	      // release database resources
	      try {
	        rs.close();
	        stmt.close();
	        conn.close();
	      } catch (Exception e) {
	      }
	    }
	}
	
	
	/**
	 * Gets a count of the records affected and creates the SQL to issue the update
	 * The SQL is split into 3 parts to allow a "select count(*)" be prepended to get an estimation of the effect of running the SQL
	 * @param sql1 The first part of the update e.g. "update table set a=b"
	 * @param sql2 The first part of the count excluding the select count(*) e.g. "from table"
	 * @param sql3 The common third part of the sql e.g. "where id=123"
	 */
	protected void selectCountAddUpdate(String sql1, String sql2, String sql3) throws Exception {
		sql.add(sql1 + " " + sql3);
		Connection conn = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    try {
	      conn = getConnection(host, user, password);
	      stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
	      rs = stmt.executeQuery("select count(*) " + sql2 + " " + sql3);
	      // extract data from the ResultSet scroll from top
	      while (rs.next()) {
	        long count = rs.getLong(1);
	        counts.add("" + count);
	      }
	      
	    } catch (Exception e) {
	    	System.err.println("ERROR: " + e.getMessage());
	    } finally {
	      // release database resources
	      try {
	        rs.close();
	        stmt.close();
	        conn.close();
	      } catch (Exception e) {
	      }
	    }
	}	
	
	/**
	 * @param args
	 *            The paramaters for execution
	 */
	public static void main(String[] args) {
		try {
			if (args.length == 4) {
				System.out.println("Generating deletion script for Data Resource ID: "+ args[3]);
				DeleteResourceSQL app = new DeleteResourceSQL(args[0], args[1], args[2], args[3], null);
				app.deleteORData();
				app.deleteDRData();
				app.debugSQL();			
				app.writeSQLFile("delete-"+args[3]+".sql");
				
			} else if (args.length == 5) {
				System.out.println("Generating deletion script for Data Resource ID["+ args[3] + "] that have not been updated since["+ args[4] + "]");
				DeleteResourceSQL app = new DeleteResourceSQL(args[0], args[1], args[2], args[3], args[4]);
				app.deleteORData();
				app.debugSQL();			
				app.writeSQLFile("delete-"+args[3]+"-"+args[4]+".sql");
				
			} else {
				System.out.println("Usage: Host User Password DataResourceId [date in yyyy-mm-dd]");
				System.out.println("E.g. rancor.gbif.org root password 123 2007-11-01");
				
			}
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * System.out's the SQL
	 */
	protected void debugSQL() {
		if (sql.size() == counts.size()) {
			for (int i=0; i<sql.size(); i++) {
				System.out.println("-- The following should update/delete " + counts.get(i) + " records");
				System.out.println(sql.get(i) + "\n");
			}
		} else {
			System.out.println("WARNING: there does not appear to be the same number of values for the predicted COUNT of deletion and the SQL statements - suggest investigation!");
			for (int i=0; i<sql.size(); i++) {
				System.out.println(sql.get(i));
			}
			
		}
	}
	
	/**
	 * @param The filename to write the SQL to
	 */
	protected void writeSQLFile(String filename) {
	     try{
	    	    FileWriter fw = new FileWriter(filename);
	    	    BufferedWriter bw = new BufferedWriter(fw);
	    		if (sql.size() == counts.size()) {
					for (int i=0; i<sql.size(); i++) {
						bw.write("-- The following should update/delete " + counts.get(i) + " records\n");
						bw.write(sql.get(i) + ";\n\n");
					}
	    		} else {
	    			 bw.write("WARNING: there does not appear to be the same number of values for the predicted COUNT of deletion and the SQL statements - suggest investigation!\n");
	    			for (int i=0; i<sql.size(); i++) {
	    				bw.write(sql.get(i) + ";\n\n");
	    			}
	    			
	    		}
	    	    bw.close();
	    	    fw.close();
	    }catch (Exception e){
	      System.err.println("Error: " + e.getMessage());
	    }		
	}

	/**
	 * 
	 * @param host DB host
	 * @param user DB user
	 * @param password DB password
	 * @return The DB connection
	 * @throws Exception On error
	 */
	static Connection getConnection(String host, String user, String password) throws Exception {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://" + host + "/portal";
		Class.forName(driver);
		return DriverManager.getConnection(url, user, password);
	}
	
}
