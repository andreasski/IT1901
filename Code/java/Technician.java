import java.sql.*;
import java.util.ArrayList;

public class Technician {
    ArrayList<String> dates = new ArrayList<>();
    ArrayList<String> stages = new ArrayList<>();
    ArrayList<String> needs = new ArrayList<>();

    public ArrayList<String> getDate() {

        try {
            Statement stmt = new ConnectionTing().connect().createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT date FROM bookingoffer");

            while (rs.next()) {
                String name = rs.getString("date");
                dates.add(name);
            }
        } catch (Exception e) {
            System.err.Println("Got an exception!");
            System.err.Println(e.getMessage());
        }
        return dates;
    }


    public ArrayList<String> getStage() {

        try {
            Statement stmt = new ConnectionTing().connect().createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT * FROM bookingoffer INNER JOIN concert on concert.idconcert = bookingoffer.concertid INNER JOIN stage on stage.idstage = concert.stageid");

            while (rs.next()) {
                String name = rs.getString("name");
                stages.add(name);
            }
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
        return stages;
    }

    public ArrayList<String> getNeed() {

        try {
            Statement stmt = new ConnectionTing().connect().createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("");

            while (rs.next()) {
                String name = rs.getString("");
                needs.add(name);
            }
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
        return needs;
    }

}
