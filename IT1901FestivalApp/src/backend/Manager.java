package backend;

/**
 * One method for adding techneeds.
 */

import java.sql.*;
import java.util.ArrayList;

public class Manager {


    private int userId = 4;
    private int bandId;
    private ArrayList<Integer> bandList;
    private ArrayList<String> bandNames;

    /* MÅ:
    Manager
    * @param: int userId
    *
    * Initialize and sets the id of the manager.
    */
    public Manager(int userId) {

        this.userId = userId;

        bandList = new ArrayList<Integer>();
        bandNames = new ArrayList<String>();

        //hente ut bandList og bandNames
        try {
            ConnectionManager.connect();

            updateBandList();

            setBandId(0);

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    /* MÅ:
    void addTechNeeds
    * @param: String need
    *
    * Adds a new technical need to the current active band.
    */
    public void addTechNeed(String need){
        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;
            String str = String.format("Insert Into  techicalneed(bandid, need) Values ('%d', '%s')", bandId, need);
            stm.executeUpdate(str);

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /* MÅ:
    void getKeyInformation
    *
    * Gets the key information of the current band.
    */
    public String getKeyInformation(){
        String out = "";
        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;
            String str = String.format("SELECT * FROM band WHERE idBand = %d", bandId);
            rs = stm.executeQuery(str);
            while (rs.next()){
                String name = rs.getString("name");
                int popularity = rs.getInt("popularity");
                int album = rs.getInt("salesalbum");
                int concert = rs.getInt("salesconcerts");
                out = String.format("%s %d %d %d", name, popularity, album, concert);
            }
        }
        catch (Exception e){
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return out;
    }

    /* MÅ:
    void setKeyInformation
    * @param: String name
    * @param: int popularity
    * @param: int salesalbum
    * @param: int salesconcert
    *
    * Modifies the current band with the following key information. Returns true if set succesfully.
    */
    public boolean setKeyInformation(String name, int popularity, int salesalbum, int salesconcert){

        if (exists(name))
        {
            return false;
        }

        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;

            String str = String.format("UPDATE band SET name = '%s', popularity = %d, salesalbum = %d, salesconcerts = %d WHERE idBand = %d", name, popularity, salesalbum, salesconcert, bandId);
            stm.executeUpdate(str);

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return true;
    }

    /* MÅ:
    void addKeyInformation
    * @param: String name
    * @param: int popularity
    * @param: int salesalbum
    * @param: int salesconcert
    *
    * Adds a new band with the set key information. Returns true if added succesfully.
    */
    public boolean addKeyInformation(String name, int popularity, int salesalbum, int salesconcert){

        if (exists(name))
        {
            return false;
        }

        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;

            String str = String.format("INSERT INTO band(name, popularity, salesalbum, salesconcerts, managerid) VALUES ('%s', %d, %d, %d, %d)", name, popularity, salesalbum, salesconcert, userId);
            stm.executeUpdate(str);


            updateBandList();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return true;
    }

    /* MÅ:
    void getTechNeeds
    *
    * Gets the techical needs of the current active band.
    */
    public ArrayList<String> getTechNeeds(){
        ArrayList<String> needs = new ArrayList<>();
        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;

            String str = String.format("SELECT * FROM `techicalneed` WHERE bandid = %d", bandId);
            rs = stm.executeQuery(str);

            while (rs.next()){
                String need = rs.getString("need");
                needs.add(need);
            }
        }
        catch (Exception e){
            System.err.println("Got an exception2! ");
            System.err.println(e.getMessage());
        }
        return needs;
    }

    /* MÅ:
    void setBandId
    * @param: int index
    *
    * Sets the current active band to the one in the index of bandlist.
    */
    public void setBandId(int index){
        this.bandId = bandList.get(index);
 
    }

    /* MÅ:
    ArrayList<Integer> getBands
    *
    * Gets a list of the bandids of this manager.
    */
    public ArrayList<Integer> getBands(){
        return bandList;
    }

    /* MÅ:
    ArrayList<String> getBandNames
    *
    * Gets a list of the band names of this manager.
    */
    public ArrayList<String> getBandNames(){
        return bandNames;
    }

    /* MÅ:
    void updateBandList
    *
    * Updates the bandlist with current data.
    */
    public void updateBandList(){
        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;

            bandList.clear();
            bandNames.clear();

            String str = String.format("SELECT * FROM band WHERE band.managerid = %d", userId);
            rs = stm.executeQuery(str);
            while (rs.next())
            {
                bandList.add(rs.getInt("idBand"));
                bandNames.add(rs.getString("name"));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean exists(String bandName){
        boolean res = false;

        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;

            String str = ("SELECT * FROM band");
            rs = stm.executeQuery(str);
            while (rs.next())
            {
                if (bandName == rs.getString("name"))
                {
                    res = true;
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return res;
    }

    public  static  void main(String [] args){
        Manager mg = new Manager(4);

        String ss = "kuler";

        mg.addTechNeed(ss);

        mg.addKeyInformation("Best Band 2", 23, 93, 21);
        mg.setKeyInformation("Good Band 3", 43, 87, 12);

        System.out.println("Done thing!");

        System.out.println("KEYINFO: " + mg.getKeyInformation());

        for (int i = 0; i < mg.bandNames.size(); i++)
        {
            System.out.println(mg.bandNames.get(i));
        }

        ArrayList<String> sl = mg.getTechNeeds();

        for (int i = 0; i < sl.size(); i++)
        {
            System.out.println(sl.get(i));
        }
    }

}
