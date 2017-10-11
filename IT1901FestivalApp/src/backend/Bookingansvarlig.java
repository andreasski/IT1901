package backend;

import backend.ConnectionManager;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Bookingansvarlig {

    ArrayList<String> bands;
    ArrayList<String> needs;

    /*
    Bookingansvarlig
    *
    * Initializes the object.
    */
    public  Bookingansvarlig()
    {
        ConnectionManager.connect();

        bands = new ArrayList<String>();
        needs = new ArrayList<String>();
    }

    /*
    ArrayList<String> searchBands
    * @param: String band
    *
    * Searches for the following band.
    */
    public ArrayList<String> searchBands(String band){
        bands.clear();
        try {
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT name FROM band WHERE name LIKE '%"+band+"%'");
            while (rs.next()) {
                String name = rs.getString("name");
                bands.add(name);
            } } catch (Exception e) {
            System.err.println("Got an exception1! ");
            System.err.println(e.getMessage());
        }
        return bands;
    }

    /*
    ArrayList<String> getTechnicalNeeds
    * @param: String band
    *
    * Gets the technical need for the following band.
    */
    public ArrayList<String> getTechnicalNeeds( String band){
        needs.clear();
        try {
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT techicalneed.need FROM techicalneed, band WHERE band.name = \""+band+"\" AND band.idBand = techicalneed.bandid");
            while (rs.next()) {
                String need = rs.getString("techicalneed.need");
                needs.add(need);
            } } catch (Exception e) {
            System.err.println("Got an exception2! ");
            System.err.println(e.getMessage());
        }
        return needs;

    }

    /*
    String getInfoBand
    * @param: String band
    *
    * Gets the information about the following band.
    */
    public String getInfoBand( String band) {
        String info = "";
        try {
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT popularity, salesalbum, salesconcerts FROM band where band.name = \"" +band+ "\"");


            while (rs.next()) {
                int pop = rs.getInt("popularity");
                int sale = rs.getInt("salesalbum");
                int conc = rs.getInt("salesconcerts");
                info = (pop + ";" + sale + ";" + conc);
            }
        } catch (Exception e) {
            System.err.println("Got an exception2! ");
            System.err.println(e.getMessage());
        }
        return info;
    }

    /*
    String getPreviosConcerts
    * @param: String band
    *
    * Gets a list of the previous concerts of a band.
    */
    public ArrayList<String> getPreviousConcerts(String band)
    {
        ArrayList<String> info = new ArrayList<String>();
        try {
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;

            String str = String.format("SELECT concert.name AS cname, stage.name AS sname, bookingoffer.date, concert.sales, concert.price, concert.expenses, concert.earnings FROM bookingoffer INNER JOIN band ON band.idBand = bookingoffer.bandid INNER JOIN concert ON concert.idconcert = bookingoffer.concertid INNER JOIN stage ON stage.idstage = concert.stageid WHERE accepted > 1 AND band.name = 'Bølgeband'", band);
            rs = stmt.executeQuery(str);
            while (rs.next()) {
                String scon = String.format("%s;%s;%s;%s;%s;%s;%s", rs.getString("cname"), rs.getString("sname"), rs.getString("date"), rs.getString("sales"), rs.getString("price"), rs.getString("expenses"), rs.getString("earnings"));

                info.add(scon);
            }
        } catch (Exception e) {
            System.err.println("Got an exception2! ");
            System.err.println(e.getMessage());
        }
        return info;

    }

    public static void main(String[] args){
        Bookingansvarlig test = new Bookingansvarlig();
        String infoting = test.getInfoBand("bølgeband");
        ArrayList<String> infoconc = test.getPreviousConcerts("bølgeband");
        System.out.println(infoting);
        System.out.println(infoconc);



    }




}
