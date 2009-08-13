package org.gbif.portal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple Utility for killing long running mysql queries.
 * 
 * @author dmartin
 */
public class LongQueryKiller {
	
  public static Connection getConnection(String host, String port, String instanceName, String username, String password) throws Exception {
    String driver = "org.gjt.mm.mysql.Driver";
    String url = "jdbc:mysql://"+host+":"+port+"/"+instanceName;
    Class.forName(driver);
    return DriverManager.getConnection(url, username, password);
  }
  
  public static void main(String[] args) {

	if(args.length<6){
		System.out.println("LongProcessKiller <db-host> <db-port> <instance-name> <username> <password> <max-process-length-in-secs> [ids to ignores]");
		return;
	}		  
	  
	String host = args[0];
	String port = args[1];
	String instanceName = args[2];
	String userName = args[3];
	String password = args[4];
	Integer maxProcessLengthInSecsAsString = null;
	try {
		maxProcessLengthInSecsAsString = Integer.parseInt(args[5]);
	} catch(NumberFormatException e){
		System.out.println("LongProcessKiller <db-host> <db-port> <instance-name> <username> <password> <max-process-length-in-secs>");
		return;
	}
	  
	List<String> ignoreIds = new ArrayList<String>();
	for(int i=6; i<args.length; i++){
		ignoreIds.add(args[i]);
	}
	
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      conn = getConnection(host, port, instanceName, userName, password);
      String query = "show processlist";
      stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
      stmt.setFetchSize(Integer.MIN_VALUE);	      
      rs = stmt.executeQuery(query);
      
      List<String> processesToKill = new ArrayList<String>();
      
      // extract data from the ResultSet scroll from top
      while (rs.next()) {
    	  
    	  //| Id | User | Host                     | db     | Command | Time | State    | Info
    	  String processId = rs.getString(1);
    	  String user = rs.getString(2);
    	  Integer timeTaken = rs.getInt(6);
    	  String state = rs.getString(5);
    	  String info = rs.getString(7);
    	  String description = rs.getString(8);
    	  if(info==null)
    		  info="";
		  System.out.println("Process id: "+processId+", User: "+user+", Time taken (secs): "+timeTaken+", State: "+state+", Information: "+info+", Description:"+description);
		  if(timeTaken>maxProcessLengthInSecsAsString 
				  && !ignoreIds.contains(processId) 
				  && description!=null 
				  && description.startsWith("select")
				  && user!=null 
				  && user.equals(userName)){
			  processesToKill.add(processId);
		  }
      }
		rs.close();
		stmt.close();

        for(String processId: processesToKill){
		  Statement killStmt = conn.createStatement();
		  System.out.println("Killing process with id "+processId);
		  int update = killStmt.executeUpdate("kill "+processId);
		  killStmt.close();
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