package backend;


import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Login {

    private String personId;
    private String username;
    private String pwd;
    private ArrayList<String> roleId = new ArrayList<>();

    public Login() {
        ConnectionManager.connect();
    }

    public String getUsername() { return username; }

    public String getPassword() { return pwd; }

    public String getPersonId() {
        return personId;
    }

    public ArrayList<String> getRoleId() {
        return roleId;
    }

    public boolean checkLogin(String name, String password) {

        boolean check1 = false;
        try {
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;
            boolean check2 = false;

            rs = stmt.executeQuery("SELECT person.idPerson, person.name, person.password, role.idrole FROM person, role, roleperson WHERE person.idPerson = roleperson.personid AND roleperson.roleid = role.idrole");

            while (rs.next()) {

                String dbName = rs.getString("person.name");
                String dbPassword = rs.getString("person.password");

                if (dbName.equals(name) && dbPassword.equals(password)) {

                    username = dbName;
                    pwd = dbPassword;
                    personId = rs.getString("person.idPerson");
                    roleId.add(rs.getString("role.idrole"));
                    check2 = true;
                }
            }
            return check2;
        } catch (Exception e) {
            System.err.println("Got an exception1! ");
            System.err.println(e.getMessage());
        }
        return check1;
    }

    public boolean register(String name, String password, String mail, String phone,  ArrayList<String> idRole) {

        try{
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs1;
            ResultSet rs2;

            rs1 = stmt.executeQuery("SELECT person.name FROM person");

            while(rs1.next()) {
                String dbName = rs1.getString("person.name");

                if (name.equals(dbName)) {
                    return false;
                }
            }

            stmt.executeUpdate(String.format("INSERT INTO person (name, password, email, phone) VALUES ('%s', '%s', '%s', '%s')", name, password, mail, phone));

            rs2 = stmt.executeQuery("SELECT person.idPerson FROM person WHERE person.idPerson = (select max(person.idPerson) from person)");

            if (rs2.next()) {
                personId = rs2.getString("person.idPerson");
            }

            Iterator<String> it = idRole.iterator();

            while (it.hasNext()) {

                String x = it.next();
                roleId.add(x);
                stmt.executeUpdate("INSERT INTO roleperson (personid, roleid) VALUES ('" + personId + "', '" + x + "')");

            }
            username = name;
            pwd = password;

        } catch (Exception e) {
            System.err.println("Got an exception2!");
            System.err.println(e.getMessage());
        }
        return true;
    }
}
