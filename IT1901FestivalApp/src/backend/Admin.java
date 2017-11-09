package backend;
/**
 * Admin class
 * @author: Andreas Skifjeld
 */

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Admin {

  /**
   * Constructor that creates an object that is connected to the database
   */
  public Admin() {
    ConnectionManager.connect();
  }

  /**
   * Method updates user 'accepted' field in the database. -1 is declined, 0 is not handled and 1 is accepted
   * @param response
   * @param name
   */
  public void handleUser(int response, String name) {
    try {
      Statement stmt = ConnectionManager.conn.createStatement();
      ResultSet rs;
      String str = String.format("UPDATE person SET person.accepted = %d WHERE person.name= '%s'", response, name);
      stmt.executeUpdate(str);
    } catch (Exception e) {
      System.err.println("Got an exception! ");
      System.err.println(e.getMessage());
    }
  }

  /**
   * Method finds users that are not handled by the admin. It then iterates through the list and places the user and the users roles in a map
   * @return Map of unhandled users and their roles (Map<String, List<String>>)
   */
  public Map<String, List<String>> getUsers() {
    Map<String, List<String>> users = new HashMap<>();
    List<String> usersLi = new ArrayList<>();
    try {
      Statement stmt = ConnectionManager.conn.createStatement();
      ResultSet rs;
      String usersQuery = "SELECT person.name FROM person WHERE person.accepted = 0 ";
      rs = stmt.executeQuery(usersQuery);
      while (rs.next()) { usersLi.add(rs.getString("person.name"));}
      for (int i = 0; i < usersLi.size(); i++) {
        String user = usersLi.get(i);
        String roleQuery = String.format("SELECT role.name FROM role, person, roleperson WHERE person.name = '%s' AND person.idPerson = roleperson.personid AND roleperson.roleid = role.idrole", user);
        ResultSet rs2 = stmt.executeQuery(roleQuery);
        List<String> roles = new ArrayList<>();
        while (rs2.next()) {
          String role = rs2.getString("role.name");
          roles.add(role);
        }
        users.put(user, roles);
      }
    } catch (Exception e) {
      System.err.println("Got an exception! ");
      System.err.println(e.getMessage());
    } return users;
  }
}

