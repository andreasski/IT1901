package backend;

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
    public String getBandDetails(int bandID, int concertID) {
       // ArrayList<String> bandDetails = new ArrayList<>();
        String bandDetail = "";
        try {
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT person.phone, person.email, concert.sales, band.biography, band.name FROM band INNER JOIN bookingoffer ON bookingoffer.bandid = band.idBand INNER JOIN concert ON bookingoffer.concertid = concert.idconcert INNER JOIN person ON person.idPerson = band.managerid WHERE band.idBand = \""+bandID+"\" AND concert.idconcert = \""+concertID+"\" ");
            while (rs.next()) {
                String bandname = rs.getString("band.name");
                bandDetail = String.format("%s;%s;%s;%s;%s;%s", bandname, rs.getString("person.phone"), rs.getString("person.email"), rs.getString("concert.sales"), rs.getString("band.biography"), "https://www.rullestein.no/"+bandname+" ");

            } } catch (Exception e) {
            System.err.println("Got an exception, getBandDetails! ");
            System.err.println(e.getMessage());
        }
        return bandDetail;

    }

    public ArrayList<Integer> getConcertIdBooked(){
        ArrayList<Integer> concertsBooked = new ArrayList<>();
        try {
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT concertid FROM bookingoffer WHERE accepted = 2 ORDER BY date");
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

    public static void main(String[] args){
        PRres p = new PRres();

        String detail = p.getBandDetails(5,6);

        List <String> detailsList = new ArrayList<String>(Arrays.asList(detail.split(";"))); //bandname, phone, email, sales, bio, link
        System.out.println(detailsList);
    }

}
