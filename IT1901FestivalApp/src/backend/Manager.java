package backend;

/**
 * One method for adding techneeds.
 */

import java.sql.*;
import java.util.ArrayList;

public class Manager {


    private int userId = 1;
    private int bandId = 1;


    public Manager(int userId, int bandId) {

        this.userId = userId;
        this.bandId = bandId;
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


    public  static  void main(String [] args){
        Manager mg = new Manager(1, 1);

        String ss = "banansplitt";

        mg.setTechNeeds(ss);
        System.out.println("Done thing!");
    }

}
