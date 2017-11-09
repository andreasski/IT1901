package backend;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static oracle.jrockit.jfr.events.Bits.intValue;

public class Serving {

    public Serving() { ConnectionManager.connect(); }

    //private String date;
    //private ArrayList<String> allDates;
    private ArrayList<String> concerts;
    private ArrayList<String> time;
    private ArrayList<Integer> popularity;
    private HashMap<String, ArrayList<String>> genre = new HashMap<>();
    private int estAudience;
    private int capacity;
    private String price;
    private String sales;
    private String expenses;
    private String earnings;
    private HashMap<String, Integer> estBooze = new HashMap<>();

    //public String getDate() { return date; }
    public ArrayList<String> getTime() { return time; }
    public ArrayList<Integer> getPopularity() { return popularity; }
    public HashMap<String, ArrayList<String>> getGenre() { return genre; }
    public int getEstAudience() { return estAudience; }
    public int getCapacity() { return capacity; }
    public String getPrice() { return price; }
    public String getSales() { return sales; }
    public String getExpenses() { return expenses; }
    public String getEarnings() { return earnings; }
    public HashMap<String, Integer> getEstBooze() { return estBooze; }


    public ArrayList<String> getAllDates() {

        ArrayList<String> allDates = new ArrayList<>();
        try{
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT date FROM concert;");


            while (rs.next()) {
                allDates.add(rs.getString("concert.date"));
            }

        } catch (Exception e) {
            System.err.println("Got an exception1! ");
            System.err.println(e.getMessage());
        }

        return allDates;
    }


    public ArrayList<String> getConcerts(String date) {

        try{
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT concert.name FROM concert WHERE concert.date = '" + date + "';");

            while (rs.next()) {
                concerts.add(rs.getString("concert.name"));
            }

        } catch (Exception e) {
            System.err.println("Got an exception2! ");
            System.err.println(e.getMessage());
        }
        return concerts;
    }


    public void getInfo(String concert) {

        try{
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT date, time, capacity, popularity, band.name, genre.name FROM bookingoffer, stage, band, genre, genreband, concert WHERE concert.name = '" + concert + "' AND concert.idconcert = bookingoffer.concertid AND concert.stageid = stage.idstage AND bookingoffer.bandid = band.idBand AND band.idBand = genreband.bandid AND genreband.genreid = genre.idGenre ORDER BY time;");

            while (rs.next()) {
                //date = rs.getString("bookingoffer.date");
                capacity = rs.getInt("stage.capacity");
                String addTime = rs.getString("bookingoffer.time");
                String addBand1 = rs.getString("band.name");
                String addGenre = rs.getString("genre.name");
                int addPopularity = rs.getInt("band.popularity");

                if (genre.containsKey(addBand1)) {
                    genre.get(addBand1).add(addGenre);
                } else {
                    genre.put(addBand1, new ArrayList<>());
                    genre.get(addBand1).add(addGenre);
                }

                if (time != null && !time.isEmpty()) {
                    String lastTime = time.get(time.size() - 1);
                    if (!lastTime.equals(addTime)) {
                        time.add(addTime);
                        popularity.add(addPopularity);
                    }
                }
                else {
                    time.add(addTime);
                    popularity.add(addPopularity);
                }
            }

        } catch (Exception e) {
            System.err.println("Got an exception3! ");
            System.err.println(e.getMessage());
        }

        for (int i = 0; i < popularity.size(); i++) {
            estAudience += Math.round(popularity.get(i)*0.1);
        }
        if (estAudience > capacity) {
            estAudience = capacity;
        }
    }

    public void setEstBooze() {

        double olEst = 0.75*estAudience;
        double vinEst = 0.2*estAudience;
        double mineralEst = 0.5*estAudience;

        estBooze.put("Øl", (int) olEst);
        estBooze.put("Vin", (int) vinEst);
        estBooze.put("Mineralvann", (int) mineralEst);

    }

}
