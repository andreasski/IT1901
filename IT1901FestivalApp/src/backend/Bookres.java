package backend;

import backend.ConnectionManager;
import com.sun.istack.internal.localization.NullLocalizable;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.System.out;
import static java.lang.System.setOut;

public class Bookres {

  ArrayList<String> bands;
  ArrayList<String> needs;
  ArrayList<String> concerts;

  /*
  Bookingansvarlig
  *
  * Initializes the object.
  */
  public Bookres() {
    ConnectionManager.connect();

    bands = new ArrayList<String>();
    needs = new ArrayList<String>();
    concerts = new ArrayList<>();
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

  public List<String> searchConcerts(String concert) {
    List concerts = new ArrayList();
    try {
      Statement stmt = ConnectionManager.conn.createStatement();
      ResultSet rs;

      rs = stmt.executeQuery("SELECT name FROM concert WHERE name LIKE '%" + concert + "%'");
      while (rs.next()) {
        String name = rs.getString("name");
        concerts.add(name);
      }
    } catch (Exception e) {
      System.err.println("Got an exception123! ");
      System.err.println(e.getMessage());
    }
    return concerts;
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

      String str = String.format("SELECT concert.name AS cname, stage.name AS sname, concert.date, concert.sales, concert.price, concert.expenses, concert.earnings FROM bookingoffer INNER JOIN band ON band.idBand = bookingoffer.bandid INNER JOIN concert ON concert.idconcert = bookingoffer.concertid INNER JOIN stage ON stage.idstage = concert.stageid WHERE accepted > 1 AND band.name = 'Bølgeband'", band);
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

  public List<String> getPubScene(String genre) {
    List<String> pubscenes = new ArrayList<>();
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

  public String addBookingOffer(String time, String concertName, String bandName, int expense) {
    try {
      Statement stm = ConnectionManager.conn.createStatement();
      ResultSet rs;
      String strBID = String.format("SELECT idBand FROM band WHERE band.name = '%s'", bandName);
      String strCID = String.format("SELECT idconcert FROM concert WHERE concert.name = '%s'", concertName);

      rs = stm.executeQuery(strBID);
      if (!rs.next() ) {
        return "Bandet " + bandName + " eksisterer ikke i databasen";
      }
      int bandID = rs.getInt("idBand");
      rs = stm.executeQuery(strCID);
      if (!rs.next()) {
        return "Konserten " + concertName + " eksisterer ikke i databasen";
      }
      int concID = rs.getInt("idconcert");
      if (!validateDateTime(time, concertName)){
        return "Det er allerede en konsert innenfor dette tidsrommet.";
      }
      String str = String.format("Insert Into bookingoffer(bandid, concertId, time, expense) Values ('%s', '%s', '%s', %d)", bandID, concID, time, expense);
      stm.executeUpdate(str);

    } catch (Exception e) {
      out.println(e.getMessage());
    } return "Booking tilbud opprettet";
  }

  public boolean validateDateTime(String time, String concertName) {
    List<String> timeList = new ArrayList<>();
    String[] splitDash = time.split("-");
    String startin = splitDash[0];
    String endin = splitDash[1];
    LocalTime startTidIn = LocalTime.parse(startin);
    LocalTime sluttTidIn = LocalTime.parse(endin);
    try {
      Statement stmt = ConnectionManager.conn.createStatement();
      ResultSet rs;
      rs = stmt.executeQuery("SELECT time FROM bookingoffer INNER JOIN concert ON concert.idconcert = bookingoffer.concertid WHERE concert.name = \"" + concertName + "\"");
      while (rs.next()) {
        String times = rs.getString("time");
        timeList.add(times);
      }
      for (String t: timeList){
        String[] splitDashs = t.split("-");
        String start = splitDashs[0];
        String end = splitDashs[1];
        LocalTime startTid = LocalTime.parse(start);
        LocalTime sluttTid = LocalTime.parse(end);
        if((startTidIn.isAfter(startTid) && startTidIn.isBefore(sluttTid))
                || startTidIn.equals(startTid)
                || sluttTidIn.equals(sluttTid)
                || (sluttTidIn.isBefore(sluttTid) && sluttTidIn.isAfter(startTid)))
        {
          return false;
        }
      }}

    catch (Exception e) {
      System.err.println("Got an exceptionTime! ");
      System.err.println(e.getMessage());
      return false;
    }
    return true;
  }


  public boolean doesBandExist(String bandName) {
    try {
      Statement stmt = ConnectionManager.conn.createStatement();
      ResultSet rs;
      rs = stmt.executeQuery("SELECT name FROM band");
      while (rs.next()) {
        if (rs.getString("name").equals(bandName)) {
          return true;
        }
      } return false;
    }
    catch (Exception e) {
      System.err.println("Got an exceptionband! ");
      System.err.println(e.getMessage());
      return false;
    }
  }

  public static void main(String[]args){
    Bookres test = new Bookres();
    System.out.println(test.validateDateTime("10:05-10:30", "partykonsert"));
  }

}