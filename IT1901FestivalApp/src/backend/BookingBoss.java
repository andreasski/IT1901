package backend;


import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

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

    public List<String> getOffers() {
        List<String> ls = new ArrayList<String>();

        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;

            String str = String.format("SELECT bookingoffer.idbookingoffer, band.name AS bname, concert.name AS cname, stage.name AS sname, bookingoffer.date, bookingoffer.time, bookingoffer.accepted FROM bookingoffer INNER JOIN band ON band.idBand = bookingoffer.bandid INNER JOIN concert on concert.idconcert = bookingoffer.concertid INNER JOIN stage on stage.idstage = concert.stageid WHERE bookingoffer.accepted = 0");
            rs = stm.executeQuery(str);

            while (rs.next()) {
                String strm = String.format("%s;%s;%s;%s;%s;%s;%s", rs.getString("idbookingoffer"), rs.getString("bname"),rs.getString("cname"), rs.getString("sname"), rs.getString("date"), rs.getString("time"), rs.getString("accepted"));
                ls.add(strm);
            }
        } catch (Exception e) {
            System.err.println("Got an exception2! ");
            System.err.println(e.getMessage());
        }

        return ls;
    }

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

    public List<String> getConcertDates(String startDate, String endDate) {
        List<String> ls = new ArrayList<String>();

        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;

            String str = String.format("SELECT bookingoffer.idbookingoffer, bookingoffer.date, bookingoffer.accepted FROM bookingoffer WHERE bookingoffer.accepted > -1 ORDER BY bookingoffer.date");
            rs = stm.executeQuery(str);

            while (rs.next()) {
                String strm = String.format("%s;%s;%s", rs.getString("idbookingoffer"), rs.getString("date"), rs.getString("accepted"));
                ls.add(strm);
            }
        } catch (Exception e) {
            System.err.println("Got an exception2! ");
            System.err.println(e.getMessage());
        }

        List<String> lst = new ArrayList<String>();

        String[] spls = startDate.split("\\.");
        String[] sple = endDate.split("\\.");

        int ys = Integer.parseInt(spls[0]);
        int ms = Integer.parseInt(spls[1]);
        int ds = Integer.parseInt(spls[2]);

        int ye = Integer.parseInt(sple[0]);
        int me = Integer.parseInt(sple[1]);
        int de = Integer.parseInt(sple[2]);

        LocalDate sdate = LocalDate.of(ys, ms, ds);
        LocalDate edate = LocalDate.of(ye, me, de);

        int booked = 0;
        int sent = 0;
        long free = ChronoUnit.DAYS.between(sdate, edate);
        System.out.println(sdate.toString() + " - " + edate.toString() + " - " + free);
        lst.add("%d;%d;%d");

        Iterator<String> its = ls.iterator();
        HashMap<String, Integer> bookedm = new HashMap<String, Integer>();
        HashMap<String, Integer> sentm = new HashMap<String, Integer>();
        while (its.hasNext())
        {
            String[] bsp = its.next().split(";");
            String[] dsp = bsp[1].split("\\.");
            LocalDate bdat = LocalDate.of(Integer.parseInt(dsp[0]), Integer.parseInt(dsp[1]), Integer.parseInt(dsp[2]));

            int acc = Integer.parseInt(bsp[2]);
            if (acc == 2)
            {
                if (bookedm.containsKey(bdat.toString()))
                {
                    bookedm.put(bdat.toString(), bookedm.get(bdat.toString()) + 1);
                }
                else
                {
                    bookedm.put(bdat.toString(), 1);
                }
            }
            else
            {
                if (sentm.containsKey(bdat.toString()))
                {
                    sentm.put(bdat.toString(), sentm.get(bdat.toString()) + 1);
                }
                else
                {
                    sentm.put(bdat.toString(), 1);
                }
            }
        }

        LocalDate cdate = LocalDate.of(ys, ms, ds);
        for (int i = 0; i < free +1; i++)
        {
            int boo = 0;
            int sen = 0;

            if (bookedm.containsKey(cdate.toString()))
            {
                boo = bookedm.get(cdate.toString());
            }
            if (sentm.containsKey(cdate.toString()))
            {
                sen = sentm.get(cdate.toString());
            }


            booked += boo;
            sent += sen;
            String str = String.format("%s;%d;%d", cdate.toString(), boo, sen);
            lst.add(str);
            cdate = cdate.plusDays(1);
        }

        lst.set(0, String.format(lst.get(0), booked, sent, free));


        return lst;
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

        System.out.println();


        bb.updateOffer(2, 1);
        its = bb.getOffers().iterator();
        while (its.hasNext())
        {
            System.out.println(its.next());
        }

        its = bb.getConcertDates("2017.10.05", "2017.10.30").iterator();
        while (its.hasNext())
        {
            System.out.println(its.next());
        }
    }
}
