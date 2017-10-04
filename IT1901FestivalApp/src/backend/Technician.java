package backend;

import backend.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;

public class Technician {

    /*
    ArrayList<String> getWork
    * @param: int techId
    *
    * Gets the work for the technican with the following techId.
    */
    public static ArrayList<String> getWork(int techId) {
        ArrayList<String> work = new ArrayList<>();
        try {
            Statement stmt = new ConnectionManager().connect().createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT bookingoffer.date, stage.name, bookingoffer.time FROM bookingoffer, concert, concerttechnician, stage WHERE concerttechnician.technicianid = " + techId + " AND bookingoffer.accepted = 1 AND concert.stageid = stage.idstage AND concerttechnician.concertid = bookingoffer.concertid AND bookingoffer.concertid = concert.idconcert ORDER BY bookingoffer.date");
            while (rs.next()) {
                String name = rs.getString("bookingoffer.date") + " " + rs.getString("stage.name") + " " + rs.getString("bookingoffer.time");
                work.add(name);
            }
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
        return work;
    }
}
