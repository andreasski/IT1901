package backend;
/**
 * Organizer class establish a connection to the database via the ConnectionManager class.
 * Using queries to get data from the database and returns it to the GUI.
 * @author: Heidi Brække, Magnus Eriksson
 */

import java.sql.*;
import java.util.ArrayList;


public class Organizer {

    ArrayList<String> dates = new ArrayList<>();
    ArrayList<String> concerts = new ArrayList<>();
    ArrayList<String> techs = new ArrayList<>();
    String concid;


    public ArrayList<String> getDate(){

        try{
        Statement stmt = new ConnectionManager().connect().createStatement();
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

    public ArrayList<String> getConcerts(String date){
        try{
            Statement stmt = new ConnectionManager().connect().createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT bookingoffer.time, bookingoffer.bandid, band.name, stage.name FROM bookingoffer, band, stage, concert WHERE bookingoffer.bandid = band.idband AND bookingoffer.concertid = concert.idconcert AND concert.stageid = stage.idstage AND bookingoffer.date = \"" + date + "\" ORDER BY bookingoffer.time");

            while ( rs.next() ) {
                String bandname = rs.getString("band.name");
                String stagename = rs.getString("stage.name");
                String time = rs.getString("bookingoffer.time");
                String concert = stagename + " " + bandname  + " " + time ;
                System.out.println(bandname + stagename);
                concerts.add(concert);
                //concerts.add(stagename);
            }
        } catch (Exception e){
            System.err.println("Got an exception2! ");
            System.err.println(e.getMessage());
        } return concerts;
    }

    public ArrayList<String> getTechnicians(String date, String time, String bandId){
        String concertid = getConcerttId(date, time, bandId);
        try {
            Statement stmt = new ConnectionManager().connect().createStatement();
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


    public String getConcerttId(String date, String time, String bandId) {
        try {
            Statement stmt = new ConnectionManager().connect().createStatement();
            ResultSet rs;

            rs = stmt.executeQuery(" SELECT bookingoffer.concertid FROM bookingoffer, band, stage, concert WHERE bookingoffer.bandid = band.idband AND bookingoffer.concertid = concert.idconcert AND concert.stageid = stage.idstage AND bookingoffer.date = \"" + date + "\" AND bookingoffer.time = \"" + time + "\" AND bookingoffer.bandid = \"" + bandId + "\"");
            while ( rs.next() ){
                concid = rs.getString("concertid");
            }
        } catch (Exception e) {
            System.err.println("Got an exception4! ");
            System.err.println(e.getMessage());
        } return concid;

    }

    public static void main (String[]args) {

        Organizer test = new Organizer();
        ArrayList<String> dates = test.getDate();
        ArrayList<String> concerts = test.getConcerts("15.10.2017");
        ArrayList<String> techs = test.getTechnicians("16.10.2017", "18.00-20.00", "3");
        System.out.println(dates);
        System.out.println(concerts);
        System.out.println(techs);
    }


}

