package backend;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class PRres {
    ArrayList<String> bookedBands;


    public PRres(){
        ConnectionManager.connect();
        bookedBands = new ArrayList<>();
    }


    public ArrayList<String> getBookedBands(){
        try {
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT bandid, concertid FROM bookingoffer WHERE accepted = 2"); //Accepted = 2 is confirmed bookingoffers
            while (rs.next()) {
                int bandid = rs.getInt("bandid");
                int concertid = rs.getInt("concertid");
                String out = Integer.toString(bandid) + ";" + Integer.toString(concertid);
                bookedBands.add(out);

            } } catch (Exception e) {
            System.err.println("Got an exception, getBookedBands! ");
            System.err.println(e.getMessage());
        }
     return bookedBands;
    }
    public ArrayList<String> getBandDetails(int bandID, int concertID) {
        ArrayList<String> bandDetails = new ArrayList<>();
        try {
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT person.phone, person.email, concert.sales, band.biography, band.name FROM band INNER JOIN bookingoffer ON bookingoffer.bandid = band.idBand INNER JOIN concert ON bookingoffer.concertid = concert.idconcert INNER JOIN person ON person.idPerson = band.managerid WHERE band.idBand = \""+bandID+"\" AND concert.idconcert = \""+concertID+"\" ");
            while (rs.next()) {
                String bandname = rs.getString("band.name");
                String bandDetail = String.format("%s;%s;%s;%s;%s;%s", bandname, rs.getString("person.phone"), rs.getString("person.email"), rs.getString("concert.sales"), rs.getString("band.biography"), "https://www.rullestein.no/"+bandname+" ");
                bandDetails.add(bandDetail);
            } } catch (Exception e) {
            System.err.println("Got an exception, getBandDetails! ");
            System.err.println(e.getMessage());
        }
        return bandDetails;

    }

    public static void main(String[] args){
        PRres p = new PRres();
        System.out.println(p.getBookedBands());
    }

}
