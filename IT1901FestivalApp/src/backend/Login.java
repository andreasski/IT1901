package backend;


import jdk.nashorn.internal.ir.Statement;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Login {

    private String personId;
    private ArrayList<String> roleId = new ArrayList<>();

    public Login() {
        ConnectionManager.connect();
    }

    public String getPersonId() {
        return personId;
    }

    public ArrayList<String> getRoleId() {
        return roleId;
    }

    public void checkLogin(String name, String password) {

        try {
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT person.idPerson, personSELECT person.idPerson, person.name, person.password, role.idrole FROM person, role, roleperson WHERE person.idPerson = roleperson.personid AND roleperson.roleid = role.idrole");

            while (rs.next()) {

                String dbName = rs.getString("person.name");
                String dbPassword = rs.getString("person.password");

                if (dbName.equals(name) && dbPassword.equals(password)) {
                    personId = rs.getString("person.idPerson");
                    roleId.add(rs.getString("role.idrole"));
                }
            }
        } catch (Exception e) {
            System.err.println("Got an exception1! ");
            System.err.println(e.getMessage());
        }
    }

    public void register(String name, String password, ArrayList<String> idRole) {

        try{
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs1, rs2;

            rs1 = stmt.executeQuery("SELECT person.name FROM person");

            while(rs1.next()) {
                String dbName = rs1.getString("person.name");

                if (name.equals(dbName)) {
                    throw new Exception("Name is taken!");
                }
            }

            rs2 = stmt.executeQuery("Insert Into person (name, password) VALUES ('" + name + "', '" + password + "'); SELECT person.idPerson FROM person WHERE person.idPerson = (select max(person.idPerson) from person);");

            personId = rs2.getString("person.idPerson");

            Iterator<String> it = idRole.iterator();

            while (it.hasNext()) {

                roleId.add(it.next());
                stmt.executeQuery("INSERT INTO roleperson (personid, roleid) VALUES ('" + personId + "', '" + it.next() + "')");

            }

        } catch (Exception e) {
            System.err.println("Got an exception2!");
            System.err.println(e.getMessage());
        }
    }
}
