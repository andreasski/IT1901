import backend.ConnectionManager;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Bookingansvarlig {

    ArrayList<String> bands = new ArrayList<>();

    public ArrayList<String> searchBands(String band){

        try {
            Statement stmt = new ConnectionManager().connect().createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT name FROM band WHERE name LIKE '%"+band+"%'");
            while (rs.next()) {
                String name = rs.getString("name");
                bands.add(name);
            }
        } catch (Exception e) {
            System.err.println("Got an exception1! ");
            System.err.println(e.getMessage());
        }
        return bands;
    }
}
