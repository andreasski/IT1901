package backend;
/**
 * Organizer class establish a connection to the database via the ConnectionManager class.
 * Using queries to get data from the database and returns it to the GUI.
 * @author: Heidi Brække, Magnus Eriksson
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Organizer {

    /*
    void init
    *
    * Initiates the required variables and such.
    */
    public static void init(){
        ConnectionManager.connect();
    }

    /*
    List<String> getDate
    *
    * Gets the dates from the database.
    */
    public static List<String> getDate(){
        Set<String> dateSet = new HashSet<>();

        try{
        Statement stmt = ConnectionManager.conn.createStatement();
        ResultSet rs;
        rs = stmt.executeQuery("SELECT date FROM concert ORDER BY concert.date");
        while ( rs.next() ) {
            String name = rs.getString("date");
            dateSet.add(name);
        }
        } catch (Exception e){
            System.err.println("Got an exception1! ");
            System.err.println(e.getMessage());
        }

        //Sort list
        List<String> dates = new ArrayList<>(dateSet);
        dates = radixSort(dates);
        return dates;
    }

    public static List<String> radixSort(List<String> dates) {
        String[] pos =  {"0.2", "3.5", "6.10"};
        List<String> sortedDates = dates;
        for (int i = 0; i < 3; i++) {
            String[] inpPos = pos[i].split("\\.");
            for (int j = 0; j < dates.size(); j++) { sortedDates = mergeSort(sortedDates, Integer.parseInt(inpPos[0]), Integer.parseInt(inpPos[1]));}
        } return sortedDates;
    }

    public static List<String> mergeSort(List<String> dates, int start, int end) {
        if (dates.size() > 1) {
            List<String> l1 = mergeSort(dates.subList(0, (dates.size() / 2)), start, end);
            List<String> l2 = mergeSort(dates.subList(dates.size() / 2, dates.size()), start, end);

            int l1Head = 0;
            int l2Head = 0;
            List<String> returnList = new ArrayList<>();

            for (int i = 0; i < ((l1.size() + l2.size())); i++) {
                if (l1Head < l1.size() && l2Head < l2.size()) {
                    if (Integer.parseInt(l1.get(l1Head).substring(start, end)) <= Integer.parseInt(l2.get(l2Head).substring(start, end))) {
                        returnList.add(l1.get(l1Head));
                        l1Head++;
                    } else {
                        returnList.add(l2.get(l2Head));
                        l2Head++;
                    }
                }
            }
            returnList.addAll((l1Head < l1.size()) ? l1.subList(l1Head, l1.size()) : l2.subList(l2Head, l2.size()));
            return returnList;
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
            rs = stmt.executeQuery(String.format("SELECT bookingoffer.time, bookingoffer.bandid, band.name, stage.name, concert.name  FROM bookingoffer, band, stage, concert WHERE concert.date = '%s' AND  bookingoffer.concertid = concert.idconcert AND bookingoffer.bandid = band.idband AND bookingoffer.concertid = concert.idconcert  AND concert.stageid = stage.idstage AND bookingoffer.accepted > 1  ORDER BY concert.name, stage.name, bookingoffer.time", date));

            while ( rs.next() ) {
                String bandname = rs.getString("band.name");
                String stagename = rs.getString("stage.name");
                String time = rs.getString("bookingoffer.time");
                String concertName = rs.getString("concert.name");
                String concert = stagename + ";" + bandname  + ";" + time + ";" + concertName;
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
    public static List<String> getTechnicians(String date, String bandName, String time){
        List<String> techs = new ArrayList<>();
        String concertid = getConcertId(date, bandName, time);
        try {
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT person.name FROM person, concerttechnician WHERE concerttechnician.concertid = '" + concertid + "' AND concerttechnician.technicianid = person.idPerson");

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
    public static String getConcertId(String date, String bandName, String time) {
        String concId = "";
        try {
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(String.format("SELECT bookingoffer.concertid FROM bookingoffer, band, stage, concert WHERE bookingoffer.bandid = band.idband AND bookingoffer.concertid = concert.idconcert AND concert.stageid = stage.idstage AND concert.date = '%s' AND bookingoffer.time = '%s' AND band.name = '%s' AND bookingoffer.accepted > 1;", date, time, bandName));
            while ( rs.next() ){
                concId = rs.getString("concertid");
            }
        } catch (Exception e) {
            System.err.println("Got an exception4! ");
            System.err.println(e.getMessage());
        } return concId;

    }
    public ArrayList<String> getTechnicalNeeds(String band) {
        ArrayList<String> needs= new ArrayList<>();
        try {
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT techicalneed.need FROM techicalneed, band WHERE band.name = \"" + band + "\" AND band.idBand = techicalneed.bandid");
            while (rs.next()) {
                String need = rs.getString("techicalneed.need");
                needs.add(need);
            }
        } catch (Exception e) {
            System.err.println("Got an exception2! ");
            System.err.println(e.getMessage());
        }
        return needs;

    }
}


