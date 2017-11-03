package backend;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Admin {

  public Admin() {
    ConnectionManager.connect();
  }


  public void handleUser(int response, String name) {
    try {
      Statement stmt = ConnectionManager.conn.createStatement();
      ResultSet rs;
      String str = String.format("UPDATE person SET person.accepted = %d WHERE person.name= '%s'", response, name);
      stmt.executeUpdate(str);
    } catch (Exception e) {
      System.err.println("Got an exception100! ");
      System.err.println(e.getMessage());
    }
  }


  public Map<String, List<String>> getUsers() {
    Map<String, List<String>> users = new HashMap<>();
    List<String> usersLi = new ArrayList<>();
    try {
      Statement stmt = ConnectionManager.conn.createStatement();
      ResultSet rs;

      String usersQuery = "SELECT person.name FROM person WHERE person.accepted = 0 ";
      rs = stmt.executeQuery(usersQuery);

      while (rs.next()) {
        usersLi.add(rs.getString("person.name"));
      }

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
      System.err.println("Got an exception100! ");
      System.err.println(e.getMessage());
    }
    return users;
  }

  public static void main(String[] args) {
    Admin admin = new Admin();
    admin.getUsers();
  }

}

