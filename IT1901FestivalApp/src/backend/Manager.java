package backend;

import java.sql.*;
import java.util.*;

public class Manager {
    private int userId;
    private Map<String, Integer> bands;

    /**
     Manager
     * @param: int userId
     *
     * Initializes the manager with the specified user id.
     */
    public Manager(int userId) {
        this.userId = userId;
        bands = new HashMap<>();
        //hente ut bandList og bandNames
        try {
            ConnectionManager.connect();
            setBands();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
    void addTechNeeds
    * @param: String need
    *
    * Adds a new technical need to the current active band.
    */
    public void addTechNeed(String need, String bandName) {
        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;
            String str = String.format("Insert Into  techicalneed(bandid, need) Values ('%d', '%s')", bands.get(bandName), need);
            stm.executeUpdate(str);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void removeTechNeed(String need, String bandname) {
        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;
            String str = String.format("DELETE FROM techicalneed WHERE techicalneed.need = '%s' AND technicalneed.bandid = %d", need, bands.get(bandname));
            stm.executeUpdate(str);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    /**
    void getTechNeeds
    *
    * Gets the techical needs of the current active band.
    */
    public List<String> getTechNeeds(String bandName) {
        List<String> needs = new ArrayList<>();
        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;

            String str = String.format("SELECT * FROM `techicalneed` WHERE bandid = %d", bands.get(bandName));
            rs = stm.executeQuery(str);

            while (rs.next()) {
                String need = rs.getString("need");
                needs.add(need);
            }
        } catch (Exception e) {
            System.err.println("Got an exception2! ");
            System.err.println(e.getMessage());
        }
        return needs;
    }


    /**
     ArrayList<String> getBandNames
     *
     * Gets a list of the band names of this manager.
     */
    public List<String> getBandNames() {
        List<String> bandNames = new ArrayList<>(bands.keySet());
        Collections.sort(bandNames);
        return bandNames;
    }

    /**
     ArrayList<String> getBandNames
     *
     * Gets a list of the band names of this manager.
     */
    public List<String> getOffers() {
        List<String> ls = new ArrayList<String>();

        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;

            String str = String.format("SELECT bookingoffer.idbookingoffer, band.name AS bname, concert.name AS cname, stage.name AS sname, bookingoffer.date, bookingoffer.time, bookingoffer.accepted FROM bookingoffer INNER JOIN band ON band.idBand = bookingoffer.bandid INNER JOIN concert on concert.idconcert = bookingoffer.concertid INNER JOIN stage on stage.idstage = concert.stageid WHERE band.managerid = %d",
                    userId);
            rs = stm.executeQuery(str);

            while (rs.next()) {
                String strm = String.format("%d|%s|%s|%s|%s|%s|%d", rs.getString("idbookingoffer"), rs.getString("bname"),rs.getString("cname"), rs.getString("sname"), rs.getString("date"), rs.getString("time"), rs.getString("accepted"));
                ls.add(strm);
            }
        } catch (Exception e) {
            System.err.println("Got an exception2! ");
            System.err.println(e.getMessage());
        }

        return ls;
    }

    /**
    void updateBandList
    *
    * Updates the bandlist with current data.
    */
    public void setBands() {
        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;
            bands.clear();
            String str = String.format("SELECT * FROM band WHERE band.managerid = %d", userId);
            rs = stm.executeQuery(str);
            while (rs.next()) {
                bands.put(rs.getString("name"), rs.getInt("idBand"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public  static  void main(String[] args)
    {
        Manager mg = new Manager(1);
    }

}
