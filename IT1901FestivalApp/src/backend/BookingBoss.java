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
                System.out.print(yr + " " + mn + " " + dy);
                if (yr < lt.getYear() || mn < lt.getMonthValue() || dy < lt.getDayOfMonth()) {
                    System.out.print(" Added! \n");
                    String strm = String.format("%s;%s;%s", rs.getString("idconcert"), rs.getString("cname"), date);
                    ls.add(strm);
                }
                else {
                    System.out.print(" not Added! \n");
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

                System.out.print(" Added! \n");
                String strm = String.format("%s;%s;%s;%s;%s", rs.getString("name"), rs.getString("sales"), rs.getString("price"), rs.getString("totalexpenses"), rs.getString("earnings"));
                res = strm;

            }
        } catch (Exception e) {
            System.err.println("Got an exception2! ");
            System.err.println(e.getMessage());
        }
        return  res;
    }

    public static void main(String[] args)
    {
        LocalDateTime lt = LocalDateTime.now();

        String sr = String.format("%s.%s.%s", lt.getYear(), lt.getMonthValue(), lt.getDayOfMonth());

        System.out.println(lt.toString());
        System.out.println(sr);

        BookingBoss bb = new BookingBoss();

        List<String> ls = bb.getConcerts();
        Iterator<String> its = ls.iterator();

        while (its.hasNext())
        {
            System.out.println(its.next());
        }

        System.out.println(bb.getConcert(4));
    }
}
