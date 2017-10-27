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

    public static void main(String[] args){
        PRres p = new PRres();
        System.out.println(p.getBookedBands());
    }

}
