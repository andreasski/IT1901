package backend;
/**
 * Organizer class establish a connection to the database via the ConnectionManager class.
 * Using queries to get data from the database and returns it to the GUI.
 * @author: Heidi Brække, Magnus Eriksson
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Organizer {

    /*
    void init
    *
    * Initiates the required variables and such.
    */
    public void init(){
        ConnectionManager.connect();
    }

    /*
    List<String> getDate
    *
    * Gets the dates from the database.
    */
    public static List<String> getDate(){
        List<String> dates = new ArrayList<>();
        try{
        Statement stmt = ConnectionManager.conn.createStatement();
        ResultSet rs;
        rs = stmt.executeQuery("SELECT date FROM bookingoffer");
        while ( rs.next() ) {
            String name = rs.getString("date");
            dates.add(name);
        }
        } catch (Exception e){
            System.err.println("Got an exception1! ");
            System.err.println(e.getMessage());
        } return dates;
    }

    /*
    List<String> getConcerts
    * @param: String date
    *
    * Gets the concerts on the data.
    */
    public static List<String> getConcerts(String date){
        List<String> concerts = new ArrayList<>();
        try{
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT bookingoffer.time, bookingoffer.bandid, band.name, stage.name FROM bookingoffer, band, stage, concert WHERE bookingoffer.bandid = band.idband AND bookingoffer.concertid = concert.idconcert AND concert.stageid = stage.idstage AND bookingoffer.date = \"" + date + "\" ORDER BY bookingoffer.time");

            while ( rs.next() ) {
                String bandname = rs.getString("band.name");
                String stagename = rs.getString("stage.name");
                String time = rs.getString("bookingoffer.time");
                String concert = stagename + "|" + bandname  + "|" + time ;
                concerts.add(concert);
            }
        } catch (Exception e){
            System.err.println("Got an exception2! ");
            System.err.println(e.getMessage());
        } return concerts;
    }

    /*
    List<String> getTechnicians
    * @param: String date
    * @param: String time
    * @param: String bandId
    *
    * Gets the technicans.
    */
    public static List<String> getTechnicians(String date, String time, String bandId){
        List<String> techs = new ArrayList<>();
        String concertid = getConcertId(date, time, bandId);
        try {
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT person.name FROM person, concerttechnician WHERE concerttechnician.concertid = \"" + concertid + "\" AND concerttechnician.technicianid = person.idPerson");

            while ( rs.next() ){
                String person = rs.getString("name");
                techs.add(person);
            }
        }
        catch (Exception e){
            System.err.println("Got an exception3! ");
            System.err.println(e.getMessage());
        } return techs;
    }

    /*
    List<String> getTechnicians
    * @param: String date
    * @param: String time
    * @param: String bandId
    *
    * Gets the concert that corresponds with the information.
    */
    public static String getConcertId(String date, String time, String bandId) {
        String concId = "";
        try {
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(" SELECT bookingoffer.concertid FROM bookingoffer, band, stage, concert WHERE bookingoffer.bandid = band.idband AND bookingoffer.concertid = concert.idconcert AND concert.stageid = stage.idstage AND bookingoffer.date = \"" + date + "\" AND bookingoffer.time = \"" + time + "\" AND bookingoffer.bandid = \"" + bandId + "\"");
            while ( rs.next() ){
                concId = rs.getString("concertid");
            }
        } catch (Exception e) {
            System.err.println("Got an exception4! ");
            System.err.println(e.getMessage());
        } return concId;

    }
}

