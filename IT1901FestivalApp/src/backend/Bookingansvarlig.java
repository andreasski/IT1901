package backend;

import backend.ConnectionManager;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Bookingansvarlig {

    ArrayList<String> bands = new ArrayList<>();
    ArrayList<String> needs = new ArrayList<>();
    String info = "";

    public ArrayList<String> searchBands(String band){

        try {
            Statement stmt = new ConnectionManager().connect().createStatement();
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

    public ArrayList<String> getTechnicalNeeds( String band){
        try {
            Statement stmt = new ConnectionManager().connect().createStatement();
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
    public String getInfoBand( String band) {
        try {
            Statement stmt = new ConnectionManager().connect().createStatement();
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
