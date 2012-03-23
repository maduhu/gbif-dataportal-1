import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * A little test class that will issue some queries and output timestamps
 *
 * @author trobertson
 */
public class Benchmark {
  String filename;

  /**
   * Does the testing
   */
  protected void benchmark() {
    Properties props = new Properties();
    try {
      props.load(Benchmark.class.getResourceAsStream(filename));
    } catch (FileNotFoundException e) {
      System.out.println("File [" + filename + "] not found");
      return;
    } catch (IOException e) {
      System.out.println("Unable to read file: " + filename);
      return;
    }

    Connection conn = null;
    try {
      try {
        conn = getConnection(props);
        System.out.println("Opened the DB connection");

        boolean cont = true;
        int i = 1;
        while (cont) {
          cont = executeQuery(props, i++, conn);
        }
      } catch (Exception e) {
        System.out.println("Unable to open the DB connection");
        e.printStackTrace();
      }
    } finally {
      try {
        conn.close();
        System.out.println("Closed the DB connection");
      } catch (SQLException e) {
      }
    }
  }

  protected boolean executeQuery(Properties props, int queryNumber, Connection conn) {
    String query = (String) props.get("query" + queryNumber + ".sql");
    String explaination = (String) props.get("query" + queryNumber + ".detail");
    if (query != null) {
      try {
        System.out.println(queryNumber + ": " + explaination);
        System.out.println(queryNumber + ": " + query);
        long time = System.currentTimeMillis();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        try {
          System.out.println("First item of first result: " + rs.getObject(1));
        } catch (RuntimeException e) {
          System.out.println("Error receiving first item of result...");
        }
        System.out.println(queryNumber + ": " + (System.currentTimeMillis() - time) + " msecs");
      } catch (SQLException e) {
        System.out.println(queryNumber + ": Has a SQL error!");
        e.printStackTrace();
      }
      return true;
    }
    return false;
  }


  /**
   * @param props File with DB props
   * @return Connection
   * @throws Exception On error
   */
  protected static Connection getConnection(Properties props) throws Exception {
    String driver = props.getProperty("db.driver");
    String url = props.getProperty("db.url");
    String username = props.getProperty("db.user");
    String password = props.getProperty("db.password");
    Class.forName(driver);
    System.out.println("Opening JDBC connection: " + url);
    return DriverManager.getConnection(url, username, password);
  }


  /**
   * @param filename with params for test
   */
  public Benchmark(String filename) {
    this.filename = filename;
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.println("No file supplied, attempting to use mysql.properties");
      Benchmark b = new Benchmark("mysql.properties");
      b.benchmark();

    } else {
      System.out.println("Using configuration file:" + args[0]);
      Benchmark b = new Benchmark(args[0]);
      b.benchmark();
    }
  }
}
