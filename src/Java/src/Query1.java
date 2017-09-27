//  Query1.java:  Query an mSQL database using JDBC. 

import java.sql.*;

/**
 * A JDBC SELECT (JDBC query) example program.
 */
class Query1 {

    public static void main (String[] args) {
        try {
            String url = "jdbc:mysql://mysql.stud.ntnu.no:3306/johngu_dbkonsert";
            Connection conn = DriverManager.getConnection(url, "johngu_db", "dbkk12");
            Statement stmt = conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT bandid FROM bookingoffers");
            while ( rs.next() ) {
                String lastName = rs.getString("bandid");
                System.out.println(lastName);
            }
            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
}