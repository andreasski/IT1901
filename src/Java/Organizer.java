package Java;

import java.sql.*;



public class Organizer {
    
    public ArrayList<> getConcert(String date){
        ArrayList<String> concerts = new ArrayList<>();
        try {
            String url = " ";
            Connection conn = DriverManager.getConnection(url, "johngu_db", "dbkk12");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM 'bookingoffer'");



        }

        catch
    }

}
