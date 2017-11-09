package backend;


import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


// Creates the backend class that is called in frontend, and that contains all the functions and variables needed
public class Login {


    // Initializes the variables person-ID, username, and password as Strings, and ID of roles as ArrayList of Strings to be sent to frontend if called for
    private String personId;
    private String username;
    private String pwd;
    private ArrayList<String> roleId = new ArrayList<>();


    // Constructor function that immediately calls for ConnectionManager-class to make a connection
    public Login() {
        ConnectionManager.connect();
    }


    // Get functions that allows access to the variables if needed
    public String getUsername() { return username; }

    public String getPassword() { return pwd; }

    public String getPersonId() {
        return personId;
    }

    public ArrayList<String> getRoleId() {
        return roleId;
    }


    // Function that returns a list of names of roles connected to the ID of the person in the database
    public List<String> getRoles() {
        // Creates a List of Strings
        List<String> roles = new ArrayList<>();

        try {
            // Connects with the ConnectionManager so data can be acquired from the database
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;

            // Uses SQL-code to ask the database for the name of all the roles connected to the ID of the person given from the private variable
            rs = stmt.executeQuery(String.format("SELECT role.name FROM role, roleperson WHERE %d = roleperson.personid AND roleperson.roleid = role.idrole", Integer.parseInt(personId)));

            // While the ResultSet rs contains another answer, add the name of the role to the list created earlier
            while (rs.next()) { roles.add(rs.getString("role.name"));}

        } catch (Exception e) {
            System.err.println("Got an exception100! ");
            System.err.println(e.getMessage());
        }


        return roles;
    }


    // Function that takes in a username and a password, and returns a true if they fit with the database, and a false if they do not
    public boolean checkLogin(String name, String password) {

        try {
            // Connects with the database
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs;

            // As a variable cannot be both inside and outside of a try, another boolean is created that starts being false until "proven otherwise"
            boolean check = false;

            // Asks the database for a list of all the peoples ID's, names, passwords and their role-ID's
            rs = stmt.executeQuery("SELECT person.idPerson, person.name, person.password, role.idrole FROM person, role, roleperson WHERE person.idPerson = roleperson.personid AND roleperson.roleid = role.idrole");

            // Loops as long as the ResultSet rs contains another result
            while (rs.next()) {

                // Acquires the name and the password of the next person of the list and assigns it to two variables
                String dbName = rs.getString("person.name");
                String dbPassword = rs.getString("person.password");

                // If both the name and the password given the function matches the name and the password in the list, it writes the name, password, and ID to the private variables.
                // As a person can have several roles, it does not end the function, but writes the role-ID to a list and continues to check if the name and password aligns again with
                // another role. The check is set to true.
                if (dbName.equals(name) && dbPassword.equals(password)) {

                    username = dbName;
                    pwd = dbPassword;
                    personId = rs.getString("person.idPerson");
                    roleId.add(rs.getString("role.idrole"));
                    check = true;
                }
            }
            // If a match was found it will return true, but if not check never changes and returns false
            return check;
        } catch (Exception e) {
            System.err.println("Got an exception1! ");
            System.err.println(e.getMessage());
        }
        // If something went wrong (and because the function demands it) it will return a false after the try if it has not ended yet
        return false;
    }


    // Function that takes in a name, password, mail, phone#, and a list of role-ID's and tries to register it to the database. If the name matches a name in the database
    // it returns a false, if it does not it returns a true
    public boolean register(String name, String password, String mail, String phone,  ArrayList<String> idRole) {

        try{
            // Connects to the database with ConnectionManager
            Statement stmt = ConnectionManager.conn.createStatement();
            ResultSet rs1;
            ResultSet rs2;

            // Asks the database for all the names of people in the database in SQL
            rs1 = stmt.executeQuery("SELECT person.name FROM person");

            // Loops while there still is another name in the ResultSet rs1
            while(rs1.next()) {

                // Assigns the name to a variable
                String dbName = rs1.getString("person.name");

                // If the name in the database matches the name given, returns false
                if (name.equals(dbName)) {
                    return false;
                }
            }

            // Inserts the values name, password, email and phone to the database
            stmt.executeUpdate(String.format("INSERT INTO person (name, password, email, phone) VALUES ('%s', '%s', '%s', '%s')", name, password, mail, phone));

            // Asks the database for the ID that was created for the last person that was added to the database
            rs2 = stmt.executeQuery("SELECT person.idPerson FROM person WHERE person.idPerson = (select max(person.idPerson) from person)");

            // If ResultSet rs2 contains an answer, assigns the ID of the person the the private variable
            if (rs2.next()) {
                personId = rs2.getString("person.idPerson");
            }

            // Makes the list of role-ID's given into an iterator
            Iterator<String> it = idRole.iterator();

            // While the iterator contains another ID, assigns it to the private variable list of ID's, and adds it to the person in the database
            while (it.hasNext()) {

                String x = it.next();
                roleId.add(x);
                stmt.executeUpdate("INSERT INTO roleperson (personid, roleid) VALUES ('" + personId + "', '" + x + "')");

            }

            // Adds the username and password to the private variables
            username = name;
            pwd = password;

        } catch (Exception e) {
            System.err.println("Got an exception2!");
            System.err.println(e.getMessage());
        }

        // If it didnt return false, or any exceptions was found, returns true
        return true;
    }
}
