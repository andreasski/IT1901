package backend;

import backend.ConnectionManager;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Bookingansvarlig {

    ArrayList<String> bands;
    ArrayList<String> needs;
    String info;

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
        info = "";
    }

    /*
    ArrayList<String> searchBands
    * @param: String band
    *
    * Searches for the following band.
    */
    public ArrayList<String> searchBands(String band){

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
        try {
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT popularity, salesalbum, salesconcerts FROM band where band.name = \"" +band+ "\"");
            while (rs.next()) {
                int pop = rs.getInt("popularity");
                int sale = rs.getInt("salesalbum");
                int conc = rs.getInt("salesconcerts");
                String pops = Integer.toString(pop);
                String sales =Integer.toString(sale);
                String concs =Integer.toString(conc);
                info += ("Popularitet: " +pops + " Albumsalg: " +sales + " Konsert salgstall: "+concs);
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
        System.out.println(infoting);



    }




}
