package backend;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Serving {

    public Serving() { ConnectionManager.connect(); }

    private String date;
    private ArrayList<String> time = null;
    private ArrayList<Integer> popularity = null;
    private HashMap<String, String> genre = new HashMap<>();
    private int estAudience;
    private int capacity;
    private String price;
    private String sales;
    private String expenses;
    private String earnings;
    private HashMap<String, Double> estBooze = new HashMap<>();

    public String getDate() { return date; }
    public ArrayList<String> getTime() { return time; }
    public ArrayList<Integer> getPopularity() { return popularity; }
    public HashMap<String, String> getGenre() { return genre; }
    public int getEstAudience() { return estAudience; }
    public int getCapacity() { return capacity; }
    public String getPrice() { return price; }
    public String getSales() { return sales; }
    public String getExpenses() { return expenses; }
    public String getEarnings() { return earnings; }
    public HashMap<String, Double> getEstBooze() { return estBooze; }

    public void getInfo(String concert) {

        try{
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT date, time, capacity, popularity, band.name, genre.name FROM bookingoffer, stage, band, genre, genreband, concert WHERE concert.name = '" + concert + "' AND concert.idconcert = bookingoffer.concertid AND concert.stageid = stage.idstage AND bookingoffer.bandid = band.idBand AND band.idBand = genreband.bandid AND genreband.genreid = genre.idGenre;");

            while (rs.next()) {
                date = rs.getString("bookingoffer.date");
                capacity = rs.getInt("stage.capacity");
                String addTime = rs.getString("bookingoffer.time");
                String addBand = rs.getString("band.name");
                String addGenre = rs.getString("genre.name");
                genre.put(addBand, addGenre);
                int addPopularity = rs.getInt("band.popularity");

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
            System.err.println("Got an exception1! ");
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

        estBooze.put("Øl", 0.75*estAudience);
        estBooze.put("Vin", 0.2*estAudience);
        estBooze.put("Mineralvann", 0.5*estAudience);

    }

}
