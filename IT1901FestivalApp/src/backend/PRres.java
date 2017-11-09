package backend;

/**
 * @author Magnus Eriksson, Heidi Brække
 *
 * @see backend.ConnectionManager
 */

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PRres {
    ArrayList<Integer> bookedBands;


    public PRres(){
        ConnectionManager.connect();
        bookedBands = new ArrayList<>();
    }

    /**
     * This method gets all the booked bands for the concert matching param concertid.
     * @param concertId
     * @return an ArrayList containg all booked bands for the concert.
     */
    public ArrayList<Integer> getBookedBands(int concertId){
        try {
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT bandid FROM bookingoffer WHERE accepted = 2 AND concertid="+concertId); //Accepted = 2 is confirmed bookingoffers
            while (rs.next()) {
                int bandid = rs.getInt("bandid");

                Integer out = Integer.valueOf(bandid);
                bookedBands.add(out);

            } } catch (Exception e) {
            System.err.println("Got an exception, getBookedBands! ");
            System.err.println(e.getMessage());
        }
     return bookedBands;
    }

    /**
     * This method get details of a band from the database which match param bandid and concertid.
     * @param bandID
     * @param concertID
     * @return String containing band details
     */
    public String getBandDetails(int bandID, int concertID) {
        String bandDetail = "";
        try {
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT person.phone, person.email, concert.sales, band.biography, band.name FROM band INNER JOIN bookingoffer ON bookingoffer.bandid = band.idBand INNER JOIN concert ON bookingoffer.concertid = concert.idconcert INNER JOIN person ON person.idPerson = band.managerid WHERE band.idBand = \""+bandID+"\" AND concert.idconcert = \""+concertID+"\" ");
            while (rs.next()) {
                String bandname = rs.getString("band.name");
                bandDetail = String.format("%s;%s;%s;%s;%s;%s", bandname, rs.getString("person.phone"), rs.getString("person.email"), rs.getString("concert.sales"), rs.getString("band.biography"), "https://www.rullestein.no/"+addLink(bandname)+" ");

            } } catch (Exception e) {
            System.err.println("Got an exception, getBandDetails! ");
            System.err.println(e.getMessage());
        }
        return bandDetail;

    }

    /**
     * This method creates a String to use as part of the link in frontend
     * @param bandname
     * @return String to use in frontend
     */

    public String addLink(String bandname){
        String link = "";
        String[] links;
        if (bandname.contains(" ")){
            links = bandname.split(" ");
            for(int i = 0; i< links.length;i++){
                link += links[i];
            }
        }
        else{
            link = bandname;
        }
        return link;
    }

    /**
     * This method gets all the concertid for the concert which have been accepted from bookingBoss and manager(accepted=2)
     * @return ArrayList containing id for all booked concerts
     */

    public ArrayList<Integer> getConcertIdBooked(){
        ArrayList<Integer> concertsBooked = new ArrayList<>();
        try {
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT concertid FROM bookingoffer WHERE accepted = 2");
            while (rs.next()) {
                int concert = rs.getInt("concertid");
                if (!concertsBooked.contains(concert)) {
                    concertsBooked.add(concert);
                }
            } } catch (Exception e) {
            System.err.println("Got an exception, getConcertIdBooked! ");
            System.err.println(e.getMessage());
        }
        return concertsBooked;
    }


    /**
     * This method get the Concerts name based on its id.
     * @param concertId
     * @return concertname
     */
    public String getConcertName(int concertId){
        String name ="";
        try {
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT name FROM concert WHERE idconcert ="+concertId);
            while (rs.next()) {
                name = rs.getString("name");

            } } catch (Exception e) {
            System.err.println("Got an exception, getConcertIdBooked! ");
            System.err.println(e.getMessage());
        }
        return name;
    }

}
