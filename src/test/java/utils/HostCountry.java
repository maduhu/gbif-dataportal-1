package utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HostCountry {
	  public static Connection getConnection() throws Exception {
	    String driver = "org.gjt.mm.mysql.Driver";
	    String url = "jdbc:mysql://localhost/portal";
	    String username = "root";
	    String password = "password";
	    Class.forName(driver);
	    return DriverManager.getConnection(url, username, password);
	  }

	  public static void main(String[] args) {
	    Connection conn = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    try {
	      conn = getConnection();
	      String query = "select * from repatriase";
	      stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
	      stmt.setFetchSize(Integer.MIN_VALUE);	      
	      rs = stmt.executeQuery(query);
	      
	      // Country<HostCountry<Count>>
	      Map<String, Map<String, Integer>> data = new HashMap<String, Map<String, Integer>>();
	      Set<String> hosts = new HashSet<String>();
	      Set<String> countries = new HashSet<String>();
	      while (rs.next()) {
	    	  String host = rs.getString(1);
	    	  if (host == null) {
	    		  host = "--";
	    	  }
	    	  String country = rs.getString(2);
	    	  if (country == null) {
	    		  country = "--";
	    	  }
	    	  int count = rs.getInt(3);
	    	  hosts.add(host);
	    	  countries.add(country);
	    	  
	    	  Map<String, Integer> countryCount = null;
	    	  if (data.containsKey(country)) {
	    		  countryCount = data.get(country);
	    	  } else {
	    		  countryCount = new HashMap<String, Integer>();
	    		  data.put(country, countryCount);	    		  
	    	  }
	    	  countryCount.put(host, count);
	      }
	      
	      List<String> orderedHosts = new LinkedList<String>();
	      orderedHosts.addAll(hosts);
	      Collections.sort(orderedHosts);
	      List<String> orderedCountries = new LinkedList<String>();
	      orderedCountries.addAll(countries);
	      Collections.sort(orderedCountries);
	      
	      System.out.print("  ,");
	      int i = 0;
	      for (String host : orderedHosts) {
	    	  i++;
	    	  System.out.print(host);
	    	  if (i<orderedHosts.size())
	    		  System.out.print(",");
	      }
	      System.out.print("\n");
	      
	      for (String countryCode : orderedCountries) {
	    	  System.out.print(countryCode);
	    	  System.out.print(",");
	    	  Map<String, Integer> countryCount = data.get(countryCode);
	    	  i = 0;
	    	  for (String hostCode : orderedHosts) {
	    		  i++;
	    		  if (countryCount.containsKey(hostCode)) {
	    	    	  System.out.print(countryCount.get(hostCode));
	    		  } else {
	    	    	  System.out.print("0");	    			  
	    		  }
		    	  if (i<orderedHosts.size())
		    		  System.out.print(",");
	    		  
	    	  }   
	    	  
	    	  System.out.print("\n");	    	  	    	  
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

