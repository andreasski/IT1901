package backend;
/**
 * Organizer class establish a connection to the database via the ConnectionManager class.
 * Using queries to get data from the database and returns it to the GUI.
 * @author: Heidi Brække, Magnus Eriksson, Andreas Skifjeld
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class Organizer {

    /**
    void init
    *
    * Initiates the required variables and such.
    */
    public static void init(){
        ConnectionManager.connect();
    }

    /**
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

    /**
     * addConcert
     * @param concertName
     * @param date
     * @param stageName
     * @return
     */
    public static String addConcert(String concertName, String date, String stageName) {
        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;
            String str1 = String.format("SELECT idstage FROM stage WHERE stage.name = '%s'", stageName);
            rs = stm.executeQuery(str1);

            int stageId;

            if (!rs.next()) {
                return "Scenen finnes ikke i databasen";
            } else {
                stageId = rs.getInt("idstage");
            }

            String str = String.format("Insert Into concert(name, date, stageid) Values ('%s', '%s', %d)", concertName, date, stageId);
            stm.executeUpdate(str);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } return "Konsert opprettet";
    }

    /**
     * getStage
     * @param stage
     * @return List of stages
     */
    public static List<String> getStage(String stage) {
        List<String> stages = new ArrayList();
        try {
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT name FROM stage WHERE name LIKE '%" + stage + "%'");
            while (rs.next()) {
                String name = rs.getString("name");
                stages.add(name);
            }
        } catch (Exception e) {
            System.err.println("Got an exception123! ");
            System.err.println(e.getMessage());
        }
        return stages;
    }

    /**
    List<String> getConcerts
    * @param: String date
    *
    * Gets the concerts on the date in the input.
    */
    public static List<String> getConcerts(String date){
        List<String> concerts = new ArrayList<>();
        try{
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(String.format("SELECT stage.name, concert.name  FROM stage, concert WHERE concert.date = '%s' AND concert.stageid = stage.idstage ORDER BY concert.name, stage.name", date));
            while ( rs.next() ) {
                String stagename = rs.getString("stage.name");
                String concertName = rs.getString("concert.name");
                String concert = stagename + ";" + concertName;
                concerts.add(concert);
            }
        } catch (Exception e){
            System.err.println("Got an exception2! ");
            System.err.println(e.getMessage());
        } return concerts;
    }

    /**
    List<String> getTechnicians
    * @param: String concertName
    *
    * Gets the technicans.
    */
    public static List<String> getTechnicians(String concertName){
        List<String> techs = new ArrayList<>();
        try {
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(String.format("SELECT person.name FROM person, concerttechnician, concert WHERE concert.name = '%s' AND concerttechnician.concertid = concert.idconcert AND concerttechnician.technicianid = person.idPerson", concertName));

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

    /**
     * getTechnicalNeeds
     * @param concertName
     * @return a list of technical needs for the concert in the input.
     */
    public static ArrayList<String> getTechnicalNeeds(String concertName) {
        Set<String> needsSet = new HashSet<>();
        ArrayList<String> needs = new ArrayList<>();
        try {
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT techicalneed.need FROM techicalneed, concert, bookingoffer, band WHERE concert.name = \"" + concertName + "\" AND concert.idconcert = bookingoffer.concertid AND bookingoffer.accepted >= 2 AND bookingoffer.bandid = band.idBand AND band.idBand = techicalneed.bandid ");
            while (rs.next()) {
                String need = rs.getString("techicalneed.need");
                needsSet.add(need);
            }
            needs = new ArrayList<>(needsSet);
            needs.sort(String::compareToIgnoreCase);
        } catch (Exception e) {
            System.err.println("Got an exception2! ");
            System.err.println(e.getMessage());
        } return needs;
    }

    /**
     * getBookingOffers
     * @param concertName
     * @return a list with name og band and time of the concert
     */
    public static List<String> getBookingOffers(String concertName) {
        List<String> bookingOffers = new ArrayList<>();
        try {
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(String.format("SELECT bookingoffer.time, band.name FROM concert, bookingoffer, band WHERE concert.name = '%s' AND concert.idconcert = bookingoffer.concertid AND bookingoffer.accepted >= 2 AND bookingoffer.bandid = band.idBand ORDER BY bookingoffer.time", concertName));
            while (rs.next()) {
                String band = rs.getString("band.name");
                String time = rs.getString("bookingoffer.time");
                bookingOffers.add(band + ";" + time);
            }
        } catch (Exception e) {
            System.err.println("Got an exception2312412312! ");
            System.err.println(e.getMessage());
        } return bookingOffers;
    }
}


