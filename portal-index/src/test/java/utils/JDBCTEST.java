package utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCTEST {
	  public static Connection getConnection() throws Exception {
	    String driver = "org.gjt.mm.mysql.Driver";
	    String url = "jdbc:mysql://aenetus/portal";
	    String username = "root";
	    String password = "aAg85kj";
	    Class.forName(driver);
	    return DriverManager.getConnection(url, username, password);
	  }

	  public static void main(String[] args) {
	    Connection conn = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    try {
	      conn = getConnection();
	      String query = "select o.* from occurrence_record o";
	      stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
	      stmt.setFetchSize(Integer.MIN_VALUE);	      
	      rs = stmt.executeQuery(query);
	      
	      // extract data from the ResultSet scroll from top
	      int i=0;
	      long time = System.currentTimeMillis();
	      long startTime = System.currentTimeMillis();
	      while (rs.next()) {
	    	  i++;
	    	  
	        //long id = rs.getLong(1);
	        //System.out.println("id=" + id);
	    	  if (i%1000000 == 0) {
	    		  System.out.println("1,000,000 records returned in: " + (1+(System.currentTimeMillis() - time)/1000) + " secs. Total records returned: " + i);	    		  
	    		  time = System.currentTimeMillis();
	    	  }
	      }
		  System.out.println("All records returned in: " + (1+(System.currentTimeMillis() - startTime)/1000) + " secs. Total records returned: " + i);	    		  
	      
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

