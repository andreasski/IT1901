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

            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;

            rs = stm.executeQuery("SELECT * FROM band WHERE band.managerid = " + userId);
            while (rs.next())
            {
                bandList.add(rs.getInt("idBand"));
                bandNames.add(rs.getString("name"));
            }

            setBandId(0);

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }
    /* MÅ:
    void setTechNeeds
    * @param: String need
    *
    * Sets a new technical need to the current active band.
    */
    public void setTechNeeds(String need){
        try {
            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;

            stm.executeUpdate("Insert Into  techicalneed(bandid, need) Values ('" + bandId + "', '" + need + "')");

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
    @param: String what to change, String change to
     */

    public void setKeyInformation(){

    }

    public void editKeyInformation(){

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

    public  static  void main(String [] args){
        Manager mg = new Manager(4);

        String ss = "kuler";

        mg.setTechNeeds(ss);
        System.out.println("Done thing!");


        for (int i = 0; i < mg.bandNames.size(); i++)
        {
            System.out.println(mg.bandNames.get(i));
        }
    }

}
