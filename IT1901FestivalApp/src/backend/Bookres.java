package backend;

import backend.ConnectionManager;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Bookres {

  ArrayList<String> band;
  ArrayList<String> bands;
  ArrayList<String> needs;
  ArrayList<String> genres;
  ArrayList<String> pubscenes;
  ArrayList<String> concerts;

  /*
  Bookingansvarlig
  *
  * Initializes the object.
  */
  public Bookres() {
    ConnectionManager.connect();

    band = new ArrayList<>();
    bands = new ArrayList<String>();
    needs = new ArrayList<String>();
    genres = new ArrayList<String>();
    pubscenes = new ArrayList<>();
    concerts = new ArrayList<>();


  }

     /*
    ArrayList<String> searchBands
    *
    * returns bands
    */


  public ArrayList<String> getBands() {
    try {
      Statement stmt = ConnectionManager.conn.createStatement();
      ResultSet rs;

      rs = stmt.executeQuery("SELECT name FROM band");
      while (rs.next()) {
        String name = rs.getString("name");
        band.add(name);
      } } catch (Exception e) {
      System.err.println("Got an exception, getBand! ");
      System.err.println(e.getMessage());
    }
    return band;
  }

  /*
  ArrayList<String> searchBands
  * @param: String band
  *
  * Searches for the following band.
  */
  public ArrayList<String> searchBands(String band) {
    bands.clear();
    try {
      Statement stmt = ConnectionManager.conn.createStatement();
      ResultSet rs;

      rs = stmt.executeQuery("SELECT name FROM band WHERE name LIKE '%" + band + "%'");
      while (rs.next()) {
        String name = rs.getString("name");
        bands.add(name);
      }
    } catch (Exception e) {
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
  public ArrayList<String> getTechnicalNeeds(String band) {
    needs.clear();
    try {
      Statement stmt = ConnectionManager.conn.createStatement();
      ResultSet rs;

      rs = stmt.executeQuery("SELECT techicalneed.need FROM techicalneed, band WHERE band.name = \"" + band + "\" AND band.idBand = techicalneed.bandid");
      while (rs.next()) {
        String need = rs.getString("techicalneed.need");
        needs.add(need);
      }
    } catch (Exception e) {
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
  public String getInfoBand(String band) {
    String info = "";
    try {
      Statement stmt = ConnectionManager.conn.createStatement();
      ResultSet rs;
      rs = stmt.executeQuery("SELECT popularity, salesalbum, salesconcerts FROM band where band.name = \"" + band + "\"");


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
  public ArrayList<String> getPreviousConcerts(String band) {
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

     /*
    String getGenre
    *
    * Gets a list of the different genres.
    */

  public List<String> getGenre() {
    List<String> genres = new ArrayList<>();
    try {
      Statement stmt = ConnectionManager.conn.createStatement();
      ResultSet rs;
      rs = stmt.executeQuery("SELECT name FROM genre");

      while (rs.next()) {
        String genre = rs.getString("name");
        genres.add(genre);
      }
    } catch (Exception e) {
      System.err.println("Got an exception7! ");
      System.err.println(e.getMessage());
    }
    return genres;
  }


    /*
    String getGenre
    * @param: String genre
    *
    * Gets an overview over audience and stage info from previous concerts.
    */

  public ArrayList<String> getPubScene(String genre) {
    try {
      Statement stmt = ConnectionManager.conn.createStatement();
      ResultSet rs;
      rs = stmt.executeQuery("SELECT DISTINCT stage.name, stage.capacity, concert.sales FROM genre INNER JOIN genreband ON genreband.genreid = genre.idGenre INNER JOIN band ON band.idBand = genreband.bandid INNER JOIN bookingoffer ON bookingoffer.bandid = band.idBand INNER JOIN concert ON concert.idconcert = bookingoffer.concertid INNER JOIN stage ON stage.idstage = concert.stageid WHERE genre.name = \"" + genre + "\" AND bookingoffer.accepted > 1");

      while (rs.next()) {
        String stage = rs.getString("name");
        String capacity = rs.getString("capacity");
        String sales = rs.getString("sales");
        String pubscene = (stage + ";" + capacity + ";" + sales);
        pubscenes.add(pubscene);
      }
    } catch (Exception e) {
      System.err.println("Got an exception8! ");
      System.err.println(e.getMessage());
    }
    return pubscenes;
  }

  public void addBookingOffer(int bandId, int concertId, String date, String time, int expence) {
    try {
      Statement stm = ConnectionManager.conn.createStatement();
      ResultSet rs;
      String str = String.format("Insert Into bookingoffer(bandid, concertid, date, time, expense) Values ('%d', '%d', '%s', '%s', %d)", bandId, concertId, date, time, expence);
      stm.executeUpdate(str);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public ArrayList<String> getConcerts() {

    try {
      Statement stm = ConnectionManager.conn.createStatement();
      ResultSet rs;
      rs = stm.executeQuery("SELECT idconcert, name FROM concert");
      while (rs.next()) {
        String idconcert = rs.getString("idconcert");
        String name = rs.getString("name");
        String out = idconcert +":"+ name;
        concerts.add(out);
      }

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return concerts;
  }

}