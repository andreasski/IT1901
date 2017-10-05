package backend;

import java.sql.*;
import java.util.*;

public class Manager {
    private int userId;
    private Map<String, Integer> bands;

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

    /** MÅ:
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

    /** MÅ:
    void getKeyInformation
    *
    * Gets the key information of the current band.
    */
    public String getKeyInformation(String bandName) {
        String out = "";
        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;
            String str = String.format("SELECT * FROM band WHERE idBand = %d", bands.get(bandName));
            rs = stm.executeQuery(str);
            while (rs.next()) {
                String name = rs.getString("name");
                int popularity = rs.getInt("popularity");
                int album = rs.getInt("salesalbum");
                int concert = rs.getInt("salesconcerts");
                out = String.format("%s %d %d %d", name, popularity, album, concert);
            }
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return out;
    }

    /** MÅ:
    void setKeyInformation
    * @param: String name
    * @param: int popularity
    * @param: int salesalbum
    * @param: int salesconcert
    *
    * Modifies the current band with the following key information. Returns true if set succesfully.
    */
    /*
    public boolean setKeyInformation(String name, int popularity, int salesalbum, int salesconcert) {

        if (!bandNames.get(bandList.indexOf(bandId)).equals(name)) {
            if (exists(name)) {
                return false;
            }
        }

        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;

            String str = String.format("UPDATE band SET name = '%s', popularity = %d, salesalbum = %d, salesconcerts = %d WHERE idBand = %d", name, popularity, salesalbum, salesconcert, bandId);
            stm.executeUpdate(str);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return true;
    }*/

    /** MÅ:
    void addKeyInformation
    * @param: String name
    * @param: int popularity
    * @param: int salesalbum
    * @param: int salesconcert
    *
    * Adds a new band with the set key information. Returns true if added succesfully.
    */
    /*
    public boolean addKeyInformation(String name, int popularity, int salesalbum, int salesconcert) {

        if (exists(name)) {
            return false;
        }

        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;

            String str = String.format("INSERT INTO band(name, popularity, salesalbum, salesconcerts, managerid) VALUES ('%s', %d, %d, %d, %d)", name, popularity, salesalbum, salesconcert, userId);
            stm.executeUpdate(str);


            updateBandList();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return true;
    }*/

    /** MÅ:
    void getTechNeeds
    *
    * Gets the techical needs of the current active band.
    */
    public ArrayList<String> getTechNeeds(String bandName) {
        ArrayList<String> needs = new ArrayList<>();
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


    /** MÅ:
    ArrayList<String> getBandNames
    *
    * Gets a list of the band names of this manager.
    */
    public List<String> getBandNames() {
        List<String> bandNames = new ArrayList<>(bands.keySet());
        Collections.sort(bandNames);
        return bandNames;
    }

    /** MÅ:
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
    /* Only necessary if we want the Manager to be able to add bands
    public boolean exists(String bandName) {
        boolean res = false;

        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;

            String str = ("SELECT * FROM band");
            rs = stm.executeQuery(str);
            while (rs.next()) {
                if (bandName.equals(rs.getString("name"))) {
                    res = true;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return res;
    }*/
}