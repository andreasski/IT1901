package backend;
/**
 * ConnectionManager establish a connection to the server and returns it via ConnectionManager.Connect().
 * @author: Heidi Brække, Magnus Eriksson
 * source: https://alvinalexander.com/java/edu/pj/jdbc/jdbc0003
 */

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager {

    public static Connection conn;

    /**
    Connection connect
    *
    * Connects to the database.
    */
    public static Connection connect(){
        try {
            String url = "jdbc:mysql://mysql.stud.ntnu.no:3306/johngu_dbkonsert";
            conn = DriverManager.getConnection(url, "johngu_db", "dbkk12");

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return conn;
    }
}
