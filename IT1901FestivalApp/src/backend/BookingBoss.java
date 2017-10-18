package backend;


import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BookingBoss {

    BookingBoss() {
        ConnectionManager.connect();
    }

    public List<String> getConcerts() {
        List<String> ls = new ArrayList<String>();

        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;

            String str = String.format("SELECT DISTINCT concert.idconcert, concert.name AS cname, bookingoffer.date FROM bookingoffer INNER JOIN concert ON concert.idconcert = bookingoffer.concertid WHERE bookingoffer.accepted > 1 ORDER BY bookingoffer.date");
            rs = stm.executeQuery(str);

            while (rs.next()) {
                String date = rs.getString("date");
                String[] datearr = date.split("\\.");
                LocalDateTime lt = LocalDateTime.now();
                int yr = Integer.parseInt(datearr[0]);
                int mn = Integer.parseInt(datearr[1]);
                int dy = Integer.parseInt(datearr[2]);
                //System.out.print(yr + " " + mn + " " + dy);
                if (yr < lt.getYear() || mn < lt.getMonthValue() || dy < lt.getDayOfMonth()) {
                    System.out.print(" Added! \n");
                    String strm = String.format("%s;%s;%s", rs.getString("idconcert"), rs.getString("cname"), date);
                    ls.add(strm);
                }
                else {
                    //System.out.print(" not Added! \n");
                }
            }
        } catch (Exception e) {
            System.err.println("Got an exception2! ");
            System.err.println(e.getMessage());
        }

        return ls;
    }

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
        }
        return  res;
    }

    public int generateTicketPrice(int concertId) {
        int price = 0;

        int expectedSales = 0;
        double expectedatt = 0.1;
        int totalexpenses = 0;
        int extraPrice = 1;

        int cexp = 0;
        int cap = 0;
        ArrayList<Integer> bexp = new ArrayList<Integer>();
        ArrayList<Integer> bpop = new ArrayList<Integer>();
        ArrayList<Integer> balb = new ArrayList<Integer>();
        ArrayList<Integer> bcon = new ArrayList<Integer>();

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

    public static void main(String[] args)
    {
        LocalDateTime lt = LocalDateTime.now();

        String sr = String.format("%s.%s.%s", lt.getYear(), lt.getMonthValue(), lt.getDayOfMonth());

        //System.out.println(lt.toString());
        //System.out.println(sr);

        BookingBoss bb = new BookingBoss();

        List<String> ls = bb.getConcerts();
        Iterator<String> its = ls.iterator();

        while (its.hasNext())
        {
            System.out.println(its.next());
        }

        System.out.println(bb.getConcert(4));
        System.out.println("ticket price 4: " + bb.generateTicketPrice(4));
        System.out.println("ticket price 5: " + bb.generateTicketPrice(5));
        System.out.println("ticket price 6: " + bb.generateTicketPrice(6));
        System.out.println("ticket price 2: " + bb.generateTicketPrice(2));
    }
}
