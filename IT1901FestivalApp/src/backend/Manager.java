package backend;

/**
 * One method for adding techneeds.
 */

import java.sql.*;
import java.util.ArrayList;

public class Manager {


    private int userId = 1;
    private int bandId;
    private ArrayList<Integer> bandList;
    private ArrayList<String> bandNames;


    public Manager(int userId) {

        this.userId = userId;
        //hente ut bandList og bandNames
        try {
            ConnectionManager.connect();

            Statement stm = ConnectionManager.conn.createStatement();
            ResultSet rs;

            //rs = stm.executeQuery("");

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }
    /* MÅ:
    void setTechNeeds
    * @param: String need
    *
    * Send bandid(fra manager) and string needs.
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

    public void setBandId(int index){
        this.bandId = bandList.get(index);
    }

    public ArrayList<Integer> getBands(){
        return bandList;
    }

    public ArrayList<String> getBandNames(){
        return bandNames;
    }

    public  static  void main(String [] args){
        Manager mg = new Manager(1);

        String ss = "banansplitt";

        mg.setTechNeeds(ss);
        System.out.println("Done thing!");
    }

}
