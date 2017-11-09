package backend;
/**
 * BookingBoss class establish a connection to the database via the ConnectionManager class.
 * Using queries to get data from the database and returns it to the GUI.
 * @author: John Gullaksen
 */

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class BookingBoss {

    public BookingBoss() {
        ConnectionManager.connect();
    }

    /**
     List<String> getConcerts
     *
     * Returns a list of concerts, with id, concert name, date and stage name.
     * @return Returns a list of concerts, with id, concert name, date and stage name.
     */
    public List<String> getConcerts() {
        List<String> ls = new ArrayList<String>();
        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;
            String str = String.format("SELECT DISTINCT concert.idconcert, concert.name AS cname, concert.date, stage.name AS sname FROM bookingoffer INNER JOIN concert ON concert.idconcert = bookingoffer.concertid INNER JOIN stage ON concert.stageid = stage.idstage WHERE bookingoffer.accepted > 1 ORDER BY concert.date");
            rs = stm.executeQuery(str);
            while (rs.next()) {
                String date = rs.getString("date");
                String[] datearr = date.split("\\.");
                LocalDateTime lt = LocalDateTime.now();
                int dy = Integer.parseInt(datearr[0]);
                int mn = Integer.parseInt(datearr[1]);
                int yr = Integer.parseInt(datearr[2]);
                if (yr < lt.getYear() || mn < lt.getMonthValue() || dy < lt.getDayOfMonth()) {
                    String strm = String.format("%s;%s;%s;%s", rs.getString("idconcert"), rs.getString("cname"), date, rs.getString("sname"));
                    ls.add(strm);
                }
            }
        } catch (Exception e) {
            System.err.println("Got an exception2! ");
            System.err.println(e.getMessage());
        } return ls;
    }

    /**
     String getConcert
     * @param: int concertId
     *
     * @return Returns information of the concert specified.
     */
    public String getConcert(int concertId) {
        String res = "";
        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;
            String str = String.format("SELECT concert.name, concert.sales, concert.price, (concert.expenses + ( SELECT SUM(bookingoffer.expense) FROM bookingoffer WHERE bookingoffer.accepted > 1 AND bookingoffer.concertid = concert.idconcert )) AS totalexpenses, (concert.sales * concert.price - (concert.expenses + ( SELECT SUM(bookingoffer.expense) FROM bookingoffer WHERE bookingoffer.accepted > 1 AND bookingoffer.concertid = concert.idconcert ))) AS earnings FROM concert WHERE concert.idconcert = %d", concertId);
            rs = stm.executeQuery(str);
            while (rs.next()) {
                String strm = String.format("%s;%s;%s;%s;%s", rs.getString("name"), rs.getString("sales"), rs.getString("price"), rs.getString("totalexpenses"), rs.getString("earnings"));
                res = strm;
            }
        } catch (Exception e) {
            System.err.println("Got an exception2! ");
            System.err.println(e.getMessage());
        } return  res;
    }

    /**
     int generateTicketPrice
     * @param: int concertId
     *
     * @return Returns a generated ticket price for the specified concert. Takes into consideration expenses.
     */
    public int generateTicketPrice(int concertId) {
        int price = 0;
        int expectedSales = 0;
        double expectedatt = 0.1;
        int totalexpenses = 0;
        int extraPrice = 1;
        int cexp = 0;
        int cap = 0;
        ArrayList<Integer> bexp = new ArrayList<>();
        ArrayList<Integer> bpop = new ArrayList<>();
        ArrayList<Integer> balb = new ArrayList<>();
        ArrayList<Integer> bcon = new ArrayList<>();
        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;
            String str = String.format("SELECT concert.expenses AS exp, stage.capacity AS cap, bookingoffer.expense AS bexpense, band.popularity, band.salesalbum, band.salesconcerts FROM concert INNER JOIN stage ON stage.idstage = concert.stageid INNER JOIN bookingoffer ON bookingoffer.concertid = concert.idconcert INNER JOIN band ON band.idBand = bookingoffer.bandid WHERE bookingoffer.accepted > 1 AND concert.idconcert = %d", concertId);
            rs = stm.executeQuery(str);
            while (rs.next()) {
                cexp = rs.getInt("exp");
                cap = rs.getInt("cap");
                bexp.add(rs.getInt("bexpense"));
                bpop.add(rs.getInt("popularity"));
                balb.add(rs.getInt("salesalbum"));
                bcon.add(rs.getInt("salesconcerts"));
            }
        } catch (Exception e) {
            System.err.println("Got an exception2! ");
            System.err.println(e.getMessage());
        }
        totalexpenses = cexp;
        for (int i = 0; i < bexp.size(); i++) {
            totalexpenses += bexp.get(i);
            expectedSales += Math.round(bpop.get(i) * expectedatt);
        }
        int attendance = expectedSales;
        if (attendance > cap) {
            attendance = cap;
            extraPrice = expectedSales / cap;
        }
        price = Math.round(totalexpenses / attendance);
        price = Math.round(price * extraPrice);
        return  price;
    }

    /**
     int getConcertId
     * @param: String concertName
     *
     * @return Returns the id of the specified concert.
     */
    public int getConcertId (String concertName) {
        int res = 0;
        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;

            String str = String.format("SELECT concert.idconcert FROM concert WHERE concert.name = '%s'", concertName);
            rs = stm.executeQuery(str);
            while (rs.next()) {
                res = rs.getInt("idConcert");
            }
        } catch (Exception e) {
            System.err.println("Got an exception123! ");
            System.err.println(e.getMessage());
        } return res;
    }

    /**
     void getConcert
     * @param: int concertId
     * @param: int price
     *
     * sets the price of a concert.
     */
    public void setPrice(int concertId, int price) {
        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;
            String str = String.format("UPDATE concert SET concert.price = %d WHERE concert.idconcert = %d", price, concertId);
            stm.executeUpdate(str);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     List<String> getOffers
     *
     * @return Returns a list of bookingoffers for the booking boss to accept.
     */
    public List<String> getOffers() {
        List<String> ls = new ArrayList<String>();
        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;
            String str = String.format("SELECT bookingoffer.idbookingoffer, band.name AS bname, concert.name AS cname, stage.name AS sname, concert.date, bookingoffer.time, bookingoffer.accepted FROM bookingoffer INNER JOIN band ON band.idBand = bookingoffer.bandid INNER JOIN concert on concert.idconcert = bookingoffer.concertid INNER JOIN stage on stage.idstage = concert.stageid WHERE bookingoffer.accepted = 0");
            rs = stm.executeQuery(str);
            while (rs.next()) {
                String strm = String.format("%s;%s;%s;%s;%s;%s;%s", rs.getString("idbookingoffer"), rs.getString("bname"),rs.getString("cname"), rs.getString("sname"), rs.getString("date"), rs.getString("time"), rs.getString("accepted"));
                ls.add(strm);
            }
        } catch (Exception e) {
            System.err.println("Got an exception2! ");
            System.err.println(e.getMessage());
        } return ls;
    }

    /**
     void updateOffer
     * @param: int offerId
     * @param: int state
     *
     * accepts or decline a bookingoffer.
     */
    public void updateOffer(int offerId, int state) {
        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;
            String str = String.format("UPDATE bookingoffer SET bookingoffer.accepted = %d WHERE bookingoffer.idbookingoffer = %d", state, offerId);
            stm.executeUpdate(str);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     List<String> getConcertDates
     * @param: String startDate
     * @param: String endDate
     *
     * @return Returns a list of dates and if the date is booked or not. The first element of the list is the amount of booked dates, sent but not booked and free days.
     */
    public String getConcertDates() {
        String strres= "%s%s%s";
        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;
            String str = String.format("SELECT (SELECT COUNT(DISTINCT concert.date) FROM bookingoffer INNER JOIN concert ON bookingoffer.concertid = concert.idconcert WHERE bookingoffer.accepted = 2) AS booked, (SELECT COUNT(DISTINCT bookingoffer.idbookingoffer) FROM bookingoffer INNER JOIN concert ON bookingoffer.concertid = concert.idconcert WHERE bookingoffer.accepted = 1) AS sent, (SELECT concert.date FROM bookingoffer INNER JOIN concert ON bookingoffer.concertid = concert.idconcert WHERE bookingoffer.accepted > 0 ORDER BY concert.date LIMIT 1) AS sdate, (SELECT concert.date FROM bookingoffer INNER JOIN concert ON bookingoffer.concertid = concert.idconcert WHERE bookingoffer.accepted > 0 ORDER BY concert.date DESC LIMIT 1) AS edate");
            rs = stm.executeQuery(str);
            while (rs.next()) {
                String[] spl = rs.getString("sdate").split("\\.");
                String[] epl = rs.getString("edate").split("\\.");
                LocalDate sd = LocalDate.of(Integer.parseInt(spl[2]), Integer.parseInt(spl[1]), Integer.parseInt(spl[0]));
                LocalDate ed = LocalDate.of(Integer.parseInt(epl[2]), Integer.parseInt(epl[1]), Integer.parseInt(epl[0]));
                int booked =  Integer.parseInt(rs.getString("booked"));
                int sent =  Integer.parseInt(rs.getString("sent"));
                strres = String.format("%d;%d;%d", booked, sent, ChronoUnit.DAYS.between(sd, ed) - booked);
            }
        } catch (Exception e) {
            System.err.println("Got an exception2! ");
            System.err.println(e.getMessage());
        } return strres;
    }
}
